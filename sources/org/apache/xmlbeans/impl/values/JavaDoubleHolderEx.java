package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaDoubleHolderEx.class */
public abstract class JavaDoubleHolderEx extends JavaDoubleHolder {
    private SchemaType _schemaType;

    public JavaDoubleHolderEx(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaDoubleHolder, org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    @Override // org.apache.xmlbeans.impl.values.JavaDoubleHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_double(double v) {
        if (_validateOnSet()) {
            validateValue(v, this._schemaType, _voorVc);
        }
        super.set_double(v);
    }

    public static double validateLexical(String v, SchemaType sType, ValidationContext context) {
        double d = JavaDoubleHolder.validateLexical(v, context);
        if (!sType.matchPatternFacet(v)) {
            context.invalid(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{XmlErrorCodes.DOUBLE, v, QNameHelper.readable(sType)});
        }
        return d;
    }

    public static void validateValue(double v, SchemaType sType, ValidationContext context) {
        XmlObject x = sType.getFacet(3);
        if (x != null) {
            double d = ((XmlObjectBase) x).doubleValue();
            if (compare(v, d) <= 0) {
                context.invalid(XmlErrorCodes.DATATYPE_MIN_EXCLUSIVE_VALID, new Object[]{XmlErrorCodes.DOUBLE, new Double(v), new Double(d), QNameHelper.readable(sType)});
            }
        }
        XmlObject x2 = sType.getFacet(4);
        if (x2 != null && compare(v, ((XmlObjectBase) x2).doubleValue()) < 0) {
            context.invalid(XmlErrorCodes.DATATYPE_MIN_INCLUSIVE_VALID, new Object[]{XmlErrorCodes.DOUBLE, new Double(v), new Double(v), QNameHelper.readable(sType)});
        }
        XmlObject x3 = sType.getFacet(5);
        if (x3 != null && compare(v, ((XmlObjectBase) x3).doubleValue()) > 0) {
            context.invalid(XmlErrorCodes.DATATYPE_MAX_INCLUSIVE_VALID, new Object[]{XmlErrorCodes.DOUBLE, new Double(v), new Double(v), QNameHelper.readable(sType)});
        }
        XmlObject x4 = sType.getFacet(6);
        if (x4 != null && compare(v, ((XmlObjectBase) x4).doubleValue()) >= 0) {
            context.invalid(XmlErrorCodes.DATATYPE_MAX_EXCLUSIVE_VALID, new Object[]{XmlErrorCodes.DOUBLE, new Double(v), new Double(v), QNameHelper.readable(sType)});
        }
        XmlObject[] vals = sType.getEnumerationValues();
        if (vals != null) {
            for (XmlObject xmlObject : vals) {
                if (compare(v, ((XmlObjectBase) xmlObject).doubleValue()) == 0) {
                    return;
                }
            }
            context.invalid(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{XmlErrorCodes.DOUBLE, new Double(v), QNameHelper.readable(sType)});
        }
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        validateLexical(lexical, schemaType(), ctx);
        validateValue(doubleValue(), schemaType(), ctx);
    }
}
