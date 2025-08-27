package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContext;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/LocaleContextResolver.class */
public interface LocaleContextResolver extends LocaleResolver {
    LocaleContext resolveLocaleContext(HttpServletRequest httpServletRequest);

    void setLocaleContext(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, LocaleContext localeContext);
}
