package org.springframework.asm;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/asm/CurrentFrame.class */
class CurrentFrame extends Frame {
    CurrentFrame() {
    }

    @Override // org.springframework.asm.Frame
    void execute(int opcode, int arg, ClassWriter cw, Item item) {
        super.execute(opcode, arg, cw, item);
        Frame successor = new Frame();
        merge(cw, successor, 0);
        set(successor);
        this.owner.inputStackTop = 0;
    }
}
