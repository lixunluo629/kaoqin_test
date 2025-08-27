package com.alibaba.excel.converters.integer;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import java.math.BigDecimal;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/integer/IntegerNumberConverter.class */
public class IntegerNumberConverter implements Converter<Integer> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return Integer.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return Integer.valueOf(cellData.getNumberValue().intValue());
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new CellData(BigDecimal.valueOf(value.intValue()));
    }
}
