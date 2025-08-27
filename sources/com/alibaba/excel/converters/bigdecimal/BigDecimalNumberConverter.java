package com.alibaba.excel.converters.bigdecimal;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import java.math.BigDecimal;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/bigdecimal/BigDecimalNumberConverter.class */
public class BigDecimalNumberConverter implements Converter<BigDecimal> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return BigDecimal.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public BigDecimal convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return cellData.getNumberValue();
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(BigDecimal value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new CellData(value);
    }
}
