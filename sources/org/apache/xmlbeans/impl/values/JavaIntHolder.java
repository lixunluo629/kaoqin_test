package org.apache.xmlbeans.impl.values;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;
import org.apache.xmlbeans.impl.util.XsTypeConverter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaIntHolder.class */
public abstract class JavaIntHolder extends XmlObjectBase {
    private int _value;
    static final BigInteger _max = BigInteger.valueOf(2147483647L);
    static final BigInteger _min = BigInteger.valueOf(-2147483648L);

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return BuiltinSchemaTypeSystem.ST_INT;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    public String compute_text(NamespaceManager nsm) {
        return Long.toString(this._value);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        try {
            set_int(XsTypeConverter.lexInt(s));
        } catch (Exception e) {
            throw new XmlValueOutOfRangeException(XmlErrorCodes.INT, new Object[]{s});
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = 0;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public BigDecimal getBigDecimalValue() {
        check_dated();
        return new BigDecimal(this._value);
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

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public int getIntValue() {
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
        set_int(v.intValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_long(long l) {
        if (l > 2147483647L || l < -2147483648L) {
            throw new XmlValueOutOfRangeException();
        }
        set_int((int) l);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_int(int i) {
        this._value = i;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int compare_to(XmlObject i) {
        if (((SimpleValue) i).instanceType().getDecimalSize() > 32) {
            return -i.compareTo(this);
        }
        if (this._value == ((XmlObjectBase) i).intValue()) {
            return 0;
        }
        return this._value < ((XmlObjectBase) i).intValue() ? -1 : 1;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject i) {
        if (((SimpleValue) i).instanceType().getDecimalSize() > 32) {
            return i.valueEquals(this);
        }
        return this._value == ((XmlObjectBase) i).intValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        return this._value;
    }
}
