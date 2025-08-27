package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlAnySimpleTypeRestriction.class */
public class XmlAnySimpleTypeRestriction extends XmlAnySimpleTypeImpl {
    private SchemaType _schemaType;

    public XmlAnySimpleTypeRestriction(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlAnySimpleTypeImpl, org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }
}
