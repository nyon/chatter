package service.command;

public enum CommandType {
    /**
     * Command type that represents a simple message
     * <p>
     * Example: Simple Message
     */
    TEXT,
    /**
     * Command type that represents a private message
     * <p>
     * Example: /msg Secret_Sheep My secret message
     */
    MSG,
    /**
     * Command type telling the server to send the client's name
     * <p>
     * Example: /me
     */
    ME
}
