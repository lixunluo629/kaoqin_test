package org.springframework.web.filter;

import javax.servlet.http.HttpServletRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/filter/ServletContextRequestLoggingFilter.class */
public class ServletContextRequestLoggingFilter extends AbstractRequestLoggingFilter {
    @Override // org.springframework.web.filter.AbstractRequestLoggingFilter
    protected void beforeRequest(HttpServletRequest request, String message) {
        getServletContext().log(message);
    }

    @Override // org.springframework.web.filter.AbstractRequestLoggingFilter
    protected void afterRequest(HttpServletRequest request, String message) {
        getServletContext().log(message);
    }
}
