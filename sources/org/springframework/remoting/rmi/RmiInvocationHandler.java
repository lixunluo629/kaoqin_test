package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.springframework.remoting.support.RemoteInvocation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/rmi/RmiInvocationHandler.class */
public interface RmiInvocationHandler extends Remote {
    String getTargetInterfaceName() throws RemoteException;

    Object invoke(RemoteInvocation remoteInvocation) throws IllegalAccessException, NoSuchMethodException, RemoteException, InvocationTargetException;
}
