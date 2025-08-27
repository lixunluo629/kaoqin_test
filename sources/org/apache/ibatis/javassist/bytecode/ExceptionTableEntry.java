package org.apache.ibatis.javassist.bytecode;

/* compiled from: ExceptionTable.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/ExceptionTableEntry.class */
class ExceptionTableEntry {
    int startPc;
    int endPc;
    int handlerPc;
    int catchType;

    ExceptionTableEntry(int start, int end, int handle, int type) {
        this.startPc = start;
        this.endPc = end;
        this.handlerPc = handle;
        this.catchType = type;
    }
}
