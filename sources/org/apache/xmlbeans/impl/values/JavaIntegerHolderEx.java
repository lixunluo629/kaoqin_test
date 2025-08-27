package org.apache.xmlbeans.impl.values;

import java.math.BigInteger;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlPositiveInteger;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaIntegerHolderEx.class */
public class JavaIntegerHolderEx extends JavaIntegerHolder {
    private SchemaType _schemaType;

    public JavaIntegerHolderEx(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaIntegerHolder, org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    @Override // org.apache.xmlbeans.impl.values.JavaIntegerHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        BigInteger v = lex(s, _voorVc);
        if (_validateOnSet()) {
            validateValue(v, this._schemaType, _voorVc);
        }
        if (_validateOnSet()) {
            validateLexical(s, this._schemaType, _voorVc);
        }
        super.set_BigInteger(v);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaIntegerHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigInteger(BigInteger v) {
        if (_validateOnSet()) {
            validateValue(v, this._schemaType, _voorVc);
        }
        super.set_BigInteger(v);
    }

    public static void validateLexical(String v, SchemaType sType, ValidationContext context) {
        JavaDecimalHolder.validateLexical(v, context);
        if (v.lastIndexOf(46) >= 0) {
            context.invalid("integer", new Object[]{v});
        }
        if (sType.hasPatternFacet() && !sType.matchPatternFacet(v)) {
            context.invalid(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{"integer", v, QNameHelper.readable(sType)});
        }
    }

    private static void validateValue(BigInteger v, SchemaType sType, ValidationContext context) {
        XmlPositiveInteger td = (XmlPositiveInteger) sType.getFacet(7);
        if (td != null) {
            String temp = v.toString();
            int len = temp.length();
            if (len > 0 && temp.charAt(0) == '-') {
                len--;
            }
            if (len > td.getBigIntegerValue().intValue()) {
                context.invalid(XmlErrorCodes.DATATYPE_TOTAL_DIGITS_VALID, new Object[]{new Integer(len), temp, new Integer(td.getBigIntegerValue().intValue()), QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject mine = sType.getFacet(3);
        if (mine != null) {
            BigInteger m = getBigIntegerValue(mine);
            if (v.compareTo(m) <= 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_EXCLUSIVE_VALID, new Object[]{"integer", v, m, QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject mini = sType.getFacet(4);
        if (mini != null) {
            BigInteger m2 = getBigIntegerValue(mini);
            if (v.compareTo(m2) < 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_INCLUSIVE_VALID, new Object[]{"integer", v, m2, QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject maxi = sType.getFacet(5);
        if (maxi != null) {
            BigInteger m3 = getBigIntegerValue(maxi);
            if (v.compareTo(m3) > 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MAX_INCLUSIVE_VALID, new Object[]{"integer", v, m3, QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject maxe = sType.getFacet(6);
        if (maxe != null) {
            BigInteger m4 = getBigIntegerValue(maxe);
            if (v.compareTo(m4) >= 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MAX_EXCLUSIVE_VALID, new Object[]{"integer", v, m4, QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject[] vals = sType.getEnumerationValues();
        if (vals != null) {
            for (XmlObject xmlObject : vals) {
                if (v.equals(getBigIntegerValue(xmlObject))) {
                    return;
                }
            }
            context.invalid(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{"integer", v, QNameHelper.readable(sType)});
        }
    }

    private static BigInteger getBigIntegerValue(XmlObject o) {
        SchemaType s = o.schemaType();
        switch (s.getDecimalSize()) {
            case SchemaType.SIZE_BIG_INTEGER /* 1000000 */:
                return ((XmlObjectBase) o).bigIntegerValue();
            case 1000001:
                return ((XmlObjectBase) o).bigDecimalValue().toBigInteger();
            default:
                throw new IllegalStateException("Bad facet type for Big Int: " + s);
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        validateLexical(lexical, schemaType(), ctx);
        validateValue(getBigIntegerValue(), schemaType(), ctx);
    }
}
