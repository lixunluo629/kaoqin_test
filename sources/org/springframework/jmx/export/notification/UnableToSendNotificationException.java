package org.springframework.jmx.export.notification;

import org.springframework.jmx.JmxException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/notification/UnableToSendNotificationException.class */
public class UnableToSendNotificationException extends JmxException {
    public UnableToSendNotificationException(String msg) {
        super(msg);
    }

    public UnableToSendNotificationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
