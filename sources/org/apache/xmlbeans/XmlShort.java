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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlShort.class */
public interface XmlShort extends XmlInt {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_short");

    short getShortValue();

    void setShortValue(short s);

    short shortValue();

    void set(short s);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlShort$Factory.class */
    public static final class Factory {
        public static XmlShort newInstance() {
            return (XmlShort) XmlBeans.getContextTypeLoader().newInstance(XmlShort.type, null);
        }

        public static XmlShort newInstance(XmlOptions options) {
            return (XmlShort) XmlBeans.getContextTypeLoader().newInstance(XmlShort.type, options);
        }

        public static XmlShort newValue(Object obj) {
            return (XmlShort) XmlShort.type.newValue(obj);
        }

        public static XmlShort parse(String s) throws XmlException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(s, XmlShort.type, (XmlOptions) null);
        }

        public static XmlShort parse(String s, XmlOptions options) throws XmlException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(s, XmlShort.type, options);
        }

        public static XmlShort parse(File f) throws XmlException, IOException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(f, XmlShort.type, (XmlOptions) null);
        }

        public static XmlShort parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(f, XmlShort.type, options);
        }

        public static XmlShort parse(URL u) throws XmlException, IOException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(u, XmlShort.type, (XmlOptions) null);
        }

        public static XmlShort parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(u, XmlShort.type, options);
        }

        public static XmlShort parse(InputStream is) throws XmlException, IOException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(is, XmlShort.type, (XmlOptions) null);
        }

        public static XmlShort parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(is, XmlShort.type, options);
        }

        public static XmlShort parse(Reader r) throws XmlException, IOException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(r, XmlShort.type, (XmlOptions) null);
        }

        public static XmlShort parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(r, XmlShort.type, options);
        }

        public static XmlShort parse(Node node) throws XmlException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(node, XmlShort.type, (XmlOptions) null);
        }

        public static XmlShort parse(Node node, XmlOptions options) throws XmlException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(node, XmlShort.type, options);
        }

        public static XmlShort parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(xis, XmlShort.type, (XmlOptions) null);
        }

        public static XmlShort parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(xis, XmlShort.type, options);
        }

        public static XmlShort parse(XMLStreamReader xsr) throws XmlException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(xsr, XmlShort.type, (XmlOptions) null);
        }

        public static XmlShort parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlShort) XmlBeans.getContextTypeLoader().parse(xsr, XmlShort.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlShort.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlShort.type, options);
        }

        private Factory() {
        }
    }
}
