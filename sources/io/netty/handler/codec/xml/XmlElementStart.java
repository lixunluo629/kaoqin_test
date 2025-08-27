package io.netty.handler.codec.xml;

import java.util.LinkedList;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/xml/XmlElementStart.class */
public class XmlElementStart extends XmlElement {
    private final List<XmlAttribute> attributes;

    public XmlElementStart(String name, String namespace, String prefix) {
        super(name, namespace, prefix);
        this.attributes = new LinkedList();
    }

    public List<XmlAttribute> attributes() {
        return this.attributes;
    }

    @Override // io.netty.handler.codec.xml.XmlElement
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || !super.equals(o)) {
            return false;
        }
        XmlElementStart that = (XmlElementStart) o;
        if (this.attributes != null) {
            if (!this.attributes.equals(that.attributes)) {
                return false;
            }
            return true;
        }
        if (that.attributes != null) {
            return false;
        }
        return true;
    }

    @Override // io.netty.handler.codec.xml.XmlElement
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.attributes != null ? this.attributes.hashCode() : 0);
    }

    @Override // io.netty.handler.codec.xml.XmlElement
    public String toString() {
        return "XmlElementStart{attributes=" + this.attributes + super.toString() + "} ";
    }
}
