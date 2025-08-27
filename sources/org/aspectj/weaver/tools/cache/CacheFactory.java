package org.aspectj.weaver.tools.cache;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/CacheFactory.class */
public interface CacheFactory {
    CacheKeyResolver createResolver();

    CacheBacking createBacking(String str);
}
