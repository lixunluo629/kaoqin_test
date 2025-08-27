package org.springframework.aop.interceptor;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInvocation;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/interceptor/AbstractMonitoringInterceptor.class */
public abstract class AbstractMonitoringInterceptor extends AbstractTraceInterceptor {
    private String prefix = "";
    private String suffix = "";
    private boolean logTargetClassInvocation = false;

    public void setPrefix(String prefix) {
        this.prefix = prefix != null ? prefix : "";
    }

    protected String getPrefix() {
        return this.prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix != null ? suffix : "";
    }

    protected String getSuffix() {
        return this.suffix;
    }

    public void setLogTargetClassInvocation(boolean logTargetClassInvocation) {
        this.logTargetClassInvocation = logTargetClassInvocation;
    }

    protected String createInvocationTraceName(MethodInvocation invocation) {
        StringBuilder sb = new StringBuilder(getPrefix());
        Method method = invocation.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        if (this.logTargetClassInvocation && clazz.isInstance(invocation.getThis())) {
            clazz = invocation.getThis().getClass();
        }
        sb.append(clazz.getName());
        sb.append('.').append(method.getName());
        sb.append(getSuffix());
        return sb.toString();
    }
}
