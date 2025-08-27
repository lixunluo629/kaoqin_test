package org.springframework.oxm;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/ValidationFailureException.class */
public class ValidationFailureException extends XmlMappingException {
    public ValidationFailureException(String msg) {
        super(msg);
    }

    public ValidationFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
