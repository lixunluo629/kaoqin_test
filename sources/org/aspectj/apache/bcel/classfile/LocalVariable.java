package org.aspectj.apache.bcel.classfile;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.Constants;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/LocalVariable.class */
public final class LocalVariable implements Constants, Cloneable, Node {
    private int start_pc;
    private int length;
    private int name_index;
    private int signature_index;
    private int index;
    private ConstantPool constant_pool;

    public LocalVariable(LocalVariable localVariable) {
        this(localVariable.getStartPC(), localVariable.getLength(), localVariable.getNameIndex(), localVariable.getSignatureIndex(), localVariable.getIndex(), localVariable.getConstantPool());
    }

    LocalVariable(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), constantPool);
    }

    public LocalVariable(int i, int i2, int i3, int i4, int i5, ConstantPool constantPool) {
        this.start_pc = i;
        this.length = i2;
        this.name_index = i3;
        this.signature_index = i4;
        this.index = i5;
        this.constant_pool = constantPool;
    }

    @Override // org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitLocalVariable(this);
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.start_pc);
        dataOutputStream.writeShort(this.length);
        dataOutputStream.writeShort(this.name_index);
        dataOutputStream.writeShort(this.signature_index);
        dataOutputStream.writeShort(this.index);
    }

    public final ConstantPool getConstantPool() {
        return this.constant_pool;
    }

    public final int getLength() {
        return this.length;
    }

    public final String getName() {
        return ((ConstantUtf8) this.constant_pool.getConstant(this.name_index, (byte) 1)).getValue();
    }

    public final int getNameIndex() {
        return this.name_index;
    }

    public final String getSignature() {
        return ((ConstantUtf8) this.constant_pool.getConstant(this.signature_index, (byte) 1)).getValue();
    }

    public final int getSignatureIndex() {
        return this.signature_index;
    }

    public final int getIndex() {
        return this.index;
    }

    public final int getStartPC() {
        return this.start_pc;
    }

    public final void setConstantPool(ConstantPool constantPool) {
        this.constant_pool = constantPool;
    }

    public final void setLength(int i) {
        this.length = i;
    }

    public final void setNameIndex(int i) {
        this.name_index = i;
    }

    public final void setSignatureIndex(int i) {
        this.signature_index = i;
    }

    public final void setIndex(int i) {
        this.index = i;
    }

    public final void setStartPC(int i) {
        this.start_pc = i;
    }

    public final String toString() {
        return "LocalVariable(start_pc = " + this.start_pc + ", length = " + this.length + ", index = " + this.index + ":" + Utility.signatureToString(getSignature()) + SymbolConstants.SPACE_SYMBOL + getName() + ")";
    }

    public LocalVariable copy() {
        try {
            return (LocalVariable) clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
