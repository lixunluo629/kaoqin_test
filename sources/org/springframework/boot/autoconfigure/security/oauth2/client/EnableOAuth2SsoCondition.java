package org.springframework.boot.autoconfigure.security.oauth2.client;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/EnableOAuth2SsoCondition.class */
class EnableOAuth2SsoCondition extends SpringBootCondition {
    EnableOAuth2SsoCondition() {
    }

    @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String[] enablers = context.getBeanFactory().getBeanNamesForAnnotation(EnableOAuth2Sso.class);
        ConditionMessage.Builder message = ConditionMessage.forCondition("@EnableOAuth2Sso Condition", new Object[0]);
        for (String name : enablers) {
            if (context.getBeanFactory().isTypeMatch(name, WebSecurityConfigurerAdapter.class)) {
                return ConditionOutcome.match(message.found("@EnableOAuth2Sso annotation on WebSecurityConfigurerAdapter").items(name));
            }
        }
        return ConditionOutcome.noMatch(message.didNotFind("@EnableOAuth2Sso annotation on any WebSecurityConfigurerAdapter").atAll());
    }
}
