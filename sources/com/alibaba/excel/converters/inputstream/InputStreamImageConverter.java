package com.alibaba.excel.converters.inputstream;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.IoUtils;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/inputstream/InputStreamImageConverter.class */
public class InputStreamImageConverter implements Converter<InputStream> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return InputStream.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.IMAGE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public InputStream convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        throw new UnsupportedOperationException("Cannot convert images to input stream");
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(InputStream value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws IOException {
        return new CellData(IoUtils.toByteArray(value));
    }
}
