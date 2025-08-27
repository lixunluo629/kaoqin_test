package org.springframework.web.accept;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/accept/ContentNegotiationStrategy.class */
public interface ContentNegotiationStrategy {
    List<MediaType> resolveMediaTypes(NativeWebRequest nativeWebRequest) throws HttpMediaTypeNotAcceptableException;
}
