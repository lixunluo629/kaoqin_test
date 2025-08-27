package org.springframework.data.geo.format;

import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Metrics;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/format/DistanceFormatter.class */
public enum DistanceFormatter implements Converter<String, Distance>, Formatter<Distance> {
    INSTANCE;

    private static final Map<String, Metric> SUPPORTED_METRICS;
    private static final String INVALID_DISTANCE = "Expected double amount optionally followed by a metrics abbreviation (%s) but got '%s'!";

    static {
        Map<String, Metric> metrics = new LinkedHashMap<>();
        for (Metric metric : Metrics.values()) {
            metrics.put(metric.getAbbreviation(), metric);
            metrics.put(metric.toString().toLowerCase(Locale.US), metric);
        }
        SUPPORTED_METRICS = Collections.unmodifiableMap(metrics);
    }

    @Override // org.springframework.core.convert.converter.Converter
    public final Distance convert(String source) {
        if (source == null) {
            return null;
        }
        return doConvert(source.trim().toLowerCase(Locale.US));
    }

    @Override // org.springframework.format.Printer
    public String print(Distance distance, Locale locale) {
        if (distance == null) {
            return null;
        }
        return String.format("%s%s", Double.valueOf(distance.getValue()), distance.getUnit().toLowerCase(locale));
    }

    @Override // org.springframework.format.Parser
    public Distance parse(String text, Locale locale) throws ParseException {
        if (StringUtils.hasText(text)) {
            return doConvert(text.trim().toLowerCase(locale));
        }
        return null;
    }

    private static Distance doConvert(String source) {
        for (Map.Entry<String, Metric> metric : SUPPORTED_METRICS.entrySet()) {
            if (source.endsWith(metric.getKey())) {
                return fromString(source, metric);
            }
        }
        try {
            return new Distance(Double.parseDouble(source));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(INVALID_DISTANCE, StringUtils.collectionToCommaDelimitedString(SUPPORTED_METRICS.keySet()), source));
        }
    }

    private static Distance fromString(String source, Map.Entry<String, Metric> metric) {
        String amountString = source.substring(0, source.indexOf(metric.getKey()));
        try {
            return new Distance(Double.parseDouble(amountString), metric.getValue());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(INVALID_DISTANCE, StringUtils.collectionToCommaDelimitedString(SUPPORTED_METRICS.keySet()), source));
        }
    }
}
