package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/LineNumber.class */
public final class LineNumber implements Node {
    private int startPC;
    private int lineNumber;

    public LineNumber(LineNumber lineNumber) {
        this(lineNumber.getStartPC(), lineNumber.getLineNumber());
    }

    LineNumber(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort());
    }

    public LineNumber(int i, int i2) {
        this.startPC = i;
        this.lineNumber = i2;
    }

    @Override // org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitLineNumber(this);
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.startPC);
        dataOutputStream.writeShort(this.lineNumber);
    }

    public final int getLineNumber() {
        return this.lineNumber;
    }

    public final int getStartPC() {
        return this.startPC;
    }

    public final String toString() {
        return "LineNumber(" + this.startPC + ", " + this.lineNumber + ")";
    }

    public LineNumber copy() {
        return new LineNumber(this);
    }
}
