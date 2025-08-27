package org.springframework.data.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/GenericArrayTypeInformation.class */
class GenericArrayTypeInformation<S> extends ParentTypeAwareTypeInformation<S> {
    private final GenericArrayType type;

    protected GenericArrayTypeInformation(GenericArrayType type, TypeDiscoverer<?> parent) {
        super(type, parent);
        this.type = type;
    }

    @Override // org.springframework.data.util.ParentTypeAwareTypeInformation, org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public Class<S> getType() {
        return (Class<S>) Array.newInstance((Class<?>) resolveType(this.type.getGenericComponentType()), 0).getClass();
    }

    @Override // org.springframework.data.util.TypeDiscoverer
    protected TypeInformation<?> doGetComponentType() {
        Type componentType = this.type.getGenericComponentType();
        return createInfo(componentType);
    }

    public String toString() {
        return this.type.toString();
    }
}
