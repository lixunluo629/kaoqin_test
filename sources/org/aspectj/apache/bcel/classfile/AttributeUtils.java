package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/AttributeUtils.class */
public class AttributeUtils {
    public static Attribute[] readAttributes(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        try {
            int unsignedShort = dataInputStream.readUnsignedShort();
            if (unsignedShort == 0) {
                return Attribute.NoAttributes;
            }
            Attribute[] attributeArr = new Attribute[unsignedShort];
            for (int i = 0; i < unsignedShort; i++) {
                attributeArr[i] = Attribute.readAttribute(dataInputStream, constantPool);
            }
            return attributeArr;
        } catch (IOException e) {
            throw new ClassFormatException("IOException whilst reading set of attributes: " + e.toString());
        }
    }

    public static void writeAttributes(Attribute[] attributeArr, DataOutputStream dataOutputStream) throws IOException {
        if (attributeArr == null) {
            dataOutputStream.writeShort(0);
            return;
        }
        dataOutputStream.writeShort(attributeArr.length);
        for (Attribute attribute : attributeArr) {
            attribute.dump(dataOutputStream);
        }
    }

    public static Signature getSignatureAttribute(Attribute[] attributeArr) {
        for (int i = 0; i < attributeArr.length; i++) {
            if (attributeArr[i].tag == 10) {
                return (Signature) attributeArr[i];
            }
        }
        return null;
    }

    public static Code getCodeAttribute(Attribute[] attributeArr) {
        for (int i = 0; i < attributeArr.length; i++) {
            if (attributeArr[i].tag == 2) {
                return (Code) attributeArr[i];
            }
        }
        return null;
    }

    public static ExceptionTable getExceptionTableAttribute(Attribute[] attributeArr) {
        for (int i = 0; i < attributeArr.length; i++) {
            if (attributeArr[i].tag == 3) {
                return (ExceptionTable) attributeArr[i];
            }
        }
        return null;
    }

    public static ConstantValue getConstantValueAttribute(Attribute[] attributeArr) {
        for (int i = 0; i < attributeArr.length; i++) {
            if (attributeArr[i].getTag() == 1) {
                return (ConstantValue) attributeArr[i];
            }
        }
        return null;
    }

    public static void accept(Attribute[] attributeArr, ClassVisitor classVisitor) {
        for (Attribute attribute : attributeArr) {
            attribute.accept(classVisitor);
        }
    }

    public static boolean hasSyntheticAttribute(Attribute[] attributeArr) {
        for (Attribute attribute : attributeArr) {
            if (attribute.tag == 7) {
                return true;
            }
        }
        return false;
    }

    public static SourceFile getSourceFileAttribute(Attribute[] attributeArr) {
        for (int i = 0; i < attributeArr.length; i++) {
            if (attributeArr[i].tag == 0) {
                return (SourceFile) attributeArr[i];
            }
        }
        return null;
    }
}
