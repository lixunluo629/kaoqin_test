package org.springframework.format.datetime.joda;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.DateTimeFormat;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/joda/JodaDateTimeFormatAnnotationFormatterFactory.class */
public class JodaDateTimeFormatAnnotationFormatterFactory extends EmbeddedValueResolutionSupport implements AnnotationFormatterFactory<DateTimeFormat> {
    private static final Set<Class<?>> FIELD_TYPES;

    @Override // org.springframework.format.AnnotationFormatterFactory
    public /* bridge */ /* synthetic */ Parser getParser(Annotation annotation, Class cls) {
        return getParser((DateTimeFormat) annotation, (Class<?>) cls);
    }

    @Override // org.springframework.format.AnnotationFormatterFactory
    public /* bridge */ /* synthetic */ Printer getPrinter(Annotation annotation, Class cls) {
        return getPrinter((DateTimeFormat) annotation, (Class<?>) cls);
    }

    static {
        Set<Class<?>> fieldTypes = new HashSet<>(8);
        fieldTypes.add(ReadableInstant.class);
        fieldTypes.add(LocalDate.class);
        fieldTypes.add(LocalTime.class);
        fieldTypes.add(LocalDateTime.class);
        fieldTypes.add(Date.class);
        fieldTypes.add(Calendar.class);
        fieldTypes.add(Long.class);
        FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
    }

    @Override // org.springframework.format.AnnotationFormatterFactory
    public final Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
        DateTimeFormatter formatter = getFormatter(annotation, fieldType);
        if (ReadablePartial.class.isAssignableFrom(fieldType)) {
            return new ReadablePartialPrinter(formatter);
        }
        if (ReadableInstant.class.isAssignableFrom(fieldType) || Calendar.class.isAssignableFrom(fieldType)) {
            return new ReadableInstantPrinter(formatter);
        }
        return new MillisecondInstantPrinter(formatter);
    }

    public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
        if (LocalDate.class == fieldType) {
            return new LocalDateParser(getFormatter(annotation, fieldType));
        }
        if (LocalTime.class == fieldType) {
            return new LocalTimeParser(getFormatter(annotation, fieldType));
        }
        if (LocalDateTime.class == fieldType) {
            return new LocalDateTimeParser(getFormatter(annotation, fieldType));
        }
        return new DateTimeParser(getFormatter(annotation, fieldType));
    }

    protected DateTimeFormatter getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
        DateTimeFormatterFactory factory = new DateTimeFormatterFactory();
        factory.setStyle(resolveEmbeddedValue(annotation.style()));
        factory.setIso(annotation.iso());
        factory.setPattern(resolveEmbeddedValue(annotation.pattern()));
        return factory.createDateTimeFormatter();
    }
}
