package org.springframework.boot.autoconfigure.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@EnableConfigurationProperties
@Configuration
@ConditionalOnClass({EnableWebSecurity.class, AuthenticationEntryPoint.class})
@EnableWebSecurity
@ConditionalOnMissingBean({WebSecurityConfiguration.class})
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SpringBootWebSecurityConfiguration.class */
public class SpringBootWebSecurityConfiguration {
    private static List<String> DEFAULT_IGNORED = Arrays.asList("/css/**", "/js/**", "/images/**", "/webjars/**", "/**/favicon.ico");

    @ConditionalOnMissingBean({IgnoredPathsWebSecurityConfigurerAdapter.class})
    @Bean
    public IgnoredPathsWebSecurityConfigurerAdapter ignoredPathsWebSecurityConfigurerAdapter(List<IgnoredRequestCustomizer> customizers) {
        return new IgnoredPathsWebSecurityConfigurerAdapter(customizers);
    }

    @Bean
    public IgnoredRequestCustomizer defaultIgnoredRequestsCustomizer(ServerProperties server, SecurityProperties security, ObjectProvider<ErrorController> errorController) {
        return new DefaultIgnoredRequestCustomizer(server, security, errorController.getIfAvailable());
    }

    public static void configureHeaders(HeadersConfigurer<?> configurer, SecurityProperties.Headers headers) throws Exception {
        if (headers.getHsts() != SecurityProperties.Headers.HSTS.NONE) {
            boolean includeSubDomains = headers.getHsts() == SecurityProperties.Headers.HSTS.ALL;
            HstsHeaderWriter writer = new HstsHeaderWriter(includeSubDomains);
            writer.setRequestMatcher(AnyRequestMatcher.INSTANCE);
            configurer.addHeaderWriter(writer);
        }
        if (!headers.isContentType()) {
            configurer.contentTypeOptions().disable();
        }
        if (StringUtils.hasText(headers.getContentSecurityPolicy())) {
            String policyDirectives = headers.getContentSecurityPolicy();
            SecurityProperties.Headers.ContentSecurityPolicyMode mode = headers.getContentSecurityPolicyMode();
            if (mode == SecurityProperties.Headers.ContentSecurityPolicyMode.DEFAULT) {
                configurer.contentSecurityPolicy(policyDirectives);
            } else {
                configurer.contentSecurityPolicy(policyDirectives).reportOnly();
            }
        }
        if (!headers.isXss()) {
            configurer.xssProtection().disable();
        }
        if (!headers.isCache()) {
            configurer.cacheControl().disable();
        }
        if (!headers.isFrame()) {
            configurer.frameOptions().disable();
        }
    }

    @Order(Integer.MIN_VALUE)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SpringBootWebSecurityConfiguration$IgnoredPathsWebSecurityConfigurerAdapter.class */
    private static class IgnoredPathsWebSecurityConfigurerAdapter implements WebSecurityConfigurer<WebSecurity> {
        private final List<IgnoredRequestCustomizer> customizers;

        IgnoredPathsWebSecurityConfigurerAdapter(List<IgnoredRequestCustomizer> customizers) {
            this.customizers = customizers;
        }

        public void configure(WebSecurity builder) throws Exception {
        }

        public void init(WebSecurity builder) throws Exception {
            for (IgnoredRequestCustomizer customizer : this.customizers) {
                customizer.customize(builder.ignoring());
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SpringBootWebSecurityConfiguration$DefaultIgnoredRequestCustomizer.class */
    private class DefaultIgnoredRequestCustomizer implements IgnoredRequestCustomizer {
        private final ServerProperties server;
        private final SecurityProperties security;
        private final ErrorController errorController;

        DefaultIgnoredRequestCustomizer(ServerProperties server, SecurityProperties security, ErrorController errorController) {
            this.server = server;
            this.security = security;
            this.errorController = errorController;
        }

        @Override // org.springframework.boot.autoconfigure.security.IgnoredRequestCustomizer
        public void customize(WebSecurity.IgnoredRequestConfigurer configurer) {
            List<String> ignored = getIgnored(this.security);
            if (this.errorController != null) {
                ignored.add(normalizePath(this.errorController.getErrorPath()));
            }
            String[] paths = this.server.getPathsArray(ignored);
            List<RequestMatcher> matchers = new ArrayList<>();
            if (!ObjectUtils.isEmpty((Object[]) paths)) {
                for (String pattern : paths) {
                    matchers.add(new AntPathRequestMatcher(pattern, (String) null));
                }
            }
            if (!matchers.isEmpty()) {
                configurer.requestMatchers(new RequestMatcher[]{new OrRequestMatcher(matchers)});
            }
        }

        private List<String> getIgnored(SecurityProperties security) {
            List<String> ignored = new ArrayList<>(security.getIgnored());
            if (ignored.isEmpty()) {
                ignored.addAll(SpringBootWebSecurityConfiguration.DEFAULT_IGNORED);
            } else if (ignored.contains("none")) {
                ignored.remove("none");
            }
            return ignored;
        }

        private String normalizePath(String errorPath) {
            String result = StringUtils.cleanPath(errorPath);
            if (!result.startsWith("/")) {
                result = "/" + result;
            }
            return result;
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "security.basic", name = {"enabled"}, havingValue = "false")
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SpringBootWebSecurityConfiguration$ApplicationNoWebSecurityConfigurerAdapter.class */
    protected static class ApplicationNoWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        protected ApplicationNoWebSecurityConfigurerAdapter() {
        }

        protected void configure(HttpSecurity http) throws Exception {
            http.requestMatcher(new RequestMatcher() { // from class: org.springframework.boot.autoconfigure.security.SpringBootWebSecurityConfiguration.ApplicationNoWebSecurityConfigurerAdapter.1
                public boolean matches(HttpServletRequest request) {
                    return false;
                }
            });
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "security.basic", name = {"enabled"}, matchIfMissing = true)
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SpringBootWebSecurityConfiguration$ApplicationWebSecurityConfigurerAdapter.class */
    protected static class ApplicationWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        private SecurityProperties security;

        protected ApplicationWebSecurityConfigurerAdapter(SecurityProperties security) {
            this.security = security;
        }

        protected void configure(HttpSecurity http) throws Exception {
            if (this.security.isRequireSsl()) {
                ((ChannelSecurityConfigurer.RequiresChannelUrl) http.requiresChannel().anyRequest()).requiresSecure();
            }
            if (!this.security.isEnableCsrf()) {
                http.csrf().disable();
            }
            http.sessionManagement().sessionCreationPolicy(this.security.getSessions());
            SpringBootWebSecurityConfiguration.configureHeaders(http.headers(), this.security.getHeaders());
            String[] paths = getSecureApplicationPaths();
            if (paths.length > 0) {
                AuthenticationEntryPoint entryPoint = entryPoint();
                http.exceptionHandling().authenticationEntryPoint(entryPoint);
                http.httpBasic().authenticationEntryPoint(entryPoint);
                http.requestMatchers().antMatchers(paths);
                String[] roles = (String[]) this.security.getUser().getRole().toArray(new String[0]);
                SecurityAuthorizeMode mode = this.security.getBasic().getAuthorizeMode();
                if (mode == null || mode == SecurityAuthorizeMode.ROLE) {
                    ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http.authorizeRequests().anyRequest()).hasAnyRole(roles);
                } else if (mode == SecurityAuthorizeMode.AUTHENTICATED) {
                    ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http.authorizeRequests().anyRequest()).authenticated();
                }
            }
        }

        private String[] getSecureApplicationPaths() {
            List<String> list = new ArrayList<>();
            String[] path = this.security.getBasic().getPath();
            int length = path.length;
            for (int i = 0; i < length; i++) {
                String path2 = path[i];
                String path3 = path2 != null ? path2.trim() : "";
                if (path3.equals("/**")) {
                    return new String[]{path3};
                }
                if (!path3.equals("")) {
                    list.add(path3);
                }
            }
            return (String[]) list.toArray(new String[list.size()]);
        }

        private AuthenticationEntryPoint entryPoint() {
            BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
            entryPoint.setRealmName(this.security.getBasic().getRealm());
            return entryPoint;
        }
    }
}
