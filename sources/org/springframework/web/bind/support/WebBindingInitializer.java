package org.springframework.web.bind.support;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/support/WebBindingInitializer.class */
public interface WebBindingInitializer {
    void initBinder(WebDataBinder webDataBinder, WebRequest webRequest);
}
