package org.springframework.boot.context.embedded.jetty;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.springframework.web.servlet.support.WebContentGenerator;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedErrorHandler.class */
class JettyEmbeddedErrorHandler extends ErrorHandler {
    private static final Set<String> SUPPORTED_METHODS;
    private final ErrorHandler delegate;

    static {
        Set<String> supportedMethods = new HashSet<>();
        supportedMethods.add("GET");
        supportedMethods.add(WebContentGenerator.METHOD_HEAD);
        supportedMethods.add(WebContentGenerator.METHOD_POST);
        SUPPORTED_METHODS = Collections.unmodifiableSet(supportedMethods);
    }

    JettyEmbeddedErrorHandler(ErrorHandler delegate) {
        this.delegate = delegate;
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isSupported(request.getMethod())) {
            request = new ErrorHttpServletRequest(request);
        }
        this.delegate.handle(target, baseRequest, request, response);
    }

    private boolean isSupported(String method) {
        for (String supportedMethod : SUPPORTED_METHODS) {
            if (supportedMethod.equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedErrorHandler$ErrorHttpServletRequest.class */
    private static class ErrorHttpServletRequest extends HttpServletRequestWrapper {
        private boolean simulateGetMethod;

        ErrorHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.simulateGetMethod = true;
        }

        @Override // javax.servlet.http.HttpServletRequestWrapper, javax.servlet.http.HttpServletRequest
        public String getMethod() {
            return this.simulateGetMethod ? HttpMethod.GET.toString() : super.getMethod();
        }

        @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
        public ServletContext getServletContext() {
            this.simulateGetMethod = false;
            return super.getServletContext();
        }
    }
}
