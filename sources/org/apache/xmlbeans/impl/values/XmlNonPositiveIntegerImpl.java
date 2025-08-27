package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlNonPositiveInteger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlNonPositiveIntegerImpl.class */
public class XmlNonPositiveIntegerImpl extends JavaIntegerHolderEx implements XmlNonPositiveInteger {
    public XmlNonPositiveIntegerImpl() {
        super(XmlNonPositiveInteger.type, false);
    }

    public XmlNonPositiveIntegerImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
