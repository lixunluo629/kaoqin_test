package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STPathFillMode$Enum.class */
public final class STPathFillMode$Enum extends StringEnumAbstractBase {
    static final int INT_NONE = 1;
    static final int INT_NORM = 2;
    static final int INT_LIGHTEN = 3;
    static final int INT_LIGHTEN_LESS = 4;
    static final int INT_DARKEN = 5;
    static final int INT_DARKEN_LESS = 6;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STPathFillMode$Enum[]{new STPathFillMode$Enum("none", 1), new STPathFillMode$Enum("norm", 2), new STPathFillMode$Enum("lighten", 3), new STPathFillMode$Enum("lightenLess", 4), new STPathFillMode$Enum("darken", 5), new STPathFillMode$Enum("darkenLess", 6)});
    private static final long serialVersionUID = 1;

    public static STPathFillMode$Enum forString(String str) {
        return (STPathFillMode$Enum) table.forString(str);
    }

    public static STPathFillMode$Enum forInt(int i) {
        return (STPathFillMode$Enum) table.forInt(i);
    }

    private STPathFillMode$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
