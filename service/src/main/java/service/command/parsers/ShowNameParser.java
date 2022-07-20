package service.command.parsers;

import org.springframework.stereotype.Component;
import service.command.Command;
import service.command.CommandType;

@Component
public class ShowNameParser implements IParser {
    @Override
    public String getCommandPrefix() {
        return "/me";
    }

    @Override
    public Command parse(String[] tokens) {
        return new Command(
                CommandType.ME,
                "",
                ""
        );
    }
}
