package org.springframework.data.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/ParentTypeAwareTypeInformation.class */
public abstract class ParentTypeAwareTypeInformation<S> extends TypeDiscoverer<S> {
    private final TypeDiscoverer<?> parent;
    private int hashCode;

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ TypeInformation specialize(ClassTypeInformation classTypeInformation) {
        return super.specialize(classTypeInformation);
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ boolean isAssignableFrom(TypeInformation typeInformation) {
        return super.isAssignableFrom(typeInformation);
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
    public /* bridge */ /* synthetic */ ClassTypeInformation getRawTypeInformation() {
        return super.getRawTypeInformation();
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ Class getType() {
        return super.getType();
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ TypeInformation getProperty(String str) {
        return super.getProperty(str);
    }

    @Override // org.springframework.data.util.TypeDiscoverer, org.springframework.data.util.TypeInformation
    public /* bridge */ /* synthetic */ List getParameterTypes(Constructor constructor) {
        return super.getParameterTypes((Constructor<?>) constructor);
    }

    protected ParentTypeAwareTypeInformation(Type type, TypeDiscoverer<?> parent) {
        this(type, parent, parent.getTypeVariableMap());
    }

    protected ParentTypeAwareTypeInformation(Type type, TypeDiscoverer<?> parent, Map<TypeVariable<?>, Type> typeVariables) {
        super(type, typeVariables);
        this.parent = parent;
    }

    @Override // org.springframework.data.util.TypeDiscoverer
    protected TypeInformation<?> createInfo(Type fieldType) {
        if (this.parent.getType().equals(fieldType)) {
            return this.parent;
        }
        return super.createInfo(fieldType);
    }

    @Override // org.springframework.data.util.TypeDiscoverer
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !getClass().equals(obj.getClass())) {
            return false;
        }
        ParentTypeAwareTypeInformation<?> that = (ParentTypeAwareTypeInformation) obj;
        return this.parent == null ? that.parent == null : this.parent.equals(that.parent);
    }

    @Override // org.springframework.data.util.TypeDiscoverer
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = super.hashCode() + (31 * this.parent.hashCode());
        }
        return this.hashCode;
    }
}
