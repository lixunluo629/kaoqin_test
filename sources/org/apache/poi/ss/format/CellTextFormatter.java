package org.apache.poi.ss.format;

import java.util.Locale;
import java.util.regex.Matcher;
import net.coobird.thumbnailator.ThumbnailParameter;
import org.apache.poi.ss.format.CellFormatPart;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellTextFormatter.class */
public class CellTextFormatter extends CellFormatter {
    private final int[] textPos;
    private final String desc;
    static final CellFormatter SIMPLE_TEXT = new CellTextFormatter("@");

    public CellTextFormatter(String format) {
        super(format);
        final int[] numPlaces = new int[1];
        this.desc = CellFormatPart.parseFormat(format, CellFormatType.TEXT, new CellFormatPart.PartHandler() { // from class: org.apache.poi.ss.format.CellTextFormatter.1
            @Override // org.apache.poi.ss.format.CellFormatPart.PartHandler
            public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc) {
                if (part.equals("@")) {
                    int[] iArr = numPlaces;
                    iArr[0] = iArr[0] + 1;
                    return ThumbnailParameter.DETERMINE_FORMAT;
                }
                return null;
            }
        }).toString();
        this.textPos = new int[numPlaces[0]];
        int pos = this.desc.length() - 1;
        for (int i = 0; i < this.textPos.length; i++) {
            this.textPos[i] = this.desc.lastIndexOf(ThumbnailParameter.DETERMINE_FORMAT, pos);
            pos = this.textPos[i] - 1;
        }
    }

    @Override // org.apache.poi.ss.format.CellFormatter
    public void formatValue(StringBuffer toAppendTo, Object obj) {
        int start = toAppendTo.length();
        String text = obj.toString();
        if (obj instanceof Boolean) {
            text = text.toUpperCase(Locale.ROOT);
        }
        toAppendTo.append(this.desc);
        for (int i = 0; i < this.textPos.length; i++) {
            int pos = start + this.textPos[i];
            toAppendTo.replace(pos, pos + 1, text);
        }
    }

    @Override // org.apache.poi.ss.format.CellFormatter
    public void simpleValue(StringBuffer toAppendTo, Object value) {
        SIMPLE_TEXT.formatValue(toAppendTo, value);
    }
}
