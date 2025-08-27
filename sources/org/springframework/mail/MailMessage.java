package org.springframework.mail;

import java.util.Date;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/mail/MailMessage.class */
public interface MailMessage {
    void setFrom(String str) throws MailParseException;

    void setReplyTo(String str) throws MailParseException;

    void setTo(String str) throws MailParseException;

    void setTo(String[] strArr) throws MailParseException;

    void setCc(String str) throws MailParseException;

    void setCc(String[] strArr) throws MailParseException;

    void setBcc(String str) throws MailParseException;

    void setBcc(String[] strArr) throws MailParseException;

    void setSentDate(Date date) throws MailParseException;

    void setSubject(String str) throws MailParseException;

    void setText(String str) throws MailParseException;
}
