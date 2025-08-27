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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlName.class */
public interface XmlName extends XmlToken {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_Name");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlName$Factory.class */
    public static final class Factory {
        public static XmlName newInstance() {
            return (XmlName) XmlBeans.getContextTypeLoader().newInstance(XmlName.type, null);
        }

        public static XmlName newInstance(XmlOptions options) {
            return (XmlName) XmlBeans.getContextTypeLoader().newInstance(XmlName.type, options);
        }

        public static XmlName newValue(Object obj) {
            return (XmlName) XmlName.type.newValue(obj);
        }

        public static XmlName parse(String s) throws XmlException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(s, XmlName.type, (XmlOptions) null);
        }

        public static XmlName parse(String s, XmlOptions options) throws XmlException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(s, XmlName.type, options);
        }

        public static XmlName parse(File f) throws XmlException, IOException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(f, XmlName.type, (XmlOptions) null);
        }

        public static XmlName parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(f, XmlName.type, options);
        }

        public static XmlName parse(URL u) throws XmlException, IOException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(u, XmlName.type, (XmlOptions) null);
        }

        public static XmlName parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(u, XmlName.type, options);
        }

        public static XmlName parse(InputStream is) throws XmlException, IOException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(is, XmlName.type, (XmlOptions) null);
        }

        public static XmlName parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(is, XmlName.type, options);
        }

        public static XmlName parse(Reader r) throws XmlException, IOException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(r, XmlName.type, (XmlOptions) null);
        }

        public static XmlName parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(r, XmlName.type, options);
        }

        public static XmlName parse(Node node) throws XmlException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(node, XmlName.type, (XmlOptions) null);
        }

        public static XmlName parse(Node node, XmlOptions options) throws XmlException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(node, XmlName.type, options);
        }

        public static XmlName parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(xis, XmlName.type, (XmlOptions) null);
        }

        public static XmlName parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(xis, XmlName.type, options);
        }

        public static XmlName parse(XMLStreamReader xsr) throws XmlException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(xsr, XmlName.type, (XmlOptions) null);
        }

        public static XmlName parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlName) XmlBeans.getContextTypeLoader().parse(xsr, XmlName.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlName.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlName.type, options);
        }

        private Factory() {
        }
    }
}
