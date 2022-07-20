package service.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.command.Command;
import service.command.CommandFactory;
import service.helper.JsonWriter;
import service.message.Message;
import service.message.MessageFactory;
import service.processors.input.IInputProcessor;
import service.processors.output.IOutputProcessor;
import service.services.MessageService;
import service.session.Session;
import service.session.SessionFactory;

import java.util.List;

/**
 * This WebSocketHandler connects all "chatter" components. See the README.MD for an abstract
 * overview of the whole process. It binds every client that connects to the service to a special
 * {@link Session}. Upon connection every client gets a randomized name. This name can be used to
 * talk to another client privately. If a client just sends a simple message, this message is sent
 * to all connected clients.
 */
@Component
public class ChatHandler implements WebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatHandler.class);
    private final MessageService messageService;
    private final CommandFactory commandFactory;
    private final MessageFactory messageFactory;
    private final List<IInputProcessor> inputProcessors;
    private final List<IOutputProcessor> outputProcessors;
    private final JsonWriter jsonWriter;
    private final SessionFactory sessionFactory;

    public ChatHandler(MessageService messageService,
                       CommandFactory commandFactory,
                       MessageFactory messageFactory,
                       List<IInputProcessor> inputProcessors,
                       List<IOutputProcessor> outputProcessors,
                       JsonWriter jsonWriter,
                       SessionFactory sessionFactory) {
        this.messageService = messageService;
        this.commandFactory = commandFactory;
        this.messageFactory = messageFactory;
        this.inputProcessors = inputProcessors;
        this.outputProcessors = outputProcessors;
        this.jsonWriter = jsonWriter;
        this.sessionFactory = sessionFactory;

        // For debugging purposes all input and output processors are logged on startup
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("" + inputProcessors.size() + " input processors found: ");
            for (IInputProcessor processor : inputProcessors) {
                LOGGER.info("\t" + processor.getClass().getName());
            }

            LOGGER.info("" + outputProcessors.size() + " output processors found: ");
            for (IOutputProcessor processor : outputProcessors) {
                LOGGER.info("\t" + processor.getClass().getName());
            }
        }
    }

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        // create a new session for every web socket session
        Session session = sessionFactory.createNewSession();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(session.getName() + " connected.");
        }

        // setup send and receive channels and subscribe to it
        sendChannel(webSocketSession, session).subscribe();
        receiveChannel(webSocketSession, session).subscribe();
        webSocketSession.closeStatus().doOnNext((status) -> logCloseStatus(status, session));

        // keep this web socket session alive "forever"
        return Mono.never();
    }

    private void logCloseStatus(CloseStatus status, Session session) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(session.getName() + " disconnected. Reason: " + status.getReason() + " (" + status.getCode() + ")");
        }
    }

    private Flux<Message> receiveChannel(WebSocketSession webSocketSession, Session session) {
        // Transform received raw web socket command to internal Command object
        Flux<Command> inputFlux = webSocketSession.receive()
                .doOnNext((message) -> logIncomingMessage(message, session))
                .map(WebSocketMessage::getPayloadAsText)
                .map(commandFactory::create)
                .onErrorStop();


        // Apply all available input processors
        for (IInputProcessor processor : inputProcessors) {
            inputFlux = processor.decorate(inputFlux, session);
        }

        // Transform to immutable Message instance and publish it to all other users
        return inputFlux
                .map(messageFactory::create)
                .doOnNext(messageService::send);
    }

    private void logIncomingMessage(WebSocketMessage webSocketMessage, Session session) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Incoming message from " + session.getName() + ": " + webSocketMessage.getPayloadAsText());
        }
    }

    private Mono<Void> sendChannel(WebSocketSession webSocketSession, Session session) {
        // Setup command publisher flux. Source of all messages that get sent to the user is
        // our command pool from our command service
        Flux<Message> outputFlux = messageService.receive();

        // apply output processing, e.g. filtering out private messages
        for (IOutputProcessor processor : outputProcessors) {
            outputFlux = processor.decorate(outputFlux, session);
        }

        // convert command to json and wrap it in a websocket compatible object
        Flux<WebSocketMessage> messagePublisher = outputFlux.map(jsonWriter::toJson)
                .map(webSocketSession::textMessage)
                .onErrorStop();

        // create flux for sending messages to the user
        return webSocketSession.send(messagePublisher);
    }
}
