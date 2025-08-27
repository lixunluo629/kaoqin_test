package org.springframework.asm;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/asm/Context.class */
class Context {
    Attribute[] attrs;
    int flags;
    char[] buffer;
    int[] bootstrapMethods;
    int access;
    String name;
    String desc;
    Label[] labels;
    int typeRef;
    TypePath typePath;
    int offset;
    Label[] start;
    Label[] end;
    int[] index;
    int mode;
    int localCount;
    int localDiff;
    Object[] local;
    int stackCount;
    Object[] stack;

    Context() {
    }
}
