package org.springframework.boot.autoconfigure.mail;

import javax.mail.Session;
import javax.naming.NamingException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@ConditionalOnJndi
@Configuration
@ConditionalOnClass({Session.class})
@ConditionalOnProperty(prefix = "spring.mail", name = {"jndi-name"})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mail/MailSenderJndiConfiguration.class */
class MailSenderJndiConfiguration {
    private final MailProperties properties;

    MailSenderJndiConfiguration(MailProperties properties) {
        this.properties = properties;
    }

    @Bean
    public JavaMailSenderImpl mailSender(Session session) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setDefaultEncoding(this.properties.getDefaultEncoding().name());
        sender.setSession(session);
        return sender;
    }

    @ConditionalOnMissingBean
    @Bean
    public Session session() {
        String jndiName = this.properties.getJndiName();
        try {
            return (Session) new JndiLocatorDelegate().lookup(jndiName, Session.class);
        } catch (NamingException ex) {
            throw new IllegalStateException(String.format("Unable to find Session in JNDI location %s", jndiName), ex);
        }
    }
}
