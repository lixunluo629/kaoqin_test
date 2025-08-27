package com.alibaba.excel.converters.url;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.IoUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/url/UrlImageConverter.class */
public class UrlImageConverter implements Converter<URL> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return URL.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.IMAGE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public URL convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        throw new UnsupportedOperationException("Cannot convert images to url.");
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(URL value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = value.openStream();
            byte[] bytes = IoUtils.toByteArray(inputStream);
            CellData cellData = new CellData(bytes);
            if (inputStream != null) {
                inputStream.close();
            }
            return cellData;
        } catch (Throwable th) {
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
    }
}
