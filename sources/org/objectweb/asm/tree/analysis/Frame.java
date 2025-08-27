package org.objectweb.asm.tree.analysis;

import java.util.ArrayList;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.Value;

/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/tree/analysis/Frame.SCL.lombok */
public class Frame<V extends Value> {
    private V returnValue;
    private V[] values;
    private int nLocals;
    private int nStack;

    public Frame(int i, int i2) {
        this.values = (V[]) new Value[i + i2];
        this.nLocals = i;
    }

    public Frame(Frame<? extends V> frame) {
        this(frame.nLocals, frame.values.length - frame.nLocals);
        init(frame);
    }

    public Frame<V> init(Frame<? extends V> frame) {
        this.returnValue = frame.returnValue;
        System.arraycopy(frame.values, 0, this.values, 0, this.values.length);
        this.nStack = frame.nStack;
        return this;
    }

    public void setReturn(V v) {
        this.returnValue = v;
    }

    public int getLocals() {
        return this.nLocals;
    }

    public int getMaxStackSize() {
        return this.values.length - this.nLocals;
    }

    public V getLocal(int index) {
        if (index >= this.nLocals) {
            throw new IndexOutOfBoundsException("Trying to access an inexistant local variable");
        }
        return this.values[index];
    }

    public void setLocal(int index, V value) {
        if (index >= this.nLocals) {
            throw new IndexOutOfBoundsException("Trying to access an inexistant local variable " + index);
        }
        this.values[index] = value;
    }

    public int getStackSize() {
        return this.nStack;
    }

    public V getStack(int index) {
        return this.values[this.nLocals + index];
    }

    public void clearStack() {
        this.nStack = 0;
    }

    public V pop() {
        if (this.nStack == 0) {
            throw new IndexOutOfBoundsException("Cannot pop operand off an empty stack.");
        }
        V[] vArr = this.values;
        int i = this.nLocals;
        int i2 = this.nStack - 1;
        this.nStack = i2;
        return vArr[i + i2];
    }

    public void push(V value) {
        if (this.nLocals + this.nStack >= this.values.length) {
            throw new IndexOutOfBoundsException("Insufficient maximum stack size.");
        }
        V[] vArr = this.values;
        int i = this.nLocals;
        int i2 = this.nStack;
        this.nStack = i2 + 1;
        vArr[i + i2] = value;
    }

    public void execute(AbstractInsnNode insn, Interpreter<V> interpreter) throws AnalyzerException {
        Value local;
        switch (insn.getOpcode()) {
            case 0:
            case 167:
            case 169:
                return;
            case 1:
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
            case 16:
            case 17:
            case 18:
                push(interpreter.newOperation(insn));
                return;
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
                throw new AnalyzerException(insn, "Illegal opcode " + insn.getOpcode());
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
                push(interpreter.copyOperation(insn, getLocal(((VarInsnNode) insn).var)));
                return;
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
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
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
                push(interpreter.binaryOperation(insn, pop(), pop()));
                return;
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
                Value valueCopyOperation = interpreter.copyOperation(insn, pop());
                int var = ((VarInsnNode) insn).var;
                setLocal(var, valueCopyOperation);
                if (valueCopyOperation.getSize() == 2) {
                    setLocal(var + 1, interpreter.newValue(null));
                }
                if (var > 0 && (local = getLocal(var - 1)) != null && local.getSize() == 2) {
                    setLocal(var - 1, interpreter.newValue(null));
                    return;
                }
                return;
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
                interpreter.ternaryOperation(insn, pop(), pop(), pop());
                return;
            case 87:
                if (pop().getSize() == 2) {
                    throw new AnalyzerException(insn, "Illegal use of POP");
                }
                return;
            case 88:
                if (pop().getSize() == 1 && pop().getSize() != 1) {
                    throw new AnalyzerException(insn, "Illegal use of POP2");
                }
                return;
            case 89:
                Value valuePop = pop();
                if (valuePop.getSize() != 1) {
                    throw new AnalyzerException(insn, "Illegal use of DUP");
                }
                push(valuePop);
                push(interpreter.copyOperation(insn, valuePop));
                return;
            case 90:
                Value valuePop2 = pop();
                Value valuePop3 = pop();
                if (valuePop2.getSize() != 1 || valuePop3.getSize() != 1) {
                    throw new AnalyzerException(insn, "Illegal use of DUP_X1");
                }
                push(interpreter.copyOperation(insn, valuePop2));
                push(valuePop3);
                push(valuePop2);
                return;
            case 91:
                Value valuePop4 = pop();
                if (valuePop4.getSize() == 1) {
                    Value valuePop5 = pop();
                    if (valuePop5.getSize() == 1) {
                        Value valuePop6 = pop();
                        if (valuePop6.getSize() == 1) {
                            push(interpreter.copyOperation(insn, valuePop4));
                            push(valuePop6);
                            push(valuePop5);
                            push(valuePop4);
                            return;
                        }
                    } else {
                        push(interpreter.copyOperation(insn, valuePop4));
                        push(valuePop5);
                        push(valuePop4);
                        return;
                    }
                }
                throw new AnalyzerException(insn, "Illegal use of DUP_X2");
            case 92:
                Value valuePop7 = pop();
                if (valuePop7.getSize() == 1) {
                    Value valuePop8 = pop();
                    if (valuePop8.getSize() == 1) {
                        push(valuePop8);
                        push(valuePop7);
                        push(interpreter.copyOperation(insn, valuePop8));
                        push(interpreter.copyOperation(insn, valuePop7));
                        return;
                    }
                    throw new AnalyzerException(insn, "Illegal use of DUP2");
                }
                push(valuePop7);
                push(interpreter.copyOperation(insn, valuePop7));
                return;
            case 93:
                Value valuePop9 = pop();
                if (valuePop9.getSize() == 1) {
                    Value valuePop10 = pop();
                    if (valuePop10.getSize() == 1) {
                        Value valuePop11 = pop();
                        if (valuePop11.getSize() == 1) {
                            push(interpreter.copyOperation(insn, valuePop10));
                            push(interpreter.copyOperation(insn, valuePop9));
                            push(valuePop11);
                            push(valuePop10);
                            push(valuePop9);
                            return;
                        }
                    }
                } else {
                    Value valuePop12 = pop();
                    if (valuePop12.getSize() == 1) {
                        push(interpreter.copyOperation(insn, valuePop9));
                        push(valuePop12);
                        push(valuePop9);
                        return;
                    }
                }
                throw new AnalyzerException(insn, "Illegal use of DUP2_X1");
            case 94:
                Value valuePop13 = pop();
                if (valuePop13.getSize() == 1) {
                    Value valuePop14 = pop();
                    if (valuePop14.getSize() == 1) {
                        Value valuePop15 = pop();
                        if (valuePop15.getSize() == 1) {
                            Value valuePop16 = pop();
                            if (valuePop16.getSize() == 1) {
                                push(interpreter.copyOperation(insn, valuePop14));
                                push(interpreter.copyOperation(insn, valuePop13));
                                push(valuePop16);
                                push(valuePop15);
                                push(valuePop14);
                                push(valuePop13);
                                return;
                            }
                        } else {
                            push(interpreter.copyOperation(insn, valuePop14));
                            push(interpreter.copyOperation(insn, valuePop13));
                            push(valuePop15);
                            push(valuePop14);
                            push(valuePop13);
                            return;
                        }
                    }
                } else {
                    Value valuePop17 = pop();
                    if (valuePop17.getSize() == 1) {
                        Value valuePop18 = pop();
                        if (valuePop18.getSize() == 1) {
                            push(interpreter.copyOperation(insn, valuePop13));
                            push(valuePop18);
                            push(valuePop17);
                            push(valuePop13);
                            return;
                        }
                    } else {
                        push(interpreter.copyOperation(insn, valuePop13));
                        push(valuePop17);
                        push(valuePop13);
                        return;
                    }
                }
                throw new AnalyzerException(insn, "Illegal use of DUP2_X2");
            case 95:
                Value valuePop19 = pop();
                Value valuePop20 = pop();
                if (valuePop20.getSize() != 1 || valuePop19.getSize() != 1) {
                    throw new AnalyzerException(insn, "Illegal use of SWAP");
                }
                push(interpreter.copyOperation(insn, valuePop19));
                push(interpreter.copyOperation(insn, valuePop20));
                return;
            case 116:
            case 117:
            case 118:
            case 119:
                push(interpreter.unaryOperation(insn, pop()));
                return;
            case 132:
                int var2 = ((IincInsnNode) insn).var;
                setLocal(var2, interpreter.unaryOperation(insn, getLocal(var2)));
                return;
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
                push(interpreter.unaryOperation(insn, pop()));
                return;
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
                interpreter.unaryOperation(insn, pop());
                return;
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 181:
                interpreter.binaryOperation(insn, pop(), pop());
                return;
            case 168:
                push(interpreter.newOperation(insn));
                return;
            case 170:
            case 171:
                interpreter.unaryOperation(insn, pop());
                return;
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
                Value valuePop21 = pop();
                interpreter.unaryOperation(insn, valuePop21);
                interpreter.returnOperation(insn, valuePop21, this.returnValue);
                return;
            case 177:
                if (this.returnValue != null) {
                    throw new AnalyzerException(insn, "Incompatible return type");
                }
                return;
            case 178:
                push(interpreter.newOperation(insn));
                return;
            case 179:
                interpreter.unaryOperation(insn, pop());
                return;
            case 180:
                push(interpreter.unaryOperation(insn, pop()));
                return;
            case 182:
            case 183:
            case 184:
            case 185:
                ArrayList arrayList = new ArrayList();
                String methodDescriptor = ((MethodInsnNode) insn).desc;
                for (int i = Type.getArgumentTypes(methodDescriptor).length; i > 0; i--) {
                    arrayList.add(0, pop());
                }
                if (insn.getOpcode() != 184) {
                    arrayList.add(0, pop());
                }
                if (Type.getReturnType(methodDescriptor) == Type.VOID_TYPE) {
                    interpreter.naryOperation(insn, arrayList);
                    return;
                } else {
                    push(interpreter.naryOperation(insn, arrayList));
                    return;
                }
            case 186:
                ArrayList arrayList2 = new ArrayList();
                String methodDesccriptor = ((InvokeDynamicInsnNode) insn).desc;
                for (int i2 = Type.getArgumentTypes(methodDesccriptor).length; i2 > 0; i2--) {
                    arrayList2.add(0, pop());
                }
                if (Type.getReturnType(methodDesccriptor) == Type.VOID_TYPE) {
                    interpreter.naryOperation(insn, arrayList2);
                    return;
                } else {
                    push(interpreter.naryOperation(insn, arrayList2));
                    return;
                }
            case 187:
                push(interpreter.newOperation(insn));
                return;
            case 188:
            case 189:
            case 190:
                push(interpreter.unaryOperation(insn, pop()));
                return;
            case 191:
                interpreter.unaryOperation(insn, pop());
                return;
            case 192:
            case 193:
                push(interpreter.unaryOperation(insn, pop()));
                return;
            case 194:
            case 195:
                interpreter.unaryOperation(insn, pop());
                return;
            case 197:
                ArrayList arrayList3 = new ArrayList();
                for (int i3 = ((MultiANewArrayInsnNode) insn).dims; i3 > 0; i3--) {
                    arrayList3.add(0, pop());
                }
                push(interpreter.naryOperation(insn, arrayList3));
                return;
            case 198:
            case 199:
                interpreter.unaryOperation(insn, pop());
                return;
        }
    }

    public boolean merge(Frame<? extends V> frame, Interpreter<V> interpreter) throws AnalyzerException {
        if (this.nStack != frame.nStack) {
            throw new AnalyzerException(null, "Incompatible stack heights");
        }
        boolean z = false;
        for (int i = 0; i < this.nLocals + this.nStack; i++) {
            Value valueMerge = interpreter.merge(this.values[i], frame.values[i]);
            if (!valueMerge.equals(this.values[i])) {
                ((V[]) this.values)[i] = valueMerge;
                z = true;
            }
        }
        return z;
    }

    public boolean merge(Frame<? extends V> frame, boolean[] localsUsed) {
        boolean changed = false;
        for (int i = 0; i < this.nLocals; i++) {
            if (!localsUsed[i] && !this.values[i].equals(frame.values[i])) {
                this.values[i] = frame.values[i];
                changed = true;
            }
        }
        return changed;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getLocals(); i++) {
            stringBuilder.append(getLocal(i));
        }
        stringBuilder.append(' ');
        for (int i2 = 0; i2 < getStackSize(); i2++) {
            stringBuilder.append(getStack(i2).toString());
        }
        return stringBuilder.toString();
    }
}
