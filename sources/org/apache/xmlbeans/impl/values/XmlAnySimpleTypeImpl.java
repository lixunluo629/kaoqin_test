package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlAnySimpleTypeImpl.class */
public class XmlAnySimpleTypeImpl extends XmlObjectBase implements XmlAnySimpleType {
    private SchemaType _schemaType;
    String _textvalue;

    public XmlAnySimpleTypeImpl(SchemaType type, boolean complex) {
        this._textvalue = "";
        this._schemaType = type;
        initComplexType(complex, false);
    }

    public XmlAnySimpleTypeImpl() {
        this._textvalue = "";
        this._schemaType = BuiltinSchemaTypeSystem.ST_ANY_SIMPLE;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int get_wscanon_rule() {
        return 1;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return this._textvalue;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        this._textvalue = s;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._textvalue = null;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject obj) {
        return this._textvalue.equals(((XmlAnySimpleType) obj).getStringValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        if (this._textvalue == null) {
            return 0;
        }
        return this._textvalue.hashCode();
    }
}
