package org.ehcache.xml.model;

import io.swagger.models.properties.StringProperty;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeners-type", propOrder = {"listener"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ListenersType.class */
public class ListenersType {
    protected List<Listener> listener;

    @XmlAttribute(name = "dispatcher-thread-pool")
    protected String dispatcherThreadPool;

    @XmlSchemaType(name = "positiveInteger")
    @XmlAttribute(name = "dispatcher-concurrency")
    protected BigInteger dispatcherConcurrency;

    public List<Listener> getListener() {
        if (this.listener == null) {
            this.listener = new ArrayList();
        }
        return this.listener;
    }

    public String getDispatcherThreadPool() {
        return this.dispatcherThreadPool;
    }

    public void setDispatcherThreadPool(String value) {
        this.dispatcherThreadPool = value;
    }

    public BigInteger getDispatcherConcurrency() {
        if (this.dispatcherConcurrency == null) {
            return new BigInteger("8");
        }
        return this.dispatcherConcurrency;
    }

    public void setDispatcherConcurrency(BigInteger value) {
        this.dispatcherConcurrency = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"clazz", "eventFiringMode", "eventOrderingMode", "eventsToFireOn"})
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ListenersType$Listener.class */
    public static class Listener {

        @XmlElement(name = "class", required = true)
        protected String clazz;

        @XmlSchemaType(name = StringProperty.TYPE)
        @XmlElement(name = "event-firing-mode", required = true)
        protected EventFiringType eventFiringMode;

        @XmlSchemaType(name = StringProperty.TYPE)
        @XmlElement(name = "event-ordering-mode", required = true)
        protected EventOrderingType eventOrderingMode;

        @XmlSchemaType(name = StringProperty.TYPE)
        @XmlElement(name = "events-to-fire-on", required = true)
        protected List<EventType> eventsToFireOn;

        public String getClazz() {
            return this.clazz;
        }

        public void setClazz(String value) {
            this.clazz = value;
        }

        public EventFiringType getEventFiringMode() {
            return this.eventFiringMode;
        }

        public void setEventFiringMode(EventFiringType value) {
            this.eventFiringMode = value;
        }

        public EventOrderingType getEventOrderingMode() {
            return this.eventOrderingMode;
        }

        public void setEventOrderingMode(EventOrderingType value) {
            this.eventOrderingMode = value;
        }

        public List<EventType> getEventsToFireOn() {
            if (this.eventsToFireOn == null) {
                this.eventsToFireOn = new ArrayList();
            }
            return this.eventsToFireOn;
        }
    }
}
