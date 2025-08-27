package com.alibaba.excel.converters.bytearray;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/bytearray/ByteArrayImageConverter.class */
public class ByteArrayImageConverter implements Converter<byte[]> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return byte[].class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.IMAGE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public byte[] convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        throw new UnsupportedOperationException("Cannot convert images to byte arrays");
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(byte[] value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new CellData(value);
    }
}
