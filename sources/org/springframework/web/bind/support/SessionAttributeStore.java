package org.springframework.web.bind.support;

import org.springframework.web.context.request.WebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/support/SessionAttributeStore.class */
public interface SessionAttributeStore {
    void storeAttribute(WebRequest webRequest, String str, Object obj);

    Object retrieveAttribute(WebRequest webRequest, String str);

    void cleanupAttribute(WebRequest webRequest, String str);
}
