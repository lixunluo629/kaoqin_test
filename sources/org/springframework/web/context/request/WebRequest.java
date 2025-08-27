package org.springframework.web.context.request;

import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/WebRequest.class */
public interface WebRequest extends RequestAttributes {
    String getHeader(String str);

    String[] getHeaderValues(String str);

    Iterator<String> getHeaderNames();

    String getParameter(String str);

    String[] getParameterValues(String str);

    Iterator<String> getParameterNames();

    Map<String, String[]> getParameterMap();

    Locale getLocale();

    String getContextPath();

    String getRemoteUser();

    Principal getUserPrincipal();

    boolean isUserInRole(String str);

    boolean isSecure();

    boolean checkNotModified(long j);

    boolean checkNotModified(String str);

    boolean checkNotModified(String str, long j);

    String getDescription(boolean z);
}
