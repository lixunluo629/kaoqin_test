package org.springframework.web.servlet.resource;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/CachingResourceTransformer.class */
public class CachingResourceTransformer implements ResourceTransformer {
    private static final Log logger = LogFactory.getLog(CachingResourceTransformer.class);
    private final Cache cache;

    public CachingResourceTransformer(CacheManager cacheManager, String cacheName) {
        this(cacheManager.getCache(cacheName));
    }

    public CachingResourceTransformer(Cache cache) {
        Assert.notNull(cache, "Cache is required");
        this.cache = cache;
    }

    public Cache getCache() {
        return this.cache;
    }

    @Override // org.springframework.web.servlet.resource.ResourceTransformer
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
        Resource transformed = (Resource) this.cache.get(resource, Resource.class);
        if (transformed != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("Found match: " + transformed);
            }
            return transformed;
        }
        Resource transformed2 = transformerChain.transform(request, resource);
        if (logger.isTraceEnabled()) {
            logger.trace("Putting transformed resource in cache: " + transformed2);
        }
        this.cache.put(resource, transformed2);
        return transformed2;
    }
}
