package com.alibaba.excel.converters.string;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/string/StringErrorConverter.class */
public class StringErrorConverter implements Converter<String> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.ERROR;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return cellData.getStringValue();
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new CellData(CellDataTypeEnum.ERROR, value);
    }
}
