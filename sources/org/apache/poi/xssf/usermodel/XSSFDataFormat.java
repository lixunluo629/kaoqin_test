package org.apache.poi.xssf.usermodel;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.util.Removal;
import org.apache.poi.xssf.model.StylesTable;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFDataFormat.class */
public class XSSFDataFormat implements DataFormat {
    private final StylesTable stylesSource;

    protected XSSFDataFormat(StylesTable stylesSource) {
        this.stylesSource = stylesSource;
    }

    @Override // org.apache.poi.ss.usermodel.DataFormat
    public short getFormat(String format) {
        int idx = BuiltinFormats.getBuiltinFormat(format);
        if (idx == -1) {
            idx = this.stylesSource.putNumberFormat(format);
        }
        return (short) idx;
    }

    @Override // org.apache.poi.ss.usermodel.DataFormat
    public String getFormat(short index) {
        String fmt = this.stylesSource.getNumberFormatAt(index);
        if (fmt == null) {
            fmt = BuiltinFormats.getBuiltinFormat(index);
        }
        return fmt;
    }

    @Removal(version = "3.18")
    public String getFormat(int index) {
        return getFormat((short) index);
    }

    public void putFormat(short index, String format) {
        this.stylesSource.putNumberFormat(index, format);
    }
}
