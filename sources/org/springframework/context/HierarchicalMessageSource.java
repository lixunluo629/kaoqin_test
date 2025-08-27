package org.springframework.context;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/HierarchicalMessageSource.class */
public interface HierarchicalMessageSource extends MessageSource {
    void setParentMessageSource(MessageSource messageSource);

    MessageSource getParentMessageSource();
}
