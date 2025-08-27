package org.springframework.core;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/MethodIntrospector.class */
public abstract class MethodIntrospector {

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/MethodIntrospector$MetadataLookup.class */
    public interface MetadataLookup<T> {
        T inspect(Method method);
    }

    public static <T> Map<Method, T> selectMethods(Class<?> targetType, final MetadataLookup<T> metadataLookup) {
        final Map<Method, T> methodMap = new LinkedHashMap<>();
        Set<Class<?>> handlerTypes = new LinkedHashSet<>();
        Class<?> specificHandlerType = null;
        if (!Proxy.isProxyClass(targetType)) {
            handlerTypes.add(targetType);
            specificHandlerType = targetType;
        }
        handlerTypes.addAll(Arrays.asList(targetType.getInterfaces()));
        for (Class<?> currentHandlerType : handlerTypes) {
            final Class<?> targetClass = specificHandlerType != null ? specificHandlerType : currentHandlerType;
            ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback() { // from class: org.springframework.core.MethodIntrospector.1
                @Override // org.springframework.util.ReflectionUtils.MethodCallback
                public void doWith(Method method) throws SecurityException, IllegalArgumentException {
                    Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
                    Object objInspect = metadataLookup.inspect(specificMethod);
                    if (objInspect != null) {
                        Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
                        if (bridgedMethod == specificMethod || metadataLookup.inspect(bridgedMethod) == null) {
                            methodMap.put(specificMethod, objInspect);
                        }
                    }
                }
            }, ReflectionUtils.USER_DECLARED_METHODS);
        }
        return methodMap;
    }

    public static Set<Method> selectMethods(Class<?> targetType, final ReflectionUtils.MethodFilter methodFilter) {
        return selectMethods(targetType, new MetadataLookup<Boolean>() { // from class: org.springframework.core.MethodIntrospector.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.core.MethodIntrospector.MetadataLookup
            public Boolean inspect(Method method) {
                if (methodFilter.matches(method)) {
                    return Boolean.TRUE;
                }
                return null;
            }
        }).keySet();
    }

    public static Method selectInvocableMethod(Method method, Class<?> targetType) {
        if (method.getDeclaringClass().isAssignableFrom(targetType)) {
            return method;
        }
        try {
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> ifc : targetType.getInterfaces()) {
                try {
                    return ifc.getMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException e) {
                }
            }
            return targetType.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e2) {
            throw new IllegalStateException(String.format("Need to invoke method '%s' declared on target class '%s', but not found in any interface(s) of the exposed proxy type. Either pull the method up to an interface or switch to CGLIB proxies by enforcing proxy-target-class mode in your configuration.", method.getName(), method.getDeclaringClass().getSimpleName()));
        }
    }
}
