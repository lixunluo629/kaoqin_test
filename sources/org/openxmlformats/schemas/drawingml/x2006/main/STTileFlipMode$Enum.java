package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTileFlipMode$Enum.class */
public final class STTileFlipMode$Enum extends StringEnumAbstractBase {
    static final int INT_NONE = 1;
    static final int INT_X = 2;
    static final int INT_Y = 3;
    static final int INT_XY = 4;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STTileFlipMode$Enum[]{new STTileFlipMode$Enum("none", 1), new STTileFlipMode$Enum("x", 2), new STTileFlipMode$Enum("y", 3), new STTileFlipMode$Enum("xy", 4)});
    private static final long serialVersionUID = 1;

    public static STTileFlipMode$Enum forString(String str) {
        return (STTileFlipMode$Enum) table.forString(str);
    }

    public static STTileFlipMode$Enum forInt(int i) {
        return (STTileFlipMode$Enum) table.forInt(i);
    }

    private STTileFlipMode$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
