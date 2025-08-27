package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/LocalVariableTypeTable.class */
public class LocalVariableTypeTable extends Attribute {
    private int local_variable_type_table_length;
    private LocalVariable[] local_variable_type_table;

    public LocalVariableTypeTable(LocalVariableTypeTable localVariableTypeTable) {
        this(localVariableTypeTable.getNameIndex(), localVariableTypeTable.getLength(), localVariableTypeTable.getLocalVariableTypeTable(), localVariableTypeTable.getConstantPool());
    }

    public LocalVariableTypeTable(int i, int i2, LocalVariable[] localVariableArr, ConstantPool constantPool) {
        super((byte) 16, i, i2, constantPool);
        setLocalVariableTable(localVariableArr);
    }

    LocalVariableTypeTable(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, (LocalVariable[]) null, constantPool);
        this.local_variable_type_table_length = dataInputStream.readUnsignedShort();
        this.local_variable_type_table = new LocalVariable[this.local_variable_type_table_length];
        for (int i3 = 0; i3 < this.local_variable_type_table_length; i3++) {
            this.local_variable_type_table[i3] = new LocalVariable(dataInputStream, constantPool);
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitLocalVariableTypeTable(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.local_variable_type_table_length);
        for (int i = 0; i < this.local_variable_type_table_length; i++) {
            this.local_variable_type_table[i].dump(dataOutputStream);
        }
    }

    public final LocalVariable[] getLocalVariableTypeTable() {
        return this.local_variable_type_table;
    }

    public final LocalVariable getLocalVariable(int i) {
        for (int i2 = 0; i2 < this.local_variable_type_table_length; i2++) {
            if (this.local_variable_type_table[i2].getIndex() == i) {
                return this.local_variable_type_table[i2];
            }
        }
        return null;
    }

    public final void setLocalVariableTable(LocalVariable[] localVariableArr) {
        this.local_variable_type_table = localVariableArr;
        this.local_variable_type_table_length = localVariableArr == null ? 0 : localVariableArr.length;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < this.local_variable_type_table_length; i++) {
            stringBuffer.append(this.local_variable_type_table[i].toString());
            if (i < this.local_variable_type_table_length - 1) {
                stringBuffer.append('\n');
            }
        }
        return stringBuffer.toString();
    }

    public final int getTableLength() {
        return this.local_variable_type_table_length;
    }
}
