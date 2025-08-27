package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STRubyAlign$Enum.class */
public final class STRubyAlign$Enum extends StringEnumAbstractBase {
    static final int INT_CENTER = 1;
    static final int INT_DISTRIBUTE_LETTER = 2;
    static final int INT_DISTRIBUTE_SPACE = 3;
    static final int INT_LEFT = 4;
    static final int INT_RIGHT = 5;
    static final int INT_RIGHT_VERTICAL = 6;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STRubyAlign$Enum[]{new STRubyAlign$Enum("center", 1), new STRubyAlign$Enum("distributeLetter", 2), new STRubyAlign$Enum("distributeSpace", 3), new STRubyAlign$Enum("left", 4), new STRubyAlign$Enum("right", 5), new STRubyAlign$Enum("rightVertical", 6)});
    private static final long serialVersionUID = 1;

    public static STRubyAlign$Enum forString(String str) {
        return (STRubyAlign$Enum) table.forString(str);
    }

    public static STRubyAlign$Enum forInt(int i) {
        return (STRubyAlign$Enum) table.forInt(i);
    }

    private STRubyAlign$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
