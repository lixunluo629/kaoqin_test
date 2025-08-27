package org.apache.ibatis.javassist.tools.rmi;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/tools/rmi/Sample.class */
public class Sample {
    private ObjectImporter importer;
    private int objectId;

    public Object forward(Object[] args, int identifier) {
        return this.importer.call(this.objectId, identifier, args);
    }

    public static Object forwardStatic(Object[] args, int identifier) throws RemoteException {
        throw new RemoteException("cannot call a static method.");
    }
}
