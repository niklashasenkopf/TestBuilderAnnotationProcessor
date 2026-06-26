package com.niklas;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.google.auto.service.AutoService;
import com.niklas.generators.fields.FieldSpecGenerator;
import com.niklas.generators.methods.BuildMethodGenerator;
import com.niklas.generators.methods.DefaultMethodGenerator;
import com.niklas.generators.methods.WithMethodGenerator;
import com.niklas.model.TestBuilderModel;
import com.niklas.model.TestBuilderModelFactory;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeSpec;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.niklas.TestBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class TestBuilderProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(
                annotation -> {
                    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
                    elements.stream()
                            .filter(TypeElement.class::isInstance)
                            .map(TypeElement.class::cast)
                            .forEach(element -> {
                                try {
                                    createTestBuilderClass(element, processingEnv);
                                } catch (
                                        IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                }
        );

        return true;
    }

    public static void createTestBuilderClass(
            TypeElement typeElement,
            ProcessingEnvironment processingEnv) throws IOException {

        TestBuilderModel model = TestBuilderModelFactory.from(typeElement, processingEnv);

        String resourceDirectory = processingEnv
                .getOptions()
                .getOrDefault("testbuilder.resources", "src/main/resources/TestBuilders");
        Path yamlFile = Paths.get(resourceDirectory, model.className() + ".yaml");

        YamlReader yamlReader = new YamlReader(processingEnv, model.className());
        Map<String, Object> defaults = yamlReader.getDefaults(yamlFile);

        TestBuilderClassAssembler assembler = new TestBuilderClassAssembler(
                new FieldSpecGenerator(),
                List.of(
                        new DefaultMethodGenerator(),
                        new BuildMethodGenerator(),
                        new WithMethodGenerator()
                )
        );

        TypeSpec typeSpec = assembler.assemble(model, defaults);

        JavaFile javaFile = JavaFile.builder("com.testBuilder." + model.className(), typeSpec)
                                    .build();

        javaFile.writeTo(processingEnv.getFiler());
    }
}
