package org.apache.xmlbeans.impl.values;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;
import org.apache.xmlbeans.impl.util.XsTypeConverter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaDecimalHolder.class */
public class JavaDecimalHolder extends XmlObjectBase {
    private BigDecimal _value;
    private static BigInteger _maxlong;
    private static BigInteger _minlong;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JavaDecimalHolder.class.desiredAssertionStatus();
        _maxlong = BigInteger.valueOf(Long.MAX_VALUE);
        _minlong = BigInteger.valueOf(Long.MIN_VALUE);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return BuiltinSchemaTypeSystem.ST_DECIMAL;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return XsTypeConverter.printDecimal(this._value);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        if (_validateOnSet()) {
            validateLexical(s, _voorVc);
        }
        try {
            set_BigDecimal(new BigDecimal(s));
        } catch (NumberFormatException e) {
            _voorVc.invalid(XmlErrorCodes.DECIMAL, new Object[]{s});
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = null;
    }

    public static void validateLexical(String v, ValidationContext context) {
        int ch2;
        int l = v.length();
        int i = 0;
        if (0 < l && ((ch2 = v.charAt(0)) == 43 || ch2 == 45)) {
            i = 0 + 1;
        }
        boolean sawDot = false;
        boolean sawDigit = false;
        while (i < l) {
            int ch3 = v.charAt(i);
            if (ch3 == 46) {
                if (sawDot) {
                    context.invalid(XmlErrorCodes.DECIMAL, new Object[]{"saw '.' more than once: " + v});
                    return;
                }
                sawDot = true;
            } else if (ch3 >= 48 && ch3 <= 57) {
                sawDigit = true;
            } else {
                context.invalid(XmlErrorCodes.DECIMAL, new Object[]{"unexpected char '" + ch3 + "'"});
                return;
            }
            i++;
        }
        if (!sawDigit) {
            context.invalid(XmlErrorCodes.DECIMAL, new Object[]{"expected at least one digit"});
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public BigDecimal getBigDecimalValue() {
        check_dated();
        return this._value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigDecimal(BigDecimal v) {
        this._value = v;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int compare_to(XmlObject decimal) {
        return this._value.compareTo(((XmlObjectBase) decimal).bigDecimalValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject decimal) {
        return this._value.compareTo(((XmlObjectBase) decimal).bigDecimalValue()) == 0;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        if (this._value.scale() > 0 && this._value.setScale(0, 1).compareTo(this._value) != 0) {
            return decimalHashCode();
        }
        BigInteger intval = this._value.toBigInteger();
        if (intval.compareTo(_maxlong) > 0 || intval.compareTo(_minlong) < 0) {
            return intval.hashCode();
        }
        long longval = intval.longValue();
        return (int) (((longval >> 32) * 19) + longval);
    }

    protected int decimalHashCode() {
        if (!$assertionsDisabled && this._value.scale() <= 0) {
            throw new AssertionError();
        }
        String strValue = this._value.toString();
        int i = strValue.length() - 1;
        while (i >= 0 && strValue.charAt(i) == '0') {
            i--;
        }
        if ($assertionsDisabled || strValue.indexOf(46) < i) {
            return strValue.substring(0, i + 1).hashCode();
        }
        throw new AssertionError();
    }
}
