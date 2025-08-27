package org.springframework.data.convert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.util.CacheValue;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/MappingContextTypeInformationMapper.class */
public class MappingContextTypeInformationMapper implements TypeInformationMapper {
    private final Map<ClassTypeInformation<?>, CacheValue<Object>> typeMap;
    private final MappingContext<? extends PersistentEntity<?, ?>, ?> mappingContext;

    public MappingContextTypeInformationMapper(MappingContext<? extends PersistentEntity<?, ?>, ?> mappingContext) {
        Assert.notNull(mappingContext, "MappingContext must not be null!");
        this.typeMap = new ConcurrentHashMap();
        this.mappingContext = mappingContext;
        for (E entity : mappingContext.getPersistentEntities()) {
            safelyAddToCache(entity.getTypeInformation().getRawTypeInformation(), entity.getTypeAlias());
        }
    }

    @Override // org.springframework.data.convert.TypeInformationMapper
    public Object createAliasFor(TypeInformation<?> type) {
        CacheValue<Object> key = this.typeMap.get(type);
        if (key != null) {
            return key.getValue();
        }
        PersistentEntity<?, ?> entity = this.mappingContext.getPersistentEntity(type);
        if (entity == null) {
            return null;
        }
        Object alias = entity.getTypeAlias();
        safelyAddToCache(type.getRawTypeInformation(), alias);
        return alias;
    }

    private void safelyAddToCache(ClassTypeInformation<?> key, Object alias) {
        CacheValue<Object> aliasToBeCached = CacheValue.ofNullable(alias);
        if (alias == null && !this.typeMap.containsKey(key)) {
            this.typeMap.put(key, aliasToBeCached);
            return;
        }
        CacheValue<Object> alreadyCachedAlias = this.typeMap.get(key);
        if (alreadyCachedAlias != null && alreadyCachedAlias.isPresent() && !alreadyCachedAlias.hasValue(alias)) {
            throw new IllegalArgumentException(String.format("Trying to register alias '%s', but found already registered alias '%s' for type %s!", alias, alreadyCachedAlias, key));
        }
        if (this.typeMap.containsValue(aliasToBeCached)) {
            for (Map.Entry<ClassTypeInformation<?>, CacheValue<Object>> entry : this.typeMap.entrySet()) {
                CacheValue<Object> value = entry.getValue();
                if (value.isPresent() && value.hasValue(alias) && !entry.getKey().equals(key)) {
                    throw new IllegalArgumentException(String.format("Detected existing type mapping of %s to alias '%s' but attempted to bind the same alias to %s!", key, alias, entry.getKey()));
                }
            }
        }
        this.typeMap.put(key, aliasToBeCached);
    }

    @Override // org.springframework.data.convert.TypeInformationMapper
    public ClassTypeInformation<?> resolveTypeFrom(Object alias) {
        if (alias == null) {
            return null;
        }
        for (Map.Entry<ClassTypeInformation<?>, CacheValue<Object>> entry : this.typeMap.entrySet()) {
            CacheValue<Object> cachedAlias = entry.getValue();
            if (cachedAlias.hasValue(alias)) {
                return entry.getKey();
            }
        }
        for (E entity : this.mappingContext.getPersistentEntities()) {
            if (alias.equals(entity.getTypeAlias())) {
                return entity.getTypeInformation().getRawTypeInformation();
            }
        }
        return null;
    }
}
