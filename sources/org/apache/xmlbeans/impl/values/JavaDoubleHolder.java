package org.apache.xmlbeans.impl.values;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;
import org.apache.xmlbeans.impl.util.XsTypeConverter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaDoubleHolder.class */
public abstract class JavaDoubleHolder extends XmlObjectBase {
    double _value;

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return BuiltinSchemaTypeSystem.ST_DOUBLE;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return serialize(this._value);
    }

    public static String serialize(double d) {
        if (d == Double.POSITIVE_INFINITY) {
            return "INF";
        }
        if (d == Double.NEGATIVE_INFINITY) {
            return "-INF";
        }
        if (d == Double.NaN) {
            return "NaN";
        }
        return Double.toString(d);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        set_double(validateLexical(s, _voorVc));
    }

    public static double validateLexical(String v, ValidationContext context) {
        try {
            return XsTypeConverter.lexDouble(v);
        } catch (NumberFormatException e) {
            context.invalid(XmlErrorCodes.DOUBLE, new Object[]{v});
            return Double.NaN;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = 0.0d;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public BigDecimal getBigDecimalValue() {
        check_dated();
        return new BigDecimal(this._value);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public double getDoubleValue() {
        check_dated();
        return this._value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public float getFloatValue() {
        check_dated();
        return (float) this._value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_double(double v) {
        this._value = v;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_float(float v) {
        set_double(v);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_long(long v) {
        set_double(v);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigDecimal(BigDecimal v) {
        set_double(v.doubleValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigInteger(BigInteger v) {
        set_double(v.doubleValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int compare_to(XmlObject d) {
        return compare(this._value, ((XmlObjectBase) d).doubleValue());
    }

    static int compare(double thisValue, double thatValue) {
        if (thisValue < thatValue) {
            return -1;
        }
        if (thisValue > thatValue) {
            return 1;
        }
        long thisBits = Double.doubleToLongBits(thisValue);
        long thatBits = Double.doubleToLongBits(thatValue);
        if (thisBits == thatBits) {
            return 0;
        }
        return thisBits < thatBits ? -1 : 1;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject d) {
        return compare(this._value, ((XmlObjectBase) d).doubleValue()) == 0;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        long v = Double.doubleToLongBits(this._value);
        return (int) (((v >> 32) * 19) + v);
    }
}
