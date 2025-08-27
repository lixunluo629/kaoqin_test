package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.poi.ss.util.CellUtil;
import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STVisibility$Enum.class */
public final class STVisibility$Enum extends StringEnumAbstractBase {
    static final int INT_VISIBLE = 1;
    static final int INT_HIDDEN = 2;
    static final int INT_VERY_HIDDEN = 3;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STVisibility$Enum[]{new STVisibility$Enum("visible", 1), new STVisibility$Enum(CellUtil.HIDDEN, 2), new STVisibility$Enum("veryHidden", 3)});
    private static final long serialVersionUID = 1;

    public static STVisibility$Enum forString(String str) {
        return (STVisibility$Enum) table.forString(str);
    }

    public static STVisibility$Enum forInt(int i) {
        return (STVisibility$Enum) table.forInt(i);
    }

    private STVisibility$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
