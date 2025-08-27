package org.springframework.context.annotation;

import org.springframework.core.type.AnnotatedTypeMetadata;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/Condition.class */
public interface Condition {
    boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata);
}
