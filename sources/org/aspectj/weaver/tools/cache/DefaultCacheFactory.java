package org.aspectj.weaver.tools.cache;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/DefaultCacheFactory.class */
public class DefaultCacheFactory implements CacheFactory {
    @Override // org.aspectj.weaver.tools.cache.CacheFactory
    public CacheKeyResolver createResolver() {
        return new DefaultCacheKeyResolver();
    }

    @Override // org.aspectj.weaver.tools.cache.CacheFactory
    public CacheBacking createBacking(String scope) {
        return DefaultFileCacheBacking.createBacking(scope);
    }
}
