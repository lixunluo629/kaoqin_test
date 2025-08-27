package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.web.context.request.async.DeferredResult;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/DeferredResultAdapter.class */
public interface DeferredResultAdapter {
    DeferredResult<?> adaptToDeferredResult(Object obj);
}
