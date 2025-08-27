package com.alibaba.excel.converters.shortconverter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import java.math.BigDecimal;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/shortconverter/ShortNumberConverter.class */
public class ShortNumberConverter implements Converter<Short> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return Short.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public Short convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return Short.valueOf(cellData.getNumberValue().shortValue());
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(Short value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new CellData(BigDecimal.valueOf(value.shortValue()));
    }
}
