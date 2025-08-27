package org.aspectj.apache.bcel.classfile;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Signature.class */
public final class Signature extends Attribute {
    private int signature_index;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Signature$MyByteArrayInputStream.class */
    private static final class MyByteArrayInputStream extends ByteArrayInputStream {
        MyByteArrayInputStream(String str) {
            super(str.getBytes());
        }

        final int mark() {
            return this.pos;
        }

        final String getData() {
            return new String(this.buf);
        }

        final void reset(int i) {
            this.pos = i;
        }

        final void unread() {
            if (this.pos > 0) {
                this.pos--;
            }
        }
    }

    public Signature(Signature signature) {
        this(signature.getNameIndex(), signature.getLength(), signature.getSignatureIndex(), signature.getConstantPool());
    }

    Signature(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, dataInputStream.readUnsignedShort(), constantPool);
    }

    public Signature(int i, int i2, int i3, ConstantPool constantPool) {
        super((byte) 10, i, i2, constantPool);
        this.signature_index = i3;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        System.err.println("Visiting non-standard Signature object");
        classVisitor.visitSignature(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.signature_index);
    }

    public final int getSignatureIndex() {
        return this.signature_index;
    }

    public final void setSignatureIndex(int i) {
        this.signature_index = i;
    }

    public final String getSignature() {
        return ((ConstantUtf8) this.cpool.getConstant(this.signature_index, (byte) 1)).getValue();
    }

    private static boolean identStart(int i) {
        return i == 84 || i == 76;
    }

    private static final void matchIdent(MyByteArrayInputStream myByteArrayInputStream, StringBuffer stringBuffer) {
        int i = myByteArrayInputStream.read();
        int i2 = i;
        if (i == -1) {
            throw new RuntimeException("Illegal signature: " + myByteArrayInputStream.getData() + " no ident, reaching EOF");
        }
        if (identStart(i2)) {
            StringBuffer stringBuffer2 = new StringBuffer();
            int i3 = myByteArrayInputStream.read();
            while (true) {
                stringBuffer2.append((char) i3);
                i3 = myByteArrayInputStream.read();
                if (i3 == -1 || (!Character.isJavaIdentifierPart((char) i3) && i3 != 47)) {
                    break;
                }
            }
            stringBuffer.append(stringBuffer2.toString().replace('/', '.'));
            if (i3 != -1) {
                myByteArrayInputStream.unread();
                return;
            }
            return;
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        int i4 = 1;
        while (Character.isJavaIdentifierPart((char) i2)) {
            stringBuffer3.append((char) i2);
            i4++;
            i2 = myByteArrayInputStream.read();
        }
        if (i2 == 58) {
            myByteArrayInputStream.skip("Ljava/lang/Object".length());
            stringBuffer.append(stringBuffer3);
            myByteArrayInputStream.read();
            myByteArrayInputStream.unread();
            return;
        }
        for (int i5 = 0; i5 < i4; i5++) {
            myByteArrayInputStream.unread();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x006e, code lost:
    
        r6.append((char) r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static final void matchGJIdent(org.aspectj.apache.bcel.classfile.Signature.MyByteArrayInputStream r5, java.lang.StringBuffer r6) {
        /*
            Method dump skipped, instructions count: 206
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.apache.bcel.classfile.Signature.matchGJIdent(org.aspectj.apache.bcel.classfile.Signature$MyByteArrayInputStream, java.lang.StringBuffer):void");
    }

    public static String translate(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        matchGJIdent(new MyByteArrayInputStream(str), stringBuffer);
        return stringBuffer.toString();
    }

    public static final boolean isFormalParameterList(String str) {
        return str.startsWith("<") && str.indexOf(58) > 0;
    }

    public static final boolean isActualParameterList(String str) {
        return str.startsWith(StandardRoles.L) && str.endsWith(">;");
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        return "Signature(" + getSignature() + ")";
    }
}
