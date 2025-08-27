package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/TypeAnnotationGen.class */
public class TypeAnnotationGen {
    public static final TypeAnnotationGen[] NO_TYPE_ANNOTATIONS;
    public static final int[] NO_TYPE_PATH;
    private ConstantPool cpool;
    private int targetType;
    private int[] typePath;
    private AnnotationGen annotation;
    private int info;
    private int info2;
    private int[] localVarTarget;
    public static final int CLASS_TYPE_PARAMETER = 0;
    public static final int METHOD_TYPE_PARAMETER = 1;
    public static final int CLASS_EXTENDS = 16;
    public static final int CLASS_TYPE_PARAMETER_BOUND = 17;
    public static final int METHOD_TYPE_PARAMETER_BOUND = 18;
    public static final int FIELD = 19;
    public static final int METHOD_RETURN = 20;
    public static final int METHOD_RECEIVER = 21;
    public static final int METHOD_FORMAL_PARAMETER = 22;
    public static final int THROWS = 23;
    public static final int LOCAL_VARIABLE = 64;
    public static final int RESOURCE_VARIABLE = 65;
    public static final int EXCEPTION_PARAMETER = 66;
    public static final int INSTANCEOF = 67;
    public static final int NEW = 68;
    public static final int CONSTRUCTOR_REFERENCE = 69;
    public static final int METHOD_REFERENCE = 70;
    public static final int CAST = 71;
    public static final int CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 72;
    public static final int METHOD_INVOCATION_TYPE_ARGUMENT = 73;
    public static final int CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 74;
    public static final int METHOD_REFERENCE_TYPE_ARGUMENT = 75;
    public static final int TYPE_PATH_ENTRY_KIND_ARRAY = 0;
    public static final int TYPE_PATH_ENTRY_KIND_INNER_TYPE = 1;
    public static final int TYPE_PATH_ENTRY_KIND_WILDCARD = 2;
    public static final int TYPE_PATH_ENTRY_KIND_TYPE_ARGUMENT = 3;
    static final /* synthetic */ boolean $assertionsDisabled;

    private TypeAnnotationGen(ConstantPool constantPool) {
        this.cpool = constantPool;
    }

    public static TypeAnnotationGen read(DataInputStream dataInputStream, ConstantPool constantPool, boolean z) throws IOException {
        TypeAnnotationGen typeAnnotationGen = new TypeAnnotationGen(constantPool);
        typeAnnotationGen.targetType = dataInputStream.readUnsignedByte();
        switch (typeAnnotationGen.targetType) {
            case 0:
                typeAnnotationGen.info = dataInputStream.readUnsignedByte();
                break;
            case 1:
                typeAnnotationGen.info = dataInputStream.readUnsignedByte();
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            default:
                throw new IllegalStateException("nyi " + typeAnnotationGen.targetType);
            case 16:
                int unsignedShort = dataInputStream.readUnsignedShort();
                if (unsignedShort == 65535) {
                    typeAnnotationGen.info = -1;
                    break;
                } else {
                    typeAnnotationGen.info = unsignedShort;
                    break;
                }
            case 17:
            case 18:
                typeAnnotationGen.info = dataInputStream.readUnsignedByte();
                typeAnnotationGen.info2 = dataInputStream.readUnsignedByte();
                break;
            case 19:
            case 20:
            case 21:
                break;
            case 22:
                typeAnnotationGen.info = dataInputStream.readUnsignedByte();
                break;
            case 23:
                typeAnnotationGen.info = dataInputStream.readUnsignedShort();
                break;
            case 64:
            case 65:
                typeAnnotationGen.localVarTarget = readLocalVarTarget(dataInputStream);
                break;
            case 66:
                typeAnnotationGen.info = dataInputStream.readUnsignedByte();
                break;
            case 67:
            case 68:
            case 69:
            case 70:
                typeAnnotationGen.info = dataInputStream.readUnsignedShort();
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                typeAnnotationGen.info = dataInputStream.readUnsignedShort();
                typeAnnotationGen.info2 = dataInputStream.readUnsignedByte();
                break;
        }
        int unsignedByte = dataInputStream.readUnsignedByte();
        if (unsignedByte == 0) {
            typeAnnotationGen.typePath = NO_TYPE_PATH;
        } else {
            typeAnnotationGen.typePath = new int[unsignedByte * 2];
            int i = unsignedByte * 2;
            for (int i2 = 0; i2 < i; i2++) {
                typeAnnotationGen.typePath[i2] = dataInputStream.readUnsignedByte();
            }
        }
        typeAnnotationGen.annotation = AnnotationGen.read(dataInputStream, constantPool, z);
        return typeAnnotationGen;
    }

    public static int[] readLocalVarTarget(DataInputStream dataInputStream) throws IOException {
        int unsignedShort = dataInputStream.readUnsignedShort();
        int[] iArr = new int[unsignedShort * 3];
        int i = 0;
        for (int i2 = 0; i2 < unsignedShort; i2++) {
            int i3 = i;
            int i4 = i + 1;
            iArr[i3] = dataInputStream.readUnsignedShort();
            int i5 = i4 + 1;
            iArr[i4] = dataInputStream.readUnsignedShort();
            i = i5 + 1;
            iArr[i5] = dataInputStream.readUnsignedShort();
        }
        return iArr;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.targetType);
        switch (this.targetType) {
            case 0:
                dataOutputStream.writeByte(this.info);
                break;
            case 1:
                dataOutputStream.writeByte(this.info);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            default:
                throw new IllegalStateException("nyi " + this.targetType);
            case 16:
                dataOutputStream.writeShort(this.info);
                break;
            case 17:
            case 18:
                dataOutputStream.writeByte(this.info);
                dataOutputStream.writeByte(this.info2);
                break;
            case 19:
            case 20:
            case 21:
                break;
            case 22:
                dataOutputStream.writeByte(this.info);
                break;
            case 23:
                dataOutputStream.writeShort(this.info);
                break;
            case 64:
            case 65:
                dataOutputStream.writeShort(this.localVarTarget.length / 3);
                for (int i = 0; i < this.localVarTarget.length; i++) {
                    dataOutputStream.writeShort(this.localVarTarget[i]);
                }
                break;
            case 66:
                dataOutputStream.writeByte(this.info);
                break;
            case 67:
            case 68:
            case 69:
            case 70:
                dataOutputStream.writeShort(this.info);
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                dataOutputStream.writeShort(this.info);
                dataOutputStream.writeByte(this.info);
                break;
        }
        dataOutputStream.writeByte(this.typePath.length);
        for (int i2 = 0; i2 < this.typePath.length; i2++) {
            dataOutputStream.writeByte(this.typePath[i2]);
        }
        this.annotation.dump(dataOutputStream);
    }

    public int getSupertypeIndex() {
        if ($assertionsDisabled || this.targetType == 16) {
            return this.info;
        }
        throw new AssertionError();
    }

    public int getOffset() {
        if ($assertionsDisabled || this.targetType == 67 || this.targetType == 68 || this.targetType == 69 || this.targetType == 70 || this.targetType == 71 || this.targetType == 72 || this.targetType == 73 || this.targetType == 74 || this.targetType == 75) {
            return this.info;
        }
        throw new AssertionError();
    }

    public int getTypeParameterIndex() {
        if ($assertionsDisabled || this.targetType == 0 || this.targetType == 1 || this.targetType == 17 || this.targetType == 18) {
            return this.info;
        }
        throw new AssertionError();
    }

    public int getTypeArgumentIndex() {
        if ($assertionsDisabled || this.targetType == 71 || this.targetType == 72 || this.targetType == 73 || this.targetType == 74 || this.targetType == 75) {
            return this.info2;
        }
        throw new AssertionError();
    }

    public int getBoundIndex() {
        if ($assertionsDisabled || this.targetType == 17 || this.targetType == 18) {
            return this.info2;
        }
        throw new AssertionError();
    }

    public int getMethodFormalParameterIndex() {
        if ($assertionsDisabled || this.targetType == 22) {
            return this.info;
        }
        throw new AssertionError();
    }

    public int getThrowsTypeIndex() {
        if ($assertionsDisabled || this.targetType == 23) {
            return this.info;
        }
        throw new AssertionError();
    }

    public int[] getLocalVarTarget() {
        if ($assertionsDisabled || this.targetType == 64 || this.targetType == 65) {
            return this.localVarTarget;
        }
        throw new AssertionError();
    }

    public int getExceptionTableIndex() {
        if ($assertionsDisabled || this.targetType == 66) {
            return this.info;
        }
        throw new AssertionError();
    }

    public int getTargetType() {
        return this.targetType;
    }

    public AnnotationGen getAnnotation() {
        return this.annotation;
    }

    public int[] getTypePath() {
        return this.typePath;
    }

    public String getTypePathString() {
        return toTypePathString(this.typePath);
    }

    public static String toTypePathString(int[] iArr) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
        while (i < iArr.length) {
            if (i > 0) {
                sb.append(",");
            }
            int i2 = i;
            i++;
            switch (iArr[i2]) {
                case 0:
                    sb.append("ARRAY");
                    i++;
                    break;
                case 1:
                    sb.append("INNER_TYPE");
                    i++;
                    break;
                case 2:
                    sb.append("WILDCARD");
                    i++;
                    break;
                case 3:
                    i++;
                    sb.append("TYPE_ARGUMENT(").append(iArr[i]).append(")");
                    break;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    static {
        $assertionsDisabled = !TypeAnnotationGen.class.desiredAssertionStatus();
        NO_TYPE_ANNOTATIONS = new TypeAnnotationGen[0];
        NO_TYPE_PATH = new int[0];
    }
}
