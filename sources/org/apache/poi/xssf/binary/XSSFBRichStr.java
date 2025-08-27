package org.apache.poi.xssf.binary;

import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/binary/XSSFBRichStr.class */
class XSSFBRichStr {
    private final String string;
    private final String phoneticString;

    public static XSSFBRichStr build(byte[] bytes, int offset) throws XSSFBParseException {
        byte first = bytes[offset];
        boolean z = ((first >> 7) & 1) == 1;
        boolean z2 = ((first >> 6) & 1) == 1;
        StringBuilder sb = new StringBuilder();
        XSSFBUtils.readXLWideString(bytes, offset + 1, sb);
        return new XSSFBRichStr(sb.toString(), "");
    }

    XSSFBRichStr(String string, String phoneticString) {
        this.string = string;
        this.phoneticString = phoneticString;
    }

    public String getString() {
        return this.string;
    }
}
