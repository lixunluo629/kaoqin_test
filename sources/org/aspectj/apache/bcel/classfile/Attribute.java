package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisParamAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisTypeAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisParamAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisTypeAnnos;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Attribute.class */
public abstract class Attribute implements Cloneable, Node, Serializable {
    public static final Attribute[] NoAttributes = new Attribute[0];
    protected byte tag;
    protected int nameIndex;
    protected int length;
    protected ConstantPool cpool;

    protected Attribute(byte b, int i, int i2, ConstantPool constantPool) {
        this.tag = b;
        this.nameIndex = i;
        this.length = i2;
        this.cpool = constantPool;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.nameIndex);
        dataOutputStream.writeInt(this.length);
    }

    public static final Attribute readAttribute(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        byte b = -1;
        int unsignedShort = dataInputStream.readUnsignedShort();
        String value = constantPool.getConstantUtf8(unsignedShort).getValue();
        int i = dataInputStream.readInt();
        byte b2 = 0;
        while (true) {
            byte b3 = b2;
            if (b3 >= 23) {
                break;
            }
            if (value.equals(Constants.ATTRIBUTE_NAMES[b3])) {
                b = b3;
                break;
            }
            b2 = (byte) (b3 + 1);
        }
        switch (b) {
            case -1:
                return new Unknown(unsignedShort, i, dataInputStream, constantPool);
            case 0:
                return new SourceFile(unsignedShort, i, dataInputStream, constantPool);
            case 1:
                return new ConstantValue(unsignedShort, i, dataInputStream, constantPool);
            case 2:
                return new Code(unsignedShort, i, dataInputStream, constantPool);
            case 3:
                return new ExceptionTable(unsignedShort, i, dataInputStream, constantPool);
            case 4:
                return new LineNumberTable(unsignedShort, i, dataInputStream, constantPool);
            case 5:
                return new LocalVariableTable(unsignedShort, i, dataInputStream, constantPool);
            case 6:
                return new InnerClasses(unsignedShort, i, dataInputStream, constantPool);
            case 7:
                return new Synthetic(unsignedShort, i, dataInputStream, constantPool);
            case 8:
                return new Deprecated(unsignedShort, i, dataInputStream, constantPool);
            case 9:
            default:
                throw new IllegalStateException();
            case 10:
                return new Signature(unsignedShort, i, dataInputStream, constantPool);
            case 11:
                return new StackMap(unsignedShort, i, dataInputStream, constantPool);
            case 12:
                return new RuntimeVisAnnos(unsignedShort, i, dataInputStream, constantPool);
            case 13:
                return new RuntimeInvisAnnos(unsignedShort, i, dataInputStream, constantPool);
            case 14:
                return new RuntimeVisParamAnnos(unsignedShort, i, dataInputStream, constantPool);
            case 15:
                return new RuntimeInvisParamAnnos(unsignedShort, i, dataInputStream, constantPool);
            case 16:
                return new LocalVariableTypeTable(unsignedShort, i, dataInputStream, constantPool);
            case 17:
                return new EnclosingMethod(unsignedShort, i, dataInputStream, constantPool);
            case 18:
                return new AnnotationDefault(unsignedShort, i, dataInputStream, constantPool);
            case 19:
                return new BootstrapMethods(unsignedShort, i, dataInputStream, constantPool);
            case 20:
                return new RuntimeVisTypeAnnos(unsignedShort, i, dataInputStream, constantPool);
            case 21:
                return new RuntimeInvisTypeAnnos(unsignedShort, i, dataInputStream, constantPool);
            case 22:
                return new MethodParameters(unsignedShort, i, dataInputStream, constantPool);
        }
    }

    public String getName() {
        return this.cpool.getConstantUtf8(this.nameIndex).getValue();
    }

    public final int getLength() {
        return this.length;
    }

    public final int getNameIndex() {
        return this.nameIndex;
    }

    public final byte getTag() {
        return this.tag;
    }

    public final ConstantPool getConstantPool() {
        return this.cpool;
    }

    public String toString() {
        return Constants.ATTRIBUTE_NAMES[this.tag];
    }

    public abstract void accept(ClassVisitor classVisitor);
}
