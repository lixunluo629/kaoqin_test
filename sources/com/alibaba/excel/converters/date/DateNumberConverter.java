package com.alibaba.excel.converters.date;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.poi.ss.usermodel.DateUtil;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/date/DateNumberConverter.class */
public class DateNumberConverter implements Converter<Date> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return Date.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public Date convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return DateUtil.getJavaDate(cellData.getNumberValue().doubleValue(), globalConfiguration.getUse1904windowing().booleanValue(), null);
        }
        return DateUtil.getJavaDate(cellData.getNumberValue().doubleValue(), contentProperty.getDateTimeFormatProperty().getUse1904windowing().booleanValue(), null);
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(Date value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new CellData(BigDecimal.valueOf(DateUtil.getExcelDate(value, globalConfiguration.getUse1904windowing().booleanValue())));
        }
        return new CellData(BigDecimal.valueOf(DateUtil.getExcelDate(value, contentProperty.getDateTimeFormatProperty().getUse1904windowing().booleanValue())));
    }
}
