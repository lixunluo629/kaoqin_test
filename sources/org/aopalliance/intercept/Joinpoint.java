package org.aopalliance.intercept;

import java.lang.reflect.AccessibleObject;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/aopalliance/intercept/Joinpoint.class */
public interface Joinpoint {
    Object proceed() throws Throwable;

    Object getThis();

    AccessibleObject getStaticPart();
}
