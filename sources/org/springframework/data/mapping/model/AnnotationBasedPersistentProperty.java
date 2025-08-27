package org.springframework.data.mapping.model;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Reference;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/AnnotationBasedPersistentProperty.class */
public abstract class AnnotationBasedPersistentProperty<P extends PersistentProperty<P>> extends AbstractPersistentProperty<P> {
    private static final String SPRING_DATA_PACKAGE = "org.springframework.data";
    private final Value value;
    private final Map<Class<? extends Annotation>, CachedValue<? extends Annotation>> annotationCache;
    private Boolean isTransient;
    private boolean usePropertyAccess;

    public AnnotationBasedPersistentProperty(Field field, PropertyDescriptor propertyDescriptor, PersistentEntity<?, P> owner, SimpleTypeHolder simpleTypeHolder) {
        super(field, propertyDescriptor, owner, simpleTypeHolder);
        this.annotationCache = new ConcurrentHashMap();
        populateAnnotationCache(field);
        AccessType accessType = (AccessType) findPropertyOrOwnerAnnotation(AccessType.class);
        this.usePropertyAccess = accessType == null ? false : AccessType.Type.PROPERTY.equals(accessType.value());
        this.value = (Value) findAnnotation(Value.class);
    }

    private final void populateAnnotationCache(Field field) {
        for (Method method : Arrays.asList(getGetter(), getSetter())) {
            if (method != null) {
                for (Annotation annotation : method.getAnnotations()) {
                    Class clsAnnotationType = annotation.annotationType();
                    validateAnnotation(annotation, "Ambiguous mapping! Annotation %s configured multiple times on accessor methods of property %s in class %s!", clsAnnotationType.getSimpleName(), getName(), getOwner().getType().getSimpleName());
                    cacheAndReturn(clsAnnotationType, AnnotatedElementUtils.findMergedAnnotation(method, clsAnnotationType));
                }
            }
        }
        if (field == null) {
            return;
        }
        for (Annotation annotation2 : field.getAnnotations()) {
            Class clsAnnotationType2 = annotation2.annotationType();
            validateAnnotation(annotation2, "Ambiguous mapping! Annotation %s configured on field %s and one of its accessor methods in class %s!", clsAnnotationType2.getSimpleName(), field.getName(), getOwner().getType().getSimpleName());
            cacheAndReturn(clsAnnotationType2, AnnotatedElementUtils.findMergedAnnotation(field, clsAnnotationType2));
        }
    }

    private void validateAnnotation(Annotation candidate, String message, Object... arguments) {
        Class<? extends Annotation> annotationType = candidate.annotationType();
        if (!annotationType.getName().startsWith(SPRING_DATA_PACKAGE)) {
            return;
        }
        CachedValue<? extends Annotation> cachedValue = this.annotationCache.get(annotationType);
        if (cachedValue != null && !((Annotation) ((CachedValue) this.annotationCache.get(annotationType)).value).equals(candidate)) {
            throw new MappingException(String.format(message, arguments));
        }
    }

    @Override // org.springframework.data.mapping.model.AbstractPersistentProperty, org.springframework.data.mapping.PersistentProperty
    public String getSpelExpression() {
        if (this.value == null) {
            return null;
        }
        return this.value.value();
    }

    @Override // org.springframework.data.mapping.model.AbstractPersistentProperty, org.springframework.data.mapping.PersistentProperty
    public boolean isTransient() {
        if (this.isTransient == null) {
            boolean potentiallyTransient = super.isTransient() || isAnnotationPresent(Transient.class);
            this.isTransient = Boolean.valueOf(potentiallyTransient || isAnnotationPresent(Value.class) || isAnnotationPresent(Autowired.class));
        }
        return this.isTransient.booleanValue();
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean isIdProperty() {
        return isAnnotationPresent(Id.class);
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean isVersionProperty() {
        return isAnnotationPresent(Version.class);
    }

    @Override // org.springframework.data.mapping.model.AbstractPersistentProperty, org.springframework.data.mapping.PersistentProperty
    public boolean isAssociation() {
        return !isTransient() && isAnnotationPresent(Reference.class);
    }

    @Override // org.springframework.data.mapping.model.AbstractPersistentProperty, org.springframework.data.mapping.PersistentProperty
    public boolean isWritable() {
        return (isTransient() || isAnnotationPresent(ReadOnlyProperty.class)) ? false : true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.mapping.PersistentProperty
    public <A extends Annotation> A findAnnotation(Class<A> cls) {
        Annotation annotationFindMergedAnnotation;
        Assert.notNull(cls, "Annotation type must not be null!");
        CachedValue<? extends Annotation> cachedValue = this.annotationCache == null ? null : this.annotationCache.get(cls);
        if (cachedValue != null) {
            return (A) cachedValue.getValue();
        }
        for (Method method : Arrays.asList(getGetter(), getSetter())) {
            if (method != null && (annotationFindMergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, cls)) != null) {
                return (A) cacheAndReturn(cls, annotationFindMergedAnnotation);
            }
        }
        return (A) cacheAndReturn(cls, this.field == null ? null : AnnotatedElementUtils.findMergedAnnotation(this.field, cls));
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public <A extends Annotation> A findPropertyOrOwnerAnnotation(Class<A> cls) {
        A a = (A) findAnnotation(cls);
        return a == null ? (A) this.owner.findAnnotation(cls) : a;
    }

    private <A extends Annotation> A cacheAndReturn(Class<? extends A> type, A annotation) {
        if (this.annotationCache != null) {
            this.annotationCache.put(type, CachedValue.of(annotation));
        }
        return annotation;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        return findAnnotation(cls) != null;
    }

    @Override // org.springframework.data.mapping.model.AbstractPersistentProperty, org.springframework.data.mapping.PersistentProperty
    public boolean usePropertyAccess() {
        return super.usePropertyAccess() || this.usePropertyAccess;
    }

    @Override // org.springframework.data.mapping.model.AbstractPersistentProperty
    public String toString() {
        if (this.annotationCache.isEmpty()) {
            populateAnnotationCache(this.field);
        }
        StringBuilder builder = new StringBuilder();
        for (CachedValue<? extends Annotation> annotation : this.annotationCache.values()) {
            if (((CachedValue) annotation).value != null) {
                builder.append(((Annotation) ((CachedValue) annotation).value).toString()).append(SymbolConstants.SPACE_SYMBOL);
            }
        }
        return builder.toString() + super.toString();
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/AnnotationBasedPersistentProperty$CachedValue.class */
    static final class CachedValue<T> {
        private final T value;

        @Generated
        private CachedValue(T value) {
            this.value = value;
        }

        @Generated
        public static <T> CachedValue<T> of(T value) {
            return new CachedValue<>(value);
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof CachedValue)) {
                return false;
            }
            CachedValue<?> other = (CachedValue) o;
            Object this$value = getValue();
            Object other$value = other.getValue();
            return this$value == null ? other$value == null : this$value.equals(other$value);
        }

        @Generated
        public int hashCode() {
            Object $value = getValue();
            int result = (1 * 59) + ($value == null ? 43 : $value.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "AnnotationBasedPersistentProperty.CachedValue(value=" + getValue() + ")";
        }

        @Generated
        public T getValue() {
            return this.value;
        }
    }
}
