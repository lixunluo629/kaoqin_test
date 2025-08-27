package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.common.XMLChar;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlNCNameImpl.class */
public class XmlNCNameImpl extends JavaStringHolderEx implements XmlNCName {
    public XmlNCNameImpl() {
        super(XmlNCName.type, false);
    }

    public XmlNCNameImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }

    public static void validateLexical(String v, ValidationContext context) {
        if (!XMLChar.isValidNCName(v)) {
            context.invalid(XmlErrorCodes.NCNAME, new Object[]{v});
        }
    }
}
