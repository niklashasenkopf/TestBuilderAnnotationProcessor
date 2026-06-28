package com.niklas;

import com.palantir.javapoet.TypeName;

public class ObjectFormatter {

    // TODO: Handle different types like lists, enums, nested objects
    public static Object formatValue(TypeName typeName, Object value) {
        if (value == null) {
            if (typeName.equals(TypeName.DOUBLE)) return 0.0;
            if (typeName.equals(TypeName.INT))    return 0;
            if (typeName.equals(TypeName.LONG))   return 0L;
            if (typeName.equals(TypeName.FLOAT))  return 0.0f;
            if (typeName.equals(TypeName.BOOLEAN)) return false;
            if (typeName.isPrimitive()) return 0; // catch-all for short/byte/char
            return null; // reference types: null is a legal initializer
        }

        if (typeName.equals(TypeName.get(String.class))) {
            return "\"" + value + "\"";
        }

        return value;
    }
}
