package org.springframework.oxm;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/MarshallingFailureException.class */
public class MarshallingFailureException extends MarshallingException {
    public MarshallingFailureException(String msg) {
        super(msg);
    }

    public MarshallingFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
