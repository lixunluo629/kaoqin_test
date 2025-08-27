package org.aspectj.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.util.ByteSequence;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/Instruction.class */
public class Instruction implements Cloneable, Serializable, Constants {
    public short opcode;
    static final /* synthetic */ boolean $assertionsDisabled;

    public Instruction(short s) {
        this.opcode = (short) -1;
        this.opcode = s;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
    }

    public String getName() {
        return Constants.OPCODE_NAMES[this.opcode];
    }

    public final Instruction copy() {
        if (InstructionConstants.INSTRUCTIONS[this.opcode] != null) {
            return this;
        }
        Instruction instruction = null;
        try {
            instruction = (Instruction) clone();
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
        }
        return instruction;
    }

    public static final Instruction readInstruction(ByteSequence byteSequence) throws IOException {
        Instruction multianewarray;
        boolean z = false;
        short unsignedByte = (short) byteSequence.readUnsignedByte();
        if (unsignedByte == 196) {
            z = true;
            unsignedByte = (short) byteSequence.readUnsignedByte();
        }
        Instruction instruction = InstructionConstants.INSTRUCTIONS[unsignedByte];
        if (instruction != null) {
            return instruction;
        }
        try {
            switch (unsignedByte) {
                case 16:
                    multianewarray = new InstructionByte((short) 16, byteSequence.readByte());
                    break;
                case 17:
                    multianewarray = new InstructionShort((short) 17, byteSequence.readShort());
                    break;
                case 18:
                    multianewarray = new InstructionCP((short) 18, byteSequence.readUnsignedByte());
                    break;
                case 19:
                case 20:
                    multianewarray = new InstructionCP(unsignedByte, byteSequence.readUnsignedShort());
                    break;
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                    multianewarray = new InstructionLV(unsignedByte, z ? byteSequence.readUnsignedShort() : byteSequence.readUnsignedByte());
                    break;
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
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
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
                case 115:
                case 116:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                case 123:
                case 124:
                case 125:
                case 126:
                case 127:
                case 128:
                case 129:
                case 130:
                case 131:
                case 133:
                case 134:
                case 135:
                case 136:
                case 137:
                case 138:
                case 139:
                case 140:
                case 141:
                case 142:
                case 143:
                case 144:
                case 145:
                case 146:
                case 147:
                case 148:
                case 149:
                case 150:
                case 151:
                case 152:
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 190:
                case 191:
                case 194:
                case 195:
                case 196:
                default:
                    throw new ClassGenException("Illegal opcode detected");
                case 132:
                    multianewarray = new IINC(z ? byteSequence.readUnsignedShort() : byteSequence.readUnsignedByte(), z ? byteSequence.readShort() : byteSequence.readByte(), z);
                    break;
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166:
                case 167:
                case 168:
                case 198:
                case 199:
                    multianewarray = new InstructionBranch(unsignedByte, byteSequence.readShort());
                    break;
                case 169:
                    multianewarray = new RET(z ? byteSequence.readUnsignedShort() : byteSequence.readUnsignedByte(), z);
                    break;
                case 170:
                    multianewarray = new TABLESWITCH(byteSequence);
                    break;
                case 171:
                    multianewarray = new LOOKUPSWITCH(byteSequence);
                    break;
                case 178:
                case 179:
                case 180:
                case 181:
                    multianewarray = new FieldInstruction(unsignedByte, byteSequence.readUnsignedShort());
                    break;
                case 182:
                case 183:
                case 184:
                    multianewarray = new InvokeInstruction(unsignedByte, byteSequence.readUnsignedShort());
                    break;
                case 185:
                    multianewarray = new INVOKEINTERFACE(byteSequence.readUnsignedShort(), byteSequence.readUnsignedByte(), byteSequence.readByte());
                    break;
                case 186:
                    multianewarray = new InvokeDynamic(byteSequence.readUnsignedShort(), byteSequence.readUnsignedShort());
                    break;
                case 187:
                    multianewarray = new InstructionCP((short) 187, byteSequence.readUnsignedShort());
                    break;
                case 188:
                    multianewarray = new InstructionByte((short) 188, byteSequence.readByte());
                    break;
                case 189:
                case 192:
                    multianewarray = new InstructionCP(unsignedByte, byteSequence.readUnsignedShort());
                    break;
                case 193:
                    multianewarray = new InstructionCP((short) 193, byteSequence.readUnsignedShort());
                    break;
                case 197:
                    multianewarray = new MULTIANEWARRAY(byteSequence.readUnsignedShort(), byteSequence.readByte());
                    break;
                case 200:
                case 201:
                    multianewarray = new InstructionBranch(unsignedByte, byteSequence.readInt());
                    break;
            }
            return multianewarray;
        } catch (ClassGenException e) {
            throw e;
        } catch (Exception e2) {
            throw new ClassGenException(e2.toString());
        }
    }

    public int consumeStack(ConstantPool constantPool) {
        return Constants.CONSUME_STACK[this.opcode];
    }

    public int produceStack(ConstantPool constantPool) {
        return Constants.stackEntriesProduced[this.opcode];
    }

    public short getOpcode() {
        return this.opcode;
    }

    public int getLength() {
        byte b = Constants.iLen[this.opcode];
        if ($assertionsDisabled || b != 0) {
            return b;
        }
        throw new AssertionError();
    }

    void dispose() {
    }

    public boolean equals(Object obj) {
        if (getClass() != Instruction.class) {
            throw new RuntimeException("NO WAY " + getClass());
        }
        return (obj instanceof Instruction) && ((Instruction) obj).opcode == this.opcode;
    }

    public int hashCode() {
        if (getClass() != Instruction.class) {
            throw new RuntimeException("NO WAY " + getClass());
        }
        return this.opcode * 37;
    }

    public Type getType() {
        return getType(null);
    }

    public Type getType(ConstantPool constantPool) {
        Type type = Constants.types[this.opcode];
        if (type != null) {
            return type;
        }
        throw new RuntimeException("Do not know type for instruction " + getName() + "(" + ((int) this.opcode) + ")");
    }

    public Number getValue() {
        if (!$assertionsDisabled && (instFlags[this.opcode] & 2) != 0) {
            throw new AssertionError();
        }
        switch (this.opcode) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return new Integer(this.opcode - 3);
            default:
                throw new IllegalStateException("Not implemented yet for " + getName());
        }
    }

    public int getIndex() {
        return -1;
    }

    public void setIndex(int i) {
        throw new IllegalStateException("Shouldnt be asking " + getName().toUpperCase());
    }

    public Object getValue(ConstantPool constantPool) {
        throw new IllegalStateException("Shouldnt be asking " + getName().toUpperCase());
    }

    public boolean isLoadInstruction() {
        return (Constants.instFlags[this.opcode] & 32) != 0;
    }

    public boolean isASTORE() {
        return false;
    }

    public boolean isALOAD() {
        return false;
    }

    public boolean isStoreInstruction() {
        return (Constants.instFlags[this.opcode] & 256) != 0;
    }

    public boolean isJsrInstruction() {
        return (Constants.instFlags[this.opcode] & 16384) != 0;
    }

    public boolean isConstantInstruction() {
        return (Constants.instFlags[this.opcode] & 2) != 0;
    }

    public boolean isConstantPoolInstruction() {
        return (Constants.instFlags[this.opcode] & 8) != 0;
    }

    public boolean isStackProducer() {
        return Constants.stackEntriesProduced[this.opcode] != 0;
    }

    public boolean isStackConsumer() {
        return Constants.CONSUME_STACK[this.opcode] != 0;
    }

    public boolean isIndexedInstruction() {
        return (Constants.instFlags[this.opcode] & 16) != 0;
    }

    public boolean isArrayCreationInstruction() {
        return this.opcode == 188 || this.opcode == 189 || this.opcode == 197;
    }

    public ObjectType getLoadClassType(ConstantPool constantPool) {
        if (!$assertionsDisabled && (Constants.instFlags[this.opcode] & 4) != 0) {
            throw new AssertionError();
        }
        Type type = getType(constantPool);
        if (type instanceof ArrayType) {
            type = ((ArrayType) type).getBasicType();
        }
        if (type instanceof ObjectType) {
            return (ObjectType) type;
        }
        return null;
    }

    public boolean isReturnInstruction() {
        return (Constants.instFlags[this.opcode] & Constants.RET_INST) != 0;
    }

    public boolean isLocalVariableInstruction() {
        return (Constants.instFlags[this.opcode] & 64) != 0;
    }

    public String toString(boolean z) {
        if (!z) {
            return getName();
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getName()).append(PropertyAccessor.PROPERTY_KEY_PREFIX).append((int) this.opcode).append("](size").append((int) Constants.iLen[this.opcode]).append(")");
        return stringBuffer.toString();
    }

    public String toString() {
        return toString(true);
    }

    static {
        $assertionsDisabled = !Instruction.class.desiredAssertionStatus();
    }
}
