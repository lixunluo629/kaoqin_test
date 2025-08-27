package org.springframework.aop;

import org.aopalliance.intercept.MethodInvocation;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/ProxyMethodInvocation.class */
public interface ProxyMethodInvocation extends MethodInvocation {
    Object getProxy();

    MethodInvocation invocableClone();

    MethodInvocation invocableClone(Object... objArr);

    void setArguments(Object... objArr);

    void setUserAttribute(String str, Object obj);

    Object getUserAttribute(String str);
}
