package org.springframework.mail;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/mail/MailParseException.class */
public class MailParseException extends MailException {
    public MailParseException(String msg) {
        super(msg);
    }

    public MailParseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MailParseException(Throwable cause) {
        super("Could not parse mail", cause);
    }
}
