package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "event-type")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/EventType.class */
public enum EventType {
    EVICTED,
    EXPIRED,
    REMOVED,
    CREATED,
    UPDATED;

    public String value() {
        return name();
    }

    public static EventType fromValue(String v) {
        return valueOf(v);
    }
}
