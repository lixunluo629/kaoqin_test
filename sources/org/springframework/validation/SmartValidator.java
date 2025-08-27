package org.springframework.validation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/SmartValidator.class */
public interface SmartValidator extends Validator {
    void validate(Object obj, Errors errors, Object... objArr);
}
