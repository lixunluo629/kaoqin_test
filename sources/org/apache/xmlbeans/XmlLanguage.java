package org.apache.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlLanguage.class */
public interface XmlLanguage extends XmlToken {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_language");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlLanguage$Factory.class */
    public static final class Factory {
        public static XmlLanguage newInstance() {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().newInstance(XmlLanguage.type, null);
        }

        public static XmlLanguage newInstance(XmlOptions options) {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().newInstance(XmlLanguage.type, options);
        }

        public static XmlLanguage newValue(Object obj) {
            return (XmlLanguage) XmlLanguage.type.newValue(obj);
        }

        public static XmlLanguage parse(String s) throws XmlException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(s, XmlLanguage.type, (XmlOptions) null);
        }

        public static XmlLanguage parse(String s, XmlOptions options) throws XmlException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(s, XmlLanguage.type, options);
        }

        public static XmlLanguage parse(File f) throws XmlException, IOException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(f, XmlLanguage.type, (XmlOptions) null);
        }

        public static XmlLanguage parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(f, XmlLanguage.type, options);
        }

        public static XmlLanguage parse(URL u) throws XmlException, IOException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(u, XmlLanguage.type, (XmlOptions) null);
        }

        public static XmlLanguage parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(u, XmlLanguage.type, options);
        }

        public static XmlLanguage parse(InputStream is) throws XmlException, IOException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(is, XmlLanguage.type, (XmlOptions) null);
        }

        public static XmlLanguage parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(is, XmlLanguage.type, options);
        }

        public static XmlLanguage parse(Reader r) throws XmlException, IOException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(r, XmlLanguage.type, (XmlOptions) null);
        }

        public static XmlLanguage parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(r, XmlLanguage.type, options);
        }

        public static XmlLanguage parse(Node node) throws XmlException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(node, XmlLanguage.type, (XmlOptions) null);
        }

        public static XmlLanguage parse(Node node, XmlOptions options) throws XmlException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(node, XmlLanguage.type, options);
        }

        public static XmlLanguage parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(xis, XmlLanguage.type, (XmlOptions) null);
        }

        public static XmlLanguage parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(xis, XmlLanguage.type, options);
        }

        public static XmlLanguage parse(XMLStreamReader xsr) throws XmlException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(xsr, XmlLanguage.type, (XmlOptions) null);
        }

        public static XmlLanguage parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlLanguage) XmlBeans.getContextTypeLoader().parse(xsr, XmlLanguage.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlLanguage.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlLanguage.type, options);
        }

        private Factory() {
        }
    }
}
