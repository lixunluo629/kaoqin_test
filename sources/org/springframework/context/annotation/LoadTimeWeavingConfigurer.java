package org.springframework.context.annotation;

import org.springframework.instrument.classloading.LoadTimeWeaver;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/LoadTimeWeavingConfigurer.class */
public interface LoadTimeWeavingConfigurer {
    LoadTimeWeaver getLoadTimeWeaver();
}
