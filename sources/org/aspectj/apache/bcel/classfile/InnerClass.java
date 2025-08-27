package org.aspectj.apache.bcel.classfile;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/InnerClass.class */
public final class InnerClass implements Cloneable, Node {
    private int inner_class_index;
    private int outer_class_index;
    private int inner_name_index;
    private int inner_access_flags;

    public InnerClass(InnerClass innerClass) {
        this(innerClass.getInnerClassIndex(), innerClass.getOuterClassIndex(), innerClass.getInnerNameIndex(), innerClass.getInnerAccessFlags());
    }

    InnerClass(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort());
    }

    public InnerClass(int i, int i2, int i3, int i4) {
        this.inner_class_index = i;
        this.outer_class_index = i2;
        this.inner_name_index = i3;
        this.inner_access_flags = i4;
    }

    @Override // org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitInnerClass(this);
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.inner_class_index);
        dataOutputStream.writeShort(this.outer_class_index);
        dataOutputStream.writeShort(this.inner_name_index);
        dataOutputStream.writeShort(this.inner_access_flags);
    }

    public final int getInnerAccessFlags() {
        return this.inner_access_flags;
    }

    public final int getInnerClassIndex() {
        return this.inner_class_index;
    }

    public final int getInnerNameIndex() {
        return this.inner_name_index;
    }

    public final int getOuterClassIndex() {
        return this.outer_class_index;
    }

    public final void setInnerAccessFlags(int i) {
        this.inner_access_flags = i;
    }

    public final void setInnerClassIndex(int i) {
        this.inner_class_index = i;
    }

    public final void setInnerNameIndex(int i) {
        this.inner_name_index = i;
    }

    public final void setOuterClassIndex(int i) {
        this.outer_class_index = i;
    }

    public final String toString() {
        return "InnerClass(" + this.inner_class_index + ", " + this.outer_class_index + ", " + this.inner_name_index + ", " + this.inner_access_flags + ")";
    }

    public final String toString(ConstantPool constantPool) {
        String strCompactClassName = Utility.compactClassName(constantPool.getConstantString(this.inner_class_index, (byte) 7));
        String strCompactClassName2 = this.outer_class_index != 0 ? Utility.compactClassName(constantPool.getConstantString(this.outer_class_index, (byte) 7)) : "<not a member>";
        String value = this.inner_name_index != 0 ? ((ConstantUtf8) constantPool.getConstant(this.inner_name_index, (byte) 1)).getValue() : "<anonymous>";
        String strAccessToString = Utility.accessToString(this.inner_access_flags, true);
        return "InnerClass:" + (strAccessToString.equals("") ? "" : strAccessToString + SymbolConstants.SPACE_SYMBOL) + strCompactClassName + "(\"" + strCompactClassName2 + "\", \"" + value + "\")";
    }

    public InnerClass copy() {
        try {
            return (InnerClass) clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
