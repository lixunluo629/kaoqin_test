package com.alibaba.excel.read.metadata;

import com.alibaba.excel.metadata.BasicParameter;
import com.alibaba.excel.read.listener.ReadListener;
import java.util.ArrayList;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/metadata/ReadBasicParameter.class */
public class ReadBasicParameter extends BasicParameter {
    private Integer headRowNumber;
    private List<ReadListener> customReadListenerList = new ArrayList();

    public Integer getHeadRowNumber() {
        return this.headRowNumber;
    }

    public void setHeadRowNumber(Integer headRowNumber) {
        this.headRowNumber = headRowNumber;
    }

    public List<ReadListener> getCustomReadListenerList() {
        return this.customReadListenerList;
    }

    public void setCustomReadListenerList(List<ReadListener> customReadListenerList) {
        this.customReadListenerList = customReadListenerList;
    }
}
