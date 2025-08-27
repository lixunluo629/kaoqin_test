package org.aspectj.apache.bcel.classfile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/LocalVariableTable.class */
public class LocalVariableTable extends Attribute {
    private boolean isInPackedState;
    private byte[] data;
    private int localVariableTableLength;
    private LocalVariable[] localVariableTable;

    public LocalVariableTable(LocalVariableTable localVariableTable) {
        this(localVariableTable.getNameIndex(), localVariableTable.getLength(), localVariableTable.getLocalVariableTable(), localVariableTable.getConstantPool());
    }

    public LocalVariableTable(int i, int i2, LocalVariable[] localVariableArr, ConstantPool constantPool) {
        super((byte) 5, i, i2, constantPool);
        this.isInPackedState = false;
        setLocalVariableTable(localVariableArr);
    }

    LocalVariableTable(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        super((byte) 5, i, i2, constantPool);
        this.isInPackedState = false;
        this.data = new byte[i2];
        dataInputStream.readFully(this.data);
        this.isInPackedState = true;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        unpack();
        classVisitor.visitLocalVariableTable(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        if (this.isInPackedState) {
            dataOutputStream.write(this.data);
            return;
        }
        dataOutputStream.writeShort(this.localVariableTableLength);
        for (int i = 0; i < this.localVariableTableLength; i++) {
            this.localVariableTable[i].dump(dataOutputStream);
        }
    }

    public final LocalVariable[] getLocalVariableTable() {
        unpack();
        return this.localVariableTable;
    }

    public final LocalVariable getLocalVariable(int i) {
        unpack();
        for (int i2 = 0; i2 < this.localVariableTableLength; i2++) {
            if (this.localVariableTable[i2] != null && this.localVariableTable[i2].getIndex() == i) {
                return this.localVariableTable[i2];
            }
        }
        return null;
    }

    public final void setLocalVariableTable(LocalVariable[] localVariableArr) {
        this.data = null;
        this.isInPackedState = false;
        this.localVariableTable = localVariableArr;
        this.localVariableTableLength = localVariableArr == null ? 0 : localVariableArr.length;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("");
        unpack();
        for (int i = 0; i < this.localVariableTableLength; i++) {
            stringBuffer.append(this.localVariableTable[i].toString());
            if (i < this.localVariableTableLength - 1) {
                stringBuffer.append('\n');
            }
        }
        return stringBuffer.toString();
    }

    public final int getTableLength() {
        unpack();
        return this.localVariableTableLength;
    }

    private void unpack() {
        if (this.isInPackedState) {
            try {
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.data));
                this.localVariableTableLength = dataInputStream.readUnsignedShort();
                this.localVariableTable = new LocalVariable[this.localVariableTableLength];
                for (int i = 0; i < this.localVariableTableLength; i++) {
                    this.localVariableTable[i] = new LocalVariable(dataInputStream, this.cpool);
                }
                dataInputStream.close();
                this.data = null;
                this.isInPackedState = false;
            } catch (IOException e) {
                throw new RuntimeException("Unpacking of LocalVariableTable attribute failed");
            }
        }
    }
}
