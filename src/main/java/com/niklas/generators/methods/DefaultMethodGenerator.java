package com.niklas.generators.methods;

import java.util.List;

import javax.lang.model.element.Modifier;

import com.niklas.model.TestBuilderModel;
import com.palantir.javapoet.MethodSpec;

public class DefaultMethodGenerator implements MethodGenerator{
    @Override
    public List<MethodSpec> generate(TestBuilderModel model) {
        return List.of(MethodSpec.methodBuilder("defaults")
                         .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                         .returns(model.testBuilderClassName())
                         .addStatement("return new $T()", model.testBuilderClassName())
                         .build());
    }
}
