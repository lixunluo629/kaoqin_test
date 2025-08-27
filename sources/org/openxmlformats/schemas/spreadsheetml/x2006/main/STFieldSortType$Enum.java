package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STFieldSortType$Enum.class */
public final class STFieldSortType$Enum extends StringEnumAbstractBase {
    static final int INT_MANUAL = 1;
    static final int INT_ASCENDING = 2;
    static final int INT_DESCENDING = 3;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STFieldSortType$Enum[]{new STFieldSortType$Enum("manual", 1), new STFieldSortType$Enum("ascending", 2), new STFieldSortType$Enum("descending", 3)});
    private static final long serialVersionUID = 1;

    public static STFieldSortType$Enum forString(String str) {
        return (STFieldSortType$Enum) table.forString(str);
    }

    public static STFieldSortType$Enum forInt(int i) {
        return (STFieldSortType$Enum) table.forInt(i);
    }

    private STFieldSortType$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
