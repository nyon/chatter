package service.processors.input;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import service.command.Command;
import service.command.CommandType;
import service.session.Session;

/**
 * Input processor to extract the session user name and inject it into the incoming command
 */
@Component
public class AddSenderNameProcessor implements IInputProcessor {
    @Override
    public Flux<Command> decorate(Flux<Command> commandFlux, Session session) {
        return commandFlux.map(command -> {
            if (command.getCommandType() != CommandType.TEXT && command.getCommandType() != CommandType.MSG) {
                return command;
            }

            command.setSenderName(session.getName());
            return command;
        });
    }
}
