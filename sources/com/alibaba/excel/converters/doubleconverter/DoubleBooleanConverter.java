package com.alibaba.excel.converters.doubleconverter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/doubleconverter/DoubleBooleanConverter.class */
public class DoubleBooleanConverter implements Converter<Double> {
    private static final Double ONE = Double.valueOf(1.0d);
    private static final Double ZERO = Double.valueOf(0.0d);

    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return Double.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.BOOLEAN;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public Double convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (cellData.getBooleanValue().booleanValue()) {
            return ONE;
        }
        return ZERO;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(Double value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (ONE.equals(value)) {
            return new CellData(Boolean.TRUE);
        }
        return new CellData(Boolean.FALSE);
    }
}
