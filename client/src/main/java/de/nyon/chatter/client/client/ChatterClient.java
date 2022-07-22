package de.nyon.chatter.client.client;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.function.Consumer;

@ClientEndpoint
public class ChatterClient {
    private final Consumer<String> onTextMessage;
    private Session session;
    private boolean isConnected;

    private ChatterClient(Consumer<String> onTextMessage) {
        this.onTextMessage = onTextMessage;
    }

    public void connect(String endpoint) throws IOException, DeploymentException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, URI.create(endpoint));
        isConnected = true;
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connection established.");
        this.session = session;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param session the session which is getting closed.
     * @param reason  the reason for connection close
     */
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Closing connection.");
        this.session = null;
        isConnected = false;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        onTextMessage.accept(message);
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public static class Builder {
        private Consumer<String> onTextMessage;

        public Builder() {

        }

        public Builder onTextMessage(Consumer<String> consumer) {
            this.onTextMessage = consumer;
            return this;
        }

        public ChatterClient build() {
            return new ChatterClient(
                    onTextMessage
            );
        }
    }
}
