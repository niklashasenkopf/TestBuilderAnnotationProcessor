package com.niklas.generators.fields;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

import com.niklas.model.FieldMetadata;
import com.niklas.model.TestBuilderModel;
import com.palantir.javapoet.FieldSpec;

import static com.niklas.ObjectFormatter.formatValue;

public class FieldSpecGenerator {

    public List<FieldSpec> generate(TestBuilderModel model, Map<String, Object> defaults) {
        return model.fields().stream()
                    .map(field -> toFieldSpec(field, defaults))
                    .toList();
    }

    private FieldSpec toFieldSpec(FieldMetadata field, Map<String, Object> defaults) {
        return FieldSpec.builder(field.typeName(), field.name())
                        .addModifiers(Modifier.PRIVATE)
                        .initializer("$L", formatValue(field.typeName(), defaults.get(field.name())))
                        .build();
    }
}
