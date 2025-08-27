package org.springframework.data.projection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/DefaultMethodInvokingMethodInterceptor.class */
public class DefaultMethodInvokingMethodInterceptor implements MethodInterceptor {
    private final MethodHandleLookup methodHandleLookup = MethodHandleLookup.getMethodHandleLookup();
    private final Map<Method, MethodHandle> methodHandleCache = new ConcurrentReferenceHashMap(10, ConcurrentReferenceHashMap.ReferenceType.WEAK);

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (!ReflectionUtils.isDefaultMethod(method)) {
            return invocation.proceed();
        }
        Object[] arguments = invocation.getArguments();
        Object proxy = ((ProxyMethodInvocation) invocation).getProxy();
        return getMethodHandle(method).bindTo(proxy).invokeWithArguments(arguments);
    }

    private MethodHandle getMethodHandle(Method method) throws Exception {
        MethodHandle handle = this.methodHandleCache.get(method);
        if (handle == null) {
            handle = this.methodHandleLookup.lookup(method);
            this.methodHandleCache.put(method, handle);
        }
        return handle;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/DefaultMethodInvokingMethodInterceptor$MethodHandleLookup.class */
    enum MethodHandleLookup {
        OPEN { // from class: org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup.1
            private final Constructor<MethodHandles.Lookup> constructor = MethodHandleLookup.getLookupConstructor();

            @Override // org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup
            MethodHandle lookup(Method method) throws ReflectiveOperationException {
                if (this.constructor == null) {
                    throw new IllegalStateException("Could not obtain MethodHandles.lookup constructor");
                }
                return this.constructor.newInstance(method.getDeclaringClass()).unreflectSpecial(method, method.getDeclaringClass());
            }

            @Override // org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup
            boolean isAvailable() {
                return this.constructor != null;
            }
        },
        ENCAPSULATED { // from class: org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup.2
            @Override // org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup
            MethodHandle lookup(Method method) throws ReflectiveOperationException {
                MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
                return MethodHandles.lookup().findSpecial(method.getDeclaringClass(), method.getName(), methodType, method.getDeclaringClass());
            }

            @Override // org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup
            boolean isAvailable() {
                return true;
            }
        };

        abstract MethodHandle lookup(Method method) throws ReflectiveOperationException;

        abstract boolean isAvailable();

        public static MethodHandleLookup getMethodHandleLookup() {
            for (MethodHandleLookup lookup : values()) {
                if (lookup.isAvailable()) {
                    return lookup;
                }
            }
            throw new IllegalStateException("No MethodHandleLookup available!");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Constructor<MethodHandles.Lookup> getLookupConstructor() throws NoSuchMethodException, SecurityException {
            try {
                Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
                org.springframework.util.ReflectionUtils.makeAccessible(constructor);
                return constructor;
            } catch (Exception ex) {
                if (ex.getClass().getName().equals("java.lang.reflect.InaccessibleObjectException")) {
                    return null;
                }
                throw new IllegalStateException(ex);
            }
        }
    }
}
