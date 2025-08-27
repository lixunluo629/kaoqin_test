package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;
import org.springframework.hateoas.Link;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STDisplacedByCustomXml$Enum.class */
public final class STDisplacedByCustomXml$Enum extends StringEnumAbstractBase {
    static final int INT_NEXT = 1;
    static final int INT_PREV = 2;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STDisplacedByCustomXml$Enum[]{new STDisplacedByCustomXml$Enum(Link.REL_NEXT, 1), new STDisplacedByCustomXml$Enum(Link.REL_PREVIOUS, 2)});
    private static final long serialVersionUID = 1;

    public static STDisplacedByCustomXml$Enum forString(String str) {
        return (STDisplacedByCustomXml$Enum) table.forString(str);
    }

    public static STDisplacedByCustomXml$Enum forInt(int i) {
        return (STDisplacedByCustomXml$Enum) table.forInt(i);
    }

    private STDisplacedByCustomXml$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
