package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/MultipartResolver.class */
public interface MultipartResolver {
    boolean isMultipart(HttpServletRequest httpServletRequest);

    MultipartHttpServletRequest resolveMultipart(HttpServletRequest httpServletRequest) throws MultipartException;

    void cleanupMultipart(MultipartHttpServletRequest multipartHttpServletRequest);
}
