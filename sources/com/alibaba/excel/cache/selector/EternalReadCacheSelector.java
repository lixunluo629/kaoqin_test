package com.alibaba.excel.cache.selector;

import com.alibaba.excel.cache.ReadCache;
import org.apache.poi.openxml4j.opc.PackagePart;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/cache/selector/EternalReadCacheSelector.class */
public class EternalReadCacheSelector implements ReadCacheSelector {
    private ReadCache readCache;

    public EternalReadCacheSelector(ReadCache readCache) {
        this.readCache = readCache;
    }

    @Override // com.alibaba.excel.cache.selector.ReadCacheSelector
    public ReadCache readCache(PackagePart sharedStringsTablePackagePart) {
        return this.readCache;
    }
}
