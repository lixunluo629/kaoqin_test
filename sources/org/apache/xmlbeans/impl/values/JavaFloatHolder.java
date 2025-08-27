package org.apache.xmlbeans.impl.values;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;
import org.apache.xmlbeans.impl.util.XsTypeConverter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaFloatHolder.class */
public abstract class JavaFloatHolder extends XmlObjectBase {
    private float _value;

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return BuiltinSchemaTypeSystem.ST_FLOAT;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return serialize(this._value);
    }

    public static String serialize(float f) {
        if (f == Float.POSITIVE_INFINITY) {
            return "INF";
        }
        if (f == Float.NEGATIVE_INFINITY) {
            return "-INF";
        }
        if (f == Float.NaN) {
            return "NaN";
        }
        return Float.toString(f);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        set_float(validateLexical(s, _voorVc));
    }

    public static float validateLexical(String v, ValidationContext context) {
        try {
            return XsTypeConverter.lexFloat(v);
        } catch (NumberFormatException e) {
            context.invalid(XmlErrorCodes.FLOAT, new Object[]{v});
            return Float.NaN;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = 0.0f;
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
        return this._value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_double(double v) {
        set_float((float) v);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_float(float v) {
        this._value = v;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_long(long v) {
        set_float(v);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigDecimal(BigDecimal v) {
        set_float(v.floatValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigInteger(BigInteger v) {
        set_float(v.floatValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int compare_to(XmlObject f) {
        return compare(this._value, ((XmlObjectBase) f).floatValue());
    }

    static int compare(float thisValue, float thatValue) {
        if (thisValue < thatValue) {
            return -1;
        }
        if (thisValue > thatValue) {
            return 1;
        }
        int thisBits = Float.floatToIntBits(thisValue);
        int thatBits = Float.floatToIntBits(thatValue);
        if (thisBits == thatBits) {
            return 0;
        }
        return thisBits < thatBits ? -1 : 1;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject f) {
        return compare(this._value, ((XmlObjectBase) f).floatValue()) == 0;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        return Float.floatToIntBits(this._value);
    }
}
