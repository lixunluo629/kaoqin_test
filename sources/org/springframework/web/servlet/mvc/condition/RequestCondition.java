package org.springframework.web.servlet.mvc.condition;

import javax.servlet.http.HttpServletRequest;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/condition/RequestCondition.class */
public interface RequestCondition<T> {
    T combine(T t);

    T getMatchingCondition(HttpServletRequest httpServletRequest);

    int compareTo(T t, HttpServletRequest httpServletRequest);
}
