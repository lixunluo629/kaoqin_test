package net.sf.cglib.core;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/LocalVariablesSorter.class */
public class LocalVariablesSorter extends MethodVisitor {
    protected final int firstLocal;
    private final State state;

    /* renamed from: net.sf.cglib.core.LocalVariablesSorter$1, reason: invalid class name */
    /* loaded from: cglib-3.1.jar:net/sf/cglib/core/LocalVariablesSorter$1.class */
    static class AnonymousClass1 {
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/core/LocalVariablesSorter$State.class */
    private static class State {
        int[] mapping;
        int nextLocal;

        private State() {
            this.mapping = new int[40];
        }

        State(AnonymousClass1 x0) {
            this();
        }
    }

    public LocalVariablesSorter(int access, String desc, MethodVisitor mv) {
        super(262144, mv);
        this.state = new State(null);
        Type[] args = Type.getArgumentTypes(desc);
        this.state.nextLocal = (8 & access) != 0 ? 0 : 1;
        for (Type type : args) {
            this.state.nextLocal += type.getSize();
        }
        this.firstLocal = this.state.nextLocal;
    }

    public LocalVariablesSorter(LocalVariablesSorter lvs) {
        super(262144, lvs.mv);
        this.state = lvs.state;
        this.firstLocal = lvs.firstLocal;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int opcode, int var) {
        int size;
        switch (opcode) {
            case 22:
            case 24:
            case 55:
            case 57:
                size = 2;
                break;
            default:
                size = 1;
                break;
        }
        this.mv.visitVarInsn(opcode, remap(var, size));
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int var, int increment) {
        this.mv.visitIincInsn(remap(var, 1), increment);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMaxs(int maxStack, int maxLocals) {
        this.mv.visitMaxs(maxStack, this.state.nextLocal);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        this.mv.visitLocalVariable(name, desc, signature, start, end, remap(index));
    }

    protected int newLocal(int size) {
        int var = this.state.nextLocal;
        this.state.nextLocal += size;
        return var;
    }

    private int remap(int var, int size) {
        if (var < this.firstLocal) {
            return var;
        }
        int key = ((2 * var) + size) - 1;
        int length = this.state.mapping.length;
        if (key >= length) {
            int[] newMapping = new int[Math.max(2 * length, key + 1)];
            System.arraycopy(this.state.mapping, 0, newMapping, 0, length);
            this.state.mapping = newMapping;
        }
        int value = this.state.mapping[key];
        if (value == 0) {
            value = this.state.nextLocal + 1;
            this.state.mapping[key] = value;
            this.state.nextLocal += size;
        }
        return value - 1;
    }

    private int remap(int var) {
        if (var < this.firstLocal) {
            return var;
        }
        int key = 2 * var;
        int value = key < this.state.mapping.length ? this.state.mapping[key] : 0;
        if (value == 0) {
            value = key + 1 < this.state.mapping.length ? this.state.mapping[key + 1] : 0;
        }
        if (value == 0) {
            throw new IllegalStateException(new StringBuffer().append("Unknown local variable ").append(var).toString());
        }
        return value - 1;
    }
}
