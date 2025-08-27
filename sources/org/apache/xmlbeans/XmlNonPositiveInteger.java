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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlNonPositiveInteger.class */
public interface XmlNonPositiveInteger extends XmlInteger {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_nonPositiveInteger");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlNonPositiveInteger$Factory.class */
    public static final class Factory {
        public static XmlNonPositiveInteger newInstance() {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().newInstance(XmlNonPositiveInteger.type, null);
        }

        public static XmlNonPositiveInteger newInstance(XmlOptions options) {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().newInstance(XmlNonPositiveInteger.type, options);
        }

        public static XmlNonPositiveInteger newValue(Object obj) {
            return (XmlNonPositiveInteger) XmlNonPositiveInteger.type.newValue(obj);
        }

        public static XmlNonPositiveInteger parse(String s) throws XmlException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(s, XmlNonPositiveInteger.type, (XmlOptions) null);
        }

        public static XmlNonPositiveInteger parse(String s, XmlOptions options) throws XmlException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(s, XmlNonPositiveInteger.type, options);
        }

        public static XmlNonPositiveInteger parse(File f) throws XmlException, IOException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(f, XmlNonPositiveInteger.type, (XmlOptions) null);
        }

        public static XmlNonPositiveInteger parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(f, XmlNonPositiveInteger.type, options);
        }

        public static XmlNonPositiveInteger parse(URL u) throws XmlException, IOException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(u, XmlNonPositiveInteger.type, (XmlOptions) null);
        }

        public static XmlNonPositiveInteger parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(u, XmlNonPositiveInteger.type, options);
        }

        public static XmlNonPositiveInteger parse(InputStream is) throws XmlException, IOException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(is, XmlNonPositiveInteger.type, (XmlOptions) null);
        }

        public static XmlNonPositiveInteger parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(is, XmlNonPositiveInteger.type, options);
        }

        public static XmlNonPositiveInteger parse(Reader r) throws XmlException, IOException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(r, XmlNonPositiveInteger.type, (XmlOptions) null);
        }

        public static XmlNonPositiveInteger parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(r, XmlNonPositiveInteger.type, options);
        }

        public static XmlNonPositiveInteger parse(Node node) throws XmlException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(node, XmlNonPositiveInteger.type, (XmlOptions) null);
        }

        public static XmlNonPositiveInteger parse(Node node, XmlOptions options) throws XmlException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(node, XmlNonPositiveInteger.type, options);
        }

        public static XmlNonPositiveInteger parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(xis, XmlNonPositiveInteger.type, (XmlOptions) null);
        }

        public static XmlNonPositiveInteger parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(xis, XmlNonPositiveInteger.type, options);
        }

        public static XmlNonPositiveInteger parse(XMLStreamReader xsr) throws XmlException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(xsr, XmlNonPositiveInteger.type, (XmlOptions) null);
        }

        public static XmlNonPositiveInteger parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlNonPositiveInteger) XmlBeans.getContextTypeLoader().parse(xsr, XmlNonPositiveInteger.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlNonPositiveInteger.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlNonPositiveInteger.type, options);
        }

        private Factory() {
        }
    }
}
