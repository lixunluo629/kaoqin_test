package org.springframework.data.convert;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters.class */
public abstract class Jsr310Converters {
    private static final boolean JAVA_8_IS_PRESENT = ClassUtils.isPresent("java.time.LocalDateTime", Jsr310Converters.class.getClassLoader());

    public static Collection<Converter<?, ?>> getConvertersToRegister() {
        if (!JAVA_8_IS_PRESENT) {
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
        converters.add(LocalDateTimeToInstantConverter.INSTANCE);
        converters.add(InstantToLocalDateTimeConverter.INSTANCE);
        converters.add(ZoneIdToStringConverter.INSTANCE);
        converters.add(StringToZoneIdConverter.INSTANCE);
        converters.add(DurationToStringConverter.INSTANCE);
        converters.add(StringToDurationConverter.INSTANCE);
        converters.add(PeriodToStringConverter.INSTANCE);
        converters.add(StringToPeriodConverter.INSTANCE);
        return converters;
    }

    public static boolean supports(Class<?> type) {
        if (!JAVA_8_IS_PRESENT) {
            return false;
        }
        return Arrays.asList(LocalDateTime.class, LocalDate.class, LocalTime.class, Instant.class).contains(type);
    }

    @ReadingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$DateToLocalDateTimeConverter.class */
    public enum DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public LocalDateTime convert(Date source) {
            if (source == null) {
                return null;
            }
            return LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    @WritingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$LocalDateTimeToDateConverter.class */
    public enum LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
        INSTANCE;

        /* JADX WARN: Type inference failed for: r0v2, types: [java.time.ZonedDateTime] */
        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(LocalDateTime source) {
            if (source == null) {
                return null;
            }
            return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$DateToLocalDateConverter.class */
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

    @WritingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$LocalDateToDateConverter.class */
    public enum LocalDateToDateConverter implements Converter<LocalDate, Date> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(LocalDate source) {
            if (source == null) {
                return null;
            }
            return Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$DateToLocalTimeConverter.class */
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

    @WritingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$LocalTimeToDateConverter.class */
    public enum LocalTimeToDateConverter implements Converter<LocalTime, Date> {
        INSTANCE;

        /* JADX WARN: Type inference failed for: r0v3, types: [java.time.ZonedDateTime] */
        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(LocalTime source) {
            if (source == null) {
                return null;
            }
            return Date.from(source.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$DateToInstantConverter.class */
    public enum DateToInstantConverter implements Converter<Date, Instant> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Instant convert(Date source) {
            if (source == null) {
                return null;
            }
            return source.toInstant();
        }
    }

    @WritingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$InstantToDateConverter.class */
    public enum InstantToDateConverter implements Converter<Instant, Date> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(Instant source) {
            if (source == null) {
                return null;
            }
            return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$LocalDateTimeToInstantConverter.class */
    public enum LocalDateTimeToInstantConverter implements Converter<LocalDateTime, Instant> {
        INSTANCE;

        /* JADX WARN: Type inference failed for: r0v1, types: [java.time.ZonedDateTime] */
        @Override // org.springframework.core.convert.converter.Converter
        public Instant convert(LocalDateTime source) {
            return source.atZone(ZoneId.systemDefault()).toInstant();
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$InstantToLocalDateTimeConverter.class */
    public enum InstantToLocalDateTimeConverter implements Converter<Instant, LocalDateTime> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public LocalDateTime convert(Instant source) {
            return LocalDateTime.ofInstant(source, ZoneId.systemDefault());
        }
    }

    @WritingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$ZoneIdToStringConverter.class */
    public enum ZoneIdToStringConverter implements Converter<ZoneId, String> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public String convert(ZoneId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$StringToZoneIdConverter.class */
    public enum StringToZoneIdConverter implements Converter<String, ZoneId> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public ZoneId convert(String source) {
            return ZoneId.of(source);
        }
    }

    @WritingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$DurationToStringConverter.class */
    public enum DurationToStringConverter implements Converter<Duration, String> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public String convert(Duration duration) {
            return duration.toString();
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$StringToDurationConverter.class */
    public enum StringToDurationConverter implements Converter<String, Duration> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Duration convert(String s) {
            return Duration.parse(s);
        }
    }

    @WritingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$PeriodToStringConverter.class */
    public enum PeriodToStringConverter implements Converter<Period, String> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public String convert(Period period) {
            return period.toString();
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/Jsr310Converters$StringToPeriodConverter.class */
    public enum StringToPeriodConverter implements Converter<String, Period> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Period convert(String s) {
            return Period.parse(s);
        }
    }
}
