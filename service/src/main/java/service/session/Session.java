package service.session;

public class Session {
    private final String name;

    Session(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
