package org.apache.poi.xssf;

import org.apache.poi.UnsupportedFileFormatException;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/XLSBUnsupportedException.class */
public class XLSBUnsupportedException extends UnsupportedFileFormatException {
    private static final long serialVersionUID = 7849681804154571175L;
    public static final String MESSAGE = ".XLSB Binary Workbooks are not supported";

    public XLSBUnsupportedException() {
        super(MESSAGE);
    }
}
