package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STDvAspect$Enum.class */
public final class STDvAspect$Enum extends StringEnumAbstractBase {
    static final int INT_DVASPECT_CONTENT = 1;
    static final int INT_DVASPECT_ICON = 2;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STDvAspect$Enum[]{new STDvAspect$Enum("DVASPECT_CONTENT", 1), new STDvAspect$Enum("DVASPECT_ICON", 2)});
    private static final long serialVersionUID = 1;

    public static STDvAspect$Enum forString(String str) {
        return (STDvAspect$Enum) table.forString(str);
    }

    public static STDvAspect$Enum forInt(int i) {
        return (STDvAspect$Enum) table.forInt(i);
    }

    private STDvAspect$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
