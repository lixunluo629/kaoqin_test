package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Code.class */
public final class Code extends Attribute {
    private int maxStack;
    private int maxLocals;
    private byte[] code;
    private CodeException[] exceptionTable;
    private Attribute[] attributes;
    private static final CodeException[] NO_EXCEPTIONS = new CodeException[0];

    public Code(Code code) {
        this(code.getNameIndex(), code.getLength(), code.getMaxStack(), code.getMaxLocals(), code.getCode(), code.getExceptionTable(), code.getAttributes(), code.getConstantPool());
    }

    Code(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), (byte[]) null, (CodeException[]) null, (Attribute[]) null, constantPool);
        this.code = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(this.code);
        int unsignedShort = dataInputStream.readUnsignedShort();
        if (unsignedShort == 0) {
            this.exceptionTable = NO_EXCEPTIONS;
        } else {
            this.exceptionTable = new CodeException[unsignedShort];
            for (int i3 = 0; i3 < unsignedShort; i3++) {
                this.exceptionTable[i3] = new CodeException(dataInputStream);
            }
        }
        this.attributes = AttributeUtils.readAttributes(dataInputStream, constantPool);
        this.length = i2;
    }

    public Code(int i, int i2, int i3, int i4, byte[] bArr, CodeException[] codeExceptionArr, Attribute[] attributeArr, ConstantPool constantPool) {
        super((byte) 2, i, i2, constantPool);
        this.maxStack = i3;
        this.maxLocals = i4;
        setCode(bArr);
        setExceptionTable(codeExceptionArr);
        setAttributes(attributeArr);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitCode(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.maxStack);
        dataOutputStream.writeShort(this.maxLocals);
        dataOutputStream.writeInt(this.code.length);
        dataOutputStream.write(this.code, 0, this.code.length);
        dataOutputStream.writeShort(this.exceptionTable.length);
        for (int i = 0; i < this.exceptionTable.length; i++) {
            this.exceptionTable[i].dump(dataOutputStream);
        }
        dataOutputStream.writeShort(this.attributes.length);
        for (int i2 = 0; i2 < this.attributes.length; i2++) {
            this.attributes[i2].dump(dataOutputStream);
        }
    }

    public final Attribute[] getAttributes() {
        return this.attributes;
    }

    public LineNumberTable getLineNumberTable() {
        for (int i = 0; i < this.attributes.length; i++) {
            if (this.attributes[i].tag == 4) {
                return (LineNumberTable) this.attributes[i];
            }
        }
        return null;
    }

    public LocalVariableTable getLocalVariableTable() {
        for (int i = 0; i < this.attributes.length; i++) {
            if (this.attributes[i].tag == 5) {
                return (LocalVariableTable) this.attributes[i];
            }
        }
        return null;
    }

    public final byte[] getCode() {
        return this.code;
    }

    public final CodeException[] getExceptionTable() {
        return this.exceptionTable;
    }

    public final int getMaxLocals() {
        return this.maxLocals;
    }

    public final int getMaxStack() {
        return this.maxStack;
    }

    private final int getInternalLength() {
        return 8 + (this.code == null ? 0 : this.code.length) + 2 + (8 * (this.exceptionTable == null ? 0 : this.exceptionTable.length)) + 2;
    }

    private final int calculateLength() {
        int i = 0;
        if (this.attributes != null) {
            for (int i2 = 0; i2 < this.attributes.length; i2++) {
                i += this.attributes[i2].length + 6;
            }
        }
        return i + getInternalLength();
    }

    public final void setAttributes(Attribute[] attributeArr) {
        this.attributes = attributeArr;
        this.length = calculateLength();
    }

    public final void setCode(byte[] bArr) {
        this.code = bArr;
    }

    public final void setExceptionTable(CodeException[] codeExceptionArr) {
        this.exceptionTable = codeExceptionArr;
    }

    public final void setMaxLocals(int i) {
        this.maxLocals = i;
    }

    public final void setMaxStack(int i) {
        this.maxStack = i;
    }

    public final String toString(boolean z) {
        StringBuffer stringBuffer = new StringBuffer("Code(max_stack = " + this.maxStack + ", max_locals = " + this.maxLocals + ", code_length = " + this.code.length + ")\n" + Utility.codeToString(this.code, this.cpool, 0, -1, z));
        if (this.exceptionTable.length > 0) {
            stringBuffer.append("\nException handler(s) = \nFrom\tTo\tHandler\tType\n");
            for (int i = 0; i < this.exceptionTable.length; i++) {
                stringBuffer.append(this.exceptionTable[i].toString(this.cpool, z) + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
        }
        if (this.attributes.length > 0) {
            stringBuffer.append("\nAttribute(s) = \n");
            for (int i2 = 0; i2 < this.attributes.length; i2++) {
                stringBuffer.append(this.attributes[i2].toString() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
        }
        return stringBuffer.toString();
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        return toString(true);
    }

    public String getCodeString() throws ClassFormatException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Code(max_stack = ").append(this.maxStack);
        stringBuffer.append(", max_locals = ").append(this.maxLocals);
        stringBuffer.append(", code_length = ").append(this.code.length).append(")\n");
        stringBuffer.append(Utility.codeToString(this.code, this.cpool, 0, -1, true));
        if (this.exceptionTable.length > 0) {
            stringBuffer.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR).append("Exception entries =  ").append(this.exceptionTable.length).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            for (int i = 0; i < this.exceptionTable.length; i++) {
                CodeException codeException = this.exceptionTable[i];
                int catchType = codeException.getCatchType();
                String constantString = "finally";
                if (catchType != 0) {
                    constantString = this.cpool.getConstantString(catchType, (byte) 7);
                }
                stringBuffer.append(constantString).append(PropertyAccessor.PROPERTY_KEY_PREFIX);
                stringBuffer.append(codeException.getStartPC()).append(">").append(codeException.getEndPC()).append("]\n");
            }
        }
        return stringBuffer.toString();
    }
}
