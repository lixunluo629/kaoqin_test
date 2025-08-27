package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/LocalElement.class */
public interface LocalElement extends Element {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(LocalElement.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("localelement2ce2type");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/LocalElement$Factory.class */
    public static final class Factory {
        public static LocalElement newInstance() {
            return (LocalElement) XmlBeans.getContextTypeLoader().newInstance(LocalElement.type, null);
        }

        public static LocalElement newInstance(XmlOptions options) {
            return (LocalElement) XmlBeans.getContextTypeLoader().newInstance(LocalElement.type, options);
        }

        public static LocalElement parse(String xmlAsString) throws XmlException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(xmlAsString, LocalElement.type, (XmlOptions) null);
        }

        public static LocalElement parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(xmlAsString, LocalElement.type, options);
        }

        public static LocalElement parse(File file) throws XmlException, IOException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(file, LocalElement.type, (XmlOptions) null);
        }

        public static LocalElement parse(File file, XmlOptions options) throws XmlException, IOException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(file, LocalElement.type, options);
        }

        public static LocalElement parse(URL u) throws XmlException, IOException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(u, LocalElement.type, (XmlOptions) null);
        }

        public static LocalElement parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(u, LocalElement.type, options);
        }

        public static LocalElement parse(InputStream is) throws XmlException, IOException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(is, LocalElement.type, (XmlOptions) null);
        }

        public static LocalElement parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(is, LocalElement.type, options);
        }

        public static LocalElement parse(Reader r) throws XmlException, IOException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(r, LocalElement.type, (XmlOptions) null);
        }

        public static LocalElement parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(r, LocalElement.type, options);
        }

        public static LocalElement parse(XMLStreamReader sr) throws XmlException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(sr, LocalElement.type, (XmlOptions) null);
        }

        public static LocalElement parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(sr, LocalElement.type, options);
        }

        public static LocalElement parse(Node node) throws XmlException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(node, LocalElement.type, (XmlOptions) null);
        }

        public static LocalElement parse(Node node, XmlOptions options) throws XmlException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(node, LocalElement.type, options);
        }

        public static LocalElement parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(xis, LocalElement.type, (XmlOptions) null);
        }

        public static LocalElement parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (LocalElement) XmlBeans.getContextTypeLoader().parse(xis, LocalElement.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, LocalElement.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, LocalElement.type, options);
        }

        private Factory() {
        }
    }
}
