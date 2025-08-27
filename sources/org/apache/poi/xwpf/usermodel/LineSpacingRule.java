package org.apache.poi.xwpf.usermodel;

import java.util.HashMap;
import java.util.Map;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/LineSpacingRule.class */
public enum LineSpacingRule {
    AUTO(1),
    EXACT(2),
    AT_LEAST(3);

    private static Map<Integer, LineSpacingRule> imap = new HashMap();
    private final int value;

    static {
        LineSpacingRule[] arr$ = values();
        for (LineSpacingRule p : arr$) {
            imap.put(Integer.valueOf(p.getValue()), p);
        }
    }

    LineSpacingRule(int val) {
        this.value = val;
    }

    public static LineSpacingRule valueOf(int type) {
        LineSpacingRule lineType = imap.get(Integer.valueOf(type));
        if (lineType == null) {
            throw new IllegalArgumentException("Unknown line type: " + type);
        }
        return lineType;
    }

    public int getValue() {
        return this.value;
    }
}
