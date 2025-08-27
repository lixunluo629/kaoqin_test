package org.apache.poi.ss.usermodel;

import org.apache.poi.util.Removal;
import org.aspectj.apache.bcel.Constants;

@Removal(version = "4.0")
@Deprecated
/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/FontCharset.class */
public enum FontCharset {
    ANSI(0),
    DEFAULT(1),
    SYMBOL(2),
    MAC(77),
    SHIFTJIS(128),
    HANGEUL(129),
    JOHAB(130),
    GB2312(134),
    CHINESEBIG5(136),
    GREEK(161),
    TURKISH(162),
    VIETNAMESE(163),
    HEBREW(177),
    ARABIC(178),
    BALTIC(186),
    RUSSIAN(204),
    THAI(Constants.ANEWARRAY_QUICK),
    EASTEUROPE(238),
    OEM(255);

    private int charset;
    private static FontCharset[] _table = new FontCharset[256];

    static {
        FontCharset[] arr$ = values();
        for (FontCharset c : arr$) {
            _table[c.getValue()] = c;
        }
    }

    FontCharset(int value) {
        this.charset = value;
    }

    public int getValue() {
        return this.charset;
    }

    public static FontCharset valueOf(int value) {
        if (value >= _table.length) {
            return null;
        }
        return _table[value];
    }
}
