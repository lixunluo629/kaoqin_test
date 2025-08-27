package org.apache.xmlbeans.impl.values;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.GDate;
import org.apache.xmlbeans.GDateSpecification;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.GDurationSpecification;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/XmlUnionImpl.class */
public class XmlUnionImpl extends XmlObjectBase implements XmlAnySimpleType {
    private SchemaType _schemaType;
    private XmlAnySimpleType _value;
    private String _textvalue = "";
    private static final int JAVA_NUMBER = 47;
    private static final int JAVA_DATE = 48;
    private static final int JAVA_CALENDAR = 49;
    private static final int JAVA_BYTEARRAY = 50;
    private static final int JAVA_LIST = 51;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XmlUnionImpl.class.desiredAssertionStatus();
    }

    public XmlUnionImpl(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public SchemaType instanceType() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).instanceType();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return this._textvalue;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean is_defaultable_ws(String v) {
        try {
            XmlAnySimpleType savedValue = this._value;
            set_text(v);
            this._value = savedValue;
            return false;
        } catch (XmlValueOutOfRangeException e) {
            return true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x010d, code lost:
    
        r8._textvalue = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x012d, code lost:
    
        throw new org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException(org.apache.xmlbeans.XmlErrorCodes.DATATYPE_VALID$UNION, new java.lang.Object[]{r9, org.apache.xmlbeans.impl.common.QNameHelper.readable(r8._schemaType)});
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0085  */
    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void set_text(java.lang.String r9) {
        /*
            Method dump skipped, instructions count: 302
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.values.XmlUnionImpl.set_text(java.lang.String):void");
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = null;
        this._textvalue = null;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int get_wscanon_rule() {
        return 1;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public float getFloatValue() {
        check_dated();
        if (this._value == null) {
            return 0.0f;
        }
        return ((SimpleValue) this._value).getFloatValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public double getDoubleValue() {
        check_dated();
        if (this._value == null) {
            return 0.0d;
        }
        return ((SimpleValue) this._value).getDoubleValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public BigDecimal getBigDecimalValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).getBigDecimalValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public BigInteger getBigIntegerValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).getBigIntegerValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public byte getByteValue() {
        check_dated();
        if (this._value == null) {
            return (byte) 0;
        }
        return ((SimpleValue) this._value).getByteValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public short getShortValue() {
        check_dated();
        if (this._value == null) {
            return (short) 0;
        }
        return ((SimpleValue) this._value).getShortValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public int getIntValue() {
        check_dated();
        if (this._value == null) {
            return 0;
        }
        return ((SimpleValue) this._value).getIntValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public long getLongValue() {
        check_dated();
        if (this._value == null) {
            return 0L;
        }
        return ((SimpleValue) this._value).getLongValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public byte[] getByteArrayValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).getByteArrayValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public boolean getBooleanValue() {
        check_dated();
        if (this._value == null) {
            return false;
        }
        return ((SimpleValue) this._value).getBooleanValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public Calendar getCalendarValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).getCalendarValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public Date getDateValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).getDateValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public GDate getGDateValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).getGDateValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public GDuration getGDurationValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).getGDurationValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public QName getQNameValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).getQNameValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public List getListValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).getListValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public List xgetListValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).xgetListValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public StringEnumAbstractBase getEnumValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return ((SimpleValue) this._value).getEnumValue();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public String getStringValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return this._value.getStringValue();
    }

    static boolean lexical_overlap(int source, int target) {
        if (source != target && source != 2 && target != 2 && source != 12 && target != 12 && source != 6 && target != 6) {
            switch (source) {
                case 3:
                    switch (target) {
                        case 7:
                        case 8:
                            break;
                    }
                case 4:
                    switch (target) {
                    }
                case 5:
                    switch (target) {
                    }
                case 7:
                case 8:
                    switch (target) {
                    }
                case 9:
                case 10:
                case 11:
                case 18:
                    switch (target) {
                    }
                case 13:
                    switch (target) {
                    }
            }
            return true;
        }
        return true;
    }

    private static boolean logical_overlap(SchemaType type, int javacode) {
        if (!$assertionsDisabled && type.getSimpleVariety() == 2) {
            throw new AssertionError();
        }
        if (javacode <= 46) {
            return type.getSimpleVariety() == 1 && type.getPrimitiveType().getBuiltinTypeCode() == javacode;
        }
        switch (javacode) {
            case 47:
                if (type.getSimpleVariety() != 1) {
                    return false;
                }
                switch (type.getPrimitiveType().getBuiltinTypeCode()) {
                    case 9:
                    case 10:
                    case 11:
                    case 18:
                    case 20:
                    case 21:
                        return true;
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 19:
                    default:
                        return false;
                }
            case 48:
                if (type.getSimpleVariety() != 1) {
                    return false;
                }
                switch (type.getPrimitiveType().getBuiltinTypeCode()) {
                    case 14:
                    case 16:
                        return true;
                    default:
                        return false;
                }
            case 49:
                if (type.getSimpleVariety() != 1) {
                    return false;
                }
                switch (type.getPrimitiveType().getBuiltinTypeCode()) {
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                        return true;
                    default:
                        return false;
                }
            case 50:
                if (type.getSimpleVariety() != 1) {
                    return false;
                }
                switch (type.getPrimitiveType().getBuiltinTypeCode()) {
                    case 4:
                    case 5:
                        return true;
                    default:
                        return false;
                }
            case 51:
                return type.getSimpleVariety() == 3;
            default:
                if ($assertionsDisabled) {
                    return false;
                }
                throw new AssertionError("missing case");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void set_primitive(int r9, java.lang.Object r10) {
        /*
            Method dump skipped, instructions count: 256
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.values.XmlUnionImpl.set_primitive(int, java.lang.Object):void");
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_boolean(boolean v) {
        set_primitive(3, new Boolean(v));
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_byte(byte v) {
        set_primitive(47, new Byte(v));
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_short(short v) {
        set_primitive(47, new Short(v));
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_int(int v) {
        set_primitive(47, new Integer(v));
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_long(long v) {
        set_primitive(47, new Long(v));
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_float(float v) {
        set_primitive(47, new Float(v));
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_double(double v) {
        set_primitive(47, new Double(v));
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_ByteArray(byte[] b) {
        set_primitive(50, b);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_hex(byte[] b) {
        set_primitive(50, b);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_b64(byte[] b) {
        set_primitive(50, b);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigInteger(BigInteger v) {
        set_primitive(47, v);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_BigDecimal(BigDecimal v) {
        set_primitive(47, v);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_QName(QName v) {
        set_primitive(7, v);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_Calendar(Calendar c) {
        set_primitive(49, c);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_Date(Date d) {
        set_primitive(48, d);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_GDate(GDateSpecification d) {
        int btc = d.getBuiltinTypeCode();
        if (btc <= 0) {
            throw new XmlValueOutOfRangeException();
        }
        set_primitive(btc, d);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_GDuration(GDurationSpecification d) {
        set_primitive(13, d);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_enum(StringEnumAbstractBase e) {
        set_primitive(12, e);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_list(List v) {
        set_primitive(51, v);
    }

    protected void set_xmlfloat(XmlObject v) {
        set_primitive(9, v);
    }

    protected void set_xmldouble(XmlObject v) {
        set_primitive(10, v);
    }

    protected void set_xmldecimal(XmlObject v) {
        set_primitive(11, v);
    }

    protected void set_xmlduration(XmlObject v) {
        set_primitive(13, v);
    }

    protected void set_xmldatetime(XmlObject v) {
        set_primitive(14, v);
    }

    protected void set_xmltime(XmlObject v) {
        set_primitive(15, v);
    }

    protected void set_xmldate(XmlObject v) {
        set_primitive(16, v);
    }

    protected void set_xmlgyearmonth(XmlObject v) {
        set_primitive(17, v);
    }

    protected void set_xmlgyear(XmlObject v) {
        set_primitive(18, v);
    }

    protected void set_xmlgmonthday(XmlObject v) {
        set_primitive(19, v);
    }

    protected void set_xmlgday(XmlObject v) {
        set_primitive(20, v);
    }

    protected void set_xmlgmonth(XmlObject v) {
        set_primitive(21, v);
    }

    private static boolean check(XmlObject v, SchemaType sType) {
        XmlObject[] vals = sType.getEnumerationValues();
        if (vals != null) {
            for (XmlObject xmlObject : vals) {
                if (xmlObject.valueEquals(v)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject xmlobj) {
        return this._value.valueEquals(xmlobj);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        return this._value.hashCode();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        try {
            check_dated();
            if (this._value == null) {
                ctx.invalid(XmlErrorCodes.UNION, new Object[]{"'" + lexical + "' does not match any of the member types for " + QNameHelper.readable(schemaType())});
            } else {
                ((XmlObjectBase) this._value).validate_simpleval(lexical, ctx);
            }
        } catch (Exception e) {
            ctx.invalid(XmlErrorCodes.UNION, new Object[]{"'" + lexical + "' does not match any of the member types for " + QNameHelper.readable(schemaType())});
        }
    }
}
