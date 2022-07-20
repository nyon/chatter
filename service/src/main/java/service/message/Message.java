package service.message;

/**
 * A POJO representing a message in our application. After a {@link service.command.Command}
 * is received and fully processed by all {@link service.processors.input.IInputProcessor}s
 * it is converted to an instance of this class.
 */
public class Message {
    /**
     * sender of the message.
     */
    private final String senderName;

    /**
     * text of the message
     */
    private final String text;

    /**
     * recipient of the message
     */
    private final String recipientName;

    Message(String senderName, String text, String recipientName) {
        this.senderName = senderName;
        this.text = text;
        this.recipientName = recipientName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getText() {
        return text;
    }

    public String getRecipientName() {
        return recipientName;
    }
}
