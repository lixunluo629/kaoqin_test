package org.springframework.data.mapping.context;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.mapping.model.ClassGeneratingPropertyAccessorFactory;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mapping.model.MutablePersistentEntity;
import org.springframework.data.mapping.model.PersistentPropertyAccessorFactory;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/AbstractMappingContext.class */
public abstract class AbstractMappingContext<E extends MutablePersistentEntity<?, P>, P extends PersistentProperty<P>> implements MappingContext<E, P>, ApplicationEventPublisherAware, InitializingBean {
    private ApplicationEventPublisher applicationEventPublisher;
    private final Map<TypeInformation<?>, E> persistentEntities = new HashMap();
    private final PersistentPropertyAccessorFactory persistentPropertyAccessorFactory = new ClassGeneratingPropertyAccessorFactory();
    private Set<? extends Class<?>> initialEntitySet = new HashSet();
    private boolean strict = false;
    private SimpleTypeHolder simpleTypeHolder = new SimpleTypeHolder();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock read = this.lock.readLock();
    private final Lock write = this.lock.writeLock();

    protected abstract <T> E createPersistentEntity(TypeInformation<T> typeInformation);

    protected abstract P createPersistentProperty(Field field, PropertyDescriptor propertyDescriptor, E e, SimpleTypeHolder simpleTypeHolder);

    @Override // org.springframework.data.mapping.context.MappingContext
    public /* bridge */ /* synthetic */ PersistentEntity getPersistentEntity(PersistentProperty persistentProperty) {
        return getPersistentEntity((AbstractMappingContext<E, P>) persistentProperty);
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public /* bridge */ /* synthetic */ PersistentEntity getPersistentEntity(TypeInformation typeInformation) {
        return getPersistentEntity((TypeInformation<?>) typeInformation);
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public /* bridge */ /* synthetic */ PersistentEntity getPersistentEntity(Class cls) {
        return getPersistentEntity((Class<?>) cls);
    }

    @Override // org.springframework.context.ApplicationEventPublisherAware
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setInitialEntitySet(Set<? extends Class<?>> initialEntitySet) {
        this.initialEntitySet = initialEntitySet;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public void setSimpleTypeHolder(SimpleTypeHolder simpleTypes) {
        this.simpleTypeHolder = simpleTypes == null ? new SimpleTypeHolder() : simpleTypes;
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public Collection<E> getPersistentEntities() {
        try {
            this.read.lock();
            return Collections.unmodifiableSet(new HashSet(this.persistentEntities.values()));
        } finally {
            this.read.unlock();
        }
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public E getPersistentEntity(Class<?> cls) {
        return (E) getPersistentEntity((TypeInformation<?>) ClassTypeInformation.from(cls));
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public boolean hasPersistentEntityFor(Class<?> type) {
        if (type == null) {
            return false;
        }
        return this.persistentEntities.containsKey(ClassTypeInformation.from(type));
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public E getPersistentEntity(TypeInformation<?> typeInformation) {
        Assert.notNull(typeInformation, "Type must not be null!");
        try {
            this.read.lock();
            E e = this.persistentEntities.get(typeInformation);
            if (e != null) {
                return e;
            }
            this.read.unlock();
            if (!shouldCreatePersistentEntityFor(typeInformation)) {
                return null;
            }
            if (this.strict) {
                throw new MappingException("Unknown persistent entity " + typeInformation);
            }
            return (E) addPersistentEntity(typeInformation);
        } finally {
            this.read.unlock();
        }
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public E getPersistentEntity(P p) {
        if (p == null || !p.isEntity()) {
            return null;
        }
        return (E) getPersistentEntity(p.getTypeInformation().getActualType());
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public PersistentPropertyPath<P> getPersistentPropertyPath(PropertyPath propertyPath) {
        Assert.notNull(propertyPath, "Property path must not be null!");
        return getPersistentPropertyPath(propertyPath.toDotPath(), propertyPath.getOwningType());
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public PersistentPropertyPath<P> getPersistentPropertyPath(String propertyPath, Class<?> type) {
        Assert.notNull(propertyPath, "Property path must not be null!");
        Assert.notNull(type, "Type must not be null!");
        return getPersistentPropertyPath(propertyPath, ClassTypeInformation.from(type));
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public PersistentPropertyPath<P> getPersistentPropertyPath(InvalidPersistentPropertyPath invalidPath) {
        return getPersistentPropertyPath(invalidPath.getResolvedPath(), invalidPath.getType());
    }

    private PersistentPropertyPath<P> getPersistentPropertyPath(String propertyPath, TypeInformation<?> type) {
        return getPersistentPropertyPath(Arrays.asList(propertyPath.split("\\.")), type);
    }

    /* JADX WARN: Type inference failed for: r0v12, types: [org.springframework.data.mapping.PersistentProperty] */
    private PersistentPropertyPath<P> getPersistentPropertyPath(Collection<String> parts, TypeInformation<?> type) {
        DefaultPersistentPropertyPath<P> path = DefaultPersistentPropertyPath.empty();
        Iterator<String> iterator = parts.iterator();
        MutablePersistentEntity persistentEntity = getPersistentEntity(type);
        while (iterator.hasNext()) {
            String segment = iterator.next();
            ?? persistentProperty = persistentEntity.getPersistentProperty(segment);
            if (persistentProperty == 0) {
                String source = StringUtils.collectionToDelimitedString(parts, ".");
                String resolvedPath = path.toDotPath();
                throw new InvalidPersistentPropertyPath(source, type, segment, resolvedPath, String.format("No property %s found on %s!", segment, persistentEntity.getName()));
            }
            path = path.append(persistentProperty);
            if (iterator.hasNext()) {
                TypeInformation<?> actualType = persistentProperty.getTypeInformation().getActualType();
                persistentEntity = getPersistentEntity(actualType);
                if (persistentEntity == null) {
                    String source2 = StringUtils.collectionToDelimitedString(parts, ".");
                    String resolvedPath2 = path.toDotPath();
                    String unresolved = iterator.next();
                    throw new InvalidPersistentPropertyPath(source2, persistentProperty.getTypeInformation(), unresolved, resolvedPath2, String.format("No entity %s found for property %s on %s !", actualType.getType().getName(), segment, persistentProperty.getOwner().getName()));
                }
            }
        }
        return path;
    }

    protected E addPersistentEntity(Class<?> cls) {
        return (E) addPersistentEntity(ClassTypeInformation.from(cls));
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected E addPersistentEntity(TypeInformation<?> typeInformation) {
        Assert.notNull(typeInformation, "TypeInformation must not be null!");
        try {
            this.read.lock();
            E e = this.persistentEntities.get(typeInformation);
            if (e != null) {
                return e;
            }
            this.read.unlock();
            Class type = typeInformation.getType();
            try {
                try {
                    this.write.lock();
                    E e2 = (E) createPersistentEntity(typeInformation);
                    this.persistentEntities.put(typeInformation, e2);
                    PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(type);
                    HashMap map = new HashMap();
                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        map.put(propertyDescriptor.getName(), propertyDescriptor);
                    }
                    try {
                        PersistentPropertyCreator persistentPropertyCreator = new PersistentPropertyCreator(e2, map);
                        ReflectionUtils.doWithFields(type, persistentPropertyCreator, PersistentPropertyFilter.INSTANCE);
                        persistentPropertyCreator.addPropertiesForRemainingDescriptors();
                        e2.verify();
                        if (this.persistentPropertyAccessorFactory.isSupported(e2)) {
                            e2.setPersistentPropertyAccessorFactory(this.persistentPropertyAccessorFactory);
                        }
                        if (this.applicationEventPublisher != null && e2 != null) {
                            this.applicationEventPublisher.publishEvent((ApplicationEvent) new MappingContextEvent(this, e2));
                        }
                        return e2;
                    } catch (MappingException e3) {
                        this.persistentEntities.remove(typeInformation);
                        throw e3;
                    }
                } catch (BeansException e4) {
                    throw new MappingException(e4.getMessage(), e4);
                }
            } finally {
                this.write.unlock();
            }
        } finally {
            this.read.unlock();
        }
    }

    @Override // org.springframework.data.mapping.context.MappingContext
    public Collection<TypeInformation<?>> getManagedTypes() {
        try {
            this.read.lock();
            return Collections.unmodifiableSet(new HashSet(this.persistentEntities.keySet()));
        } finally {
            this.read.unlock();
        }
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        initialize();
    }

    public void initialize() {
        for (Class<?> initialEntity : this.initialEntitySet) {
            addPersistentEntity(initialEntity);
        }
    }

    protected boolean shouldCreatePersistentEntityFor(TypeInformation<?> type) {
        return !this.simpleTypeHolder.isSimpleType(type.getType());
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/AbstractMappingContext$PersistentPropertyCreator.class */
    private final class PersistentPropertyCreator implements ReflectionUtils.FieldCallback {
        private final E entity;
        private final Map<String, PropertyDescriptor> descriptors;
        private final Map<String, PropertyDescriptor> remainingDescriptors;

        private PersistentPropertyCreator(E entity, Map<String, PropertyDescriptor> descriptors) {
            Assert.notNull(entity, "PersistentEntity must not be null!");
            Assert.notNull(descriptors, "PropertyDescriptors must not be null!");
            this.entity = entity;
            this.descriptors = descriptors;
            this.remainingDescriptors = new HashMap(descriptors);
        }

        @Override // org.springframework.util.ReflectionUtils.FieldCallback
        public void doWith(Field field) {
            String fieldName = field.getName();
            ReflectionUtils.makeAccessible(field);
            createAndRegisterProperty(field, this.descriptors.get(fieldName));
            this.remainingDescriptors.remove(fieldName);
        }

        public void addPropertiesForRemainingDescriptors() {
            for (PropertyDescriptor descriptor : this.remainingDescriptors.values()) {
                if (PersistentPropertyFilter.INSTANCE.matches(descriptor)) {
                    createAndRegisterProperty(null, descriptor);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void createAndRegisterProperty(Field field, PropertyDescriptor propertyDescriptor) {
            PersistentProperty persistentPropertyCreatePersistentProperty = AbstractMappingContext.this.createPersistentProperty(field, propertyDescriptor, this.entity, AbstractMappingContext.this.simpleTypeHolder);
            if (persistentPropertyCreatePersistentProperty.isTransient()) {
                return;
            }
            if (field == null && !persistentPropertyCreatePersistentProperty.usePropertyAccess()) {
                return;
            }
            this.entity.addPersistentProperty(persistentPropertyCreatePersistentProperty);
            if (persistentPropertyCreatePersistentProperty.isAssociation()) {
                this.entity.addAssociation(persistentPropertyCreatePersistentProperty.getAssociation());
            }
            if (this.entity.getType().equals(persistentPropertyCreatePersistentProperty.getRawType())) {
                return;
            }
            Iterator<? extends TypeInformation<?>> it = persistentPropertyCreatePersistentProperty.getPersistentEntityType().iterator();
            while (it.hasNext()) {
                AbstractMappingContext.this.addPersistentEntity(it.next());
            }
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/AbstractMappingContext$PersistentPropertyFilter.class */
    enum PersistentPropertyFilter implements ReflectionUtils.FieldFilter {
        INSTANCE;

        private static final Iterable<PropertyMatch> UNMAPPED_PROPERTIES;

        static {
            Set<PropertyMatch> matches = new HashSet<>();
            matches.add(new PropertyMatch("class", null));
            matches.add(new PropertyMatch("this\\$.*", null));
            matches.add(new PropertyMatch("metaClass", "groovy.lang.MetaClass"));
            UNMAPPED_PROPERTIES = Collections.unmodifiableCollection(matches);
        }

        @Override // org.springframework.util.ReflectionUtils.FieldFilter
        public boolean matches(Field field) {
            if (Modifier.isStatic(field.getModifiers())) {
                return false;
            }
            for (PropertyMatch candidate : UNMAPPED_PROPERTIES) {
                if (candidate.matches(field.getName(), field.getType())) {
                    return false;
                }
            }
            return true;
        }

        public boolean matches(PropertyDescriptor descriptor) {
            Assert.notNull(descriptor, "PropertyDescriptor must not be null!");
            if (descriptor.getReadMethod() == null && descriptor.getWriteMethod() == null) {
                return false;
            }
            for (PropertyMatch candidate : UNMAPPED_PROPERTIES) {
                if (candidate.matches(descriptor.getName(), descriptor.getPropertyType())) {
                    return false;
                }
            }
            return true;
        }

        /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/AbstractMappingContext$PersistentPropertyFilter$PropertyMatch.class */
        static class PropertyMatch {
            private final String namePattern;
            private final String typeName;

            public PropertyMatch(String namePattern, String typeName) {
                Assert.isTrue((namePattern == null && typeName == null) ? false : true, "Either name patter or type name must be given!");
                this.namePattern = namePattern;
                this.typeName = typeName;
            }

            public boolean matches(String name, Class<?> type) {
                if (this.namePattern != null && !name.matches(this.namePattern)) {
                    return false;
                }
                if (this.typeName != null && !type.getName().equals(this.typeName)) {
                    return false;
                }
                return true;
            }
        }
    }
}
