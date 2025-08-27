package org.objectweb.asm;

/* JADX WARN: Classes with same name are omitted:
  asm-4.2.jar:org/objectweb/asm/Frame.class
 */
/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/Frame.SCL.lombok */
class Frame {
    static final int SAME_FRAME = 0;
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
    static final int RESERVED = 128;
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
    static final int CHOP_FRAME = 248;
    static final int SAME_FRAME_EXTENDED = 251;
    static final int APPEND_FRAME = 252;
    static final int FULL_FRAME = 255;
    static final int ITEM_TOP = 0;
    static final int ITEM_INTEGER = 1;
    static final int ITEM_FLOAT = 2;
    static final int ITEM_DOUBLE = 3;
    static final int ITEM_LONG = 4;
    static final int ITEM_NULL = 5;
    static final int ITEM_UNINITIALIZED_THIS = 6;
    static final int ITEM_OBJECT = 7;
    static final int ITEM_UNINITIALIZED = 8;
    private static final int ITEM_ASM_BOOLEAN = 9;
    private static final int ITEM_ASM_BYTE = 10;
    private static final int ITEM_ASM_CHAR = 11;
    private static final int ITEM_ASM_SHORT = 12;
    private static final int DIM_MASK = -268435456;
    private static final int KIND_MASK = 251658240;
    private static final int FLAGS_MASK = 15728640;
    private static final int VALUE_MASK = 1048575;
    private static final int DIM_SHIFT = 28;
    private static final int ARRAY_OF = 268435456;
    private static final int ELEMENT_OF = -268435456;
    private static final int CONSTANT_KIND = 16777216;
    private static final int REFERENCE_KIND = 33554432;
    private static final int UNINITIALIZED_KIND = 50331648;
    private static final int LOCAL_KIND = 67108864;
    private static final int STACK_KIND = 83886080;
    private static final int TOP_IF_LONG_OR_DOUBLE_FLAG = 1048576;
    private static final int TOP = 16777216;
    private static final int BOOLEAN = 16777225;
    private static final int BYTE = 16777226;
    private static final int CHAR = 16777227;
    private static final int SHORT = 16777228;
    private static final int INTEGER = 16777217;
    private static final int FLOAT = 16777218;
    private static final int LONG = 16777220;
    private static final int DOUBLE = 16777219;
    private static final int NULL = 16777221;
    private static final int UNINITIALIZED_THIS = 16777222;
    Label owner;
    private int[] inputLocals;
    private int[] inputStack;
    private int[] outputLocals;
    private int[] outputStack;
    private short outputStackStart;
    private short outputStackTop;
    private int initializationCount;
    private int[] initializations;

    static int getAbstractTypeFromApiFormat(SymbolTable symbolTable, Object type) {
        if (type instanceof Integer) {
            return 16777216 | ((Integer) type).intValue();
        }
        if (type instanceof String) {
            String descriptor = Type.getObjectType((String) type).getDescriptor();
            return getAbstractTypeFromDescriptor(symbolTable, descriptor, 0);
        }
        return UNINITIALIZED_KIND | symbolTable.addUninitializedType("", ((Label) type).bytecodeOffset);
    }

    static int getAbstractTypeFromInternalName(SymbolTable symbolTable, String internalName) {
        return 33554432 | symbolTable.addType(internalName);
    }

    private static int getAbstractTypeFromDescriptor(SymbolTable symbolTable, String buffer, int offset) {
        int typeValue;
        switch (buffer.charAt(offset)) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                return 16777217;
            case 'D':
                return 16777219;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new IllegalArgumentException();
            case 'F':
                return 16777218;
            case 'J':
                return 16777220;
            case 'L':
                String internalName = buffer.substring(offset + 1, buffer.length() - 1);
                return 33554432 | symbolTable.addType(internalName);
            case 'V':
                return 0;
            case '[':
                int elementDescriptorOffset = offset + 1;
                while (buffer.charAt(elementDescriptorOffset) == '[') {
                    elementDescriptorOffset++;
                }
                switch (buffer.charAt(elementDescriptorOffset)) {
                    case 'B':
                        typeValue = BYTE;
                        break;
                    case 'C':
                        typeValue = CHAR;
                        break;
                    case 'D':
                        typeValue = 16777219;
                        break;
                    case 'E':
                    case 'G':
                    case 'H':
                    case 'K':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    default:
                        throw new IllegalArgumentException();
                    case 'F':
                        typeValue = 16777218;
                        break;
                    case 'I':
                        typeValue = 16777217;
                        break;
                    case 'J':
                        typeValue = 16777220;
                        break;
                    case 'L':
                        String internalName2 = buffer.substring(elementDescriptorOffset + 1, buffer.length() - 1);
                        typeValue = 33554432 | symbolTable.addType(internalName2);
                        break;
                    case 'S':
                        typeValue = SHORT;
                        break;
                    case 'Z':
                        typeValue = 16777225;
                        break;
                }
                return ((elementDescriptorOffset - offset) << 28) | typeValue;
        }
    }

    Frame(Label owner) {
        this.owner = owner;
    }

    final void copyFrom(Frame frame) {
        this.inputLocals = frame.inputLocals;
        this.inputStack = frame.inputStack;
        this.outputStackStart = (short) 0;
        this.outputLocals = frame.outputLocals;
        this.outputStack = frame.outputStack;
        this.outputStackTop = frame.outputStackTop;
        this.initializationCount = frame.initializationCount;
        this.initializations = frame.initializations;
    }

    final void setInputFrameFromDescriptor(SymbolTable symbolTable, int access, String descriptor, int maxLocals) {
        this.inputLocals = new int[maxLocals];
        this.inputStack = new int[0];
        int inputLocalIndex = 0;
        if ((access & 8) == 0) {
            if ((access & 262144) == 0) {
                inputLocalIndex = 0 + 1;
                this.inputLocals[0] = 33554432 | symbolTable.addType(symbolTable.getClassName());
            } else {
                inputLocalIndex = 0 + 1;
                this.inputLocals[0] = 16777222;
            }
        }
        for (Type argumentType : Type.getArgumentTypes(descriptor)) {
            int abstractType = getAbstractTypeFromDescriptor(symbolTable, argumentType.getDescriptor(), 0);
            int i = inputLocalIndex;
            inputLocalIndex++;
            this.inputLocals[i] = abstractType;
            if (abstractType == 16777220 || abstractType == 16777219) {
                inputLocalIndex++;
                this.inputLocals[inputLocalIndex] = 16777216;
            }
        }
        while (inputLocalIndex < maxLocals) {
            int i2 = inputLocalIndex;
            inputLocalIndex++;
            this.inputLocals[i2] = 16777216;
        }
    }

    final void setInputFrameFromApiFormat(SymbolTable symbolTable, int nLocal, Object[] local, int nStack, Object[] stack) {
        int inputLocalIndex = 0;
        for (int i = 0; i < nLocal; i++) {
            int i2 = inputLocalIndex;
            inputLocalIndex++;
            this.inputLocals[i2] = getAbstractTypeFromApiFormat(symbolTable, local[i]);
            if (local[i] == Opcodes.LONG || local[i] == Opcodes.DOUBLE) {
                inputLocalIndex++;
                this.inputLocals[inputLocalIndex] = 16777216;
            }
        }
        while (inputLocalIndex < this.inputLocals.length) {
            int i3 = inputLocalIndex;
            inputLocalIndex++;
            this.inputLocals[i3] = 16777216;
        }
        int nStackTop = 0;
        for (int i4 = 0; i4 < nStack; i4++) {
            if (stack[i4] == Opcodes.LONG || stack[i4] == Opcodes.DOUBLE) {
                nStackTop++;
            }
        }
        this.inputStack = new int[nStack + nStackTop];
        int inputStackIndex = 0;
        for (int i5 = 0; i5 < nStack; i5++) {
            int i6 = inputStackIndex;
            inputStackIndex++;
            this.inputStack[i6] = getAbstractTypeFromApiFormat(symbolTable, stack[i5]);
            if (stack[i5] == Opcodes.LONG || stack[i5] == Opcodes.DOUBLE) {
                inputStackIndex++;
                this.inputStack[inputStackIndex] = 16777216;
            }
        }
        this.outputStackTop = (short) 0;
        this.initializationCount = 0;
    }

    final int getInputStackSize() {
        return this.inputStack.length;
    }

    private int getLocal(int localIndex) {
        if (this.outputLocals == null || localIndex >= this.outputLocals.length) {
            return 67108864 | localIndex;
        }
        int abstractType = this.outputLocals[localIndex];
        if (abstractType == 0) {
            int i = 67108864 | localIndex;
            this.outputLocals[localIndex] = i;
            abstractType = i;
        }
        return abstractType;
    }

    private void setLocal(int localIndex, int abstractType) {
        if (this.outputLocals == null) {
            this.outputLocals = new int[10];
        }
        int outputLocalsLength = this.outputLocals.length;
        if (localIndex >= outputLocalsLength) {
            int[] newOutputLocals = new int[Math.max(localIndex + 1, 2 * outputLocalsLength)];
            System.arraycopy(this.outputLocals, 0, newOutputLocals, 0, outputLocalsLength);
            this.outputLocals = newOutputLocals;
        }
        this.outputLocals[localIndex] = abstractType;
    }

    private void push(int abstractType) {
        if (this.outputStack == null) {
            this.outputStack = new int[10];
        }
        int outputStackLength = this.outputStack.length;
        if (this.outputStackTop >= outputStackLength) {
            int[] newOutputStack = new int[Math.max(this.outputStackTop + 1, 2 * outputStackLength)];
            System.arraycopy(this.outputStack, 0, newOutputStack, 0, outputStackLength);
            this.outputStack = newOutputStack;
        }
        int[] iArr = this.outputStack;
        short s = this.outputStackTop;
        this.outputStackTop = (short) (s + 1);
        iArr[s] = abstractType;
        short outputStackSize = (short) (this.outputStackStart + this.outputStackTop);
        if (outputStackSize > this.owner.outputStackMax) {
            this.owner.outputStackMax = outputStackSize;
        }
    }

    private void push(SymbolTable symbolTable, String descriptor) {
        int typeDescriptorOffset = descriptor.charAt(0) == '(' ? descriptor.indexOf(41) + 1 : 0;
        int abstractType = getAbstractTypeFromDescriptor(symbolTable, descriptor, typeDescriptorOffset);
        if (abstractType != 0) {
            push(abstractType);
            if (abstractType == 16777220 || abstractType == 16777219) {
                push(16777216);
            }
        }
    }

    private int pop() {
        if (this.outputStackTop > 0) {
            int[] iArr = this.outputStack;
            short s = (short) (this.outputStackTop - 1);
            this.outputStackTop = s;
            return iArr[s];
        }
        short s2 = (short) (this.outputStackStart - 1);
        this.outputStackStart = s2;
        return STACK_KIND | (-s2);
    }

    private void pop(int elements) {
        if (this.outputStackTop >= elements) {
            this.outputStackTop = (short) (this.outputStackTop - elements);
        } else {
            this.outputStackStart = (short) (this.outputStackStart - (elements - this.outputStackTop));
            this.outputStackTop = (short) 0;
        }
    }

    private void pop(String descriptor) {
        char firstDescriptorChar = descriptor.charAt(0);
        if (firstDescriptorChar == '(') {
            pop((Type.getArgumentsAndReturnSizes(descriptor) >> 2) - 1);
        } else if (firstDescriptorChar == 'J' || firstDescriptorChar == 'D') {
            pop(2);
        } else {
            pop(1);
        }
    }

    private void addInitializedType(int abstractType) {
        if (this.initializations == null) {
            this.initializations = new int[2];
        }
        int initializationsLength = this.initializations.length;
        if (this.initializationCount >= initializationsLength) {
            int[] newInitializations = new int[Math.max(this.initializationCount + 1, 2 * initializationsLength)];
            System.arraycopy(this.initializations, 0, newInitializations, 0, initializationsLength);
            this.initializations = newInitializations;
        }
        int[] iArr = this.initializations;
        int i = this.initializationCount;
        this.initializationCount = i + 1;
        iArr[i] = abstractType;
    }

    private int getInitializedType(SymbolTable symbolTable, int abstractType) {
        if (abstractType == 16777222 || (abstractType & (-16777216)) == UNINITIALIZED_KIND) {
            for (int i = 0; i < this.initializationCount; i++) {
                int initializedType = this.initializations[i];
                int dim = initializedType & (-268435456);
                int kind = initializedType & KIND_MASK;
                int value = initializedType & 1048575;
                if (kind == 67108864) {
                    initializedType = dim + this.inputLocals[value];
                } else if (kind == STACK_KIND) {
                    initializedType = dim + this.inputStack[this.inputStack.length - value];
                }
                if (abstractType == initializedType) {
                    if (abstractType == 16777222) {
                        return 33554432 | symbolTable.addType(symbolTable.getClassName());
                    }
                    return 33554432 | symbolTable.addType(symbolTable.getType(abstractType & 1048575).value);
                }
            }
        }
        return abstractType;
    }

    void execute(int opcode, int arg, Symbol argSymbol, SymbolTable symbolTable) {
        switch (opcode) {
            case 0:
            case 116:
            case 117:
            case 118:
            case 119:
            case 145:
            case 146:
            case 147:
            case 167:
            case 177:
                return;
            case 1:
                push(16777221);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 16:
            case 17:
            case 21:
                push(16777217);
                return;
            case 9:
            case 10:
            case 22:
                push(16777220);
                push(16777216);
                return;
            case 11:
            case 12:
            case 13:
            case 23:
                push(16777218);
                return;
            case 14:
            case 15:
            case 24:
                push(16777219);
                push(16777216);
                return;
            case 18:
                switch (argSymbol.tag) {
                    case 3:
                        push(16777217);
                        return;
                    case 4:
                        push(16777218);
                        return;
                    case 5:
                        push(16777220);
                        push(16777216);
                        return;
                    case 6:
                        push(16777219);
                        push(16777216);
                        return;
                    case 7:
                        push(33554432 | symbolTable.addType("java/lang/Class"));
                        return;
                    case 8:
                        push(33554432 | symbolTable.addType("java/lang/String"));
                        return;
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    default:
                        throw new AssertionError();
                    case 15:
                        push(33554432 | symbolTable.addType("java/lang/invoke/MethodHandle"));
                        return;
                    case 16:
                        push(33554432 | symbolTable.addType("java/lang/invoke/MethodType"));
                        return;
                    case 17:
                        push(symbolTable, argSymbol.value);
                        return;
                }
            case 19:
            case 20:
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
            case 196:
            default:
                throw new IllegalArgumentException();
            case 25:
                push(getLocal(arg));
                return;
            case 46:
            case 51:
            case 52:
            case 53:
            case 96:
            case 100:
            case 104:
            case 108:
            case 112:
            case 120:
            case 122:
            case 124:
            case 126:
            case 128:
            case 130:
            case 136:
            case 142:
            case 149:
            case 150:
                pop(2);
                push(16777217);
                return;
            case 47:
            case 143:
                pop(2);
                push(16777220);
                push(16777216);
                return;
            case 48:
            case 98:
            case 102:
            case 106:
            case 110:
            case 114:
            case 137:
            case 144:
                pop(2);
                push(16777218);
                return;
            case 49:
            case 138:
                pop(2);
                push(16777219);
                push(16777216);
                return;
            case 50:
                pop(1);
                int abstractType1 = pop();
                push(abstractType1 == 16777221 ? abstractType1 : (-268435456) + abstractType1);
                return;
            case 54:
            case 56:
            case 58:
                int abstractType12 = pop();
                setLocal(arg, abstractType12);
                if (arg > 0) {
                    int previousLocalType = getLocal(arg - 1);
                    if (previousLocalType == 16777220 || previousLocalType == 16777219) {
                        setLocal(arg - 1, 16777216);
                        return;
                    } else {
                        if ((previousLocalType & KIND_MASK) == 67108864 || (previousLocalType & KIND_MASK) == STACK_KIND) {
                            setLocal(arg - 1, previousLocalType | 1048576);
                            return;
                        }
                        return;
                    }
                }
                return;
            case 55:
            case 57:
                pop(1);
                int abstractType13 = pop();
                setLocal(arg, abstractType13);
                setLocal(arg + 1, 16777216);
                if (arg > 0) {
                    int previousLocalType2 = getLocal(arg - 1);
                    if (previousLocalType2 == 16777220 || previousLocalType2 == 16777219) {
                        setLocal(arg - 1, 16777216);
                        return;
                    } else {
                        if ((previousLocalType2 & KIND_MASK) == 67108864 || (previousLocalType2 & KIND_MASK) == STACK_KIND) {
                            setLocal(arg - 1, previousLocalType2 | 1048576);
                            return;
                        }
                        return;
                    }
                }
                return;
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
                pop(3);
                return;
            case 80:
            case 82:
                pop(4);
                return;
            case 87:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 170:
            case 171:
            case 172:
            case 174:
            case 176:
            case 191:
            case 194:
            case 195:
            case 198:
            case 199:
                pop(1);
                return;
            case 88:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 173:
            case 175:
                pop(2);
                return;
            case 89:
                int abstractType14 = pop();
                push(abstractType14);
                push(abstractType14);
                return;
            case 90:
                int abstractType15 = pop();
                int abstractType2 = pop();
                push(abstractType15);
                push(abstractType2);
                push(abstractType15);
                return;
            case 91:
                int abstractType16 = pop();
                int abstractType22 = pop();
                int abstractType3 = pop();
                push(abstractType16);
                push(abstractType3);
                push(abstractType22);
                push(abstractType16);
                return;
            case 92:
                int abstractType17 = pop();
                int abstractType23 = pop();
                push(abstractType23);
                push(abstractType17);
                push(abstractType23);
                push(abstractType17);
                return;
            case 93:
                int abstractType18 = pop();
                int abstractType24 = pop();
                int abstractType32 = pop();
                push(abstractType24);
                push(abstractType18);
                push(abstractType32);
                push(abstractType24);
                push(abstractType18);
                return;
            case 94:
                int abstractType19 = pop();
                int abstractType25 = pop();
                int abstractType33 = pop();
                int abstractType4 = pop();
                push(abstractType25);
                push(abstractType19);
                push(abstractType4);
                push(abstractType33);
                push(abstractType25);
                push(abstractType19);
                return;
            case 95:
                int abstractType110 = pop();
                int abstractType26 = pop();
                push(abstractType110);
                push(abstractType26);
                return;
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 127:
            case 129:
            case 131:
                pop(4);
                push(16777220);
                push(16777216);
                return;
            case 99:
            case 103:
            case 107:
            case 111:
            case 115:
                pop(4);
                push(16777219);
                push(16777216);
                return;
            case 121:
            case 123:
            case 125:
                pop(3);
                push(16777220);
                push(16777216);
                return;
            case 132:
                setLocal(arg, 16777217);
                return;
            case 133:
            case 140:
                pop(1);
                push(16777220);
                push(16777216);
                return;
            case 134:
                pop(1);
                push(16777218);
                return;
            case 135:
            case 141:
                pop(1);
                push(16777219);
                push(16777216);
                return;
            case 139:
            case 190:
            case 193:
                pop(1);
                push(16777217);
                return;
            case 148:
            case 151:
            case 152:
                pop(4);
                push(16777217);
                return;
            case 168:
            case 169:
                throw new IllegalArgumentException("JSR/RET are not supported with computeFrames option");
            case 178:
                push(symbolTable, argSymbol.value);
                return;
            case 179:
                pop(argSymbol.value);
                return;
            case 180:
                pop(1);
                push(symbolTable, argSymbol.value);
                return;
            case 181:
                pop(argSymbol.value);
                pop();
                return;
            case 182:
            case 183:
            case 184:
            case 185:
                pop(argSymbol.value);
                if (opcode != 184) {
                    int abstractType111 = pop();
                    if (opcode == 183 && argSymbol.name.charAt(0) == '<') {
                        addInitializedType(abstractType111);
                    }
                }
                push(symbolTable, argSymbol.value);
                return;
            case 186:
                pop(argSymbol.value);
                push(symbolTable, argSymbol.value);
                return;
            case 187:
                push(UNINITIALIZED_KIND | symbolTable.addUninitializedType(argSymbol.value, arg));
                return;
            case 188:
                pop();
                switch (arg) {
                    case 4:
                        push(285212681);
                        return;
                    case 5:
                        push(285212683);
                        return;
                    case 6:
                        push(285212674);
                        return;
                    case 7:
                        push(285212675);
                        return;
                    case 8:
                        push(285212682);
                        return;
                    case 9:
                        push(285212684);
                        return;
                    case 10:
                        push(285212673);
                        return;
                    case 11:
                        push(285212676);
                        return;
                    default:
                        throw new IllegalArgumentException();
                }
            case 189:
                String arrayElementType = argSymbol.value;
                pop();
                if (arrayElementType.charAt(0) == '[') {
                    push(symbolTable, '[' + arrayElementType);
                    return;
                } else {
                    push(301989888 | symbolTable.addType(arrayElementType));
                    return;
                }
            case 192:
                String castType = argSymbol.value;
                pop();
                if (castType.charAt(0) == '[') {
                    push(symbolTable, castType);
                    return;
                } else {
                    push(33554432 | symbolTable.addType(castType));
                    return;
                }
            case 197:
                pop(arg);
                push(symbolTable, argSymbol.value);
                return;
        }
    }

    final boolean merge(SymbolTable symbolTable, Frame dstFrame, int catchTypeIndex) {
        int concreteOutputType;
        int concreteOutputType2;
        int abstractOutputType;
        boolean frameChanged = false;
        int nLocal = this.inputLocals.length;
        int nStack = this.inputStack.length;
        if (dstFrame.inputLocals == null) {
            dstFrame.inputLocals = new int[nLocal];
            frameChanged = true;
        }
        int i = 0;
        while (i < nLocal) {
            if (this.outputLocals == null || i >= this.outputLocals.length || (abstractOutputType = this.outputLocals[i]) == 0) {
                concreteOutputType2 = this.inputLocals[i];
            } else {
                int dim = abstractOutputType & (-268435456);
                int kind = abstractOutputType & KIND_MASK;
                if (kind == 67108864) {
                    concreteOutputType2 = dim + this.inputLocals[abstractOutputType & 1048575];
                    if ((abstractOutputType & 1048576) != 0 && (concreteOutputType2 == 16777220 || concreteOutputType2 == 16777219)) {
                        concreteOutputType2 = 16777216;
                    }
                } else if (kind == STACK_KIND) {
                    concreteOutputType2 = dim + this.inputStack[nStack - (abstractOutputType & 1048575)];
                    if ((abstractOutputType & 1048576) != 0 && (concreteOutputType2 == 16777220 || concreteOutputType2 == 16777219)) {
                        concreteOutputType2 = 16777216;
                    }
                } else {
                    concreteOutputType2 = abstractOutputType;
                }
            }
            if (this.initializations != null) {
                concreteOutputType2 = getInitializedType(symbolTable, concreteOutputType2);
            }
            frameChanged |= merge(symbolTable, concreteOutputType2, dstFrame.inputLocals, i);
            i++;
        }
        if (catchTypeIndex > 0) {
            for (int i2 = 0; i2 < nLocal; i2++) {
                frameChanged |= merge(symbolTable, this.inputLocals[i2], dstFrame.inputLocals, i2);
            }
            if (dstFrame.inputStack == null) {
                dstFrame.inputStack = new int[1];
                frameChanged = true;
            }
            return frameChanged | merge(symbolTable, catchTypeIndex, dstFrame.inputStack, 0);
        }
        int nInputStack = this.inputStack.length + this.outputStackStart;
        if (dstFrame.inputStack == null) {
            dstFrame.inputStack = new int[nInputStack + this.outputStackTop];
            frameChanged = true;
        }
        for (int i3 = 0; i3 < nInputStack; i3++) {
            int concreteOutputType3 = this.inputStack[i3];
            if (this.initializations != null) {
                concreteOutputType3 = getInitializedType(symbolTable, concreteOutputType3);
            }
            frameChanged |= merge(symbolTable, concreteOutputType3, dstFrame.inputStack, i3);
        }
        for (int i4 = 0; i4 < this.outputStackTop; i4++) {
            int abstractOutputType2 = this.outputStack[i4];
            int dim2 = abstractOutputType2 & (-268435456);
            int kind2 = abstractOutputType2 & KIND_MASK;
            if (kind2 == 67108864) {
                concreteOutputType = dim2 + this.inputLocals[abstractOutputType2 & 1048575];
                if ((abstractOutputType2 & 1048576) != 0 && (concreteOutputType == 16777220 || concreteOutputType == 16777219)) {
                    concreteOutputType = 16777216;
                }
            } else if (kind2 == STACK_KIND) {
                concreteOutputType = dim2 + this.inputStack[nStack - (abstractOutputType2 & 1048575)];
                if ((abstractOutputType2 & 1048576) != 0 && (concreteOutputType == 16777220 || concreteOutputType == 16777219)) {
                    concreteOutputType = 16777216;
                }
            } else {
                concreteOutputType = abstractOutputType2;
            }
            if (this.initializations != null) {
                concreteOutputType = getInitializedType(symbolTable, concreteOutputType);
            }
            frameChanged |= merge(symbolTable, concreteOutputType, dstFrame.inputStack, nInputStack + i4);
        }
        return frameChanged;
    }

    private static boolean merge(SymbolTable symbolTable, int sourceType, int[] dstTypes, int dstIndex) {
        int mergedType;
        int dstType = dstTypes[dstIndex];
        if (dstType == sourceType) {
            return false;
        }
        int srcType = sourceType;
        if ((sourceType & 268435455) == 16777221) {
            if (dstType == 16777221) {
                return false;
            }
            srcType = 16777221;
        }
        if (dstType == 0) {
            dstTypes[dstIndex] = srcType;
            return true;
        }
        if ((dstType & (-268435456)) != 0 || (dstType & KIND_MASK) == 33554432) {
            if (srcType == 16777221) {
                return false;
            }
            if ((srcType & (-16777216)) == (dstType & (-16777216))) {
                if ((dstType & KIND_MASK) == 33554432) {
                    mergedType = (srcType & (-268435456)) | 33554432 | symbolTable.addMergedType(srcType & 1048575, dstType & 1048575);
                } else {
                    int mergedDim = (-268435456) + (srcType & (-268435456));
                    mergedType = mergedDim | 33554432 | symbolTable.addType("java/lang/Object");
                }
            } else if ((srcType & (-268435456)) != 0 || (srcType & KIND_MASK) == 33554432) {
                int srcDim = srcType & (-268435456);
                if (srcDim != 0 && (srcType & KIND_MASK) != 33554432) {
                    srcDim = (-268435456) + srcDim;
                }
                int dstDim = dstType & (-268435456);
                if (dstDim != 0 && (dstType & KIND_MASK) != 33554432) {
                    dstDim = (-268435456) + dstDim;
                }
                mergedType = Math.min(srcDim, dstDim) | 33554432 | symbolTable.addType("java/lang/Object");
            } else {
                mergedType = 16777216;
            }
        } else if (dstType == 16777221) {
            mergedType = ((srcType & (-268435456)) != 0 || (srcType & KIND_MASK) == 33554432) ? srcType : 16777216;
        } else {
            mergedType = 16777216;
        }
        if (mergedType != dstType) {
            dstTypes[dstIndex] = mergedType;
            return true;
        }
        return false;
    }

    final void accept(MethodWriter methodWriter) {
        int[] localTypes = this.inputLocals;
        int nLocal = 0;
        int nTrailingTop = 0;
        int i = 0;
        while (i < localTypes.length) {
            int localType = localTypes[i];
            i += (localType == 16777220 || localType == 16777219) ? 2 : 1;
            if (localType == 16777216) {
                nTrailingTop++;
            } else {
                nLocal += nTrailingTop + 1;
                nTrailingTop = 0;
            }
        }
        int[] stackTypes = this.inputStack;
        int nStack = 0;
        int i2 = 0;
        while (i2 < stackTypes.length) {
            int stackType = stackTypes[i2];
            i2 += (stackType == 16777220 || stackType == 16777219) ? 2 : 1;
            nStack++;
        }
        int frameIndex = methodWriter.visitFrameStart(this.owner.bytecodeOffset, nLocal, nStack);
        int i3 = 0;
        while (true) {
            int i4 = nLocal;
            nLocal--;
            if (i4 <= 0) {
                break;
            }
            int localType2 = localTypes[i3];
            i3 += (localType2 == 16777220 || localType2 == 16777219) ? 2 : 1;
            int i5 = frameIndex;
            frameIndex++;
            methodWriter.visitAbstractType(i5, localType2);
        }
        int i6 = 0;
        while (true) {
            int i7 = nStack;
            nStack--;
            if (i7 > 0) {
                int stackType2 = stackTypes[i6];
                i6 += (stackType2 == 16777220 || stackType2 == 16777219) ? 2 : 1;
                int i8 = frameIndex;
                frameIndex++;
                methodWriter.visitAbstractType(i8, stackType2);
            } else {
                methodWriter.visitFrameEnd();
                return;
            }
        }
    }

    static void putAbstractType(SymbolTable symbolTable, int abstractType, ByteVector output) {
        int arrayDimensions = (abstractType & (-268435456)) >> 28;
        if (arrayDimensions == 0) {
            int typeValue = abstractType & 1048575;
            switch (abstractType & KIND_MASK) {
                case 16777216:
                    output.putByte(typeValue);
                    return;
                case 33554432:
                    output.putByte(7).putShort(symbolTable.addConstantClass(symbolTable.getType(typeValue).value).index);
                    return;
                case UNINITIALIZED_KIND /* 50331648 */:
                    output.putByte(8).putShort((int) symbolTable.getType(typeValue).data);
                    return;
                default:
                    throw new AssertionError();
            }
        }
        StringBuilder typeDescriptor = new StringBuilder();
        while (true) {
            int i = arrayDimensions;
            arrayDimensions--;
            if (i <= 0) {
                break;
            } else {
                typeDescriptor.append('[');
            }
        }
        if ((abstractType & KIND_MASK) == 33554432) {
            typeDescriptor.append('L').append(symbolTable.getType(abstractType & 1048575).value).append(';');
        } else {
            switch (abstractType & 1048575) {
                case 1:
                    typeDescriptor.append('I');
                    break;
                case 2:
                    typeDescriptor.append('F');
                    break;
                case 3:
                    typeDescriptor.append('D');
                    break;
                case 4:
                    typeDescriptor.append('J');
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                default:
                    throw new AssertionError();
                case 9:
                    typeDescriptor.append('Z');
                    break;
                case 10:
                    typeDescriptor.append('B');
                    break;
                case 11:
                    typeDescriptor.append('C');
                    break;
                case 12:
                    typeDescriptor.append('S');
                    break;
            }
        }
        output.putByte(7).putShort(symbolTable.addConstantClass(typeDescriptor.toString()).index);
    }
}
