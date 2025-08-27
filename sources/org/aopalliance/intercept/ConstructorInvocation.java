package org.aopalliance.intercept;

import java.lang.reflect.Constructor;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/aopalliance/intercept/ConstructorInvocation.class */
public interface ConstructorInvocation extends Invocation {
    Constructor<?> getConstructor();
}
