package org.springframework.web;

import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/HttpRequestMethodNotSupportedException.class */
public class HttpRequestMethodNotSupportedException extends ServletException {
    private String method;
    private String[] supportedMethods;

    public HttpRequestMethodNotSupportedException(String method) {
        this(method, (String[]) null);
    }

    public HttpRequestMethodNotSupportedException(String method, String msg) {
        this(method, null, msg);
    }

    public HttpRequestMethodNotSupportedException(String method, Collection<String> supportedMethods) {
        this(method, StringUtils.toStringArray(supportedMethods));
    }

    public HttpRequestMethodNotSupportedException(String method, String[] supportedMethods) {
        this(method, supportedMethods, "Request method '" + method + "' not supported");
    }

    public HttpRequestMethodNotSupportedException(String method, String[] supportedMethods, String msg) {
        super(msg);
        this.method = method;
        this.supportedMethods = supportedMethods;
    }

    public String getMethod() {
        return this.method;
    }

    public String[] getSupportedMethods() {
        return this.supportedMethods;
    }

    public Set<HttpMethod> getSupportedHttpMethods() {
        if (this.supportedMethods == null) {
            return null;
        }
        List<HttpMethod> supportedMethods = new LinkedList<>();
        for (String value : this.supportedMethods) {
            HttpMethod resolved = HttpMethod.resolve(value);
            if (resolved != null) {
                supportedMethods.add(resolved);
            }
        }
        return EnumSet.copyOf((Collection) supportedMethods);
    }
}
