package service.processors.output;

import reactor.core.publisher.Flux;
import service.message.Message;
import service.session.Session;

/**
 * Output processors are applied for all clients on all messages coming from the service
 * and going to a client. Output processor usage should be minimized, because they are applied
 * for every client. Try to implement your wanted behavior using a {@link service.processors.input.IInputProcessor}
 * first. If this is not possible, use an output processor.
 */
public interface IOutputProcessor {
    Flux<Message> decorate(Flux<Message> messageFlux, Session session);
}
