package com.alibaba.excel.cache;

import com.alibaba.excel.context.AnalysisContext;
import java.util.HashMap;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/cache/MapCache.class */
public class MapCache implements ReadCache {
    private Map<Integer, String> cache = new HashMap();
    private int index = 0;

    @Override // com.alibaba.excel.cache.ReadCache
    public void init(AnalysisContext analysisContext) {
    }

    @Override // com.alibaba.excel.cache.ReadCache
    public void put(String value) {
        Map<Integer, String> map = this.cache;
        int i = this.index;
        this.index = i + 1;
        map.put(Integer.valueOf(i), value);
    }

    @Override // com.alibaba.excel.cache.ReadCache
    public String get(Integer key) {
        if (key == null || key.intValue() < 0) {
            return null;
        }
        return this.cache.get(key);
    }

    @Override // com.alibaba.excel.cache.ReadCache
    public void putFinished() {
    }

    @Override // com.alibaba.excel.cache.ReadCache
    public void destroy() {
    }
}
