package org.springframework.data.repository.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/DomainClassConverter.class */
public class DomainClassConverter<T extends ConversionService & ConverterRegistry> implements ConditionalGenericConverter, ApplicationContextAware {
    private final T conversionService;
    private Repositories repositories = Repositories.NONE;
    private DomainClassConverter<T>.ToEntityConverter toEntityConverter;
    private DomainClassConverter<T>.ToIdConverter toIdConverter;

    public DomainClassConverter(T conversionService) {
        Assert.notNull(conversionService, "ConversionService must not be null!");
        this.conversionService = conversionService;
    }

    @Override // org.springframework.core.convert.converter.GenericConverter
    public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object.class));
    }

    @Override // org.springframework.core.convert.converter.GenericConverter
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (this.repositories.hasRepositoryFor(targetType.getType())) {
            return this.toEntityConverter.convert(source, sourceType, targetType);
        }
        return this.toIdConverter.convert(source, sourceType, targetType);
    }

    @Override // org.springframework.core.convert.converter.ConditionalConverter
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return this.repositories.hasRepositoryFor(targetType.getType()) ? this.toEntityConverter.matches(sourceType, targetType) : this.toIdConverter.matches(sourceType, targetType);
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext context) {
        this.repositories = new Repositories(context);
        this.toEntityConverter = new ToEntityConverter(this.repositories, this.conversionService);
        this.conversionService.addConverter(this.toEntityConverter);
        this.toIdConverter = new ToIdConverter();
        this.conversionService.addConverter(this.toIdConverter);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/DomainClassConverter$ToEntityConverter.class */
    private class ToEntityConverter implements ConditionalGenericConverter {
        private final RepositoryInvokerFactory repositoryInvokerFactory;

        public ToEntityConverter(Repositories repositories, ConversionService conversionService) {
            this.repositoryInvokerFactory = new DefaultRepositoryInvokerFactory(repositories, conversionService);
        }

        @Override // org.springframework.core.convert.converter.GenericConverter
        public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object.class));
        }

        @Override // org.springframework.core.convert.converter.GenericConverter
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (source == null || !StringUtils.hasText(source.toString())) {
                return null;
            }
            if (sourceType.equals(targetType)) {
                return source;
            }
            Class<?> domainType = targetType.getType();
            RepositoryInformation info = DomainClassConverter.this.repositories.getRepositoryInformationFor(domainType);
            RepositoryInvoker invoker = this.repositoryInvokerFactory.getInvokerFor(domainType);
            return invoker.invokeFindOne((Serializable) DomainClassConverter.this.conversionService.convert(source, info.getIdType()));
        }

        @Override // org.springframework.core.convert.converter.ConditionalConverter
        public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (!sourceType.isAssignableTo(targetType) && DomainClassConverter.this.repositories.hasRepositoryFor(targetType.getType())) {
                Class<?> rawIdType = DomainClassConverter.this.repositories.getRepositoryInformationFor(targetType.getType()).getIdType();
                return sourceType.equals(TypeDescriptor.valueOf(rawIdType)) || DomainClassConverter.this.conversionService.canConvert(sourceType.getType(), rawIdType);
            }
            return false;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/DomainClassConverter$ToIdConverter.class */
    class ToIdConverter implements ConditionalGenericConverter {
        ToIdConverter() {
        }

        @Override // org.springframework.core.convert.converter.GenericConverter
        public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object.class));
        }

        @Override // org.springframework.core.convert.converter.GenericConverter
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (source == null || !StringUtils.hasText(source.toString())) {
                return null;
            }
            if (sourceType.equals(targetType)) {
                return source;
            }
            Class<?> domainType = sourceType.getType();
            EntityInformation<Object, ?> entityInformation = DomainClassConverter.this.repositories.getEntityInformationFor(domainType);
            return DomainClassConverter.this.conversionService.convert(entityInformation.getId(source), targetType.getType());
        }

        @Override // org.springframework.core.convert.converter.ConditionalConverter
        public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (!sourceType.isAssignableTo(targetType) && DomainClassConverter.this.repositories.hasRepositoryFor(sourceType.getType())) {
                Class<?> rawIdType = DomainClassConverter.this.repositories.getRepositoryInformationFor(sourceType.getType()).getIdType();
                return targetType.equals(TypeDescriptor.valueOf(rawIdType)) || DomainClassConverter.this.conversionService.canConvert(rawIdType, targetType.getType());
            }
            return false;
        }
    }
}
