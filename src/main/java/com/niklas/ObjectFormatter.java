package com.niklas;

import com.palantir.javapoet.TypeName;

public class ObjectFormatter {

    // TODO: Handle different types like lists, enums, nested objects
    public static Object formatValue(TypeName typeName, Object value) {
        if (typeName.equals(TypeName.get(String.class))) {
            return "\"" + value + "\"";
        }

        return value;
    }
}
