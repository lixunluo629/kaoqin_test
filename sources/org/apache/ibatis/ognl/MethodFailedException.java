package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/MethodFailedException.class */
public class MethodFailedException extends OgnlException {
    public MethodFailedException(Object source, String name) {
        super("Method \"" + name + "\" failed for object " + source);
    }

    public MethodFailedException(Object source, String name, Throwable reason) {
        super("Method \"" + name + "\" failed for object " + source, reason);
    }
}
