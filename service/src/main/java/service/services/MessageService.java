package service.services;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import service.message.Message;

import javax.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MessageService {
    private final Queue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    private Flux<Object> messageQueueFlux;

    @PostConstruct
    public void initializeMessageQueueFlux() {
        messageQueueFlux = Flux.create(sink -> {
            while (!sink.isCancelled()) {
                Message newestMessage = messageQueue.poll();

                if (newestMessage == null) {
                    continue;
                }

                sink.next(newestMessage);
            }
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
