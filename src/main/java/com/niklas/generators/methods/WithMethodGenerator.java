package com.niklas.generators.methods;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import com.niklas.model.FieldMetadata;
import com.niklas.model.TestBuilderModel;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import org.apache.commons.lang3.StringUtils;

public class WithMethodGenerator implements MethodGenerator {

    @Override
    public List<MethodSpec> generate(TestBuilderModel model) {
        List<MethodSpec> withMethodSpecs = new ArrayList<>();
        model.fields().forEach(
                fieldMetadata -> withMethodSpecs.add(
                        generateOne(fieldMetadata, model)));
        return withMethodSpecs;
    }

    public MethodSpec generateOne(FieldMetadata field, TestBuilderModel model) {
        String methodName = "with" + StringUtils.capitalize(field.name());
        ParameterSpec parameterSpec = ParameterSpec.builder(field.typeName(), field.name()).build();
        return MethodSpec
                .methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(model.testBuilderClassName())
                .addParameter(parameterSpec)
                .addStatement("this.$N = $N", field.name(), field.name())
                .addStatement("return this")
                .build();
    }
}
