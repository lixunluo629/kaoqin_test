package org.springframework.boot.autoconfigure.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/Http401AuthenticationEntryPoint.class */
public class Http401AuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final String headerValue;

    public Http401AuthenticationEntryPoint(String headerValue) {
        this.headerValue = headerValue;
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws ServletException, IOException {
        response.setHeader("WWW-Authenticate", this.headerValue);
        response.sendError(401, authException.getMessage());
    }
}
