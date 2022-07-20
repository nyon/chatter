package service.processors.input;

import reactor.core.publisher.Flux;
import service.command.Command;
import service.session.Session;

/**
 * Interface to define an input processor for incoming commands from a client
 */
public interface IInputProcessor {
    Flux<Command> decorate(Flux<Command> commandFlux, Session session);
}
