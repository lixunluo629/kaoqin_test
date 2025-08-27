package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STPTabAlignment$Enum.class */
public final class STPTabAlignment$Enum extends StringEnumAbstractBase {
    static final int INT_LEFT = 1;
    static final int INT_CENTER = 2;
    static final int INT_RIGHT = 3;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STPTabAlignment$Enum[]{new STPTabAlignment$Enum("left", 1), new STPTabAlignment$Enum("center", 2), new STPTabAlignment$Enum("right", 3)});
    private static final long serialVersionUID = 1;

    public static STPTabAlignment$Enum forString(String str) {
        return (STPTabAlignment$Enum) table.forString(str);
    }

    public static STPTabAlignment$Enum forInt(int i) {
        return (STPTabAlignment$Enum) table.forInt(i);
    }

    private STPTabAlignment$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
