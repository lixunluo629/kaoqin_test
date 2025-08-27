package org.apache.ibatis.javassist;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/NotFoundException.class */
public class NotFoundException extends Exception {
    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String msg, Exception e) {
        super(msg + " because of " + e.toString());
    }
}
