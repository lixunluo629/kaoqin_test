package org.springframework.aop.framework;

import org.aopalliance.intercept.Interceptor;
import org.springframework.aop.TargetSource;
import org.springframework.util.ClassUtils;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/ProxyFactory.class */
public class ProxyFactory extends ProxyCreatorSupport {
    public ProxyFactory() {
    }

    public ProxyFactory(Object target) {
        setTarget(target);
        setInterfaces(ClassUtils.getAllInterfaces(target));
    }

    public ProxyFactory(Class<?>... proxyInterfaces) {
        setInterfaces(proxyInterfaces);
    }

    public ProxyFactory(Class<?> proxyInterface, Interceptor interceptor) {
        addInterface(proxyInterface);
        addAdvice(interceptor);
    }

    public ProxyFactory(Class<?> proxyInterface, TargetSource targetSource) {
        addInterface(proxyInterface);
        setTargetSource(targetSource);
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    public Object getProxy(ClassLoader classLoader) {
        return createAopProxy().getProxy(classLoader);
    }

    public static <T> T getProxy(Class<T> cls, Interceptor interceptor) {
        return (T) new ProxyFactory((Class<?>) cls, interceptor).getProxy();
    }

    public static <T> T getProxy(Class<T> cls, TargetSource targetSource) {
        return (T) new ProxyFactory((Class<?>) cls, targetSource).getProxy();
    }

    public static Object getProxy(TargetSource targetSource) {
        if (targetSource.getTargetClass() == null) {
            throw new IllegalArgumentException("Cannot create class proxy for TargetSource with null target class");
        }
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTargetSource(targetSource);
        proxyFactory.setProxyTargetClass(true);
        return proxyFactory.getProxy();
    }
}
