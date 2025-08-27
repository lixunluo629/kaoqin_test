package com.microsoft.schemas.office.office;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/STConnectorType$Enum.class */
public final class STConnectorType$Enum extends StringEnumAbstractBase {
    static final int INT_NONE = 1;
    static final int INT_STRAIGHT = 2;
    static final int INT_ELBOW = 3;
    static final int INT_CURVED = 4;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STConnectorType$Enum[]{new STConnectorType$Enum("none", 1), new STConnectorType$Enum("straight", 2), new STConnectorType$Enum("elbow", 3), new STConnectorType$Enum("curved", 4)});
    private static final long serialVersionUID = 1;

    public static STConnectorType$Enum forString(String str) {
        return (STConnectorType$Enum) table.forString(str);
    }

    public static STConnectorType$Enum forInt(int i) {
        return (STConnectorType$Enum) table.forInt(i);
    }

    private STConnectorType$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
