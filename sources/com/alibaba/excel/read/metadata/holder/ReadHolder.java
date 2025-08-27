package com.alibaba.excel.read.metadata.holder;

import com.alibaba.excel.metadata.ConfigurationHolder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.property.ExcelReadHeadProperty;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/metadata/holder/ReadHolder.class */
public interface ReadHolder extends ConfigurationHolder {
    List<ReadListener> readListenerList();

    ExcelReadHeadProperty excelReadHeadProperty();
}
