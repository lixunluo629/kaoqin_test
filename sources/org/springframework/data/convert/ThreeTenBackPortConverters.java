package org.springframework.data.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ClassUtils;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters.class */
public abstract class ThreeTenBackPortConverters {
    private static final boolean THREE_TEN_BACK_PORT_IS_PRESENT = ClassUtils.isPresent("org.threeten.bp.LocalDateTime", ThreeTenBackPortConverters.class.getClassLoader());

    public static Collection<Converter<?, ?>> getConvertersToRegister() {
        if (!THREE_TEN_BACK_PORT_IS_PRESENT) {
            return Collections.emptySet();
        }
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(DateToLocalDateTimeConverter.INSTANCE);
        converters.add(LocalDateTimeToDateConverter.INSTANCE);
        converters.add(DateToLocalDateConverter.INSTANCE);
        converters.add(LocalDateToDateConverter.INSTANCE);
        converters.add(DateToLocalTimeConverter.INSTANCE);
        converters.add(LocalTimeToDateConverter.INSTANCE);
        converters.add(DateToInstantConverter.INSTANCE);
        converters.add(InstantToDateConverter.INSTANCE);
        converters.add(ZoneIdToStringConverter.INSTANCE);
        converters.add(StringToZoneIdConverter.INSTANCE);
        return converters;
    }

    public static boolean supports(Class<?> type) {
        if (!THREE_TEN_BACK_PORT_IS_PRESENT) {
            return false;
        }
        return Arrays.asList(LocalDateTime.class, LocalDate.class, LocalTime.class, Instant.class).contains(type);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters$DateToLocalDateTimeConverter.class */
    public enum DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public LocalDateTime convert(Date source) {
            if (source == null) {
                return null;
            }
            return LocalDateTime.ofInstant(DateTimeUtils.toInstant(source), ZoneId.systemDefault());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters$LocalDateTimeToDateConverter.class */
    public enum LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(LocalDateTime source) {
            if (source == null) {
                return null;
            }
            return DateTimeUtils.toDate(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters$DateToLocalDateConverter.class */
    public enum DateToLocalDateConverter implements Converter<Date, LocalDate> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public LocalDate convert(Date source) {
            if (source == null) {
                return null;
            }
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(source.getTime()), ZoneId.systemDefault()).toLocalDate();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters$LocalDateToDateConverter.class */
    public enum LocalDateToDateConverter implements Converter<LocalDate, Date> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(LocalDate source) {
            if (source == null) {
                return null;
            }
            return DateTimeUtils.toDate(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters$DateToLocalTimeConverter.class */
    public enum DateToLocalTimeConverter implements Converter<Date, LocalTime> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public LocalTime convert(Date source) {
            if (source == null) {
                return null;
            }
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(source.getTime()), ZoneId.systemDefault()).toLocalTime();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters$LocalTimeToDateConverter.class */
    public enum LocalTimeToDateConverter implements Converter<LocalTime, Date> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(LocalTime source) {
            if (source == null) {
                return null;
            }
            return DateTimeUtils.toDate(source.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters$DateToInstantConverter.class */
    public enum DateToInstantConverter implements Converter<Date, Instant> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Instant convert(Date source) {
            if (source == null) {
                return null;
            }
            return DateTimeUtils.toInstant(source);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters$InstantToDateConverter.class */
    public enum InstantToDateConverter implements Converter<Instant, Date> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(Instant source) {
            if (source == null) {
                return null;
            }
            return DateTimeUtils.toDate(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    @WritingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters$ZoneIdToStringConverter.class */
    public enum ZoneIdToStringConverter implements Converter<ZoneId, String> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public String convert(ZoneId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ThreeTenBackPortConverters$StringToZoneIdConverter.class */
    public enum StringToZoneIdConverter implements Converter<String, ZoneId> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public ZoneId convert(String source) {
            return ZoneId.of(source);
        }
    }
}
