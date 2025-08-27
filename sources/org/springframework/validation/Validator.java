package org.springframework.validation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/Validator.class */
public interface Validator {
    boolean supports(Class<?> cls);

    void validate(Object obj, Errors errors);
}
