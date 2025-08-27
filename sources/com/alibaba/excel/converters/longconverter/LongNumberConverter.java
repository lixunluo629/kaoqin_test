package com.alibaba.excel.converters.longconverter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import java.math.BigDecimal;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/longconverter/LongNumberConverter.class */
public class LongNumberConverter implements Converter<Long> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return Long.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public Long convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return Long.valueOf(cellData.getNumberValue().longValue());
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(Long value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new CellData(BigDecimal.valueOf(value.longValue()));
    }
}
