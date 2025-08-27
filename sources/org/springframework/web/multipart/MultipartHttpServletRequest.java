package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/MultipartHttpServletRequest.class */
public interface MultipartHttpServletRequest extends HttpServletRequest, MultipartRequest {
    HttpMethod getRequestMethod();

    HttpHeaders getRequestHeaders();

    HttpHeaders getMultipartHeaders(String str);
}
