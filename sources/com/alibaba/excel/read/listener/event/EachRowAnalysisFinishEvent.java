package com.alibaba.excel.read.listener.event;

import com.alibaba.excel.metadata.CellData;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/listener/event/EachRowAnalysisFinishEvent.class */
public class EachRowAnalysisFinishEvent implements AnalysisFinishEvent {
    private Map<Integer, CellData> result;

    public EachRowAnalysisFinishEvent(Map<Integer, CellData> content) {
        this.result = content;
    }

    @Override // com.alibaba.excel.read.listener.event.AnalysisFinishEvent
    public Map<Integer, CellData> getAnalysisResult() {
        return this.result;
    }
}
