package org.springframework.aop.framework;

import java.lang.reflect.Method;
import java.util.List;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/AdvisorChainFactory.class */
public interface AdvisorChainFactory {
    List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised advised, Method method, Class<?> cls);
}
