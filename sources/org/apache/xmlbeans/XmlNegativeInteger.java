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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlNegativeInteger.class */
public interface XmlNegativeInteger extends XmlNonPositiveInteger {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_negativeInteger");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlNegativeInteger$Factory.class */
    public static final class Factory {
        public static XmlNegativeInteger newInstance() {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().newInstance(XmlNegativeInteger.type, null);
        }

        public static XmlNegativeInteger newInstance(XmlOptions options) {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().newInstance(XmlNegativeInteger.type, options);
        }

        public static XmlNegativeInteger newValue(Object obj) {
            return (XmlNegativeInteger) XmlNegativeInteger.type.newValue(obj);
        }

        public static XmlNegativeInteger parse(String s) throws XmlException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(s, XmlNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNegativeInteger parse(String s, XmlOptions options) throws XmlException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(s, XmlNegativeInteger.type, options);
        }

        public static XmlNegativeInteger parse(File f) throws XmlException, IOException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(f, XmlNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNegativeInteger parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(f, XmlNegativeInteger.type, options);
        }

        public static XmlNegativeInteger parse(URL u) throws XmlException, IOException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(u, XmlNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNegativeInteger parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(u, XmlNegativeInteger.type, options);
        }

        public static XmlNegativeInteger parse(InputStream is) throws XmlException, IOException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(is, XmlNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNegativeInteger parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(is, XmlNegativeInteger.type, options);
        }

        public static XmlNegativeInteger parse(Reader r) throws XmlException, IOException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(r, XmlNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNegativeInteger parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(r, XmlNegativeInteger.type, options);
        }

        public static XmlNegativeInteger parse(Node node) throws XmlException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(node, XmlNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNegativeInteger parse(Node node, XmlOptions options) throws XmlException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(node, XmlNegativeInteger.type, options);
        }

        public static XmlNegativeInteger parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(xis, XmlNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNegativeInteger parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(xis, XmlNegativeInteger.type, options);
        }

        public static XmlNegativeInteger parse(XMLStreamReader xsr) throws XmlException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(xsr, XmlNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNegativeInteger parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlNegativeInteger) XmlBeans.getContextTypeLoader().parse(xsr, XmlNegativeInteger.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlNegativeInteger.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlNegativeInteger.type, options);
        }

        private Factory() {
        }
    }
}
