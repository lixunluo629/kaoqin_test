package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlDate;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlDateImpl.class */
public class XmlDateImpl extends JavaGDateHolderEx implements XmlDate {
    public XmlDateImpl() {
        super(XmlDate.type, false);
    }

    public XmlDateImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
