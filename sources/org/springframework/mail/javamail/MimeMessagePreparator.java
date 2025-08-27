package org.springframework.mail.javamail;

import javax.mail.internet.MimeMessage;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/mail/javamail/MimeMessagePreparator.class */
public interface MimeMessagePreparator {
    void prepare(MimeMessage mimeMessage) throws Exception;
}
