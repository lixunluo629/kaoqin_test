package org.springframework.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/BridgeMethodResolver.class */
public abstract class BridgeMethodResolver {
    public static Method findBridgedMethod(Method bridgeMethod) throws SecurityException, IllegalArgumentException {
        if (bridgeMethod == null || !bridgeMethod.isBridge()) {
            return bridgeMethod;
        }
        List<Method> candidateMethods = new ArrayList<>();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bridgeMethod.getDeclaringClass());
        for (Method candidateMethod : methods) {
            if (isBridgedCandidateFor(candidateMethod, bridgeMethod)) {
                candidateMethods.add(candidateMethod);
            }
        }
        if (candidateMethods.size() == 1) {
            return candidateMethods.get(0);
        }
        Method bridgedMethod = searchCandidates(candidateMethods, bridgeMethod);
        if (bridgedMethod != null) {
            return bridgedMethod;
        }
        return bridgeMethod;
    }

    private static boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod) {
        return !candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod) && candidateMethod.getName().equals(bridgeMethod.getName()) && candidateMethod.getParameterTypes().length == bridgeMethod.getParameterTypes().length;
    }

    private static Method searchCandidates(List<Method> candidateMethods, Method bridgeMethod) {
        if (candidateMethods.isEmpty()) {
            return null;
        }
        Method previousMethod = null;
        boolean sameSig = true;
        for (Method candidateMethod : candidateMethods) {
            if (isBridgeMethodFor(bridgeMethod, candidateMethod, bridgeMethod.getDeclaringClass())) {
                return candidateMethod;
            }
            if (previousMethod != null) {
                sameSig = sameSig && Arrays.equals(candidateMethod.getGenericParameterTypes(), previousMethod.getGenericParameterTypes());
            }
            previousMethod = candidateMethod;
        }
        if (sameSig) {
            return candidateMethods.get(0);
        }
        return null;
    }

    static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Class<?> declaringClass) {
        if (isResolvedTypeMatch(candidateMethod, bridgeMethod, declaringClass)) {
            return true;
        }
        Method method = findGenericDeclaration(bridgeMethod);
        return method != null && isResolvedTypeMatch(method, candidateMethod, declaringClass);
    }

    private static boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod, Class<?> declaringClass) {
        Type[] genericParameters = genericMethod.getGenericParameterTypes();
        Class<?>[] candidateParameters = candidateMethod.getParameterTypes();
        if (genericParameters.length != candidateParameters.length) {
            return false;
        }
        for (int i = 0; i < candidateParameters.length; i++) {
            ResolvableType genericParameter = ResolvableType.forMethodParameter(genericMethod, i, declaringClass);
            Class<?> candidateParameter = candidateParameters[i];
            if ((candidateParameter.isArray() && !candidateParameter.getComponentType().equals(genericParameter.getComponentType().resolve(Object.class))) || !candidateParameter.equals(genericParameter.resolve(Object.class))) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x002d, code lost:
    
        r0 = org.springframework.util.ClassUtils.getAllInterfacesForClass(r3.getDeclaringClass());
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x003a, code lost:
    
        return searchInterfaces(r0, r3);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.reflect.Method findGenericDeclaration(java.lang.reflect.Method r3) {
        /*
            r0 = r3
            java.lang.Class r0 = r0.getDeclaringClass()
            java.lang.Class r0 = r0.getSuperclass()
            r4 = r0
        L8:
            r0 = r4
            if (r0 == 0) goto L2d
            java.lang.Class<java.lang.Object> r0 = java.lang.Object.class
            r1 = r4
            if (r0 == r1) goto L2d
            r0 = r4
            r1 = r3
            java.lang.reflect.Method r0 = searchForMatch(r0, r1)
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L25
            r0 = r5
            boolean r0 = r0.isBridge()
            if (r0 != 0) goto L25
            r0 = r5
            return r0
        L25:
            r0 = r4
            java.lang.Class r0 = r0.getSuperclass()
            r4 = r0
            goto L8
        L2d:
            r0 = r3
            java.lang.Class r0 = r0.getDeclaringClass()
            java.lang.Class[] r0 = org.springframework.util.ClassUtils.getAllInterfacesForClass(r0)
            r5 = r0
            r0 = r5
            r1 = r3
            java.lang.reflect.Method r0 = searchInterfaces(r0, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.core.BridgeMethodResolver.findGenericDeclaration(java.lang.reflect.Method):java.lang.reflect.Method");
    }

    private static Method searchInterfaces(Class<?>[] interfaces, Method bridgeMethod) {
        for (Class<?> ifc : interfaces) {
            Method method = searchForMatch(ifc, bridgeMethod);
            if (method != null && !method.isBridge()) {
                return method;
            }
            Method method2 = searchInterfaces(ifc.getInterfaces(), bridgeMethod);
            if (method2 != null) {
                return method2;
            }
        }
        return null;
    }

    private static Method searchForMatch(Class<?> type, Method bridgeMethod) {
        try {
            return type.getDeclaredMethod(bridgeMethod.getName(), bridgeMethod.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static boolean isVisibilityBridgeMethodPair(Method bridgeMethod, Method bridgedMethod) {
        if (bridgeMethod == bridgedMethod) {
            return true;
        }
        return bridgeMethod.getReturnType().equals(bridgedMethod.getReturnType()) && Arrays.equals(bridgeMethod.getParameterTypes(), bridgedMethod.getParameterTypes());
    }
}
