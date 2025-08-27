package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/rmi/RmiInvocationWrapper.class */
class RmiInvocationWrapper implements RmiInvocationHandler {
    private final Object wrappedObject;
    private final RmiBasedExporter rmiExporter;

    public RmiInvocationWrapper(Object wrappedObject, RmiBasedExporter rmiExporter) {
        Assert.notNull(wrappedObject, "Object to wrap is required");
        Assert.notNull(rmiExporter, "RMI exporter is required");
        this.wrappedObject = wrappedObject;
        this.rmiExporter = rmiExporter;
    }

    @Override // org.springframework.remoting.rmi.RmiInvocationHandler
    public String getTargetInterfaceName() {
        Class<?> ifc = this.rmiExporter.getServiceInterface();
        if (ifc != null) {
            return ifc.getName();
        }
        return null;
    }

    @Override // org.springframework.remoting.rmi.RmiInvocationHandler
    public Object invoke(RemoteInvocation invocation) throws IllegalAccessException, NoSuchMethodException, RemoteException, InvocationTargetException {
        return this.rmiExporter.invoke(invocation, this.wrappedObject);
    }
}
