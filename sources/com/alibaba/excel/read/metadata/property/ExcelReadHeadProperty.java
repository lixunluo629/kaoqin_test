package com.alibaba.excel.read.metadata.property;

import com.alibaba.excel.metadata.Holder;
import com.alibaba.excel.metadata.property.ExcelHeadProperty;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/metadata/property/ExcelReadHeadProperty.class */
public class ExcelReadHeadProperty extends ExcelHeadProperty {
    public ExcelReadHeadProperty(Holder holder, Class headClazz, List<List<String>> head, Boolean convertAllFiled) {
        super(holder, headClazz, head, convertAllFiled);
    }
}
