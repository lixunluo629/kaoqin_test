package org.aspectj.apache.bcel.classfile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/LineNumberTable.class */
public final class LineNumberTable extends Attribute {
    private boolean isInPackedState;
    private byte[] data;
    private int tableLength;
    private LineNumber[] table;

    public LineNumberTable(LineNumberTable lineNumberTable) {
        this(lineNumberTable.getNameIndex(), lineNumberTable.getLength(), lineNumberTable.getLineNumberTable(), lineNumberTable.getConstantPool());
    }

    public LineNumberTable(int i, int i2, LineNumber[] lineNumberArr, ConstantPool constantPool) {
        super((byte) 4, i, i2, constantPool);
        this.isInPackedState = false;
        setLineNumberTable(lineNumberArr);
        this.isInPackedState = false;
    }

    LineNumberTable(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, (LineNumber[]) null, constantPool);
        this.data = new byte[i2];
        dataInputStream.readFully(this.data);
        this.isInPackedState = true;
    }

    private void unpack() {
        if (this.isInPackedState) {
            try {
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.data));
                this.tableLength = dataInputStream.readUnsignedShort();
                this.table = new LineNumber[this.tableLength];
                for (int i = 0; i < this.tableLength; i++) {
                    this.table[i] = new LineNumber(dataInputStream);
                }
                dataInputStream.close();
                this.data = null;
                this.isInPackedState = false;
            } catch (IOException e) {
                throw new RuntimeException("Unpacking of LineNumberTable attribute failed");
            }
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        unpack();
        classVisitor.visitLineNumberTable(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        if (this.isInPackedState) {
            dataOutputStream.write(this.data);
            return;
        }
        dataOutputStream.writeShort(this.tableLength);
        for (int i = 0; i < this.tableLength; i++) {
            this.table[i].dump(dataOutputStream);
        }
    }

    public final LineNumber[] getLineNumberTable() {
        unpack();
        return this.table;
    }

    public final void setLineNumberTable(LineNumber[] lineNumberArr) {
        this.data = null;
        this.isInPackedState = false;
        this.table = lineNumberArr;
        this.tableLength = lineNumberArr == null ? 0 : lineNumberArr.length;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        unpack();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        for (int i = 0; i < this.tableLength; i++) {
            stringBuffer2.append(this.table[i].toString());
            if (i < this.tableLength - 1) {
                stringBuffer2.append(", ");
            }
            if (stringBuffer2.length() > 72) {
                stringBuffer2.append('\n');
                stringBuffer.append(stringBuffer2);
                stringBuffer2.setLength(0);
            }
        }
        stringBuffer.append(stringBuffer2);
        return stringBuffer.toString();
    }

    public int getSourceLine(int i) {
        unpack();
        int i2 = 0;
        int i3 = this.tableLength - 1;
        if (i3 < 0) {
            return -1;
        }
        int i4 = -1;
        int i5 = -1;
        do {
            int i6 = (i2 + i3) / 2;
            int startPC = this.table[i6].getStartPC();
            if (startPC == i) {
                return this.table[i6].getLineNumber();
            }
            if (i < startPC) {
                i3 = i6 - 1;
            } else {
                i2 = i6 + 1;
            }
            if (startPC < i && startPC > i5) {
                i5 = startPC;
                i4 = i6;
            }
        } while (i2 <= i3);
        if (i4 < 0) {
            return -1;
        }
        return this.table[i4].getLineNumber();
    }

    public final int getTableLength() {
        unpack();
        return this.tableLength;
    }
}
