package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ValidationContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/JavaBooleanHolderEx.class */
public abstract class JavaBooleanHolderEx extends JavaBooleanHolder {
    private SchemaType _schemaType;

    @Override // org.apache.xmlbeans.impl.values.JavaBooleanHolder, org.apache.xmlbeans.impl.values.XmlObjectBase, org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return this._schemaType;
    }

    public static boolean validateLexical(String v, SchemaType sType, ValidationContext context) {
        boolean b = JavaBooleanHolder.validateLexical(v, context);
        validatePattern(v, sType, context);
        return b;
    }

    public static void validatePattern(String v, SchemaType sType, ValidationContext context) {
        if (!sType.matchPatternFacet(v)) {
            context.invalid(XmlErrorCodes.DATATYPE_VALID$PATTERN_VALID, new Object[]{"boolean", v, QNameHelper.readable(sType)});
        }
    }

    public JavaBooleanHolderEx(SchemaType type, boolean complex) {
        this._schemaType = type;
        initComplexType(complex, false);
    }

    @Override // org.apache.xmlbeans.impl.values.JavaBooleanHolder, org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void set_text(String s) {
        if (_validateOnSet()) {
            validatePattern(s, this._schemaType, _voorVc);
        }
        super.set_text(s);
    }

    @Override // org.apache.xmlbeans.impl.values.XmlObjectBase
    protected void validate_simpleval(String lexical, ValidationContext ctx) {
        validateLexical(lexical, schemaType(), ctx);
    }
}
