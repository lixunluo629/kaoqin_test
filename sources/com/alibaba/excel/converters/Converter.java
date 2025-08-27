package com.alibaba.excel.converters;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/Converter.class */
public interface Converter<T> {
    Class supportJavaTypeKey();

    CellDataTypeEnum supportExcelTypeKey();

    T convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception;

    CellData convertToExcelData(T t, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception;
}
