package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationBasedExporter;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/rmi/RmiBasedExporter.class */
public abstract class RmiBasedExporter extends RemoteInvocationBasedExporter {
    protected Remote getObjectToExport() {
        if ((getService() instanceof Remote) && (getServiceInterface() == null || Remote.class.isAssignableFrom(getServiceInterface()))) {
            return (Remote) getService();
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("RMI service [" + getService() + "] is an RMI invoker");
        }
        return new RmiInvocationWrapper(getProxyForService(), this);
    }

    @Override // org.springframework.remoting.support.RemoteInvocationBasedExporter
    protected Object invoke(RemoteInvocation invocation, Object targetObject) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return super.invoke(invocation, targetObject);
    }
}
