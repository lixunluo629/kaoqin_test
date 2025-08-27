package org.springframework.oxm;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/MarshallingException.class */
public abstract class MarshallingException extends XmlMappingException {
    protected MarshallingException(String msg) {
        super(msg);
    }

    protected MarshallingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
