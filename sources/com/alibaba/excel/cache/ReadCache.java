package com.alibaba.excel.cache;

import com.alibaba.excel.context.AnalysisContext;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/cache/ReadCache.class */
public interface ReadCache {
    void init(AnalysisContext analysisContext);

    void put(String str);

    String get(Integer num);

    void putFinished();

    void destroy();
}
