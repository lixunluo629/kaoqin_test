package org.apache.xmlbeans.impl.common;

import org.apache.xmlbeans.xml.stream.XMLName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XmlNameImpl.class */
public class XmlNameImpl implements XMLName {
    private String namespaceUri;
    private String localName;
    private String prefix;
    private int hash;

    public XmlNameImpl() {
        this.namespaceUri = null;
        this.localName = null;
        this.prefix = null;
        this.hash = 0;
    }

    public XmlNameImpl(String localName) {
        this.namespaceUri = null;
        this.localName = null;
        this.prefix = null;
        this.hash = 0;
        this.localName = localName;
    }

    public XmlNameImpl(String namespaceUri, String localName) {
        this.namespaceUri = null;
        this.localName = null;
        this.prefix = null;
        this.hash = 0;
        setNamespaceUri(namespaceUri);
        this.localName = localName;
    }

    public XmlNameImpl(String namespaceUri, String localName, String prefix) {
        this.namespaceUri = null;
        this.localName = null;
        this.prefix = null;
        this.hash = 0;
        setNamespaceUri(namespaceUri);
        this.localName = localName;
        this.prefix = prefix;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLName
    public String getNamespaceUri() {
        return this.namespaceUri;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLName
    public String getLocalName() {
        return this.localName;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLName
    public String getPrefix() {
        return this.prefix;
    }

    public void setNamespaceUri(String namespaceUri) {
        this.hash = 0;
        if (namespaceUri == null || !namespaceUri.equals("")) {
            this.namespaceUri = namespaceUri;
        }
    }

    public void setLocalName(String localName) {
        this.localName = localName;
        this.hash = 0;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override // org.apache.xmlbeans.xml.stream.XMLName
    public String getQualifiedName() {
        if (this.prefix != null && this.prefix.length() > 0) {
            return this.prefix + ":" + this.localName;
        }
        return this.localName;
    }

    public String toString() {
        if (getNamespaceUri() != null) {
            return "['" + getNamespaceUri() + "']:" + getQualifiedName();
        }
        return getQualifiedName();
    }

    public final int hashCode() {
        int tmp_hash = this.hash;
        if (tmp_hash == 0) {
            tmp_hash = 17;
            if (this.namespaceUri != null) {
                tmp_hash = (37 * 17) + this.namespaceUri.hashCode();
            }
            if (this.localName != null) {
                tmp_hash = (37 * tmp_hash) + this.localName.hashCode();
            }
            this.hash = tmp_hash;
        }
        return tmp_hash;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof XMLName) {
            XMLName name = (XMLName) obj;
            String lname = this.localName;
            if (lname == null) {
                if (name.getLocalName() != null) {
                    return false;
                }
            } else if (!lname.equals(name.getLocalName())) {
                return false;
            }
            String uri = this.namespaceUri;
            return uri == null ? name.getNamespaceUri() == null : uri.equals(name.getNamespaceUri());
        }
        return false;
    }
}
