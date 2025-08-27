package org.springframework.boot.autoconfigure.h2;

import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.autoconfigure.security.SecurityAuthorizeMode;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

@EnableConfigurationProperties({H2ConsoleProperties.class})
@Configuration
@ConditionalOnClass({WebServlet.class})
@AutoConfigureAfter({SecurityAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.h2.console", name = {"enabled"}, havingValue = "true", matchIfMissing = false)
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/h2/H2ConsoleAutoConfiguration.class */
public class H2ConsoleAutoConfiguration {
    private final H2ConsoleProperties properties;

    public H2ConsoleAutoConfiguration(H2ConsoleProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ServletRegistrationBean h2Console() {
        String path = this.properties.getPath();
        String urlMapping = path.endsWith("/") ? path + "*" : path + ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER;
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet(), urlMapping);
        H2ConsoleProperties.Settings settings = this.properties.getSettings();
        if (settings.isTrace()) {
            registration.addInitParameter("trace", "");
        }
        if (settings.isWebAllowOthers()) {
            registration.addInitParameter("webAllowOthers", "");
        }
        return registration;
    }

    @Configuration
    @ConditionalOnClass({WebSecurityConfigurerAdapter.class})
    @ConditionalOnBean({ObjectPostProcessor.class})
    @ConditionalOnProperty(prefix = "security.basic", name = {"enabled"}, matchIfMissing = true)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/h2/H2ConsoleAutoConfiguration$H2ConsoleSecurityConfiguration.class */
    static class H2ConsoleSecurityConfiguration {
        H2ConsoleSecurityConfiguration() {
        }

        @Bean
        public WebSecurityConfigurerAdapter h2ConsoleSecurityConfigurer() {
            return new H2ConsoleSecurityConfigurer();
        }

        @Order(2147483632)
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/h2/H2ConsoleAutoConfiguration$H2ConsoleSecurityConfiguration$H2ConsoleSecurityConfigurer.class */
        private static class H2ConsoleSecurityConfigurer extends WebSecurityConfigurerAdapter {

            @Autowired
            private H2ConsoleProperties console;

            @Autowired
            private SecurityProperties security;

            private H2ConsoleSecurityConfigurer() {
            }

            public void configure(HttpSecurity http) throws Exception {
                String path = this.console.getPath();
                String antPattern = path.endsWith("/") ? path + SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS : path + "/**";
                HttpSecurity h2Console = http.antMatcher(antPattern);
                h2Console.csrf().disable();
                h2Console.httpBasic();
                h2Console.headers().frameOptions().sameOrigin();
                String[] roles = (String[]) this.security.getUser().getRole().toArray(new String[0]);
                SecurityAuthorizeMode mode = this.security.getBasic().getAuthorizeMode();
                if (mode == null || mode == SecurityAuthorizeMode.ROLE) {
                    ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http.authorizeRequests().anyRequest()).hasAnyRole(roles);
                } else if (mode == SecurityAuthorizeMode.AUTHENTICATED) {
                    ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http.authorizeRequests().anyRequest()).authenticated();
                }
            }
        }
    }
}
