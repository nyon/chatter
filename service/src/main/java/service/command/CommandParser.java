package service.command;

import org.springframework.stereotype.Component;
import service.command.parsers.IParser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Component to parse an incoming message. Is only used internally for the {@link CommandFactory}.
 * <p>
 * This parser component aggregates all custom made parsers that implement the {@link IParser} interface.
 */
@Component
public class CommandParser {
    private final Map<String, IParser> parsers;

    public CommandParser(List<IParser> parsers) {
        // prepare a map of parsers to improve performance while parsing commands
        // command name -> IParser instance
        // e.g. /me -> ShowNameParser instance
        this.parsers = parsers.stream()
                .collect(Collectors.toMap(
                        IParser::getCommandPrefix,
                        Function.identity()
                ));
    }

    /**
     * Tries to parse the given message.
     *
     * @param message The message sent by the client
     * @return if parsing succeeds, a parsed command instance will be returned.
     * if not, an empty optional will be returned
     */
    Optional<Command> tryParse(String message) {
        String[] tokens = message.split(" ");
        String commandName = tokens[0];

        // find correct parser for command prefix in all available parsers
        Optional<IParser> foundParser = Optional.ofNullable(parsers.get(commandName));

        // apply parser if one is found, else return empty optional
        return foundParser.map(parser -> parser.parse(tokens));
    }
}
