package org.springframework.web.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/WebUtils.class */
public abstract class WebUtils {
    public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
    public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
    public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
    public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
    public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";
    public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
    public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
    public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
    public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
    public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";
    public static final String ERROR_STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";
    public static final String ERROR_EXCEPTION_TYPE_ATTRIBUTE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE_ATTRIBUTE = "javax.servlet.error.message";
    public static final String ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
    public static final String ERROR_REQUEST_URI_ATTRIBUTE = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME_ATTRIBUTE = "javax.servlet.error.servlet_name";
    public static final String CONTENT_TYPE_CHARSET_PREFIX = ";charset=";
    public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
    public static final String TEMP_DIR_CONTEXT_ATTRIBUTE = "javax.servlet.context.tempdir";
    public static final String HTML_ESCAPE_CONTEXT_PARAM = "defaultHtmlEscape";
    public static final String RESPONSE_ENCODED_HTML_ESCAPE_CONTEXT_PARAM = "responseEncodedHtmlEscape";
    public static final String WEB_APP_ROOT_KEY_PARAM = "webAppRootKey";
    public static final String DEFAULT_WEB_APP_ROOT_KEY = "webapp.root";
    public static final String[] SUBMIT_IMAGE_SUFFIXES = {".x", ".y"};
    public static final String SESSION_MUTEX_ATTRIBUTE = WebUtils.class.getName() + ".MUTEX";

    public static void setWebAppRootSystemProperty(ServletContext servletContext) throws IllegalStateException {
        Assert.notNull(servletContext, "ServletContext must not be null");
        String root = servletContext.getRealPath("/");
        if (root == null) {
            throw new IllegalStateException("Cannot set web app root system property when WAR file is not expanded");
        }
        String param = servletContext.getInitParameter(WEB_APP_ROOT_KEY_PARAM);
        String key = param != null ? param : DEFAULT_WEB_APP_ROOT_KEY;
        String oldValue = System.getProperty(key);
        if (oldValue != null && !StringUtils.pathEquals(oldValue, root)) {
            throw new IllegalStateException("Web app root system property already set to different value: '" + key + "' = [" + oldValue + "] instead of [" + root + "] - Choose unique values for the 'webAppRootKey' context-param in your web.xml files!");
        }
        System.setProperty(key, root);
        servletContext.log("Set web app root system property: '" + key + "' = [" + root + "]");
    }

    public static void removeWebAppRootSystemProperty(ServletContext servletContext) {
        Assert.notNull(servletContext, "ServletContext must not be null");
        String param = servletContext.getInitParameter(WEB_APP_ROOT_KEY_PARAM);
        String key = param != null ? param : DEFAULT_WEB_APP_ROOT_KEY;
        System.getProperties().remove(key);
    }

    @Deprecated
    public static boolean isDefaultHtmlEscape(ServletContext servletContext) {
        if (servletContext == null) {
            return false;
        }
        String param = servletContext.getInitParameter(HTML_ESCAPE_CONTEXT_PARAM);
        return Boolean.valueOf(param).booleanValue();
    }

    public static Boolean getDefaultHtmlEscape(ServletContext servletContext) {
        if (servletContext == null) {
            return null;
        }
        String param = servletContext.getInitParameter(HTML_ESCAPE_CONTEXT_PARAM);
        if (StringUtils.hasText(param)) {
            return Boolean.valueOf(param);
        }
        return null;
    }

    public static Boolean getResponseEncodedHtmlEscape(ServletContext servletContext) {
        if (servletContext == null) {
            return null;
        }
        String param = servletContext.getInitParameter(RESPONSE_ENCODED_HTML_ESCAPE_CONTEXT_PARAM);
        if (StringUtils.hasText(param)) {
            return Boolean.valueOf(param);
        }
        return null;
    }

    public static File getTempDir(ServletContext servletContext) {
        Assert.notNull(servletContext, "ServletContext must not be null");
        return (File) servletContext.getAttribute("javax.servlet.context.tempdir");
    }

    public static String getRealPath(ServletContext servletContext, String path) throws FileNotFoundException {
        Assert.notNull(servletContext, "ServletContext must not be null");
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        String realPath = servletContext.getRealPath(path);
        if (realPath == null) {
            throw new FileNotFoundException("ServletContext resource [" + path + "] cannot be resolved to absolute file path - web application archive not expanded?");
        }
        return realPath;
    }

    public static String getSessionId(HttpServletRequest request) {
        Assert.notNull(request, "Request must not be null");
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getId();
        }
        return null;
    }

    public static Object getSessionAttribute(HttpServletRequest request, String name) {
        Assert.notNull(request, "Request must not be null");
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(name);
        }
        return null;
    }

    public static Object getRequiredSessionAttribute(HttpServletRequest request, String name) throws IllegalStateException {
        Object attr = getSessionAttribute(request, name);
        if (attr == null) {
            throw new IllegalStateException("No session attribute '" + name + "' found");
        }
        return attr;
    }

    public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
        Assert.notNull(request, "Request must not be null");
        if (value != null) {
            request.getSession().setAttribute(name, value);
            return;
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(name);
        }
    }

    @Deprecated
    public static Object getOrCreateSessionAttribute(HttpSession session, String name, Class<?> clazz) throws IllegalAccessException, InstantiationException, IllegalArgumentException {
        Assert.notNull(session, "Session must not be null");
        Object sessionObject = session.getAttribute(name);
        if (sessionObject == null) {
            try {
                sessionObject = clazz.newInstance();
                session.setAttribute(name, sessionObject);
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException("Could not access default constructor of class [" + clazz.getName() + "] for session attribute '" + name + "': " + ex.getMessage());
            } catch (InstantiationException ex2) {
                throw new IllegalArgumentException("Could not instantiate class [" + clazz.getName() + "] for session attribute '" + name + "': " + ex2.getMessage());
            }
        }
        return sessionObject;
    }

    public static Object getSessionMutex(HttpSession session) {
        Assert.notNull(session, "Session must not be null");
        Object mutex = session.getAttribute(SESSION_MUTEX_ATTRIBUTE);
        if (mutex == null) {
            mutex = session;
        }
        return mutex;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T getNativeRequest(ServletRequest servletRequest, Class<T> cls) {
        if (cls != null) {
            if (cls.isInstance(servletRequest)) {
                return servletRequest;
            }
            if (servletRequest instanceof ServletRequestWrapper) {
                return (T) getNativeRequest(((ServletRequestWrapper) servletRequest).getRequest(), cls);
            }
            return null;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T getNativeResponse(ServletResponse servletResponse, Class<T> cls) {
        if (cls != null) {
            if (cls.isInstance(servletResponse)) {
                return servletResponse;
            }
            if (servletResponse instanceof ServletResponseWrapper) {
                return (T) getNativeResponse(((ServletResponseWrapper) servletResponse).getResponse(), cls);
            }
            return null;
        }
        return null;
    }

    public static boolean isIncludeRequest(ServletRequest request) {
        return request.getAttribute("javax.servlet.include.request_uri") != null;
    }

    public static void exposeErrorRequestAttributes(HttpServletRequest request, Throwable ex, String servletName) {
        exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.status_code", 200);
        exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.exception_type", ex.getClass());
        exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.message", ex.getMessage());
        exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.exception", ex);
        exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.request_uri", request.getRequestURI());
        exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.servlet_name", servletName);
    }

    private static void exposeRequestAttributeIfNotPresent(ServletRequest request, String name, Object value) {
        if (request.getAttribute(name) == null) {
            request.setAttribute(name, value);
        }
    }

    public static void clearErrorRequestAttributes(HttpServletRequest request) {
        request.removeAttribute("javax.servlet.error.status_code");
        request.removeAttribute("javax.servlet.error.exception_type");
        request.removeAttribute("javax.servlet.error.message");
        request.removeAttribute("javax.servlet.error.exception");
        request.removeAttribute("javax.servlet.error.request_uri");
        request.removeAttribute("javax.servlet.error.servlet_name");
    }

    @Deprecated
    public static void exposeRequestAttributes(ServletRequest request, Map<String, ?> attributes) {
        Assert.notNull(request, "Request must not be null");
        Assert.notNull(attributes, "Attributes Map must not be null");
        for (Map.Entry<String, ?> entry : attributes.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Assert.notNull(request, "Request must not be null");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
            return null;
        }
        return null;
    }

    public static boolean hasSubmitParameter(ServletRequest request, String name) {
        Assert.notNull(request, "Request must not be null");
        if (request.getParameter(name) != null) {
            return true;
        }
        for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
            if (request.getParameter(name + suffix) != null) {
                return true;
            }
        }
        return false;
    }

    public static String findParameterValue(ServletRequest request, String name) {
        return findParameterValue((Map<String, ?>) request.getParameterMap(), name);
    }

    public static String findParameterValue(Map<String, ?> parameters, String name) {
        Object value = parameters.get(name);
        if (value instanceof String[]) {
            String[] values = (String[]) value;
            if (values.length > 0) {
                return values[0];
            }
            return null;
        }
        if (value != null) {
            return value.toString();
        }
        String prefix = name + "_";
        for (String paramName : parameters.keySet()) {
            if (paramName.startsWith(prefix)) {
                for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
                    if (paramName.endsWith(suffix)) {
                        return paramName.substring(prefix.length(), paramName.length() - suffix.length());
                    }
                }
                return paramName.substring(prefix.length());
            }
        }
        return null;
    }

    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if (values != null && values.length != 0) {
                    if (values.length > 1) {
                        params.put(unprefixed, values);
                    } else {
                        params.put(unprefixed, values[0]);
                    }
                }
            }
        }
        return params;
    }

    @Deprecated
    public static int getTargetPage(ServletRequest request, String paramPrefix, int currentPage) {
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.startsWith(paramPrefix)) {
                for (int i = 0; i < SUBMIT_IMAGE_SUFFIXES.length; i++) {
                    String suffix = SUBMIT_IMAGE_SUFFIXES[i];
                    if (paramName.endsWith(suffix)) {
                        paramName = paramName.substring(0, paramName.length() - suffix.length());
                    }
                }
                return Integer.parseInt(paramName.substring(paramPrefix.length()));
            }
        }
        return currentPage;
    }

    @Deprecated
    public static String extractFilenameFromUrlPath(String urlPath) {
        String filename = extractFullFilenameFromUrlPath(urlPath);
        int dotIndex = filename.lastIndexOf(46);
        if (dotIndex != -1) {
            filename = filename.substring(0, dotIndex);
        }
        return filename;
    }

    @Deprecated
    public static String extractFullFilenameFromUrlPath(String urlPath) {
        int end = urlPath.indexOf(63);
        if (end == -1) {
            end = urlPath.indexOf(35);
            if (end == -1) {
                end = urlPath.length();
            }
        }
        int begin = urlPath.lastIndexOf(47, end) + 1;
        int paramIndex = urlPath.indexOf(59, begin);
        return urlPath.substring(begin, (paramIndex == -1 || paramIndex >= end) ? end : paramIndex);
    }

    public static MultiValueMap<String, String> parseMatrixVariables(String matrixVariables) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        if (!StringUtils.hasText(matrixVariables)) {
            return result;
        }
        StringTokenizer pairs = new StringTokenizer(matrixVariables, ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        while (pairs.hasMoreTokens()) {
            String pair = pairs.nextToken();
            int index = pair.indexOf(61);
            if (index != -1) {
                String name = pair.substring(0, index);
                String rawValue = pair.substring(index + 1);
                for (String value : StringUtils.commaDelimitedListToStringArray(rawValue)) {
                    result.add(name, value);
                }
            } else {
                result.add(pair, "");
            }
        }
        return result;
    }

    public static boolean isValidOrigin(HttpRequest request, Collection<String> allowedOrigins) {
        Assert.notNull(request, "Request must not be null");
        Assert.notNull(allowedOrigins, "Allowed origins must not be null");
        String origin = request.getHeaders().getOrigin();
        if (origin == null || allowedOrigins.contains("*")) {
            return true;
        }
        if (CollectionUtils.isEmpty(allowedOrigins)) {
            return isSameOrigin(request);
        }
        return allowedOrigins.contains(origin);
    }

    public static boolean isSameOrigin(HttpRequest request) {
        UriComponentsBuilder urlBuilder;
        String origin = request.getHeaders().getOrigin();
        if (origin == null) {
            return true;
        }
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            urlBuilder = new UriComponentsBuilder().scheme(servletRequest.getScheme()).host(servletRequest.getServerName()).port(servletRequest.getServerPort()).adaptFromForwardedHeaders(request.getHeaders());
        } else {
            urlBuilder = UriComponentsBuilder.fromHttpRequest(request);
        }
        UriComponents actualUrl = urlBuilder.build();
        UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
        return ObjectUtils.nullSafeEquals(actualUrl.getHost(), originUrl.getHost()) && getPort(actualUrl) == getPort(originUrl);
    }

    private static int getPort(UriComponents uri) {
        int port = uri.getPort();
        if (port == -1) {
            if ("http".equals(uri.getScheme()) || "ws".equals(uri.getScheme())) {
                port = 80;
            } else if ("https".equals(uri.getScheme()) || "wss".equals(uri.getScheme())) {
                port = 443;
            }
        }
        return port;
    }
}
