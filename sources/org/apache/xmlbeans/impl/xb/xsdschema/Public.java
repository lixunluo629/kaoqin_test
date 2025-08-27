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
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Public.class */
public interface Public extends XmlToken {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Public.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("publicf3catype");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Public$Factory.class */
    public static final class Factory {
        public static Public newValue(Object obj) {
            return (Public) Public.type.newValue(obj);
        }

        public static Public newInstance() {
            return (Public) XmlBeans.getContextTypeLoader().newInstance(Public.type, null);
        }

        public static Public newInstance(XmlOptions options) {
            return (Public) XmlBeans.getContextTypeLoader().newInstance(Public.type, options);
        }

        public static Public parse(String xmlAsString) throws XmlException {
            return (Public) XmlBeans.getContextTypeLoader().parse(xmlAsString, Public.type, (XmlOptions) null);
        }

        public static Public parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Public) XmlBeans.getContextTypeLoader().parse(xmlAsString, Public.type, options);
        }

        public static Public parse(File file) throws XmlException, IOException {
            return (Public) XmlBeans.getContextTypeLoader().parse(file, Public.type, (XmlOptions) null);
        }

        public static Public parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Public) XmlBeans.getContextTypeLoader().parse(file, Public.type, options);
        }

        public static Public parse(URL u) throws XmlException, IOException {
            return (Public) XmlBeans.getContextTypeLoader().parse(u, Public.type, (XmlOptions) null);
        }

        public static Public parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Public) XmlBeans.getContextTypeLoader().parse(u, Public.type, options);
        }

        public static Public parse(InputStream is) throws XmlException, IOException {
            return (Public) XmlBeans.getContextTypeLoader().parse(is, Public.type, (XmlOptions) null);
        }

        public static Public parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Public) XmlBeans.getContextTypeLoader().parse(is, Public.type, options);
        }

        public static Public parse(Reader r) throws XmlException, IOException {
            return (Public) XmlBeans.getContextTypeLoader().parse(r, Public.type, (XmlOptions) null);
        }

        public static Public parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Public) XmlBeans.getContextTypeLoader().parse(r, Public.type, options);
        }

        public static Public parse(XMLStreamReader sr) throws XmlException {
            return (Public) XmlBeans.getContextTypeLoader().parse(sr, Public.type, (XmlOptions) null);
        }

        public static Public parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Public) XmlBeans.getContextTypeLoader().parse(sr, Public.type, options);
        }

        public static Public parse(Node node) throws XmlException {
            return (Public) XmlBeans.getContextTypeLoader().parse(node, Public.type, (XmlOptions) null);
        }

        public static Public parse(Node node, XmlOptions options) throws XmlException {
            return (Public) XmlBeans.getContextTypeLoader().parse(node, Public.type, options);
        }

        public static Public parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Public) XmlBeans.getContextTypeLoader().parse(xis, Public.type, (XmlOptions) null);
        }

        public static Public parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Public) XmlBeans.getContextTypeLoader().parse(xis, Public.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Public.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Public.type, options);
        }

        private Factory() {
        }
    }
}
