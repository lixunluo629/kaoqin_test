package com.alibaba.excel.cache.selector;

import com.alibaba.excel.cache.ReadCache;
import org.apache.poi.openxml4j.opc.PackagePart;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/cache/selector/ReadCacheSelector.class */
public interface ReadCacheSelector {
    ReadCache readCache(PackagePart packagePart);
}
