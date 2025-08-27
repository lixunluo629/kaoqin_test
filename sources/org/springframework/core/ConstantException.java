package org.springframework.core;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ConstantException.class */
public class ConstantException extends IllegalArgumentException {
    public ConstantException(String className, String field, String message) {
        super("Field '" + field + "' " + message + " in class [" + className + "]");
    }

    public ConstantException(String className, String namePrefix, Object value) {
        super("No '" + namePrefix + "' field with value '" + value + "' found in class [" + className + "]");
    }
}
