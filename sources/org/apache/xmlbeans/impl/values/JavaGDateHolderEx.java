package org.apache.xmlbeans.impl.values;

import java.util.Calendar;
import java.util.Date;
import org.apache.xmlbeans.GDate;
import org.apache.xmlbeans.GDateBuilder;
import org.apache.xmlbeans.GDateSpecification;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaGDateHolderEx.class */
public abstract class JavaGDateHolderEx extends XmlObjectBase {
    private SchemaType _schemaType;
    private GDate _value;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JavaGDateHolderEx.class.desiredAssertionStatus();
    }

    public JavaGDateHolderEx(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return this._value == null ? "" : this._value.toString();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        GDate newVal;
        if (_validateOnSet()) {
            newVal = validateLexical(s, this._schemaType, _voorVc);
        } else {
            newVal = lex(s, this._schemaType, _voorVc);
        }
        if (_validateOnSet() && newVal != null) {
            validateValue(newVal, this._schemaType, _voorVc);
        }
        this._value = newVal;
    }

    public static GDate lex(String v, SchemaType sType, ValidationContext context) {
        GDate date = null;
        try {
            date = new GDate(v);
        } catch (Exception e) {
            context.invalid("date", new Object[]{v});
        }
        if (date != null) {
            if (date.getBuiltinTypeCode() != sType.getPrimitiveType().getBuiltinTypeCode()) {
                context.invalid("date", new Object[]{"wrong type: " + v});
                date = null;
            } else if (!date.isValid()) {
                context.invalid("date", new Object[]{v});
                date = null;
            }
        }
        return date;
    }

    public static GDate validateLexical(String v, SchemaType sType, ValidationContext context) {
        GDate date = lex(v, sType, context);
        if (date != null && sType.hasPatternFacet() && !sType.matchPatternFacet(v)) {
            context.invalid(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{"date", v, QNameHelper.readable(sType)});
        }
        return date;
    }

    public static void validateValue(GDateSpecification v, SchemaType sType, ValidationContext context) {
        if (v.getBuiltinTypeCode() != sType.getPrimitiveType().getBuiltinTypeCode()) {
            context.invalid("date", new Object[]{"Date (" + v + ") does not have the set of fields required for " + QNameHelper.readable(sType)});
        }
        XmlObject x = sType.getFacet(3);
        if (x != null) {
            GDate g = ((XmlObjectBase) x).gDateValue();
            if (v.compareToGDate(g) <= 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_EXCLUSIVE_VALID, new Object[]{"date", v, g, QNameHelper.readable(sType)});
            }
        }
        XmlObject x2 = sType.getFacet(4);
        if (x2 != null) {
            GDate g2 = ((XmlObjectBase) x2).gDateValue();
            if (v.compareToGDate(g2) < 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_INCLUSIVE_VALID, new Object[]{"date", v, g2, QNameHelper.readable(sType)});
            }
        }
        XmlObject x3 = sType.getFacet(6);
        if (x3 != null) {
            GDate g3 = ((XmlObjectBase) x3).gDateValue();
            if (v.compareToGDate(g3) >= 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MAX_EXCLUSIVE_VALID, new Object[]{"date", v, g3, QNameHelper.readable(sType)});
            }
        }
        XmlObject x4 = sType.getFacet(5);
        if (x4 != null) {
            GDate g4 = ((XmlObjectBase) x4).gDateValue();
            if (v.compareToGDate(g4) > 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MAX_INCLUSIVE_VALID, new Object[]{"date", v, g4, QNameHelper.readable(sType)});
            }
        }
        XmlObject[] vals = sType.getEnumerationValues();
        if (vals != null) {
            for (XmlObject xmlObject : vals) {
                if (v.compareToGDate(((XmlObjectBase) xmlObject).gDateValue()) == 0) {
                    return;
                }
            }
            context.invalid(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{"date", v, QNameHelper.readable(sType)});
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = null;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public int getIntValue() {
        int code = schemaType().getPrimitiveType().getBuiltinTypeCode();
        if (code != 20 && code != 21 && code != 18) {
            throw new XmlValueOutOfRangeException();
        }
        check_dated();
        if (this._value == null) {
            return 0;
        }
        switch (code) {
            case 18:
                return this._value.getYear();
            case 19:
            default:
                if ($assertionsDisabled) {
                    throw new IllegalStateException();
                }
                throw new AssertionError();
            case 20:
                return this._value.getDay();
            case 21:
                return this._value.getMonth();
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public GDate getGDateValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return this._value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public Calendar getCalendarValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return this._value.getCalendar();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public Date getDateValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return this._value.getDate();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_int(int v) {
        int code = schemaType().getPrimitiveType().getBuiltinTypeCode();
        if (code != 20 && code != 21 && code != 18) {
            throw new XmlValueOutOfRangeException();
        }
        GDateBuilder value = new GDateBuilder();
        switch (code) {
            case 18:
                value.setYear(v);
                break;
            case 20:
                value.setDay(v);
                break;
            case 21:
                value.setMonth(v);
                break;
        }
        if (_validateOnSet()) {
            validateValue(value, this._schemaType, _voorVc);
        }
        this._value = value.toGDate();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_GDate(GDateSpecification v) {
        GDate candidate;
        int code = schemaType().getPrimitiveType().getBuiltinTypeCode();
        if (v.isImmutable() && (v instanceof GDate) && v.getBuiltinTypeCode() == code) {
            candidate = (GDate) v;
        } else {
            if (v.getBuiltinTypeCode() != code) {
                GDateBuilder gDateBuilder = new GDateBuilder(v);
                gDateBuilder.setBuiltinTypeCode(code);
                v = gDateBuilder;
            }
            candidate = new GDate(v);
        }
        if (_validateOnSet()) {
            validateValue(candidate, this._schemaType, _voorVc);
        }
        this._value = candidate;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_Calendar(Calendar c) {
        int code = schemaType().getPrimitiveType().getBuiltinTypeCode();
        GDateBuilder gDateBuilder = new GDateBuilder(c);
        gDateBuilder.setBuiltinTypeCode(code);
        GDate value = gDateBuilder.toGDate();
        if (_validateOnSet()) {
            validateValue(value, this._schemaType, _voorVc);
        }
        this._value = value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_Date(Date v) {
        int code = schemaType().getPrimitiveType().getBuiltinTypeCode();
        if ((code != 16 && code != 14) || v == null) {
            throw new XmlValueOutOfRangeException();
        }
        GDateBuilder gDateBuilder = new GDateBuilder(v);
        gDateBuilder.setBuiltinTypeCode(code);
        GDate value = gDateBuilder.toGDate();
        if (_validateOnSet()) {
            validateValue(value, this._schemaType, _voorVc);
        }
        this._value = value;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int compare_to(XmlObject obj) {
        return this._value.compareToGDate(((XmlObjectBase) obj).gDateValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject obj) {
        return this._value.equals(((XmlObjectBase) obj).gDateValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        return this._value.hashCode();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        validateLexical(lexical, schemaType(), ctx);
        validateValue(gDateValue(), schemaType(), ctx);
    }
}
