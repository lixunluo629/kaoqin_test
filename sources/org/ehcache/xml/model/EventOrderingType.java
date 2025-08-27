package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "event-ordering-type")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/EventOrderingType.class */
public enum EventOrderingType {
    UNORDERED,
    ORDERED;

    public String value() {
        return name();
    }

    public static EventOrderingType fromValue(String v) {
        return valueOf(v);
    }
}
