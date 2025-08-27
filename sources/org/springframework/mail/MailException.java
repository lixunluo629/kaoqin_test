package org.springframework.mail;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/mail/MailException.class */
public abstract class MailException extends NestedRuntimeException {
    public MailException(String msg) {
        super(msg);
    }

    public MailException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
