package org.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/support/RemoteInvocationFactory.class */
public interface RemoteInvocationFactory {
    RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation);
}
