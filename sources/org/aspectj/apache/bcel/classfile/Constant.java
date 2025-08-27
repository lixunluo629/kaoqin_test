package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.Constants;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Constant.class */
public abstract class Constant implements Cloneable, Node {
    protected byte tag;

    Constant(byte b) {
        this.tag = b;
    }

    public final byte getTag() {
        return this.tag;
    }

    public String toString() {
        return Constants.CONSTANT_NAMES[this.tag] + PropertyAccessor.PROPERTY_KEY_PREFIX + ((int) this.tag) + "]";
    }

    @Override // org.aspectj.apache.bcel.classfile.Node
    public abstract void accept(ClassVisitor classVisitor);

    public abstract void dump(DataOutputStream dataOutputStream) throws IOException;

    public abstract Object getValue();

    public Constant copy() {
        try {
            return (Constant) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    static final Constant readConstant(DataInputStream dataInputStream) throws IOException, ClassFormatException {
        byte b = dataInputStream.readByte();
        switch (b) {
            case 1:
                return new ConstantUtf8(dataInputStream);
            case 2:
            case 13:
            case 14:
            case 17:
            default:
                throw new ClassFormatException("Invalid byte tag in constant pool: " + ((int) b));
            case 3:
                return new ConstantInteger(dataInputStream);
            case 4:
                return new ConstantFloat(dataInputStream);
            case 5:
                return new ConstantLong(dataInputStream);
            case 6:
                return new ConstantDouble(dataInputStream);
            case 7:
                return new ConstantClass(dataInputStream);
            case 8:
                return new ConstantString(dataInputStream);
            case 9:
                return new ConstantFieldref(dataInputStream);
            case 10:
                return new ConstantMethodref(dataInputStream);
            case 11:
                return new ConstantInterfaceMethodref(dataInputStream);
            case 12:
                return new ConstantNameAndType(dataInputStream);
            case 15:
                return new ConstantMethodHandle(dataInputStream);
            case 16:
                return new ConstantMethodType(dataInputStream);
            case 18:
                return new ConstantInvokeDynamic(dataInputStream);
        }
    }
}
