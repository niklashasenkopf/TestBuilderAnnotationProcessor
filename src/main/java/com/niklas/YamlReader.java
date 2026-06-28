package com.niklas;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;


import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;

public class YamlReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    public Map<String, Object> getDefaults(Path yaml) {

        try (InputStream in = Files.newInputStream(yaml)) {
            return OBJECT_MAPPER.readValue(in, new TypeReference<>() {});
        } catch (IOException e) {
            System.err.printf("Error reading yaml file: %s%n", e.getMessage());
        }

        return null;
    }
}
