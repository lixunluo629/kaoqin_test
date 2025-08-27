package org.springframework.boot.autoconfigure.sendgrid;

import com.sendgrid.SendGrid;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;

@EnableConfigurationProperties({SendGridProperties.class})
@Configuration
@ConditionalOnClass({SendGrid.class})
@Conditional({SendGridPropertyCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/sendgrid/SendGridAutoConfiguration.class */
public class SendGridAutoConfiguration {
    private final SendGridProperties properties;

    public SendGridAutoConfiguration(SendGridProperties properties) {
        this.properties = properties;
    }

    @ConditionalOnMissingBean({SendGrid.class})
    @Bean
    public SendGrid sendGrid() {
        SendGrid sendGrid = createSendGrid();
        if (this.properties.isProxyConfigured()) {
            HttpHost proxy = new HttpHost(this.properties.getProxy().getHost(), this.properties.getProxy().getPort().intValue());
            sendGrid.setClient(HttpClientBuilder.create().setProxy(proxy).setUserAgent("sendgrid/" + sendGrid.getVersion() + ";java").build());
        }
        return sendGrid;
    }

    private SendGrid createSendGrid() {
        if (this.properties.getApiKey() != null) {
            return new SendGrid(this.properties.getApiKey());
        }
        return new SendGrid(this.properties.getUsername(), this.properties.getPassword());
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/sendgrid/SendGridAutoConfiguration$SendGridPropertyCondition.class */
    static class SendGridPropertyCondition extends AnyNestedCondition {
        SendGridPropertyCondition() {
            super(ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnProperty(prefix = "spring.sendgrid", value = {"username"})
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/sendgrid/SendGridAutoConfiguration$SendGridPropertyCondition$SendGridUserProperty.class */
        static class SendGridUserProperty {
            SendGridUserProperty() {
            }
        }

        @ConditionalOnProperty(prefix = "spring.sendgrid", value = {"api-key"})
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/sendgrid/SendGridAutoConfiguration$SendGridPropertyCondition$SendGridApiKeyProperty.class */
        static class SendGridApiKeyProperty {
            SendGridApiKeyProperty() {
            }
        }
    }
}
