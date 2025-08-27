package org.springframework.data.repository.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Generated;
import lombok.NonNull;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.Slice;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ResultProcessor.class */
public class ResultProcessor {
    private final QueryMethod method;
    private final ProjectingConverter converter;
    private final ProjectionFactory factory;
    private ReturnedType type;

    @Generated
    private ResultProcessor(QueryMethod method, ProjectingConverter converter, ProjectionFactory factory, ReturnedType type) {
        this.method = method;
        this.converter = converter;
        this.factory = factory;
        this.type = type;
    }

    ResultProcessor(QueryMethod method, ProjectionFactory factory) {
        this(method, factory, method.getReturnedObjectType());
    }

    private ResultProcessor(QueryMethod method, ProjectionFactory factory, Class<?> type) {
        Assert.notNull(method, "QueryMethod must not be null!");
        Assert.notNull(factory, "ProjectionFactory must not be null!");
        Assert.notNull(type, "Type must not be null!");
        this.method = method;
        this.type = ReturnedType.of(type, method.getDomainClass(), factory);
        this.converter = new ProjectingConverter(this.type, factory);
        this.factory = factory;
    }

    public ResultProcessor withDynamicProjection(ParameterAccessor accessor) {
        if (accessor == null) {
            return this;
        }
        Class<?> projectionType = accessor.getDynamicProjection();
        return projectionType == null ? this : withType(projectionType);
    }

    public ReturnedType getReturnedType() {
        return this.type;
    }

    public <T> T processResult(Object obj) {
        return (T) processResult(obj, NoOpConverter.INSTANCE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v37, types: [T, java.util.Collection] */
    public <T> T processResult(Object obj, Converter<Object, Object> converter) {
        if (obj == 0 || this.type.isInstance(obj) || !this.type.isProjecting()) {
            return obj;
        }
        Assert.notNull(converter, "Preparing converter must not be null!");
        ChainingConverter chainingConverterAnd = ChainingConverter.of(this.type.getReturnedType(), converter).and(this.converter);
        if (((obj instanceof Slice) && this.method.isPageQuery()) || this.method.isSliceQuery()) {
            return (T) ((Slice) obj).map(chainingConverterAnd);
        }
        if ((obj instanceof Collection) && this.method.isCollectionQuery()) {
            Collection collection = (Collection) obj;
            ?? r0 = (T) createCollectionFor(collection);
            for (Object obj2 : collection) {
                r0.add(this.type.isInstance(obj2) ? obj2 : chainingConverterAnd.convert2(obj2));
            }
            return r0;
        }
        if (ReflectionUtils.isJava8StreamType(obj.getClass()) && this.method.isStreamQuery()) {
            return (T) new StreamQueryResultHandler(this.type, chainingConverterAnd).handle(obj);
        }
        return (T) chainingConverterAnd.convert2(obj);
    }

    private ResultProcessor withType(Class<?> type) {
        ReturnedType returnedType = ReturnedType.of(type, this.method.getDomainClass(), this.factory);
        return new ResultProcessor(this.method, this.converter.withType(returnedType), this.factory, returnedType);
    }

    private static Collection<Object> createCollectionFor(Collection<?> source) {
        try {
            return CollectionFactory.createCollection(source.getClass(), source.size());
        } catch (RuntimeException e) {
            return CollectionFactory.createApproximateCollection(source, source.size());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ResultProcessor$ChainingConverter.class */
    private static class ChainingConverter implements Converter<Object, Object> {

        @NonNull
        private final Class<?> targetType;

        @NonNull
        private final Converter<Object, Object> delegate;

        @Generated
        private ChainingConverter(@NonNull Class<?> targetType, @NonNull Converter<Object, Object> delegate) {
            if (targetType == null) {
                throw new IllegalArgumentException("targetType is marked @NonNull but is null");
            }
            if (delegate == null) {
                throw new IllegalArgumentException("delegate is marked @NonNull but is null");
            }
            this.targetType = targetType;
            this.delegate = delegate;
        }

        @Generated
        public static ChainingConverter of(@NonNull Class<?> targetType, @NonNull Converter<Object, Object> delegate) {
            return new ChainingConverter(targetType, delegate);
        }

        public ChainingConverter and(final Converter<Object, Object> converter) {
            Assert.notNull(converter, "Converter must not be null!");
            return new ChainingConverter(this.targetType, new Converter<Object, Object>() { // from class: org.springframework.data.repository.query.ResultProcessor.ChainingConverter.1
                @Override // org.springframework.core.convert.converter.Converter
                /* renamed from: convert */
                public Object convert2(Object source) {
                    Object intermediate = ChainingConverter.this.convert2(source);
                    return ChainingConverter.this.targetType.isInstance(intermediate) ? intermediate : converter.convert2(intermediate);
                }
            });
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public Object convert2(Object source) {
            return this.delegate.convert2(source);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ResultProcessor$NoOpConverter.class */
    private enum NoOpConverter implements Converter<Object, Object> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public Object convert2(Object source) {
            return source;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ResultProcessor$ProjectingConverter.class */
    private static class ProjectingConverter implements Converter<Object, Object> {

        @NonNull
        private final ReturnedType type;

        @NonNull
        private final ProjectionFactory factory;

        @NonNull
        private final ConversionService conversionService;

        @Generated
        public ProjectingConverter(@NonNull ReturnedType type, @NonNull ProjectionFactory factory, @NonNull ConversionService conversionService) {
            if (type == null) {
                throw new IllegalArgumentException("type is marked @NonNull but is null");
            }
            if (factory == null) {
                throw new IllegalArgumentException("factory is marked @NonNull but is null");
            }
            if (conversionService == null) {
                throw new IllegalArgumentException("conversionService is marked @NonNull but is null");
            }
            this.type = type;
            this.factory = factory;
            this.conversionService = conversionService;
        }

        ProjectingConverter(ReturnedType type, ProjectionFactory factory) {
            this(type, factory, new DefaultConversionService());
        }

        ProjectingConverter withType(ReturnedType type) {
            Assert.notNull(type, "ReturnedType must not be null!");
            return new ProjectingConverter(type, this.factory, this.conversionService);
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public Object convert2(Object source) {
            Class<?> targetType = this.type.getReturnedType();
            if (targetType.isInterface()) {
                return this.factory.createProjection(targetType, getProjectionTarget(source));
            }
            return this.conversionService.convert(source, targetType);
        }

        private Object getProjectionTarget(Object source) {
            if (source != null && source.getClass().isArray()) {
                source = Arrays.asList((Object[]) source);
            }
            if (source instanceof Collection) {
                return toMap((Collection) source, this.type.getInputProperties());
            }
            return source;
        }

        private static Map<String, Object> toMap(Collection<?> values, List<String> names) {
            int i = 0;
            Map<String, Object> result = new HashMap<>(values.size());
            for (Object element : values) {
                int i2 = i;
                i++;
                result.put(names.get(i2), element);
            }
            return result;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ResultProcessor$StreamQueryResultHandler.class */
    static class StreamQueryResultHandler {

        @NonNull
        private final ReturnedType returnType;

        @NonNull
        private final Converter<Object, Object> converter;

        @Generated
        public StreamQueryResultHandler(@NonNull ReturnedType returnType, @NonNull Converter<Object, Object> converter) {
            if (returnType == null) {
                throw new IllegalArgumentException("returnType is marked @NonNull but is null");
            }
            if (converter == null) {
                throw new IllegalArgumentException("converter is marked @NonNull but is null");
            }
            this.returnType = returnType;
            this.converter = converter;
        }

        public Object handle(Object source) {
            Assert.isInstanceOf(Stream.class, source, "Source must not be null and an instance of Stream!");
            return ((Stream) source).map(new Function<Object, Object>() { // from class: org.springframework.data.repository.query.ResultProcessor.StreamQueryResultHandler.1
                @Override // java.util.function.Function
                public Object apply(Object element) {
                    return StreamQueryResultHandler.this.returnType.isInstance(element) ? element : StreamQueryResultHandler.this.converter.convert2(element);
                }
            });
        }
    }
}
