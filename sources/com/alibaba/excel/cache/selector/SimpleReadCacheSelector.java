package com.alibaba.excel.cache.selector;

import com.alibaba.excel.cache.Ehcache;
import com.alibaba.excel.cache.MapCache;
import com.alibaba.excel.cache.ReadCache;
import java.io.IOException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/cache/selector/SimpleReadCacheSelector.class */
public class SimpleReadCacheSelector implements ReadCacheSelector {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) SimpleReadCacheSelector.class);
    private static final long B2M = 1000000;
    private static final int DEFAULT_MAX_USE_MAP_CACHE_SIZE = 5;
    private static final int DEFAULT_MAX_EHCACHE_ACTIVATE_SIZE = 20;
    private long maxUseMapCacheSize;
    private int maxCacheActivateSize;

    public SimpleReadCacheSelector() {
        this(5L, 20);
    }

    public SimpleReadCacheSelector(long maxUseMapCacheSize, int maxCacheActivateSize) {
        if (maxUseMapCacheSize <= 0) {
            this.maxUseMapCacheSize = 5L;
        } else {
            this.maxUseMapCacheSize = maxUseMapCacheSize;
        }
        if (maxCacheActivateSize <= 0) {
            this.maxCacheActivateSize = 20;
        } else {
            this.maxCacheActivateSize = maxCacheActivateSize;
        }
    }

    @Override // com.alibaba.excel.cache.selector.ReadCacheSelector
    public ReadCache readCache(PackagePart sharedStringsTablePackagePart) {
        long size = sharedStringsTablePackagePart.getSize();
        if (size < 0) {
            try {
                size = sharedStringsTablePackagePart.getInputStream().available();
            } catch (IOException e) {
                LOGGER.warn("Unable to get file size, default used MapCache");
                return new MapCache();
            }
        }
        if (size < this.maxUseMapCacheSize * 1000000) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Use map cache.size:{}", Long.valueOf(size));
            }
            return new MapCache();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Use ehcache.size:{}", Long.valueOf(size));
        }
        return new Ehcache(this.maxCacheActivateSize);
    }
}
