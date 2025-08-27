package org.aspectj.apache.bcel.generic;

import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.Utility;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionFactory.class */
public class InstructionFactory implements InstructionConstants {
    protected ClassGen cg;
    protected ConstantPool cp;
    private static final char[] shortNames = {'C', 'F', 'D', 'B', 'S', 'I', 'L'};

    public InstructionFactory(ClassGen classGen, ConstantPool constantPool) {
        this.cg = classGen;
        this.cp = constantPool;
    }

    public InstructionFactory(ClassGen classGen) {
        this(classGen, classGen.getConstantPool());
    }

    public InstructionFactory(ConstantPool constantPool) {
        this(null, constantPool);
    }

    public InvokeInstruction createInvoke(String str, String str2, Type type, Type[] typeArr, short s) {
        int iAddMethodref;
        String methodSignature = Utility.toMethodSignature(type, typeArr);
        if (s == 185) {
            iAddMethodref = this.cp.addInterfaceMethodref(str, str2, methodSignature);
        } else {
            if (s == 186) {
                throw new IllegalStateException("NYI");
            }
            iAddMethodref = this.cp.addMethodref(str, str2, methodSignature);
        }
        switch (s) {
            case 182:
                return new InvokeInstruction((short) 182, iAddMethodref);
            case 183:
                return new InvokeInstruction((short) 183, iAddMethodref);
            case 184:
                return new InvokeInstruction((short) 184, iAddMethodref);
            case 185:
                int size = 0;
                for (Type type2 : typeArr) {
                    size += type2.getSize();
                }
                return new INVOKEINTERFACE(iAddMethodref, size + 1, 0);
            default:
                throw new RuntimeException("Oops: Unknown invoke kind:" + ((int) s));
        }
    }

    public InvokeInstruction createInvoke(String str, String str2, String str3, short s) throws ClassFormatException {
        int iAddMethodref;
        if (s == 185) {
            iAddMethodref = this.cp.addInterfaceMethodref(str, str2, str3);
        } else {
            if (s == 186) {
                throw new IllegalStateException("NYI");
            }
            iAddMethodref = this.cp.addMethodref(str, str2, str3);
        }
        switch (s) {
            case 182:
                return new InvokeInstruction((short) 182, iAddMethodref);
            case 183:
                return new InvokeInstruction((short) 183, iAddMethodref);
            case 184:
                return new InvokeInstruction((short) 184, iAddMethodref);
            case 185:
                int size = 0;
                for (Type type : Type.getArgumentTypes(str3)) {
                    size += type.getSize();
                }
                return new INVOKEINTERFACE(iAddMethodref, size + 1, 0);
            default:
                throw new RuntimeException("Oops: Unknown invoke kind:" + ((int) s));
        }
    }

    public static Instruction createALOAD(int i) {
        return i < 4 ? new InstructionLV((short) (42 + i)) : new InstructionLV((short) 25, i);
    }

    public static Instruction createASTORE(int i) {
        return i < 4 ? new InstructionLV((short) (75 + i)) : new InstructionLV((short) 58, i);
    }

    public Instruction createConstant(Object obj) {
        Instruction instructionPUSH;
        if (obj instanceof Number) {
            instructionPUSH = PUSH(this.cp, (Number) obj);
        } else if (obj instanceof String) {
            instructionPUSH = PUSH(this.cp, (String) obj);
        } else if (obj instanceof Boolean) {
            instructionPUSH = PUSH(this.cp, (Boolean) obj);
        } else if (obj instanceof Character) {
            instructionPUSH = PUSH(this.cp, (Character) obj);
        } else {
            if (!(obj instanceof ObjectType)) {
                throw new ClassGenException("Illegal type: " + obj.getClass());
            }
            instructionPUSH = PUSH(this.cp, (ObjectType) obj);
        }
        return instructionPUSH;
    }

    public FieldInstruction createFieldAccess(String str, String str2, Type type, short s) {
        int iAddFieldref = this.cp.addFieldref(str, str2, type.getSignature());
        switch (s) {
            case 178:
                return new FieldInstruction((short) 178, iAddFieldref);
            case 179:
                return new FieldInstruction((short) 179, iAddFieldref);
            case 180:
                return new FieldInstruction((short) 180, iAddFieldref);
            case 181:
                return new FieldInstruction((short) 181, iAddFieldref);
            default:
                throw new RuntimeException("Oops: Unknown getfield kind:" + ((int) s));
        }
    }

    public static Instruction createThis() {
        return new InstructionLV((short) 25, 0);
    }

    public static Instruction createReturn(Type type) {
        switch (type.getType()) {
            case 4:
            case 5:
            case 8:
            case 9:
            case 10:
                return IRETURN;
            case 6:
                return FRETURN;
            case 7:
                return DRETURN;
            case 11:
                return LRETURN;
            case 12:
                return RETURN;
            case 13:
            case 14:
                return ARETURN;
            default:
                throw new RuntimeException("Invalid type: " + type);
        }
    }

    public static Instruction createPop(int i) {
        return i == 2 ? POP2 : POP;
    }

    public static Instruction createDup(int i) {
        return i == 2 ? DUP2 : DUP;
    }

    public static Instruction createDup_2(int i) {
        return i == 2 ? DUP2_X2 : DUP_X2;
    }

    public static Instruction createDup_1(int i) {
        return i == 2 ? DUP2_X1 : DUP_X1;
    }

    public static InstructionLV createStore(Type type, int i) {
        switch (type.getType()) {
            case 4:
            case 5:
            case 8:
            case 9:
            case 10:
                return new InstructionLV((short) 54, i);
            case 6:
                return new InstructionLV((short) 56, i);
            case 7:
                return new InstructionLV((short) 57, i);
            case 11:
                return new InstructionLV((short) 55, i);
            case 12:
            default:
                throw new RuntimeException("Invalid type " + type);
            case 13:
            case 14:
                return new InstructionLV((short) 58, i);
        }
    }

    public static InstructionLV createLoad(Type type, int i) {
        switch (type.getType()) {
            case 4:
            case 5:
            case 8:
            case 9:
            case 10:
                return new InstructionLV((short) 21, i);
            case 6:
                return new InstructionLV((short) 23, i);
            case 7:
                return new InstructionLV((short) 24, i);
            case 11:
                return new InstructionLV((short) 22, i);
            case 12:
            default:
                throw new RuntimeException("Invalid type " + type);
            case 13:
            case 14:
                return new InstructionLV((short) 25, i);
        }
    }

    public static Instruction createArrayLoad(Type type) {
        switch (type.getType()) {
            case 4:
            case 8:
                return BALOAD;
            case 5:
                return CALOAD;
            case 6:
                return FALOAD;
            case 7:
                return DALOAD;
            case 9:
                return SALOAD;
            case 10:
                return IALOAD;
            case 11:
                return LALOAD;
            case 12:
            default:
                throw new RuntimeException("Invalid type " + type);
            case 13:
            case 14:
                return AALOAD;
        }
    }

    public static Instruction createArrayStore(Type type) {
        switch (type.getType()) {
            case 4:
            case 8:
                return BASTORE;
            case 5:
                return CASTORE;
            case 6:
                return FASTORE;
            case 7:
                return DASTORE;
            case 9:
                return SASTORE;
            case 10:
                return IASTORE;
            case 11:
                return LASTORE;
            case 12:
            default:
                throw new RuntimeException("Invalid type " + type);
            case 13:
            case 14:
                return AASTORE;
        }
    }

    public Instruction createCast(Type type, Type type2) {
        if (!(type instanceof BasicType) || !(type2 instanceof BasicType)) {
            if ((type instanceof ReferenceType) && (type2 instanceof ReferenceType)) {
                return type2 instanceof ArrayType ? new InstructionCP((short) 192, this.cp.addArrayClass((ArrayType) type2)) : new InstructionCP((short) 192, this.cp.addClass(((ObjectType) type2).getClassName()));
            }
            throw new RuntimeException("Can not cast " + type + " to " + type2);
        }
        byte type3 = type2.getType();
        byte type4 = type.getType();
        if (type3 == 11 && (type4 == 5 || type4 == 8 || type4 == 9)) {
            type4 = 10;
        }
        if (type4 == 7) {
            switch (type3) {
                case 6:
                    return InstructionConstants.D2F;
                case 10:
                    return InstructionConstants.D2I;
                case 11:
                    return InstructionConstants.D2L;
                default:
                    return null;
            }
        }
        if (type4 == 6) {
            switch (type3) {
                case 7:
                    return InstructionConstants.F2D;
                case 8:
                case 9:
                default:
                    return null;
                case 10:
                    return InstructionConstants.F2I;
                case 11:
                    return InstructionConstants.F2L;
            }
        }
        if (type4 != 10) {
            if (type4 != 11) {
                return null;
            }
            switch (type3) {
                case 6:
                    return InstructionConstants.L2F;
                case 7:
                    return InstructionConstants.L2D;
                case 8:
                case 9:
                default:
                    return null;
                case 10:
                    return InstructionConstants.L2I;
            }
        }
        switch (type3) {
            case 5:
                return InstructionConstants.I2C;
            case 6:
                return InstructionConstants.I2F;
            case 7:
                return InstructionConstants.I2D;
            case 8:
                return InstructionConstants.I2B;
            case 9:
                return InstructionConstants.I2S;
            case 10:
            default:
                return null;
            case 11:
                return InstructionConstants.I2L;
        }
    }

    public FieldInstruction createGetField(String str, String str2, Type type) {
        return new FieldInstruction((short) 180, this.cp.addFieldref(str, str2, type.getSignature()));
    }

    public FieldInstruction createGetStatic(String str, String str2, Type type) {
        return new FieldInstruction((short) 178, this.cp.addFieldref(str, str2, type.getSignature()));
    }

    public FieldInstruction createPutField(String str, String str2, Type type) {
        return new FieldInstruction((short) 181, this.cp.addFieldref(str, str2, type.getSignature()));
    }

    public FieldInstruction createPutStatic(String str, String str2, Type type) {
        return new FieldInstruction((short) 179, this.cp.addFieldref(str, str2, type.getSignature()));
    }

    public Instruction createCheckCast(ReferenceType referenceType) {
        return referenceType instanceof ArrayType ? new InstructionCP((short) 192, this.cp.addArrayClass((ArrayType) referenceType)) : new InstructionCP((short) 192, this.cp.addClass((ObjectType) referenceType));
    }

    public Instruction createInstanceOf(ReferenceType referenceType) {
        return referenceType instanceof ArrayType ? new InstructionCP((short) 193, this.cp.addArrayClass((ArrayType) referenceType)) : new InstructionCP((short) 193, this.cp.addClass((ObjectType) referenceType));
    }

    public Instruction createNew(ObjectType objectType) {
        return new InstructionCP((short) 187, this.cp.addClass(objectType));
    }

    public Instruction createNew(String str) {
        return createNew(new ObjectType(str));
    }

    public Instruction createNewArray(Type type, short s) {
        if (s == 1) {
            return type instanceof ObjectType ? new InstructionCP((short) 189, this.cp.addClass((ObjectType) type)) : type instanceof ArrayType ? new InstructionCP((short) 189, this.cp.addArrayClass((ArrayType) type)) : new InstructionByte((short) 188, ((BasicType) type).getType());
        }
        return new MULTIANEWARRAY(this.cp.addArrayClass(type instanceof ArrayType ? (ArrayType) type : new ArrayType(type, s)), s);
    }

    public static Instruction createNull(Type type) {
        switch (type.getType()) {
            case 4:
            case 5:
            case 8:
            case 9:
            case 10:
                return ICONST_0;
            case 6:
                return FCONST_0;
            case 7:
                return DCONST_0;
            case 11:
                return LCONST_0;
            case 12:
                return NOP;
            case 13:
            case 14:
                return ACONST_NULL;
            default:
                throw new RuntimeException("Invalid type: " + type);
        }
    }

    public static InstructionBranch createBranchInstruction(short s, InstructionHandle instructionHandle) {
        switch (s) {
            case 153:
                return new InstructionBranch((short) 153, instructionHandle);
            case 154:
                return new InstructionBranch((short) 154, instructionHandle);
            case 155:
                return new InstructionBranch((short) 155, instructionHandle);
            case 156:
                return new InstructionBranch((short) 156, instructionHandle);
            case 157:
                return new InstructionBranch((short) 157, instructionHandle);
            case 158:
                return new InstructionBranch((short) 158, instructionHandle);
            case 159:
                return new InstructionBranch((short) 159, instructionHandle);
            case 160:
                return new InstructionBranch((short) 160, instructionHandle);
            case 161:
                return new InstructionBranch((short) 161, instructionHandle);
            case 162:
                return new InstructionBranch((short) 162, instructionHandle);
            case 163:
                return new InstructionBranch((short) 163, instructionHandle);
            case 164:
                return new InstructionBranch((short) 164, instructionHandle);
            case 165:
                return new InstructionBranch((short) 165, instructionHandle);
            case 166:
                return new InstructionBranch((short) 166, instructionHandle);
            case 167:
                return new InstructionBranch((short) 167, instructionHandle);
            case 168:
                return new InstructionBranch((short) 168, instructionHandle);
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            default:
                throw new RuntimeException("Invalid opcode: " + ((int) s));
            case 198:
                return new InstructionBranch((short) 198, instructionHandle);
            case 199:
                return new InstructionBranch((short) 199, instructionHandle);
            case 200:
                return new InstructionBranch((short) 200, instructionHandle);
            case 201:
                return new InstructionBranch((short) 201, instructionHandle);
        }
    }

    public void setClassGen(ClassGen classGen) {
        this.cg = classGen;
    }

    public ClassGen getClassGen() {
        return this.cg;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.cp = constantPool;
    }

    public ConstantPool getConstantPool() {
        return this.cp;
    }

    public static Instruction PUSH(ConstantPool constantPool, int i) {
        Instruction instructionCP;
        if (i >= -1 && i <= 5) {
            return INSTRUCTIONS[3 + i];
        }
        if (i >= -128 && i <= 127) {
            instructionCP = new InstructionByte((short) 16, (byte) i);
        } else if (i < -32768 || i > 32767) {
            int iAddInteger = constantPool.addInteger(i);
            instructionCP = iAddInteger <= 255 ? new InstructionCP((short) 18, iAddInteger) : new InstructionCP((short) 19, iAddInteger);
        } else {
            instructionCP = new InstructionShort((short) 17, (short) i);
        }
        return instructionCP;
    }

    public static Instruction PUSH(ConstantPool constantPool, ObjectType objectType) {
        return new InstructionCP((short) 19, constantPool.addClass(objectType));
    }

    public static Instruction PUSH(ConstantPool constantPool, boolean z) {
        return INSTRUCTIONS[3 + (z ? 1 : 0)];
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [org.aspectj.apache.bcel.generic.Instruction] */
    /* JADX WARN: Type inference failed for: r0v14, types: [org.aspectj.apache.bcel.generic.Instruction] */
    /* JADX WARN: Type inference failed for: r0v16, types: [org.aspectj.apache.bcel.generic.Instruction] */
    public static Instruction PUSH(ConstantPool constantPool, float f) {
        InstructionCP instructionCP;
        if (f == 0.0d) {
            instructionCP = FCONST_0;
        } else if (f == 1.0d) {
            instructionCP = FCONST_1;
        } else if (f == 2.0d) {
            instructionCP = FCONST_2;
        } else {
            int iAddFloat = constantPool.addFloat(f);
            instructionCP = new InstructionCP(iAddFloat <= 255 ? (short) 18 : (short) 19, iAddFloat);
        }
        return instructionCP;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v6, types: [org.aspectj.apache.bcel.generic.Instruction] */
    /* JADX WARN: Type inference failed for: r0v8, types: [org.aspectj.apache.bcel.generic.Instruction] */
    public static Instruction PUSH(ConstantPool constantPool, long j) {
        return j == 0 ? LCONST_0 : j == 1 ? LCONST_1 : new InstructionCP((short) 20, constantPool.addLong(j));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v6, types: [org.aspectj.apache.bcel.generic.Instruction] */
    /* JADX WARN: Type inference failed for: r0v8, types: [org.aspectj.apache.bcel.generic.Instruction] */
    public static Instruction PUSH(ConstantPool constantPool, double d) {
        return d == 0.0d ? DCONST_0 : d == 1.0d ? DCONST_1 : new InstructionCP((short) 20, constantPool.addDouble(d));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v6, types: [org.aspectj.apache.bcel.generic.Instruction] */
    public static Instruction PUSH(ConstantPool constantPool, String str) {
        InstructionCP instructionCP;
        if (str == null) {
            instructionCP = ACONST_NULL;
        } else {
            int iAddString = constantPool.addString(str);
            instructionCP = new InstructionCP(iAddString <= 255 ? (short) 18 : (short) 19, iAddString);
        }
        return instructionCP;
    }

    public static Instruction PUSH(ConstantPool constantPool, Number number) {
        Instruction instructionPUSH;
        if ((number instanceof Integer) || (number instanceof Short) || (number instanceof Byte)) {
            instructionPUSH = PUSH(constantPool, number.intValue());
        } else if (number instanceof Double) {
            instructionPUSH = PUSH(constantPool, number.doubleValue());
        } else if (number instanceof Float) {
            instructionPUSH = PUSH(constantPool, number.floatValue());
        } else {
            if (!(number instanceof Long)) {
                throw new ClassGenException("What's this: " + number);
            }
            instructionPUSH = PUSH(constantPool, number.longValue());
        }
        return instructionPUSH;
    }

    public static Instruction PUSH(ConstantPool constantPool, Character ch2) {
        return PUSH(constantPool, (int) ch2.charValue());
    }

    public static Instruction PUSH(ConstantPool constantPool, Boolean bool) {
        return PUSH(constantPool, bool.booleanValue());
    }

    public InstructionList PUSHCLASS(ConstantPool constantPool, String str) {
        InstructionList instructionList = new InstructionList();
        int iAddClass = constantPool.addClass(str);
        if (this.cg == null || this.cg.getMajor() < 49) {
            instructionList.append(PUSH(constantPool, str));
            instructionList.append(createInvoke("java.lang.Class", "forName", ObjectType.CLASS, Type.STRINGARRAY1, (short) 184));
        } else if (iAddClass <= 255) {
            instructionList.append(new InstructionCP((short) 18, iAddClass));
        } else {
            instructionList.append(new InstructionCP((short) 19, iAddClass));
        }
        return instructionList;
    }
}
