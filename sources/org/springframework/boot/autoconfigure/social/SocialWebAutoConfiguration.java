package org.springframework.boot.autoconfigure.social;

import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.connect.web.DisconnectInterceptor;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInInterceptor;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.connect.web.thymeleaf.SpringSocialDialect;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;

@AutoConfigureBefore({ThymeleafAutoConfiguration.class})
@Configuration
@ConditionalOnClass({ConnectController.class, SocialConfigurerAdapter.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@ConditionalOnBean({ConnectionFactoryLocator.class, UsersConnectionRepository.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/SocialWebAutoConfiguration.class */
public class SocialWebAutoConfiguration {

    @Configuration
    @EnableSocial
    @ConditionalOnWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/SocialWebAutoConfiguration$SocialAutoConfigurationAdapter.class */
    protected static class SocialAutoConfigurationAdapter extends SocialConfigurerAdapter {
        private final List<ConnectInterceptor<?>> connectInterceptors;
        private final List<DisconnectInterceptor<?>> disconnectInterceptors;
        private final List<ProviderSignInInterceptor<?>> signInInterceptors;

        public SocialAutoConfigurationAdapter(ObjectProvider<List<ConnectInterceptor<?>>> connectInterceptorsProvider, ObjectProvider<List<DisconnectInterceptor<?>>> disconnectInterceptorsProvider, ObjectProvider<List<ProviderSignInInterceptor<?>>> signInInterceptorsProvider) {
            this.connectInterceptors = connectInterceptorsProvider.getIfAvailable();
            this.disconnectInterceptors = disconnectInterceptorsProvider.getIfAvailable();
            this.signInInterceptors = signInInterceptorsProvider.getIfAvailable();
        }

        @ConditionalOnMissingBean({ConnectController.class})
        @Bean
        public ConnectController connectController(ConnectionFactoryLocator factoryLocator, ConnectionRepository repository) {
            ConnectController controller = new ConnectController(factoryLocator, repository);
            if (!CollectionUtils.isEmpty(this.connectInterceptors)) {
                controller.setConnectInterceptors(this.connectInterceptors);
            }
            if (!CollectionUtils.isEmpty(this.disconnectInterceptors)) {
                controller.setDisconnectInterceptors(this.disconnectInterceptors);
            }
            return controller;
        }

        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "spring.social", name = {"auto-connection-views"})
        @Bean
        public BeanNameViewResolver beanNameViewResolver() {
            BeanNameViewResolver viewResolver = new BeanNameViewResolver();
            viewResolver.setOrder(Integer.MIN_VALUE);
            return viewResolver;
        }

        @ConditionalOnMissingBean
        @ConditionalOnBean({SignInAdapter.class})
        @Bean
        public ProviderSignInController signInController(ConnectionFactoryLocator factoryLocator, UsersConnectionRepository usersRepository, SignInAdapter signInAdapter) {
            ProviderSignInController controller = new ProviderSignInController(factoryLocator, usersRepository, signInAdapter);
            if (!CollectionUtils.isEmpty(this.signInInterceptors)) {
                controller.setSignInInterceptors(this.signInInterceptors);
            }
            return controller;
        }
    }

    @ConditionalOnMissingClass({"org.springframework.security.core.context.SecurityContextHolder"})
    @Configuration
    @EnableSocial
    @ConditionalOnWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/SocialWebAutoConfiguration$AnonymousUserIdSourceConfig.class */
    protected static class AnonymousUserIdSourceConfig extends SocialConfigurerAdapter {
        protected AnonymousUserIdSourceConfig() {
        }

        public UserIdSource getUserIdSource() {
            return new UserIdSource() { // from class: org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration.AnonymousUserIdSourceConfig.1
                public String getUserId() {
                    return "anonymous";
                }
            };
        }
    }

    @Configuration
    @ConditionalOnClass({SecurityContextHolder.class})
    @EnableSocial
    @ConditionalOnWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/SocialWebAutoConfiguration$AuthenticationUserIdSourceConfig.class */
    protected static class AuthenticationUserIdSourceConfig extends SocialConfigurerAdapter {
        protected AuthenticationUserIdSourceConfig() {
        }

        public UserIdSource getUserIdSource() {
            return new SecurityContextUserIdSource();
        }
    }

    @Configuration
    @ConditionalOnClass({SpringResourceResourceResolver.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/SocialWebAutoConfiguration$SpringSocialThymeleafConfig.class */
    protected static class SpringSocialThymeleafConfig {
        protected SpringSocialThymeleafConfig() {
        }

        @ConditionalOnMissingBean
        @Bean
        public SpringSocialDialect springSocialDialect() {
            return new SpringSocialDialect();
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/SocialWebAutoConfiguration$SecurityContextUserIdSource.class */
    private static class SecurityContextUserIdSource implements UserIdSource {
        private SecurityContextUserIdSource() {
        }

        public String getUserId() {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            Assert.state(authentication != null, "Unable to get a ConnectionRepository: no user signed in");
            return authentication.getName();
        }
    }
}
