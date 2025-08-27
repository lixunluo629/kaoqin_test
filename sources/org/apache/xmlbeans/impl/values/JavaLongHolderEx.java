package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.util.XsTypeConverter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaLongHolderEx.class */
public abstract class JavaLongHolderEx extends JavaLongHolder {
    private SchemaType _schemaType;

    public JavaLongHolderEx(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaLongHolder, org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    @Override // org.apache.xmlbeans.impl.values.JavaLongHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        try {
            long v = XsTypeConverter.lexLong(s);
            if (_validateOnSet()) {
                validateValue(v, this._schemaType, _voorVc);
                validateLexical(s, this._schemaType, _voorVc);
            }
            super.set_long(v);
        } catch (Exception e) {
            throw new XmlValueOutOfRangeException();
        }
    }

    @Override // org.apache.xmlbeans.impl.values.JavaLongHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_long(long v) {
        if (_validateOnSet()) {
            validateValue(v, this._schemaType, _voorVc);
        }
        super.set_long(v);
    }

    public static void validateLexical(String v, SchemaType sType, ValidationContext context) {
        JavaDecimalHolder.validateLexical(v, context);
        if (sType.hasPatternFacet() && !sType.matchPatternFacet(v)) {
            context.invalid(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{XmlErrorCodes.LONG, v, QNameHelper.readable(sType)});
        }
    }

    private static void validateValue(long v, SchemaType sType, ValidationContext context) {
        XmlObject td = sType.getFacet(7);
        if (td != null) {
            long m = getLongValue(td);
            String temp = Long.toString(v);
            int len = temp.length();
            if (len > 0 && temp.charAt(0) == '-') {
                len--;
            }
            if (len > m) {
                context.invalid(XmlErrorCodes.DATATYPE_TOTAL_DIGITS_VALID, new Object[]{new Integer(len), temp, new Long(m), QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject mine = sType.getFacet(3);
        if (mine != null) {
            long m2 = getLongValue(mine);
            if (v <= m2) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_EXCLUSIVE_VALID, new Object[]{XmlErrorCodes.LONG, new Long(v), new Long(m2), QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject mini = sType.getFacet(4);
        if (mini != null) {
            long m3 = getLongValue(mini);
            if (v < m3) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_INCLUSIVE_VALID, new Object[]{XmlErrorCodes.LONG, new Long(v), new Long(m3), QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject maxi = sType.getFacet(5);
        if (maxi != null) {
            long m4 = getLongValue(maxi);
            if (v > m4) {
                context.invalid(XmlErrorCodes.DATATYPE_MAX_INCLUSIVE_VALID, new Object[]{XmlErrorCodes.LONG, new Long(v), new Long(m4), QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject maxe = sType.getFacet(6);
        if (maxe != null) {
            long m5 = getLongValue(maxe);
            if (v >= m5) {
                context.invalid(XmlErrorCodes.DATATYPE_MAX_EXCLUSIVE_VALID, new Object[]{XmlErrorCodes.LONG, new Long(v), new Long(m5), QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject[] vals = sType.getEnumerationValues();
        if (vals != null) {
            for (XmlObject xmlObject : vals) {
                if (v == getLongValue(xmlObject)) {
                    return;
                }
            }
            context.invalid(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{XmlErrorCodes.LONG, new Long(v), QNameHelper.readable(sType)});
        }
    }

    private static long getLongValue(XmlObject o) {
        SchemaType s = o.schemaType();
        switch (s.getDecimalSize()) {
            case 64:
                return ((XmlObjectBase) o).getLongValue();
            case SchemaType.SIZE_BIG_INTEGER /* 1000000 */:
                return ((XmlObjectBase) o).getBigIntegerValue().longValue();
            case 1000001:
                return ((XmlObjectBase) o).getBigDecimalValue().longValue();
            default:
                throw new IllegalStateException("Bad facet type: " + s);
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        validateLexical(lexical, schemaType(), ctx);
        validateValue(getLongValue(), schemaType(), ctx);
    }
}
