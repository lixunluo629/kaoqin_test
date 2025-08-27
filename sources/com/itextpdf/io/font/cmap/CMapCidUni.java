package com.itextpdf.io.font.cmap;

import com.itextpdf.io.util.IntHashtable;
import com.itextpdf.io.util.TextUtil;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/CMapCidUni.class */
public class CMapCidUni extends AbstractCMap {
    private static final long serialVersionUID = 6879167385978230141L;
    private IntHashtable map = new IntHashtable(65537);

    @Override // com.itextpdf.io.font.cmap.AbstractCMap
    void addChar(String mark, CMapObject code) {
        int codePoint;
        if (code.isNumber()) {
            String s = toUnicodeString(mark, true);
            if (TextUtil.isSurrogatePair(s, 0)) {
                codePoint = TextUtil.convertToUtf32(s, 0);
            } else {
                codePoint = s.charAt(0);
            }
            this.map.put(((Integer) code.getValue()).intValue(), codePoint);
        }
    }

    public int lookup(int character) {
        return this.map.get(character);
    }

    public int[] getCids() {
        return this.map.getKeys();
    }
}
