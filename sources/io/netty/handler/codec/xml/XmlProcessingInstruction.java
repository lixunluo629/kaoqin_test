package io.netty.handler.codec.xml;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/xml/XmlProcessingInstruction.class */
public class XmlProcessingInstruction {
    private final String data;
    private final String target;

    public XmlProcessingInstruction(String data, String target) {
        this.data = data;
        this.target = target;
    }

    public String data() {
        return this.data;
    }

    public String target() {
        return this.target;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlProcessingInstruction that = (XmlProcessingInstruction) o;
        if (this.data != null) {
            if (!this.data.equals(that.data)) {
                return false;
            }
        } else if (that.data != null) {
            return false;
        }
        if (this.target != null) {
            if (!this.target.equals(that.target)) {
                return false;
            }
            return true;
        }
        if (that.target != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.data != null ? this.data.hashCode() : 0;
        return (31 * result) + (this.target != null ? this.target.hashCode() : 0);
    }

    public String toString() {
        return "XmlProcessingInstruction{data='" + this.data + "', target='" + this.target + "'}";
    }
}
