package org.springframework.web.servlet;

import java.util.Locale;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/ViewResolver.class */
public interface ViewResolver {
    View resolveViewName(String str, Locale locale) throws Exception;
}
