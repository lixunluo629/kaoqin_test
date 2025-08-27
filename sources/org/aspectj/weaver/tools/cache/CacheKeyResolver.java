package org.aspectj.weaver.tools.cache;

import java.util.List;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/CacheKeyResolver.class */
public interface CacheKeyResolver {
    CachedClassReference generatedKey(String str);

    CachedClassReference weavedKey(String str, byte[] bArr);

    String keyToClass(String str);

    String createClassLoaderScope(ClassLoader classLoader, List<String> list);

    String getGeneratedRegex();

    String getWeavedRegex();
}
