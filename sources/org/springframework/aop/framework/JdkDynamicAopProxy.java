package org.springframework.aop.framework;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AopInvocationException;
import org.springframework.aop.RawTargetAccess;
import org.springframework.aop.TargetSource;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.DecoratingProxy;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/JdkDynamicAopProxy.class */
final class JdkDynamicAopProxy implements AopProxy, InvocationHandler, Serializable {
    private static final long serialVersionUID = 5531744639992436476L;
    private static final Log logger = LogFactory.getLog(JdkDynamicAopProxy.class);
    private final AdvisedSupport advised;
    private boolean equalsDefined;
    private boolean hashCodeDefined;

    public JdkDynamicAopProxy(AdvisedSupport config) throws AopConfigException {
        Assert.notNull(config, "AdvisedSupport must not be null");
        if (config.getAdvisors().length == 0 && config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE) {
            throw new AopConfigException("No advisors and no TargetSource specified");
        }
        this.advised = config;
    }

    @Override // org.springframework.aop.framework.AopProxy
    public Object getProxy() {
        return getProxy(ClassUtils.getDefaultClassLoader());
    }

    @Override // org.springframework.aop.framework.AopProxy
    public Object getProxy(ClassLoader classLoader) throws SecurityException {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating JDK dynamic proxy: target source is " + this.advised.getTargetSource());
        }
        Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised, true);
        findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
        return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
    }

    private void findDefinedEqualsAndHashCodeMethods(Class<?>[] proxiedInterfaces) throws SecurityException {
        for (Class<?> proxiedInterface : proxiedInterfaces) {
            Method[] methods = proxiedInterface.getDeclaredMethods();
            for (Method method : methods) {
                if (AopUtils.isEqualsMethod(method)) {
                    this.equalsDefined = true;
                }
                if (AopUtils.isHashCodeMethod(method)) {
                    this.hashCodeDefined = true;
                }
                if (this.equalsDefined && this.hashCodeDefined) {
                    return;
                }
            }
        }
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        Object retVal;
        Object oldProxy = null;
        boolean setProxyContext = false;
        TargetSource targetSource = this.advised.targetSource;
        Class<?> targetClass = null;
        try {
            if (!this.equalsDefined && AopUtils.isEqualsMethod(method)) {
                Boolean boolValueOf = Boolean.valueOf(equals(args[0]));
                if (0 != 0 && !targetSource.isStatic()) {
                    targetSource.releaseTarget(null);
                }
                if (0 != 0) {
                    AopContext.setCurrentProxy(null);
                }
                return boolValueOf;
            }
            if (!this.hashCodeDefined && AopUtils.isHashCodeMethod(method)) {
                Integer numValueOf = Integer.valueOf(hashCode());
                if (0 != 0 && !targetSource.isStatic()) {
                    targetSource.releaseTarget(null);
                }
                if (0 != 0) {
                    AopContext.setCurrentProxy(null);
                }
                return numValueOf;
            }
            if (method.getDeclaringClass() == DecoratingProxy.class) {
                Class<?> clsUltimateTargetClass = AopProxyUtils.ultimateTargetClass(this.advised);
                if (0 != 0 && !targetSource.isStatic()) {
                    targetSource.releaseTarget(null);
                }
                if (0 != 0) {
                    AopContext.setCurrentProxy(null);
                }
                return clsUltimateTargetClass;
            }
            if (!this.advised.opaque && method.getDeclaringClass().isInterface() && method.getDeclaringClass().isAssignableFrom(Advised.class)) {
                Object objInvokeJoinpointUsingReflection = AopUtils.invokeJoinpointUsingReflection(this.advised, method, args);
                if (0 != 0 && !targetSource.isStatic()) {
                    targetSource.releaseTarget(null);
                }
                if (0 != 0) {
                    AopContext.setCurrentProxy(null);
                }
                return objInvokeJoinpointUsingReflection;
            }
            if (this.advised.exposeProxy) {
                oldProxy = AopContext.setCurrentProxy(proxy);
                setProxyContext = true;
            }
            Object target = targetSource.getTarget();
            if (target != null) {
                targetClass = target.getClass();
            }
            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
            if (chain.isEmpty()) {
                Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
                retVal = AopUtils.invokeJoinpointUsingReflection(target, method, argsToUse);
            } else {
                MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
                retVal = invocation.proceed();
            }
            Class<?> returnType = method.getReturnType();
            if (retVal != null && retVal == target && returnType != Object.class && returnType.isInstance(proxy) && !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {
                retVal = proxy;
            } else if (retVal == null && returnType != Void.TYPE && returnType.isPrimitive()) {
                throw new AopInvocationException("Null return value from advice does not match primitive return type for: " + method);
            }
            Object obj = retVal;
            if (target != null && !targetSource.isStatic()) {
                targetSource.releaseTarget(target);
            }
            if (setProxyContext) {
                AopContext.setCurrentProxy(oldProxy);
            }
            return obj;
        } catch (Throwable th) {
            if (0 != 0 && !targetSource.isStatic()) {
                targetSource.releaseTarget(null);
            }
            if (0 != 0) {
                AopContext.setCurrentProxy(null);
            }
            throw th;
        }
    }

    public boolean equals(Object other) throws IllegalArgumentException {
        JdkDynamicAopProxy otherProxy;
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof JdkDynamicAopProxy) {
            otherProxy = (JdkDynamicAopProxy) other;
        } else if (Proxy.isProxyClass(other.getClass())) {
            InvocationHandler ih = Proxy.getInvocationHandler(other);
            if (!(ih instanceof JdkDynamicAopProxy)) {
                return false;
            }
            otherProxy = (JdkDynamicAopProxy) ih;
        } else {
            return false;
        }
        return AopProxyUtils.equalsInProxy(this.advised, otherProxy.advised);
    }

    public int hashCode() {
        return (JdkDynamicAopProxy.class.hashCode() * 13) + this.advised.getTargetSource().hashCode();
    }
}
