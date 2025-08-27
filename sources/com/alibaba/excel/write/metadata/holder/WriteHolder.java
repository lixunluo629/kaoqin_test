package com.alibaba.excel.write.metadata.holder;

import com.alibaba.excel.metadata.ConfigurationHolder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.property.ExcelWriteHeadProperty;
import java.util.List;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/holder/WriteHolder.class */
public interface WriteHolder extends ConfigurationHolder {
    ExcelWriteHeadProperty excelWriteHeadProperty();

    Map<Class<? extends WriteHandler>, List<WriteHandler>> writeHandlerMap();

    boolean ignore(String str, Integer num);

    boolean needHead();

    boolean automaticMergeHead();

    int relativeHeadRowIndex();
}
