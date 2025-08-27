package org.springframework.mail;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/mail/MailSender.class */
public interface MailSender {
    void send(SimpleMailMessage simpleMailMessage) throws MailException;

    void send(SimpleMailMessage... simpleMailMessageArr) throws MailException;
}
