package service.processors.input;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import service.command.Command;
import service.command.CommandType;
import service.session.Session;

/**
 * Input processor which only reacts to a special command type: ME (/me)
 * Creates a text message for the sending user containing the client's user name.
 */
@Component
public class TransformMeProcessor implements IInputProcessor {
    @Override
    public Flux<Command> decorate(Flux<Command> commandFlux, Session session) {
        return commandFlux.map(command -> {
            // Only change command when command type is /me
            if (command.getCommandType() != CommandType.ME) {
                return command;
            }

            command.setText("Your name is " + session.getName());
            command.setRecipientName(session.getName());

            return command;
        });
    }
}
