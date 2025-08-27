package org.springframework.web.servlet.handler;

import org.springframework.web.method.HandlerMethod;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/HandlerMethodMappingNamingStrategy.class */
public interface HandlerMethodMappingNamingStrategy<T> {
    String getName(HandlerMethod handlerMethod, T t);
}
