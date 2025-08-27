package com.alibaba.excel.converters.doubleconverter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import java.math.BigDecimal;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/doubleconverter/DoubleNumberConverter.class */
public class DoubleNumberConverter implements Converter<Double> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return Double.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public Double convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return Double.valueOf(cellData.getNumberValue().doubleValue());
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(Double value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new CellData(BigDecimal.valueOf(value.doubleValue()));
    }
}
