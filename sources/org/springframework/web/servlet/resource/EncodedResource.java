package org.springframework.web.servlet.resource;

import org.springframework.core.io.Resource;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/EncodedResource.class */
public interface EncodedResource extends Resource {
    String getContentEncoding();
}
