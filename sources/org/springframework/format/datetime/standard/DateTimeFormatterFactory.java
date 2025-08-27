package org.springframework.format.datetime.standard;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.ResolverStyle;
import java.util.TimeZone;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@UsesJava8
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/standard/DateTimeFormatterFactory.class */
public class DateTimeFormatterFactory {
    private String pattern;
    private DateTimeFormat.ISO iso;
    private FormatStyle dateStyle;
    private FormatStyle timeStyle;
    private TimeZone timeZone;

    public DateTimeFormatterFactory() {
    }

    public DateTimeFormatterFactory(String pattern) {
        this.pattern = pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setIso(DateTimeFormat.ISO iso) {
        this.iso = iso;
    }

    public void setDateStyle(FormatStyle dateStyle) {
        this.dateStyle = dateStyle;
    }

    public void setTimeStyle(FormatStyle timeStyle) {
        this.timeStyle = timeStyle;
    }

    public void setDateTimeStyle(FormatStyle dateTimeStyle) {
        this.dateStyle = dateTimeStyle;
        this.timeStyle = dateTimeStyle;
    }

    public void setStylePattern(String style) {
        Assert.isTrue(style != null && style.length() == 2, "Style pattern must consist of two characters");
        this.dateStyle = convertStyleCharacter(style.charAt(0));
        this.timeStyle = convertStyleCharacter(style.charAt(1));
    }

    private FormatStyle convertStyleCharacter(char c) {
        switch (c) {
            case '-':
                return null;
            case 'F':
                return FormatStyle.FULL;
            case 'L':
                return FormatStyle.LONG;
            case 'M':
                return FormatStyle.MEDIUM;
            case 'S':
                return FormatStyle.SHORT;
            default:
                throw new IllegalArgumentException("Invalid style character '" + c + "'");
        }
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public DateTimeFormatter createDateTimeFormatter() {
        return createDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
    }

    public DateTimeFormatter createDateTimeFormatter(DateTimeFormatter fallbackFormatter) {
        DateTimeFormatter dateTimeFormatter = null;
        if (StringUtils.hasLength(this.pattern)) {
            String patternToUse = this.pattern.replace("yy", "uu");
            dateTimeFormatter = DateTimeFormatter.ofPattern(patternToUse).withResolverStyle(ResolverStyle.STRICT);
        } else if (this.iso != null && this.iso != DateTimeFormat.ISO.NONE) {
            switch (this.iso) {
                case DATE:
                    dateTimeFormatter = DateTimeFormatter.ISO_DATE;
                    break;
                case TIME:
                    dateTimeFormatter = DateTimeFormatter.ISO_TIME;
                    break;
                case DATE_TIME:
                    dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
                    break;
                case NONE:
                    break;
                default:
                    throw new IllegalStateException("Unsupported ISO format: " + this.iso);
            }
        } else if (this.dateStyle != null && this.timeStyle != null) {
            dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(this.dateStyle, this.timeStyle);
        } else if (this.dateStyle != null) {
            dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(this.dateStyle);
        } else if (this.timeStyle != null) {
            dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(this.timeStyle);
        }
        if (dateTimeFormatter != null && this.timeZone != null) {
            dateTimeFormatter = dateTimeFormatter.withZone(this.timeZone.toZoneId());
        }
        return dateTimeFormatter != null ? dateTimeFormatter : fallbackFormatter;
    }
}
