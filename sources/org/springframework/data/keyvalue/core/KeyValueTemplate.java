package org.springframework.data.keyvalue.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.event.KeyValueEvent;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentEntity;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.keyvalue.core.mapping.context.KeyValueMappingContext;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/KeyValueTemplate.class */
public class KeyValueTemplate implements KeyValueOperations, ApplicationEventPublisherAware {
    private static final PersistenceExceptionTranslator DEFAULT_PERSISTENCE_EXCEPTION_TRANSLATOR = new KeyValuePersistenceExceptionTranslator();
    private final KeyValueAdapter adapter;
    private final MappingContext<? extends KeyValuePersistentEntity<?>, ? extends KeyValuePersistentProperty> mappingContext;
    private final IdentifierGenerator identifierGenerator;
    private PersistenceExceptionTranslator exceptionTranslator;
    private ApplicationEventPublisher eventPublisher;
    private boolean publishEvents;
    private Set<Class<? extends KeyValueEvent>> eventTypesToPublish;

    public KeyValueTemplate(KeyValueAdapter adapter) {
        this(adapter, new KeyValueMappingContext());
    }

    public KeyValueTemplate(KeyValueAdapter adapter, MappingContext<? extends KeyValuePersistentEntity<?>, ? extends KeyValuePersistentProperty> mappingContext) {
        this.exceptionTranslator = DEFAULT_PERSISTENCE_EXCEPTION_TRANSLATOR;
        this.publishEvents = true;
        this.eventTypesToPublish = Collections.emptySet();
        Assert.notNull(adapter, "Adapter must not be null!");
        Assert.notNull(mappingContext, "MappingContext must not be null!");
        this.adapter = adapter;
        this.mappingContext = mappingContext;
        this.identifierGenerator = DefaultIdentifierGenerator.INSTANCE;
    }

    public void setExceptionTranslator(PersistenceExceptionTranslator exceptionTranslator) {
        Assert.notNull(exceptionTranslator, "ExceptionTranslator must not be null.");
        this.exceptionTranslator = exceptionTranslator;
    }

    public void setEventTypesToPublish(Set<Class<? extends KeyValueEvent>> eventTypesToPublish) {
        if (CollectionUtils.isEmpty(eventTypesToPublish)) {
            this.publishEvents = false;
        } else {
            this.publishEvents = true;
            this.eventTypesToPublish = Collections.unmodifiableSet(eventTypesToPublish);
        }
    }

    @Override // org.springframework.context.ApplicationEventPublisherAware
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public <T> T insert(T objectToInsert) {
        PersistentEntity<?, ?> entity = this.mappingContext.getPersistentEntity(ClassUtils.getUserClass(objectToInsert));
        GeneratingIdAccessor generatingIdAccessor = new GeneratingIdAccessor(entity.getPropertyAccessor(objectToInsert), entity.getIdProperty(), this.identifierGenerator);
        Object id = generatingIdAccessor.getOrGenerateIdentifier();
        insert((Serializable) id, objectToInsert);
        return objectToInsert;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public void insert(final Serializable id, final Object objectToInsert) {
        Assert.notNull(id, "Id for object to be inserted must not be null!");
        Assert.notNull(objectToInsert, "Object to be inserted must not be null!");
        final String keyspace = resolveKeySpace(objectToInsert.getClass());
        potentiallyPublishEvent(KeyValueEvent.beforeInsert(id, keyspace, objectToInsert.getClass(), objectToInsert));
        execute(new KeyValueCallback<Void>() { // from class: org.springframework.data.keyvalue.core.KeyValueTemplate.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.keyvalue.core.KeyValueCallback
            public Void doInKeyValue(KeyValueAdapter adapter) {
                if (adapter.contains(id, keyspace)) {
                    throw new DuplicateKeyException(String.format("Cannot insert existing object with id %s!. Please use update.", id));
                }
                adapter.put(id, objectToInsert, keyspace);
                return null;
            }
        });
        potentiallyPublishEvent(KeyValueEvent.afterInsert(id, keyspace, objectToInsert.getClass(), objectToInsert));
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public void update(Object objectToUpdate) {
        PersistentEntity<?, ? extends PersistentProperty> entity = this.mappingContext.getPersistentEntity(ClassUtils.getUserClass(objectToUpdate));
        if (!entity.hasIdProperty()) {
            throw new InvalidDataAccessApiUsageException(String.format("Cannot determine id for type %s", ClassUtils.getUserClass(objectToUpdate)));
        }
        update((Serializable) entity.getIdentifierAccessor(objectToUpdate).getIdentifier(), objectToUpdate);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public void update(final Serializable id, final Object objectToUpdate) {
        Assert.notNull(id, "Id for object to be inserted must not be null!");
        Assert.notNull(objectToUpdate, "Object to be updated must not be null!");
        final String keyspace = resolveKeySpace(objectToUpdate.getClass());
        potentiallyPublishEvent(KeyValueEvent.beforeUpdate(id, keyspace, objectToUpdate.getClass(), objectToUpdate));
        Object existing = execute(new KeyValueCallback<Object>() { // from class: org.springframework.data.keyvalue.core.KeyValueTemplate.2
            @Override // org.springframework.data.keyvalue.core.KeyValueCallback
            public Object doInKeyValue(KeyValueAdapter adapter) {
                return adapter.put(id, objectToUpdate, keyspace);
            }
        });
        potentiallyPublishEvent(KeyValueEvent.afterUpdate(id, keyspace, objectToUpdate.getClass(), objectToUpdate, existing));
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public <T> Iterable<T> findAll(final Class<T> type) {
        Assert.notNull(type, "Type to fetch must not be null!");
        return (Iterable) execute(new KeyValueCallback<Iterable<T>>() { // from class: org.springframework.data.keyvalue.core.KeyValueTemplate.3
            @Override // org.springframework.data.keyvalue.core.KeyValueCallback
            public Iterable<T> doInKeyValue(KeyValueAdapter adapter) {
                Iterable<?> values = adapter.getAllOf(KeyValueTemplate.this.resolveKeySpace(type));
                if (values == null) {
                    return Collections.emptySet();
                }
                ArrayList arrayList = new ArrayList();
                for (Object candidate : values) {
                    if (KeyValueTemplate.typeCheck(type, candidate)) {
                        arrayList.add(candidate);
                    }
                }
                return arrayList;
            }
        });
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public <T> T findById(final Serializable serializable, final Class<T> cls) {
        Assert.notNull(serializable, "Id for object to be inserted must not be null!");
        Assert.notNull(cls, "Type to fetch must not be null!");
        final String strResolveKeySpace = resolveKeySpace(cls);
        potentiallyPublishEvent(KeyValueEvent.beforeGet(serializable, strResolveKeySpace, cls));
        T t = (T) execute(new KeyValueCallback<T>() { // from class: org.springframework.data.keyvalue.core.KeyValueTemplate.4
            @Override // org.springframework.data.keyvalue.core.KeyValueCallback
            public T doInKeyValue(KeyValueAdapter keyValueAdapter) {
                T t2 = (T) keyValueAdapter.get(serializable, strResolveKeySpace, cls);
                if (t2 == null || KeyValueTemplate.typeCheck(cls, t2)) {
                    return t2;
                }
                return null;
            }
        });
        potentiallyPublishEvent(KeyValueEvent.afterGet(serializable, strResolveKeySpace, cls, t));
        return t;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public void delete(Class<?> type) {
        Assert.notNull(type, "Type to delete must not be null!");
        final String keyspace = resolveKeySpace(type);
        potentiallyPublishEvent(KeyValueEvent.beforeDropKeySpace(keyspace, type));
        execute(new KeyValueCallback<Void>() { // from class: org.springframework.data.keyvalue.core.KeyValueTemplate.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.keyvalue.core.KeyValueCallback
            public Void doInKeyValue(KeyValueAdapter adapter) {
                adapter.deleteAllOf(keyspace);
                return null;
            }
        });
        potentiallyPublishEvent(KeyValueEvent.afterDropKeySpace(keyspace, type));
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public <T> T delete(T t) {
        Class<?> userClass = ClassUtils.getUserClass(t);
        return (T) delete((Serializable) this.mappingContext.getPersistentEntity(userClass).getIdentifierAccessor(t).getIdentifier(), userClass);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public <T> T delete(final Serializable serializable, final Class<T> cls) {
        Assert.notNull(serializable, "Id for object to be deleted must not be null!");
        Assert.notNull(cls, "Type to delete must not be null!");
        final String strResolveKeySpace = resolveKeySpace(cls);
        potentiallyPublishEvent(KeyValueEvent.beforeDelete(serializable, strResolveKeySpace, cls));
        T t = (T) execute(new KeyValueCallback<T>() { // from class: org.springframework.data.keyvalue.core.KeyValueTemplate.6
            @Override // org.springframework.data.keyvalue.core.KeyValueCallback
            public T doInKeyValue(KeyValueAdapter keyValueAdapter) {
                return (T) keyValueAdapter.delete(serializable, strResolveKeySpace, cls);
            }
        });
        potentiallyPublishEvent(KeyValueEvent.afterDelete(serializable, strResolveKeySpace, cls, t));
        return t;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public long count(Class<?> type) {
        Assert.notNull(type, "Type for count must not be null!");
        return this.adapter.count(resolveKeySpace(type));
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public <T> T execute(KeyValueCallback<T> action) {
        Assert.notNull(action, "KeyValueCallback must not be null!");
        try {
            return action.doInKeyValue(this.adapter);
        } catch (RuntimeException e) {
            throw resolveExceptionIfPossible(e);
        }
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public <T> Iterable<T> find(final KeyValueQuery<?> query, final Class<T> type) {
        return (Iterable) execute(new KeyValueCallback<Iterable<T>>() { // from class: org.springframework.data.keyvalue.core.KeyValueTemplate.7
            @Override // org.springframework.data.keyvalue.core.KeyValueCallback
            public Iterable<T> doInKeyValue(KeyValueAdapter adapter) {
                Iterable<T> iterableFind = adapter.find(query, KeyValueTemplate.this.resolveKeySpace(type), type);
                if (iterableFind == null) {
                    return Collections.emptySet();
                }
                List<T> filtered = new ArrayList<>();
                for (T t : iterableFind) {
                    if (KeyValueTemplate.typeCheck(type, t)) {
                        filtered.add(t);
                    }
                }
                return filtered;
            }
        });
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public <T> Iterable<T> findAll(Sort sort, Class<T> type) {
        return find(new KeyValueQuery<>(sort), type);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public <T> Iterable<T> findInRange(int offset, int rows, Class<T> type) {
        return find(new KeyValueQuery().skip(offset).limit(rows), type);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public <T> Iterable<T> findInRange(int offset, int rows, Sort sort, Class<T> type) {
        return find(new KeyValueQuery(sort).skip(offset).limit(rows), type);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public long count(final KeyValueQuery<?> query, final Class<?> type) {
        return ((Long) execute(new KeyValueCallback<Long>() { // from class: org.springframework.data.keyvalue.core.KeyValueTemplate.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.keyvalue.core.KeyValueCallback
            public Long doInKeyValue(KeyValueAdapter adapter) {
                return Long.valueOf(adapter.count(query, KeyValueTemplate.this.resolveKeySpace(type)));
            }
        })).longValue();
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueOperations
    public MappingContext<?, ?> getMappingContext() {
        return this.mappingContext;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws Exception {
        this.adapter.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String resolveKeySpace(Class<?> type) {
        return ((KeyValuePersistentEntity) this.mappingContext.getPersistentEntity(type)).getKeySpace();
    }

    private RuntimeException resolveExceptionIfPossible(RuntimeException e) {
        DataAccessException translatedException = this.exceptionTranslator.translateExceptionIfPossible(e);
        return translatedException != null ? translatedException : e;
    }

    private void potentiallyPublishEvent(KeyValueEvent event) {
        if (this.eventPublisher != null && this.publishEvents) {
            if (this.eventTypesToPublish.isEmpty() || this.eventTypesToPublish.contains(event.getClass())) {
                this.eventPublisher.publishEvent((ApplicationEvent) event);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean typeCheck(Class<?> requiredType, Object candidate) {
        if (candidate == null) {
            return true;
        }
        return ClassUtils.isAssignable(requiredType, candidate.getClass());
    }
}
