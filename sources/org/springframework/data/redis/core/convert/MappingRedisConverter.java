package org.springframework.data.redis.core.convert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.convert.DefaultTypeMapper;
import org.springframework.data.convert.EntityInstantiators;
import org.springframework.data.convert.TypeAliasAccessor;
import org.springframework.data.convert.TypeMapper;
import org.springframework.data.keyvalue.core.mapping.KeySpaceResolver;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentEntity;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.AssociationHandler;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.context.PersistentPropertyPath;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mapping.model.PersistentEntityParameterValueProvider;
import org.springframework.data.mapping.model.PropertyValueProvider;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.core.mapping.RedisPersistentEntity;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.comparator.NullSafeComparator;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/MappingRedisConverter.class */
public class MappingRedisConverter implements RedisConverter, InitializingBean {
    private static final String TYPE_HINT_ALIAS = "_class";
    private static final String INVALID_TYPE_ASSIGNMENT = "Value of type %s cannot be assigned to property %s of type %s.";
    private final RedisMappingContext mappingContext;
    private final GenericConversionService conversionService;
    private final EntityInstantiators entityInstantiators;
    private final TypeMapper<RedisData> typeMapper;
    private final Comparator<String> listKeyComparator;
    private ReferenceResolver referenceResolver;
    private IndexResolver indexResolver;
    private CustomConversions customConversions;

    MappingRedisConverter(RedisMappingContext context) {
        this(context, null, null);
    }

    public MappingRedisConverter(RedisMappingContext mappingContext, IndexResolver indexResolver, ReferenceResolver referenceResolver) {
        this.listKeyComparator = new NullSafeComparator(NaturalOrderingKeyComparator.INSTANCE, true);
        this.mappingContext = mappingContext != null ? mappingContext : new RedisMappingContext();
        this.entityInstantiators = new EntityInstantiators();
        this.conversionService = new DefaultConversionService();
        this.customConversions = new CustomConversions();
        this.typeMapper = new DefaultTypeMapper(new RedisTypeAliasAccessor(this.conversionService));
        this.referenceResolver = referenceResolver;
        this.indexResolver = indexResolver != null ? indexResolver : new PathIndexResolver(this.mappingContext);
    }

    @Override // org.springframework.data.convert.EntityReader
    public <R> R read(Class<R> cls, RedisData redisData) {
        return (R) readInternal("", cls, redisData);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v19, types: [org.springframework.data.mapping.PersistentProperty] */
    public <R> R readInternal(final String str, Class<R> cls, final RedisData redisData) {
        if (redisData.getBucket() == null || redisData.getBucket().isEmpty()) {
            return null;
        }
        TypeInformation<?> type = this.typeMapper.readType(redisData);
        TypeInformation<?> typeInformationFrom = type != null ? type : ClassTypeInformation.from(cls);
        final RedisPersistentEntity<?> persistentEntity = this.mappingContext.getPersistentEntity(typeInformationFrom);
        if (this.customConversions.hasCustomReadTarget(Map.class, typeInformationFrom.getType())) {
            HashMap map = new HashMap();
            if (!str.isEmpty()) {
                for (Map.Entry<String, byte[]> entry : redisData.getBucket().extract(str + ".").entrySet()) {
                    map.put(entry.getKey().substring(str.length() + 1), entry.getValue());
                }
            } else {
                map.putAll(redisData.getBucket().asMap());
            }
            R r = (R) this.conversionService.convert(map, typeInformationFrom.getType());
            if (persistentEntity.hasIdProperty()) {
                persistentEntity.getPropertyAccessor(r).setProperty(persistentEntity.getIdProperty(), redisData.getId());
            }
            return r;
        }
        if (this.conversionService.canConvert(byte[].class, typeInformationFrom.getType())) {
            return (R) this.conversionService.convert(redisData.getBucket().get(StringUtils.hasText(str) ? str : "_raw"), typeInformationFrom.getType());
        }
        R r2 = (R) this.entityInstantiators.getInstantiatorFor(persistentEntity).createInstance(persistentEntity, new PersistentEntityParameterValueProvider(persistentEntity, new ConverterAwareParameterValueProvider(str, redisData, this.conversionService), this.conversionService));
        final PersistentPropertyAccessor propertyAccessor = persistentEntity.getPropertyAccessor(r2);
        persistentEntity.doWithProperties(new PropertyHandler<KeyValuePersistentProperty>() { // from class: org.springframework.data.redis.core.convert.MappingRedisConverter.1
            @Override // org.springframework.data.mapping.PropertyHandler
            public void doWithPersistentProperty(KeyValuePersistentProperty persistentProperty) {
                String currentPath = !str.isEmpty() ? str + "." + persistentProperty.getName() : persistentProperty.getName();
                if (persistentEntity.getPersistenceConstructor().isConstructorParameter(persistentProperty)) {
                    return;
                }
                if (persistentProperty.isMap()) {
                    Map<?, ?> targetValue = MappingRedisConverter.this.conversionService.canConvert(byte[].class, persistentProperty.getMapValueType()) ? MappingRedisConverter.this.readMapOfSimpleTypes(currentPath, persistentProperty.getType(), persistentProperty.getComponentType(), persistentProperty.getMapValueType(), redisData) : MappingRedisConverter.this.readMapOfComplexTypes(currentPath, persistentProperty.getType(), persistentProperty.getComponentType(), persistentProperty.getMapValueType(), redisData);
                    if (targetValue != null) {
                        propertyAccessor.setProperty(persistentProperty, targetValue);
                        return;
                    }
                    return;
                }
                if (persistentProperty.isCollectionLike()) {
                    Object targetValue2 = MappingRedisConverter.this.readCollectionOrArray(currentPath, persistentProperty.getType(), persistentProperty.getTypeInformation().getComponentType().getActualType().getType(), redisData.getBucket());
                    if (targetValue2 != null) {
                        propertyAccessor.setProperty(persistentProperty, targetValue2);
                        return;
                    }
                    return;
                }
                if (persistentProperty.isEntity() && !MappingRedisConverter.this.conversionService.canConvert(byte[].class, persistentProperty.getTypeInformation().getActualType().getType())) {
                    Class<?> targetType = persistentProperty.getTypeInformation().getActualType().getType();
                    Bucket bucket = redisData.getBucket().extract(currentPath + ".");
                    RedisData newBucket = new RedisData(bucket);
                    byte[] type2 = bucket.get(currentPath + "._class");
                    if (type2 != null && type2.length > 0) {
                        newBucket.getBucket().put("_class", type2);
                    }
                    propertyAccessor.setProperty(persistentProperty, MappingRedisConverter.this.readInternal(currentPath, targetType, newBucket));
                    return;
                }
                if (persistentProperty.isIdProperty() && StringUtils.isEmpty(Boolean.valueOf(str.isEmpty()))) {
                    if (redisData.getBucket().get(currentPath) == null) {
                        propertyAccessor.setProperty(persistentProperty, MappingRedisConverter.this.fromBytes(redisData.getBucket().get(currentPath), persistentProperty.getActualType()));
                    } else {
                        propertyAccessor.setProperty(persistentProperty, redisData.getId());
                    }
                }
                Class<?> typeToUse = MappingRedisConverter.this.getTypeHint(currentPath, redisData.getBucket(), persistentProperty.getActualType());
                propertyAccessor.setProperty(persistentProperty, MappingRedisConverter.this.fromBytes(redisData.getBucket().get(currentPath), typeToUse));
            }
        });
        readAssociation(str, redisData, persistentEntity, propertyAccessor);
        return r2;
    }

    private void readAssociation(final String path, final RedisData source, KeyValuePersistentEntity<?> entity, final PersistentPropertyAccessor accessor) {
        entity.doWithAssociations(new AssociationHandler<KeyValuePersistentProperty>() { // from class: org.springframework.data.redis.core.convert.MappingRedisConverter.2
            @Override // org.springframework.data.mapping.AssociationHandler
            public void doWithAssociation(Association<KeyValuePersistentProperty> association) {
                String currentPath = !path.isEmpty() ? path + "." + ((KeyValuePersistentProperty) association.getInverse()).getName() : ((KeyValuePersistentProperty) association.getInverse()).getName();
                if (((KeyValuePersistentProperty) association.getInverse()).isCollectionLike()) {
                    Bucket bucket = source.getBucket().extract(currentPath + ".[");
                    Collection<Object> target = CollectionFactory.createCollection(((KeyValuePersistentProperty) association.getInverse()).getType(), ((KeyValuePersistentProperty) association.getInverse()).getComponentType(), bucket.size());
                    for (Map.Entry<String, byte[]> entry : bucket.entrySet()) {
                        String referenceKey = (String) MappingRedisConverter.this.fromBytes(entry.getValue(), String.class);
                        if (KeyspaceIdentifier.isValid(referenceKey)) {
                            KeyspaceIdentifier identifier = KeyspaceIdentifier.of(referenceKey);
                            Map<byte[], byte[]> rawHash = MappingRedisConverter.this.referenceResolver.resolveReference(identifier.getId(), identifier.getKeyspace());
                            if (!CollectionUtils.isEmpty((Map<?, ?>) rawHash)) {
                                target.add(MappingRedisConverter.this.read((Class) ((KeyValuePersistentProperty) association.getInverse()).getActualType(), new RedisData(rawHash)));
                            }
                        }
                    }
                    accessor.setProperty(association.getInverse(), target);
                    return;
                }
                byte[] binKey = source.getBucket().get(currentPath);
                if (binKey == null || binKey.length == 0) {
                    return;
                }
                String referenceKey2 = (String) MappingRedisConverter.this.fromBytes(binKey, String.class);
                if (KeyspaceIdentifier.isValid(referenceKey2)) {
                    KeyspaceIdentifier identifier2 = KeyspaceIdentifier.of(referenceKey2);
                    Map<byte[], byte[]> rawHash2 = MappingRedisConverter.this.referenceResolver.resolveReference(identifier2.getId(), identifier2.getKeyspace());
                    if (!CollectionUtils.isEmpty((Map<?, ?>) rawHash2)) {
                        accessor.setProperty(association.getInverse(), MappingRedisConverter.this.read((Class) ((KeyValuePersistentProperty) association.getInverse()).getActualType(), new RedisData(rawHash2)));
                    }
                }
            }
        });
    }

    @Override // org.springframework.data.convert.EntityWriter
    public void write(Object source, RedisData sink) {
        if (source == null) {
            return;
        }
        if (source instanceof PartialUpdate) {
            writePartialUpdate((PartialUpdate) source, sink);
            return;
        }
        RedisPersistentEntity entity = this.mappingContext.getPersistentEntity(source.getClass());
        if (!this.customConversions.hasCustomWriteTarget(source.getClass())) {
            this.typeMapper.writeType(ClassUtils.getUserClass(source), (Class<?>) sink);
        }
        if (entity == null) {
            this.typeMapper.writeType(ClassUtils.getUserClass(source), (Class<?>) sink);
            sink.getBucket().put("_raw", (byte[]) this.conversionService.convert(source, byte[].class));
            return;
        }
        sink.setKeyspace(entity.getKeySpace());
        writeInternal(entity.getKeySpace(), "", source, entity.getTypeInformation(), sink);
        sink.setId((String) getConversionService().convert(entity.getIdentifierAccessor(source).getIdentifier(), String.class));
        Long ttl = entity.getTimeToLiveAccessor().getTimeToLive(source);
        if (ttl != null && ttl.longValue() > 0) {
            sink.setTimeToLive(ttl);
        }
        for (IndexedData indexedData : this.indexResolver.resolveIndexesFor(entity.getTypeInformation(), source)) {
            sink.addIndexedData(indexedData);
        }
    }

    protected void writePartialUpdate(PartialUpdate<?> update, RedisData sink) {
        Long ttl;
        RedisPersistentEntity<?> entity = this.mappingContext.getPersistentEntity(update.getTarget());
        write(update.getValue(), sink);
        if (sink.getBucket().keySet().contains("_class")) {
            sink.getBucket().put("_class", null);
        }
        if (update.isRefreshTtl() && !update.getPropertyUpdates().isEmpty() && (ttl = entity.getTimeToLiveAccessor().getTimeToLive(update)) != null && ttl.longValue() > 0) {
            sink.setTimeToLive(ttl);
        }
        for (PartialUpdate.PropertyUpdate pUpdate : update.getPropertyUpdates()) {
            String path = pUpdate.getPropertyPath();
            if (PartialUpdate.UpdateCommand.SET.equals(pUpdate.getCmd())) {
                writePartialPropertyUpdate(update, pUpdate, sink, entity, path);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v29, types: [org.springframework.data.mapping.PersistentProperty] */
    /* JADX WARN: Type inference failed for: r1v41, types: [org.springframework.data.mapping.PersistentProperty] */
    private void writePartialPropertyUpdate(PartialUpdate<?> update, PartialUpdate.PropertyUpdate pUpdate, RedisData sink, RedisPersistentEntity<?> entity, String path) {
        TypeInformation<?> actualType;
        KeyValuePersistentProperty targetProperty = getTargetPropertyOrNullForPath(path, update.getTarget());
        if (targetProperty == null) {
            KeyValuePersistentProperty targetProperty2 = getTargetPropertyOrNullForPath(path.replaceAll("\\.\\[.*\\]", ""), update.getTarget());
            if (targetProperty2 == null) {
                actualType = ClassTypeInformation.OBJECT;
            } else if (targetProperty2.isMap()) {
                actualType = targetProperty2.getTypeInformation().getMapValueType() != null ? targetProperty2.getTypeInformation().getMapValueType() : ClassTypeInformation.OBJECT;
            } else {
                actualType = targetProperty2.getTypeInformation().getActualType();
            }
            TypeInformation<?> ti = actualType;
            writeInternal(entity.getKeySpace(), pUpdate.getPropertyPath(), pUpdate.getValue(), ti, sink);
            return;
        }
        if (targetProperty.isAssociation()) {
            if (targetProperty.isCollectionLike()) {
                KeyValuePersistentEntity<?> ref = this.mappingContext.getPersistentEntity(((KeyValuePersistentProperty) targetProperty.getAssociation().getInverse()).getTypeInformation().getComponentType().getActualType());
                int i = 0;
                for (Object o : (Collection) pUpdate.getValue()) {
                    Object refId = ref.getPropertyAccessor(o).getProperty(ref.getIdProperty());
                    sink.getBucket().put(pUpdate.getPropertyPath() + ".[" + i + "]", toBytes(ref.getKeySpace() + ":" + refId));
                    i++;
                }
                return;
            }
            KeyValuePersistentEntity<?> ref2 = this.mappingContext.getPersistentEntity(((KeyValuePersistentProperty) targetProperty.getAssociation().getInverse()).getTypeInformation());
            Object refId2 = ref2.getPropertyAccessor(pUpdate.getValue()).getProperty(ref2.getIdProperty());
            sink.getBucket().put(pUpdate.getPropertyPath(), toBytes(ref2.getKeySpace() + ":" + refId2));
            return;
        }
        if (targetProperty.isCollectionLike()) {
            Collection<?> collection = pUpdate.getValue() instanceof Collection ? (Collection) pUpdate.getValue() : Collections.singleton(pUpdate.getValue());
            writeCollection(entity.getKeySpace(), pUpdate.getPropertyPath(), collection, targetProperty.getTypeInformation().getActualType(), sink);
            return;
        }
        if (targetProperty.isMap()) {
            Map<Object, Object> map = new HashMap<>();
            if (pUpdate.getValue() instanceof Map) {
                map.putAll((Map) pUpdate.getValue());
            } else if (pUpdate.getValue() instanceof Map.Entry) {
                map.put(((Map.Entry) pUpdate.getValue()).getKey(), ((Map.Entry) pUpdate.getValue()).getValue());
            } else {
                throw new MappingException(String.format("Cannot set update value for map property '%s' to '%s'. Please use a Map or Map.Entry.", pUpdate.getPropertyPath(), pUpdate.getValue()));
            }
            writeMap(entity.getKeySpace(), pUpdate.getPropertyPath(), targetProperty.getMapValueType(), map, sink);
            return;
        }
        writeInternal(entity.getKeySpace(), pUpdate.getPropertyPath(), pUpdate.getValue(), targetProperty.getTypeInformation(), sink);
        Set<IndexedData> data = this.indexResolver.resolveIndexesFor(entity.getKeySpace(), pUpdate.getPropertyPath(), targetProperty.getTypeInformation(), pUpdate.getValue());
        if (data.isEmpty()) {
            data = this.indexResolver.resolveIndexesFor(entity.getKeySpace(), pUpdate.getPropertyPath(), targetProperty.getOwner().getTypeInformation(), pUpdate.getValue());
        }
        sink.addIndexedData(data);
    }

    KeyValuePersistentProperty getTargetPropertyOrNullForPath(String path, Class<?> type) {
        try {
            PersistentPropertyPath<KeyValuePersistentProperty> persistentPropertyPath = this.mappingContext.getPersistentPropertyPath(path, type);
            return (KeyValuePersistentProperty) persistentPropertyPath.getLeafProperty();
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeInternal(final String keyspace, final String path, Object value, TypeInformation<?> typeHint, final RedisData sink) {
        if (value == null) {
            return;
        }
        if (this.customConversions.hasCustomWriteTarget(value.getClass())) {
            if (!StringUtils.hasText(path) && this.customConversions.getCustomWriteTarget(value.getClass()).equals(byte[].class)) {
                sink.getBucket().put(StringUtils.hasText(path) ? path : "_raw", (byte[]) this.conversionService.convert(value, byte[].class));
                return;
            } else {
                if (!ClassUtils.isAssignable(typeHint.getType(), value.getClass())) {
                    throw new MappingException(String.format(INVALID_TYPE_ASSIGNMENT, value.getClass(), path, typeHint.getType()));
                }
                writeToBucket(path, value, sink, typeHint.getType());
                return;
            }
        }
        if (value.getClass() != typeHint.getType()) {
            sink.getBucket().put(!path.isEmpty() ? path + "._class" : "_class", toBytes(value.getClass().getName()));
        }
        KeyValuePersistentEntity<?> entity = this.mappingContext.getPersistentEntity(value.getClass());
        final PersistentPropertyAccessor accessor = entity.getPropertyAccessor(value);
        entity.doWithProperties(new PropertyHandler<KeyValuePersistentProperty>() { // from class: org.springframework.data.redis.core.convert.MappingRedisConverter.3
            @Override // org.springframework.data.mapping.PropertyHandler
            public void doWithPersistentProperty(KeyValuePersistentProperty persistentProperty) {
                String propertyStringPath = (!path.isEmpty() ? path + "." : "") + persistentProperty.getName();
                if (persistentProperty.isIdProperty()) {
                    sink.getBucket().put(propertyStringPath, MappingRedisConverter.this.toBytes(accessor.getProperty(persistentProperty)));
                    return;
                }
                if (persistentProperty.isMap()) {
                    MappingRedisConverter.this.writeMap(keyspace, propertyStringPath, persistentProperty.getMapValueType(), (Map) accessor.getProperty(persistentProperty), sink);
                    return;
                }
                if (!persistentProperty.isCollectionLike()) {
                    if (persistentProperty.isEntity()) {
                        MappingRedisConverter.this.writeInternal(keyspace, propertyStringPath, accessor.getProperty(persistentProperty), persistentProperty.getTypeInformation().getActualType(), sink);
                        return;
                    } else {
                        Object propertyValue = accessor.getProperty(persistentProperty);
                        MappingRedisConverter.this.writeToBucket(propertyStringPath, propertyValue, sink, persistentProperty.getType());
                        return;
                    }
                }
                Object property = accessor.getProperty(persistentProperty);
                if (property == null || Iterable.class.isAssignableFrom(property.getClass())) {
                    MappingRedisConverter.this.writeCollection(keyspace, propertyStringPath, (Iterable) property, persistentProperty.getTypeInformation().getComponentType(), sink);
                } else {
                    if (property.getClass().isArray()) {
                        MappingRedisConverter.this.writeCollection(keyspace, propertyStringPath, CollectionUtils.arrayToList(property), persistentProperty.getTypeInformation().getComponentType(), sink);
                        return;
                    }
                    throw new RuntimeException("Don't know how to handle " + property.getClass() + " type collection");
                }
            }
        });
        writeAssociation(path, entity, value, sink);
    }

    private void writeAssociation(final String path, KeyValuePersistentEntity<?> entity, Object value, final RedisData sink) {
        if (value == null) {
            return;
        }
        final PersistentPropertyAccessor accessor = entity.getPropertyAccessor(value);
        entity.doWithAssociations(new AssociationHandler<KeyValuePersistentProperty>() { // from class: org.springframework.data.redis.core.convert.MappingRedisConverter.4
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r1v42, types: [org.springframework.data.mapping.PersistentProperty] */
            /* JADX WARN: Type inference failed for: r1v8, types: [org.springframework.data.mapping.PersistentProperty] */
            @Override // org.springframework.data.mapping.AssociationHandler
            public void doWithAssociation(Association<KeyValuePersistentProperty> association) {
                Object refObject = accessor.getProperty(association.getInverse());
                if (refObject == null) {
                    return;
                }
                if (((KeyValuePersistentProperty) association.getInverse()).isCollectionLike()) {
                    KeyValuePersistentEntity<?> ref = MappingRedisConverter.this.mappingContext.getPersistentEntity(((KeyValuePersistentProperty) association.getInverse()).getTypeInformation().getComponentType().getActualType());
                    String keyspace = ref.getKeySpace();
                    String propertyStringPath = (!path.isEmpty() ? path + "." : "") + ((KeyValuePersistentProperty) association.getInverse()).getName();
                    int i = 0;
                    for (Object o : (Collection) refObject) {
                        Object refId = ref.getPropertyAccessor(o).getProperty(ref.getIdProperty());
                        sink.getBucket().put(propertyStringPath + ".[" + i + "]", MappingRedisConverter.this.toBytes(keyspace + ":" + refId));
                        i++;
                    }
                    return;
                }
                KeyValuePersistentEntity<?> ref2 = MappingRedisConverter.this.mappingContext.getPersistentEntity(((KeyValuePersistentProperty) association.getInverse()).getTypeInformation());
                String keyspace2 = ref2.getKeySpace();
                Object refId2 = ref2.getPropertyAccessor(refObject).getProperty(ref2.getIdProperty());
                String propertyStringPath2 = (!path.isEmpty() ? path + "." : "") + ((KeyValuePersistentProperty) association.getInverse()).getName();
                sink.getBucket().put(propertyStringPath2, MappingRedisConverter.this.toBytes(keyspace2 + ":" + refId2));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeCollection(String keyspace, String path, Iterable<?> values, TypeInformation<?> typeHint, RedisData sink) {
        Object value;
        if (values == null) {
            return;
        }
        int i = 0;
        Iterator<?> it = values.iterator();
        while (it.hasNext() && (value = it.next()) != null) {
            String currentPath = path + ".[" + i + "]";
            if (!ClassUtils.isAssignable(typeHint.getType(), value.getClass())) {
                throw new MappingException(String.format(INVALID_TYPE_ASSIGNMENT, value.getClass(), currentPath, typeHint.getType()));
            }
            if (this.customConversions.hasCustomWriteTarget(value.getClass())) {
                writeToBucket(currentPath, value, sink, typeHint.getType());
            } else {
                writeInternal(keyspace, currentPath, value, typeHint, sink);
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeToBucket(String path, Object value, RedisData sink, Class<?> propertyType) {
        if (value != null && this.customConversions.hasCustomWriteTarget(value.getClass())) {
            Class<?> targetType = this.customConversions.getCustomWriteTarget(value.getClass());
            if (!ClassUtils.isAssignable(Map.class, targetType) && this.customConversions.isSimpleType(value.getClass()) && value.getClass() != propertyType) {
                sink.getBucket().put(!path.isEmpty() ? path + "._class" : "_class", toBytes(value.getClass().getName()));
            }
            if (!ClassUtils.isAssignable(Map.class, targetType)) {
                if (ClassUtils.isAssignable(byte[].class, targetType)) {
                    sink.getBucket().put(path, toBytes(value));
                    return;
                }
                throw new IllegalArgumentException(String.format("Cannot convert value '%s' of type %s to bytes.", value, value.getClass()));
            }
            Map<?, ?> map = (Map) this.conversionService.convert(value, targetType);
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                sink.getBucket().put(path + (StringUtils.hasText(path) ? "." : "") + entry.getKey(), toBytes(entry.getValue()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object readCollectionOrArray(String path, Class<?> collectionType, Class<?> valueType, Bucket bucket) {
        List<String> keys = new ArrayList<>(bucket.extractAllKeysFor(path));
        Collections.sort(keys, this.listKeyComparator);
        boolean isArray = collectionType.isArray();
        Class<?> collectionTypeToUse = isArray ? ArrayList.class : collectionType;
        Collection<Object> target = CollectionFactory.createCollection(collectionTypeToUse, valueType, keys.size());
        for (String key : keys) {
            if (!key.endsWith("_class")) {
                Bucket elementData = bucket.extract(key);
                byte[] typeInfo = elementData.get(key + "._class");
                if (typeInfo != null && typeInfo.length > 0) {
                    elementData.put("_class", typeInfo);
                }
                Class<?> typeToUse = getTypeHint(key, elementData, valueType);
                if (this.conversionService.canConvert(byte[].class, typeToUse)) {
                    target.add(fromBytes(elementData.get(key), typeToUse));
                } else {
                    target.add(readInternal(key, valueType, new RedisData(elementData)));
                }
            }
        }
        if (isArray) {
            return toArray(target, collectionType, valueType);
        }
        if (target.isEmpty()) {
            return null;
        }
        return target;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeMap(String keyspace, String path, Class<?> mapValueType, Map<?, ?> source, RedisData sink) {
        if (CollectionUtils.isEmpty(source)) {
            return;
        }
        for (Map.Entry<?, ?> entry : source.entrySet()) {
            if (entry.getValue() != null && entry.getKey() != null) {
                String currentPath = path + ".[" + mapMapKey(entry.getKey()) + "]";
                if (!ClassUtils.isAssignable(mapValueType, entry.getValue().getClass())) {
                    throw new MappingException(String.format(INVALID_TYPE_ASSIGNMENT, entry.getValue().getClass(), currentPath, mapValueType));
                }
                if (this.customConversions.hasCustomWriteTarget(entry.getValue().getClass())) {
                    writeToBucket(currentPath, entry.getValue(), sink, mapValueType);
                } else {
                    writeInternal(keyspace, currentPath, entry.getValue(), ClassTypeInformation.from(mapValueType), sink);
                }
            }
        }
    }

    private String mapMapKey(Object key) {
        if (this.conversionService.canConvert(key.getClass(), byte[].class)) {
            return new String((byte[]) this.conversionService.convert(key, byte[].class));
        }
        return (String) this.conversionService.convert(key, String.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<?, ?> readMapOfSimpleTypes(String path, Class<?> mapType, Class<?> keyType, Class<?> valueType, RedisData source) {
        Bucket partial = source.getBucket().extract(path + ".[");
        Map<Object, Object> target = CollectionFactory.createMap(mapType, partial.size());
        for (Map.Entry<String, byte[]> entry : partial.entrySet()) {
            if (!entry.getKey().endsWith("_class")) {
                Object key = extractMapKeyForPath(path, entry.getKey(), keyType);
                Class<?> typeToUse = getTypeHint(path + ".[" + key + "]", source.getBucket(), valueType);
                target.put(key, fromBytes(entry.getValue(), typeToUse));
            }
        }
        if (target.isEmpty()) {
            return null;
        }
        return target;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<?, ?> readMapOfComplexTypes(String path, Class<?> mapType, Class<?> keyType, Class<?> valueType, RedisData source) {
        Set<String> keys = source.getBucket().extractAllKeysFor(path);
        Map<Object, Object> target = CollectionFactory.createMap(mapType, keys.size());
        for (String key : keys) {
            Bucket partial = source.getBucket().extract(key);
            byte[] typeInfo = partial.get(key + "._class");
            if (typeInfo != null && typeInfo.length > 0) {
                partial.put("_class", typeInfo);
            }
            Object value = readInternal(key, valueType, new RedisData(partial));
            Object mapKey = extractMapKeyForPath(path, key, keyType);
            target.put(mapKey, value);
        }
        if (target.isEmpty()) {
            return null;
        }
        return target;
    }

    private Object extractMapKeyForPath(String path, String key, Class<?> targetType) {
        String regex = "^(" + Pattern.quote(path) + "\\.\\[)(.*?)(\\])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(key);
        if (!matcher.find()) {
            throw new IllegalArgumentException(String.format("Cannot extract map value for key '%s' in path '%s'.", key, path));
        }
        Object mapKey = matcher.group(2);
        if (ClassUtils.isAssignable(targetType, mapKey.getClass())) {
            return mapKey;
        }
        return this.conversionService.convert(toBytes(mapKey), targetType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Class<?> getTypeHint(String path, Bucket bucket, Class<?> fallback) {
        byte[] typeInfo = bucket.get(path + "._class");
        if (typeInfo == null || typeInfo.length < 1) {
            return fallback;
        }
        String typeName = (String) fromBytes(typeInfo, String.class);
        try {
            return ClassUtils.forName(typeName, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new MappingException(String.format("Cannot find class for type %s. ", typeName), e);
        } catch (LinkageError e2) {
            throw new MappingException(String.format("Cannot find class for type %s. ", typeName), e2);
        }
    }

    public byte[] toBytes(Object source) {
        if (source instanceof byte[]) {
            return (byte[]) source;
        }
        return (byte[]) this.conversionService.convert(source, byte[].class);
    }

    public <T> T fromBytes(byte[] bArr, Class<T> cls) {
        return (T) this.conversionService.convert(bArr, cls);
    }

    private Object toArray(Collection<Object> source, Class<?> arrayType, Class<?> valueType) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        if (source.isEmpty()) {
            return null;
        }
        if (!ClassUtils.isPrimitiveArray(arrayType)) {
            return source.toArray((Object[]) Array.newInstance(valueType, source.size()));
        }
        Object targetArray = Array.newInstance(valueType, source.size());
        Iterator<Object> iterator = source.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Array.set(targetArray, i, this.conversionService.convert(iterator.next(), valueType));
            i++;
        }
        if (i > 0) {
            return targetArray;
        }
        return null;
    }

    public void setCustomConversions(CustomConversions customConversions) {
        this.customConversions = customConversions != null ? customConversions : new CustomConversions();
    }

    public void setReferenceResolver(ReferenceResolver referenceResolver) {
        this.referenceResolver = referenceResolver;
    }

    public void setIndexResolver(IndexResolver indexResolver) {
        this.indexResolver = indexResolver;
    }

    @Override // org.springframework.data.convert.EntityConverter
    public RedisMappingContext getMappingContext() {
        return this.mappingContext;
    }

    @Override // org.springframework.data.convert.EntityConverter
    public ConversionService getConversionService() {
        return this.conversionService;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        initializeConverters();
    }

    private void initializeConverters() {
        this.customConversions.registerConvertersIn(this.conversionService);
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/MappingRedisConverter$ConverterAwareParameterValueProvider.class */
    private static class ConverterAwareParameterValueProvider implements PropertyValueProvider<KeyValuePersistentProperty> {
        private final String path;
        private final RedisData source;
        private final ConversionService conversionService;

        public ConverterAwareParameterValueProvider(String path, RedisData source, ConversionService conversionService) {
            this.path = path;
            this.source = source;
            this.conversionService = conversionService;
        }

        @Override // org.springframework.data.mapping.model.PropertyValueProvider
        public <T> T getPropertyValue(KeyValuePersistentProperty keyValuePersistentProperty) {
            return (T) this.conversionService.convert(this.source.getBucket().get(StringUtils.hasText(this.path) ? this.path + "." + keyValuePersistentProperty.getName() : keyValuePersistentProperty.getName()), keyValuePersistentProperty.getActualType());
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/MappingRedisConverter$RedisTypeAliasAccessor.class */
    private static class RedisTypeAliasAccessor implements TypeAliasAccessor<RedisData> {
        private final String typeKey;
        private final ConversionService conversionService;

        RedisTypeAliasAccessor(ConversionService conversionService) {
            this(conversionService, "_class");
        }

        RedisTypeAliasAccessor(ConversionService conversionService, String typeKey) {
            this.conversionService = conversionService;
            this.typeKey = typeKey;
        }

        @Override // org.springframework.data.convert.TypeAliasAccessor
        public Object readAliasFrom(RedisData source) {
            return this.conversionService.convert(source.getBucket().get(this.typeKey), String.class);
        }

        @Override // org.springframework.data.convert.TypeAliasAccessor
        public void writeTypeTo(RedisData sink, Object alias) {
            sink.getBucket().put(this.typeKey, (byte[]) this.conversionService.convert(alias, byte[].class));
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/MappingRedisConverter$ClassNameKeySpaceResolver.class */
    enum ClassNameKeySpaceResolver implements KeySpaceResolver {
        INSTANCE;

        @Override // org.springframework.data.keyvalue.core.mapping.KeySpaceResolver
        public String resolveKeySpace(Class<?> type) {
            Assert.notNull(type, "Type must not be null!");
            return ClassUtils.getUserClass(type).getName();
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/MappingRedisConverter$NaturalOrderingKeyComparator.class */
    private enum NaturalOrderingKeyComparator implements Comparator<String> {
        INSTANCE;

        @Override // java.util.Comparator
        public int compare(String s1, String s2) {
            int s1offset = 0;
            int length = 0;
            while (true) {
                int s2offset = length;
                if (s1offset < s1.length() && s2offset < s2.length()) {
                    Part thisPart = extractPart(s1, s1offset);
                    Part thatPart = extractPart(s2, s2offset);
                    int result = thisPart.compareTo(thatPart);
                    if (result != 0) {
                        return result;
                    }
                    s1offset += thisPart.length();
                    length = s2offset + thatPart.length();
                } else {
                    return 0;
                }
            }
        }

        private Part extractPart(String source, int offset) {
            StringBuilder builder = new StringBuilder();
            char c = source.charAt(offset);
            builder.append(c);
            boolean isDigit = Character.isDigit(c);
            for (int i = offset + 1; i < source.length(); i++) {
                char c2 = source.charAt(i);
                if ((isDigit && !Character.isDigit(c2)) || (!isDigit && Character.isDigit(c2))) {
                    break;
                }
                builder.append(c2);
            }
            return new Part(builder.toString(), isDigit);
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/MappingRedisConverter$NaturalOrderingKeyComparator$Part.class */
        private static class Part implements Comparable<Part> {
            private final String rawValue;
            private final Long longValue;

            Part(String value, boolean isDigit) {
                this.rawValue = value;
                this.longValue = isDigit ? Long.valueOf(value) : null;
            }

            boolean isNumeric() {
                return this.longValue != null;
            }

            int length() {
                return this.rawValue.length();
            }

            @Override // java.lang.Comparable
            public int compareTo(Part that) {
                if (isNumeric() && that.isNumeric()) {
                    return this.longValue.compareTo(that.longValue);
                }
                return this.rawValue.compareTo(that.rawValue);
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/MappingRedisConverter$KeyspaceIdentifier.class */
    public static class KeyspaceIdentifier {
        public static final String PHANTOM = "phantom";
        public static final String DELIMITTER = ":";
        public static final String PHANTOM_SUFFIX = ":phantom";
        private String keyspace;
        private String id;
        private boolean phantomKey;

        private KeyspaceIdentifier(String keyspace, String id, boolean phantomKey) {
            this.keyspace = keyspace;
            this.id = id;
            this.phantomKey = phantomKey;
        }

        public String getKeyspace() {
            return this.keyspace;
        }

        public String getId() {
            return this.id;
        }

        public boolean isPhantomKey() {
            return this.phantomKey;
        }

        public static KeyspaceIdentifier of(String key) {
            String id;
            Assert.isTrue(isValid(key), String.format("Invalid key %s", key));
            boolean phantomKey = key.endsWith(PHANTOM_SUFFIX);
            int keyspaceEndIndex = key.indexOf(":");
            String keyspace = key.substring(0, keyspaceEndIndex);
            if (phantomKey) {
                id = key.substring(keyspaceEndIndex + 1, key.length() - PHANTOM_SUFFIX.length());
            } else {
                id = key.substring(keyspaceEndIndex + 1);
            }
            return new KeyspaceIdentifier(keyspace, id, phantomKey);
        }

        public static boolean isValid(String key) {
            int keyspaceEndIndex;
            return key != null && (keyspaceEndIndex = key.indexOf(":")) > 0 && key.length() > keyspaceEndIndex;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/MappingRedisConverter$BinaryKeyspaceIdentifier.class */
    public static class BinaryKeyspaceIdentifier {
        public static final byte DELIMITTER = 58;
        private byte[] keyspace;
        private byte[] id;
        private boolean phantomKey;
        public static final byte[] PHANTOM = KeyspaceIdentifier.PHANTOM.getBytes();
        public static final byte[] PHANTOM_SUFFIX = ByteUtils.concat(new byte[]{58}, PHANTOM);

        private BinaryKeyspaceIdentifier(byte[] keyspace, byte[] id, boolean phantomKey) {
            this.keyspace = keyspace;
            this.id = id;
            this.phantomKey = phantomKey;
        }

        public byte[] getKeyspace() {
            return this.keyspace;
        }

        public byte[] getId() {
            return this.id;
        }

        public boolean isPhantomKey() {
            return this.phantomKey;
        }

        public static BinaryKeyspaceIdentifier of(byte[] key) {
            Assert.isTrue(isValid(key), String.format("Invalid key %s", new String(key)));
            boolean phantomKey = ByteUtils.startsWith(key, PHANTOM_SUFFIX, key.length - PHANTOM_SUFFIX.length);
            int keyspaceEndIndex = ByteUtils.indexOf(key, (byte) 58);
            byte[] keyspace = extractKeyspace(key, keyspaceEndIndex);
            byte[] id = extractId(key, phantomKey, keyspaceEndIndex);
            return new BinaryKeyspaceIdentifier(keyspace, id, phantomKey);
        }

        public static boolean isValid(byte[] key) {
            int keyspaceEndIndex;
            return key != null && (keyspaceEndIndex = ByteUtils.indexOf(key, (byte) 58)) > 0 && key.length > keyspaceEndIndex;
        }

        private static byte[] extractId(byte[] key, boolean phantomKey, int keyspaceEndIndex) {
            int idSize;
            if (phantomKey) {
                idSize = (key.length - PHANTOM_SUFFIX.length) - (keyspaceEndIndex + 1);
            } else {
                idSize = key.length - (keyspaceEndIndex + 1);
            }
            byte[] id = new byte[idSize];
            System.arraycopy(key, keyspaceEndIndex + 1, id, 0, idSize);
            return id;
        }

        private static byte[] extractKeyspace(byte[] key, int keyspaceEndIndex) {
            byte[] keyspace = new byte[keyspaceEndIndex];
            System.arraycopy(key, 0, keyspace, 0, keyspaceEndIndex);
            return keyspace;
        }
    }
}
