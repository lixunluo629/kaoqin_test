package com.alibaba.excel.converters.byteconverter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/byteconverter/ByteBooleanConverter.class */
public class ByteBooleanConverter implements Converter<Byte> {
    private static final Byte ONE = (byte) 1;
    private static final Byte ZERO = (byte) 0;

    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return Byte.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.BOOLEAN;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public Byte convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (cellData.getBooleanValue().booleanValue()) {
            return ONE;
        }
        return ZERO;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(Byte value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (ONE.equals(value)) {
            return new CellData(Boolean.TRUE);
        }
        return new CellData(Boolean.FALSE);
    }
}
