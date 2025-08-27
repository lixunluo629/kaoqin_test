package org.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/support/DefaultRemoteInvocationFactory.class */
public class DefaultRemoteInvocationFactory implements RemoteInvocationFactory {
    @Override // org.springframework.remoting.support.RemoteInvocationFactory
    public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
        return new RemoteInvocation(methodInvocation);
    }
}
