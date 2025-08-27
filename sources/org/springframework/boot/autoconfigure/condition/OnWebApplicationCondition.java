package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Annotation;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.StandardServletEnvironment;

@Order(-2147483628)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnWebApplicationCondition.class */
class OnWebApplicationCondition extends SpringBootCondition {
    private static final String WEB_CONTEXT_CLASS = "org.springframework.web.context.support.GenericWebApplicationContext";

    OnWebApplicationCondition() {
    }

    @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        boolean required = metadata.isAnnotated(ConditionalOnWebApplication.class.getName());
        ConditionOutcome outcome = isWebApplication(context, metadata, required);
        if (required && !outcome.isMatch()) {
            return ConditionOutcome.noMatch(outcome.getConditionMessage());
        }
        if (!required && outcome.isMatch()) {
            return ConditionOutcome.noMatch(outcome.getConditionMessage());
        }
        return ConditionOutcome.match(outcome.getConditionMessage());
    }

    private ConditionOutcome isWebApplication(ConditionContext context, AnnotatedTypeMetadata metadata, boolean required) {
        Object[] objArr = new Object[1];
        objArr[0] = required ? "(required)" : "";
        ConditionMessage.Builder message = ConditionMessage.forCondition((Class<? extends Annotation>) ConditionalOnWebApplication.class, objArr);
        if (!ClassUtils.isPresent(WEB_CONTEXT_CLASS, context.getClassLoader())) {
            return ConditionOutcome.noMatch(message.didNotFind("web application classes").atAll());
        }
        if (context.getBeanFactory() != null) {
            String[] scopes = context.getBeanFactory().getRegisteredScopeNames();
            if (ObjectUtils.containsElement(scopes, "session")) {
                return ConditionOutcome.match(message.foundExactly("'session' scope"));
            }
        }
        if (context.getEnvironment() instanceof StandardServletEnvironment) {
            return ConditionOutcome.match(message.foundExactly("StandardServletEnvironment"));
        }
        if (context.getResourceLoader() instanceof WebApplicationContext) {
            return ConditionOutcome.match(message.foundExactly("WebApplicationContext"));
        }
        return ConditionOutcome.noMatch(message.because("not a web application"));
    }
}
