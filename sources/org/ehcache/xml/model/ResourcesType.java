package org.ehcache.xml.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resources-type", propOrder = {"resource"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ResourcesType.class */
public class ResourcesType {

    @XmlAnyElement
    protected List<Element> resource;

    public List<Element> getResource() {
        if (this.resource == null) {
            this.resource = new ArrayList();
        }
        return this.resource;
    }
}
