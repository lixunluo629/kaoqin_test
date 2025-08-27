package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "memory-unit")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/MemoryUnit.class */
public enum MemoryUnit {
    B("B"),
    K_B("kB"),
    MB("MB"),
    GB("GB"),
    TB("TB"),
    PB("PB");

    private final String value;

    MemoryUnit(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static MemoryUnit fromValue(String v) {
        MemoryUnit[] arr$ = values();
        for (MemoryUnit c : arr$) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
