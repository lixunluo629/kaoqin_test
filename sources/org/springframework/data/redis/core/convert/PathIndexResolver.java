package org.springframework.data.redis.core.convert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.data.geo.Point;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.index.ConfigurableIndexDefinitionProvider;
import org.springframework.data.redis.core.index.GeoIndexDefinition;
import org.springframework.data.redis.core.index.GeoIndexed;
import org.springframework.data.redis.core.index.IndexDefinition;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.data.redis.core.index.SimpleIndexDefinition;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.core.mapping.RedisPersistentEntity;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/PathIndexResolver.class */
public class PathIndexResolver implements IndexResolver {
    private final Set<Class<?>> VALUE_TYPES;
    private ConfigurableIndexDefinitionProvider indexConfiguration;
    private RedisMappingContext mappingContext;
    private IndexedDataFactoryProvider indexedDataFactoryProvider;

    public PathIndexResolver() {
        this(new RedisMappingContext());
    }

    public PathIndexResolver(RedisMappingContext mappingContext) {
        this.VALUE_TYPES = new HashSet(Arrays.asList(Point.class, RedisGeoCommands.GeoLocation.class));
        Assert.notNull(mappingContext, "MappingContext must not be null!");
        this.mappingContext = mappingContext;
        this.indexConfiguration = mappingContext.getMappingConfiguration().getIndexConfiguration();
        this.indexedDataFactoryProvider = new IndexedDataFactoryProvider();
    }

    @Override // org.springframework.data.redis.core.convert.IndexResolver
    public Set<IndexedData> resolveIndexesFor(TypeInformation<?> typeInformation, Object value) {
        return doResolveIndexesFor(this.mappingContext.getPersistentEntity(typeInformation).getKeySpace(), "", typeInformation, null, value);
    }

    @Override // org.springframework.data.redis.core.convert.IndexResolver
    public Set<IndexedData> resolveIndexesFor(String keyspace, String path, TypeInformation<?> typeInformation, Object value) {
        return doResolveIndexesFor(keyspace, path, typeInformation, null, value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<IndexedData> doResolveIndexesFor(final String keyspace, final String path, TypeInformation<?> typeInformation, PersistentProperty<?> fallback, Object value) {
        RedisPersistentEntity<?> entity = this.mappingContext.getPersistentEntity(typeInformation);
        if (entity == null || (value != null && this.VALUE_TYPES.contains(value.getClass()))) {
            return resolveIndex(keyspace, path, fallback, value);
        }
        if (!ClassUtils.isAssignable(entity.getType(), value.getClass())) {
            String propertyName = path.lastIndexOf(46) > 0 ? path.substring(path.lastIndexOf(46) + 1, path.length()) : path;
            return resolveIndex(keyspace, path, entity.getPersistentProperty(propertyName), value);
        }
        final PersistentPropertyAccessor accessor = entity.getPropertyAccessor(value);
        final Set<IndexedData> indexes = new LinkedHashSet<>();
        entity.doWithProperties(new PropertyHandler<KeyValuePersistentProperty>() { // from class: org.springframework.data.redis.core.convert.PathIndexResolver.1
            @Override // org.springframework.data.mapping.PropertyHandler
            public void doWithPersistentProperty(KeyValuePersistentProperty persistentProperty) {
                TypeInformation<?> actualType;
                Iterable<?> iterable;
                String currentPath = !path.isEmpty() ? path + "." + persistentProperty.getName() : persistentProperty.getName();
                Object propertyValue = accessor.getProperty(persistentProperty);
                if (propertyValue != null) {
                    if (persistentProperty.isMap()) {
                        actualType = persistentProperty.getTypeInformation().getMapValueType();
                    } else {
                        actualType = persistentProperty.getTypeInformation().getActualType();
                    }
                    TypeInformation<?> typeHint = actualType;
                    if (persistentProperty.isMap()) {
                        for (Map.Entry<?, ?> entry : ((Map) propertyValue).entrySet()) {
                            TypeInformation<?> typeToUse = updateTypeHintForActualValue(typeHint, entry.getValue());
                            indexes.addAll(PathIndexResolver.this.doResolveIndexesFor(keyspace, currentPath + "." + entry.getKey(), typeToUse.getActualType(), persistentProperty, entry.getValue()));
                        }
                        return;
                    }
                    if (persistentProperty.isCollectionLike()) {
                        if (Iterable.class.isAssignableFrom(propertyValue.getClass())) {
                            iterable = (Iterable) propertyValue;
                        } else if (propertyValue.getClass().isArray()) {
                            iterable = CollectionUtils.arrayToList(propertyValue);
                        } else {
                            throw new RuntimeException("Don't know how to handle " + propertyValue.getClass() + " type of collection");
                        }
                        for (Object listValue : iterable) {
                            if (listValue != null) {
                                TypeInformation<?> typeToUse2 = updateTypeHintForActualValue(typeHint, listValue);
                                indexes.addAll(PathIndexResolver.this.doResolveIndexesFor(keyspace, currentPath, typeToUse2.getActualType(), persistentProperty, listValue));
                            }
                        }
                        return;
                    }
                    if (persistentProperty.isEntity() || persistentProperty.getTypeInformation().getActualType().equals(ClassTypeInformation.OBJECT)) {
                        indexes.addAll(PathIndexResolver.this.doResolveIndexesFor(keyspace, currentPath, updateTypeHintForActualValue(typeHint, propertyValue).getActualType(), persistentProperty, propertyValue));
                    } else {
                        indexes.addAll(PathIndexResolver.this.resolveIndex(keyspace, currentPath, persistentProperty, propertyValue));
                    }
                }
            }

            private TypeInformation<?> updateTypeHintForActualValue(TypeInformation<?> typeHint, Object propertyValue) {
                if (typeHint.equals(ClassTypeInformation.OBJECT) || typeHint.getClass().isInterface()) {
                    try {
                        typeHint = PathIndexResolver.this.mappingContext.getPersistentEntity(propertyValue.getClass()).getTypeInformation();
                    } catch (Exception e) {
                    }
                }
                return typeHint;
            }
        });
        return indexes;
    }

    protected Set<IndexedData> resolveIndex(String keyspace, String propertyPath, PersistentProperty<?> property, Object value) {
        IndexedData indexedDataCreateIndexedDataFor;
        String path = normalizeIndexPath(propertyPath, property);
        Set<IndexedData> data = new LinkedHashSet<>();
        if (this.indexConfiguration.hasIndexFor(keyspace, path)) {
            IndexDefinition.IndexingContext context = new IndexDefinition.IndexingContext(keyspace, path, property != null ? property.getTypeInformation() : ClassTypeInformation.OBJECT);
            for (IndexDefinition indexDefinition : this.indexConfiguration.getIndexDefinitionsFor(keyspace, path)) {
                if (verifyConditions(indexDefinition.getConditions(), value, context)) {
                    Object transformedValue = indexDefinition.valueTransformer().convert2(value);
                    if (transformedValue == null) {
                        indexedDataCreateIndexedDataFor = new RemoveIndexedData(null);
                    } else {
                        indexedDataCreateIndexedDataFor = this.indexedDataFactoryProvider.getIndexedDataFactory(indexDefinition).createIndexedDataFor(value);
                    }
                    IndexedData indexedData = indexedDataCreateIndexedDataFor;
                    data.add(indexedData);
                }
            }
        } else if (property != null && property.isAnnotationPresent(Indexed.class)) {
            SimpleIndexDefinition indexDefinition2 = new SimpleIndexDefinition(keyspace, path);
            this.indexConfiguration.addIndexDefinition(indexDefinition2);
            data.add(this.indexedDataFactoryProvider.getIndexedDataFactory(indexDefinition2).createIndexedDataFor(value));
        } else if (property != null && property.isAnnotationPresent(GeoIndexed.class)) {
            GeoIndexDefinition indexDefinition3 = new GeoIndexDefinition(keyspace, path);
            this.indexConfiguration.addIndexDefinition(indexDefinition3);
            data.add(this.indexedDataFactoryProvider.getIndexedDataFactory(indexDefinition3).createIndexedDataFor(value));
        }
        return data;
    }

    private boolean verifyConditions(Iterable<IndexDefinition.Condition<?>> conditions, Object value, IndexDefinition.IndexingContext context) {
        for (IndexDefinition.Condition condition : conditions) {
            if (!condition.matches(value, context)) {
                return false;
            }
        }
        return true;
    }

    private String normalizeIndexPath(String path, PersistentProperty<?> property) {
        if (property == null) {
            return path;
        }
        if (property.isMap()) {
            return path.replaceAll("\\[", "").replaceAll("\\]", "");
        }
        if (property.isCollectionLike()) {
            return path.replaceAll("\\[(\\p{Digit})*\\]", "").replaceAll("\\.\\.", ".");
        }
        return path;
    }
}
