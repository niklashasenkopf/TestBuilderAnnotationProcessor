package com.niklas.generators.methods;

import java.util.List;

import javax.lang.model.element.Modifier;

import com.niklas.model.FieldMetadata;
import com.niklas.model.TestBuilderModel;
import com.palantir.javapoet.MethodSpec;
import org.apache.commons.lang3.StringUtils;

public class BuildMethodGenerator implements MethodGenerator{
    @Override
    public List<MethodSpec> generate(TestBuilderModel model) {
        String variableName = model.className().toLowerCase();

        MethodSpec.Builder builder = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(model.entityClass())
                .addStatement("$T $N = new $T()", model.entityClass(), variableName, model.entityClass());

        for(FieldMetadata field : model.fields()) {
            builder.addStatement("$N.set$N($N)", variableName, StringUtils.capitalize(field.name()), field.name());
        }

        builder.addStatement("return $N", variableName);

        return List.of(builder.build());
    }
}
