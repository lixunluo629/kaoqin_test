package org.springframework.core.convert;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/TypeDescriptor.class */
public class TypeDescriptor implements Serializable {
    static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
    private static final boolean streamAvailable = ClassUtils.isPresent("java.util.stream.Stream", TypeDescriptor.class.getClassLoader());
    private static final Map<Class<?>, TypeDescriptor> commonTypesCache = new HashMap(32);
    private static final Class<?>[] CACHED_COMMON_TYPES = {Boolean.TYPE, Boolean.class, Byte.TYPE, Byte.class, Character.TYPE, Character.class, Double.TYPE, Double.class, Float.TYPE, Float.class, Integer.TYPE, Integer.class, Long.TYPE, Long.class, Short.TYPE, Short.class, String.class, Object.class};
    private final Class<?> type;
    private final ResolvableType resolvableType;
    private final AnnotatedElementAdapter annotatedElement;

    static {
        for (Class<?> preCachedClass : CACHED_COMMON_TYPES) {
            commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
        }
    }

    public TypeDescriptor(MethodParameter methodParameter) {
        this.resolvableType = ResolvableType.forMethodParameter(methodParameter);
        this.type = this.resolvableType.resolve(methodParameter.getNestedParameterType());
        this.annotatedElement = new AnnotatedElementAdapter(methodParameter.getParameterIndex() == -1 ? methodParameter.getMethodAnnotations() : methodParameter.getParameterAnnotations());
    }

    public TypeDescriptor(Field field) {
        this.resolvableType = ResolvableType.forField(field);
        this.type = this.resolvableType.resolve(field.getType());
        this.annotatedElement = new AnnotatedElementAdapter(field.getAnnotations());
    }

    public TypeDescriptor(Property property) {
        Assert.notNull(property, "Property must not be null");
        this.resolvableType = ResolvableType.forMethodParameter(property.getMethodParameter());
        this.type = this.resolvableType.resolve(property.getType());
        this.annotatedElement = new AnnotatedElementAdapter(property.getAnnotations());
    }

    protected TypeDescriptor(ResolvableType resolvableType, Class<?> type, Annotation[] annotations) {
        this.resolvableType = resolvableType;
        this.type = type != null ? type : resolvableType.resolve(Object.class);
        this.annotatedElement = new AnnotatedElementAdapter(annotations);
    }

    public Class<?> getObjectType() {
        return ClassUtils.resolvePrimitiveIfNecessary(getType());
    }

    public Class<?> getType() {
        return this.type;
    }

    public ResolvableType getResolvableType() {
        return this.resolvableType;
    }

    public Object getSource() {
        if (this.resolvableType != null) {
            return this.resolvableType.getSource();
        }
        return null;
    }

    public TypeDescriptor narrow(Object value) {
        if (value == null) {
            return this;
        }
        ResolvableType narrowed = ResolvableType.forType(value.getClass(), getResolvableType());
        return new TypeDescriptor(narrowed, value.getClass(), getAnnotations());
    }

    public TypeDescriptor upcast(Class<?> superType) {
        if (superType == null) {
            return null;
        }
        Assert.isAssignable(superType, getType());
        return new TypeDescriptor(getResolvableType().as(superType), superType, getAnnotations());
    }

    public String getName() {
        return ClassUtils.getQualifiedName(getType());
    }

    public boolean isPrimitive() {
        return getType().isPrimitive();
    }

    public Annotation[] getAnnotations() {
        return this.annotatedElement.getAnnotations();
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        if (this.annotatedElement.isEmpty()) {
            return false;
        }
        return AnnotatedElementUtils.isAnnotated(this.annotatedElement, annotationType);
    }

    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        if (this.annotatedElement.isEmpty()) {
            return null;
        }
        return (T) AnnotatedElementUtils.getMergedAnnotation(this.annotatedElement, cls);
    }

    public boolean isAssignableTo(TypeDescriptor typeDescriptor) {
        boolean typesAssignable = typeDescriptor.getObjectType().isAssignableFrom(getObjectType());
        if (!typesAssignable) {
            return false;
        }
        if (isArray() && typeDescriptor.isArray()) {
            return getElementTypeDescriptor().isAssignableTo(typeDescriptor.getElementTypeDescriptor());
        }
        if (isCollection() && typeDescriptor.isCollection()) {
            return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
        }
        if (isMap() && typeDescriptor.isMap()) {
            return isNestedAssignable(getMapKeyTypeDescriptor(), typeDescriptor.getMapKeyTypeDescriptor()) && isNestedAssignable(getMapValueTypeDescriptor(), typeDescriptor.getMapValueTypeDescriptor());
        }
        return true;
    }

    private boolean isNestedAssignable(TypeDescriptor nestedTypeDescriptor, TypeDescriptor otherNestedTypeDescriptor) {
        if (nestedTypeDescriptor == null || otherNestedTypeDescriptor == null) {
            return true;
        }
        return nestedTypeDescriptor.isAssignableTo(otherNestedTypeDescriptor);
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(getType());
    }

    public boolean isArray() {
        return getType().isArray();
    }

    public TypeDescriptor getElementTypeDescriptor() {
        if (getResolvableType().isArray()) {
            return new TypeDescriptor(getResolvableType().getComponentType(), null, getAnnotations());
        }
        if (streamAvailable && StreamDelegate.isStream(getType())) {
            return StreamDelegate.getStreamElementType(this);
        }
        return getRelatedIfResolvable(this, getResolvableType().asCollection().getGeneric(0));
    }

    public TypeDescriptor elementTypeDescriptor(Object element) {
        return narrow(element, getElementTypeDescriptor());
    }

    public boolean isMap() {
        return Map.class.isAssignableFrom(getType());
    }

    public TypeDescriptor getMapKeyTypeDescriptor() {
        Assert.state(isMap(), "Not a [java.util.Map]");
        return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(0));
    }

    public TypeDescriptor getMapKeyTypeDescriptor(Object mapKey) {
        return narrow(mapKey, getMapKeyTypeDescriptor());
    }

    public TypeDescriptor getMapValueTypeDescriptor() {
        Assert.state(isMap(), "Not a [java.util.Map]");
        return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(1));
    }

    public TypeDescriptor getMapValueTypeDescriptor(Object mapValue) {
        return narrow(mapValue, getMapValueTypeDescriptor());
    }

    private TypeDescriptor narrow(Object value, TypeDescriptor typeDescriptor) {
        if (typeDescriptor != null) {
            return typeDescriptor.narrow(value);
        }
        if (value != null) {
            return narrow(value);
        }
        return null;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TypeDescriptor)) {
            return false;
        }
        TypeDescriptor otherDesc = (TypeDescriptor) other;
        if (getType() != otherDesc.getType() || !annotationsMatch(otherDesc)) {
            return false;
        }
        if (isCollection() || isArray()) {
            return ObjectUtils.nullSafeEquals(getElementTypeDescriptor(), otherDesc.getElementTypeDescriptor());
        }
        if (isMap()) {
            return ObjectUtils.nullSafeEquals(getMapKeyTypeDescriptor(), otherDesc.getMapKeyTypeDescriptor()) && ObjectUtils.nullSafeEquals(getMapValueTypeDescriptor(), otherDesc.getMapValueTypeDescriptor());
        }
        return true;
    }

    private boolean annotationsMatch(TypeDescriptor otherDesc) {
        Annotation[] anns = getAnnotations();
        Annotation[] otherAnns = otherDesc.getAnnotations();
        if (anns == otherAnns) {
            return true;
        }
        if (anns.length != otherAnns.length) {
            return false;
        }
        if (anns.length > 0) {
            for (int i = 0; i < anns.length; i++) {
                if (!annotationEquals(anns[i], otherAnns[i])) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    private boolean annotationEquals(Annotation ann, Annotation otherAnn) {
        return ann == otherAnn || (ann.getClass() == otherAnn.getClass() && ann.equals(otherAnn));
    }

    public int hashCode() {
        return getType().hashCode();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Annotation ann : getAnnotations()) {
            builder.append("@").append(ann.annotationType().getName()).append(' ');
        }
        builder.append(getResolvableType().toString());
        return builder.toString();
    }

    public static TypeDescriptor forObject(Object source) {
        if (source != null) {
            return valueOf(source.getClass());
        }
        return null;
    }

    public static TypeDescriptor valueOf(Class<?> type) {
        if (type == null) {
            type = Object.class;
        }
        TypeDescriptor desc = commonTypesCache.get(type);
        return desc != null ? desc : new TypeDescriptor(ResolvableType.forClass(type), null, null);
    }

    public static TypeDescriptor collection(Class<?> collectionType, TypeDescriptor elementTypeDescriptor) {
        Assert.notNull(collectionType, "Collection type must not be null");
        if (!Collection.class.isAssignableFrom(collectionType)) {
            throw new IllegalArgumentException("Collection type must be a [java.util.Collection]");
        }
        ResolvableType element = elementTypeDescriptor != null ? elementTypeDescriptor.resolvableType : null;
        return new TypeDescriptor(ResolvableType.forClassWithGenerics(collectionType, element), null, null);
    }

    public static TypeDescriptor map(Class<?> mapType, TypeDescriptor keyTypeDescriptor, TypeDescriptor valueTypeDescriptor) {
        Assert.notNull(mapType, "Map type must not be null");
        if (!Map.class.isAssignableFrom(mapType)) {
            throw new IllegalArgumentException("Map type must be a [java.util.Map]");
        }
        ResolvableType key = keyTypeDescriptor != null ? keyTypeDescriptor.resolvableType : null;
        ResolvableType value = valueTypeDescriptor != null ? valueTypeDescriptor.resolvableType : null;
        return new TypeDescriptor(ResolvableType.forClassWithGenerics(mapType, key, value), null, null);
    }

    public static TypeDescriptor array(TypeDescriptor elementTypeDescriptor) {
        if (elementTypeDescriptor == null) {
            return null;
        }
        return new TypeDescriptor(ResolvableType.forArrayComponent(elementTypeDescriptor.resolvableType), null, elementTypeDescriptor.getAnnotations());
    }

    public static TypeDescriptor nested(MethodParameter methodParameter, int nestingLevel) {
        if (methodParameter.getNestingLevel() != 1) {
            throw new IllegalArgumentException("MethodParameter nesting level must be 1: use the nestingLevel parameter to specify the desired nestingLevel for nested type traversal");
        }
        return nested(new TypeDescriptor(methodParameter), nestingLevel);
    }

    public static TypeDescriptor nested(Field field, int nestingLevel) {
        return nested(new TypeDescriptor(field), nestingLevel);
    }

    public static TypeDescriptor nested(Property property, int nestingLevel) {
        return nested(new TypeDescriptor(property), nestingLevel);
    }

    private static TypeDescriptor nested(TypeDescriptor typeDescriptor, int nestingLevel) {
        ResolvableType nested = typeDescriptor.resolvableType;
        for (int i = 0; i < nestingLevel; i++) {
            if (Object.class != nested.getType()) {
                nested = nested.getNested(2);
            }
        }
        if (nested == ResolvableType.NONE) {
            return null;
        }
        return getRelatedIfResolvable(typeDescriptor, nested);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static TypeDescriptor getRelatedIfResolvable(TypeDescriptor source, ResolvableType type) {
        if (type.resolve() == null) {
            return null;
        }
        return new TypeDescriptor(type, null, source.getAnnotations());
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/TypeDescriptor$AnnotatedElementAdapter.class */
    private class AnnotatedElementAdapter implements AnnotatedElement, Serializable {
        private final Annotation[] annotations;

        public AnnotatedElementAdapter(Annotation[] annotations) {
            this.annotations = annotations;
        }

        @Override // java.lang.reflect.AnnotatedElement
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            for (Annotation annotation : getAnnotations()) {
                if (annotation.annotationType() == annotationClass) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.lang.reflect.AnnotatedElement
        public <T extends Annotation> T getAnnotation(Class<T> cls) {
            for (Annotation annotation : getAnnotations()) {
                T t = (T) annotation;
                if (t.annotationType() == cls) {
                    return t;
                }
            }
            return null;
        }

        @Override // java.lang.reflect.AnnotatedElement
        public Annotation[] getAnnotations() {
            return this.annotations != null ? this.annotations : TypeDescriptor.EMPTY_ANNOTATION_ARRAY;
        }

        @Override // java.lang.reflect.AnnotatedElement
        public Annotation[] getDeclaredAnnotations() {
            return getAnnotations();
        }

        public boolean isEmpty() {
            return ObjectUtils.isEmpty((Object[]) this.annotations);
        }

        public boolean equals(Object other) {
            return this == other || ((other instanceof AnnotatedElementAdapter) && Arrays.equals(this.annotations, ((AnnotatedElementAdapter) other).annotations));
        }

        public int hashCode() {
            return Arrays.hashCode(this.annotations);
        }

        public String toString() {
            return TypeDescriptor.this.toString();
        }
    }

    @UsesJava8
    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/TypeDescriptor$StreamDelegate.class */
    private static class StreamDelegate {
        private StreamDelegate() {
        }

        public static boolean isStream(Class<?> type) {
            return Stream.class.isAssignableFrom(type);
        }

        public static TypeDescriptor getStreamElementType(TypeDescriptor source) {
            return TypeDescriptor.getRelatedIfResolvable(source, source.getResolvableType().as(Stream.class).getGeneric(0));
        }
    }
}
