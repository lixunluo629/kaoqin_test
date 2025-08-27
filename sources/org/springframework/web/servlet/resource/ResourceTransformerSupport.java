package org.springframework.web.servlet.resource;

import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/ResourceTransformerSupport.class */
public abstract class ResourceTransformerSupport implements ResourceTransformer {
    private ResourceUrlProvider resourceUrlProvider;

    public void setResourceUrlProvider(ResourceUrlProvider resourceUrlProvider) {
        this.resourceUrlProvider = resourceUrlProvider;
    }

    public ResourceUrlProvider getResourceUrlProvider() {
        return this.resourceUrlProvider;
    }

    protected String resolveUrlPath(String resourcePath, HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) {
        if (resourcePath.startsWith("/")) {
            ResourceUrlProvider urlProvider = findResourceUrlProvider(request);
            if (urlProvider != null) {
                return urlProvider.getForRequestUrl(request, resourcePath);
            }
            return null;
        }
        return transformerChain.getResolverChain().resolveUrlPath(resourcePath, Collections.singletonList(resource));
    }

    private ResourceUrlProvider findResourceUrlProvider(HttpServletRequest request) {
        if (this.resourceUrlProvider != null) {
            return this.resourceUrlProvider;
        }
        return (ResourceUrlProvider) request.getAttribute(ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR);
    }
}
