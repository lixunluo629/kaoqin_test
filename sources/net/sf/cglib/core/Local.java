package net.sf.cglib.core;

import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/Local.class */
public class Local {
    private Type type;
    private int index;

    public Local(int index, Type type) {
        this.type = type;
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public Type getType() {
        return this.type;
    }
}
