package service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import service.message.Message;

import javax.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private final Queue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    private Flux<Object> messageQueueFlux;

    @PostConstruct
    public void initializeMessageQueueFlux() {
        messageQueueFlux = Flux.create(sink -> {
            LOGGER.info("Sink started");
            while (!sink.isCancelled()) {
                Message newestMessage = messageQueue.poll();

                if (newestMessage == null) {
                    continue;
                }

                LOGGER.info("Emit " + newestMessage);
                sink.next(newestMessage);
            }
            LOGGER.info("Sink finished");
        })
                .metrics()
                .subscribeOn(Schedulers.newSingle("messageQueue"))
                .share()
        ;
    }

    public Flux<Message> receive() {
        return messageQueueFlux.map(object -> (Message) object);
    }

    public void send(Message message) {
        messageQueue.add(message);
    }
}
