package io.netty.handler.codec.xml;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/xml/XmlContent.class */
public abstract class XmlContent {
    private final String data;

    protected XmlContent(String data) {
        this.data = data;
    }

    public String data() {
        return this.data;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlContent that = (XmlContent) o;
        if (this.data != null) {
            if (!this.data.equals(that.data)) {
                return false;
            }
            return true;
        }
        if (that.data != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.data != null) {
            return this.data.hashCode();
        }
        return 0;
    }

    public String toString() {
        return "XmlContent{data='" + this.data + "'}";
    }
}
