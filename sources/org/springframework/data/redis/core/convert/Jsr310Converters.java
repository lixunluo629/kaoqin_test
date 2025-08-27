package org.springframework.data.redis.core.convert;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.core.convert.BinaryConverters;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters.class */
public abstract class Jsr310Converters {
    private static final boolean JAVA_8_IS_PRESENT = ClassUtils.isPresent("java.time.LocalDateTime", Jsr310Converters.class.getClassLoader());

    public static Collection<Converter<?, ?>> getConvertersToRegister() {
        if (!JAVA_8_IS_PRESENT) {
            return Collections.emptySet();
        }
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new LocalDateTimeToBytesConverter());
        converters.add(new BytesToLocalDateTimeConverter());
        converters.add(new LocalDateToBytesConverter());
        converters.add(new BytesToLocalDateConverter());
        converters.add(new LocalTimeToBytesConverter());
        converters.add(new BytesToLocalTimeConverter());
        converters.add(new ZonedDateTimeToBytesConverter());
        converters.add(new BytesToZonedDateTimeConverter());
        converters.add(new InstantToBytesConverter());
        converters.add(new BytesToInstantConverter());
        converters.add(new ZoneIdToBytesConverter());
        converters.add(new BytesToZoneIdConverter());
        converters.add(new PeriodToBytesConverter());
        converters.add(new BytesToPeriodConverter());
        converters.add(new DurationToBytesConverter());
        converters.add(new BytesToDurationConverter());
        return converters;
    }

    public static boolean supports(Class<?> type) {
        if (!JAVA_8_IS_PRESENT) {
            return false;
        }
        return Arrays.asList(LocalDateTime.class, LocalDate.class, LocalTime.class, Instant.class, ZonedDateTime.class, ZoneId.class, Period.class, Duration.class).contains(type);
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$LocalDateTimeToBytesConverter.class */
    static class LocalDateTimeToBytesConverter extends BinaryConverters.StringBasedConverter implements Converter<LocalDateTime, byte[]> {
        LocalDateTimeToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(LocalDateTime source) {
            return fromString(source.toString());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$BytesToLocalDateTimeConverter.class */
    static class BytesToLocalDateTimeConverter extends BinaryConverters.StringBasedConverter implements Converter<byte[], LocalDateTime> {
        BytesToLocalDateTimeConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public LocalDateTime convert(byte[] source) {
            return LocalDateTime.parse(toString(source));
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$LocalDateToBytesConverter.class */
    static class LocalDateToBytesConverter extends BinaryConverters.StringBasedConverter implements Converter<LocalDate, byte[]> {
        LocalDateToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(LocalDate source) {
            return fromString(source.toString());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$BytesToLocalDateConverter.class */
    static class BytesToLocalDateConverter extends BinaryConverters.StringBasedConverter implements Converter<byte[], LocalDate> {
        BytesToLocalDateConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public LocalDate convert(byte[] source) {
            return LocalDate.parse(toString(source));
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$LocalTimeToBytesConverter.class */
    static class LocalTimeToBytesConverter extends BinaryConverters.StringBasedConverter implements Converter<LocalTime, byte[]> {
        LocalTimeToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(LocalTime source) {
            return fromString(source.toString());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$BytesToLocalTimeConverter.class */
    static class BytesToLocalTimeConverter extends BinaryConverters.StringBasedConverter implements Converter<byte[], LocalTime> {
        BytesToLocalTimeConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public LocalTime convert(byte[] source) {
            return LocalTime.parse(toString(source));
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$ZonedDateTimeToBytesConverter.class */
    static class ZonedDateTimeToBytesConverter extends BinaryConverters.StringBasedConverter implements Converter<ZonedDateTime, byte[]> {
        ZonedDateTimeToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(ZonedDateTime source) {
            return fromString(source.toString());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$BytesToZonedDateTimeConverter.class */
    static class BytesToZonedDateTimeConverter extends BinaryConverters.StringBasedConverter implements Converter<byte[], ZonedDateTime> {
        BytesToZonedDateTimeConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public ZonedDateTime convert(byte[] source) {
            return ZonedDateTime.parse(toString(source));
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$InstantToBytesConverter.class */
    static class InstantToBytesConverter extends BinaryConverters.StringBasedConverter implements Converter<Instant, byte[]> {
        InstantToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(Instant source) {
            return fromString(source.toString());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$BytesToInstantConverter.class */
    static class BytesToInstantConverter extends BinaryConverters.StringBasedConverter implements Converter<byte[], Instant> {
        BytesToInstantConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public Instant convert(byte[] source) {
            return Instant.parse(toString(source));
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$ZoneIdToBytesConverter.class */
    static class ZoneIdToBytesConverter extends BinaryConverters.StringBasedConverter implements Converter<ZoneId, byte[]> {
        ZoneIdToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(ZoneId source) {
            return fromString(source.toString());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$BytesToZoneIdConverter.class */
    static class BytesToZoneIdConverter extends BinaryConverters.StringBasedConverter implements Converter<byte[], ZoneId> {
        BytesToZoneIdConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public ZoneId convert(byte[] source) {
            return ZoneId.of(toString(source));
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$PeriodToBytesConverter.class */
    static class PeriodToBytesConverter extends BinaryConverters.StringBasedConverter implements Converter<Period, byte[]> {
        PeriodToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(Period source) {
            return fromString(source.toString());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$BytesToPeriodConverter.class */
    static class BytesToPeriodConverter extends BinaryConverters.StringBasedConverter implements Converter<byte[], Period> {
        BytesToPeriodConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public Period convert(byte[] source) {
            return Period.parse(toString(source));
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$DurationToBytesConverter.class */
    static class DurationToBytesConverter extends BinaryConverters.StringBasedConverter implements Converter<Duration, byte[]> {
        DurationToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(Duration source) {
            return fromString(source.toString());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Jsr310Converters$BytesToDurationConverter.class */
    static class BytesToDurationConverter extends BinaryConverters.StringBasedConverter implements Converter<byte[], Duration> {
        BytesToDurationConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public Duration convert(byte[] source) {
            return Duration.parse(toString(source));
        }
    }
}
