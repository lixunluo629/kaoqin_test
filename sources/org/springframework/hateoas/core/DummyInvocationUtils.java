package org.springframework.hateoas.core;

import java.beans.ConstructorProperties;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import lombok.NonNull;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.EmptyTargetSource;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.Factory;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/DummyInvocationUtils.class */
public class DummyInvocationUtils {
    private static final ObjenesisStd OBJENESIS = new ObjenesisStd();
    private static final Map<Class<?>, Class<?>> CLASS_CACHE = new ConcurrentReferenceHashMap(16, ConcurrentReferenceHashMap.ReferenceType.WEAK);

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/DummyInvocationUtils$LastInvocationAware.class */
    public interface LastInvocationAware {
        Iterator<Object> getObjectParameters();

        MethodInvocation getLastInvocation();
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/DummyInvocationUtils$MethodInvocation.class */
    public interface MethodInvocation {
        Object[] getArguments();

        Method getMethod();

        Class<?> getTargetType();
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/DummyInvocationUtils$InvocationRecordingMethodInterceptor.class */
    private static class InvocationRecordingMethodInterceptor implements MethodInterceptor, LastInvocationAware, org.springframework.cglib.proxy.MethodInterceptor {
        private static final Method GET_INVOCATIONS = ReflectionUtils.findMethod(LastInvocationAware.class, "getLastInvocation");
        private static final Method GET_OBJECT_PARAMETERS = ReflectionUtils.findMethod(LastInvocationAware.class, "getObjectParameters");
        private final Class<?> targetType;
        private final Object[] objectParameters;
        private MethodInvocation invocation;

        InvocationRecordingMethodInterceptor(Class<?> targetType, Object... parameters) {
            Assert.notNull(targetType, "Target type must not be null!");
            Assert.notNull(parameters, "Parameters must not be null!");
            this.targetType = targetType;
            this.objectParameters = (Object[]) parameters.clone();
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
            if (GET_INVOCATIONS.equals(method)) {
                return getLastInvocation();
            }
            if (GET_OBJECT_PARAMETERS.equals(method)) {
                return getObjectParameters();
            }
            if (Object.class.equals(method.getDeclaringClass())) {
                return ReflectionUtils.invokeMethod(method, obj, args);
            }
            this.invocation = new SimpleMethodInvocation(this.targetType, method, args);
            Class<?> returnType = method.getReturnType();
            return returnType.cast(DummyInvocationUtils.getProxyWithInterceptor(returnType, this, obj.getClass().getClassLoader()));
        }

        @Override // org.aopalliance.intercept.MethodInterceptor
        public Object invoke(org.aopalliance.intercept.MethodInvocation invocation) throws Throwable {
            return intercept(invocation.getThis(), invocation.getMethod(), invocation.getArguments(), null);
        }

        @Override // org.springframework.hateoas.core.DummyInvocationUtils.LastInvocationAware
        public MethodInvocation getLastInvocation() {
            return this.invocation;
        }

        @Override // org.springframework.hateoas.core.DummyInvocationUtils.LastInvocationAware
        public Iterator<Object> getObjectParameters() {
            return Arrays.asList(this.objectParameters).iterator();
        }
    }

    public static <T> T methodOn(Class<T> cls, Object... objArr) {
        Assert.notNull(cls, "Given type must not be null!");
        return (T) getProxyWithInterceptor(cls, new InvocationRecordingMethodInterceptor(cls, objArr), cls.getClassLoader());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v9, types: [T, org.springframework.cglib.proxy.Factory] */
    public static <T> T getProxyWithInterceptor(Class<?> cls, InvocationRecordingMethodInterceptor invocationRecordingMethodInterceptor, ClassLoader classLoader) {
        if (cls.isInterface()) {
            ProxyFactory proxyFactory = new ProxyFactory(EmptyTargetSource.INSTANCE);
            proxyFactory.addInterface(cls);
            proxyFactory.addInterface(LastInvocationAware.class);
            proxyFactory.addAdvice(invocationRecordingMethodInterceptor);
            return (T) proxyFactory.getProxy();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setInterfaces(new Class[]{LastInvocationAware.class});
        enhancer.setCallbackType(org.springframework.cglib.proxy.MethodInterceptor.class);
        enhancer.setClassLoader(classLoader);
        ?? r0 = (T) ((Factory) OBJENESIS.newInstance(getOrCreateEnhancedClass(cls, classLoader)));
        r0.setCallbacks(new Callback[]{invocationRecordingMethodInterceptor});
        return r0;
    }

    private static Class<?> getOrCreateEnhancedClass(Class<?> type, ClassLoader classLoader) {
        Assert.notNull(type, "Source type must not be null!");
        Assert.notNull(classLoader, "ClassLoader must not be null!");
        Class<?> result = CLASS_CACHE.get(type);
        if (result != null) {
            return result;
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);
        enhancer.setInterfaces(new Class[]{LastInvocationAware.class});
        enhancer.setCallbackType(org.springframework.cglib.proxy.MethodInterceptor.class);
        enhancer.setClassLoader(classLoader);
        Class<?> result2 = enhancer.createClass();
        CLASS_CACHE.put(type, result2);
        return result2;
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/DummyInvocationUtils$SimpleMethodInvocation.class */
    static final class SimpleMethodInvocation implements MethodInvocation {

        @NonNull
        private final Class<?> targetType;

        @NonNull
        private final Method method;

        @NonNull
        private final Object[] arguments;

        @ConstructorProperties({"targetType", JamXmlElements.METHOD, "arguments"})
        public SimpleMethodInvocation(@NonNull Class<?> targetType, @NonNull Method method, @NonNull Object[] arguments) {
            if (targetType == null) {
                throw new NullPointerException("targetType");
            }
            if (method == null) {
                throw new NullPointerException(JamXmlElements.METHOD);
            }
            if (arguments == null) {
                throw new NullPointerException("arguments");
            }
            this.targetType = targetType;
            this.method = method;
            this.arguments = arguments;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof SimpleMethodInvocation)) {
                return false;
            }
            SimpleMethodInvocation other = (SimpleMethodInvocation) o;
            Object this$targetType = getTargetType();
            Object other$targetType = other.getTargetType();
            if (this$targetType == null) {
                if (other$targetType != null) {
                    return false;
                }
            } else if (!this$targetType.equals(other$targetType)) {
                return false;
            }
            Object this$method = getMethod();
            Object other$method = other.getMethod();
            if (this$method == null) {
                if (other$method != null) {
                    return false;
                }
            } else if (!this$method.equals(other$method)) {
                return false;
            }
            return Arrays.deepEquals(getArguments(), other.getArguments());
        }

        public int hashCode() {
            Object $targetType = getTargetType();
            int result = (1 * 59) + ($targetType == null ? 43 : $targetType.hashCode());
            Object $method = getMethod();
            return (((result * 59) + ($method == null ? 43 : $method.hashCode())) * 59) + Arrays.deepHashCode(getArguments());
        }

        public String toString() {
            return "DummyInvocationUtils.SimpleMethodInvocation(targetType=" + getTargetType() + ", method=" + getMethod() + ", arguments=" + Arrays.deepToString(getArguments()) + ")";
        }

        @Override // org.springframework.hateoas.core.DummyInvocationUtils.MethodInvocation
        @NonNull
        public Class<?> getTargetType() {
            return this.targetType;
        }

        @Override // org.springframework.hateoas.core.DummyInvocationUtils.MethodInvocation
        @NonNull
        public Method getMethod() {
            return this.method;
        }

        @Override // org.springframework.hateoas.core.DummyInvocationUtils.MethodInvocation
        @NonNull
        public Object[] getArguments() {
            return this.arguments;
        }
    }
}
