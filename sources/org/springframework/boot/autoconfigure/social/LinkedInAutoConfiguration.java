package org.springframework.boot.autoconfigure.social;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.GenericConnectionStatusView;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;

@AutoConfigureBefore({SocialWebAutoConfiguration.class})
@Configuration
@ConditionalOnClass({SocialConfigurerAdapter.class, LinkedInConnectionFactory.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.social.linkedin", name = {"app-id"})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/LinkedInAutoConfiguration.class */
public class LinkedInAutoConfiguration {

    @EnableConfigurationProperties({LinkedInProperties.class})
    @Configuration
    @EnableSocial
    @ConditionalOnWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/LinkedInAutoConfiguration$LinkedInConfigurerAdapter.class */
    protected static class LinkedInConfigurerAdapter extends SocialAutoConfigurerAdapter {
        private final LinkedInProperties properties;

        protected LinkedInConfigurerAdapter(LinkedInProperties properties) {
            this.properties = properties;
        }

        @ConditionalOnMissingBean({LinkedIn.class})
        @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
        @Bean
        public LinkedIn linkedin(ConnectionRepository repository) {
            Connection<LinkedIn> connection = repository.findPrimaryConnection(LinkedIn.class);
            if (connection != null) {
                return (LinkedIn) connection.getApi();
            }
            return null;
        }

        @ConditionalOnProperty(prefix = "spring.social", name = {"auto-connection-views"})
        @Bean(name = {"connect/linkedinConnect", "connect/linkedinConnected"})
        public GenericConnectionStatusView linkedInConnectView() {
            return new GenericConnectionStatusView("linkedin", "LinkedIn");
        }

        @Override // org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter
        protected ConnectionFactory<?> createConnectionFactory() {
            return new LinkedInConnectionFactory(this.properties.getAppId(), this.properties.getAppSecret());
        }
    }
}
