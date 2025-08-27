package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/HandlerAdapter.class */
public interface HandlerAdapter {
    boolean supports(Object obj);

    ModelAndView handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj) throws Exception;

    long getLastModified(HttpServletRequest httpServletRequest, Object obj);
}
