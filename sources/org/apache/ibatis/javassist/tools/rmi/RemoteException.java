package org.apache.ibatis.javassist.tools.rmi;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/tools/rmi/RemoteException.class */
public class RemoteException extends RuntimeException {
    public RemoteException(String msg) {
        super(msg);
    }

    public RemoteException(Exception e) {
        super("by " + e.toString());
    }
}
