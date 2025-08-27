package io.netty.handler.codec.xml;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/xml/XmlElementEnd.class */
public class XmlElementEnd extends XmlElement {
    public XmlElementEnd(String name, String namespace, String prefix) {
        super(name, namespace, prefix);
    }

    @Override // io.netty.handler.codec.xml.XmlElement
    public String toString() {
        return "XmlElementStart{" + super.toString() + "} ";
    }
}
