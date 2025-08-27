package org.apache.ibatis.javassist.tools.rmi;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/tools/rmi/ObjectNotFoundException.class */
public class ObjectNotFoundException extends Exception {
    public ObjectNotFoundException(String name) {
        super(name + " is not exported");
    }

    public ObjectNotFoundException(String name, Exception e) {
        super(name + " because of " + e.toString());
    }
}
