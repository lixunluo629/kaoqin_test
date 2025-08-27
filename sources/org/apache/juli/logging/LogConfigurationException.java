package org.apache.juli.logging;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/juli/logging/LogConfigurationException.class */
public class LogConfigurationException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public LogConfigurationException() {
    }

    public LogConfigurationException(String message) {
        super(message);
    }

    public LogConfigurationException(Throwable cause) {
        super(cause);
    }

    public LogConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
