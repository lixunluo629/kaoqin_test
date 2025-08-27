package org.springframework.web.accept;

import java.util.List;
import org.springframework.http.MediaType;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/accept/MediaTypeFileExtensionResolver.class */
public interface MediaTypeFileExtensionResolver {
    List<String> resolveFileExtensions(MediaType mediaType);

    List<String> getAllFileExtensions();
}
