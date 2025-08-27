package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STSheetViewType$Enum.class */
public final class STSheetViewType$Enum extends StringEnumAbstractBase {
    static final int INT_NORMAL = 1;
    static final int INT_PAGE_BREAK_PREVIEW = 2;
    static final int INT_PAGE_LAYOUT = 3;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STSheetViewType$Enum[]{new STSheetViewType$Enum("normal", 1), new STSheetViewType$Enum("pageBreakPreview", 2), new STSheetViewType$Enum("pageLayout", 3)});
    private static final long serialVersionUID = 1;

    public static STSheetViewType$Enum forString(String str) {
        return (STSheetViewType$Enum) table.forString(str);
    }

    public static STSheetViewType$Enum forInt(int i) {
        return (STSheetViewType$Enum) table.forInt(i);
    }

    private STSheetViewType$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
