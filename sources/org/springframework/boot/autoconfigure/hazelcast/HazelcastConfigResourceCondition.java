package org.springframework.boot.autoconfigure.hazelcast;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ResourceCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastConfigResourceCondition.class */
public abstract class HazelcastConfigResourceCondition extends ResourceCondition {
    static final String CONFIG_SYSTEM_PROPERTY = "hazelcast.config";

    protected HazelcastConfigResourceCondition(String prefix, String propertyName) {
        super("Hazelcast", prefix, propertyName, "file:./hazelcast.xml", "classpath:/hazelcast.xml");
    }

    @Override // org.springframework.boot.autoconfigure.condition.ResourceCondition
    protected ConditionOutcome getResourceOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (System.getProperty(CONFIG_SYSTEM_PROPERTY) != null) {
            return ConditionOutcome.match(startConditionMessage().because("System property 'hazelcast.config' is set."));
        }
        return super.getResourceOutcome(context, metadata);
    }
}
