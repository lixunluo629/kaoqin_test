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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlNMTOKEN.class */
public interface XmlNMTOKEN extends XmlToken {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_NMTOKEN");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlNMTOKEN$Factory.class */
    public static final class Factory {
        public static XmlNMTOKEN newInstance() {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().newInstance(XmlNMTOKEN.type, null);
        }

        public static XmlNMTOKEN newInstance(XmlOptions options) {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().newInstance(XmlNMTOKEN.type, options);
        }

        public static XmlNMTOKEN newValue(Object obj) {
            return (XmlNMTOKEN) XmlNMTOKEN.type.newValue(obj);
        }

        public static XmlNMTOKEN parse(String s) throws XmlException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(s, XmlNMTOKEN.type, (XmlOptions) null);
        }

        public static XmlNMTOKEN parse(String s, XmlOptions options) throws XmlException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(s, XmlNMTOKEN.type, options);
        }

        public static XmlNMTOKEN parse(File f) throws XmlException, IOException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(f, XmlNMTOKEN.type, (XmlOptions) null);
        }

        public static XmlNMTOKEN parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(f, XmlNMTOKEN.type, options);
        }

        public static XmlNMTOKEN parse(URL u) throws XmlException, IOException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(u, XmlNMTOKEN.type, (XmlOptions) null);
        }

        public static XmlNMTOKEN parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(u, XmlNMTOKEN.type, options);
        }

        public static XmlNMTOKEN parse(InputStream is) throws XmlException, IOException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(is, XmlNMTOKEN.type, (XmlOptions) null);
        }

        public static XmlNMTOKEN parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(is, XmlNMTOKEN.type, options);
        }

        public static XmlNMTOKEN parse(Reader r) throws XmlException, IOException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(r, XmlNMTOKEN.type, (XmlOptions) null);
        }

        public static XmlNMTOKEN parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(r, XmlNMTOKEN.type, options);
        }

        public static XmlNMTOKEN parse(Node node) throws XmlException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(node, XmlNMTOKEN.type, (XmlOptions) null);
        }

        public static XmlNMTOKEN parse(Node node, XmlOptions options) throws XmlException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(node, XmlNMTOKEN.type, options);
        }

        public static XmlNMTOKEN parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(xis, XmlNMTOKEN.type, (XmlOptions) null);
        }

        public static XmlNMTOKEN parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(xis, XmlNMTOKEN.type, options);
        }

        public static XmlNMTOKEN parse(XMLStreamReader xsr) throws XmlException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(xsr, XmlNMTOKEN.type, (XmlOptions) null);
        }

        public static XmlNMTOKEN parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlNMTOKEN) XmlBeans.getContextTypeLoader().parse(xsr, XmlNMTOKEN.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlNMTOKEN.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlNMTOKEN.type, options);
        }

        private Factory() {
        }
    }
}
