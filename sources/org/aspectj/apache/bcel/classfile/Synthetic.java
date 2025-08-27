package org.aspectj.apache.bcel.classfile;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.ibatis.javassist.bytecode.SyntheticAttribute;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Synthetic.class */
public final class Synthetic extends Attribute {
    private byte[] bytes;

    public Synthetic(Synthetic synthetic) {
        this(synthetic.getNameIndex(), synthetic.getLength(), synthetic.getBytes(), synthetic.getConstantPool());
    }

    public Synthetic(int i, int i2, byte[] bArr, ConstantPool constantPool) {
        super((byte) 7, i, i2, constantPool);
        this.bytes = bArr;
    }

    Synthetic(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, (byte[]) null, constantPool);
        if (i2 > 0) {
            this.bytes = new byte[i2];
            dataInputStream.readFully(this.bytes);
            System.err.println("Synthetic attribute with length > 0");
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitSynthetic(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        if (this.length > 0) {
            dataOutputStream.write(this.bytes, 0, this.length);
        }
    }

    public final byte[] getBytes() {
        return this.bytes;
    }

    public final void setBytes(byte[] bArr) {
        this.bytes = bArr;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer(SyntheticAttribute.tag);
        if (this.length > 0) {
            stringBuffer.append(SymbolConstants.SPACE_SYMBOL + Utility.toHexString(this.bytes));
        }
        return stringBuffer.toString();
    }
}
