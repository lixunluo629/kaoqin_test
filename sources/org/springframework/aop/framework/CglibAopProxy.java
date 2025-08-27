package org.springframework.aop.framework;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.AopInvocationException;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.RawTargetAccess;
import org.springframework.aop.TargetSource;
import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.core.ClassGenerator;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Dispatcher;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.Factory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.cglib.transform.impl.UndeclaredThrowableStrategy;
import org.springframework.core.SmartClassLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy.class */
class CglibAopProxy implements AopProxy, Serializable {
    private static final int AOP_PROXY = 0;
    private static final int INVOKE_TARGET = 1;
    private static final int NO_OVERRIDE = 2;
    private static final int DISPATCH_TARGET = 3;
    private static final int DISPATCH_ADVISED = 4;
    private static final int INVOKE_EQUALS = 5;
    private static final int INVOKE_HASHCODE = 6;
    protected static final Log logger = LogFactory.getLog(CglibAopProxy.class);
    private static final Map<Class<?>, Boolean> validatedClasses = new WeakHashMap();
    protected final AdvisedSupport advised;
    protected Object[] constructorArgs;
    protected Class<?>[] constructorArgTypes;
    private final transient AdvisedDispatcher advisedDispatcher;
    private transient Map<String, Integer> fixedInterceptorMap;
    private transient int fixedInterceptorOffset;

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$SerializableNoOp.class */
    public static class SerializableNoOp implements NoOp, Serializable {
    }

    public CglibAopProxy(AdvisedSupport config) throws AopConfigException {
        Assert.notNull(config, "AdvisedSupport must not be null");
        if (config.getAdvisors().length == 0 && config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE) {
            throw new AopConfigException("No advisors and no TargetSource specified");
        }
        this.advised = config;
        this.advisedDispatcher = new AdvisedDispatcher(this.advised);
    }

    public void setConstructorArguments(Object[] constructorArgs, Class<?>[] constructorArgTypes) {
        if (constructorArgs == null || constructorArgTypes == null) {
            throw new IllegalArgumentException("Both 'constructorArgs' and 'constructorArgTypes' need to be specified");
        }
        if (constructorArgs.length != constructorArgTypes.length) {
            throw new IllegalArgumentException("Number of 'constructorArgs' (" + constructorArgs.length + ") must match number of 'constructorArgTypes' (" + constructorArgTypes.length + ")");
        }
        this.constructorArgs = constructorArgs;
        this.constructorArgTypes = constructorArgTypes;
    }

    @Override // org.springframework.aop.framework.AopProxy
    public Object getProxy() {
        return getProxy(null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.aop.framework.AopProxy
    public Object getProxy(ClassLoader classLoader) {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating CGLIB proxy: target source is " + this.advised.getTargetSource());
        }
        try {
            Class<?> rootClass = this.advised.getTargetClass();
            Assert.state(rootClass != null, "Target class must be available for creating a CGLIB proxy");
            Class<?> proxySuperClass = rootClass;
            if (ClassUtils.isCglibProxyClass(rootClass)) {
                proxySuperClass = rootClass.getSuperclass();
                Class<?>[] additionalInterfaces = rootClass.getInterfaces();
                for (Class<?> additionalInterface : additionalInterfaces) {
                    this.advised.addInterface(additionalInterface);
                }
            }
            validateClassIfNecessary(proxySuperClass, classLoader);
            Enhancer enhancer = createEnhancer();
            if (classLoader != 0) {
                enhancer.setClassLoader(classLoader);
                if ((classLoader instanceof SmartClassLoader) && ((SmartClassLoader) classLoader).isClassReloadable(proxySuperClass)) {
                    enhancer.setUseCache(false);
                }
            }
            enhancer.setSuperclass(proxySuperClass);
            enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));
            enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
            enhancer.setStrategy(new ClassLoaderAwareUndeclaredThrowableStrategy(classLoader));
            Callback[] callbacks = getCallbacks(rootClass);
            Class<?>[] types = new Class[callbacks.length];
            for (int x = 0; x < types.length; x++) {
                types[x] = callbacks[x].getClass();
            }
            enhancer.setCallbackFilter(new ProxyCallbackFilter(this.advised.getConfigurationOnlyCopy(), this.fixedInterceptorMap, this.fixedInterceptorOffset));
            enhancer.setCallbackTypes(types);
            return createProxyClassAndInstance(enhancer, callbacks);
        } catch (IllegalArgumentException ex) {
            throw new AopConfigException("Could not generate CGLIB subclass of " + this.advised.getTargetClass() + ": Common causes of this problem include using a final class or a non-visible class", ex);
        } catch (CodeGenerationException ex2) {
            throw new AopConfigException("Could not generate CGLIB subclass of " + this.advised.getTargetClass() + ": Common causes of this problem include using a final class or a non-visible class", ex2);
        } catch (Throwable ex3) {
            throw new AopConfigException("Unexpected AOP exception", ex3);
        }
    }

    protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
        enhancer.setInterceptDuringConstruction(false);
        enhancer.setCallbacks(callbacks);
        if (this.constructorArgs != null) {
            return enhancer.create(this.constructorArgTypes, this.constructorArgs);
        }
        return enhancer.create();
    }

    protected Enhancer createEnhancer() {
        return new Enhancer();
    }

    private void validateClassIfNecessary(Class<?> proxySuperClass, ClassLoader proxyClassLoader) {
        if (logger.isWarnEnabled()) {
            synchronized (validatedClasses) {
                if (!validatedClasses.containsKey(proxySuperClass)) {
                    doValidateClass(proxySuperClass, proxyClassLoader, ClassUtils.getAllInterfacesForClassAsSet(proxySuperClass));
                    validatedClasses.put(proxySuperClass, Boolean.TRUE);
                }
            }
        }
    }

    private void doValidateClass(Class<?> proxySuperClass, ClassLoader proxyClassLoader, Set<Class<?>> ifcs) throws SecurityException {
        if (proxySuperClass != Object.class) {
            Method[] methods = proxySuperClass.getDeclaredMethods();
            for (Method method : methods) {
                int mod = method.getModifiers();
                if (!Modifier.isStatic(mod) && !Modifier.isPrivate(mod)) {
                    if (Modifier.isFinal(mod)) {
                        if (implementsInterface(method, ifcs)) {
                            logger.warn("Unable to proxy interface-implementing method [" + method + "] because it is marked as final: Consider using interface-based JDK proxies instead!");
                        }
                        logger.info("Final method [" + method + "] cannot get proxied via CGLIB: Calls to this method will NOT be routed to the target instance and might lead to NPEs against uninitialized fields in the proxy instance.");
                    } else if (!Modifier.isPublic(mod) && !Modifier.isProtected(mod) && proxyClassLoader != null && proxySuperClass.getClassLoader() != proxyClassLoader) {
                        logger.info("Method [" + method + "] is package-visible across different ClassLoaders and cannot get proxied via CGLIB: Declare this method as public or protected if you need to support invocations through the proxy.");
                    }
                }
            }
            doValidateClass(proxySuperClass.getSuperclass(), proxyClassLoader, ifcs);
        }
    }

    private Callback[] getCallbacks(Class<?> rootClass) throws Exception {
        Callback dynamicUnadvisedInterceptor;
        Callback targetInterceptor;
        Callback[] callbacks;
        Callback dynamicUnadvisedExposedInterceptor;
        boolean exposeProxy = this.advised.isExposeProxy();
        boolean isFrozen = this.advised.isFrozen();
        boolean isStatic = this.advised.getTargetSource().isStatic();
        Callback aopInterceptor = new DynamicAdvisedInterceptor(this.advised);
        if (exposeProxy) {
            if (isStatic) {
                dynamicUnadvisedExposedInterceptor = new StaticUnadvisedExposedInterceptor(this.advised.getTargetSource().getTarget());
            } else {
                dynamicUnadvisedExposedInterceptor = new DynamicUnadvisedExposedInterceptor(this.advised.getTargetSource());
            }
            targetInterceptor = dynamicUnadvisedExposedInterceptor;
        } else {
            if (isStatic) {
                dynamicUnadvisedInterceptor = new StaticUnadvisedInterceptor(this.advised.getTargetSource().getTarget());
            } else {
                dynamicUnadvisedInterceptor = new DynamicUnadvisedInterceptor(this.advised.getTargetSource());
            }
            targetInterceptor = dynamicUnadvisedInterceptor;
        }
        Callback targetDispatcher = isStatic ? new StaticDispatcher(this.advised.getTargetSource().getTarget()) : new SerializableNoOp();
        Callback[] mainCallbacks = {aopInterceptor, targetInterceptor, new SerializableNoOp(), targetDispatcher, this.advisedDispatcher, new EqualsInterceptor(this.advised), new HashCodeInterceptor(this.advised)};
        if (isStatic && isFrozen) {
            Method[] methods = rootClass.getMethods();
            Callback[] fixedCallbacks = new Callback[methods.length];
            this.fixedInterceptorMap = new HashMap(methods.length);
            for (int x = 0; x < methods.length; x++) {
                List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(methods[x], rootClass);
                fixedCallbacks[x] = new FixedChainStaticTargetInterceptor(chain, this.advised.getTargetSource().getTarget(), this.advised.getTargetClass());
                this.fixedInterceptorMap.put(methods[x].toString(), Integer.valueOf(x));
            }
            callbacks = new Callback[mainCallbacks.length + fixedCallbacks.length];
            System.arraycopy(mainCallbacks, 0, callbacks, 0, mainCallbacks.length);
            System.arraycopy(fixedCallbacks, 0, callbacks, mainCallbacks.length, fixedCallbacks.length);
            this.fixedInterceptorOffset = mainCallbacks.length;
        } else {
            callbacks = mainCallbacks;
        }
        return callbacks;
    }

    public boolean equals(Object other) {
        return this == other || ((other instanceof CglibAopProxy) && AopProxyUtils.equalsInProxy(this.advised, ((CglibAopProxy) other).advised));
    }

    public int hashCode() {
        return (CglibAopProxy.class.hashCode() * 13) + this.advised.getTargetSource().hashCode();
    }

    private static boolean implementsInterface(Method method, Set<Class<?>> ifcs) {
        for (Class<?> ifc : ifcs) {
            if (ClassUtils.hasMethod(ifc, method.getName(), method.getParameterTypes())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object processReturnType(Object proxy, Object target, Method method, Object retVal) {
        if (retVal != null && retVal == target && !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {
            retVal = proxy;
        }
        Class<?> returnType = method.getReturnType();
        if (retVal == null && returnType != Void.TYPE && returnType.isPrimitive()) {
            throw new AopInvocationException("Null return value from advice does not match primitive return type for: " + method);
        }
        return retVal;
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$StaticUnadvisedInterceptor.class */
    private static class StaticUnadvisedInterceptor implements MethodInterceptor, Serializable {
        private final Object target;

        public StaticUnadvisedInterceptor(Object target) {
            this.target = target;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object retVal = methodProxy.invoke(this.target, args);
            return CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$StaticUnadvisedExposedInterceptor.class */
    private static class StaticUnadvisedExposedInterceptor implements MethodInterceptor, Serializable {
        private final Object target;

        public StaticUnadvisedExposedInterceptor(Object target) {
            this.target = target;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object oldProxy = null;
            try {
                oldProxy = AopContext.setCurrentProxy(proxy);
                Object retVal = methodProxy.invoke(this.target, args);
                Object objProcessReturnType = CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
                AopContext.setCurrentProxy(oldProxy);
                return objProcessReturnType;
            } catch (Throwable th) {
                AopContext.setCurrentProxy(oldProxy);
                throw th;
            }
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$DynamicUnadvisedInterceptor.class */
    private static class DynamicUnadvisedInterceptor implements MethodInterceptor, Serializable {
        private final TargetSource targetSource;

        public DynamicUnadvisedInterceptor(TargetSource targetSource) {
            this.targetSource = targetSource;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Exception {
            Object target = this.targetSource.getTarget();
            try {
                Object retVal = methodProxy.invoke(target, args);
                Object objProcessReturnType = CglibAopProxy.processReturnType(proxy, target, method, retVal);
                this.targetSource.releaseTarget(target);
                return objProcessReturnType;
            } catch (Throwable th) {
                this.targetSource.releaseTarget(target);
                throw th;
            }
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$DynamicUnadvisedExposedInterceptor.class */
    private static class DynamicUnadvisedExposedInterceptor implements MethodInterceptor, Serializable {
        private final TargetSource targetSource;

        public DynamicUnadvisedExposedInterceptor(TargetSource targetSource) {
            this.targetSource = targetSource;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Exception {
            Object oldProxy = null;
            Object target = this.targetSource.getTarget();
            try {
                oldProxy = AopContext.setCurrentProxy(proxy);
                Object retVal = methodProxy.invoke(target, args);
                Object objProcessReturnType = CglibAopProxy.processReturnType(proxy, target, method, retVal);
                AopContext.setCurrentProxy(oldProxy);
                this.targetSource.releaseTarget(target);
                return objProcessReturnType;
            } catch (Throwable th) {
                AopContext.setCurrentProxy(oldProxy);
                this.targetSource.releaseTarget(target);
                throw th;
            }
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$StaticDispatcher.class */
    private static class StaticDispatcher implements Dispatcher, Serializable {
        private Object target;

        public StaticDispatcher(Object target) {
            this.target = target;
        }

        @Override // org.springframework.cglib.proxy.Dispatcher
        public Object loadObject() {
            return this.target;
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$AdvisedDispatcher.class */
    private static class AdvisedDispatcher implements Dispatcher, Serializable {
        private final AdvisedSupport advised;

        public AdvisedDispatcher(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override // org.springframework.cglib.proxy.Dispatcher
        public Object loadObject() throws Exception {
            return this.advised;
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$EqualsInterceptor.class */
    private static class EqualsInterceptor implements MethodInterceptor, Serializable {
        private final AdvisedSupport advised;

        public EqualsInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) {
            Object other = args[0];
            if (proxy == other) {
                return true;
            }
            if (other instanceof Factory) {
                Callback callback = ((Factory) other).getCallback(5);
                if (!(callback instanceof EqualsInterceptor)) {
                    return false;
                }
                AdvisedSupport otherAdvised = ((EqualsInterceptor) callback).advised;
                return Boolean.valueOf(AopProxyUtils.equalsInProxy(this.advised, otherAdvised));
            }
            return false;
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$HashCodeInterceptor.class */
    private static class HashCodeInterceptor implements MethodInterceptor, Serializable {
        private final AdvisedSupport advised;

        public HashCodeInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) {
            return Integer.valueOf((CglibAopProxy.class.hashCode() * 13) + this.advised.getTargetSource().hashCode());
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$FixedChainStaticTargetInterceptor.class */
    private static class FixedChainStaticTargetInterceptor implements MethodInterceptor, Serializable {
        private final List<Object> adviceChain;
        private final Object target;
        private final Class<?> targetClass;

        public FixedChainStaticTargetInterceptor(List<Object> adviceChain, Object target, Class<?> targetClass) {
            this.adviceChain = adviceChain;
            this.target = target;
            this.targetClass = targetClass;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            MethodInvocation invocation = new CglibMethodInvocation(proxy, this.target, method, args, this.targetClass, this.adviceChain, methodProxy);
            Object retVal = invocation.proceed();
            return CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$DynamicAdvisedInterceptor.class */
    private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {
        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Exception {
            Object retVal;
            Object oldProxy = null;
            boolean setProxyContext = false;
            Class<?> targetClass = null;
            Object target = null;
            try {
                if (this.advised.exposeProxy) {
                    oldProxy = AopContext.setCurrentProxy(proxy);
                    setProxyContext = true;
                }
                target = getTarget();
                if (target != null) {
                    targetClass = target.getClass();
                }
                List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
                if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
                    Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
                    retVal = methodProxy.invoke(target, argsToUse);
                } else {
                    retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
                }
                Object retVal2 = CglibAopProxy.processReturnType(proxy, target, method, retVal);
                if (target != null) {
                    releaseTarget(target);
                }
                if (setProxyContext) {
                    AopContext.setCurrentProxy(oldProxy);
                }
                return retVal2;
            } catch (Throwable th) {
                if (target != null) {
                    releaseTarget(target);
                }
                if (setProxyContext) {
                    AopContext.setCurrentProxy(oldProxy);
                }
                throw th;
            }
        }

        public boolean equals(Object other) {
            return this == other || ((other instanceof DynamicAdvisedInterceptor) && this.advised.equals(((DynamicAdvisedInterceptor) other).advised));
        }

        public int hashCode() {
            return this.advised.hashCode();
        }

        protected Object getTarget() throws Exception {
            return this.advised.getTargetSource().getTarget();
        }

        protected void releaseTarget(Object target) throws Exception {
            this.advised.getTargetSource().releaseTarget(target);
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$CglibMethodInvocation.class */
    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {
        private final MethodProxy methodProxy;
        private final boolean publicMethod;

        public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers, MethodProxy methodProxy) {
            super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
            this.methodProxy = methodProxy;
            this.publicMethod = Modifier.isPublic(method.getModifiers());
        }

        @Override // org.springframework.aop.framework.ReflectiveMethodInvocation
        protected Object invokeJoinpoint() throws Throwable {
            if (this.publicMethod) {
                return this.methodProxy.invoke(this.target, this.arguments);
            }
            return super.invokeJoinpoint();
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$ProxyCallbackFilter.class */
    private static class ProxyCallbackFilter implements CallbackFilter {
        private final AdvisedSupport advised;
        private final Map<String, Integer> fixedInterceptorMap;
        private final int fixedInterceptorOffset;

        public ProxyCallbackFilter(AdvisedSupport advised, Map<String, Integer> fixedInterceptorMap, int fixedInterceptorOffset) {
            this.advised = advised;
            this.fixedInterceptorMap = fixedInterceptorMap;
            this.fixedInterceptorOffset = fixedInterceptorOffset;
        }

        @Override // org.springframework.cglib.proxy.CallbackFilter
        public int accept(Method method) {
            if (AopUtils.isFinalizeMethod(method)) {
                CglibAopProxy.logger.debug("Found finalize() method - using NO_OVERRIDE");
                return 2;
            }
            if (!this.advised.isOpaque() && method.getDeclaringClass().isInterface() && method.getDeclaringClass().isAssignableFrom(Advised.class)) {
                if (CglibAopProxy.logger.isDebugEnabled()) {
                    CglibAopProxy.logger.debug("Method is declared on Advised interface: " + method);
                    return 4;
                }
                return 4;
            }
            if (AopUtils.isEqualsMethod(method)) {
                if (CglibAopProxy.logger.isDebugEnabled()) {
                    CglibAopProxy.logger.debug("Found 'equals' method: " + method);
                    return 5;
                }
                return 5;
            }
            if (AopUtils.isHashCodeMethod(method)) {
                if (CglibAopProxy.logger.isDebugEnabled()) {
                    CglibAopProxy.logger.debug("Found 'hashCode' method: " + method);
                    return 6;
                }
                return 6;
            }
            Class<?> targetClass = this.advised.getTargetClass();
            List<?> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
            boolean haveAdvice = !chain.isEmpty();
            boolean exposeProxy = this.advised.isExposeProxy();
            boolean isStatic = this.advised.getTargetSource().isStatic();
            boolean isFrozen = this.advised.isFrozen();
            if (haveAdvice || !isFrozen) {
                if (exposeProxy) {
                    if (CglibAopProxy.logger.isDebugEnabled()) {
                        CglibAopProxy.logger.debug("Must expose proxy on advised method: " + method);
                        return 0;
                    }
                    return 0;
                }
                String key = method.toString();
                if (isStatic && isFrozen && this.fixedInterceptorMap.containsKey(key)) {
                    if (CglibAopProxy.logger.isDebugEnabled()) {
                        CglibAopProxy.logger.debug("Method has advice and optimizations are enabled: " + method);
                    }
                    int index = this.fixedInterceptorMap.get(key).intValue();
                    return index + this.fixedInterceptorOffset;
                }
                if (CglibAopProxy.logger.isDebugEnabled()) {
                    CglibAopProxy.logger.debug("Unable to apply any optimizations to advised method: " + method);
                    return 0;
                }
                return 0;
            }
            if (exposeProxy || !isStatic) {
                return 1;
            }
            Class<?> returnType = method.getReturnType();
            if (returnType.isAssignableFrom(targetClass)) {
                if (CglibAopProxy.logger.isDebugEnabled()) {
                    CglibAopProxy.logger.debug("Method return type is assignable from target type and may therefore return 'this' - using INVOKE_TARGET: " + method);
                    return 1;
                }
                return 1;
            }
            if (CglibAopProxy.logger.isDebugEnabled()) {
                CglibAopProxy.logger.debug("Method return type ensures 'this' cannot be returned - using DISPATCH_TARGET: " + method);
                return 3;
            }
            return 3;
        }

        @Override // org.springframework.cglib.proxy.CallbackFilter
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ProxyCallbackFilter)) {
                return false;
            }
            ProxyCallbackFilter otherCallbackFilter = (ProxyCallbackFilter) other;
            AdvisedSupport otherAdvised = otherCallbackFilter.advised;
            if (this.advised == null || otherAdvised == null || this.advised.isFrozen() != otherAdvised.isFrozen() || this.advised.isExposeProxy() != otherAdvised.isExposeProxy() || this.advised.getTargetSource().isStatic() != otherAdvised.getTargetSource().isStatic() || !AopProxyUtils.equalsProxiedInterfaces(this.advised, otherAdvised)) {
                return false;
            }
            Advisor[] thisAdvisors = this.advised.getAdvisors();
            Advisor[] thatAdvisors = otherAdvised.getAdvisors();
            if (thisAdvisors.length != thatAdvisors.length) {
                return false;
            }
            for (int i = 0; i < thisAdvisors.length; i++) {
                Advisor thisAdvisor = thisAdvisors[i];
                Advisor thatAdvisor = thatAdvisors[i];
                if (!equalsAdviceClasses(thisAdvisor, thatAdvisor) || !equalsPointcuts(thisAdvisor, thatAdvisor)) {
                    return false;
                }
            }
            return true;
        }

        private boolean equalsAdviceClasses(Advisor a, Advisor b) {
            Advice aa = a.getAdvice();
            Advice ba = b.getAdvice();
            return (aa == null || ba == null) ? aa == ba : aa.getClass() == ba.getClass();
        }

        private boolean equalsPointcuts(Advisor a, Advisor b) {
            return !(a instanceof PointcutAdvisor) || ((b instanceof PointcutAdvisor) && ObjectUtils.nullSafeEquals(((PointcutAdvisor) a).getPointcut(), ((PointcutAdvisor) b).getPointcut()));
        }

        public int hashCode() {
            int hashCode = 0;
            Advisor[] advisors = this.advised.getAdvisors();
            for (Advisor advisor : advisors) {
                Advice advice = advisor.getAdvice();
                if (advice != null) {
                    hashCode = (13 * hashCode) + advice.getClass().hashCode();
                }
            }
            return (13 * ((13 * ((13 * ((13 * hashCode) + (this.advised.isFrozen() ? 1 : 0))) + (this.advised.isExposeProxy() ? 1 : 0))) + (this.advised.isOptimize() ? 1 : 0))) + (this.advised.isOpaque() ? 1 : 0);
        }
    }

    /* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/CglibAopProxy$ClassLoaderAwareUndeclaredThrowableStrategy.class */
    private static class ClassLoaderAwareUndeclaredThrowableStrategy extends UndeclaredThrowableStrategy {
        private final ClassLoader classLoader;

        public ClassLoaderAwareUndeclaredThrowableStrategy(ClassLoader classLoader) {
            super(UndeclaredThrowableException.class);
            this.classLoader = classLoader;
        }

        @Override // org.springframework.cglib.core.DefaultGeneratorStrategy, org.springframework.cglib.core.GeneratorStrategy
        public byte[] generate(ClassGenerator cg) throws Exception {
            if (this.classLoader == null) {
                return super.generate(cg);
            }
            Thread currentThread = Thread.currentThread();
            try {
                ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
                boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
                if (overrideClassLoader) {
                    currentThread.setContextClassLoader(this.classLoader);
                }
                try {
                    byte[] bArrGenerate = super.generate(cg);
                    if (overrideClassLoader) {
                        currentThread.setContextClassLoader(threadContextClassLoader);
                    }
                    return bArrGenerate;
                } catch (Throwable th) {
                    if (overrideClassLoader) {
                        currentThread.setContextClassLoader(threadContextClassLoader);
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                return super.generate(cg);
            }
        }
    }
}
