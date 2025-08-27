package org.springframework.web.bind.support;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/support/WebDataBinderFactory.class */
public interface WebDataBinderFactory {
    WebDataBinder createBinder(NativeWebRequest nativeWebRequest, Object obj, String str) throws Exception;
}
