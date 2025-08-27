package org.springframework.boot.autoconfigure.session;

import java.util.Locale;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionCondition.class */
class SessionCondition extends SpringBootCondition {
    SessionCondition() {
    }

    @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition("Session Condition", new Object[0]);
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "spring.session.");
        StoreType sessionStoreType = SessionStoreMappings.getType(((AnnotationMetadata) metadata).getClassName());
        if (!resolver.containsProperty("store-type")) {
            return ConditionOutcome.noMatch(message.didNotFind("spring.session.store-type property").atAll());
        }
        String value = resolver.getProperty("store-type").replace('-', '_').toUpperCase(Locale.ENGLISH);
        return value.equals(sessionStoreType.name()) ? ConditionOutcome.match(message.found("spring.session.store-type property").items(sessionStoreType)) : ConditionOutcome.noMatch(message.found("spring.session.store-type property").items(value));
    }
}
