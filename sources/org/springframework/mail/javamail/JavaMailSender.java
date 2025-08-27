package org.springframework.mail.javamail;

import java.io.InputStream;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/mail/javamail/JavaMailSender.class */
public interface JavaMailSender extends MailSender {
    MimeMessage createMimeMessage();

    MimeMessage createMimeMessage(InputStream inputStream) throws MailException;

    void send(MimeMessage mimeMessage) throws MailException;

    void send(MimeMessage... mimeMessageArr) throws MailException;

    void send(MimeMessagePreparator mimeMessagePreparator) throws MailException;

    void send(MimeMessagePreparator... mimeMessagePreparatorArr) throws MailException;
}
