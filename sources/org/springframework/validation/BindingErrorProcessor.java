package org.springframework.validation;

import org.springframework.beans.PropertyAccessException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/BindingErrorProcessor.class */
public interface BindingErrorProcessor {
    void processMissingFieldError(String str, BindingResult bindingResult);

    void processPropertyAccessException(PropertyAccessException propertyAccessException, BindingResult bindingResult);
}
