package com.niklas;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.StandardLocation;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;

public class YamlReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());
    private ProcessingEnvironment processingEnvironment;
    private String entityName;

    public YamlReader(
            ProcessingEnvironment processingEnvironment,
            String entityName
    ) {
        this.processingEnvironment = processingEnvironment;
        this.entityName = entityName;
    }

    public Map<String, Object> getDefaults(Path yaml) throws IOException {

        try (InputStream in = processingEnvironment.getFiler()
                .getResource(StandardLocation.CLASS_PATH, "", "TestBuilders/" + entityName + ".yaml")
                                                   .openInputStream()) {
            return OBJECT_MAPPER.readValue(in, new TypeReference<>() {});
        } catch (IOException e) {
            System.err.printf("Error reading yaml file: %s%n", e.getMessage());
        }

        return null;
    }
}
