package com.alibaba.excel.read.listener.event;

import com.alibaba.excel.metadata.CellData;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/listener/event/AnalysisFinishEvent.class */
public interface AnalysisFinishEvent {
    Map<Integer, CellData> getAnalysisResult();
}
