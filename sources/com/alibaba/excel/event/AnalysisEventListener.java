package com.alibaba.excel.event;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/event/AnalysisEventListener.class */
public abstract class AnalysisEventListener<T> implements ReadListener<T> {
    @Override // com.alibaba.excel.read.listener.ReadListener
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        invokeHeadMap(ConverterUtils.convertToStringMap(headMap, context), context);
    }

    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
    }

    @Override // com.alibaba.excel.read.listener.ReadListener
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        throw exception;
    }

    @Override // com.alibaba.excel.read.listener.ReadListener
    public boolean hasNext(AnalysisContext context) {
        return true;
    }
}
