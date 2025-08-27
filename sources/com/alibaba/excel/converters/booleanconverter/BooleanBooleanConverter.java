package com.alibaba.excel.converters.booleanconverter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/booleanconverter/BooleanBooleanConverter.class */
public class BooleanBooleanConverter implements Converter<Boolean> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return Boolean.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.BOOLEAN;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public Boolean convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return cellData.getBooleanValue();
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(Boolean value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new CellData(value);
    }
}
