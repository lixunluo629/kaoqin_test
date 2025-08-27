package org.aspectj.apache.bcel.classfile;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/BootstrapMethods.class */
public final class BootstrapMethods extends Attribute {
    private boolean isInPackedState;
    private byte[] data;
    private int numBootstrapMethods;
    private BootstrapMethod[] bootstrapMethods;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/BootstrapMethods$BootstrapMethod.class */
    public static class BootstrapMethod {
        private int bootstrapMethodRef;
        private int[] bootstrapArguments;

        BootstrapMethod(DataInputStream dataInputStream) throws IOException {
            this(dataInputStream.readUnsignedShort(), readBootstrapArguments(dataInputStream));
        }

        private static int[] readBootstrapArguments(DataInputStream dataInputStream) throws IOException {
            int unsignedShort = dataInputStream.readUnsignedShort();
            int[] iArr = new int[unsignedShort];
            for (int i = 0; i < unsignedShort; i++) {
                iArr[i] = dataInputStream.readUnsignedShort();
            }
            return iArr;
        }

        public BootstrapMethod(int i, int[] iArr) {
            this.bootstrapMethodRef = i;
            this.bootstrapArguments = iArr;
        }

        public int getBootstrapMethodRef() {
            return this.bootstrapMethodRef;
        }

        public int[] getBootstrapArguments() {
            return this.bootstrapArguments;
        }

        public final void dump(DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeShort(this.bootstrapMethodRef);
            int length = this.bootstrapArguments.length;
            dataOutputStream.writeShort(length);
            for (int i = 0; i < length; i++) {
                dataOutputStream.writeShort(this.bootstrapArguments[i]);
            }
        }

        public final int getLength() {
            return 4 + (2 * this.bootstrapArguments.length);
        }
    }

    public BootstrapMethods(BootstrapMethods bootstrapMethods) {
        this(bootstrapMethods.getNameIndex(), bootstrapMethods.getLength(), bootstrapMethods.getBootstrapMethods(), bootstrapMethods.getConstantPool());
    }

    public BootstrapMethods(int i, int i2, BootstrapMethod[] bootstrapMethodArr, ConstantPool constantPool) {
        super((byte) 19, i, i2, constantPool);
        this.isInPackedState = false;
        setBootstrapMethods(bootstrapMethodArr);
        this.isInPackedState = false;
    }

    public final void setBootstrapMethods(BootstrapMethod[] bootstrapMethodArr) {
        this.data = null;
        this.isInPackedState = false;
        this.bootstrapMethods = bootstrapMethodArr;
        this.numBootstrapMethods = bootstrapMethodArr == null ? 0 : bootstrapMethodArr.length;
    }

    BootstrapMethods(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, (BootstrapMethod[]) null, constantPool);
        this.data = new byte[i2];
        dataInputStream.readFully(this.data);
        this.isInPackedState = true;
    }

    private void unpack() {
        if (this.isInPackedState) {
            try {
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.data));
                this.numBootstrapMethods = dataInputStream.readUnsignedShort();
                this.bootstrapMethods = new BootstrapMethod[this.numBootstrapMethods];
                for (int i = 0; i < this.numBootstrapMethods; i++) {
                    this.bootstrapMethods[i] = new BootstrapMethod(dataInputStream);
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
        classVisitor.visitBootstrapMethods(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        if (this.isInPackedState) {
            dataOutputStream.write(this.data);
            return;
        }
        int length = this.bootstrapMethods.length;
        dataOutputStream.writeShort(length);
        for (int i = 0; i < length; i++) {
            this.bootstrapMethods[i].dump(dataOutputStream);
        }
    }

    public final BootstrapMethod[] getBootstrapMethods() {
        unpack();
        return this.bootstrapMethods;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        unpack();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        for (int i = 0; i < this.numBootstrapMethods; i++) {
            BootstrapMethod bootstrapMethod = this.bootstrapMethods[i];
            stringBuffer2.append("BootstrapMethod[").append(i).append("]:");
            int bootstrapMethodRef = bootstrapMethod.getBootstrapMethodRef();
            ConstantMethodHandle constantMethodHandle = (ConstantMethodHandle) getConstantPool().getConstant(bootstrapMethodRef);
            stringBuffer2.append("#" + bootstrapMethodRef + ":");
            stringBuffer2.append(ConstantMethodHandle.kindToString(constantMethodHandle.getReferenceKind()));
            stringBuffer2.append(SymbolConstants.SPACE_SYMBOL).append(getConstantPool().getConstant(constantMethodHandle.getReferenceIndex()));
            int[] bootstrapArguments = bootstrapMethod.getBootstrapArguments();
            stringBuffer2.append(" argcount:").append(bootstrapArguments == null ? 0 : bootstrapArguments.length).append(SymbolConstants.SPACE_SYMBOL);
            if (bootstrapArguments != null) {
                for (int i2 = 0; i2 < bootstrapArguments.length; i2++) {
                    stringBuffer2.append(bootstrapArguments[i2]).append("(").append(getConstantPool().getConstant(bootstrapArguments[i2])).append(") ");
                }
            }
            if (i < this.numBootstrapMethods - 1) {
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

    public final int getNumBootstrapMethods() {
        unpack();
        return this.bootstrapMethods.length;
    }
}
