package org.apache.xmlbeans.impl.values;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;
import org.apache.xmlbeans.impl.util.XsTypeConverter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaLongHolder.class */
public abstract class JavaLongHolder extends XmlObjectBase {
    private long _value;
    private static final BigInteger _max = BigInteger.valueOf(Long.MAX_VALUE);
    private static final BigInteger _min = BigInteger.valueOf(Long.MIN_VALUE);

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return BuiltinSchemaTypeSystem.ST_LONG;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return Long.toString(this._value);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        try {
            set_long(XsTypeConverter.lexLong(s));
        } catch (Exception e) {
            throw new XmlValueOutOfRangeException(XmlErrorCodes.LONG, new Object[]{s});
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = 0L;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public BigDecimal getBigDecimalValue() {
        check_dated();
        return BigDecimal.valueOf(this._value);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public BigInteger getBigIntegerValue() {
        check_dated();
        return BigInteger.valueOf(this._value);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public long getLongValue() {
        check_dated();
        return this._value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigDecimal(BigDecimal v) {
        set_BigInteger(v.toBigInteger());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigInteger(BigInteger v) {
        if (v.compareTo(_max) > 0 || v.compareTo(_min) < 0) {
            throw new XmlValueOutOfRangeException();
        }
        this._value = v.longValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_long(long l) {
        this._value = l;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int compare_to(XmlObject l) {
        if (((SimpleValue) l).instanceType().getDecimalSize() > 64) {
            return -l.compareTo(this);
        }
        if (this._value == ((XmlObjectBase) l).longValue()) {
            return 0;
        }
        return this._value < ((XmlObjectBase) l).longValue() ? -1 : 1;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject l) {
        if (((SimpleValue) l).instanceType().getDecimalSize() > 64) {
            return l.valueEquals(this);
        }
        return this._value == ((XmlObjectBase) l).longValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        return (int) (((this._value >> 32) * 19) + this._value);
    }
}
