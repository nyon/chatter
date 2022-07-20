package service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Simple helper class to stringify a java object
 */
@Component
public class JsonWriter {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWriter.class);

    /**
     * Stringifies a java object instance. writes an error if the stringification fails
     *
     * @param instance for stringification
     * @return json string of the object instance
     */
    public String toJson(Object instance) {
        try {
            return OBJECT_MAPPER.writeValueAsString(instance);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while outputting json", e);
            return "{}";
        }
    }

}
