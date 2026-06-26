package com.niklas.generators.methods;

import java.util.List;

import com.niklas.model.TestBuilderModel;
import com.palantir.javapoet.MethodSpec;

public interface MethodGenerator {
    List<MethodSpec> generate(TestBuilderModel model);
}
