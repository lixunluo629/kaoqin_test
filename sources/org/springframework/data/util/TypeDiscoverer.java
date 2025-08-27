package org.springframework.data.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/TypeDiscoverer.class */
class TypeDiscoverer<S> implements TypeInformation<S> {
    private static final Iterable<Class<?>> MAP_TYPES;
    private final Type type;
    private final Map<TypeVariable<?>, Type> typeVariableMap;
    private final int hashCode;
    private TypeInformation<?> componentType;
    private TypeInformation<?> valueType;
    private Class<S> resolvedType;
    private final Map<String, ValueHolder> fieldTypes = new ConcurrentHashMap();
    private boolean componentTypeResolved = false;
    private boolean valueTypeResolved = false;

    static {
        Set<Class<?>> mapTypes = new HashSet<>();
        mapTypes.add(Map.class);
        tryToAddClassTo("javaslang.collection.Map", mapTypes);
        tryToAddClassTo("io.vavr.collection.Map", mapTypes);
        MAP_TYPES = Collections.unmodifiableSet(mapTypes);
    }

    protected TypeDiscoverer(Type type, Map<TypeVariable<?>, Type> typeVariableMap) {
        Assert.notNull(type, "Type must not be null!");
        Assert.notNull(typeVariableMap, "TypeVariableMap must not be null!");
        this.type = type;
        this.typeVariableMap = typeVariableMap;
        this.hashCode = 17 + (31 * type.hashCode()) + (31 * typeVariableMap.hashCode());
    }

    protected Map<TypeVariable<?>, Type> getTypeVariableMap() {
        return this.typeVariableMap;
    }

    protected TypeInformation<?> createInfo(Type fieldType) {
        if (fieldType.equals(this.type)) {
            return this;
        }
        if (fieldType instanceof Class) {
            return ClassTypeInformation.from((Class) fieldType);
        }
        if (fieldType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) fieldType;
            return new ParameterizedTypeInformation(parameterizedType, this);
        }
        if (fieldType instanceof TypeVariable) {
            TypeVariable<?> variable = (TypeVariable) fieldType;
            return new TypeVariableTypeInformation(variable, this);
        }
        if (fieldType instanceof GenericArrayType) {
            return new GenericArrayTypeInformation((GenericArrayType) fieldType, this);
        }
        if (fieldType instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) fieldType;
            Type[] bounds = wildcardType.getLowerBounds();
            if (bounds.length > 0) {
                return createInfo(bounds[0]);
            }
            Type[] bounds2 = wildcardType.getUpperBounds();
            if (bounds2.length > 0) {
                return createInfo(bounds2[0]);
            }
        }
        throw new IllegalArgumentException();
    }

    protected Class<S> resolveType(Type type) {
        HashMap map = new HashMap();
        map.putAll(getTypeVariableMap());
        return (Class<S>) GenericTypeResolver.resolveType(type, map);
    }

    @Override // org.springframework.data.util.TypeInformation
    public List<TypeInformation<?>> getParameterTypes(Constructor<?> constructor) {
        Assert.notNull(constructor, "Constructor must not be null!");
        Type[] types = constructor.getGenericParameterTypes();
        List<TypeInformation<?>> result = new ArrayList<>(types.length);
        for (Type parameterType : types) {
            result.add(createInfo(parameterType));
        }
        return result;
    }

    @Override // org.springframework.data.util.TypeInformation
    public TypeInformation<?> getProperty(String fieldname) throws BeansException {
        int separatorIndex = fieldname.indexOf(46);
        if (separatorIndex == -1) {
            if (this.fieldTypes.containsKey(fieldname)) {
                return this.fieldTypes.get(fieldname).getType();
            }
            TypeInformation<?> propertyInformation = getPropertyInformation(fieldname);
            this.fieldTypes.put(fieldname, ValueHolder.of(propertyInformation));
            return propertyInformation;
        }
        String head = fieldname.substring(0, separatorIndex);
        TypeInformation<?> info = getProperty(head);
        if (info == null) {
            return null;
        }
        return info.getProperty(fieldname.substring(separatorIndex + 1));
    }

    private TypeInformation<?> getPropertyInformation(String fieldname) throws BeansException {
        Class<S> type = getType();
        Field field = org.springframework.util.ReflectionUtils.findField(type, fieldname);
        if (field != null) {
            return createInfo(field.getGenericType());
        }
        PropertyDescriptor descriptor = findPropertyDescriptor(type, fieldname);
        if (descriptor == null) {
            return null;
        }
        return createInfo(getGenericType(descriptor));
    }

    private static PropertyDescriptor findPropertyDescriptor(Class<?> type, String fieldname) throws BeansException {
        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(type, fieldname);
        if (descriptor != null) {
            return descriptor;
        }
        List<Class<?>> superTypes = new ArrayList<>();
        superTypes.addAll(Arrays.asList(type.getInterfaces()));
        superTypes.add(type.getSuperclass());
        for (Class<?> interfaceType : type.getInterfaces()) {
            PropertyDescriptor descriptor2 = findPropertyDescriptor(interfaceType, fieldname);
            if (descriptor2 != null) {
                return descriptor2;
            }
        }
        return null;
    }

    private static Type getGenericType(PropertyDescriptor descriptor) {
        Method method = descriptor.getReadMethod();
        if (method != null) {
            return method.getGenericReturnType();
        }
        Method method2 = descriptor.getWriteMethod();
        if (method2 == null) {
            return null;
        }
        Type[] parameterTypes = method2.getGenericParameterTypes();
        if (parameterTypes.length == 0) {
            return null;
        }
        return parameterTypes[0];
    }

    @Override // org.springframework.data.util.TypeInformation
    public Class<S> getType() {
        if (this.resolvedType == null) {
            this.resolvedType = resolveType(this.type);
        }
        return this.resolvedType;
    }

    @Override // org.springframework.data.util.TypeInformation
    public ClassTypeInformation<?> getRawTypeInformation() {
        return ClassTypeInformation.from(getType()).getRawTypeInformation();
    }

    @Override // org.springframework.data.util.TypeInformation
    public TypeInformation<?> getActualType() {
        if (isMap()) {
            return getMapValueType();
        }
        if (isCollectionLike()) {
            return getComponentType();
        }
        return this;
    }

    @Override // org.springframework.data.util.TypeInformation
    public boolean isMap() {
        for (Class<?> mapType : MAP_TYPES) {
            if (mapType.isAssignableFrom(getType())) {
                return true;
            }
        }
        return false;
    }

    @Override // org.springframework.data.util.TypeInformation
    public TypeInformation<?> getMapValueType() {
        if (!this.valueTypeResolved) {
            this.valueType = doGetMapValueType();
            this.valueTypeResolved = true;
        }
        return this.valueType;
    }

    protected TypeInformation<?> doGetMapValueType() {
        if (isMap()) {
            return getTypeArgument(getBaseType(MAP_TYPES), 1);
        }
        List<TypeInformation<?>> arguments = getTypeArguments();
        if (arguments.size() > 1) {
            return arguments.get(1);
        }
        return null;
    }

    @Override // org.springframework.data.util.TypeInformation
    public boolean isCollectionLike() {
        Class<S> type = getType();
        if (type.isArray() || Iterable.class.equals(type)) {
            return true;
        }
        return Collection.class.isAssignableFrom(type);
    }

    @Override // org.springframework.data.util.TypeInformation
    public final TypeInformation<?> getComponentType() {
        if (!this.componentTypeResolved) {
            this.componentType = doGetComponentType();
            this.componentTypeResolved = true;
        }
        return this.componentType;
    }

    protected TypeInformation<?> doGetComponentType() {
        Class<S> rawType = getType();
        if (rawType.isArray()) {
            return createInfo(rawType.getComponentType());
        }
        if (isMap()) {
            return getTypeArgument(getBaseType(MAP_TYPES), 0);
        }
        if (Iterable.class.isAssignableFrom(rawType)) {
            return getTypeArgument(Iterable.class, 0);
        }
        List<TypeInformation<?>> arguments = getTypeArguments();
        if (arguments.size() > 0) {
            return arguments.get(0);
        }
        return null;
    }

    @Override // org.springframework.data.util.TypeInformation
    public TypeInformation<?> getReturnType(Method method) {
        Assert.notNull(method, "Method must not be null!");
        return createInfo(method.getGenericReturnType());
    }

    @Override // org.springframework.data.util.TypeInformation
    public List<TypeInformation<?>> getParameterTypes(Method method) {
        Assert.notNull(method, "Method most not be null!");
        Type[] types = method.getGenericParameterTypes();
        List<TypeInformation<?>> result = new ArrayList<>(types.length);
        for (Type parameterType : types) {
            result.add(createInfo(parameterType));
        }
        return result;
    }

    @Override // org.springframework.data.util.TypeInformation
    public TypeInformation<?> getSuperTypeInformation(Class<?> superType) {
        Class<S> type = getType();
        if (!superType.isAssignableFrom(type)) {
            return null;
        }
        if (getType().equals(superType)) {
            return this;
        }
        List<Type> candidates = new ArrayList<>();
        Type genericSuperclass = type.getGenericSuperclass();
        if (genericSuperclass != null) {
            candidates.add(genericSuperclass);
        }
        candidates.addAll(Arrays.asList(type.getGenericInterfaces()));
        for (Type candidate : candidates) {
            TypeInformation<?> candidateInfo = createInfo(candidate);
            if (superType.equals(candidateInfo.getType())) {
                return candidateInfo;
            }
            TypeInformation<?> nestedSuperType = candidateInfo.getSuperTypeInformation(superType);
            if (nestedSuperType != null) {
                return nestedSuperType;
            }
        }
        return null;
    }

    @Override // org.springframework.data.util.TypeInformation
    public List<TypeInformation<?>> getTypeArguments() {
        return Collections.emptyList();
    }

    @Override // org.springframework.data.util.TypeInformation
    public boolean isAssignableFrom(TypeInformation<?> target) {
        return target.getSuperTypeInformation(getType()).equals(this);
    }

    @Override // org.springframework.data.util.TypeInformation
    public TypeInformation<?> specialize(ClassTypeInformation<?> type) {
        Assert.isTrue(getType().isAssignableFrom(type.getType()), String.format("%s must be assignable from %s", getType(), type.getType()));
        List<TypeInformation<?>> arguments = getTypeArguments();
        return arguments.isEmpty() ? type : createInfo(new SyntheticParamterizedType(type, arguments));
    }

    private TypeInformation<?> getTypeArgument(Class<?> bound, int index) {
        Class<?>[] arguments = GenericTypeResolver.resolveTypeArguments(getType(), bound);
        if (arguments == null) {
            if (getSuperTypeInformation(bound) instanceof ParameterizedTypeInformation) {
                return ClassTypeInformation.OBJECT;
            }
            return null;
        }
        return createInfo(arguments[index]);
    }

    private Class<?> getBaseType(Iterable<Class<?>> candidates) {
        for (Class<?> candidate : candidates) {
            if (candidate.isAssignableFrom(getType())) {
                return candidate;
            }
        }
        throw new IllegalArgumentException(String.format("Type %s not contained in candidates %s!", getType(), candidates));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        TypeDiscoverer<?> that = (TypeDiscoverer) obj;
        return this.type.equals(that.type) && this.typeVariableMap.equals(that.typeVariableMap);
    }

    public int hashCode() {
        return this.hashCode;
    }

    private static final void tryToAddClassTo(String className, Set<Class<?>> classes) {
        try {
            classes.add(ClassUtils.forName(className, TypeDiscoverer.class.getClassLoader()));
        } catch (ClassNotFoundException e) {
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/TypeDiscoverer$SyntheticParamterizedType.class */
    private static class SyntheticParamterizedType implements ParameterizedType {

        @NonNull
        private final ClassTypeInformation<?> typeInformation;

        @NonNull
        private final List<TypeInformation<?>> typeParameters;

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof SyntheticParamterizedType)) {
                return false;
            }
            SyntheticParamterizedType other = (SyntheticParamterizedType) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$typeInformation = this.typeInformation;
            Object other$typeInformation = other.typeInformation;
            if (this$typeInformation == null) {
                if (other$typeInformation != null) {
                    return false;
                }
            } else if (!this$typeInformation.equals(other$typeInformation)) {
                return false;
            }
            Object this$typeParameters = this.typeParameters;
            Object other$typeParameters = other.typeParameters;
            return this$typeParameters == null ? other$typeParameters == null : this$typeParameters.equals(other$typeParameters);
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof SyntheticParamterizedType;
        }

        @Generated
        public int hashCode() {
            Object $typeInformation = this.typeInformation;
            int result = (1 * 59) + ($typeInformation == null ? 43 : $typeInformation.hashCode());
            Object $typeParameters = this.typeParameters;
            return (result * 59) + ($typeParameters == null ? 43 : $typeParameters.hashCode());
        }

        @Generated
        public SyntheticParamterizedType(@NonNull ClassTypeInformation<?> typeInformation, @NonNull List<TypeInformation<?>> typeParameters) {
            if (typeInformation == null) {
                throw new IllegalArgumentException("typeInformation is marked @NonNull but is null");
            }
            if (typeParameters == null) {
                throw new IllegalArgumentException("typeParameters is marked @NonNull but is null");
            }
            this.typeInformation = typeInformation;
            this.typeParameters = typeParameters;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.typeInformation.getType();
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getOwnerType() {
            return null;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            Type[] result = new Type[this.typeParameters.size()];
            for (int i = 0; i < this.typeParameters.size(); i++) {
                result[i] = this.typeParameters.get(i).getType();
            }
            return result;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/TypeDiscoverer$ValueHolder.class */
    private static final class ValueHolder {
        static ValueHolder NULL_HOLDER = new ValueHolder(null);
        private final TypeInformation<?> type;

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ValueHolder)) {
                return false;
            }
            ValueHolder other = (ValueHolder) o;
            Object this$type = getType();
            Object other$type = other.getType();
            return this$type == null ? other$type == null : this$type.equals(other$type);
        }

        @Generated
        public int hashCode() {
            Object $type = getType();
            int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "TypeDiscoverer.ValueHolder(type=" + getType() + ")";
        }

        @Generated
        private ValueHolder(TypeInformation<?> type) {
            this.type = type;
        }

        @Generated
        public TypeInformation<?> getType() {
            return this.type;
        }

        public static ValueHolder of(TypeInformation<?> type) {
            return null == type ? NULL_HOLDER : new ValueHolder(type);
        }
    }
}
