package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlNMTOKENS;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlNmTokensImpl.class */
public class XmlNmTokensImpl extends XmlListImpl implements XmlNMTOKENS {
    public XmlNmTokensImpl() {
        super(XmlNMTOKENS.type, false);
    }

    public XmlNmTokensImpl(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
