package org.apache.ibatis.javassist.tools.reflect;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/tools/reflect/CannotCreateException.class */
public class CannotCreateException extends Exception {
    public CannotCreateException(String s) {
        super(s);
    }

    public CannotCreateException(Exception e) {
        super("by " + e.toString());
    }
}
