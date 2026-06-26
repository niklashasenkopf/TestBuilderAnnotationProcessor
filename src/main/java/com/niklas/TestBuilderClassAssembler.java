package com.niklas;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

import com.niklas.generators.fields.FieldSpecGenerator;
import com.niklas.generators.methods.MethodGenerator;
import com.niklas.model.TestBuilderModel;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;

public class TestBuilderClassAssembler {

    private final FieldSpecGenerator fieldSpecGenerator;
    private final List<MethodGenerator> methodGenerators;

    public TestBuilderClassAssembler(FieldSpecGenerator fieldSpecGenerator, List<MethodGenerator> methodGenerators) {
        this.fieldSpecGenerator = fieldSpecGenerator;
        this.methodGenerators = methodGenerators;
    }

    public TypeSpec assemble(TestBuilderModel model, Map<String, Object> defaults) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(model.testBuilderClassName().simpleName())
                                           .addModifiers(Modifier.PUBLIC);

        fieldSpecGenerator.generate(model, defaults).forEach(builder::addField);

        for (MethodGenerator generator : methodGenerators) {
            List<MethodSpec> methods = generator.generate(model);
            methods.forEach(builder::addMethod);
        }

        return builder.build();
    }
}
