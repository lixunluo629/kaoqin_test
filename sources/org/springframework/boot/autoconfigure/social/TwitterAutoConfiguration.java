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
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@AutoConfigureBefore({SocialWebAutoConfiguration.class})
@Configuration
@ConditionalOnClass({SocialConfigurerAdapter.class, TwitterConnectionFactory.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.social.twitter", name = {"app-id"})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/TwitterAutoConfiguration.class */
public class TwitterAutoConfiguration {

    @EnableConfigurationProperties({TwitterProperties.class})
    @Configuration
    @EnableSocial
    @ConditionalOnWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/TwitterAutoConfiguration$TwitterConfigurerAdapter.class */
    protected static class TwitterConfigurerAdapter extends SocialAutoConfigurerAdapter {
        private final TwitterProperties properties;

        protected TwitterConfigurerAdapter(TwitterProperties properties) {
            this.properties = properties;
        }

        @ConditionalOnMissingBean
        @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
        @Bean
        public Twitter twitter(ConnectionRepository repository) {
            Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
            if (connection != null) {
                return (Twitter) connection.getApi();
            }
            return new TwitterTemplate(this.properties.getAppId(), this.properties.getAppSecret());
        }

        @ConditionalOnProperty(prefix = "spring.social", name = {"auto-connection-views"})
        @Bean(name = {"connect/twitterConnect", "connect/twitterConnected"})
        public GenericConnectionStatusView twitterConnectView() {
            return new GenericConnectionStatusView("twitter", "Twitter");
        }

        @Override // org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter
        protected ConnectionFactory<?> createConnectionFactory() {
            return new TwitterConnectionFactory(this.properties.getAppId(), this.properties.getAppSecret());
        }
    }
}
