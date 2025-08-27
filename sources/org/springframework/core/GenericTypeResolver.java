package org.springframework.core;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/GenericTypeResolver.class */
public abstract class GenericTypeResolver {
    private static final Map<Class<?>, Map<TypeVariable, Type>> typeVariableCache = new ConcurrentReferenceHashMap();

    @Deprecated
    public static Type getTargetType(MethodParameter methodParameter) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");
        return methodParameter.getGenericParameterType();
    }

    public static Class<?> resolveParameterType(MethodParameter methodParameter, Class<?> implementationClass) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");
        Assert.notNull(implementationClass, "Class must not be null");
        methodParameter.setContainingClass(implementationClass);
        ResolvableType.resolveMethodParameter(methodParameter);
        return methodParameter.getParameterType();
    }

    public static Class<?> resolveReturnType(Method method, Class<?> clazz) {
        Assert.notNull(method, "Method must not be null");
        Assert.notNull(clazz, "Class must not be null");
        return ResolvableType.forMethodReturnType(method, clazz).resolve(method.getReturnType());
    }

    @Deprecated
    public static Class<?> resolveReturnTypeForGenericMethod(Method method, Object[] args, ClassLoader classLoader) {
        Assert.notNull(method, "Method must not be null");
        Assert.notNull(args, "Argument array must not be null");
        TypeVariable<Method>[] declaredTypeVariables = method.getTypeParameters();
        Type genericReturnType = method.getGenericReturnType();
        Type[] methodArgumentTypes = method.getGenericParameterTypes();
        if (declaredTypeVariables.length == 0) {
            return method.getReturnType();
        }
        if (args.length < methodArgumentTypes.length) {
            return null;
        }
        boolean locallyDeclaredTypeVariableMatchesReturnType = false;
        int length = declaredTypeVariables.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            TypeVariable<Method> currentTypeVariable = declaredTypeVariables[i];
            if (!currentTypeVariable.equals(genericReturnType)) {
                i++;
            } else {
                locallyDeclaredTypeVariableMatchesReturnType = true;
                break;
            }
        }
        if (locallyDeclaredTypeVariableMatchesReturnType) {
            for (int i2 = 0; i2 < methodArgumentTypes.length; i2++) {
                Type currentMethodArgumentType = methodArgumentTypes[i2];
                if (currentMethodArgumentType.equals(genericReturnType)) {
                    return args[i2].getClass();
                }
                if (currentMethodArgumentType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) currentMethodArgumentType;
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    for (Type typeArg : actualTypeArguments) {
                        if (typeArg.equals(genericReturnType)) {
                            Object arg = args[i2];
                            if (arg instanceof Class) {
                                return (Class) arg;
                            }
                            if ((arg instanceof String) && classLoader != null) {
                                try {
                                    return classLoader.loadClass((String) arg);
                                } catch (ClassNotFoundException ex) {
                                    throw new IllegalStateException("Could not resolve specific class name argument [" + arg + "]", ex);
                                }
                            }
                            return method.getReturnType();
                        }
                    }
                }
            }
        }
        return method.getReturnType();
    }

    public static Class<?> resolveReturnTypeArgument(Method method, Class<?> genericIfc) {
        Assert.notNull(method, "Method must not be null");
        ResolvableType resolvableType = ResolvableType.forMethodReturnType(method).as(genericIfc);
        if (!resolvableType.hasGenerics() || (resolvableType.getType() instanceof WildcardType)) {
            return null;
        }
        return getSingleGeneric(resolvableType);
    }

    public static Class<?> resolveTypeArgument(Class<?> clazz, Class<?> genericIfc) {
        ResolvableType resolvableType = ResolvableType.forClass(clazz).as(genericIfc);
        if (!resolvableType.hasGenerics()) {
            return null;
        }
        return getSingleGeneric(resolvableType);
    }

    private static Class<?> getSingleGeneric(ResolvableType resolvableType) {
        if (resolvableType.getGenerics().length > 1) {
            throw new IllegalArgumentException("Expected 1 type argument on generic interface [" + resolvableType + "] but found " + resolvableType.getGenerics().length);
        }
        return resolvableType.getGeneric(new int[0]).resolve();
    }

    public static Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
        ResolvableType type = ResolvableType.forClass(clazz).as(genericIfc);
        if (!type.hasGenerics() || type.isEntirelyUnresolvable()) {
            return null;
        }
        return type.resolveGenerics(Object.class);
    }

    public static Class<?> resolveType(Type genericType, Map<TypeVariable, Type> map) {
        return ResolvableType.forType(genericType, new TypeVariableMapVariableResolver(map)).resolve(Object.class);
    }

    public static Map<TypeVariable, Type> getTypeVariableMap(Class<?> clazz) {
        Map<TypeVariable, Type> typeVariableMap = typeVariableCache.get(clazz);
        if (typeVariableMap == null) {
            typeVariableMap = new HashMap();
            buildTypeVariableMap(ResolvableType.forClass(clazz), typeVariableMap);
            typeVariableCache.put(clazz, Collections.unmodifiableMap(typeVariableMap));
        }
        return typeVariableMap;
    }

    private static void buildTypeVariableMap(ResolvableType type, Map<TypeVariable, Type> typeVariableMap) {
        ResolvableType generic;
        if (type != ResolvableType.NONE) {
            if (type.getType() instanceof ParameterizedType) {
                TypeVariable<?>[] variables = type.resolve().getTypeParameters();
                for (int i = 0; i < variables.length; i++) {
                    ResolvableType generic2 = type.getGeneric(i);
                    while (true) {
                        generic = generic2;
                        if (!(generic.getType() instanceof TypeVariable)) {
                            break;
                        } else {
                            generic2 = generic.resolveType();
                        }
                    }
                    if (generic != ResolvableType.NONE) {
                        typeVariableMap.put(variables[i], generic.getType());
                    }
                }
            }
            buildTypeVariableMap(type.getSuperType(), typeVariableMap);
            for (ResolvableType interfaceType : type.getInterfaces()) {
                buildTypeVariableMap(interfaceType, typeVariableMap);
            }
            if (type.resolve().isMemberClass()) {
                buildTypeVariableMap(ResolvableType.forClass(type.resolve().getEnclosingClass()), typeVariableMap);
            }
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/GenericTypeResolver$TypeVariableMapVariableResolver.class */
    private static class TypeVariableMapVariableResolver implements ResolvableType.VariableResolver {
        private final Map<TypeVariable, Type> typeVariableMap;

        public TypeVariableMapVariableResolver(Map<TypeVariable, Type> typeVariableMap) {
            this.typeVariableMap = typeVariableMap;
        }

        @Override // org.springframework.core.ResolvableType.VariableResolver
        public ResolvableType resolveVariable(TypeVariable<?> variable) {
            Type type = this.typeVariableMap.get(variable);
            if (type != null) {
                return ResolvableType.forType(type);
            }
            return null;
        }

        @Override // org.springframework.core.ResolvableType.VariableResolver
        public Object getSource() {
            return this.typeVariableMap;
        }
    }
}
