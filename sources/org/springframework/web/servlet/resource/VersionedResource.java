package org.springframework.web.servlet.resource;

import org.springframework.core.io.Resource;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/VersionedResource.class */
public interface VersionedResource extends Resource {
    String getVersion();
}
