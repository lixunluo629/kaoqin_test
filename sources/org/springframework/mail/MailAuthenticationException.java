package org.springframework.mail;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/mail/MailAuthenticationException.class */
public class MailAuthenticationException extends MailException {
    public MailAuthenticationException(String msg) {
        super(msg);
    }

    public MailAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MailAuthenticationException(Throwable cause) {
        super("Authentication failed", cause);
    }
}
