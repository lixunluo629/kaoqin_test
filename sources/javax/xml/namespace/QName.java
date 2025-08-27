package javax.xml.namespace;

import java.io.Serializable;

/* loaded from: stax-api-1.0.1.jar:javax/xml/namespace/QName.class */
public class QName implements Serializable {
    private String namespaceURI;
    private String localPart;
    private String prefix;

    public QName(String localPart) {
        this("", localPart);
    }

    public QName(String namespaceURI, String localPart) {
        this(namespaceURI, localPart, "");
    }

    public QName(String namespaceURI, String localPart, String prefix) {
        if (localPart == null) {
            throw new IllegalArgumentException("Local part not allowed to be null");
        }
        namespaceURI = namespaceURI == null ? "" : namespaceURI;
        prefix = prefix == null ? "" : prefix;
        this.namespaceURI = namespaceURI;
        this.localPart = localPart;
        this.prefix = prefix;
    }

    public String getNamespaceURI() {
        return this.namespaceURI;
    }

    public String getLocalPart() {
        return this.localPart;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String toString() {
        if (this.namespaceURI.equals("")) {
            return this.localPart;
        }
        return new StringBuffer().append("{").append(this.namespaceURI).append("}").append(this.localPart).toString();
    }

    public static QName valueOf(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException("invalid QName literal");
        }
        if (s.charAt(0) == '{') {
            int i = s.indexOf(125);
            if (i == -1) {
                throw new IllegalArgumentException("invalid QName literal");
            }
            if (i == s.length() - 1) {
                throw new IllegalArgumentException("invalid QName literal");
            }
            return new QName(s.substring(1, i), s.substring(i + 1));
        }
        return new QName(s);
    }

    public final int hashCode() {
        return this.namespaceURI.hashCode() ^ this.localPart.hashCode();
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof QName)) {
            return false;
        }
        QName qname = (QName) obj;
        return this.localPart.equals(qname.localPart) && this.namespaceURI.equals(qname.namespaceURI);
    }
}
