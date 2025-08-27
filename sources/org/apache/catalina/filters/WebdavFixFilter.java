package org.apache.catalina.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/filters/WebdavFixFilter.class */
public class WebdavFixFilter implements Filter {
    private static final String LOG_MESSAGE_PREAMBLE = "WebdavFixFilter: Detected client problem: ";
    private static final String UA_MINIDIR_START = "Microsoft-WebDAV-MiniRedir";
    private static final String UA_MINIDIR_5_1_2600 = "Microsoft-WebDAV-MiniRedir/5.1.2600";
    private static final String UA_MINIDIR_5_2_3790 = "Microsoft-WebDAV-MiniRedir/5.2.3790";

    @Override // javax.servlet.Filter
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override // javax.servlet.Filter
    public void destroy() {
    }

    @Override // javax.servlet.Filter
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String ua = httpRequest.getHeader("User-Agent");
        if (ua == null || ua.length() == 0 || !ua.startsWith(UA_MINIDIR_START)) {
            chain.doFilter(request, response);
            return;
        }
        if (ua.startsWith(UA_MINIDIR_5_1_2600)) {
            httpResponse.sendRedirect(buildRedirect(httpRequest));
            return;
        }
        if (ua.startsWith(UA_MINIDIR_5_2_3790)) {
            if (!"".equals(httpRequest.getContextPath())) {
                log(httpRequest, "XP-x64-SP2 clients only work with the root context");
            }
            log(httpRequest, "XP-x64-SP2 is known not to work with WebDAV Servlet");
            chain.doFilter(request, response);
            return;
        }
        httpResponse.sendRedirect(buildRedirect(httpRequest));
    }

    private String buildRedirect(HttpServletRequest request) {
        StringBuilder location = new StringBuilder(request.getRequestURL().length());
        location.append(request.getScheme());
        location.append("://");
        location.append(request.getServerName());
        location.append(':');
        location.append(request.getServerPort());
        location.append(request.getRequestURI());
        return location.toString();
    }

    private void log(ServletRequest request, String msg) {
        request.getServletContext().log(LOG_MESSAGE_PREAMBLE + msg);
    }
}
