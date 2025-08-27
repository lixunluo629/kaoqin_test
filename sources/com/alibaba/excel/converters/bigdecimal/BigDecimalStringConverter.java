package com.alibaba.excel.converters.bigdecimal;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.NumberUtils;
import java.math.BigDecimal;
import java.text.ParseException;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/bigdecimal/BigDecimalStringConverter.class */
public class BigDecimalStringConverter implements Converter<BigDecimal> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return BigDecimal.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public BigDecimal convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws ParseException {
        return NumberUtils.parseBigDecimal(cellData.getStringValue(), contentProperty);
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(BigDecimal value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return NumberUtils.formatToCellData(value, contentProperty);
    }
}
