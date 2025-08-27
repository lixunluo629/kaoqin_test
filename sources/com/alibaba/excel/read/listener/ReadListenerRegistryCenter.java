package com.alibaba.excel.read.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.event.AnalysisFinishEvent;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/listener/ReadListenerRegistryCenter.class */
public interface ReadListenerRegistryCenter {
    void register(AnalysisEventListener analysisEventListener);

    void notifyEndOneRow(AnalysisFinishEvent analysisFinishEvent, AnalysisContext analysisContext);

    void notifyAfterAllAnalysed(AnalysisContext analysisContext);
}
