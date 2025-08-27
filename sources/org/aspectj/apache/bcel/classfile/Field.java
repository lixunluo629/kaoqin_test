package org.aspectj.apache.bcel.classfile;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataInputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.generic.Type;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Field.class */
public final class Field extends FieldOrMethod {
    public static final Field[] NoFields = new Field[0];
    private Type fieldType;

    private Field() {
        this.fieldType = null;
    }

    public Field(Field field) {
        super(field);
        this.fieldType = null;
    }

    Field(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        super(dataInputStream, constantPool);
        this.fieldType = null;
    }

    public Field(int i, int i2, int i3, Attribute[] attributeArr, ConstantPool constantPool) {
        super(i, i2, i3, attributeArr, constantPool);
        this.fieldType = null;
    }

    @Override // org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitField(this);
    }

    public final ConstantValue getConstantValue() {
        return AttributeUtils.getConstantValueAttribute(this.attributes);
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer(Utility.accessToString(this.modifiers));
        if (stringBuffer.length() > 0) {
            stringBuffer.append(SymbolConstants.SPACE_SYMBOL);
        }
        stringBuffer.append(Utility.signatureToString(getSignature())).append(SymbolConstants.SPACE_SYMBOL).append(getName());
        ConstantValue constantValue = getConstantValue();
        if (constantValue != null) {
            stringBuffer.append(" = ").append(constantValue);
        }
        for (Attribute attribute : this.attributes) {
            if (!(attribute instanceof ConstantValue)) {
                stringBuffer.append(" [").append(attribute.toString()).append("]");
            }
        }
        return stringBuffer.toString();
    }

    public Type getType() {
        if (this.fieldType == null) {
            this.fieldType = Type.getReturnType(getSignature());
        }
        return this.fieldType;
    }
}
