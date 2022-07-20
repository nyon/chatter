package service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import service.handler.ChatHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler configuration for the Chat service application
 */
@Configuration
public class HandlerMappings {
    private final ChatHandler chatHandler;

    public HandlerMappings(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @Bean
    public HandlerMapping chatHandlerMapping() {
        Map<String, WebSocketHandler> mapping = new HashMap<>();
        mapping.put("/chat", chatHandler);
        return new SimpleUrlHandlerMapping(mapping, 1);
    }
}
