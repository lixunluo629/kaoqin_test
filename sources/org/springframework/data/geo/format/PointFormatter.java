package org.springframework.data.geo.format;

import java.text.ParseException;
import java.util.Locale;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.geo.Point;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/format/PointFormatter.class */
public enum PointFormatter implements Converter<String, Point>, Formatter<Point> {
    INSTANCE;

    public static final GenericConverter.ConvertiblePair CONVERTIBLE = new GenericConverter.ConvertiblePair(String.class, Point.class);
    private static final String INVALID_FORMAT = "Expected two doubles separated by a comma but got '%s'!";

    @Override // org.springframework.core.convert.converter.Converter
    public Point convert(String source) throws NumberFormatException {
        String[] parts = source.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, source));
        }
        try {
            double latitude = Double.parseDouble(parts[0]);
            double longitude = Double.parseDouble(parts[1]);
            return new Point(longitude, latitude);
        } catch (NumberFormatException o_O) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, source), o_O);
        }
    }

    @Override // org.springframework.format.Printer
    public String print(Point point, Locale locale) {
        if (point == null) {
            return null;
        }
        return String.format("%s,%s", Double.valueOf(point.getY()), Double.valueOf(point.getX()));
    }

    @Override // org.springframework.format.Parser
    public Point parse(String text, Locale locale) throws ParseException {
        if (StringUtils.hasText(text)) {
            return convert(text);
        }
        return null;
    }
}
