package org.apache.xmlbeans.impl.values;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.PrefixResolver;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaQNameHolderEx.class */
public abstract class JavaQNameHolderEx extends JavaQNameHolder {
    private SchemaType _schemaType;

    @Override // org.apache.xmlbeans.impl.values.JavaQNameHolder, org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    public JavaQNameHolderEx(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaQNameHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int get_wscanon_rule() {
        return schemaType().getWhiteSpaceRule();
    }

    @Override // org.apache.xmlbeans.impl.values.JavaQNameHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        QName v;
        PrefixResolver resolver = NamespaceContext.getCurrent();
        if (resolver == null && has_store()) {
            resolver = get_store();
        }
        if (_validateOnSet()) {
            v = validateLexical(s, this._schemaType, _voorVc, resolver);
            if (v != null) {
                validateValue(v, this._schemaType, _voorVc);
            }
        } else {
            v = JavaQNameHolder.validateLexical(s, _voorVc, resolver);
        }
        super.set_QName(v);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaQNameHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_QName(QName name) {
        if (_validateOnSet()) {
            validateValue(name, this._schemaType, _voorVc);
        }
        super.set_QName(name);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaQNameHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_xmlanysimple(XmlAnySimpleType value) {
        QName v;
        if (_validateOnSet()) {
            v = validateLexical(value.getStringValue(), this._schemaType, _voorVc, NamespaceContext.getCurrent());
            if (v != null) {
                validateValue(v, this._schemaType, _voorVc);
            }
        } else {
            v = JavaQNameHolder.validateLexical(value.getStringValue(), _voorVc, NamespaceContext.getCurrent());
        }
        super.set_QName(v);
    }

    public static QName validateLexical(String v, SchemaType sType, ValidationContext context, PrefixResolver resolver) {
        QName name = JavaQNameHolder.validateLexical(v, context, resolver);
        if (sType.hasPatternFacet() && !sType.matchPatternFacet(v)) {
            context.invalid(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{XmlErrorCodes.QNAME, v, QNameHelper.readable(sType)});
        }
        return name;
    }

    public static void validateValue(QName v, SchemaType sType, ValidationContext context) {
        XmlObject[] vals = sType.getEnumerationValues();
        if (vals != null) {
            for (XmlObject xmlObject : vals) {
                if (v.equals(((XmlObjectBase) xmlObject).getQNameValue())) {
                    return;
                }
            }
            context.invalid(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{XmlErrorCodes.QNAME, v, QNameHelper.readable(sType)});
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        validateValue(getQNameValue(), schemaType(), ctx);
    }
}
