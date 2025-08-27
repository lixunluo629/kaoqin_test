package io.netty.handler.codec.xml;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/xml/XmlDocumentStart.class */
public class XmlDocumentStart {
    private final String encoding;
    private final String version;
    private final boolean standalone;
    private final String encodingScheme;

    public XmlDocumentStart(String encoding, String version, boolean standalone, String encodingScheme) {
        this.encoding = encoding;
        this.version = version;
        this.standalone = standalone;
        this.encodingScheme = encodingScheme;
    }

    public String encoding() {
        return this.encoding;
    }

    public String version() {
        return this.version;
    }

    public boolean standalone() {
        return this.standalone;
    }

    public String encodingScheme() {
        return this.encodingScheme;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlDocumentStart that = (XmlDocumentStart) o;
        if (this.standalone != that.standalone) {
            return false;
        }
        if (this.encoding != null) {
            if (!this.encoding.equals(that.encoding)) {
                return false;
            }
        } else if (that.encoding != null) {
            return false;
        }
        if (this.encodingScheme != null) {
            if (!this.encodingScheme.equals(that.encodingScheme)) {
                return false;
            }
        } else if (that.encodingScheme != null) {
            return false;
        }
        if (this.version != null) {
            if (!this.version.equals(that.version)) {
                return false;
            }
            return true;
        }
        if (that.version != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.encoding != null ? this.encoding.hashCode() : 0;
        return (31 * ((31 * ((31 * result) + (this.version != null ? this.version.hashCode() : 0))) + (this.standalone ? 1 : 0))) + (this.encodingScheme != null ? this.encodingScheme.hashCode() : 0);
    }

    public String toString() {
        return "XmlDocumentStart{encoding='" + this.encoding + "', version='" + this.version + "', standalone=" + this.standalone + ", encodingScheme='" + this.encodingScheme + "'}";
    }
}
