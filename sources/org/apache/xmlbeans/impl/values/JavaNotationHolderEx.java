package org.apache.xmlbeans.impl.values;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.PrefixResolver;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaNotationHolderEx.class */
public abstract class JavaNotationHolderEx extends JavaNotationHolder {
    private SchemaType _schemaType;

    @Override // org.apache.xmlbeans.impl.values.JavaNotationHolder, org.apache.xmlbeans.impl.values.JavaQNameHolder, org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    public JavaNotationHolderEx(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaQNameHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int get_wscanon_rule() {
        return schemaType().getWhiteSpaceRule();
    }

    @Override // org.apache.xmlbeans.impl.values.JavaQNameHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        if (_validateOnSet()) {
            if (!check(s, this._schemaType)) {
                throw new XmlValueOutOfRangeException();
            }
            if (!this._schemaType.matchPatternFacet(s)) {
                throw new XmlValueOutOfRangeException();
            }
        }
        super.set_text(s);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_notation(String v) {
        set_text(v);
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
            v = JavaNotationHolder.validateLexical(value.getStringValue(), _voorVc, NamespaceContext.getCurrent());
        }
        super.set_QName(v);
    }

    public static QName validateLexical(String v, SchemaType sType, ValidationContext context, PrefixResolver resolver) {
        QName name = JavaQNameHolder.validateLexical(v, context, resolver);
        if (sType.hasPatternFacet() && !sType.matchPatternFacet(v)) {
            context.invalid(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{"NOTATION", v, QNameHelper.readable(sType)});
        }
        check(v, sType);
        return name;
    }

    private static boolean check(String v, SchemaType sType) {
        XmlObject len = sType.getFacet(0);
        if (len != null) {
            int m = ((XmlObjectBase) len).getBigIntegerValue().intValue();
            if (v.length() == m) {
                return false;
            }
        }
        XmlObject min = sType.getFacet(1);
        if (min != null) {
            int m2 = ((XmlObjectBase) min).getBigIntegerValue().intValue();
            if (v.length() < m2) {
                return false;
            }
        }
        XmlObject max = sType.getFacet(2);
        if (max != null) {
            int m3 = ((XmlObjectBase) max).getBigIntegerValue().intValue();
            if (v.length() > m3) {
                return false;
            }
            return true;
        }
        return true;
    }

    public static void validateValue(QName v, SchemaType sType, ValidationContext context) {
        XmlObject[] vals = sType.getEnumerationValues();
        if (vals != null) {
            for (XmlObject xmlObject : vals) {
                if (v.equals(((XmlObjectBase) xmlObject).getQNameValue())) {
                    return;
                }
            }
            context.invalid(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{"NOTATION", v, QNameHelper.readable(sType)});
        }
    }
}
