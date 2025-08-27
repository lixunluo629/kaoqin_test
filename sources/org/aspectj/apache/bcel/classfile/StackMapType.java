package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.Constants;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/StackMapType.class */
public final class StackMapType implements Cloneable {
    private byte type;
    private int index;
    private ConstantPool constant_pool;

    StackMapType(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(dataInputStream.readByte(), -1, constantPool);
        if (hasIndex()) {
            setIndex(dataInputStream.readShort());
        }
        setConstantPool(constantPool);
    }

    public StackMapType(byte b, int i, ConstantPool constantPool) {
        this.index = -1;
        setType(b);
        setIndex(i);
        setConstantPool(constantPool);
    }

    public void setType(byte b) {
        if (b < 0 || b > 8) {
            throw new RuntimeException("Illegal type for StackMapType: " + ((int) b));
        }
        this.type = b;
    }

    public byte getType() {
        return this.type;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.type);
        if (hasIndex()) {
            dataOutputStream.writeShort(getIndex());
        }
    }

    public final boolean hasIndex() {
        return this.type == 7 || this.type == 8;
    }

    private String printIndex() {
        return this.type == 7 ? ", class=" + this.constant_pool.constantToString(this.index, (byte) 7) : this.type == 8 ? ", offset=" + this.index : "";
    }

    public final String toString() {
        return "(type=" + Constants.ITEM_NAMES[this.type] + printIndex() + ")";
    }

    public StackMapType copy() {
        try {
            return (StackMapType) clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public final ConstantPool getConstantPool() {
        return this.constant_pool;
    }

    public final void setConstantPool(ConstantPool constantPool) {
        this.constant_pool = constantPool;
    }
}
