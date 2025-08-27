package org.springframework.data.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/DefaultTypeMapper.class */
public class DefaultTypeMapper<S> implements TypeMapper<S> {
    private final TypeAliasAccessor<S> accessor;
    private final List<? extends TypeInformationMapper> mappers;
    private final Map<Object, TypeInformation<?>> typeCache;

    public DefaultTypeMapper(TypeAliasAccessor<S> accessor) {
        this(accessor, Arrays.asList(new SimpleTypeInformationMapper()));
    }

    public DefaultTypeMapper(TypeAliasAccessor<S> accessor, List<? extends TypeInformationMapper> mappers) {
        this(accessor, null, mappers);
    }

    public DefaultTypeMapper(TypeAliasAccessor<S> accessor, MappingContext<? extends PersistentEntity<?, ?>, ?> mappingContext, List<? extends TypeInformationMapper> additionalMappers) {
        Assert.notNull(accessor, "Accessor must not be null!");
        Assert.notNull(additionalMappers, "AdditionalMappers must not be null!");
        List<TypeInformationMapper> mappers = new ArrayList<>(additionalMappers.size() + 1);
        if (mappingContext != null) {
            mappers.add(new MappingContextTypeInformationMapper(mappingContext));
        }
        mappers.addAll(additionalMappers);
        this.mappers = Collections.unmodifiableList(mappers);
        this.accessor = accessor;
        this.typeCache = new ConcurrentHashMap();
    }

    @Override // org.springframework.data.convert.TypeMapper
    public TypeInformation<?> readType(S source) {
        Assert.notNull(source, "Source object must not be null!");
        Object alias = this.accessor.readAliasFrom(source);
        if (alias == null) {
            return null;
        }
        return getFromCacheOrCreate(alias);
    }

    private TypeInformation<?> getFromCacheOrCreate(Object alias) {
        TypeInformation<?> typeInformation = this.typeCache.get(alias);
        if (typeInformation != null) {
            return typeInformation;
        }
        for (TypeInformationMapper mapper : this.mappers) {
            typeInformation = mapper.resolveTypeFrom(alias);
            if (typeInformation != null) {
                this.typeCache.put(alias, typeInformation);
                return typeInformation;
            }
        }
        return typeInformation;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.convert.TypeMapper
    public <T> TypeInformation<? extends T> readType(S s, TypeInformation<T> typeInformation) {
        boolean z;
        Assert.notNull(s, "Source object must not be null!");
        Class<?> defaultedTypeToBeUsed = getDefaultedTypeToBeUsed(s);
        if (defaultedTypeToBeUsed == null) {
            return typeInformation;
        }
        Class<T> type = typeInformation == 0 ? null : typeInformation.getType();
        if (type == null) {
            z = true;
        } else {
            z = type.isAssignableFrom(defaultedTypeToBeUsed) && !type.equals(defaultedTypeToBeUsed);
        }
        if (!z) {
            return typeInformation;
        }
        ClassTypeInformation classTypeInformationFrom = ClassTypeInformation.from(defaultedTypeToBeUsed);
        return typeInformation != 0 ? (TypeInformation<? extends T>) typeInformation.specialize(classTypeInformationFrom) : classTypeInformationFrom;
    }

    private Class<?> getDefaultedTypeToBeUsed(S source) {
        TypeInformation<?> documentsTargetTypeInformation = readType(source);
        TypeInformation<?> documentsTargetTypeInformation2 = documentsTargetTypeInformation == null ? getFallbackTypeFor(source) : documentsTargetTypeInformation;
        if (documentsTargetTypeInformation2 == null) {
            return null;
        }
        return documentsTargetTypeInformation2.getType();
    }

    protected TypeInformation<?> getFallbackTypeFor(S source) {
        return null;
    }

    @Override // org.springframework.data.convert.TypeMapper
    public void writeType(Class<?> type, S dbObject) {
        writeType((TypeInformation<?>) ClassTypeInformation.from(type), (ClassTypeInformation) dbObject);
    }

    @Override // org.springframework.data.convert.TypeMapper
    public void writeType(TypeInformation<?> info, S sink) {
        Assert.notNull(info, "TypeInformation must not be null!");
        Object alias = getAliasFor(info);
        if (alias != null) {
            this.accessor.writeTypeTo(sink, alias);
        }
    }

    protected final Object getAliasFor(TypeInformation<?> info) {
        Assert.notNull(info, "TypeInformation must not be null!");
        for (TypeInformationMapper mapper : this.mappers) {
            Object alias = mapper.createAliasFor(info);
            if (alias != null) {
                return alias;
            }
        }
        return null;
    }
}
