package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/RequestToViewNameTranslator.class */
public interface RequestToViewNameTranslator {
    String getViewName(HttpServletRequest httpServletRequest) throws Exception;
}
