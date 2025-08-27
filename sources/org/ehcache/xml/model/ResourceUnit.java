package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "resource-unit")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ResourceUnit.class */
public enum ResourceUnit {
    ENTRIES("entries"),
    B("B"),
    K_B("kB"),
    MB("MB"),
    GB("GB"),
    TB("TB"),
    PB("PB");

    private final String value;

    ResourceUnit(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static ResourceUnit fromValue(String v) {
        ResourceUnit[] arr$ = values();
        for (ResourceUnit c : arr$) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
