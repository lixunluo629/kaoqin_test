package org.springframework.data.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/JodaTimeConverters.class */
public abstract class JodaTimeConverters {
    private static final boolean JODA_TIME_IS_PRESENT = ClassUtils.isPresent("org.joda.time.LocalDate", null);

    public static Collection<Converter<?, ?>> getConvertersToRegister() {
        if (!JODA_TIME_IS_PRESENT) {
            return Collections.emptySet();
        }
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(LocalDateToDateConverter.INSTANCE);
        converters.add(LocalDateTimeToDateConverter.INSTANCE);
        converters.add(DateTimeToDateConverter.INSTANCE);
        converters.add(DateMidnightToDateConverter.INSTANCE);
        converters.add(DateToLocalDateConverter.INSTANCE);
        converters.add(DateToLocalDateTimeConverter.INSTANCE);
        converters.add(DateToDateTimeConverter.INSTANCE);
        converters.add(DateToDateMidnightConverter.INSTANCE);
        return converters;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/JodaTimeConverters$LocalDateToDateConverter.class */
    public enum LocalDateToDateConverter implements Converter<LocalDate, Date> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(LocalDate source) {
            if (source == null) {
                return null;
            }
            return source.toDate();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/JodaTimeConverters$LocalDateTimeToDateConverter.class */
    public enum LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(LocalDateTime source) {
            if (source == null) {
                return null;
            }
            return source.toDate();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/JodaTimeConverters$DateTimeToDateConverter.class */
    public enum DateTimeToDateConverter implements Converter<DateTime, Date> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(DateTime source) {
            if (source == null) {
                return null;
            }
            return source.toDate();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/JodaTimeConverters$DateMidnightToDateConverter.class */
    public enum DateMidnightToDateConverter implements Converter<DateMidnight, Date> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Date convert(DateMidnight source) {
            if (source == null) {
                return null;
            }
            return source.toDate();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/JodaTimeConverters$DateToLocalDateConverter.class */
    public enum DateToLocalDateConverter implements Converter<Date, LocalDate> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public LocalDate convert(Date source) {
            if (source == null) {
                return null;
            }
            return new LocalDate(source.getTime());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/JodaTimeConverters$DateToLocalDateTimeConverter.class */
    public enum DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public LocalDateTime convert(Date source) {
            if (source == null) {
                return null;
            }
            return new LocalDateTime(source.getTime());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/JodaTimeConverters$DateToDateTimeConverter.class */
    public enum DateToDateTimeConverter implements Converter<Date, DateTime> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public DateTime convert(Date source) {
            if (source == null) {
                return null;
            }
            return new DateTime(source.getTime());
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/JodaTimeConverters$DateToDateMidnightConverter.class */
    public enum DateToDateMidnightConverter implements Converter<Date, DateMidnight> {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public DateMidnight convert(Date source) {
            if (source == null) {
                return null;
            }
            return new DateMidnight(source.getTime());
        }
    }
}
