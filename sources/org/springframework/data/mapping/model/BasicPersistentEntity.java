package org.springframework.data.mapping.model;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import lombok.Generated;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.AssociationHandler;
import org.springframework.data.mapping.IdentifierAccessor;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/BasicPersistentEntity.class */
public class BasicPersistentEntity<T, P extends PersistentProperty<P>> implements MutablePersistentEntity<T, P> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) BasicPersistentEntity.class);
    private static final String TYPE_MISMATCH = "Target bean of type %s is not of type of the persistent entity (%s)!";
    private static final String NULL_ASSOCIATION = "%s.addAssociation(…) was called with a null association! Usually indicates a problem in a Spring Data MappingContext implementation. Be sure to file a bug at https://jira.spring.io!";
    private final PreferredConstructor<T, P> constructor;
    private final TypeInformation<T> information;
    private final List<P> properties;
    private final Comparator<P> comparator;
    private final Set<Association<P>> associations;
    private final Map<String, P> propertyCache;
    private final Map<Class<? extends Annotation>, Annotation> annotationCache;
    private P idProperty;
    private P versionProperty;
    private PersistentPropertyAccessorFactory propertyAccessorFactory;

    public BasicPersistentEntity(TypeInformation<T> information) {
        this(information, null);
    }

    public BasicPersistentEntity(TypeInformation<T> information, Comparator<P> comparator) {
        Assert.notNull(information, "Information must not be null!");
        this.information = information;
        this.properties = new ArrayList();
        this.comparator = comparator;
        this.constructor = new PreferredConstructorDiscoverer(information, this).getConstructor();
        this.associations = comparator == null ? new HashSet<>() : new TreeSet<>(new AssociationComparator(comparator));
        this.propertyCache = new HashMap();
        this.annotationCache = new HashMap();
        this.propertyAccessorFactory = BeanWrapperPropertyAccessorFactory.INSTANCE;
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public PreferredConstructor<T, P> getPersistenceConstructor() {
        return this.constructor;
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public boolean isConstructorArgument(PersistentProperty<?> property) {
        if (this.constructor == null) {
            return false;
        }
        return this.constructor.isConstructorParameter(property);
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public boolean isIdProperty(PersistentProperty<?> property) {
        if (this.idProperty == null) {
            return false;
        }
        return this.idProperty.equals(property);
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public boolean isVersionProperty(PersistentProperty<?> property) {
        if (this.versionProperty == null) {
            return false;
        }
        return this.versionProperty.equals(property);
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public String getName() {
        return getType().getName();
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public P getIdProperty() {
        return this.idProperty;
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public P getVersionProperty() {
        return this.versionProperty;
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public boolean hasIdProperty() {
        return this.idProperty != null;
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public boolean hasVersionProperty() {
        return this.versionProperty != null;
    }

    @Override // org.springframework.data.mapping.model.MutablePersistentEntity
    public void addPersistentProperty(P p) {
        Assert.notNull(p, "Property must not be null!");
        if (this.properties.contains(p)) {
            return;
        }
        this.properties.add(p);
        if (!this.propertyCache.containsKey(p.getName())) {
            this.propertyCache.put(p.getName(), p);
        }
        P p2 = (P) returnPropertyIfBetterIdPropertyCandidateOrNull(p);
        if (p2 != null) {
            this.idProperty = p2;
        }
        if (p.isVersionProperty()) {
            if (this.versionProperty != null) {
                throw new MappingException(String.format("Attempt to add version property %s but already have property %s registered as version. Check your mapping configuration!", p.getField(), this.versionProperty.getField()));
            }
            this.versionProperty = p;
        }
    }

    protected P returnPropertyIfBetterIdPropertyCandidateOrNull(P property) {
        if (!property.isIdProperty()) {
            return null;
        }
        if (this.idProperty != null) {
            throw new MappingException(String.format("Attempt to add id property %s but already have property %s registered as id. Check your mapping configuration!", property.getField(), this.idProperty.getField()));
        }
        return property;
    }

    @Override // org.springframework.data.mapping.model.MutablePersistentEntity
    public void addAssociation(Association<P> association) {
        if (association == null) {
            LOGGER.warn(String.format(NULL_ASSOCIATION, getClass().getName()));
        } else if (!this.associations.contains(association)) {
            this.associations.add(association);
        }
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public P getPersistentProperty(String name) {
        return this.propertyCache.get(name);
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public P getPersistentProperty(Class<? extends Annotation> cls) {
        Assert.notNull(cls, "Annotation type must not be null!");
        for (P p : this.properties) {
            if (p.isAnnotationPresent(cls)) {
                return p;
            }
        }
        Iterator<Association<P>> it = this.associations.iterator();
        while (it.hasNext()) {
            P p2 = (P) it.next().getInverse();
            if (p2.isAnnotationPresent(cls)) {
                return p2;
            }
        }
        return null;
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public Class<T> getType() {
        return this.information.getType();
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public Object getTypeAlias() {
        TypeAlias alias = (TypeAlias) AnnotatedElementUtils.findMergedAnnotation(getType(), TypeAlias.class);
        if (alias != null && StringUtils.hasText(alias.value())) {
            return alias.value();
        }
        return null;
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public TypeInformation<T> getTypeInformation() {
        return this.information;
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public void doWithProperties(PropertyHandler<P> handler) {
        Assert.notNull(handler, "Handler must not be null!");
        for (P property : this.properties) {
            if (!property.isTransient() && !property.isAssociation()) {
                handler.doWithPersistentProperty(property);
            }
        }
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public void doWithProperties(SimplePropertyHandler handler) {
        Assert.notNull(handler, "Handler must not be null!");
        for (P property : this.properties) {
            if (!property.isTransient() && !property.isAssociation()) {
                handler.doWithPersistentProperty(property);
            }
        }
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public void doWithAssociations(AssociationHandler<P> handler) {
        Assert.notNull(handler, "Handler must not be null!");
        for (Association<P> association : this.associations) {
            handler.doWithAssociation(association);
        }
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public void doWithAssociations(SimpleAssociationHandler simpleAssociationHandler) {
        Assert.notNull(simpleAssociationHandler, "Handler must not be null!");
        Iterator<Association<P>> it = this.associations.iterator();
        while (it.hasNext()) {
            simpleAssociationHandler.doWithAssociation(it.next());
        }
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public <A extends Annotation> A findAnnotation(Class<A> cls) {
        if (this.annotationCache.containsKey(cls)) {
            return (A) this.annotationCache.get(cls);
        }
        A a = (A) AnnotatedElementUtils.findMergedAnnotation(getType(), cls);
        this.annotationCache.put(cls, a);
        return a;
    }

    @Override // org.springframework.data.mapping.model.MutablePersistentEntity
    public void verify() {
        if (this.comparator != null) {
            Collections.sort(this.properties, this.comparator);
        }
    }

    @Override // org.springframework.data.mapping.model.MutablePersistentEntity
    public void setPersistentPropertyAccessorFactory(PersistentPropertyAccessorFactory factory) {
        this.propertyAccessorFactory = factory;
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public PersistentPropertyAccessor getPropertyAccessor(Object bean) {
        Assert.notNull(bean, "Target bean must not be null!");
        assertBeanType(bean);
        return this.propertyAccessorFactory.getPropertyAccessor(this, bean);
    }

    @Override // org.springframework.data.mapping.PersistentEntity
    public IdentifierAccessor getIdentifierAccessor(Object bean) {
        Assert.notNull(bean, "Target bean must not be null!");
        assertBeanType(bean);
        if (Persistable.class.isAssignableFrom(getType())) {
            return new PersistableIdentifierAccessor((Persistable) bean);
        }
        return hasIdProperty() ? new IdPropertyIdentifierAccessor(this, bean) : NullReturningIdentifierAccessor.INSTANCE;
    }

    private void assertBeanType(Object bean) {
        if (!getType().isInstance(bean)) {
            throw new IllegalArgumentException(String.format(TYPE_MISMATCH, bean.getClass().getName(), getType().getName()));
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/BasicPersistentEntity$NullReturningIdentifierAccessor.class */
    private enum NullReturningIdentifierAccessor implements IdentifierAccessor {
        INSTANCE;

        @Override // org.springframework.data.mapping.IdentifierAccessor
        public Object getIdentifier() {
            return null;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/BasicPersistentEntity$AssociationComparator.class */
    private static final class AssociationComparator<P extends PersistentProperty<P>> implements Comparator<Association<P>>, Serializable {
        private static final long serialVersionUID = 4508054194886854513L;

        @NonNull
        private final Comparator<P> delegate;

        @Generated
        public AssociationComparator(@NonNull Comparator<P> delegate) {
            if (delegate == null) {
                throw new IllegalArgumentException("delegate is marked @NonNull but is null");
            }
            this.delegate = delegate;
        }

        @Override // java.util.Comparator
        public int compare(Association<P> association, Association<P> association2) {
            return this.delegate.compare(association.getInverse(), association2.getInverse());
        }
    }
}
