package org.w3.x2000.x09.xmldsig.impl;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.JavaBase64HolderEx;
import org.w3.x2000.x09.xmldsig.DigestValueType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/impl/DigestValueTypeImpl.class */
public class DigestValueTypeImpl extends JavaBase64HolderEx implements DigestValueType {
    public DigestValueTypeImpl(SchemaType schemaType) {
        super(schemaType, false);
    }

    protected DigestValueTypeImpl(SchemaType schemaType, boolean z) {
        super(schemaType, z);
    }
}
