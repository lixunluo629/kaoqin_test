package org.springframework.remoting;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/RemoteTimeoutException.class */
public class RemoteTimeoutException extends RemoteAccessException {
    public RemoteTimeoutException(String msg) {
        super(msg);
    }

    public RemoteTimeoutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
