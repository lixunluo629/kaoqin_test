package org.springframework.core.type.classreading;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/type/classreading/CachingMetadataReaderFactory.class */
public class CachingMetadataReaderFactory extends SimpleMetadataReaderFactory {
    public static final int DEFAULT_CACHE_LIMIT = 256;
    private volatile int cacheLimit;
    private final Map<Resource, MetadataReader> metadataReaderCache;

    public CachingMetadataReaderFactory() {
        this.cacheLimit = 256;
        this.metadataReaderCache = new LinkedHashMap<Resource, MetadataReader>(256, 0.75f, true) { // from class: org.springframework.core.type.classreading.CachingMetadataReaderFactory.1
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<Resource, MetadataReader> eldest) {
                return size() > CachingMetadataReaderFactory.this.getCacheLimit();
            }
        };
    }

    public CachingMetadataReaderFactory(ResourceLoader resourceLoader) {
        super(resourceLoader);
        this.cacheLimit = 256;
        this.metadataReaderCache = new LinkedHashMap<Resource, MetadataReader>(256, 0.75f, true) { // from class: org.springframework.core.type.classreading.CachingMetadataReaderFactory.1
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<Resource, MetadataReader> eldest) {
                return size() > CachingMetadataReaderFactory.this.getCacheLimit();
            }
        };
    }

    public CachingMetadataReaderFactory(ClassLoader classLoader) {
        super(classLoader);
        this.cacheLimit = 256;
        this.metadataReaderCache = new LinkedHashMap<Resource, MetadataReader>(256, 0.75f, true) { // from class: org.springframework.core.type.classreading.CachingMetadataReaderFactory.1
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<Resource, MetadataReader> eldest) {
                return size() > CachingMetadataReaderFactory.this.getCacheLimit();
            }
        };
    }

    public void setCacheLimit(int cacheLimit) {
        this.cacheLimit = cacheLimit;
    }

    public int getCacheLimit() {
        return this.cacheLimit;
    }

    @Override // org.springframework.core.type.classreading.SimpleMetadataReaderFactory, org.springframework.core.type.classreading.MetadataReaderFactory
    public MetadataReader getMetadataReader(Resource resource) throws IOException {
        MetadataReader metadataReader;
        if (getCacheLimit() <= 0) {
            return super.getMetadataReader(resource);
        }
        synchronized (this.metadataReaderCache) {
            MetadataReader metadataReader2 = this.metadataReaderCache.get(resource);
            if (metadataReader2 == null) {
                metadataReader2 = super.getMetadataReader(resource);
                this.metadataReaderCache.put(resource, metadataReader2);
            }
            metadataReader = metadataReader2;
        }
        return metadataReader;
    }

    public void clearCache() {
        synchronized (this.metadataReaderCache) {
            this.metadataReaderCache.clear();
        }
    }
}
