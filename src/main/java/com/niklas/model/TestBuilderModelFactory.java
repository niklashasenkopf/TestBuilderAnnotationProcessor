package com.niklas.model;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
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
                          .filter(element -> element.getKind() == ElementKind.FIELD)
                          .map(element -> (VariableElement) element)
                          .map(field -> new FieldMetadata(
                                  field.getSimpleName().toString(),
                                  TypeName.get(field.asType())))
                          .toList();
    }
}
