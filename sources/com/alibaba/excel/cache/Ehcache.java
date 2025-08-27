package com.alibaba.excel.cache;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.FileUtils;
import com.mysql.jdbc.MysqlErrorNumbers;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import org.apache.tomcat.jni.Time;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/cache/Ehcache.class */
public class Ehcache implements ReadCache {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) Ehcache.class);
    private static final int BATCH_COUNT = 1000;
    private static final int DEBUG_WRITE_SIZE = 1000000;
    private static final int DEBUG_CACHE_MISS_SIZE = 1000;
    private static CacheManager fileCacheManager;
    private static CacheConfiguration<Integer, HashMap> fileCacheConfiguration;
    private static CacheManager activeCacheManager;
    private CacheConfiguration<Integer, HashMap> activeCacheConfiguration;
    private Cache<Integer, HashMap> fileCache;
    private Cache<Integer, HashMap> activeCache;
    private String cacheAlias;
    private int index = 0;
    private HashMap<Integer, String> dataMap = new HashMap<>(MysqlErrorNumbers.ER_SP_CANT_ALTER);
    private int cacheMiss = 0;

    static {
        File cacheFile = FileUtils.createCacheTmpFile();
        fileCacheManager = CacheManagerBuilder.newCacheManagerBuilder().with(CacheManagerBuilder.persistence(cacheFile)).build(true);
        activeCacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        fileCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, HashMap.class, ResourcePoolsBuilder.newResourcePoolsBuilder().disk(10L, MemoryUnit.GB)).withSizeOfMaxObjectGraph(Time.APR_USEC_PER_SEC).withSizeOfMaxObjectSize(10L, MemoryUnit.GB).build2();
    }

    public Ehcache(int maxCacheActivateSize) {
        this.activeCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, HashMap.class, ResourcePoolsBuilder.newResourcePoolsBuilder().heap(maxCacheActivateSize, MemoryUnit.MB)).withSizeOfMaxObjectGraph(Time.APR_USEC_PER_SEC).withSizeOfMaxObjectSize(maxCacheActivateSize, MemoryUnit.MB).build2();
    }

    @Override // com.alibaba.excel.cache.ReadCache
    public void init(AnalysisContext analysisContext) {
        this.cacheAlias = UUID.randomUUID().toString();
        this.fileCache = fileCacheManager.createCache(this.cacheAlias, fileCacheConfiguration);
        this.activeCache = activeCacheManager.createCache(this.cacheAlias, this.activeCacheConfiguration);
    }

    @Override // com.alibaba.excel.cache.ReadCache
    public void put(String value) throws CacheWritingException {
        this.dataMap.put(Integer.valueOf(this.index), value);
        if ((this.index + 1) % 1000 == 0) {
            this.fileCache.put(Integer.valueOf(this.index / 1000), this.dataMap);
            this.dataMap = new HashMap<>(MysqlErrorNumbers.ER_SP_CANT_ALTER);
        }
        this.index++;
        if (LOGGER.isDebugEnabled() && this.index % 1000000 == 0) {
            LOGGER.debug("Already put :{}", Integer.valueOf(this.index));
        }
    }

    @Override // com.alibaba.excel.cache.ReadCache
    public String get(Integer key) throws CacheLoadingException, CacheWritingException {
        if (key == null || key.intValue() < 0) {
            return null;
        }
        int route = key.intValue() / 1000;
        HashMap<Integer, String> dataMap = this.activeCache.get(Integer.valueOf(route));
        if (dataMap == null) {
            dataMap = this.fileCache.get(Integer.valueOf(route));
            this.activeCache.put(Integer.valueOf(route), dataMap);
            if (LOGGER.isDebugEnabled()) {
                int i = this.cacheMiss;
                this.cacheMiss = i + 1;
                if (i % 1000 == 0) {
                    LOGGER.debug("Cache misses count:{}", Integer.valueOf(this.cacheMiss));
                }
            }
        }
        return dataMap.get(key);
    }

    @Override // com.alibaba.excel.cache.ReadCache
    public void putFinished() throws CacheWritingException {
        if (CollectionUtils.isEmpty(this.dataMap)) {
            return;
        }
        this.fileCache.put(Integer.valueOf(this.index / 1000), this.dataMap);
    }

    @Override // com.alibaba.excel.cache.ReadCache
    public void destroy() {
        fileCacheManager.removeCache(this.cacheAlias);
        activeCacheManager.removeCache(this.cacheAlias);
    }
}
