package org.springframework.core;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import org.springframework.core.SerializableTypeWrapper;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ResolvableType.class */
public class ResolvableType implements Serializable {
    public static final ResolvableType NONE = new ResolvableType((Type) null, (SerializableTypeWrapper.TypeProvider) null, (VariableResolver) null, (Integer) 0);
    private static final ResolvableType[] EMPTY_TYPES_ARRAY = new ResolvableType[0];
    private static final ConcurrentReferenceHashMap<ResolvableType, ResolvableType> cache = new ConcurrentReferenceHashMap<>(256);
    private final Type type;
    private final SerializableTypeWrapper.TypeProvider typeProvider;
    private final VariableResolver variableResolver;
    private final ResolvableType componentType;
    private final Class<?> resolved;
    private final Integer hash;
    private ResolvableType superType;
    private ResolvableType[] interfaces;
    private ResolvableType[] generics;

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ResolvableType$VariableResolver.class */
    interface VariableResolver extends Serializable {
        Object getSource();

        ResolvableType resolveVariable(TypeVariable<?> typeVariable);
    }

    private ResolvableType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver) {
        this.type = type;
        this.typeProvider = typeProvider;
        this.variableResolver = variableResolver;
        this.componentType = null;
        this.resolved = null;
        this.hash = Integer.valueOf(calculateHashCode());
    }

    private ResolvableType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver, Integer hash) {
        this.type = type;
        this.typeProvider = typeProvider;
        this.variableResolver = variableResolver;
        this.componentType = null;
        this.resolved = resolveClass();
        this.hash = hash;
    }

    private ResolvableType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver, ResolvableType componentType) {
        this.type = type;
        this.typeProvider = typeProvider;
        this.variableResolver = variableResolver;
        this.componentType = componentType;
        this.resolved = resolveClass();
        this.hash = null;
    }

    private ResolvableType(Class<?> clazz) {
        this.resolved = clazz != null ? clazz : Object.class;
        this.type = this.resolved;
        this.typeProvider = null;
        this.variableResolver = null;
        this.componentType = null;
        this.hash = null;
    }

    public Type getType() {
        return SerializableTypeWrapper.unwrap(this.type);
    }

    public Class<?> getRawClass() {
        if (this.type == this.resolved) {
            return this.resolved;
        }
        Type rawType = this.type;
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }
        if (rawType instanceof Class) {
            return (Class) rawType;
        }
        return null;
    }

    public Object getSource() {
        Object source = this.typeProvider != null ? this.typeProvider.getSource() : null;
        return source != null ? source : this.type;
    }

    public boolean isInstance(Object obj) {
        return obj != null && isAssignableFrom(obj.getClass());
    }

    public boolean isAssignableFrom(Class<?> other) {
        return isAssignableFrom(forClass(other), null);
    }

    public boolean isAssignableFrom(ResolvableType other) {
        return isAssignableFrom(other, null);
    }

    private boolean isAssignableFrom(ResolvableType other, Map<Type, Type> matchedBefore) {
        ResolvableType resolved;
        ResolvableType resolved2;
        Assert.notNull(other, "ResolvableType must not be null");
        if (this == NONE || other == NONE) {
            return false;
        }
        if (isArray()) {
            return other.isArray() && getComponentType().isAssignableFrom(other.getComponentType());
        }
        if (matchedBefore != null && matchedBefore.get(this.type) == other.type) {
            return true;
        }
        WildcardBounds ourBounds = WildcardBounds.get(this);
        WildcardBounds typeBounds = WildcardBounds.get(other);
        if (typeBounds != null) {
            return ourBounds != null && ourBounds.isSameKind(typeBounds) && ourBounds.isAssignableFrom(typeBounds.getBounds());
        }
        if (ourBounds != null) {
            return ourBounds.isAssignableFrom(other);
        }
        boolean exactMatch = matchedBefore != null;
        boolean checkGenerics = true;
        Class<?> ourResolved = null;
        if (this.type instanceof TypeVariable) {
            TypeVariable<?> variable = (TypeVariable) this.type;
            if (this.variableResolver != null && (resolved2 = this.variableResolver.resolveVariable(variable)) != null) {
                ourResolved = resolved2.resolve();
            }
            if (ourResolved == null && other.variableResolver != null && (resolved = other.variableResolver.resolveVariable(variable)) != null) {
                ourResolved = resolved.resolve();
                checkGenerics = false;
            }
            if (ourResolved == null) {
                exactMatch = false;
            }
        }
        if (ourResolved == null) {
            ourResolved = resolve(Object.class);
        }
        Class<?> otherResolved = other.resolve(Object.class);
        if (exactMatch) {
            if (!ourResolved.equals(otherResolved)) {
                return false;
            }
        } else if (!ClassUtils.isAssignable(ourResolved, otherResolved)) {
            return false;
        }
        if (checkGenerics) {
            ResolvableType[] ourGenerics = getGenerics();
            ResolvableType[] typeGenerics = other.as(ourResolved).getGenerics();
            if (ourGenerics.length != typeGenerics.length) {
                return false;
            }
            if (matchedBefore == null) {
                matchedBefore = new IdentityHashMap(1);
            }
            matchedBefore.put(this.type, other.type);
            for (int i = 0; i < ourGenerics.length; i++) {
                if (!ourGenerics[i].isAssignableFrom(typeGenerics[i], matchedBefore)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public boolean isArray() {
        if (this == NONE) {
            return false;
        }
        return ((this.type instanceof Class) && ((Class) this.type).isArray()) || (this.type instanceof GenericArrayType) || resolveType().isArray();
    }

    public ResolvableType getComponentType() {
        if (this == NONE) {
            return NONE;
        }
        if (this.componentType != null) {
            return this.componentType;
        }
        if (this.type instanceof Class) {
            Class<?> componentType = ((Class) this.type).getComponentType();
            return forType(componentType, this.variableResolver);
        }
        if (this.type instanceof GenericArrayType) {
            return forType(((GenericArrayType) this.type).getGenericComponentType(), this.variableResolver);
        }
        return resolveType().getComponentType();
    }

    public ResolvableType asCollection() {
        return as(Collection.class);
    }

    public ResolvableType asMap() {
        return as(Map.class);
    }

    public ResolvableType as(Class<?> type) {
        if (this == NONE) {
            return NONE;
        }
        if (ObjectUtils.nullSafeEquals(resolve(), type)) {
            return this;
        }
        for (ResolvableType interfaceType : getInterfaces()) {
            ResolvableType interfaceAsType = interfaceType.as(type);
            if (interfaceAsType != NONE) {
                return interfaceAsType;
            }
        }
        return getSuperType().as(type);
    }

    public ResolvableType getSuperType() {
        Class<?> resolved = resolve();
        if (resolved == null || resolved.getGenericSuperclass() == null) {
            return NONE;
        }
        if (this.superType == null) {
            this.superType = forType(SerializableTypeWrapper.forGenericSuperclass(resolved), asVariableResolver());
        }
        return this.superType;
    }

    public ResolvableType[] getInterfaces() {
        Class<?> resolved = resolve();
        if (resolved == null || ObjectUtils.isEmpty((Object[]) resolved.getGenericInterfaces())) {
            return EMPTY_TYPES_ARRAY;
        }
        if (this.interfaces == null) {
            this.interfaces = forTypes(SerializableTypeWrapper.forGenericInterfaces(resolved), asVariableResolver());
        }
        return this.interfaces;
    }

    public boolean hasGenerics() {
        return getGenerics().length > 0;
    }

    boolean isEntirelyUnresolvable() {
        if (this == NONE) {
            return false;
        }
        ResolvableType[] generics = getGenerics();
        for (ResolvableType generic : generics) {
            if (!generic.isUnresolvableTypeVariable() && !generic.isWildcardWithoutBounds()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasUnresolvableGenerics() {
        if (this == NONE) {
            return false;
        }
        ResolvableType[] generics = getGenerics();
        for (ResolvableType generic : generics) {
            if (generic.isUnresolvableTypeVariable() || generic.isWildcardWithoutBounds()) {
                return true;
            }
        }
        Class<?> resolved = resolve();
        if (resolved != null) {
            for (Type genericInterface : resolved.getGenericInterfaces()) {
                if ((genericInterface instanceof Class) && forClass((Class) genericInterface).hasGenerics()) {
                    return true;
                }
            }
            return getSuperType().hasUnresolvableGenerics();
        }
        return false;
    }

    private boolean isUnresolvableTypeVariable() {
        if (this.type instanceof TypeVariable) {
            if (this.variableResolver == null) {
                return true;
            }
            TypeVariable<?> variable = (TypeVariable) this.type;
            ResolvableType resolved = this.variableResolver.resolveVariable(variable);
            if (resolved == null || resolved.isUnresolvableTypeVariable()) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean isWildcardWithoutBounds() {
        if (this.type instanceof WildcardType) {
            WildcardType wt = (WildcardType) this.type;
            if (wt.getLowerBounds().length == 0) {
                Type[] upperBounds = wt.getUpperBounds();
                if (upperBounds.length == 0) {
                    return true;
                }
                if (upperBounds.length == 1 && Object.class == upperBounds[0]) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public ResolvableType getNested(int nestingLevel) {
        return getNested(nestingLevel, null);
    }

    public ResolvableType getNested(int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel) {
        ResolvableType generic;
        ResolvableType result = this;
        for (int i = 2; i <= nestingLevel; i++) {
            if (result.isArray()) {
                generic = result.getComponentType();
            } else {
                while (result != NONE && !result.hasGenerics()) {
                    result = result.getSuperType();
                }
                Integer index = typeIndexesPerLevel != null ? typeIndexesPerLevel.get(Integer.valueOf(i)) : null;
                generic = result.getGeneric(Integer.valueOf(index == null ? result.getGenerics().length - 1 : index.intValue()).intValue());
            }
            result = generic;
        }
        return result;
    }

    public ResolvableType getGeneric(int... indexes) {
        ResolvableType[] generics = getGenerics();
        if (indexes == null || indexes.length == 0) {
            return generics.length == 0 ? NONE : generics[0];
        }
        ResolvableType generic = this;
        for (int index : indexes) {
            ResolvableType[] generics2 = generic.getGenerics();
            if (index < 0 || index >= generics2.length) {
                return NONE;
            }
            generic = generics2[index];
        }
        return generic;
    }

    public ResolvableType[] getGenerics() {
        if (this == NONE) {
            return EMPTY_TYPES_ARRAY;
        }
        if (this.generics == null) {
            if (this.type instanceof Class) {
                Class<?> typeClass = (Class) this.type;
                this.generics = forTypes(SerializableTypeWrapper.forTypeParameters(typeClass), this.variableResolver);
            } else if (this.type instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) this.type).getActualTypeArguments();
                ResolvableType[] generics = new ResolvableType[actualTypeArguments.length];
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    generics[i] = forType(actualTypeArguments[i], this.variableResolver);
                }
                this.generics = generics;
            } else {
                this.generics = resolveType().getGenerics();
            }
        }
        return this.generics;
    }

    public Class<?>[] resolveGenerics() {
        return resolveGenerics(null);
    }

    public Class<?>[] resolveGenerics(Class<?> fallback) {
        ResolvableType[] generics = getGenerics();
        Class<?>[] resolvedGenerics = new Class[generics.length];
        for (int i = 0; i < generics.length; i++) {
            resolvedGenerics[i] = generics[i].resolve(fallback);
        }
        return resolvedGenerics;
    }

    public Class<?> resolveGeneric(int... indexes) {
        return getGeneric(indexes).resolve();
    }

    public Class<?> resolve() {
        return resolve(null);
    }

    public Class<?> resolve(Class<?> fallback) {
        return this.resolved != null ? this.resolved : fallback;
    }

    private Class<?> resolveClass() {
        if ((this.type instanceof Class) || this.type == null) {
            return (Class) this.type;
        }
        if (this.type instanceof GenericArrayType) {
            Class<?> resolvedComponent = getComponentType().resolve();
            if (resolvedComponent != null) {
                return Array.newInstance(resolvedComponent, 0).getClass();
            }
            return null;
        }
        return resolveType().resolve();
    }

    ResolvableType resolveType() {
        ResolvableType resolved;
        if (this.type instanceof ParameterizedType) {
            return forType(((ParameterizedType) this.type).getRawType(), this.variableResolver);
        }
        if (this.type instanceof WildcardType) {
            Type resolved2 = resolveBounds(((WildcardType) this.type).getUpperBounds());
            if (resolved2 == null) {
                resolved2 = resolveBounds(((WildcardType) this.type).getLowerBounds());
            }
            return forType(resolved2, this.variableResolver);
        }
        if (this.type instanceof TypeVariable) {
            TypeVariable<?> variable = (TypeVariable) this.type;
            if (this.variableResolver != null && (resolved = this.variableResolver.resolveVariable(variable)) != null) {
                return resolved;
            }
            return forType(resolveBounds(variable.getBounds()), this.variableResolver);
        }
        return NONE;
    }

    private Type resolveBounds(Type[] bounds) {
        if (ObjectUtils.isEmpty((Object[]) bounds) || Object.class == bounds[0]) {
            return null;
        }
        return bounds[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ResolvableType resolveVariable(TypeVariable<?> variable) {
        if (this.type instanceof TypeVariable) {
            return resolveType().resolveVariable(variable);
        }
        if (this.type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) this.type;
            TypeVariable<?>[] variables = resolve().getTypeParameters();
            for (int i = 0; i < variables.length; i++) {
                if (ObjectUtils.nullSafeEquals(variables[i].getName(), variable.getName())) {
                    Type actualType = parameterizedType.getActualTypeArguments()[i];
                    return forType(actualType, this.variableResolver);
                }
            }
            if (parameterizedType.getOwnerType() != null) {
                return forType(parameterizedType.getOwnerType(), this.variableResolver).resolveVariable(variable);
            }
        }
        if (this.variableResolver != null) {
            return this.variableResolver.resolveVariable(variable);
        }
        return null;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ResolvableType)) {
            return false;
        }
        ResolvableType otherType = (ResolvableType) other;
        if (!ObjectUtils.nullSafeEquals(this.type, otherType.type)) {
            return false;
        }
        if (this.typeProvider != otherType.typeProvider && (this.typeProvider == null || otherType.typeProvider == null || !ObjectUtils.nullSafeEquals(this.typeProvider.getType(), otherType.typeProvider.getType()))) {
            return false;
        }
        if ((this.variableResolver != otherType.variableResolver && (this.variableResolver == null || otherType.variableResolver == null || !ObjectUtils.nullSafeEquals(this.variableResolver.getSource(), otherType.variableResolver.getSource()))) || !ObjectUtils.nullSafeEquals(this.componentType, otherType.componentType)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.hash != null ? this.hash.intValue() : calculateHashCode();
    }

    private int calculateHashCode() {
        int hashCode = ObjectUtils.nullSafeHashCode(this.type);
        if (this.typeProvider != null) {
            hashCode = (31 * hashCode) + ObjectUtils.nullSafeHashCode(this.typeProvider.getType());
        }
        if (this.variableResolver != null) {
            hashCode = (31 * hashCode) + ObjectUtils.nullSafeHashCode(this.variableResolver.getSource());
        }
        if (this.componentType != null) {
            hashCode = (31 * hashCode) + ObjectUtils.nullSafeHashCode(this.componentType);
        }
        return hashCode;
    }

    VariableResolver asVariableResolver() {
        if (this == NONE) {
            return null;
        }
        return new DefaultVariableResolver();
    }

    private Object readResolve() {
        return this.type == null ? NONE : this;
    }

    public String toString() {
        if (isArray()) {
            return getComponentType() + "[]";
        }
        if (this.resolved == null) {
            return "?";
        }
        if (this.type instanceof TypeVariable) {
            TypeVariable<?> variable = (TypeVariable) this.type;
            if (this.variableResolver == null || this.variableResolver.resolveVariable(variable) == null) {
                return "?";
            }
        }
        StringBuilder result = new StringBuilder(this.resolved.getName());
        if (hasGenerics()) {
            result.append('<');
            result.append(StringUtils.arrayToDelimitedString(getGenerics(), ", "));
            result.append('>');
        }
        return result.toString();
    }

    public static ResolvableType forClass(Class<?> clazz) {
        return new ResolvableType(clazz);
    }

    public static ResolvableType forRawClass(Class<?> clazz) {
        return new ResolvableType(clazz) { // from class: org.springframework.core.ResolvableType.1
            @Override // org.springframework.core.ResolvableType
            public ResolvableType[] getGenerics() {
                return ResolvableType.EMPTY_TYPES_ARRAY;
            }

            @Override // org.springframework.core.ResolvableType
            public boolean isAssignableFrom(Class<?> other) {
                return ClassUtils.isAssignable(getRawClass(), other);
            }

            @Override // org.springframework.core.ResolvableType
            public boolean isAssignableFrom(ResolvableType other) {
                Class<?> otherClass = other.getRawClass();
                return otherClass != null && ClassUtils.isAssignable(getRawClass(), otherClass);
            }
        };
    }

    public static ResolvableType forClass(Class<?> baseType, Class<?> implementationClass) {
        Assert.notNull(baseType, "Base type must not be null");
        ResolvableType asType = forType(implementationClass).as(baseType);
        return asType == NONE ? forType(baseType) : asType;
    }

    public static ResolvableType forClassWithGenerics(Class<?> clazz, Class<?>... generics) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(generics, "Generics array must not be null");
        ResolvableType[] resolvableGenerics = new ResolvableType[generics.length];
        for (int i = 0; i < generics.length; i++) {
            resolvableGenerics[i] = forClass(generics[i]);
        }
        return forClassWithGenerics(clazz, resolvableGenerics);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v23, types: [java.lang.reflect.Type] */
    public static ResolvableType forClassWithGenerics(Class<?> cls, ResolvableType... resolvableTypeArr) {
        Assert.notNull(cls, "Class must not be null");
        Assert.notNull(resolvableTypeArr, "Generics array must not be null");
        TypeVariable<Class<?>>[] typeParameters = cls.getTypeParameters();
        Assert.isTrue(typeParameters.length == resolvableTypeArr.length, "Mismatched number of generics specified");
        Type[] typeArr = new Type[resolvableTypeArr.length];
        for (int i = 0; i < resolvableTypeArr.length; i++) {
            ResolvableType resolvableType = resolvableTypeArr[i];
            TypeVariable<Class<?>> type = resolvableType != null ? resolvableType.getType() : null;
            typeArr[i] = type != null ? type : typeParameters[i];
        }
        return forType(new SyntheticParameterizedType(cls, typeArr), new TypeVariablesVariableResolver(typeParameters, resolvableTypeArr));
    }

    public static ResolvableType forInstance(Object instance) {
        ResolvableType type;
        Assert.notNull(instance, "Instance must not be null");
        if ((instance instanceof ResolvableTypeProvider) && (type = ((ResolvableTypeProvider) instance).getResolvableType()) != null) {
            return type;
        }
        return forClass(instance.getClass());
    }

    public static ResolvableType forField(Field field) {
        Assert.notNull(field, "Field must not be null");
        return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), null);
    }

    public static ResolvableType forField(Field field, Class<?> implementationClass) {
        Assert.notNull(field, "Field must not be null");
        ResolvableType owner = forType(implementationClass).as(field.getDeclaringClass());
        return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver());
    }

    public static ResolvableType forField(Field field, ResolvableType implementationType) {
        Assert.notNull(field, "Field must not be null");
        ResolvableType owner = implementationType != null ? implementationType : NONE;
        return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.as(field.getDeclaringClass()).asVariableResolver());
    }

    public static ResolvableType forField(Field field, int nestingLevel) {
        Assert.notNull(field, "Field must not be null");
        return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), null).getNested(nestingLevel);
    }

    public static ResolvableType forField(Field field, int nestingLevel, Class<?> implementationClass) {
        Assert.notNull(field, "Field must not be null");
        ResolvableType owner = forType(implementationClass).as(field.getDeclaringClass());
        return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver()).getNested(nestingLevel);
    }

    public static ResolvableType forConstructorParameter(Constructor<?> constructor, int parameterIndex) {
        Assert.notNull(constructor, "Constructor must not be null");
        return forMethodParameter(new MethodParameter(constructor, parameterIndex));
    }

    public static ResolvableType forConstructorParameter(Constructor<?> constructor, int parameterIndex, Class<?> implementationClass) {
        Assert.notNull(constructor, "Constructor must not be null");
        MethodParameter methodParameter = new MethodParameter(constructor, parameterIndex);
        methodParameter.setContainingClass(implementationClass);
        return forMethodParameter(methodParameter);
    }

    public static ResolvableType forMethodReturnType(Method method) {
        Assert.notNull(method, "Method must not be null");
        return forMethodParameter(new MethodParameter(method, -1));
    }

    public static ResolvableType forMethodReturnType(Method method, Class<?> implementationClass) {
        Assert.notNull(method, "Method must not be null");
        MethodParameter methodParameter = new MethodParameter(method, -1);
        methodParameter.setContainingClass(implementationClass);
        return forMethodParameter(methodParameter);
    }

    public static ResolvableType forMethodParameter(Method method, int parameterIndex) {
        Assert.notNull(method, "Method must not be null");
        return forMethodParameter(new MethodParameter(method, parameterIndex));
    }

    public static ResolvableType forMethodParameter(Method method, int parameterIndex, Class<?> implementationClass) {
        Assert.notNull(method, "Method must not be null");
        MethodParameter methodParameter = new MethodParameter(method, parameterIndex);
        methodParameter.setContainingClass(implementationClass);
        return forMethodParameter(methodParameter);
    }

    public static ResolvableType forMethodParameter(MethodParameter methodParameter) {
        return forMethodParameter(methodParameter, (Type) null);
    }

    public static ResolvableType forMethodParameter(MethodParameter methodParameter, ResolvableType implementationType) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");
        ResolvableType owner = (implementationType != null ? implementationType : forType(methodParameter.getContainingClass())).as(methodParameter.getDeclaringClass());
        return forType(null, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver()).getNested(methodParameter.getNestingLevel(), methodParameter.typeIndexesPerLevel);
    }

    public static ResolvableType forMethodParameter(MethodParameter methodParameter, Type targetType) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");
        ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
        return forType(targetType, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver()).getNested(methodParameter.getNestingLevel(), methodParameter.typeIndexesPerLevel);
    }

    static void resolveMethodParameter(MethodParameter methodParameter) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");
        ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
        methodParameter.setParameterType(forType(null, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver()).resolve());
    }

    public static ResolvableType forArrayComponent(ResolvableType componentType) {
        Assert.notNull(componentType, "Component type must not be null");
        Class<?> arrayClass = Array.newInstance(componentType.resolve(), 0).getClass();
        return new ResolvableType(arrayClass, (SerializableTypeWrapper.TypeProvider) null, (VariableResolver) null, componentType);
    }

    private static ResolvableType[] forTypes(Type[] types, VariableResolver owner) {
        ResolvableType[] result = new ResolvableType[types.length];
        for (int i = 0; i < types.length; i++) {
            result[i] = forType(types[i], owner);
        }
        return result;
    }

    public static ResolvableType forType(Type type) {
        return forType(type, null, null);
    }

    public static ResolvableType forType(Type type, ResolvableType owner) {
        VariableResolver variableResolver = null;
        if (owner != null) {
            variableResolver = owner.asVariableResolver();
        }
        return forType(type, variableResolver);
    }

    public static ResolvableType forType(ParameterizedTypeReference<?> typeReference) {
        return forType(typeReference.getType(), null, null);
    }

    static ResolvableType forType(Type type, VariableResolver variableResolver) {
        return forType(type, null, variableResolver);
    }

    static ResolvableType forType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver) {
        if (type == null && typeProvider != null) {
            type = SerializableTypeWrapper.forTypeProvider(typeProvider);
        }
        if (type == null) {
            return NONE;
        }
        if (type instanceof Class) {
            return new ResolvableType(type, typeProvider, variableResolver, (ResolvableType) null);
        }
        cache.purgeUnreferencedEntries();
        ResolvableType key = new ResolvableType(type, typeProvider, variableResolver);
        ResolvableType resolvableType = cache.get(key);
        if (resolvableType == null) {
            resolvableType = new ResolvableType(type, typeProvider, variableResolver, key.hash);
            cache.put(resolvableType, resolvableType);
        }
        return resolvableType;
    }

    public static void clearCache() {
        cache.clear();
        SerializableTypeWrapper.cache.clear();
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ResolvableType$DefaultVariableResolver.class */
    private class DefaultVariableResolver implements VariableResolver {
        private DefaultVariableResolver() {
        }

        @Override // org.springframework.core.ResolvableType.VariableResolver
        public ResolvableType resolveVariable(TypeVariable<?> variable) {
            return ResolvableType.this.resolveVariable(variable);
        }

        @Override // org.springframework.core.ResolvableType.VariableResolver
        public Object getSource() {
            return ResolvableType.this;
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ResolvableType$TypeVariablesVariableResolver.class */
    private static class TypeVariablesVariableResolver implements VariableResolver {
        private final TypeVariable<?>[] variables;
        private final ResolvableType[] generics;

        public TypeVariablesVariableResolver(TypeVariable<?>[] variables, ResolvableType[] generics) {
            this.variables = variables;
            this.generics = generics;
        }

        @Override // org.springframework.core.ResolvableType.VariableResolver
        public ResolvableType resolveVariable(TypeVariable<?> variable) {
            for (int i = 0; i < this.variables.length; i++) {
                TypeVariable<?> v1 = (TypeVariable) SerializableTypeWrapper.unwrap(this.variables[i]);
                TypeVariable<?> v2 = (TypeVariable) SerializableTypeWrapper.unwrap(variable);
                if (ObjectUtils.nullSafeEquals(v1, v2)) {
                    return this.generics[i];
                }
            }
            return null;
        }

        @Override // org.springframework.core.ResolvableType.VariableResolver
        public Object getSource() {
            return this.generics;
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ResolvableType$SyntheticParameterizedType.class */
    private static final class SyntheticParameterizedType implements ParameterizedType, Serializable {
        private final Type rawType;
        private final Type[] typeArguments;

        public SyntheticParameterizedType(Type rawType, Type[] typeArguments) {
            this.rawType = rawType;
            this.typeArguments = typeArguments;
        }

        @Override // java.lang.reflect.Type
        @UsesJava8
        public String getTypeName() {
            StringBuilder result = new StringBuilder(this.rawType.getTypeName());
            if (this.typeArguments.length > 0) {
                result.append('<');
                for (int i = 0; i < this.typeArguments.length; i++) {
                    if (i > 0) {
                        result.append(", ");
                    }
                    result.append(this.typeArguments[i].getTypeName());
                }
                result.append('>');
            }
            return result.toString();
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getOwnerType() {
            return null;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.rawType;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            return this.typeArguments;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType otherType = (ParameterizedType) other;
            return otherType.getOwnerType() == null && this.rawType.equals(otherType.getRawType()) && Arrays.equals(this.typeArguments, otherType.getActualTypeArguments());
        }

        public int hashCode() {
            return (this.rawType.hashCode() * 31) + Arrays.hashCode(this.typeArguments);
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ResolvableType$WildcardBounds.class */
    private static class WildcardBounds {
        private final Kind kind;
        private final ResolvableType[] bounds;

        /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ResolvableType$WildcardBounds$Kind.class */
        enum Kind {
            UPPER,
            LOWER
        }

        public WildcardBounds(Kind kind, ResolvableType[] bounds) {
            this.kind = kind;
            this.bounds = bounds;
        }

        public boolean isSameKind(WildcardBounds bounds) {
            return this.kind == bounds.kind;
        }

        public boolean isAssignableFrom(ResolvableType... types) {
            for (ResolvableType bound : this.bounds) {
                for (ResolvableType type : types) {
                    if (!isAssignable(bound, type)) {
                        return false;
                    }
                }
            }
            return true;
        }

        private boolean isAssignable(ResolvableType source, ResolvableType from) {
            return this.kind == Kind.UPPER ? source.isAssignableFrom(from) : from.isAssignableFrom(source);
        }

        public ResolvableType[] getBounds() {
            return this.bounds;
        }

        public static WildcardBounds get(ResolvableType type) {
            ResolvableType resolvableTypeResolveType = type;
            while (true) {
                ResolvableType resolveToWildcard = resolvableTypeResolveType;
                if (resolveToWildcard.getType() instanceof WildcardType) {
                    WildcardType wildcardType = (WildcardType) resolveToWildcard.type;
                    Kind boundsType = wildcardType.getLowerBounds().length > 0 ? Kind.LOWER : Kind.UPPER;
                    Type[] bounds = boundsType == Kind.UPPER ? wildcardType.getUpperBounds() : wildcardType.getLowerBounds();
                    ResolvableType[] resolvableBounds = new ResolvableType[bounds.length];
                    for (int i = 0; i < bounds.length; i++) {
                        resolvableBounds[i] = ResolvableType.forType(bounds[i], type.variableResolver);
                    }
                    return new WildcardBounds(boundsType, resolvableBounds);
                }
                if (resolveToWildcard == ResolvableType.NONE) {
                    return null;
                }
                resolvableTypeResolveType = resolveToWildcard.resolveType();
            }
        }
    }
}
