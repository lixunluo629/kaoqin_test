package com.alibaba.excel.read.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.Listener;
import com.alibaba.excel.metadata.CellData;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/listener/ReadListener.class */
public interface ReadListener<T> extends Listener {
    void onException(Exception exc, AnalysisContext analysisContext) throws Exception;

    void invokeHead(Map<Integer, CellData> map, AnalysisContext analysisContext);

    void invoke(T t, AnalysisContext analysisContext);

    void doAfterAllAnalysed(AnalysisContext analysisContext);

    boolean hasNext(AnalysisContext analysisContext);
}
