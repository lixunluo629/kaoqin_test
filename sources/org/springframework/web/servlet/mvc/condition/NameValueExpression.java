package org.springframework.web.servlet.mvc.condition;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/condition/NameValueExpression.class */
public interface NameValueExpression<T> {
    String getName();

    T getValue();

    boolean isNegated();
}
