package com.niklas.model;

import java.util.List;

import com.palantir.javapoet.ClassName;

public record TestBuilderModel (
        String className,
        String packageName,
        ClassName testBuilderClassName,
        ClassName entityClass,
        List<FieldMetadata> fields
) {}
