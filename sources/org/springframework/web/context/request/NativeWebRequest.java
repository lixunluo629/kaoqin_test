package org.springframework.web.context.request;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/NativeWebRequest.class */
public interface NativeWebRequest extends WebRequest {
    Object getNativeRequest();

    Object getNativeResponse();

    <T> T getNativeRequest(Class<T> cls);

    <T> T getNativeResponse(Class<T> cls);
}
