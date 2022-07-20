package service.command;

import org.springframework.stereotype.Component;

/**
 * A factory component used to create Command instances using the {@link CommandParser}
 */
@Component
public class CommandFactory {
    private final CommandParser commandParser;

    public CommandFactory(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    public Command create(String message) {
        // Is this message not a real "command" message
        if (!message.startsWith("/")) {
            // then create a simple public message
            return createPublicMessage(message);
        }

        // This command starts with a slash meaning it must be some kind of command
        // Try to parse it, if parsing fails (e.g. no parser found) just send a public
        // message
        return commandParser
                .tryParse(message)
                .orElseGet(() -> createPublicMessage(message));
    }

    private Command createPublicMessage(String text) {
        // Create a simple text command, which will be received
        // by all chat users
        return new Command(
                CommandType.TEXT,
                text,
                "all"
        );
    }
}
