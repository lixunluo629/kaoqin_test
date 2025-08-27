package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlNegativeInteger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlNegativeIntegerImpl.class */
public class XmlNegativeIntegerImpl extends JavaIntegerHolderEx implements XmlNegativeInteger {
    public XmlNegativeIntegerImpl() {
        super(XmlNegativeInteger.type, false);
    }

    public XmlNegativeIntegerImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
