package org.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/support/RemoteInvocationExecutor.class */
public interface RemoteInvocationExecutor {
    Object invoke(RemoteInvocation remoteInvocation, Object obj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}
