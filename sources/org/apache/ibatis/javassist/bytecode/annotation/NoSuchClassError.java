package org.apache.ibatis.javassist.bytecode.annotation;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/annotation/NoSuchClassError.class */
public class NoSuchClassError extends Error {
    private String className;

    public NoSuchClassError(String className, Error cause) {
        super(cause.toString(), cause);
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }
}
