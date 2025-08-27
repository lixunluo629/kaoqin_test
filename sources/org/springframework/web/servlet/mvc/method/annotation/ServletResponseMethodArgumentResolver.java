package org.springframework.web.servlet.mvc.method.annotation;

import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ServletResponseMethodArgumentResolver.class */
public class ServletResponseMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return ServletResponse.class.isAssignableFrom(paramType) || OutputStream.class.isAssignableFrom(paramType) || Writer.class.isAssignableFrom(paramType);
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (mavContainer != null) {
            mavContainer.setRequestHandled(true);
        }
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse(HttpServletResponse.class);
        Class<?> paramType = parameter.getParameterType();
        if (ServletResponse.class.isAssignableFrom(paramType)) {
            Object nativeResponse = webRequest.getNativeResponse(paramType);
            if (nativeResponse == null) {
                throw new IllegalStateException("Current response is not of type [" + paramType.getName() + "]: " + response);
            }
            return nativeResponse;
        }
        if (OutputStream.class.isAssignableFrom(paramType)) {
            return response.getOutputStream();
        }
        if (Writer.class.isAssignableFrom(paramType)) {
            return response.getWriter();
        }
        Method method = parameter.getMethod();
        throw new UnsupportedOperationException("Unknown parameter type: " + paramType + " in method: " + method);
    }
}
