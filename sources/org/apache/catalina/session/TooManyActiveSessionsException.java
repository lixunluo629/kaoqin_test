package org.apache.catalina.session;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/session/TooManyActiveSessionsException.class */
public class TooManyActiveSessionsException extends IllegalStateException {
    private static final long serialVersionUID = 1;
    private final int maxActiveSessions;

    public TooManyActiveSessionsException(String message, int maxActive) {
        super(message);
        this.maxActiveSessions = maxActive;
    }

    public int getMaxActiveSessions() {
        return this.maxActiveSessions;
    }
}
