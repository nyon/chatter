package service;

import de.nyon.chatter.client.client.ChatterClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientServerIntegrationTest {
    @LocalServerPort
    private int port;

    @Test
    void messageShouldBeSent() throws IOException, DeploymentException, ExecutionException, InterruptedException {
        CompletableFuture<String> receivedMessage = new CompletableFuture<>();

        ChatterClient testClient = new ChatterClient.Builder()
                .onTextMessage(receivedMessage::complete)
                .build();
        testClient.connect("ws://localhost:" + port + "/chat");
        testClient.sendMessage("Hello");

        String actualMessage = receivedMessage.get();
        assertTrue(actualMessage.contains("\"text\":\"Hello\""), "received message should be a json object of sent one");
    }

}
