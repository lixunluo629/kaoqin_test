package org.springframework.context.annotation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConfigurationCondition.class */
public interface ConfigurationCondition extends Condition {

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConfigurationCondition$ConfigurationPhase.class */
    public enum ConfigurationPhase {
        PARSE_CONFIGURATION,
        REGISTER_BEAN
    }

    ConfigurationPhase getConfigurationPhase();
}
