package service.command.parsers;

import org.springframework.stereotype.Component;
import service.command.Command;
import service.command.CommandType;

import java.util.Arrays;

@Component
public class DirectMessageParser implements IParser {
    @Override
    public String getCommandPrefix() {
        return "/msg";
    }

    @Override
    public Command parse(String[] tokens) {
        String[] textTokens = Arrays.copyOfRange(tokens, 2, tokens.length);
        String recipientName = tokens[1];
        return new Command(
                CommandType.MSG,
                String.join(" ", textTokens),
                recipientName
        );
    }
}
