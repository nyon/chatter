package service.helper;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NameGeneratorTest {
    private final NameGenerator nameGenerator = new NameGenerator();

    @Test
    void shouldGenerateNonEmptyName() {
        // when
        String name = nameGenerator.generateName();

        // then
        assertTrue(StringUtils.isNotBlank(name), "name should not be empty");
    }
}