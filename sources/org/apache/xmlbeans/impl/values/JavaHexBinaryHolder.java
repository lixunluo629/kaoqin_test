package org.apache.xmlbeans.impl.values;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlHexBinary;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;
import org.apache.xmlbeans.impl.util.HexBin;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaHexBinaryHolder.class */
public abstract class JavaHexBinaryHolder extends XmlObjectBase {
    protected byte[] _value;
    protected boolean _hashcached = false;
    protected int hashcode = 0;
    protected static MessageDigest md5;

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return BuiltinSchemaTypeSystem.ST_HEX_BINARY;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return new String(HexBin.encode(this._value));
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        this._hashcached = false;
        if (_validateOnSet()) {
            this._value = validateLexical(s, schemaType(), _voorVc);
        } else {
            this._value = lex(s, _voorVc);
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._hashcached = false;
        this._value = null;
    }

    public static byte[] lex(String v, ValidationContext context) throws UnsupportedEncodingException {
        byte[] vBytes = null;
        try {
            vBytes = v.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        byte[] bytes = HexBin.decode(vBytes);
        if (bytes == null) {
            context.invalid(XmlErrorCodes.HEXBINARY, new Object[]{"not encoded properly"});
        }
        return bytes;
    }

    public static byte[] validateLexical(String v, SchemaType sType, ValidationContext context) throws UnsupportedEncodingException {
        byte[] bytes = lex(v, context);
        if (bytes == null) {
            return null;
        }
        if (!sType.matchPatternFacet(v)) {
            context.invalid("Hex encoded data does not match pattern for " + QNameHelper.readable(sType));
            return null;
        }
        return bytes;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public byte[] getByteArrayValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        byte[] result = new byte[this._value.length];
        System.arraycopy(this._value, 0, result, 0, this._value.length);
        return result;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_ByteArray(byte[] ba) {
        this._hashcached = false;
        this._value = new byte[ba.length];
        System.arraycopy(ba, 0, this._value, 0, ba.length);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject i) {
        byte[] ival = ((XmlHexBinary) i).getByteArrayValue();
        return Arrays.equals(this._value, ival);
    }

    static {
        try {
            md5 = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Cannot find MD5 hash Algorithm");
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        if (this._hashcached) {
            return this.hashcode;
        }
        this._hashcached = true;
        if (this._value == null) {
            this.hashcode = 0;
            return 0;
        }
        byte[] res = md5.digest(this._value);
        int i = ((res[0] << (24 + res[1])) << (16 + res[2])) << (8 + res[3]);
        this.hashcode = i;
        return i;
    }
}
