package org.springframework.web.servlet.support;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.UrlPathHelper;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/support/ServletUriComponentsBuilder.class */
public class ServletUriComponentsBuilder extends UriComponentsBuilder {
    private String originalPath;

    protected ServletUriComponentsBuilder() {
    }

    protected ServletUriComponentsBuilder(ServletUriComponentsBuilder other) {
        super(other);
        this.originalPath = other.originalPath;
    }

    public static ServletUriComponentsBuilder fromContextPath(HttpServletRequest request) {
        ServletUriComponentsBuilder builder = initFromRequest(request);
        String forwardedPrefix = getForwardedPrefix(request);
        builder.replacePath(forwardedPrefix != null ? forwardedPrefix : request.getContextPath());
        return builder;
    }

    public static ServletUriComponentsBuilder fromServletMapping(HttpServletRequest request) {
        ServletUriComponentsBuilder builder = fromContextPath(request);
        if (StringUtils.hasText(new UrlPathHelper().getPathWithinServletMapping(request))) {
            builder.path(request.getServletPath());
        }
        return builder;
    }

    public static ServletUriComponentsBuilder fromRequestUri(HttpServletRequest request) {
        ServletUriComponentsBuilder builder = initFromRequest(request);
        builder.initPath(getRequestUriWithForwardedPrefix(request));
        return builder;
    }

    public static ServletUriComponentsBuilder fromRequest(HttpServletRequest request) {
        ServletUriComponentsBuilder builder = initFromRequest(request);
        builder.initPath(getRequestUriWithForwardedPrefix(request));
        builder.query(request.getQueryString());
        return builder;
    }

    private static ServletUriComponentsBuilder initFromRequest(HttpServletRequest request) {
        HttpRequest httpRequest = new ServletServerHttpRequest(request);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();
        String scheme = uriComponents.getScheme();
        String host = uriComponents.getHost();
        int port = uriComponents.getPort();
        ServletUriComponentsBuilder builder = new ServletUriComponentsBuilder();
        builder.scheme(scheme);
        builder.host(host);
        if (("http".equals(scheme) && port != 80) || ("https".equals(scheme) && port != 443)) {
            builder.port(port);
        }
        return builder;
    }

    private static String getForwardedPrefix(HttpServletRequest request) {
        String prefix = null;
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if ("X-Forwarded-Prefix".equalsIgnoreCase(name)) {
                prefix = request.getHeader(name);
            }
        }
        if (prefix != null) {
            while (prefix.endsWith("/")) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }
        }
        return prefix;
    }

    private static String getRequestUriWithForwardedPrefix(HttpServletRequest request) {
        String path = request.getRequestURI();
        String forwardedPrefix = getForwardedPrefix(request);
        if (forwardedPrefix != null) {
            String contextPath = request.getContextPath();
            if (!StringUtils.isEmpty(contextPath) && !contextPath.equals("/") && path.startsWith(contextPath)) {
                path = path.substring(contextPath.length());
            }
            path = forwardedPrefix + path;
        }
        return path;
    }

    public static ServletUriComponentsBuilder fromCurrentContextPath() {
        return fromContextPath(getCurrentRequest());
    }

    public static ServletUriComponentsBuilder fromCurrentServletMapping() {
        return fromServletMapping(getCurrentRequest());
    }

    public static ServletUriComponentsBuilder fromCurrentRequestUri() {
        return fromRequestUri(getCurrentRequest());
    }

    public static ServletUriComponentsBuilder fromCurrentRequest() {
        return fromRequest(getCurrentRequest());
    }

    protected static HttpServletRequest getCurrentRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        Assert.state(attrs instanceof ServletRequestAttributes, "No current ServletRequestAttributes");
        return ((ServletRequestAttributes) attrs).getRequest();
    }

    private void initPath(String path) {
        this.originalPath = path;
        replacePath(path);
    }

    public String removePathExtension() {
        String extension = null;
        if (this.originalPath != null) {
            extension = UriUtils.extractFileExtension(this.originalPath);
            if (!StringUtils.isEmpty(extension)) {
                int end = this.originalPath.length() - (extension.length() + 1);
                replacePath(this.originalPath.substring(0, end));
            }
            this.originalPath = null;
        }
        return extension;
    }

    @Override // org.springframework.web.util.UriComponentsBuilder
    public ServletUriComponentsBuilder cloneBuilder() {
        return new ServletUriComponentsBuilder(this);
    }
}
