package org.ehcache.xml;

import java.util.concurrent.TimeUnit;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/XmlModel.class */
public class XmlModel {
    public static TimeUnit convertToJavaTimeUnit(org.ehcache.xml.model.TimeUnit unit) {
        switch (unit) {
            case NANOS:
                return TimeUnit.NANOSECONDS;
            case MICROS:
                return TimeUnit.MICROSECONDS;
            case MILLIS:
                return TimeUnit.MILLISECONDS;
            case SECONDS:
                return TimeUnit.SECONDS;
            case MINUTES:
                return TimeUnit.MINUTES;
            case HOURS:
                return TimeUnit.HOURS;
            case DAYS:
                return TimeUnit.DAYS;
            default:
                throw new IllegalArgumentException("Unknown time unit: " + unit);
        }
    }
}
