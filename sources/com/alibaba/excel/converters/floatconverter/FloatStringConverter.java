package com.alibaba.excel.converters.floatconverter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.NumberUtils;
import java.text.ParseException;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/floatconverter/FloatStringConverter.class */
public class FloatStringConverter implements Converter<Float> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return Float.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public Float convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws ParseException {
        return NumberUtils.parseFloat(cellData.getStringValue(), contentProperty);
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(Float value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return NumberUtils.formatToCellData(value, contentProperty);
    }
}
