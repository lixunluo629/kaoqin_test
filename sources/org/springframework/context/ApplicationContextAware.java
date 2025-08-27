package org.springframework.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/ApplicationContextAware.class */
public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
