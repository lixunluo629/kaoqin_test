package io.netty.handler.codec.xml;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/xml/XmlNamespace.class */
public class XmlNamespace {
    private final String prefix;
    private final String uri;

    public XmlNamespace(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    public String prefix() {
        return this.prefix;
    }

    public String uri() {
        return this.uri;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlNamespace that = (XmlNamespace) o;
        if (this.prefix != null) {
            if (!this.prefix.equals(that.prefix)) {
                return false;
            }
        } else if (that.prefix != null) {
            return false;
        }
        if (this.uri != null) {
            if (!this.uri.equals(that.uri)) {
                return false;
            }
            return true;
        }
        if (that.uri != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.prefix != null ? this.prefix.hashCode() : 0;
        return (31 * result) + (this.uri != null ? this.uri.hashCode() : 0);
    }

    public String toString() {
        return "XmlNamespace{prefix='" + this.prefix + "', uri='" + this.uri + "'}";
    }
}
