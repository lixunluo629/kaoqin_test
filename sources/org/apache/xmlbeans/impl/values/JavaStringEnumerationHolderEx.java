package org.apache.xmlbeans.impl.values;

import io.swagger.models.properties.StringProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaStringEnumerationHolderEx.class */
public abstract class JavaStringEnumerationHolderEx extends JavaStringHolderEx {
    private StringEnumAbstractBase _val;

    public JavaStringEnumerationHolderEx(SchemaType type, boolean complex) {
        super(type, complex);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaStringHolderEx, org.apache.xmlbeans.impl.values.JavaStringHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        StringEnumAbstractBase se = schemaType().enumForString(s);
        if (se == null) {
            throw new XmlValueOutOfRangeException(XmlErrorCodes.DATATYPE_ENUM_VALID, new Object[]{StringProperty.TYPE, s, QNameHelper.readable(schemaType())});
        }
        super.set_text(s);
        this._val = se;
    }

    public static void validateLexical(String v, SchemaType sType, ValidationContext context) {
        JavaStringHolderEx.validateLexical(v, sType, context);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaStringHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_nil() {
        this._val = null;
        super.set_nil();
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.SimpleValue
    public StringEnumAbstractBase getEnumValue() {
        check_dated();
        return this._val;
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_enum(StringEnumAbstractBase se) {
        Class ejc = schemaType().getEnumJavaClass();
        if (ejc != null && !se.getClass().equals(ejc)) {
            throw new XmlValueOutOfRangeException();
        }
        super.set_text(se.toString());
        this._val = se;
    }
}
