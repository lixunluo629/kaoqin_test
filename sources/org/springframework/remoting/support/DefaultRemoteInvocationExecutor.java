package org.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/support/DefaultRemoteInvocationExecutor.class */
public class DefaultRemoteInvocationExecutor implements RemoteInvocationExecutor {
    @Override // org.springframework.remoting.support.RemoteInvocationExecutor
    public Object invoke(RemoteInvocation invocation, Object targetObject) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Assert.notNull(invocation, "RemoteInvocation must not be null");
        Assert.notNull(targetObject, "Target object must not be null");
        return invocation.invoke(targetObject);
    }
}
