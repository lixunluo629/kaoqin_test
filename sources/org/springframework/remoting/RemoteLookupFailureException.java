package org.springframework.remoting;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/RemoteLookupFailureException.class */
public class RemoteLookupFailureException extends RemoteAccessException {
    public RemoteLookupFailureException(String msg) {
        super(msg);
    }

    public RemoteLookupFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
