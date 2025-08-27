package org.springframework.boot.logging;

import java.net.URLClassLoader;
import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/ClasspathLoggingApplicationListener.class */
public final class ClasspathLoggingApplicationListener implements GenericApplicationListener {
    private static final int ORDER = -2147483627;
    private static final Log logger = LogFactory.getLog(ClasspathLoggingApplicationListener.class);

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ApplicationEvent event) {
        if (logger.isDebugEnabled()) {
            if (event instanceof ApplicationEnvironmentPreparedEvent) {
                logger.debug("Application started with classpath: " + getClasspath());
            } else if (event instanceof ApplicationFailedEvent) {
                logger.debug("Application failed to start with classpath: " + getClasspath());
            }
        }
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return ORDER;
    }

    @Override // org.springframework.context.event.GenericApplicationListener
    public boolean supportsEventType(ResolvableType resolvableType) {
        Class<?> type = resolvableType.getRawClass();
        if (type == null) {
            return false;
        }
        return ApplicationEnvironmentPreparedEvent.class.isAssignableFrom(type) || ApplicationFailedEvent.class.isAssignableFrom(type);
    }

    @Override // org.springframework.context.event.GenericApplicationListener
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    private String getClasspath() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader instanceof URLClassLoader) {
            return Arrays.toString(((URLClassLoader) classLoader).getURLs());
        }
        return "unknown";
    }
}
