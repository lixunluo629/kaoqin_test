package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "event-firing-type")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/EventFiringType.class */
public enum EventFiringType {
    ASYNCHRONOUS,
    SYNCHRONOUS;

    public String value() {
        return name();
    }

    public static EventFiringType fromValue(String v) {
        return valueOf(v);
    }
}
