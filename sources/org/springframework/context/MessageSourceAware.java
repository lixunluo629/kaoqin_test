package org.springframework.context;

import org.springframework.beans.factory.Aware;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/MessageSourceAware.class */
public interface MessageSourceAware extends Aware {
    void setMessageSource(MessageSource messageSource);
}
