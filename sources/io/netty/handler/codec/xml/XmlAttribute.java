package io.netty.handler.codec.xml;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/xml/XmlAttribute.class */
public class XmlAttribute {
    private final String type;
    private final String name;
    private final String prefix;
    private final String namespace;
    private final String value;

    public XmlAttribute(String type, String name, String prefix, String namespace, String value) {
        this.type = type;
        this.name = name;
        this.prefix = prefix;
        this.namespace = namespace;
        this.value = value;
    }

    public String type() {
        return this.type;
    }

    public String name() {
        return this.name;
    }

    public String prefix() {
        return this.prefix;
    }

    public String namespace() {
        return this.namespace;
    }

    public String value() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlAttribute that = (XmlAttribute) o;
        if (!this.name.equals(that.name)) {
            return false;
        }
        if (this.namespace != null) {
            if (!this.namespace.equals(that.namespace)) {
                return false;
            }
        } else if (that.namespace != null) {
            return false;
        }
        if (this.prefix != null) {
            if (!this.prefix.equals(that.prefix)) {
                return false;
            }
        } else if (that.prefix != null) {
            return false;
        }
        if (this.type != null) {
            if (!this.type.equals(that.type)) {
                return false;
            }
        } else if (that.type != null) {
            return false;
        }
        if (this.value != null) {
            if (!this.value.equals(that.value)) {
                return false;
            }
            return true;
        }
        if (that.value != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.type != null ? this.type.hashCode() : 0;
        return (31 * ((31 * ((31 * ((31 * result) + this.name.hashCode())) + (this.prefix != null ? this.prefix.hashCode() : 0))) + (this.namespace != null ? this.namespace.hashCode() : 0))) + (this.value != null ? this.value.hashCode() : 0);
    }

    public String toString() {
        return "XmlAttribute{type='" + this.type + "', name='" + this.name + "', prefix='" + this.prefix + "', namespace='" + this.namespace + "', value='" + this.value + "'}";
    }
}
