package org.aspectj.weaver.tools.cache;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/CacheBacking.class */
public interface CacheBacking {
    String[] getKeys(String str);

    void remove(CachedClassReference cachedClassReference);

    void clear();

    CachedClassEntry get(CachedClassReference cachedClassReference, byte[] bArr);

    void put(CachedClassEntry cachedClassEntry, byte[] bArr);
}
