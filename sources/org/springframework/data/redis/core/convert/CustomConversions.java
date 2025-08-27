package org.springframework.data.redis.core.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.redis.core.convert.BinaryConverters;
import org.springframework.data.util.CacheValue;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/CustomConversions.class */
public class CustomConversions {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) CustomConversions.class);
    private static final String READ_CONVERTER_NOT_SIMPLE = "Registering converter from %s to %s as reading converter although it doesn't convert from a Redis supported type! You might wanna check you annotation setup at the converter implementation.";
    private static final String WRITE_CONVERTER_NOT_SIMPLE = "Registering converter from %s to %s as writing converter although it doesn't convert to a Redis supported type! You might wanna check you annotation setup at the converter implementation.";
    private final Set<GenericConverter.ConvertiblePair> readingPairs;
    private final Set<GenericConverter.ConvertiblePair> writingPairs;
    private final Set<Class<?>> customSimpleTypes;
    private final SimpleTypeHolder simpleTypeHolder;
    private final List<Object> converters;
    private final Map<GenericConverter.ConvertiblePair, CacheValue<Class<?>>> customReadTargetTypes;
    private final Map<GenericConverter.ConvertiblePair, CacheValue<Class<?>>> customWriteTargetTypes;
    private final Map<Class<?>, CacheValue<Class<?>>> rawWriteTargetTypes;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/CustomConversions$Producer.class */
    private interface Producer {
        Class<?> get();
    }

    public CustomConversions() {
        this(new ArrayList());
    }

    public CustomConversions(List<?> converters) {
        Assert.notNull(converters, "Converters must not be null!");
        this.readingPairs = new LinkedHashSet();
        this.writingPairs = new LinkedHashSet();
        this.customSimpleTypes = new HashSet();
        this.customReadTargetTypes = new ConcurrentHashMap();
        this.customWriteTargetTypes = new ConcurrentHashMap();
        this.rawWriteTargetTypes = new ConcurrentHashMap();
        List<Object> toRegister = new ArrayList<>();
        toRegister.addAll(converters);
        toRegister.add(new BinaryConverters.StringToBytesConverter());
        toRegister.add(new BinaryConverters.BytesToStringConverter());
        toRegister.add(new BinaryConverters.NumberToBytesConverter());
        toRegister.add(new BinaryConverters.BytesToNumberConverterFactory());
        toRegister.add(new BinaryConverters.EnumToBytesConverter());
        toRegister.add(new BinaryConverters.BytesToEnumConverterFactory());
        toRegister.add(new BinaryConverters.BooleanToBytesConverter());
        toRegister.add(new BinaryConverters.BytesToBooleanConverter());
        toRegister.add(new BinaryConverters.DateToBytesConverter());
        toRegister.add(new BinaryConverters.BytesToDateConverter());
        toRegister.addAll(Jsr310Converters.getConvertersToRegister());
        for (Object c : toRegister) {
            registerConversion(c);
        }
        Collections.reverse(toRegister);
        this.converters = Collections.unmodifiableList(toRegister);
        this.simpleTypeHolder = new SimpleTypeHolder((Set<? extends Class<?>>) this.customSimpleTypes, true);
    }

    public SimpleTypeHolder getSimpleTypeHolder() {
        return this.simpleTypeHolder;
    }

    public boolean isSimpleType(Class<?> type) {
        return this.simpleTypeHolder.isSimpleType(type);
    }

    public void registerConvertersIn(GenericConversionService conversionService) {
        for (Object converter : this.converters) {
            boolean added = false;
            if (converter instanceof Converter) {
                conversionService.addConverter((Converter<?, ?>) converter);
                added = true;
            }
            if (converter instanceof ConverterFactory) {
                conversionService.addConverterFactory((ConverterFactory) converter);
                added = true;
            }
            if (converter instanceof GenericConverter) {
                conversionService.addConverter((GenericConverter) converter);
                added = true;
            }
            if (!added) {
                throw new IllegalArgumentException("Given set contains element that is neither Converter nor ConverterFactory!");
            }
        }
    }

    private void registerConversion(Object converter) {
        Class<?> type = converter.getClass();
        boolean isWriting = type.isAnnotationPresent(WritingConverter.class);
        boolean isReading = type.isAnnotationPresent(ReadingConverter.class);
        if (converter instanceof GenericConverter) {
            GenericConverter genericConverter = (GenericConverter) converter;
            for (GenericConverter.ConvertiblePair pair : genericConverter.getConvertibleTypes()) {
                register(new ConverterRegistration(pair, isReading, isWriting));
            }
            return;
        }
        if (converter instanceof Converter) {
            Class<?>[] arguments = GenericTypeResolver.resolveTypeArguments(converter.getClass(), Converter.class);
            register(new ConverterRegistration(new GenericConverter.ConvertiblePair(arguments[0], arguments[1]), isReading, isWriting));
        } else {
            if (converter instanceof ConverterFactory) {
                Class<?>[] arguments2 = GenericTypeResolver.resolveTypeArguments(converter.getClass(), ConverterFactory.class);
                register(new ConverterRegistration(new GenericConverter.ConvertiblePair(arguments2[0], arguments2[1]), isReading, isWriting));
                return;
            }
            throw new IllegalArgumentException("Unsupported Converter type!");
        }
    }

    private void register(ConverterRegistration converterRegistration) {
        GenericConverter.ConvertiblePair pair = converterRegistration.getConvertiblePair();
        if (converterRegistration.isReading()) {
            this.readingPairs.add(pair);
            if (LOG.isWarnEnabled() && !converterRegistration.isSimpleSourceType()) {
                LOG.warn(String.format(READ_CONVERTER_NOT_SIMPLE, pair.getSourceType(), pair.getTargetType()));
            }
        }
        if (converterRegistration.isWriting()) {
            this.writingPairs.add(pair);
            this.customSimpleTypes.add(pair.getSourceType());
            if (LOG.isWarnEnabled() && !converterRegistration.isSimpleTargetType()) {
                LOG.warn(String.format(WRITE_CONVERTER_NOT_SIMPLE, pair.getSourceType(), pair.getTargetType()));
            }
        }
    }

    public Class<?> getCustomWriteTarget(final Class<?> sourceType) {
        return getOrCreateAndCache(sourceType, this.rawWriteTargetTypes, new Producer() { // from class: org.springframework.data.redis.core.convert.CustomConversions.1
            @Override // org.springframework.data.redis.core.convert.CustomConversions.Producer
            public Class<?> get() {
                return CustomConversions.getCustomTarget(sourceType, null, CustomConversions.this.writingPairs);
            }
        });
    }

    public Class<?> getCustomWriteTarget(final Class<?> sourceType, final Class<?> requestedTargetType) {
        if (requestedTargetType == null) {
            return getCustomWriteTarget(sourceType);
        }
        return getOrCreateAndCache(new GenericConverter.ConvertiblePair(sourceType, requestedTargetType), this.customWriteTargetTypes, new Producer() { // from class: org.springframework.data.redis.core.convert.CustomConversions.2
            @Override // org.springframework.data.redis.core.convert.CustomConversions.Producer
            public Class<?> get() {
                return CustomConversions.getCustomTarget(sourceType, requestedTargetType, CustomConversions.this.writingPairs);
            }
        });
    }

    public boolean hasCustomWriteTarget(Class<?> sourceType) {
        return hasCustomWriteTarget(sourceType, null);
    }

    public boolean hasCustomWriteTarget(Class<?> sourceType, Class<?> requestedTargetType) {
        return getCustomWriteTarget(sourceType, requestedTargetType) != null;
    }

    public boolean hasCustomReadTarget(Class<?> sourceType, Class<?> requestedTargetType) {
        return getCustomReadTarget(sourceType, requestedTargetType) != null;
    }

    private Class<?> getCustomReadTarget(final Class<?> sourceType, final Class<?> requestedTargetType) {
        if (requestedTargetType == null) {
            return null;
        }
        return getOrCreateAndCache(new GenericConverter.ConvertiblePair(sourceType, requestedTargetType), this.customReadTargetTypes, new Producer() { // from class: org.springframework.data.redis.core.convert.CustomConversions.3
            @Override // org.springframework.data.redis.core.convert.CustomConversions.Producer
            public Class<?> get() {
                return CustomConversions.getCustomTarget(sourceType, requestedTargetType, CustomConversions.this.readingPairs);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Class<?> getCustomTarget(Class<?> sourceType, Class<?> requestedTargetType, Collection<GenericConverter.ConvertiblePair> pairs) {
        Assert.notNull(sourceType, "SourceType must not be null!");
        Assert.notNull(pairs, "Convertible pairs must not be null!");
        if (requestedTargetType != null && pairs.contains(new GenericConverter.ConvertiblePair(sourceType, requestedTargetType))) {
            return requestedTargetType;
        }
        for (GenericConverter.ConvertiblePair typePair : pairs) {
            if (typePair.getSourceType().isAssignableFrom(sourceType)) {
                Class<?> targetType = typePair.getTargetType();
                if (requestedTargetType == null || targetType.isAssignableFrom(requestedTargetType)) {
                    return targetType;
                }
            }
        }
        return null;
    }

    private static <T> Class<?> getOrCreateAndCache(T key, Map<T, CacheValue<Class<?>>> cache, Producer producer) {
        CacheValue<Class<?>> cacheValue = cache.get(key);
        if (cacheValue != null) {
            return cacheValue.getValue();
        }
        Class<?> type = producer.get();
        cache.put(key, CacheValue.ofNullable(type));
        return type;
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/CustomConversions$ConverterRegistration.class */
    static class ConverterRegistration {
        private final GenericConverter.ConvertiblePair convertiblePair;
        private final boolean reading;
        private final boolean writing;

        public ConverterRegistration(GenericConverter.ConvertiblePair convertiblePair, boolean isReading, boolean isWriting) {
            Assert.notNull(convertiblePair, "Convertible pair must not be null!");
            this.convertiblePair = convertiblePair;
            this.reading = isReading;
            this.writing = isWriting;
        }

        public ConverterRegistration(Class<?> source, Class<?> target, boolean isReading, boolean isWriting) {
            this(new GenericConverter.ConvertiblePair(source, target), isReading, isWriting);
        }

        public boolean isWriting() {
            return this.writing || (!this.reading && isSimpleTargetType());
        }

        public boolean isReading() {
            return this.reading || (!this.writing && isSimpleSourceType());
        }

        public GenericConverter.ConvertiblePair getConvertiblePair() {
            return this.convertiblePair;
        }

        public boolean isSimpleSourceType() {
            return isRedisBasicType(this.convertiblePair.getSourceType());
        }

        public boolean isSimpleTargetType() {
            return isRedisBasicType(this.convertiblePair.getTargetType());
        }

        private static boolean isRedisBasicType(Class<?> type) {
            return byte[].class.equals(type) || Map.class.equals(type);
        }
    }
}
