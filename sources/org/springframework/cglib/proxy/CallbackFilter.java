package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/proxy/CallbackFilter.class */
public interface CallbackFilter {
    int accept(Method method);

    boolean equals(Object obj);
}
