package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "time-unit")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/TimeUnit.class */
public enum TimeUnit {
    NANOS("nanos"),
    MICROS("micros"),
    MILLIS("millis"),
    SECONDS("seconds"),
    MINUTES("minutes"),
    HOURS("hours"),
    DAYS("days");

    private final String value;

    TimeUnit(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static TimeUnit fromValue(String v) {
        TimeUnit[] arr$ = values();
        for (TimeUnit c : arr$) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
