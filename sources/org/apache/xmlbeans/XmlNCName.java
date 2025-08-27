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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlNCName.class */
public interface XmlNCName extends XmlName {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_NCName");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlNCName$Factory.class */
    public static final class Factory {
        public static XmlNCName newInstance() {
            return (XmlNCName) XmlBeans.getContextTypeLoader().newInstance(XmlNCName.type, null);
        }

        public static XmlNCName newInstance(XmlOptions options) {
            return (XmlNCName) XmlBeans.getContextTypeLoader().newInstance(XmlNCName.type, options);
        }

        public static XmlNCName newValue(Object obj) {
            return (XmlNCName) XmlNCName.type.newValue(obj);
        }

        public static XmlNCName parse(String s) throws XmlException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(s, XmlNCName.type, (XmlOptions) null);
        }

        public static XmlNCName parse(String s, XmlOptions options) throws XmlException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(s, XmlNCName.type, options);
        }

        public static XmlNCName parse(File f) throws XmlException, IOException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(f, XmlNCName.type, (XmlOptions) null);
        }

        public static XmlNCName parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(f, XmlNCName.type, options);
        }

        public static XmlNCName parse(URL u) throws XmlException, IOException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(u, XmlNCName.type, (XmlOptions) null);
        }

        public static XmlNCName parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(u, XmlNCName.type, options);
        }

        public static XmlNCName parse(InputStream is) throws XmlException, IOException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(is, XmlNCName.type, (XmlOptions) null);
        }

        public static XmlNCName parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(is, XmlNCName.type, options);
        }

        public static XmlNCName parse(Reader r) throws XmlException, IOException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(r, XmlNCName.type, (XmlOptions) null);
        }

        public static XmlNCName parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(r, XmlNCName.type, options);
        }

        public static XmlNCName parse(Node node) throws XmlException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(node, XmlNCName.type, (XmlOptions) null);
        }

        public static XmlNCName parse(Node node, XmlOptions options) throws XmlException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(node, XmlNCName.type, options);
        }

        public static XmlNCName parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(xis, XmlNCName.type, (XmlOptions) null);
        }

        public static XmlNCName parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(xis, XmlNCName.type, options);
        }

        public static XmlNCName parse(XMLStreamReader xsr) throws XmlException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(xsr, XmlNCName.type, (XmlOptions) null);
        }

        public static XmlNCName parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlNCName) XmlBeans.getContextTypeLoader().parse(xsr, XmlNCName.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlNCName.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlNCName.type, options);
        }

        private Factory() {
        }
    }
}
