package org.apache.xmlbeans.impl.values;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaIntegerHolder.class */
public abstract class JavaIntegerHolder extends XmlObjectBase {
    private BigInteger _value;
    private static BigInteger _maxlong = BigInteger.valueOf(Long.MAX_VALUE);
    private static BigInteger _minlong = BigInteger.valueOf(Long.MIN_VALUE);

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return BuiltinSchemaTypeSystem.ST_INTEGER;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return this._value.toString();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        set_BigInteger(lex(s, _voorVc));
    }

    public static BigInteger lex(String s, ValidationContext vc) {
        if (s.length() > 0 && s.charAt(0) == '+') {
            s = s.substring(1);
        }
        try {
            return new BigInteger(s);
        } catch (Exception e) {
            vc.invalid("integer", new Object[]{s});
            return null;
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = null;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public BigDecimal getBigDecimalValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return new BigDecimal(this._value);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public BigInteger getBigIntegerValue() {
        check_dated();
        return this._value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigDecimal(BigDecimal v) {
        this._value = v.toBigInteger();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigInteger(BigInteger v) {
        this._value = v;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int compare_to(XmlObject i) {
        if (((SimpleValue) i).instanceType().getDecimalSize() > 1000000) {
            return -i.compareTo(this);
        }
        return this._value.compareTo(((XmlObjectBase) i).bigIntegerValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject i) {
        if (((SimpleValue) i).instanceType().getDecimalSize() > 1000000) {
            return i.valueEquals(this);
        }
        return this._value.equals(((XmlObjectBase) i).bigIntegerValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        if (this._value.compareTo(_maxlong) > 0 || this._value.compareTo(_minlong) < 0) {
            return this._value.hashCode();
        }
        long longval = this._value.longValue();
        return (int) (((longval >> 32) * 19) + longval);
    }
}
