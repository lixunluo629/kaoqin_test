package com.alibaba.excel.event;

import com.alibaba.excel.context.AnalysisContext;
import java.util.ArrayList;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/event/SyncReadListener.class */
public class SyncReadListener extends AnalysisEventListener<Object> {
    private List<Object> list = new ArrayList();

    @Override // com.alibaba.excel.read.listener.ReadListener
    public void invoke(Object object, AnalysisContext context) {
        this.list.add(object);
    }

    @Override // com.alibaba.excel.read.listener.ReadListener
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    public List<Object> getList() {
        return this.list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }
}
