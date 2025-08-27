package org.apache.xmlbeans.impl.xb.xmlconfig.impl;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.apache.xmlbeans.impl.xb.xmlconfig.JavaName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/impl/JavaNameImpl.class */
public class JavaNameImpl extends JavaStringHolderEx implements JavaName {
    private static final long serialVersionUID = 1;

    public JavaNameImpl(SchemaType sType) {
        super(sType, false);
    }

    protected JavaNameImpl(SchemaType sType, boolean b) {
        super(sType, b);
    }
}
