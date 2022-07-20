package service.command.parsers;

import service.command.Command;

public interface IParser {

    String getCommandPrefix();

    Command parse(String[] tokens);
}
