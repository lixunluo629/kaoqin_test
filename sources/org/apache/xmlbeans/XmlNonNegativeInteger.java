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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlNonNegativeInteger.class */
public interface XmlNonNegativeInteger extends XmlInteger {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_nonNegativeInteger");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlNonNegativeInteger$Factory.class */
    public static final class Factory {
        public static XmlNonNegativeInteger newInstance() {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().newInstance(XmlNonNegativeInteger.type, null);
        }

        public static XmlNonNegativeInteger newInstance(XmlOptions options) {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().newInstance(XmlNonNegativeInteger.type, options);
        }

        public static XmlNonNegativeInteger newValue(Object obj) {
            return (XmlNonNegativeInteger) XmlNonNegativeInteger.type.newValue(obj);
        }

        public static XmlNonNegativeInteger parse(String s) throws XmlException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(s, XmlNonNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNonNegativeInteger parse(String s, XmlOptions options) throws XmlException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(s, XmlNonNegativeInteger.type, options);
        }

        public static XmlNonNegativeInteger parse(File f) throws XmlException, IOException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(f, XmlNonNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNonNegativeInteger parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(f, XmlNonNegativeInteger.type, options);
        }

        public static XmlNonNegativeInteger parse(URL u) throws XmlException, IOException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(u, XmlNonNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNonNegativeInteger parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(u, XmlNonNegativeInteger.type, options);
        }

        public static XmlNonNegativeInteger parse(InputStream is) throws XmlException, IOException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(is, XmlNonNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNonNegativeInteger parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(is, XmlNonNegativeInteger.type, options);
        }

        public static XmlNonNegativeInteger parse(Reader r) throws XmlException, IOException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(r, XmlNonNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNonNegativeInteger parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(r, XmlNonNegativeInteger.type, options);
        }

        public static XmlNonNegativeInteger parse(Node node) throws XmlException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(node, XmlNonNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNonNegativeInteger parse(Node node, XmlOptions options) throws XmlException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(node, XmlNonNegativeInteger.type, options);
        }

        public static XmlNonNegativeInteger parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(xis, XmlNonNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNonNegativeInteger parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(xis, XmlNonNegativeInteger.type, options);
        }

        public static XmlNonNegativeInteger parse(XMLStreamReader xsr) throws XmlException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(xsr, XmlNonNegativeInteger.type, (XmlOptions) null);
        }

        public static XmlNonNegativeInteger parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlNonNegativeInteger) XmlBeans.getContextTypeLoader().parse(xsr, XmlNonNegativeInteger.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlNonNegativeInteger.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlNonNegativeInteger.type, options);
        }

        private Factory() {
        }
    }
}
