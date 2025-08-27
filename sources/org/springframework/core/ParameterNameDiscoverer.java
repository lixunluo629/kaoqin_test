package org.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ParameterNameDiscoverer.class */
public interface ParameterNameDiscoverer {
    String[] getParameterNames(Method method);

    String[] getParameterNames(Constructor<?> constructor);
}
