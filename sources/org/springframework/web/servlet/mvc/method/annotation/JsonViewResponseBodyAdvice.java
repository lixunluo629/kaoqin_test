package org.springframework.web.servlet.mvc.method.annotation;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/JsonViewResponseBodyAdvice.class */
public class JsonViewResponseBodyAdvice extends AbstractMappingJacksonResponseBodyAdvice {
    @Override // org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice, org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType) && returnType.hasMethodAnnotation(JsonView.class);
    }

    @Override // org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        JsonView annotation = (JsonView) returnType.getMethodAnnotation(JsonView.class);
        Class<?>[] classes = annotation.value();
        if (classes.length != 1) {
            throw new IllegalArgumentException("@JsonView only supported for response body advice with exactly 1 class argument: " + returnType);
        }
        bodyContainer.setSerializationView(classes[0]);
    }
}
