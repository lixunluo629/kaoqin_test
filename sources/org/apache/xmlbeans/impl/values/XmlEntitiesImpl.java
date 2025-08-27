package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlENTITIES;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlEntitiesImpl.class */
public class XmlEntitiesImpl extends XmlListImpl implements XmlENTITIES {
    public XmlEntitiesImpl() {
        super(XmlENTITIES.type, false);
    }

    public XmlEntitiesImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
