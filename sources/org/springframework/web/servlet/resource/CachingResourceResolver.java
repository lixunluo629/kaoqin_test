package org.springframework.web.servlet.resource;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/CachingResourceResolver.class */
public class CachingResourceResolver extends AbstractResourceResolver {
    public static final String RESOLVED_RESOURCE_CACHE_KEY_PREFIX = "resolvedResource:";
    public static final String RESOLVED_URL_PATH_CACHE_KEY_PREFIX = "resolvedUrlPath:";
    private final Cache cache;

    public CachingResourceResolver(CacheManager cacheManager, String cacheName) {
        this(cacheManager.getCache(cacheName));
    }

    public CachingResourceResolver(Cache cache) {
        Assert.notNull(cache, "Cache is required");
        this.cache = cache;
    }

    public Cache getCache() {
        return this.cache;
    }

    @Override // org.springframework.web.servlet.resource.AbstractResourceResolver
    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        String key = computeKey(request, requestPath);
        Resource resource = (Resource) this.cache.get(key, Resource.class);
        if (resource != null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Found match: " + resource);
            }
            return resource;
        }
        Resource resource2 = chain.resolveResource(request, requestPath, locations);
        if (resource2 != null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Putting resolved resource in cache: " + resource2);
            }
            this.cache.put(key, resource2);
        }
        return resource2;
    }

    protected String computeKey(HttpServletRequest request, String requestPath) {
        String encoding;
        StringBuilder key = new StringBuilder(RESOLVED_RESOURCE_CACHE_KEY_PREFIX);
        key.append(requestPath);
        if (request != null && (encoding = request.getHeader("Accept-Encoding")) != null && encoding.contains("gzip")) {
            key.append("+encoding=gzip");
        }
        return key.toString();
    }

    @Override // org.springframework.web.servlet.resource.AbstractResourceResolver
    protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        String key = RESOLVED_URL_PATH_CACHE_KEY_PREFIX + resourceUrlPath;
        String resolvedUrlPath = (String) this.cache.get(key, String.class);
        if (resolvedUrlPath != null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Found match: \"" + resolvedUrlPath + SymbolConstants.QUOTES_SYMBOL);
            }
            return resolvedUrlPath;
        }
        String resolvedUrlPath2 = chain.resolveUrlPath(resourceUrlPath, locations);
        if (resolvedUrlPath2 != null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Putting resolved resource URL path in cache: \"" + resolvedUrlPath2 + SymbolConstants.QUOTES_SYMBOL);
            }
            this.cache.put(key, resolvedUrlPath2);
        }
        return resolvedUrlPath2;
    }
}
