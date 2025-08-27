package org.aspectj.apache.bcel.classfile.annotation;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.ConstantUtf8;
import org.aspectj.apache.bcel.generic.ObjectType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/EnumElementValue.class */
public class EnumElementValue extends ElementValue {
    private int typeIdx;
    private int valueIdx;

    protected EnumElementValue(int i, int i2, ConstantPool constantPool) {
        super(101, constantPool);
        if (this.type != 101) {
            throw new RuntimeException("Only element values of type enum can be built with this ctor");
        }
        this.typeIdx = i;
        this.valueIdx = i2;
    }

    public EnumElementValue(ObjectType objectType, String str, ConstantPool constantPool) {
        super(101, constantPool);
        this.typeIdx = constantPool.addUtf8(objectType.getSignature());
        this.valueIdx = constantPool.addUtf8(str);
    }

    public EnumElementValue(EnumElementValue enumElementValue, ConstantPool constantPool, boolean z) {
        super(101, constantPool);
        if (z) {
            this.typeIdx = constantPool.addUtf8(enumElementValue.getEnumTypeString());
            this.valueIdx = constantPool.addUtf8(enumElementValue.getEnumValueString());
        } else {
            this.typeIdx = enumElementValue.getTypeIndex();
            this.valueIdx = enumElementValue.getValueIndex();
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.ElementValue
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.type);
        dataOutputStream.writeShort(this.typeIdx);
        dataOutputStream.writeShort(this.valueIdx);
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.ElementValue
    public String stringifyValue() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(((ConstantUtf8) this.cpool.getConstant(this.typeIdx, (byte) 1)).getValue());
        stringBuffer.append(((ConstantUtf8) this.cpool.getConstant(this.valueIdx, (byte) 1)).getValue());
        return stringBuffer.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("E(");
        sb.append(getEnumTypeString()).append(SymbolConstants.SPACE_SYMBOL).append(getEnumValueString()).append(")");
        return sb.toString();
    }

    public String getEnumTypeString() {
        return ((ConstantUtf8) getConstantPool().getConstant(this.typeIdx)).getValue();
    }

    public String getEnumValueString() {
        return ((ConstantUtf8) getConstantPool().getConstant(this.valueIdx)).getValue();
    }

    public int getValueIndex() {
        return this.valueIdx;
    }

    public int getTypeIndex() {
        return this.typeIdx;
    }
}
