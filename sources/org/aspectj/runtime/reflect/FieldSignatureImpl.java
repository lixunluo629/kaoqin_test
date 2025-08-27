package org.aspectj.runtime.reflect;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Field;
import org.aspectj.lang.reflect.FieldSignature;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/reflect/FieldSignatureImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/reflect/FieldSignatureImpl.class */
public class FieldSignatureImpl extends MemberSignatureImpl implements FieldSignature {
    Class fieldType;
    private Field field;

    FieldSignatureImpl(int modifiers, String name, Class declaringType, Class fieldType) {
        super(modifiers, name, declaringType);
        this.fieldType = fieldType;
    }

    FieldSignatureImpl(String stringRep) {
        super(stringRep);
    }

    @Override // org.aspectj.lang.reflect.FieldSignature
    public Class getFieldType() {
        if (this.fieldType == null) {
            this.fieldType = extractType(3);
        }
        return this.fieldType;
    }

    @Override // org.aspectj.runtime.reflect.SignatureImpl
    protected String createToString(StringMaker sm) {
        StringBuffer buf = new StringBuffer();
        buf.append(sm.makeModifiersString(getModifiers()));
        if (sm.includeArgs) {
            buf.append(sm.makeTypeName(getFieldType()));
        }
        if (sm.includeArgs) {
            buf.append(SymbolConstants.SPACE_SYMBOL);
        }
        buf.append(sm.makePrimaryTypeName(getDeclaringType(), getDeclaringTypeName()));
        buf.append(".");
        buf.append(getName());
        return buf.toString();
    }

    @Override // org.aspectj.lang.reflect.FieldSignature
    public Field getField() {
        if (this.field == null) {
            try {
                this.field = getDeclaringType().getDeclaredField(getName());
            } catch (Exception e) {
            }
        }
        return this.field;
    }
}
