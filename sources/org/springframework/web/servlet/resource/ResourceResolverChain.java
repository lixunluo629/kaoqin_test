package org.springframework.web.servlet.resource;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/ResourceResolverChain.class */
public interface ResourceResolverChain {
    Resource resolveResource(HttpServletRequest httpServletRequest, String str, List<? extends Resource> list);

    String resolveUrlPath(String str, List<? extends Resource> list);
}
