package com.niklas.model;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;

public class TestBuilderModelFactory {

    public static TestBuilderModel from(
            TypeElement typeElement,
            ProcessingEnvironment processingEnvironment
    ) {
        String className = typeElement.getSimpleName().toString();
        String packageName = processingEnvironment.getElementUtils().getPackageOf(typeElement).toString();
        List<FieldMetadata> fields = extractFields(typeElement);
        ClassName testBuilderClassName = ClassName.get("", className + "TestBuilder");
        ClassName entityClass = ClassName.get(packageName, className);

        return new TestBuilderModel(className, packageName, testBuilderClassName, entityClass, fields);
    }

    private static List<FieldMetadata> extractFields(TypeElement typeElement) {
        return typeElement.getEnclosedElements().stream()
                          .filter(e -> e.getKind() == ElementKind.FIELD)
                          .map(e -> (VariableElement) e)
                          .filter(field -> hasAnySetter(typeElement, field))
                          .map(field -> new FieldMetadata(
                                  field.getSimpleName().toString(),
                                  TypeName.get(field.asType())))
                          .toList();
    }

    private static boolean hasAnySetter(TypeElement typeElement, VariableElement element) {
        return classHasLombokSetter(typeElement) || hasExplicitSetter(typeElement, element) || fieldHasLombokSetter(element);
    }

    private static String setterName(VariableElement field) {
        String name = field.getSimpleName().toString();
        return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private static boolean classHasLombokSetter(TypeElement type) {
        return type.getAnnotationMirrors().stream()
                      .map(a -> a.getAnnotationType().toString())
                      .anyMatch(name -> name.equals("lombok.Setter") || name.equals("lombok.Data"));
    }

    private static boolean fieldHasLombokSetter(VariableElement field) {
        return field.getAnnotationMirrors().stream()
                .map(a -> a.getAnnotationType().toString())
                .anyMatch(name -> name.equals("lombok.Setter") || name.equals("lombok.Data"));
    }

    private static boolean hasExplicitSetter(TypeElement type, VariableElement field) {
        String expectedName = setterName(field);

        return type.getEnclosedElements().stream()
                   .filter(e -> e.getKind() == ElementKind.METHOD)
                   .map(e -> (ExecutableElement) e)
                   .anyMatch(method ->
                           method.getSimpleName().contentEquals(expectedName)
                                   && method.getParameters().size() == 1
                                   && method.getParameters().getFirst().asType()
                                            .equals(field.asType()));
    }
}
