package service.helper;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonWriterTest {
    private final JsonWriter jsonWriter = new JsonWriter();

    @Test
    void shouldWriteProperJson() {
        // given
        Map<String, String> testJson = new HashMap<>();
        testJson.put("foo", "bar");

        // when
        String stringifiedMap = jsonWriter.toJson(testJson);

        // then
        assertEquals("{\"foo\":\"bar\"}", stringifiedMap, "map was not correctly stringified");
    }

    @Test
    void shouldFallbackOnUnserializableObjects() {
        // ObjectMapper fails on empty objects,
        // so an empty object is a valid test case here

        // given
        Object unserializableObject = new Object();

        // when
        String fallbackJson = jsonWriter.toJson(unserializableObject);

        // then
        assertEquals("{}", fallbackJson, "unserializable object should fall back to empty json");
    }
}