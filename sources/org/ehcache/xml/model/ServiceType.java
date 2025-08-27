package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "service-type", propOrder = {"serviceCreationConfiguration"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ServiceType.class */
public class ServiceType {

    @XmlAnyElement
    protected Element serviceCreationConfiguration;

    public Element getServiceCreationConfiguration() {
        return this.serviceCreationConfiguration;
    }

    public void setServiceCreationConfiguration(Element value) {
        this.serviceCreationConfiguration = value;
    }
}
