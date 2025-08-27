package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlTime;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlTimeImpl.class */
public class XmlTimeImpl extends JavaGDateHolderEx implements XmlTime {
    public XmlTimeImpl() {
        super(XmlTime.type, false);
    }

    public XmlTimeImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
