package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STPhoneticAlignment$Enum.class */
public final class STPhoneticAlignment$Enum extends StringEnumAbstractBase {
    static final int INT_NO_CONTROL = 1;
    static final int INT_LEFT = 2;
    static final int INT_CENTER = 3;
    static final int INT_DISTRIBUTED = 4;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STPhoneticAlignment$Enum[]{new STPhoneticAlignment$Enum("noControl", 1), new STPhoneticAlignment$Enum("left", 2), new STPhoneticAlignment$Enum("center", 3), new STPhoneticAlignment$Enum("distributed", 4)});
    private static final long serialVersionUID = 1;

    public static STPhoneticAlignment$Enum forString(String str) {
        return (STPhoneticAlignment$Enum) table.forString(str);
    }

    public static STPhoneticAlignment$Enum forInt(int i) {
        return (STPhoneticAlignment$Enum) table.forInt(i);
    }

    private STPhoneticAlignment$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
