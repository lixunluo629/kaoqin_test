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
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@AutoConfigureBefore({SocialWebAutoConfiguration.class})
@Configuration
@ConditionalOnClass({SocialConfigurerAdapter.class, FacebookConnectionFactory.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.social.facebook", name = {"app-id"})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/FacebookAutoConfiguration.class */
public class FacebookAutoConfiguration {

    @EnableConfigurationProperties({FacebookProperties.class})
    @Configuration
    @EnableSocial
    @ConditionalOnWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/FacebookAutoConfiguration$FacebookConfigurerAdapter.class */
    protected static class FacebookConfigurerAdapter extends SocialAutoConfigurerAdapter {
        private final FacebookProperties properties;

        protected FacebookConfigurerAdapter(FacebookProperties properties) {
            this.properties = properties;
        }

        @ConditionalOnMissingBean({Facebook.class})
        @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
        @Bean
        public Facebook facebook(ConnectionRepository repository) {
            Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
            if (connection != null) {
                return (Facebook) connection.getApi();
            }
            return null;
        }

        @ConditionalOnProperty(prefix = "spring.social", name = {"auto-connection-views"})
        @Bean(name = {"connect/facebookConnect", "connect/facebookConnected"})
        public GenericConnectionStatusView facebookConnectView() {
            return new GenericConnectionStatusView("facebook", "Facebook");
        }

        @Override // org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter
        protected ConnectionFactory<?> createConnectionFactory() {
            return new FacebookConnectionFactory(this.properties.getAppId(), this.properties.getAppSecret());
        }
    }
}
