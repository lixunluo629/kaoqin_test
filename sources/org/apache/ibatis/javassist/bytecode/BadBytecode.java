package org.apache.ibatis.javassist.bytecode;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/BadBytecode.class */
public class BadBytecode extends Exception {
    public BadBytecode(int opcode) {
        super("bytecode " + opcode);
    }

    public BadBytecode(String msg) {
        super(msg);
    }

    public BadBytecode(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadBytecode(MethodInfo minfo, Throwable cause) {
        super(minfo.toString() + " in " + minfo.getConstPool().getClassName() + ": " + cause.getMessage(), cause);
    }
}
