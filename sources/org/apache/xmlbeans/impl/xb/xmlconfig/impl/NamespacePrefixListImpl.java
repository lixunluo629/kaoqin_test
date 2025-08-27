package org.apache.xmlbeans.impl.xb.xmlconfig.impl;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlListImpl;
import org.apache.xmlbeans.impl.xb.xmlconfig.NamespacePrefixList;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/impl/NamespacePrefixListImpl.class */
public class NamespacePrefixListImpl extends XmlListImpl implements NamespacePrefixList {
    private static final long serialVersionUID = 1;

    public NamespacePrefixListImpl(SchemaType sType) {
        super(sType, false);
    }

    protected NamespacePrefixListImpl(SchemaType sType, boolean b) {
        super(sType, b);
    }
}
