package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STProofErr$Enum.class */
public final class STProofErr$Enum extends StringEnumAbstractBase {
    static final int INT_SPELL_START = 1;
    static final int INT_SPELL_END = 2;
    static final int INT_GRAM_START = 3;
    static final int INT_GRAM_END = 4;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STProofErr$Enum[]{new STProofErr$Enum("spellStart", 1), new STProofErr$Enum("spellEnd", 2), new STProofErr$Enum("gramStart", 3), new STProofErr$Enum("gramEnd", 4)});
    private static final long serialVersionUID = 1;

    public static STProofErr$Enum forString(String str) {
        return (STProofErr$Enum) table.forString(str);
    }

    public static STProofErr$Enum forInt(int i) {
        return (STProofErr$Enum) table.forInt(i);
    }

    private STProofErr$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
