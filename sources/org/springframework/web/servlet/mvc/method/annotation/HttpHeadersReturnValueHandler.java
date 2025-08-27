package org.springframework.web.servlet.mvc.method.annotation;

import javax.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/HttpHeadersReturnValueHandler.class */
public class HttpHeadersReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public boolean supportsReturnType(MethodParameter returnType) {
        return HttpHeaders.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        Assert.state(returnValue instanceof HttpHeaders, "HttpHeaders expected");
        HttpHeaders headers = (HttpHeaders) returnValue;
        if (!headers.isEmpty()) {
            HttpServletResponse servletResponse = (HttpServletResponse) webRequest.getNativeResponse(HttpServletResponse.class);
            ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(servletResponse);
            outputMessage.getHeaders().putAll(headers);
            outputMessage.getBody();
        }
    }
}
