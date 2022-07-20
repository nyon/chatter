package service.session;

import org.springframework.stereotype.Component;
import service.helper.NameGenerator;

/**
 * Simple component to create new session for a new client connection
 */
@Component
public class SessionFactory {
    private final NameGenerator nameGenerator;

    public SessionFactory(NameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    public Session createNewSession() {
        String randomName = nameGenerator.generateName();
        return new Session(randomName);
    }
}
