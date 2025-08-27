package org.springframework.core;

import org.springframework.util.ClassUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/DefaultParameterNameDiscoverer.class */
public class DefaultParameterNameDiscoverer extends PrioritizedParameterNameDiscoverer {
    private static final boolean standardReflectionAvailable = ClassUtils.isPresent("java.lang.reflect.Executable", DefaultParameterNameDiscoverer.class.getClassLoader());

    public DefaultParameterNameDiscoverer() {
        if (standardReflectionAvailable) {
            addDiscoverer(new StandardReflectionParameterNameDiscoverer());
        }
        addDiscoverer(new LocalVariableTableParameterNameDiscoverer());
    }
}
