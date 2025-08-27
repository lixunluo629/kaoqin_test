package org.springframework.web.servlet;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/LocaleResolver.class */
public interface LocaleResolver {
    Locale resolveLocale(HttpServletRequest httpServletRequest);

    void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale);
}
