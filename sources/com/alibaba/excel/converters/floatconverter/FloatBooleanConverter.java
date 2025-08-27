package com.alibaba.excel.converters.floatconverter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/floatconverter/FloatBooleanConverter.class */
public class FloatBooleanConverter implements Converter<Float> {
    private static final Float ONE = Float.valueOf(1.0f);
    private static final Float ZERO = Float.valueOf(0.0f);

    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return Float.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.BOOLEAN;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public Float convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (cellData.getBooleanValue().booleanValue()) {
            return ONE;
        }
        return ZERO;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(Float value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (ONE.equals(value)) {
            return new CellData(Boolean.TRUE);
        }
        return new CellData(Boolean.FALSE);
    }
}
