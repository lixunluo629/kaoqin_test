package org.apache.xmlbeans.impl.values;

import java.math.BigDecimal;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaDecimalHolderEx.class */
public abstract class JavaDecimalHolderEx extends JavaDecimalHolder {
    private SchemaType _schemaType;

    @Override // org.apache.xmlbeans.impl.values.JavaDecimalHolder, org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    public JavaDecimalHolderEx(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaDecimalHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        if (_validateOnSet()) {
            validateLexical(s, this._schemaType, _voorVc);
        }
        BigDecimal v = null;
        try {
            v = new BigDecimal(s);
        } catch (NumberFormatException e) {
            _voorVc.invalid(XmlErrorCodes.DECIMAL, new Object[]{s});
        }
        if (_validateOnSet()) {
            validateValue(v, this._schemaType, _voorVc);
        }
        super.set_BigDecimal(v);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaDecimalHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigDecimal(BigDecimal v) {
        if (_validateOnSet()) {
            validateValue(v, this._schemaType, _voorVc);
        }
        super.set_BigDecimal(v);
    }

    public static void validateLexical(String v, SchemaType sType, ValidationContext context) {
        JavaDecimalHolder.validateLexical(v, context);
        if (sType.hasPatternFacet() && !sType.matchPatternFacet(v)) {
            context.invalid(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{XmlErrorCodes.DECIMAL, v, QNameHelper.readable(sType)});
        }
    }

    public static void validateValue(BigDecimal v, SchemaType sType, ValidationContext context) {
        XmlObject fd = sType.getFacet(8);
        if (fd != null) {
            int scale = ((XmlObjectBase) fd).getBigIntegerValue().intValue();
            try {
                v.setScale(scale);
            } catch (ArithmeticException e) {
                context.invalid(XmlErrorCodes.DATATYPE_FRACTION_DIGITS_VALID, new Object[]{new Integer(v.scale()), v.toString(), new Integer(scale), QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject td = sType.getFacet(7);
        if (td != null) {
            String temp = v.unscaledValue().toString();
            int tdf = ((XmlObjectBase) td).getBigIntegerValue().intValue();
            int origLen = temp.length();
            int len = origLen;
            if (origLen > 0) {
                if (temp.charAt(0) == '-') {
                    len--;
                }
                int insignificantTrailingZeros = 0;
                int vScale = v.scale();
                for (int j = origLen - 1; temp.charAt(j) == '0' && j > 0 && insignificantTrailingZeros < vScale; j--) {
                    insignificantTrailingZeros++;
                }
                len -= insignificantTrailingZeros;
            }
            if (len > tdf) {
                context.invalid(XmlErrorCodes.DATATYPE_TOTAL_DIGITS_VALID, new Object[]{new Integer(len), v.toString(), new Integer(tdf), QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject mine = sType.getFacet(3);
        if (mine != null) {
            BigDecimal m = ((XmlObjectBase) mine).getBigDecimalValue();
            if (v.compareTo(m) <= 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_EXCLUSIVE_VALID, new Object[]{XmlErrorCodes.DECIMAL, v, m, QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject mini = sType.getFacet(4);
        if (mini != null) {
            BigDecimal m2 = ((XmlObjectBase) mini).getBigDecimalValue();
            if (v.compareTo(m2) < 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_INCLUSIVE_VALID, new Object[]{XmlErrorCodes.DECIMAL, v, m2, QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject maxi = sType.getFacet(5);
        if (maxi != null) {
            BigDecimal m3 = ((XmlObjectBase) maxi).getBigDecimalValue();
            if (v.compareTo(m3) > 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MAX_INCLUSIVE_VALID, new Object[]{XmlErrorCodes.DECIMAL, v, m3, QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject maxe = sType.getFacet(6);
        if (maxe != null) {
            BigDecimal m4 = ((XmlObjectBase) maxe).getBigDecimalValue();
            if (v.compareTo(m4) >= 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MAX_EXCLUSIVE_VALID, new Object[]{XmlErrorCodes.DECIMAL, v, m4, QNameHelper.readable(sType)});
                return;
            }
        }
        XmlObject[] vals = sType.getEnumerationValues();
        if (vals != null) {
            for (XmlObject xmlObject : vals) {
                if (v.equals(((XmlObjectBase) xmlObject).getBigDecimalValue())) {
                    return;
                }
            }
            context.invalid(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{XmlErrorCodes.DECIMAL, v, QNameHelper.readable(sType)});
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        validateLexical(lexical, schemaType(), ctx);
        validateValue(getBigDecimalValue(), schemaType(), ctx);
    }
}
