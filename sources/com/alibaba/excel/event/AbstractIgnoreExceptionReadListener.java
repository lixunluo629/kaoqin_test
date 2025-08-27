package com.alibaba.excel.event;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/event/AbstractIgnoreExceptionReadListener.class */
public abstract class AbstractIgnoreExceptionReadListener<T> implements ReadListener<T> {
    @Override // com.alibaba.excel.read.listener.ReadListener
    public void onException(Exception exception, AnalysisContext context) {
    }

    @Override // com.alibaba.excel.read.listener.ReadListener
    public boolean hasNext(AnalysisContext context) {
        return true;
    }
}
