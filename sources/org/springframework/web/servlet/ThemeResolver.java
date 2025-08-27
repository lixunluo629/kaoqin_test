package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/ThemeResolver.class */
public interface ThemeResolver {
    String resolveThemeName(HttpServletRequest httpServletRequest);

    void setThemeName(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String str);
}
