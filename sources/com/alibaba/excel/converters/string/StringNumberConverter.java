package com.alibaba.excel.converters.string;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.util.NumberUtils;
import java.math.BigDecimal;
import org.apache.poi.ss.usermodel.DateUtil;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/string/StringNumberConverter.class */
public class StringNumberConverter implements Converter<String> {
    @Override // com.alibaba.excel.converters.Converter
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.excel.converters.Converter
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (contentProperty != null && contentProperty.getDateTimeFormatProperty() != null) {
            return DateUtils.format(DateUtil.getJavaDate(cellData.getNumberValue().doubleValue(), contentProperty.getDateTimeFormatProperty().getUse1904windowing().booleanValue(), null), contentProperty.getDateTimeFormatProperty().getFormat());
        }
        if (contentProperty != null && contentProperty.getNumberFormatProperty() != null) {
            return NumberUtils.format(cellData.getNumberValue(), contentProperty);
        }
        if (cellData.getDataFormat() != null) {
            if (DateUtil.isADateFormat(cellData.getDataFormat().intValue(), cellData.getDataFormatString())) {
                return DateUtils.format(DateUtil.getJavaDate(cellData.getNumberValue().doubleValue(), globalConfiguration.getUse1904windowing().booleanValue(), null));
            }
            return NumberUtils.format(cellData.getNumberValue(), contentProperty);
        }
        return NumberUtils.format(cellData.getNumberValue(), contentProperty);
    }

    @Override // com.alibaba.excel.converters.Converter
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new CellData(new BigDecimal(value));
    }
}
