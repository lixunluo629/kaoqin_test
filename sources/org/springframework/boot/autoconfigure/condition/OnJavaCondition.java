package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Order(-2147483628)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnJavaCondition.class */
class OnJavaCondition extends SpringBootCondition {
    private static final ConditionalOnJava.JavaVersion JVM_VERSION = ConditionalOnJava.JavaVersion.getJavaVersion();

    OnJavaCondition() {
    }

    @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnJava.class.getName());
        ConditionalOnJava.Range range = (ConditionalOnJava.Range) attributes.get("range");
        ConditionalOnJava.JavaVersion version = (ConditionalOnJava.JavaVersion) attributes.get("value");
        return getMatchOutcome(range, JVM_VERSION, version);
    }

    protected ConditionOutcome getMatchOutcome(ConditionalOnJava.Range range, ConditionalOnJava.JavaVersion runningVersion, ConditionalOnJava.JavaVersion version) {
        boolean match = runningVersion.isWithin(range, version);
        String expected = String.format(range != ConditionalOnJava.Range.EQUAL_OR_NEWER ? "(older than %s)" : "(%s or newer)", version);
        ConditionMessage message = ConditionMessage.forCondition((Class<? extends Annotation>) ConditionalOnJava.class, expected).foundExactly(runningVersion);
        return new ConditionOutcome(match, message);
    }
}
