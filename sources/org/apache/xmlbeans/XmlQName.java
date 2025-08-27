package org.apache.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlQName.class */
public interface XmlQName extends XmlAnySimpleType {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_QName");

    QName getQNameValue();

    void setQNameValue(QName qName);

    QName qNameValue();

    void set(QName qName);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlQName$Factory.class */
    public static final class Factory {
        public static XmlQName newInstance() {
            return (XmlQName) XmlBeans.getContextTypeLoader().newInstance(XmlQName.type, null);
        }

        public static XmlQName newInstance(XmlOptions options) {
            return (XmlQName) XmlBeans.getContextTypeLoader().newInstance(XmlQName.type, options);
        }

        public static XmlQName newValue(Object obj) {
            return (XmlQName) XmlQName.type.newValue(obj);
        }

        public static XmlQName parse(String s) throws XmlException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(s, XmlQName.type, (XmlOptions) null);
        }

        public static XmlQName parse(String s, XmlOptions options) throws XmlException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(s, XmlQName.type, options);
        }

        public static XmlQName parse(File f) throws XmlException, IOException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(f, XmlQName.type, (XmlOptions) null);
        }

        public static XmlQName parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(f, XmlQName.type, options);
        }

        public static XmlQName parse(URL u) throws XmlException, IOException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(u, XmlQName.type, (XmlOptions) null);
        }

        public static XmlQName parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(u, XmlQName.type, options);
        }

        public static XmlQName parse(InputStream is) throws XmlException, IOException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(is, XmlQName.type, (XmlOptions) null);
        }

        public static XmlQName parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(is, XmlQName.type, options);
        }

        public static XmlQName parse(Reader r) throws XmlException, IOException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(r, XmlQName.type, (XmlOptions) null);
        }

        public static XmlQName parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(r, XmlQName.type, options);
        }

        public static XmlQName parse(Node node) throws XmlException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(node, XmlQName.type, (XmlOptions) null);
        }

        public static XmlQName parse(Node node, XmlOptions options) throws XmlException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(node, XmlQName.type, options);
        }

        public static XmlQName parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(xis, XmlQName.type, (XmlOptions) null);
        }

        public static XmlQName parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(xis, XmlQName.type, options);
        }

        public static XmlQName parse(XMLStreamReader xsr) throws XmlException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(xsr, XmlQName.type, (XmlOptions) null);
        }

        public static XmlQName parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlQName) XmlBeans.getContextTypeLoader().parse(xsr, XmlQName.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlQName.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlQName.type, options);
        }

        private Factory() {
        }
    }
}
