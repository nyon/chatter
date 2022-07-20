package service.processors.output;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import service.message.Message;
import service.session.Session;

/**
 * Output processor for filtering foreign messages. This processor is necessary for
 * private messaging. Without this processor, private message would be visible to all
 * connected clients.
 */
@Component
public class DiscardForeignMessages implements IOutputProcessor {
    @Override
    public Flux<Message> decorate(Flux<Message> messageFlux, Session session) {
        return messageFlux.filter((message) -> {
            boolean isPublicMessage = "all".equals(message.getRecipientName());
            boolean isMyMessage = session.getName().equals(message.getRecipientName());

            return isPublicMessage || isMyMessage;
        });
    }
}
