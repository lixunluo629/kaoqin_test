package org.springframework.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/LastModified.class */
public interface LastModified {
    long getLastModified(HttpServletRequest httpServletRequest);
}
