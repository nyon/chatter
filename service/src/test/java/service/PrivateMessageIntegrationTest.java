package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.nyon.chatter.client.client.ChatterClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PrivateMessageIntegrationTest {
    private final static String MY_SECRET_MESSAGE = "MY SECRET";
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @LocalServerPort
    private int port;

    @Test
    void privateMessageShouldBeReceivedByOtherClient() throws IOException, DeploymentException, InterruptedException {
        ArrayBlockingQueue<String> receivedMessagesByClientA = new ArrayBlockingQueue<>(10);
        ArrayBlockingQueue<String> receivedMessagesByClientB = new ArrayBlockingQueue<>(10);

        ChatterClient clientA = new ChatterClient.Builder()
                .onTextMessage(receivedMessagesByClientA::add)
                .build();
        clientA.connect("ws://localhost:" + port + "/chat");


        ChatterClient clientB = new ChatterClient.Builder()
                .onTextMessage(receivedMessagesByClientB::add)
                .build();
        clientB.connect("ws://localhost:" + port + "/chat");


        clientA.sendMessage("/me ");
        String yourNameIsText = extractText(receivedMessagesByClientA.poll(10, TimeUnit.SECONDS));
        String clientAName = yourNameIsText.replace("Your name is ", "");


        clientB.sendMessage("/msg " + clientAName + " " + MY_SECRET_MESSAGE);
        String secretMessageFromClientB = receivedMessagesByClientA.poll(10, TimeUnit.SECONDS);
        String secretText = extractText(secretMessageFromClientB);
        String secretRecipient = extractRecipient(secretMessageFromClientB);

        assertEquals(clientAName, secretRecipient, "recipient of message mismatches");
        assertEquals(MY_SECRET_MESSAGE, secretText, "secret message is not as expected");

    }

    @Test
    void privateMessageShouldNotBeReceivedByUnknownClient() throws IOException, DeploymentException, ExecutionException, InterruptedException {
        ArrayBlockingQueue<String> receivedMessagesByClientA = new ArrayBlockingQueue<>(10);
        ArrayBlockingQueue<String> receivedMessagesByClientB = new ArrayBlockingQueue<>(10);

        ChatterClient clientA = new ChatterClient.Builder()
                .onTextMessage(receivedMessagesByClientA::add)
                .build();
        clientA.connect("ws://localhost:" + port + "/chat");


        ChatterClient clientB = new ChatterClient.Builder()
                .onTextMessage(receivedMessagesByClientB::add)
                .build();
        clientB.connect("ws://localhost:" + port + "/chat");


        clientA.sendMessage("/me ");
        String yourNameIsText = extractText(receivedMessagesByClientA.poll(10, TimeUnit.SECONDS));
        String clientAName = yourNameIsText.replace("Your name is ", "");


        clientB.sendMessage("/msg NotConnectedClient123 " + MY_SECRET_MESSAGE);
        clientB.sendMessage("Normal message");
        String secretMessageFromClientB = receivedMessagesByClientA.poll(10, TimeUnit.SECONDS);
        String secretText = extractText(secretMessageFromClientB);

        assertNotEquals(MY_SECRET_MESSAGE, secretText, "secret message should not leak to other client");

    }

    private String extractRecipient(String jsonObjectAsString) throws JsonProcessingException {
        HashMap<String, String> parsedJson = OBJECT_MAPPER.readValue(
                jsonObjectAsString,
                new TypeReference<HashMap<String, String>>() {
                }
        );
        return parsedJson.get("recipientName");
    }

    private String extractText(String jsonObjectAsString) throws JsonProcessingException {
        HashMap<String, String> parsedJson = OBJECT_MAPPER.readValue(
                jsonObjectAsString,
                new TypeReference<HashMap<String, String>>() {
                }
        );
        return parsedJson.get("text");

    }

}
