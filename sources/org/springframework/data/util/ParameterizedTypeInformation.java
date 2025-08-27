package org.springframework.data.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/ParameterizedTypeInformation.class */
class ParameterizedTypeInformation<T> extends ParentTypeAwareTypeInformation<T> {
    private final ParameterizedType type;
    private Boolean resolved;

    public ParameterizedTypeInformation(ParameterizedType type, TypeDiscoverer<?> parent) {
        super(type, parent, calculateTypeVariables(type, parent));
        this.type = type;
    }

    @Override // org.springframework.data.util.TypeDiscoverer
    protected TypeInformation<?> doGetMapValueType() {
        if (Map.class.isAssignableFrom(getType())) {
            Type[] arguments = this.type.getActualTypeArguments();
            if (arguments.length > 1) {
                return createInfo(arguments[1]);
            }
        }
        Class<?> rawType = getType();
        Set<Type> supertypes = new HashSet<>();
        supertypes.add(rawType.getGenericSuperclass());
        supertypes.addAll(Arrays.asList(rawType.getGenericInterfaces()));
        for (Type supertype : supertypes) {
            if (Map.class.isAssignableFrom(resolveType(supertype))) {
                ParameterizedType parameterizedSupertype = (ParameterizedType) supertype;
                return createInfo(parameterizedSupertype.getActualTypeArguments()[1]);
            }
        }
        return super.doGetMapValueType();
    }

    @Override // org.springframework.data.util.ParentTypeAwareTypeInformation, org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public List<TypeInformation<?>> getTypeArguments() {
        List<TypeInformation<?>> result = new ArrayList<>();
        for (Type argument : this.type.getActualTypeArguments()) {
            result.add(createInfo(argument));
        }
        return result;
    }

    @Override // org.springframework.data.util.ParentTypeAwareTypeInformation, org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public boolean isAssignableFrom(TypeInformation<?> target) {
        if (equals(target)) {
            return true;
        }
        Class<?> type = getType();
        Class<?> rawTargetType = target.getType();
        if (!type.isAssignableFrom(rawTargetType)) {
            return false;
        }
        TypeInformation<?> otherTypeInformation = type.equals(rawTargetType) ? target : target.getSuperTypeInformation(type);
        List<TypeInformation<?>> myParameters = getTypeArguments();
        List<TypeInformation<?>> typeParameters = otherTypeInformation.getTypeArguments();
        if (myParameters.size() != typeParameters.size()) {
            return false;
        }
        for (int i = 0; i < myParameters.size(); i++) {
            if (!myParameters.get(i).isAssignableFrom(typeParameters.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override // org.springframework.data.util.TypeDiscoverer
    protected TypeInformation<?> doGetComponentType() {
        return createInfo(this.type.getActualTypeArguments()[0]);
    }

    @Override // org.springframework.data.util.ParentTypeAwareTypeInformation, org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public TypeInformation<?> specialize(ClassTypeInformation<?> type) {
        return isResolvedCompletely() ? type : super.specialize(type);
    }

    @Override // org.springframework.data.util.ParentTypeAwareTypeInformation, org.springframework.data.util.TypeDiscoverer
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ParameterizedTypeInformation)) {
            return false;
        }
        ParameterizedTypeInformation<?> that = (ParameterizedTypeInformation) obj;
        if (isResolvedCompletely() && that.isResolvedCompletely()) {
            return this.type.equals(that.type);
        }
        return super.equals(obj);
    }

    @Override // org.springframework.data.util.ParentTypeAwareTypeInformation, org.springframework.data.util.TypeDiscoverer
    public int hashCode() {
        return isResolvedCompletely() ? this.type.hashCode() : super.hashCode();
    }

    public String toString() {
        return String.format("%s<%s>", getType().getName(), StringUtils.collectionToCommaDelimitedString(getTypeArguments()));
    }

    private boolean isResolvedCompletely() {
        if (this.resolved != null) {
            return this.resolved.booleanValue();
        }
        Type[] typeArguments = this.type.getActualTypeArguments();
        if (typeArguments.length == 0) {
            return cacheAndReturn(false);
        }
        for (Type typeArgument : typeArguments) {
            TypeInformation<?> info = createInfo(typeArgument);
            if ((info instanceof ParameterizedTypeInformation) && !((ParameterizedTypeInformation) info).isResolvedCompletely()) {
                return cacheAndReturn(false);
            }
            if (!(info instanceof ClassTypeInformation)) {
                return cacheAndReturn(false);
            }
        }
        return cacheAndReturn(true);
    }

    private boolean cacheAndReturn(boolean resolved) {
        this.resolved = Boolean.valueOf(resolved);
        return resolved;
    }

    private static Map<TypeVariable<?>, Type> calculateTypeVariables(ParameterizedType type, TypeDiscoverer<?> parent) {
        Class<?> resolvedType = parent.resolveType(type);
        TypeVariable<?>[] typeParameters = resolvedType.getTypeParameters();
        Type[] arguments = type.getActualTypeArguments();
        Map<TypeVariable<?>, Type> localTypeVariables = new HashMap<>(parent.getTypeVariableMap());
        for (int i = 0; i < typeParameters.length; i++) {
            localTypeVariables.put(typeParameters[i], flattenTypeVariable(arguments[i], localTypeVariables));
        }
        return localTypeVariables;
    }

    private static Type flattenTypeVariable(Type source, Map<TypeVariable<?>, Type> variables) {
        if (!(source instanceof TypeVariable)) {
            return source;
        }
        Type value = variables.get(source);
        return value == null ? source : flattenTypeVariable(value, variables);
    }
}
