package org.springframework.hateoas.core;

import java.lang.reflect.Method;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/MappingDiscoverer.class */
public interface MappingDiscoverer {
    String getMapping(Class<?> cls);

    String getMapping(Method method);

    String getMapping(Class<?> cls, Method method);
}
