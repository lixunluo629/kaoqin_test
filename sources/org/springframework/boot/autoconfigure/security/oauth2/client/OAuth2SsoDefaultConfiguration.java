package org.springframework.boot.autoconfigure.security.oauth2.client;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.util.ClassUtils;

@Configuration
@Conditional({NeedsWebSecurityCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2SsoDefaultConfiguration.class */
public class OAuth2SsoDefaultConfiguration extends WebSecurityConfigurerAdapter implements Ordered {
    private final ApplicationContext applicationContext;
    private final OAuth2SsoProperties sso;

    public OAuth2SsoDefaultConfiguration(ApplicationContext applicationContext, OAuth2SsoProperties sso) {
        this.applicationContext = applicationContext;
        this.sso = sso;
    }

    protected void configure(HttpSecurity http) throws Exception {
        ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http.antMatcher("/**").authorizeRequests().anyRequest()).authenticated();
        new SsoSecurityConfigurer(this.applicationContext).configure(http);
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        if (this.sso.getFilterOrder() != null) {
            return this.sso.getFilterOrder().intValue();
        }
        if (ClassUtils.isPresent("org.springframework.boot.actuate.autoconfigure.ManagementServerProperties", null)) {
            return 2147483635;
        }
        return SecurityProperties.ACCESS_OVERRIDE_ORDER;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2SsoDefaultConfiguration$NeedsWebSecurityCondition.class */
    protected static class NeedsWebSecurityCondition extends EnableOAuth2SsoCondition {
        protected NeedsWebSecurityCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2SsoCondition, org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ConditionOutcome.inverse(super.getMatchOutcome(context, metadata));
        }
    }
}
