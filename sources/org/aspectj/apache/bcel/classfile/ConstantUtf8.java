package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantUtf8.class */
public final class ConstantUtf8 extends Constant implements SimpleConstant {
    private String string;
    static final /* synthetic */ boolean $assertionsDisabled;

    ConstantUtf8(DataInputStream dataInputStream) throws IOException {
        super((byte) 1);
        this.string = dataInputStream.readUTF();
    }

    public ConstantUtf8(String str) {
        super((byte) 1);
        if (!$assertionsDisabled && str == null) {
            throw new AssertionError();
        }
        this.string = str;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantUtf8(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeUTF(this.string);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(\"" + Utility.replace(this.string, ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "\\n") + "\")";
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public String getValue() {
        return this.string;
    }

    @Override // org.aspectj.apache.bcel.classfile.SimpleConstant
    public String getStringValue() {
        return this.string;
    }

    static {
        $assertionsDisabled = !ConstantUtf8.class.desiredAssertionStatus();
    }
}
