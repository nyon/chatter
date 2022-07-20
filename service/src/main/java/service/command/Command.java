package service.command;

/**
 * A POJO representing incoming commands from a client.
 * <p>
 * For example:
 * /me
 * /msg Private_Recipient Secret message
 * Simple message
 * <p>
 * Those incoming messages are getting parsed by the {@link CommandParser} and
 * {@link CommandFactory}
 */
public class Command {
    /**
     * Command type of the incoming command.
     * <p>
     * For example: CommandType.ME for /me
     */
    private CommandType commandType;

    /**
     * Corresponding text of the command. Contains the whole message if a
     * simple message is received from the client. Contains the private message of a
     * /msg command
     */
    private String text;

    /**
     * Recipient of the command or message. The special value "all" is used for a public
     * message to all connected clients.
     */
    private String recipientName;

    /**
     * Sender of the command or message.
     */
    private String senderName;

    public Command(CommandType commandType, String text, String recipientName) {
        this.commandType = commandType;
        this.text = text;
        this.recipientName = recipientName;
        this.senderName = "Server";
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "Command{" +
                "commandType=" + commandType +
                ", text='" + text + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", senderName='" + senderName + '\'' +
                '}';
    }
}
