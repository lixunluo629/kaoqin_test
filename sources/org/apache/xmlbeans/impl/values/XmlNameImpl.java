package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlNameImpl.class */
public class XmlNameImpl extends JavaStringHolderEx implements XmlName {
    public XmlNameImpl() {
        super(XmlName.type, false);
    }

    public XmlNameImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
