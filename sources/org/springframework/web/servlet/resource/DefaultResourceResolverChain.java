package org.springframework.web.servlet.resource;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/DefaultResourceResolverChain.class */
class DefaultResourceResolverChain implements ResourceResolverChain {
    private final List<ResourceResolver> resolvers = new ArrayList();
    private int index = -1;

    public DefaultResourceResolverChain(List<? extends ResourceResolver> resolvers) {
        if (resolvers != null) {
            this.resolvers.addAll(resolvers);
        }
    }

    @Override // org.springframework.web.servlet.resource.ResourceResolverChain
    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations) {
        ResourceResolver resolver = getNext();
        if (resolver == null) {
            return null;
        }
        try {
            Resource resourceResolveResource = resolver.resolveResource(request, requestPath, locations, this);
            this.index--;
            return resourceResolveResource;
        } catch (Throwable th) {
            this.index--;
            throw th;
        }
    }

    @Override // org.springframework.web.servlet.resource.ResourceResolverChain
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations) {
        ResourceResolver resolver = getNext();
        if (resolver == null) {
            return null;
        }
        try {
            String strResolveUrlPath = resolver.resolveUrlPath(resourcePath, locations, this);
            this.index--;
            return strResolveUrlPath;
        } catch (Throwable th) {
            this.index--;
            throw th;
        }
    }

    private ResourceResolver getNext() {
        Assert.state(this.index <= this.resolvers.size(), "Current index exceeds the number of configured ResourceResolvers");
        if (this.index == this.resolvers.size() - 1) {
            return null;
        }
        this.index++;
        return this.resolvers.get(this.index);
    }
}
