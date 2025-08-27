package org.springframework.boot.autoconfigure.web;

import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/WebMvcRegistrations.class */
public interface WebMvcRegistrations {
    RequestMappingHandlerMapping getRequestMappingHandlerMapping();

    RequestMappingHandlerAdapter getRequestMappingHandlerAdapter();

    ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver();
}
