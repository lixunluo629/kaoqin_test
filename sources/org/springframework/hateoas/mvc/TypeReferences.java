package org.springframework.hateoas.mvc;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/TypeReferences.class */
public class TypeReferences {

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/TypeReferences$ResourceType.class */
    public static class ResourceType<T> extends SyntheticParameterizedTypeReference<Resource<T>> {
        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ Type getType() {
            return super.getType();
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/TypeReferences$ResourcesType.class */
    public static class ResourcesType<T> extends SyntheticParameterizedTypeReference<Resources<T>> {
        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ Type getType() {
            return super.getType();
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/TypeReferences$PagedResourcesType.class */
    public static class PagedResourcesType<T> extends SyntheticParameterizedTypeReference<PagedResources<T>> {
        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override // org.springframework.hateoas.mvc.TypeReferences.SyntheticParameterizedTypeReference, org.springframework.core.ParameterizedTypeReference
        public /* bridge */ /* synthetic */ Type getType() {
            return super.getType();
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/TypeReferences$SyntheticParameterizedTypeReference.class */
    private static abstract class SyntheticParameterizedTypeReference<T> extends ParameterizedTypeReference<T> {
        private final Type type;

        protected SyntheticParameterizedTypeReference() {
            Type genericSuperclass = getClass().getGenericSuperclass();
            ParameterizedType bar = (ParameterizedType) genericSuperclass;
            Type domainType = bar.getActualTypeArguments()[0];
            Class<?> parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(getClass());
            Type type = parameterizedTypeReferenceSubclass.getGenericSuperclass();
            Assert.isInstanceOf(ParameterizedType.class, type);
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Assert.isTrue(parameterizedType.getActualTypeArguments().length == 1);
            Class<?> resourceType = GenericTypeResolver.resolveType(parameterizedType.getActualTypeArguments()[0], new HashMap());
            this.type = new SyntheticParameterizedType(resourceType, domainType);
        }

        @Override // org.springframework.core.ParameterizedTypeReference
        public Type getType() {
            return this.type;
        }

        @Override // org.springframework.core.ParameterizedTypeReference
        public boolean equals(Object obj) {
            return this == obj || ((obj instanceof SyntheticParameterizedTypeReference) && this.type.equals(((SyntheticParameterizedTypeReference) obj).type));
        }

        @Override // org.springframework.core.ParameterizedTypeReference
        public int hashCode() {
            return this.type.hashCode();
        }

        @Override // org.springframework.core.ParameterizedTypeReference
        public String toString() {
            return "SyntheticParameterizedTypeReference<" + this.type + ">";
        }

        private static Class<?> findParameterizedTypeReferenceSubclass(Class<?> child) {
            Class<?> parent = child.getSuperclass();
            if (Object.class.equals(parent)) {
                throw new IllegalStateException("Expected SyntheticParameterizedTypeReference superclass");
            }
            if (SyntheticParameterizedTypeReference.class.equals(parent)) {
                return child;
            }
            return findParameterizedTypeReferenceSubclass(parent);
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/TypeReferences$SyntheticParameterizedType.class */
    private static final class SyntheticParameterizedType implements ParameterizedType, Serializable {
        private static final long serialVersionUID = -521679299810654826L;
        private final Type rawType;
        private final Type[] typeArguments;

        public SyntheticParameterizedType(Type rawType, Type... typeArguments) {
            this.rawType = rawType;
            this.typeArguments = typeArguments;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            return this.typeArguments;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.rawType;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getOwnerType() {
            return null;
        }
    }
}
