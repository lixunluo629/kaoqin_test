package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlUnsignedInt;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlUnsignedIntImpl.class */
public class XmlUnsignedIntImpl extends JavaLongHolderEx implements XmlUnsignedInt {
    public XmlUnsignedIntImpl() {
        super(XmlUnsignedInt.type, false);
    }

    public XmlUnsignedIntImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
