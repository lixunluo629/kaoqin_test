package com.alibaba.excel.converters.file;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.FileUtils;
import java.io.File;
import java.io.IOException;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/file/FileImageConverter.class */
public class FileImageConverter implements Converter<File> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return File.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.IMAGE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public File convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        throw new UnsupportedOperationException("Cannot convert images to file");
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(File value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws IOException {
        return new CellData(FileUtils.readFileToByteArray(value));
    }
}
