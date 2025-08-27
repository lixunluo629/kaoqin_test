package org.springframework.data.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/ClassTypeInformation.class */
public class ClassTypeInformation<S> extends TypeDiscoverer<S> {
    public static final ClassTypeInformation<Collection> COLLECTION = new ClassTypeInformation<>(Collection.class);
    public static final ClassTypeInformation<List> LIST = new ClassTypeInformation<>(List.class);
    public static final ClassTypeInformation<Set> SET = new ClassTypeInformation<>(Set.class);
    public static final ClassTypeInformation<Map> MAP = new ClassTypeInformation<>(Map.class);
    public static final ClassTypeInformation<Object> OBJECT = new ClassTypeInformation<>(Object.class);
    private static final Map<Class<?>, Reference<ClassTypeInformation<?>>> CACHE = Collections.synchronizedMap(new WeakHashMap());
    private final Class<S> type;

    @Override // org.springframework.data.util.TypeDiscoverer
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // org.springframework.data.util.TypeDiscoverer
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ List getTypeArguments() {
        return super.getTypeArguments();
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ TypeInformation getSuperTypeInformation(Class cls) {
        return super.getSuperTypeInformation(cls);
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ List getParameterTypes(Method method) {
        return super.getParameterTypes(method);
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ TypeInformation getReturnType(Method method) {
        return super.getReturnType(method);
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ boolean isCollectionLike() {
        return super.isCollectionLike();
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ TypeInformation getMapValueType() {
        return super.getMapValueType();
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ boolean isMap() {
        return super.isMap();
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ TypeInformation getActualType() {
        return super.getActualType();
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ TypeInformation getProperty(String str) {
        return super.getProperty(str);
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ List getParameterTypes(Constructor constructor) {
        return super.getParameterTypes((Constructor<?>) constructor);
    }

    static {
        for (ClassTypeInformation<?> info : Arrays.asList(COLLECTION, LIST, SET, MAP, OBJECT)) {
            CACHE.put(info.getType(), new WeakReference(info));
        }
    }

    public static <S> ClassTypeInformation<S> from(Class<S> type) {
        Assert.notNull(type, "Type must not be null!");
        Reference<ClassTypeInformation<?>> cachedReference = CACHE.get(type);
        TypeInformation<?> cachedTypeInfo = cachedReference == null ? null : (ClassTypeInformation) cachedReference.get();
        if (cachedTypeInfo != null) {
            return (ClassTypeInformation) cachedTypeInfo;
        }
        ClassTypeInformation<S> result = new ClassTypeInformation<>(type);
        CACHE.put(type, new WeakReference(result));
        return result;
    }

    public static <S> TypeInformation<S> fromReturnTypeOf(Method method) {
        Assert.notNull(method, "Method must not be null!");
        return (TypeInformation<S>) from(method.getDeclaringClass()).createInfo(method.getGenericReturnType());
    }

    ClassTypeInformation(Class<S> type) {
        super(ProxyUtils.getUserClass((Class<?>) type), getTypeVariableMap(type));
        this.type = type;
    }

    private static Map<TypeVariable<?>, Type> getTypeVariableMap(Class<?> type) {
        return getTypeVariableMap(type, new HashSet());
    }

    private static Map<TypeVariable<?>, Type> getTypeVariableMap(Class<?> type, Collection<Type> visited) {
        if (visited.contains(type)) {
            return Collections.emptyMap();
        }
        visited.add(type);
        Map<TypeVariable, Type> source = GenericTypeResolver.getTypeVariableMap(type);
        Map<TypeVariable<?>, Type> map = new HashMap<>(source.size());
        for (Map.Entry<TypeVariable, Type> entry : source.entrySet()) {
            Type value = entry.getValue();
            map.put(entry.getKey(), entry.getValue());
            if (value instanceof Class) {
                for (Map.Entry<TypeVariable<?>, Type> nestedEntry : getTypeVariableMap((Class) value, visited).entrySet()) {
                    if (!map.containsKey(nestedEntry.getKey())) {
                        map.put(nestedEntry.getKey(), nestedEntry.getValue());
                    }
                }
            }
        }
        return map;
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public Class<S> getType() {
        return this.type;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public ClassTypeInformation<?> getRawTypeInformation() {
        return this;
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public boolean isAssignableFrom(TypeInformation<?> target) {
        return getType().isAssignableFrom(target.getType());
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public TypeInformation<?> specialize(ClassTypeInformation<?> type) {
        return type;
    }

    public String toString() {
        return this.type.getName();
    }
}
