package javax.servlet.http;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/HttpServletRequest.class */
public interface HttpServletRequest extends ServletRequest {
    public static final String BASIC_AUTH = "BASIC";
    public static final String FORM_AUTH = "FORM";
    public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";
    public static final String DIGEST_AUTH = "DIGEST";

    String getAuthType();

    Cookie[] getCookies();

    long getDateHeader(String str);

    String getHeader(String str);

    Enumeration<String> getHeaders(String str);

    Enumeration<String> getHeaderNames();

    int getIntHeader(String str);

    String getMethod();

    String getPathInfo();

    String getPathTranslated();

    String getContextPath();

    String getQueryString();

    String getRemoteUser();

    boolean isUserInRole(String str);

    Principal getUserPrincipal();

    String getRequestedSessionId();

    String getRequestURI();

    StringBuffer getRequestURL();

    String getServletPath();

    HttpSession getSession(boolean z);

    HttpSession getSession();

    String changeSessionId();

    boolean isRequestedSessionIdValid();

    boolean isRequestedSessionIdFromCookie();

    boolean isRequestedSessionIdFromURL();

    boolean isRequestedSessionIdFromUrl();

    boolean authenticate(HttpServletResponse httpServletResponse) throws ServletException, IOException;

    void login(String str, String str2) throws ServletException;

    void logout() throws ServletException;

    Collection<Part> getParts() throws ServletException, IOException;

    Part getPart(String str) throws ServletException, IOException;

    <T extends HttpUpgradeHandler> T upgrade(Class<T> cls) throws ServletException, IOException;
}
