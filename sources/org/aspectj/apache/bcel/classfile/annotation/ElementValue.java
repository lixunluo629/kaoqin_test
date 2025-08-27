package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/ElementValue.class */
public abstract class ElementValue {
    public static final int STRING = 115;
    public static final int ENUM_CONSTANT = 101;
    public static final int CLASS = 99;
    public static final int ANNOTATION = 64;
    public static final int ARRAY = 91;
    public static final int PRIMITIVE_INT = 73;
    public static final int PRIMITIVE_BYTE = 66;
    public static final int PRIMITIVE_CHAR = 67;
    public static final int PRIMITIVE_DOUBLE = 68;
    public static final int PRIMITIVE_FLOAT = 70;
    public static final int PRIMITIVE_LONG = 74;
    public static final int PRIMITIVE_SHORT = 83;
    public static final int PRIMITIVE_BOOLEAN = 90;
    protected int type;
    protected ConstantPool cpool;

    protected ElementValue(int i, ConstantPool constantPool) {
        this.type = i;
        this.cpool = constantPool;
    }

    public int getElementValueType() {
        return this.type;
    }

    public abstract String stringifyValue();

    public abstract void dump(DataOutputStream dataOutputStream) throws IOException;

    public static ElementValue readElementValue(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        int unsignedByte = dataInputStream.readUnsignedByte();
        switch (unsignedByte) {
            case 64:
                return new AnnotationElementValue(64, AnnotationGen.read(dataInputStream, constantPool, true), constantPool);
            case 65:
            case 69:
            case 71:
            case 72:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 100:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            default:
                throw new RuntimeException("Unexpected element value kind in annotation: " + unsignedByte);
            case 66:
                return new SimpleElementValue(66, dataInputStream.readUnsignedShort(), constantPool);
            case 67:
                return new SimpleElementValue(67, dataInputStream.readUnsignedShort(), constantPool);
            case 68:
                return new SimpleElementValue(68, dataInputStream.readUnsignedShort(), constantPool);
            case 70:
                return new SimpleElementValue(70, dataInputStream.readUnsignedShort(), constantPool);
            case 73:
                return new SimpleElementValue(73, dataInputStream.readUnsignedShort(), constantPool);
            case 74:
                return new SimpleElementValue(74, dataInputStream.readUnsignedShort(), constantPool);
            case 83:
                return new SimpleElementValue(83, dataInputStream.readUnsignedShort(), constantPool);
            case 90:
                return new SimpleElementValue(90, dataInputStream.readUnsignedShort(), constantPool);
            case 91:
                int unsignedShort = dataInputStream.readUnsignedShort();
                ElementValue[] elementValueArr = new ElementValue[unsignedShort];
                for (int i = 0; i < unsignedShort; i++) {
                    elementValueArr[i] = readElementValue(dataInputStream, constantPool);
                }
                return new ArrayElementValue(91, elementValueArr, constantPool);
            case 99:
                return new ClassElementValue(dataInputStream.readUnsignedShort(), constantPool);
            case 101:
                return new EnumElementValue(dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), constantPool);
            case 115:
                return new SimpleElementValue(115, dataInputStream.readUnsignedShort(), constantPool);
        }
    }

    protected ConstantPool getConstantPool() {
        return this.cpool;
    }

    public static ElementValue copy(ElementValue elementValue, ConstantPool constantPool, boolean z) {
        switch (elementValue.getElementValueType()) {
            case 64:
                return new AnnotationElementValue((AnnotationElementValue) elementValue, constantPool, z);
            case 65:
            case 69:
            case 71:
            case 72:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 100:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            default:
                throw new RuntimeException("Not implemented yet! (" + elementValue.getElementValueType() + ")");
            case 66:
            case 67:
            case 68:
            case 70:
            case 73:
            case 74:
            case 83:
            case 90:
            case 115:
                return new SimpleElementValue((SimpleElementValue) elementValue, constantPool, z);
            case 91:
                return new ArrayElementValue((ArrayElementValue) elementValue, constantPool, z);
            case 99:
                return new ClassElementValue((ClassElementValue) elementValue, constantPool, z);
            case 101:
                return new EnumElementValue((EnumElementValue) elementValue, constantPool, z);
        }
    }
}
