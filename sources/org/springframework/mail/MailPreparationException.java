package org.springframework.mail;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/mail/MailPreparationException.class */
public class MailPreparationException extends MailException {
    public MailPreparationException(String msg) {
        super(msg);
    }

    public MailPreparationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MailPreparationException(Throwable cause) {
        super("Could not prepare mail", cause);
    }
}
