package org.ehcache.xml.model;

import io.netty.handler.codec.rtsp.RtspHeaders;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "expiry-type", propOrder = {"clazz", "tti", RtspHeaders.Values.TTL, "none"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ExpiryType.class */
public class ExpiryType {

    @XmlElement(name = "class")
    protected String clazz;
    protected TimeType tti;
    protected TimeType ttl;
    protected None none;

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ExpiryType$None.class */
    public static class None {
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String value) {
        this.clazz = value;
    }

    public TimeType getTti() {
        return this.tti;
    }

    public void setTti(TimeType value) {
        this.tti = value;
    }

    public TimeType getTtl() {
        return this.ttl;
    }

    public void setTtl(TimeType value) {
        this.ttl = value;
    }

    public None getNone() {
        return this.none;
    }

    public void setNone(None value) {
        this.none = value;
    }
}
