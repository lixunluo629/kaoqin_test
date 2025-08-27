package org.springframework.remoting.httpinvoker;

import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/httpinvoker/HttpInvokerRequestExecutor.class */
public interface HttpInvokerRequestExecutor {
    RemoteInvocationResult executeRequest(HttpInvokerClientConfiguration httpInvokerClientConfiguration, RemoteInvocation remoteInvocation) throws Exception;
}
