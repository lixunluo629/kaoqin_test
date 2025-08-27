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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlID.class */
public interface XmlID extends XmlNCName {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_ID");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlID$Factory.class */
    public static final class Factory {
        public static XmlID newInstance() {
            return (XmlID) XmlBeans.getContextTypeLoader().newInstance(XmlID.type, null);
        }

        public static XmlID newInstance(XmlOptions options) {
            return (XmlID) XmlBeans.getContextTypeLoader().newInstance(XmlID.type, options);
        }

        public static XmlID newValue(Object obj) {
            return (XmlID) XmlID.type.newValue(obj);
        }

        public static XmlID parse(String s) throws XmlException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(s, XmlID.type, (XmlOptions) null);
        }

        public static XmlID parse(String s, XmlOptions options) throws XmlException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(s, XmlID.type, options);
        }

        public static XmlID parse(File f) throws XmlException, IOException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(f, XmlID.type, (XmlOptions) null);
        }

        public static XmlID parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(f, XmlID.type, options);
        }

        public static XmlID parse(URL u) throws XmlException, IOException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(u, XmlID.type, (XmlOptions) null);
        }

        public static XmlID parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(u, XmlID.type, options);
        }

        public static XmlID parse(InputStream is) throws XmlException, IOException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(is, XmlID.type, (XmlOptions) null);
        }

        public static XmlID parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(is, XmlID.type, options);
        }

        public static XmlID parse(Reader r) throws XmlException, IOException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(r, XmlID.type, (XmlOptions) null);
        }

        public static XmlID parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(r, XmlID.type, options);
        }

        public static XmlID parse(Node node) throws XmlException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(node, XmlID.type, (XmlOptions) null);
        }

        public static XmlID parse(Node node, XmlOptions options) throws XmlException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(node, XmlID.type, options);
        }

        public static XmlID parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(xis, XmlID.type, (XmlOptions) null);
        }

        public static XmlID parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(xis, XmlID.type, options);
        }

        public static XmlID parse(XMLStreamReader xsr) throws XmlException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(xsr, XmlID.type, (XmlOptions) null);
        }

        public static XmlID parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlID) XmlBeans.getContextTypeLoader().parse(xsr, XmlID.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlID.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlID.type, options);
        }

        private Factory() {
        }
    }
}
