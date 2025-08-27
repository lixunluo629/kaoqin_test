package org.springframework.boot.autoconfigure.web;

import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/WebMvcRegistrationsAdapter.class */
public class WebMvcRegistrationsAdapter implements WebMvcRegistrations {
    @Override // org.springframework.boot.autoconfigure.web.WebMvcRegistrations
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return null;
    }

    @Override // org.springframework.boot.autoconfigure.web.WebMvcRegistrations
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return null;
    }

    @Override // org.springframework.boot.autoconfigure.web.WebMvcRegistrations
    public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
        return null;
    }
}
