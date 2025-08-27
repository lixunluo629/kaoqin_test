package org.springframework.data.repository.util;

import com.google.common.base.Optional;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import javaslang.collection.Seq;
import javaslang.collection.Traversable;
import lombok.Generated;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.repository.util.JavaslangCollections;
import org.springframework.data.repository.util.VavrCollections;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.data.util.TypeInformation;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import scala.Function0;
import scala.Option;
import scala.runtime.AbstractFunction0;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters.class */
public abstract class QueryExecutionConverters {
    private static final boolean GUAVA_PRESENT = org.springframework.util.ClassUtils.isPresent("com.google.common.base.Optional", QueryExecutionConverters.class.getClassLoader());
    private static final boolean JDK_8_PRESENT = org.springframework.util.ClassUtils.isPresent("java.util.Optional", QueryExecutionConverters.class.getClassLoader());
    private static final boolean SCALA_PRESENT = org.springframework.util.ClassUtils.isPresent("scala.Option", QueryExecutionConverters.class.getClassLoader());
    private static final boolean JAVASLANG_PRESENT = org.springframework.util.ClassUtils.isPresent("javaslang.control.Option", QueryExecutionConverters.class.getClassLoader());
    private static final boolean VAVR_PRESENT = org.springframework.util.ClassUtils.isPresent("io.vavr.control.Option", QueryExecutionConverters.class.getClassLoader());
    private static final Set<WrapperType> WRAPPER_TYPES = new HashSet();
    private static final Set<Converter<Object, Object>> UNWRAPPERS = new HashSet();
    private static final Set<Class<?>> ALLOWED_PAGEABLE_TYPES = new HashSet();

    static {
        WRAPPER_TYPES.add(WrapperType.singleValue(Future.class));
        WRAPPER_TYPES.add(WrapperType.singleValue(ListenableFuture.class));
        ALLOWED_PAGEABLE_TYPES.add(Slice.class);
        ALLOWED_PAGEABLE_TYPES.add(Page.class);
        ALLOWED_PAGEABLE_TYPES.add(List.class);
        if (GUAVA_PRESENT) {
            WRAPPER_TYPES.add(NullableWrapperToGuavaOptionalConverter.getWrapperType());
            UNWRAPPERS.add(GuavaOptionalUnwrapper.INSTANCE);
        }
        if (JDK_8_PRESENT) {
            WRAPPER_TYPES.add(NullableWrapperToJdk8OptionalConverter.getWrapperType());
            UNWRAPPERS.add(Jdk8OptionalUnwrapper.INSTANCE);
        }
        if (JDK_8_PRESENT) {
            WRAPPER_TYPES.add(NullableWrapperToCompletableFutureConverter.getWrapperType());
        }
        if (SCALA_PRESENT) {
            WRAPPER_TYPES.add(NullableWrapperToScalaOptionConverter.getWrapperType());
            UNWRAPPERS.add(ScalOptionUnwrapper.INSTANCE);
        }
        if (JAVASLANG_PRESENT) {
            WRAPPER_TYPES.add(NullableWrapperToJavaslangOptionConverter.getWrapperType());
            WRAPPER_TYPES.add(JavaslangCollections.ToJavaConverter.INSTANCE.getWrapperType());
            UNWRAPPERS.add(JavaslangOptionUnwrapper.INSTANCE);
            ALLOWED_PAGEABLE_TYPES.add(Seq.class);
        }
        if (VAVR_PRESENT) {
            WRAPPER_TYPES.add(NullableWrapperToVavrOptionConverter.getWrapperType());
            WRAPPER_TYPES.add(VavrCollections.ToJavaConverter.INSTANCE.getWrapperType());
            UNWRAPPERS.add(VavrOptionUnwrapper.INSTANCE);
            ALLOWED_PAGEABLE_TYPES.add(io.vavr.collection.Seq.class);
        }
    }

    private QueryExecutionConverters() {
    }

    public static boolean supports(Class<?> type) {
        Assert.notNull(type, "Type must not be null!");
        for (WrapperType candidate : WRAPPER_TYPES) {
            if (candidate.getType().isAssignableFrom(type)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSingleValue(Class<?> type) {
        for (WrapperType candidate : WRAPPER_TYPES) {
            if (candidate.getType().isAssignableFrom(type)) {
                return candidate.isSingleValue();
            }
        }
        return false;
    }

    public static Set<Class<?>> getAllowedPageableTypes() {
        return Collections.unmodifiableSet(ALLOWED_PAGEABLE_TYPES);
    }

    public static void registerConvertersIn(ConfigurableConversionService conversionService) {
        Assert.notNull(conversionService, "ConversionService must not be null!");
        conversionService.removeConvertible(Collection.class, Object.class);
        if (GUAVA_PRESENT) {
            conversionService.addConverter(new NullableWrapperToGuavaOptionalConverter(conversionService));
        }
        if (JDK_8_PRESENT) {
            conversionService.addConverter(new NullableWrapperToJdk8OptionalConverter(conversionService));
            conversionService.addConverter(new NullableWrapperToCompletableFutureConverter(conversionService));
        }
        if (SCALA_PRESENT) {
            conversionService.addConverter(new NullableWrapperToScalaOptionConverter(conversionService));
        }
        if (JAVASLANG_PRESENT) {
            conversionService.addConverter(new NullableWrapperToJavaslangOptionConverter(conversionService));
            conversionService.addConverter(JavaslangCollections.FromJavaConverter.INSTANCE);
        }
        if (VAVR_PRESENT) {
            conversionService.addConverter(new NullableWrapperToVavrOptionConverter(conversionService));
            conversionService.addConverter(VavrCollections.FromJavaConverter.INSTANCE);
        }
        conversionService.addConverter(new NullableWrapperToFutureConverter(conversionService));
    }

    public static Object unwrap(Object source) {
        if (source == null || !supports(source.getClass())) {
            return source;
        }
        for (Converter<Object, Object> converter : UNWRAPPERS) {
            Object result = converter.convert2(source);
            if (result != source) {
                return result;
            }
        }
        return source;
    }

    public static TypeInformation<?> unwrapWrapperTypes(TypeInformation<?> type) {
        Assert.notNull(type, "type must not be null");
        Class<?> rawType = type.getType();
        boolean needToUnwrap = type.isCollectionLike() || Slice.class.isAssignableFrom(rawType) || GeoResults.class.isAssignableFrom(rawType) || rawType.isArray() || supports(rawType) || ReflectionUtils.isJava8StreamType(rawType);
        return needToUnwrap ? unwrapWrapperTypes(type.getComponentType()) : type;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$AbstractWrapperTypeConverter.class */
    private static abstract class AbstractWrapperTypeConverter implements GenericConverter {
        private final ConversionService conversionService;
        private final Class<?>[] wrapperTypes;
        private final Object nullValue;

        protected abstract Object wrap(Object obj);

        protected AbstractWrapperTypeConverter(ConversionService conversionService, Object nullValue, Class<?>... wrapperTypes) {
            Assert.notNull(conversionService, "ConversionService must not be null!");
            Assert.notEmpty(wrapperTypes, "Wrapper type must not be empty!");
            this.conversionService = conversionService;
            this.wrapperTypes = wrapperTypes;
            this.nullValue = nullValue;
        }

        @Override // org.springframework.core.convert.converter.GenericConverter
        public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
            Set<GenericConverter.ConvertiblePair> pairs = new HashSet<>(this.wrapperTypes.length);
            for (Class<?> wrapperType : this.wrapperTypes) {
                pairs.add(new GenericConverter.ConvertiblePair(NullableWrapper.class, wrapperType));
            }
            return Collections.unmodifiableSet(pairs);
        }

        @Override // org.springframework.core.convert.converter.GenericConverter
        public final Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            NullableWrapper wrapper = (NullableWrapper) source;
            Object value = wrapper.getValue();
            return value == null ? this.nullValue : wrap(value);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$NullableWrapperToGuavaOptionalConverter.class */
    private static class NullableWrapperToGuavaOptionalConverter extends AbstractWrapperTypeConverter {
        public NullableWrapperToGuavaOptionalConverter(ConversionService conversionService) {
            super(conversionService, Optional.absent(), Optional.class);
        }

        @Override // org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter
        protected Object wrap(Object source) {
            return Optional.of(source);
        }

        public static WrapperType getWrapperType() {
            return WrapperType.singleValue(Optional.class);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$NullableWrapperToJdk8OptionalConverter.class */
    private static class NullableWrapperToJdk8OptionalConverter extends AbstractWrapperTypeConverter {
        public NullableWrapperToJdk8OptionalConverter(ConversionService conversionService) {
            super(conversionService, java.util.Optional.empty(), java.util.Optional.class);
        }

        @Override // org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter
        protected Object wrap(Object source) {
            return java.util.Optional.of(source);
        }

        public static WrapperType getWrapperType() {
            return WrapperType.singleValue(java.util.Optional.class);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$NullableWrapperToFutureConverter.class */
    private static class NullableWrapperToFutureConverter extends AbstractWrapperTypeConverter {
        public NullableWrapperToFutureConverter(ConversionService conversionService) {
            super(conversionService, new AsyncResult(null), Future.class, ListenableFuture.class);
        }

        @Override // org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter
        protected Object wrap(Object source) {
            return new AsyncResult(source);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$NullableWrapperToCompletableFutureConverter.class */
    private static class NullableWrapperToCompletableFutureConverter extends AbstractWrapperTypeConverter {
        public NullableWrapperToCompletableFutureConverter(ConversionService conversionService) {
            super(conversionService, CompletableFuture.completedFuture(null), CompletableFuture.class);
        }

        @Override // org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter
        protected Object wrap(Object source) {
            return source instanceof CompletableFuture ? source : CompletableFuture.completedFuture(source);
        }

        public static WrapperType getWrapperType() {
            return WrapperType.singleValue(CompletableFuture.class);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$NullableWrapperToScalaOptionConverter.class */
    private static class NullableWrapperToScalaOptionConverter extends AbstractWrapperTypeConverter {
        public NullableWrapperToScalaOptionConverter(ConversionService conversionService) {
            super(conversionService, Option.empty(), Option.class);
        }

        @Override // org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter
        protected Object wrap(Object source) {
            return Option.apply(source);
        }

        public static WrapperType getWrapperType() {
            return WrapperType.singleValue(Option.class);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$NullableWrapperToJavaslangOptionConverter.class */
    private static class NullableWrapperToJavaslangOptionConverter extends AbstractWrapperTypeConverter {
        private static final Method OF_METHOD = org.springframework.util.ReflectionUtils.findMethod(javaslang.control.Option.class, "of", Object.class);
        private static final Method NONE_METHOD = org.springframework.util.ReflectionUtils.findMethod(javaslang.control.Option.class, "none");

        public NullableWrapperToJavaslangOptionConverter(ConversionService conversionService) {
            super(conversionService, createEmptyOption(), javaslang.control.Option.class);
        }

        public static WrapperType getWrapperType() {
            return WrapperType.singleValue(javaslang.control.Option.class);
        }

        @Override // org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter
        protected Object wrap(Object source) {
            return org.springframework.util.ReflectionUtils.invokeMethod(OF_METHOD, null, source);
        }

        private static javaslang.control.Option<Object> createEmptyOption() {
            return (javaslang.control.Option) org.springframework.util.ReflectionUtils.invokeMethod(NONE_METHOD, null);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$NullableWrapperToVavrOptionConverter.class */
    private static class NullableWrapperToVavrOptionConverter extends AbstractWrapperTypeConverter {
        private static final Method OF_METHOD = org.springframework.util.ReflectionUtils.findMethod(io.vavr.control.Option.class, "of", Object.class);
        private static final Method NONE_METHOD = org.springframework.util.ReflectionUtils.findMethod(io.vavr.control.Option.class, "none");

        public NullableWrapperToVavrOptionConverter(ConversionService conversionService) {
            super(conversionService, createEmptyOption(), io.vavr.control.Option.class);
        }

        @Override // org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter
        protected Object wrap(Object source) {
            return org.springframework.util.ReflectionUtils.invokeMethod(OF_METHOD, null, source);
        }

        public static WrapperType getWrapperType() {
            return WrapperType.singleValue(io.vavr.control.Option.class);
        }

        private static io.vavr.control.Option<Object> createEmptyOption() {
            return (io.vavr.control.Option) org.springframework.util.ReflectionUtils.invokeMethod(NONE_METHOD, null);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$GuavaOptionalUnwrapper.class */
    private enum GuavaOptionalUnwrapper implements Converter<Object, Object> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public Object convert2(Object source) {
            return source instanceof Optional ? ((Optional) source).orNull() : source;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$Jdk8OptionalUnwrapper.class */
    private enum Jdk8OptionalUnwrapper implements Converter<Object, Object> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public Object convert2(Object source) {
            return source instanceof java.util.Optional ? ((java.util.Optional) source).orElse(null) : source;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$ScalOptionUnwrapper.class */
    private enum ScalOptionUnwrapper implements Converter<Object, Object> {
        INSTANCE;

        private final Function0<Object> alternative = new AbstractFunction0<Object>() { // from class: org.springframework.data.repository.util.QueryExecutionConverters.ScalOptionUnwrapper.1
            /* renamed from: apply, reason: merged with bridge method [inline-methods] */
            public Option<Object> m7870apply() {
                return null;
            }
        };

        ScalOptionUnwrapper() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public Object convert2(Object source) {
            return source instanceof Option ? ((Option) source).getOrElse(this.alternative) : source;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$JavaslangOptionUnwrapper.class */
    private enum JavaslangOptionUnwrapper implements Converter<Object, Object> {
        INSTANCE;

        private static final Supplier<Object> NULL_SUPPLIER = new Supplier<Object>() { // from class: org.springframework.data.repository.util.QueryExecutionConverters.JavaslangOptionUnwrapper.1
            @Override // java.util.function.Supplier
            public Object get() {
                return null;
            }
        };

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public Object convert2(Object source) {
            if (source instanceof javaslang.control.Option) {
                return ((javaslang.control.Option) source).getOrElse(NULL_SUPPLIER);
            }
            if (source instanceof Traversable) {
                return JavaslangCollections.ToJavaConverter.INSTANCE.convert2(source);
            }
            return source;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$VavrOptionUnwrapper.class */
    private enum VavrOptionUnwrapper implements Converter<Object, Object> {
        INSTANCE;

        private static final Supplier<Object> NULL_SUPPLIER = new Supplier<Object>() { // from class: org.springframework.data.repository.util.QueryExecutionConverters.VavrOptionUnwrapper.1
            @Override // java.util.function.Supplier
            public Object get() {
                return null;
            }
        };

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public Object convert2(Object source) {
            if (source instanceof io.vavr.control.Option) {
                return ((io.vavr.control.Option) source).getOrElse(NULL_SUPPLIER);
            }
            if (source instanceof io.vavr.collection.Traversable) {
                return VavrCollections.ToJavaConverter.INSTANCE.convert2(source);
            }
            return source;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/QueryExecutionConverters$WrapperType.class */
    public static final class WrapperType {
        private final Class<?> type;
        private final boolean singleValue;

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof WrapperType)) {
                return false;
            }
            WrapperType other = (WrapperType) o;
            Object this$type = getType();
            Object other$type = other.getType();
            if (this$type == null) {
                if (other$type != null) {
                    return false;
                }
            } else if (!this$type.equals(other$type)) {
                return false;
            }
            return isSingleValue() == other.isSingleValue();
        }

        @Generated
        public int hashCode() {
            Object $type = getType();
            int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
            return (result * 59) + (isSingleValue() ? 79 : 97);
        }

        @Generated
        public String toString() {
            return "QueryExecutionConverters.WrapperType(type=" + getType() + ", singleValue=" + isSingleValue() + ")";
        }

        @Generated
        private WrapperType(Class<?> type, boolean singleValue) {
            this.type = type;
            this.singleValue = singleValue;
        }

        @Generated
        public Class<?> getType() {
            return this.type;
        }

        @Generated
        public boolean isSingleValue() {
            return this.singleValue;
        }

        public static WrapperType singleValue(Class<?> type) {
            return new WrapperType(type, true);
        }

        public static WrapperType multiValue(Class<?> type) {
            return new WrapperType(type, false);
        }
    }
}
