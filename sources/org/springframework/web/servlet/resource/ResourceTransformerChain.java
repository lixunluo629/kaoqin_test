package org.springframework.web.servlet.resource;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/ResourceTransformerChain.class */
public interface ResourceTransformerChain {
    ResourceResolverChain getResolverChain();

    Resource transform(HttpServletRequest httpServletRequest, Resource resource) throws IOException;
}
