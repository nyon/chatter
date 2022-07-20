package service.message;

import org.springframework.stereotype.Component;
import service.command.Command;

@Component
public class MessageFactory {
    /**
     * Converts a command to a message object
     *
     * @param command which needs to be converted to a message
     * @return converted message
     */
    public Message create(Command command) {
        return new Message(
                command.getSenderName(),
                command.getText(),
                command.getRecipientName()
        );
    }
}
