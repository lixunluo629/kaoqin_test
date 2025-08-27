package org.springframework.web.method;

import java.lang.reflect.Method;
import java.util.Set;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.ReflectionUtils;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/HandlerMethodSelector.class */
public abstract class HandlerMethodSelector {
    public static Set<Method> selectMethods(Class<?> handlerType, ReflectionUtils.MethodFilter handlerMethodFilter) {
        return MethodIntrospector.selectMethods(handlerType, handlerMethodFilter);
    }
}
