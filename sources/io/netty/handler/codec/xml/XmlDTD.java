package io.netty.handler.codec.xml;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/xml/XmlDTD.class */
public class XmlDTD {
    private final String text;

    public XmlDTD(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlDTD xmlDTD = (XmlDTD) o;
        if (this.text != null) {
            if (!this.text.equals(xmlDTD.text)) {
                return false;
            }
            return true;
        }
        if (xmlDTD.text != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.text != null) {
            return this.text.hashCode();
        }
        return 0;
    }

    public String toString() {
        return "XmlDTD{text='" + this.text + "'}";
    }
}
