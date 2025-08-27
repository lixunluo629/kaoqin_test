package net.dongliu.apk.parser.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceStartTag;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/XmlNamespaces.class */
class XmlNamespaces {
    private List<XmlNamespace> namespaces = new ArrayList();
    private List<XmlNamespace> newNamespaces = new ArrayList();

    public void addNamespace(XmlNamespaceStartTag tag) {
        XmlNamespace namespace = new XmlNamespace(tag.getPrefix(), tag.getUri());
        this.namespaces.add(namespace);
        this.newNamespaces.add(namespace);
    }

    public void removeNamespace(XmlNamespaceEndTag tag) {
        XmlNamespace namespace = new XmlNamespace(tag.getPrefix(), tag.getUri());
        this.namespaces.remove(namespace);
        this.newNamespaces.remove(namespace);
    }

    public String getPrefixViaUri(String uri) {
        if (uri == null) {
            return null;
        }
        for (XmlNamespace namespace : this.namespaces) {
            if (namespace.uri.equals(uri)) {
                return namespace.prefix;
            }
        }
        return null;
    }

    public List<XmlNamespace> consumeNameSpaces() {
        if (!this.newNamespaces.isEmpty()) {
            List<XmlNamespace> xmlNamespaces = new ArrayList<>();
            xmlNamespaces.addAll(this.newNamespaces);
            this.newNamespaces.clear();
            return xmlNamespaces;
        }
        return Collections.emptyList();
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/XmlNamespaces$XmlNamespace.class */
    public static class XmlNamespace {
        private String prefix;
        private String uri;

        private XmlNamespace(String prefix, String uri) {
            this.prefix = prefix;
            this.uri = uri;
        }

        public String getPrefix() {
            return this.prefix;
        }

        public String getUri() {
            return this.uri;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            XmlNamespace namespace = (XmlNamespace) o;
            if (this.prefix == null && namespace.prefix != null) {
                return false;
            }
            if (this.uri == null && namespace.uri != null) {
                return false;
            }
            if (this.prefix == null || this.prefix.equals(namespace.prefix)) {
                return this.uri == null || this.uri.equals(namespace.uri);
            }
            return false;
        }

        public int hashCode() {
            int result = this.prefix.hashCode();
            return (31 * result) + this.uri.hashCode();
        }
    }
}
