package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Marker;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/pattern/MarkerConverter.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/pattern/MarkerConverter.class */
public class MarkerConverter extends ClassicConverter {
    private static String EMPTY = "";

    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent le) {
        Marker marker = le.getMarker();
        if (marker == null) {
            return EMPTY;
        }
        return marker.toString();
    }
}
