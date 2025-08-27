package org.apache.ibatis.builder;

import org.apache.ibatis.cache.Cache;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/CacheRefResolver.class */
public class CacheRefResolver {
    private final MapperBuilderAssistant assistant;
    private final String cacheRefNamespace;

    public CacheRefResolver(MapperBuilderAssistant assistant, String cacheRefNamespace) {
        this.assistant = assistant;
        this.cacheRefNamespace = cacheRefNamespace;
    }

    public Cache resolveCacheRef() {
        return this.assistant.useCacheRef(this.cacheRefNamespace);
    }
}
