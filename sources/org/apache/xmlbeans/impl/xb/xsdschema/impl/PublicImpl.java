package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/PublicImpl.class */
public class PublicImpl extends JavaStringHolderEx implements Public {
    private static final long serialVersionUID = 1;

    public PublicImpl(SchemaType sType) {
        super(sType, false);
    }

    protected PublicImpl(SchemaType sType, boolean b) {
        super(sType, b);
    }
}
