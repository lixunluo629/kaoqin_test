package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlDateTime;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlDateTimeImpl.class */
public class XmlDateTimeImpl extends JavaGDateHolderEx implements XmlDateTime {
    public XmlDateTimeImpl() {
        super(XmlDateTime.type, false);
    }

    public XmlDateTimeImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
