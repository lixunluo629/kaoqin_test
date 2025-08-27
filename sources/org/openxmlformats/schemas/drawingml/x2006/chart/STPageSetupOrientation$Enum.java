package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STPageSetupOrientation$Enum.class */
public final class STPageSetupOrientation$Enum extends StringEnumAbstractBase {
    static final int INT_DEFAULT = 1;
    static final int INT_PORTRAIT = 2;
    static final int INT_LANDSCAPE = 3;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STPageSetupOrientation$Enum[]{new STPageSetupOrientation$Enum("default", 1), new STPageSetupOrientation$Enum("portrait", 2), new STPageSetupOrientation$Enum("landscape", 3)});
    private static final long serialVersionUID = 1;

    public static STPageSetupOrientation$Enum forString(String str) {
        return (STPageSetupOrientation$Enum) table.forString(str);
    }

    public static STPageSetupOrientation$Enum forInt(int i) {
        return (STPageSetupOrientation$Enum) table.forInt(i);
    }

    private STPageSetupOrientation$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
