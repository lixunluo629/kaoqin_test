package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.http.server.ServerHttpResponse;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitterAdapter.class */
public interface ResponseBodyEmitterAdapter {
    ResponseBodyEmitter adaptToEmitter(Object obj, ServerHttpResponse serverHttpResponse);
}
