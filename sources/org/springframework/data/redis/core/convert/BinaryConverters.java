package org.springframework.data.redis.core.convert;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.util.NumberUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters.class */
final class BinaryConverters {
    public static final Charset CHARSET = Charset.forName("UTF-8");

    private BinaryConverters() {
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$StringBasedConverter.class */
    static class StringBasedConverter {
        StringBasedConverter() {
        }

        byte[] fromString(String source) {
            if (source == null) {
                return new byte[0];
            }
            return source.getBytes(BinaryConverters.CHARSET);
        }

        String toString(byte[] source) {
            return new String(source, BinaryConverters.CHARSET);
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$StringToBytesConverter.class */
    static class StringToBytesConverter extends StringBasedConverter implements Converter<String, byte[]> {
        StringToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(String source) {
            return fromString(source);
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$BytesToStringConverter.class */
    static class BytesToStringConverter extends StringBasedConverter implements Converter<byte[], String> {
        BytesToStringConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public String convert(byte[] source) {
            return toString(source);
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$NumberToBytesConverter.class */
    static class NumberToBytesConverter extends StringBasedConverter implements Converter<Number, byte[]> {
        NumberToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(Number source) {
            if (source == null) {
                return new byte[0];
            }
            return fromString(source.toString());
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$EnumToBytesConverter.class */
    static class EnumToBytesConverter extends StringBasedConverter implements Converter<Enum<?>, byte[]> {
        EnumToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(Enum<?> source) {
            if (source == null) {
                return new byte[0];
            }
            return fromString(source.name());
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$BytesToEnumConverterFactory.class */
    static final class BytesToEnumConverterFactory implements ConverterFactory<byte[], Enum<?>> {
        BytesToEnumConverterFactory() {
        }

        @Override // org.springframework.core.convert.converter.ConverterFactory
        public <T extends Enum<?>> Converter<byte[], T> getConverter(Class<T> targetType) {
            Class<T> cls;
            Class<T> superclass = targetType;
            while (true) {
                cls = superclass;
                if (cls == null || cls.isEnum()) {
                    break;
                }
                superclass = cls.getSuperclass();
            }
            if (cls == null) {
                throw new IllegalArgumentException("The target type " + targetType.getName() + " does not refer to an enum");
            }
            return new BytesToEnum(cls);
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$BytesToEnumConverterFactory$BytesToEnum.class */
        private class BytesToEnum<T extends Enum<T>> extends StringBasedConverter implements Converter<byte[], T> {
            private final Class<T> enumType;

            public BytesToEnum(Class<T> enumType) {
                this.enumType = enumType;
            }

            @Override // org.springframework.core.convert.converter.Converter
            public T convert(byte[] bArr) {
                String string = toString(bArr);
                if (string == null || string.length() == 0) {
                    return null;
                }
                return (T) Enum.valueOf(this.enumType, string.trim());
            }
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$BytesToNumberConverterFactory.class */
    static class BytesToNumberConverterFactory implements ConverterFactory<byte[], Number> {
        BytesToNumberConverterFactory() {
        }

        @Override // org.springframework.core.convert.converter.ConverterFactory
        public <T extends Number> Converter<byte[], T> getConverter(Class<T> targetType) {
            return new BytesToNumberConverter(targetType);
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$BytesToNumberConverterFactory$BytesToNumberConverter.class */
        private static final class BytesToNumberConverter<T extends Number> extends StringBasedConverter implements Converter<byte[], T> {
            private final Class<T> targetType;

            public BytesToNumberConverter(Class<T> targetType) {
                this.targetType = targetType;
            }

            @Override // org.springframework.core.convert.converter.Converter
            public T convert(byte[] bArr) {
                if (bArr == null || bArr.length == 0) {
                    return null;
                }
                return (T) NumberUtils.parseNumber(toString(bArr), this.targetType);
            }
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$BooleanToBytesConverter.class */
    static class BooleanToBytesConverter extends StringBasedConverter implements Converter<Boolean, byte[]> {
        final byte[] _true = fromString("1");
        final byte[] _false = fromString("0");

        BooleanToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(Boolean source) {
            if (source == null) {
                return new byte[0];
            }
            return source.booleanValue() ? this._true : this._false;
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$BytesToBooleanConverter.class */
    static class BytesToBooleanConverter extends StringBasedConverter implements Converter<byte[], Boolean> {
        BytesToBooleanConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public Boolean convert(byte[] source) {
            if (source == null || source.length == 0) {
                return null;
            }
            String value = toString(source);
            return ("1".equals(value) || "true".equalsIgnoreCase(value)) ? Boolean.TRUE : Boolean.FALSE;
        }
    }

    @WritingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$DateToBytesConverter.class */
    static class DateToBytesConverter extends StringBasedConverter implements Converter<Date, byte[]> {
        DateToBytesConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public byte[] convert(Date source) {
            if (source == null) {
                return new byte[0];
            }
            return fromString(Long.toString(source.getTime()));
        }
    }

    @ReadingConverter
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/BinaryConverters$BytesToDateConverter.class */
    static class BytesToDateConverter extends StringBasedConverter implements Converter<byte[], Date> {
        BytesToDateConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(byte[] source) {
            if (source == null || source.length == 0) {
                return null;
            }
            String value = toString(source);
            try {
                return new Date(((Long) NumberUtils.parseNumber(value, Long.class)).longValue());
            } catch (NumberFormatException e) {
                try {
                    return DateFormat.getInstance().parse(value);
                } catch (ParseException e2) {
                    throw new IllegalArgumentException("Cannot parse date out of " + source);
                }
            }
        }
    }
}
