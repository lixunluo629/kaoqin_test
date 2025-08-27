package org.springframework.data.mapping.model;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/AbstractPersistentProperty.class */
public abstract class AbstractPersistentProperty<P extends PersistentProperty<P>> implements PersistentProperty<P> {
    private static final Field CAUSE_FIELD = ReflectionUtils.findField(Throwable.class, "cause");
    protected final String name;
    protected final PropertyDescriptor propertyDescriptor;
    protected final TypeInformation<?> information;
    protected final Class<?> rawType;
    protected final Field field;
    protected final Association<P> association;
    protected final PersistentEntity<?, P> owner;
    private final SimpleTypeHolder simpleTypeHolder;
    private final int hashCode;

    protected abstract Association<P> createAssociation();

    public AbstractPersistentProperty(Field field, PropertyDescriptor propertyDescriptor, PersistentEntity<?, P> owner, SimpleTypeHolder simpleTypeHolder) {
        Class<?> propertyType;
        Assert.notNull(simpleTypeHolder, "SimpleTypeHolder must not be null!");
        Assert.notNull(owner, "Owner entity must not be null!");
        this.propertyDescriptor = propertyDescriptor;
        this.field = field;
        this.owner = owner;
        this.simpleTypeHolder = simpleTypeHolder;
        this.name = field == null ? propertyDescriptor.getName() : field.getName();
        this.information = owner.getTypeInformation().getProperty(this.name);
        if (this.information != null) {
            propertyType = this.information.getType();
        } else {
            propertyType = field == null ? propertyDescriptor.getPropertyType() : field.getType();
        }
        this.rawType = propertyType;
        this.association = isAssociation() ? createAssociation() : null;
        this.hashCode = this.field == null ? this.propertyDescriptor.hashCode() : this.field.hashCode();
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public PersistentEntity<?, P> getOwner() {
        return this.owner;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public String getName() {
        return this.name;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public Class<?> getType() {
        return this.information.getType();
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public Class<?> getRawType() {
        return this.rawType;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public TypeInformation<?> getTypeInformation() {
        return this.information;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public Iterable<? extends TypeInformation<?>> getPersistentEntityType() {
        if (!isEntity()) {
            return Collections.emptySet();
        }
        TypeInformation<?> candidate = getTypeInformationIfEntityCandidate();
        return candidate != null ? Collections.singleton(candidate) : Collections.emptySet();
    }

    private TypeInformation<?> getTypeInformationIfEntityCandidate() {
        TypeInformation<?> candidate = this.information.getActualType();
        if (candidate == null || this.simpleTypeHolder.isSimpleType(candidate.getType()) || candidate.isCollectionLike() || candidate.isMap()) {
            return null;
        }
        return candidate;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public Method getGetter() {
        Method getter;
        if (this.propertyDescriptor == null || (getter = this.propertyDescriptor.getReadMethod()) == null) {
            return null;
        }
        Class<?> returnType = this.owner.getTypeInformation().getReturnType(getter).getType();
        if (this.rawType.isAssignableFrom(returnType)) {
            return getter;
        }
        return null;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public Method getSetter() {
        Method setter;
        if (this.propertyDescriptor == null || (setter = this.propertyDescriptor.getWriteMethod()) == null) {
            return null;
        }
        Class<?> parameterType = this.owner.getTypeInformation().getParameterTypes(setter).get(0).getType();
        if (parameterType.isAssignableFrom(this.rawType)) {
            return setter;
        }
        return null;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public Field getField() {
        return this.field;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public String getSpelExpression() {
        return null;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean isTransient() {
        return false;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean isWritable() {
        return !isTransient();
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean isAssociation() {
        return (this.field == null || AnnotationUtils.getAnnotation(this.field, Reference.class) == null) ? false : true;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public Association<P> getAssociation() {
        return this.association;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean isCollectionLike() {
        return this.information.isCollectionLike();
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean isMap() {
        return Map.class.isAssignableFrom(getType());
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean isArray() {
        return getType().isArray();
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean isEntity() {
        return (isTransient() || getTypeInformationIfEntityCandidate() == null) ? false : true;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public Class<?> getComponentType() {
        TypeInformation<?> componentType;
        if ((isMap() || isCollectionLike()) && (componentType = this.information.getComponentType()) != null) {
            return componentType.getType();
        }
        return null;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public Class<?> getMapValueType() {
        if (isMap()) {
            return this.information.getMapValueType().getType();
        }
        return null;
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public Class<?> getActualType() {
        return this.information.getActualType().getType();
    }

    @Override // org.springframework.data.mapping.PersistentProperty
    public boolean usePropertyAccess() {
        return this.owner.getType().isInterface() || CAUSE_FIELD.equals(getField());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractPersistentProperty)) {
            return false;
        }
        AbstractPersistentProperty<?> that = (AbstractPersistentProperty) obj;
        return this.field == null ? this.propertyDescriptor.equals(that.propertyDescriptor) : this.field.equals(that.field);
    }

    public int hashCode() {
        return this.hashCode;
    }

    public String toString() {
        return this.field == null ? this.propertyDescriptor.toString() : this.field.toString();
    }
}
