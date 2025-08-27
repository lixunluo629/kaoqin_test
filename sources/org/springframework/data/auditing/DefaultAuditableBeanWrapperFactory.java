package org.springframework.data.auditing;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.convert.ThreeTenBackPortConverters;
import org.springframework.data.domain.Auditable;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/DefaultAuditableBeanWrapperFactory.class */
class DefaultAuditableBeanWrapperFactory implements AuditableBeanWrapperFactory {
    DefaultAuditableBeanWrapperFactory() {
    }

    @Override // org.springframework.data.auditing.AuditableBeanWrapperFactory
    public AuditableBeanWrapper getBeanWrapperFor(Object source) {
        if (source == null) {
            return null;
        }
        if (source instanceof Auditable) {
            return new AuditableInterfaceBeanWrapper((Auditable) source);
        }
        AnnotationAuditingMetadata metadata = AnnotationAuditingMetadata.getMetadata(source.getClass());
        if (metadata.isAuditable()) {
            return new ReflectionAuditingBeanWrapper(source);
        }
        return null;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/DefaultAuditableBeanWrapperFactory$AuditableInterfaceBeanWrapper.class */
    static class AuditableInterfaceBeanWrapper extends DateConvertingAuditableBeanWrapper {
        private final Auditable<Object, ?> auditable;

        public AuditableInterfaceBeanWrapper(Auditable<Object, ?> auditable) {
            this.auditable = auditable;
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setCreatedBy(Object value) {
            this.auditable.setCreatedBy(value);
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setCreatedDate(Calendar value) {
            this.auditable.setCreatedDate(new DateTime(value));
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setLastModifiedBy(Object value) {
            this.auditable.setLastModifiedBy(value);
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public Calendar getLastModifiedDate() {
            return getAsCalendar(this.auditable.getLastModifiedDate());
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setLastModifiedDate(Calendar value) {
            this.auditable.setLastModifiedDate(new DateTime(value));
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/DefaultAuditableBeanWrapperFactory$DateConvertingAuditableBeanWrapper.class */
    static abstract class DateConvertingAuditableBeanWrapper implements AuditableBeanWrapper {
        private static final boolean IS_JODA_TIME_PRESENT = ClassUtils.isPresent("org.joda.time.DateTime", ReflectionAuditingBeanWrapper.class.getClassLoader());
        private final ConversionService conversionService;

        public DateConvertingAuditableBeanWrapper() {
            DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
            if (IS_JODA_TIME_PRESENT) {
                conversionService.addConverter(CalendarToDateTimeConverter.INSTANCE);
                conversionService.addConverter(CalendarToLocalDateTimeConverter.INSTANCE);
            }
            for (Converter<?, ?> converter : Jsr310Converters.getConvertersToRegister()) {
                conversionService.addConverter(converter);
            }
            for (Converter<?, ?> converter2 : ThreeTenBackPortConverters.getConvertersToRegister()) {
                conversionService.addConverter(converter2);
            }
            this.conversionService = conversionService;
        }

        protected Object getDateValueToSet(Calendar value, Class<?> targetType, Object source) {
            if (value == null) {
                return null;
            }
            if (Calendar.class.equals(targetType)) {
                return value;
            }
            if (this.conversionService.canConvert(Calendar.class, targetType)) {
                return this.conversionService.convert(value, targetType);
            }
            if (this.conversionService.canConvert(Date.class, targetType)) {
                Date date = (Date) this.conversionService.convert(value, Date.class);
                return this.conversionService.convert(date, targetType);
            }
            throw new IllegalArgumentException(String.format("Invalid date type for member %s! Supported types are %s.", source, AnnotationAuditingMetadata.SUPPORTED_DATE_TYPES));
        }

        protected Calendar getAsCalendar(Object source) {
            if (source == null || (source instanceof Calendar)) {
                return (Calendar) source;
            }
            return (Calendar) this.conversionService.convert(((source instanceof Date) || !this.conversionService.canConvert(source.getClass(), Date.class)) ? source : this.conversionService.convert(source, Date.class), Calendar.class);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/DefaultAuditableBeanWrapperFactory$ReflectionAuditingBeanWrapper.class */
    static class ReflectionAuditingBeanWrapper extends DateConvertingAuditableBeanWrapper {
        private final AnnotationAuditingMetadata metadata;
        private final Object target;

        public ReflectionAuditingBeanWrapper(Object target) {
            Assert.notNull(target, "Target object must not be null!");
            this.metadata = AnnotationAuditingMetadata.getMetadata(target.getClass());
            this.target = target;
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setCreatedBy(Object value) throws IllegalAccessException, IllegalArgumentException {
            setField(this.metadata.getCreatedByField(), value);
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setCreatedDate(Calendar value) throws IllegalAccessException, IllegalArgumentException {
            setDateField(this.metadata.getCreatedDateField(), value);
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setLastModifiedBy(Object value) throws IllegalAccessException, IllegalArgumentException {
            setField(this.metadata.getLastModifiedByField(), value);
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public Calendar getLastModifiedDate() {
            return getAsCalendar(ReflectionUtils.getField(this.metadata.getLastModifiedDateField(), this.target));
        }

        @Override // org.springframework.data.auditing.AuditableBeanWrapper
        public void setLastModifiedDate(Calendar value) throws IllegalAccessException, IllegalArgumentException {
            setDateField(this.metadata.getLastModifiedDateField(), value);
        }

        private void setField(Field field, Object value) throws IllegalAccessException, IllegalArgumentException {
            if (field != null) {
                org.springframework.data.util.ReflectionUtils.setField(field, this.target, value);
            }
        }

        private void setDateField(Field field, Calendar value) throws IllegalAccessException, IllegalArgumentException {
            if (field == null) {
                return;
            }
            org.springframework.data.util.ReflectionUtils.setField(field, this.target, getDateValueToSet(value, field.getType(), field));
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/DefaultAuditableBeanWrapperFactory$CalendarToDateTimeConverter.class */
    private enum CalendarToDateTimeConverter implements Converter<Calendar, DateTime> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public DateTime convert2(Calendar source) {
            return new DateTime(source);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/DefaultAuditableBeanWrapperFactory$CalendarToLocalDateTimeConverter.class */
    private enum CalendarToLocalDateTimeConverter implements Converter<Calendar, LocalDateTime> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public LocalDateTime convert2(Calendar source) {
            return new LocalDateTime(source);
        }
    }
}
