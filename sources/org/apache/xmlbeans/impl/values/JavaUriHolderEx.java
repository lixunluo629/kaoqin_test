package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaUriHolderEx.class */
public class JavaUriHolderEx extends JavaUriHolder {
    private SchemaType _schemaType;

    @Override // org.apache.xmlbeans.impl.values.JavaUriHolder, org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    public JavaUriHolderEx(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int get_wscanon_rule() {
        return schemaType().getWhiteSpaceRule();
    }

    @Override // org.apache.xmlbeans.impl.values.JavaUriHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
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

    public static void validateLexical(String v, SchemaType sType, ValidationContext context) {
        int i;
        int i2;
        int i3;
        XmlAnyUriImpl.validateLexical(v, context);
        XmlObject[] vals = sType.getEnumerationValues();
        if (vals != null) {
            int i4 = 0;
            while (i4 < vals.length) {
                String e = ((SimpleValue) vals[i4]).getStringValue();
                if (e.equals(v)) {
                    break;
                } else {
                    i4++;
                }
            }
            if (i4 >= vals.length) {
                context.invalid(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{XmlErrorCodes.ANYURI, v, QNameHelper.readable(sType)});
            }
        }
        if (sType.hasPatternFacet() && !sType.matchPatternFacet(v)) {
            context.invalid(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{XmlErrorCodes.ANYURI, v, QNameHelper.readable(sType)});
        }
        XmlObject x = sType.getFacet(0);
        if (x != null && (i3 = ((SimpleValue) x).getBigIntegerValue().intValue()) != v.length()) {
            context.invalid(XmlErrorCodes.DATATYPE_LENGTH_VALID$STRING, new Object[]{XmlErrorCodes.ANYURI, v, new Integer(i3), QNameHelper.readable(sType)});
        }
        XmlObject x2 = sType.getFacet(1);
        if (x2 != null && (i2 = ((SimpleValue) x2).getBigIntegerValue().intValue()) > v.length()) {
            context.invalid(XmlErrorCodes.DATATYPE_MIN_LENGTH_VALID$STRING, new Object[]{XmlErrorCodes.ANYURI, v, new Integer(i2), QNameHelper.readable(sType)});
        }
        XmlObject x3 = sType.getFacet(2);
        if (x3 != null && (i = ((SimpleValue) x3).getBigIntegerValue().intValue()) < v.length()) {
            context.invalid(XmlErrorCodes.DATATYPE_MAX_LENGTH_VALID$STRING, new Object[]{XmlErrorCodes.ANYURI, v, new Integer(i), QNameHelper.readable(sType)});
        }
    }

    private static boolean check(String v, SchemaType sType) {
        int length = v == null ? 0 : v.length();
        XmlObject len = sType.getFacet(0);
        if (len != null) {
            int m = ((SimpleValue) len).getBigIntegerValue().intValue();
            if (length == m) {
                return false;
            }
        }
        XmlObject min = sType.getFacet(1);
        if (min != null) {
            int m2 = ((SimpleValue) min).getBigIntegerValue().intValue();
            if (length < m2) {
                return false;
            }
        }
        XmlObject max = sType.getFacet(2);
        if (max != null) {
            int m3 = ((SimpleValue) max).getBigIntegerValue().intValue();
            if (length > m3) {
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        validateLexical(stringValue(), schemaType(), ctx);
    }
}
