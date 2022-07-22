package de.nyon.chatter.client;

import de.nyon.chatter.client.client.ChatterClient;

import javax.websocket.DeploymentException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatterClientApplication {
    public static void main(String[] args) throws IOException, DeploymentException {
        String endpoint = "ws://localhost:8080/chat";
        if (args.length > 0) {
            endpoint = args[0];
        }

        ChatterClient client = new ChatterClient.Builder()
                .onTextMessage(System.out::println)
                .build();

        client.connect(endpoint);

        BufferedInputStream is = new BufferedInputStream(System.in);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        while (client.isConnected()) {
            String line = bufferedReader.readLine();
            client.sendMessage(line);
        }
    }
}
