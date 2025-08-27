package org.springframework.data.repository.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javaslang.collection.LinkedHashMap;
import javaslang.collection.LinkedHashSet;
import javaslang.collection.Map;
import javaslang.collection.Seq;
import javaslang.collection.Set;
import javaslang.collection.Traversable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.repository.util.QueryExecutionConverters;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/JavaslangCollections.class */
class JavaslangCollections {
    JavaslangCollections() {
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/JavaslangCollections$ToJavaConverter.class */
    public enum ToJavaConverter implements Converter<Object, Object> {
        INSTANCE;

        public QueryExecutionConverters.WrapperType getWrapperType() {
            return QueryExecutionConverters.WrapperType.multiValue(Traversable.class);
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public Object convert2(Object source) {
            if (source instanceof Seq) {
                return ((Seq) source).toJavaList();
            }
            if (source instanceof Map) {
                return ((Map) source).toJavaMap();
            }
            if (source instanceof Set) {
                return ((Set) source).toJavaSet();
            }
            throw new IllegalArgumentException("Unsupported Javaslang collection " + source.getClass());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/JavaslangCollections$FromJavaConverter.class */
    public enum FromJavaConverter implements ConditionalGenericConverter {
        INSTANCE { // from class: org.springframework.data.repository.util.JavaslangCollections.FromJavaConverter.1
            @Override // org.springframework.core.convert.converter.GenericConverter
            public java.util.Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
                return FromJavaConverter.CONVERTIBLE_PAIRS;
            }

            @Override // org.springframework.core.convert.converter.ConditionalConverter
            public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
                if (sourceType.isCollection() && Map.class.isAssignableFrom(targetType.getType())) {
                    return false;
                }
                if (sourceType.isMap() && !Map.class.isAssignableFrom(targetType.getType()) && !targetType.getType().equals(Traversable.class)) {
                    return false;
                }
                return true;
            }

            @Override // org.springframework.core.convert.converter.GenericConverter
            public Object convert(Object source, TypeDescriptor sourceDescriptor, TypeDescriptor targetDescriptor) {
                Class<?> targetType = targetDescriptor.getType();
                return Seq.class.isAssignableFrom(targetType) ? ReflectionUtils.invokeMethod(FromJavaConverter.LIST_FACTORY_METHOD, null, source) : Set.class.isAssignableFrom(targetType) ? ReflectionUtils.invokeMethod(FromJavaConverter.SET_FACTORY_METHOD, null, source) : Map.class.isAssignableFrom(targetType) ? ReflectionUtils.invokeMethod(FromJavaConverter.MAP_FACTORY_METHOD, null, source) : source instanceof List ? ReflectionUtils.invokeMethod(FromJavaConverter.LIST_FACTORY_METHOD, null, source) : source instanceof java.util.Set ? ReflectionUtils.invokeMethod(FromJavaConverter.SET_FACTORY_METHOD, null, source) : source instanceof java.util.Map ? ReflectionUtils.invokeMethod(FromJavaConverter.MAP_FACTORY_METHOD, null, source) : source;
            }
        };

        private static final java.util.Set<GenericConverter.ConvertiblePair> CONVERTIBLE_PAIRS;
        private static final Method LIST_FACTORY_METHOD;
        private static final Method SET_FACTORY_METHOD;
        private static final Method MAP_FACTORY_METHOD;

        static {
            java.util.Set<GenericConverter.ConvertiblePair> pairs = new HashSet<>();
            pairs.add(new GenericConverter.ConvertiblePair(Collection.class, Traversable.class));
            pairs.add(new GenericConverter.ConvertiblePair(java.util.Map.class, Traversable.class));
            CONVERTIBLE_PAIRS = Collections.unmodifiableSet(pairs);
            MAP_FACTORY_METHOD = ReflectionUtils.findMethod(LinkedHashMap.class, "ofAll", java.util.Map.class);
            LIST_FACTORY_METHOD = ReflectionUtils.findMethod(javaslang.collection.List.class, "ofAll", Iterable.class);
            SET_FACTORY_METHOD = ReflectionUtils.findMethod(LinkedHashSet.class, "ofAll", Iterable.class);
        }
    }
}
