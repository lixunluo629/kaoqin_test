package org.springframework.beans.factory.support;

import java.lang.reflect.Method;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/MethodReplacer.class */
public interface MethodReplacer {
    Object reimplement(Object obj, Method method, Object[] objArr) throws Throwable;
}
