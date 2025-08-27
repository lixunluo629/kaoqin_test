package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlPositiveInteger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlPositiveIntegerImpl.class */
public class XmlPositiveIntegerImpl extends JavaIntegerHolderEx implements XmlPositiveInteger {
    public XmlPositiveIntegerImpl() {
        super(XmlPositiveInteger.type, false);
    }

    public XmlPositiveIntegerImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
