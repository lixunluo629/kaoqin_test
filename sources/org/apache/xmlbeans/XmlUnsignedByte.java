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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlUnsignedByte.class */
public interface XmlUnsignedByte extends XmlUnsignedShort {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_unsignedByte");

    short getShortValue();

    void setShortValue(short s);

    short shortValue();

    void set(short s);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlUnsignedByte$Factory.class */
    public static final class Factory {
        public static XmlUnsignedByte newInstance() {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().newInstance(XmlUnsignedByte.type, null);
        }

        public static XmlUnsignedByte newInstance(XmlOptions options) {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().newInstance(XmlUnsignedByte.type, options);
        }

        public static XmlUnsignedByte newValue(Object obj) {
            return (XmlUnsignedByte) XmlUnsignedByte.type.newValue(obj);
        }

        public static XmlUnsignedByte parse(String s) throws XmlException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(s, XmlUnsignedByte.type, (XmlOptions) null);
        }

        public static XmlUnsignedByte parse(String s, XmlOptions options) throws XmlException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(s, XmlUnsignedByte.type, options);
        }

        public static XmlUnsignedByte parse(File f) throws XmlException, IOException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(f, XmlUnsignedByte.type, (XmlOptions) null);
        }

        public static XmlUnsignedByte parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(f, XmlUnsignedByte.type, options);
        }

        public static XmlUnsignedByte parse(URL u) throws XmlException, IOException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(u, XmlUnsignedByte.type, (XmlOptions) null);
        }

        public static XmlUnsignedByte parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(u, XmlUnsignedByte.type, options);
        }

        public static XmlUnsignedByte parse(InputStream is) throws XmlException, IOException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(is, XmlUnsignedByte.type, (XmlOptions) null);
        }

        public static XmlUnsignedByte parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(is, XmlUnsignedByte.type, options);
        }

        public static XmlUnsignedByte parse(Reader r) throws XmlException, IOException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(r, XmlUnsignedByte.type, (XmlOptions) null);
        }

        public static XmlUnsignedByte parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(r, XmlUnsignedByte.type, options);
        }

        public static XmlUnsignedByte parse(Node node) throws XmlException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(node, XmlUnsignedByte.type, (XmlOptions) null);
        }

        public static XmlUnsignedByte parse(Node node, XmlOptions options) throws XmlException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(node, XmlUnsignedByte.type, options);
        }

        public static XmlUnsignedByte parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(xis, XmlUnsignedByte.type, (XmlOptions) null);
        }

        public static XmlUnsignedByte parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(xis, XmlUnsignedByte.type, options);
        }

        public static XmlUnsignedByte parse(XMLStreamReader xsr) throws XmlException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(xsr, XmlUnsignedByte.type, (XmlOptions) null);
        }

        public static XmlUnsignedByte parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlUnsignedByte) XmlBeans.getContextTypeLoader().parse(xsr, XmlUnsignedByte.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlUnsignedByte.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlUnsignedByte.type, options);
        }

        private Factory() {
        }
    }
}
