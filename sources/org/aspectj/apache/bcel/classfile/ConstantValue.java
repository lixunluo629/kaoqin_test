package org.aspectj.apache.bcel.classfile;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantValue.class */
public final class ConstantValue extends Attribute {
    private int constantvalue_index;

    ConstantValue(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, dataInputStream.readUnsignedShort(), constantPool);
    }

    public ConstantValue(int i, int i2, int i3, ConstantPool constantPool) {
        super((byte) 1, i, i2, constantPool);
        this.constantvalue_index = i3;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantValue(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.constantvalue_index);
    }

    public final int getConstantValueIndex() {
        return this.constantvalue_index;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        String str;
        Constant constant = this.cpool.getConstant(this.constantvalue_index);
        switch (constant.getTag()) {
            case 3:
                str = "" + ((ConstantInteger) constant).getValue();
                break;
            case 4:
                str = "" + ((ConstantFloat) constant).getValue();
                break;
            case 5:
                str = "" + ((ConstantLong) constant).getValue();
                break;
            case 6:
                str = "" + ((ConstantDouble) constant).getValue();
                break;
            case 7:
            default:
                throw new IllegalStateException("Type of ConstValue invalid: " + constant);
            case 8:
                str = SymbolConstants.QUOTES_SYMBOL + Utility.convertString(((ConstantUtf8) this.cpool.getConstant(((ConstantString) constant).getStringIndex(), (byte) 1)).getValue()) + SymbolConstants.QUOTES_SYMBOL;
                break;
        }
        return str;
    }
}
