package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.GDurationSpecification;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaGDurationHolderEx.class */
public abstract class JavaGDurationHolderEx extends XmlObjectBase {
    GDuration _value;
    private SchemaType _schemaType;

    public JavaGDurationHolderEx(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        GDuration newVal;
        if (_validateOnSet()) {
            newVal = validateLexical(s, this._schemaType, _voorVc);
        } else {
            newVal = lex(s, _voorVc);
        }
        if (_validateOnSet() && newVal != null) {
            validateValue(newVal, this._schemaType, _voorVc);
        }
        this._value = newVal;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_GDuration(GDurationSpecification v) {
        if (_validateOnSet()) {
            validateValue(v, this._schemaType, _voorVc);
        }
        if (v.isImmutable() && (v instanceof GDuration)) {
            this._value = (GDuration) v;
        } else {
            this._value = new GDuration(v);
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected String compute_text(NamespaceManager nsm) {
        return this._value == null ? "" : this._value.toString();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._value = null;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public GDuration getGDurationValue() {
        check_dated();
        if (this._value == null) {
            return null;
        }
        return this._value;
    }

    public static GDuration lex(String v, ValidationContext context) {
        GDuration duration = null;
        try {
            duration = new GDuration(v);
        } catch (Exception e) {
            context.invalid(XmlErrorCodes.DURATION, new Object[]{v});
        }
        return duration;
    }

    public static GDuration validateLexical(String v, SchemaType sType, ValidationContext context) {
        GDuration duration = lex(v, context);
        if (duration != null && sType.hasPatternFacet() && !sType.matchPatternFacet(v)) {
            context.invalid(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{XmlErrorCodes.DURATION, v, QNameHelper.readable(sType)});
        }
        return duration;
    }

    public static void validateValue(GDurationSpecification v, SchemaType sType, ValidationContext context) {
        XmlObject x = sType.getFacet(3);
        if (x != null) {
            GDuration g = ((XmlObjectBase) x).gDurationValue();
            if (v.compareToGDuration(g) <= 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_EXCLUSIVE_VALID, new Object[]{XmlErrorCodes.DURATION, v, g, QNameHelper.readable(sType)});
            }
        }
        XmlObject x2 = sType.getFacet(4);
        if (x2 != null) {
            GDuration g2 = ((XmlObjectBase) x2).gDurationValue();
            if (v.compareToGDuration(g2) < 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_INCLUSIVE_VALID, new Object[]{XmlErrorCodes.DURATION, v, g2, QNameHelper.readable(sType)});
            }
        }
        XmlObject x3 = sType.getFacet(6);
        if (x3 != null) {
            GDuration g3 = ((XmlObjectBase) x3).gDurationValue();
            if (v.compareToGDuration(g3) >= 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MAX_EXCLUSIVE_VALID, new Object[]{XmlErrorCodes.DURATION, v, g3, QNameHelper.readable(sType)});
            }
        }
        XmlObject x4 = sType.getFacet(5);
        if (x4 != null) {
            GDuration g4 = ((XmlObjectBase) x4).gDurationValue();
            if (v.compareToGDuration(g4) > 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MAX_INCLUSIVE_VALID, new Object[]{XmlErrorCodes.DURATION, v, g4, QNameHelper.readable(sType)});
            }
        }
        XmlObject[] vals = sType.getEnumerationValues();
        if (vals != null) {
            for (XmlObject xmlObject : vals) {
                if (v.compareToGDuration(((XmlObjectBase) xmlObject).gDurationValue()) == 0) {
                    return;
                }
            }
            context.invalid(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{XmlErrorCodes.DURATION, v, QNameHelper.readable(sType)});
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int compare_to(XmlObject d) {
        return this._value.compareToGDuration(((XmlObjectBase) d).gDurationValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected boolean equal_to(XmlObject d) {
        return this._value.equals(((XmlObjectBase) d).gDurationValue());
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected int value_hash_code() {
        return this._value.hashCode();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        validateLexical(lexical, schemaType(), ctx);
        validateValue(gDurationValue(), schemaType(), ctx);
    }
}
