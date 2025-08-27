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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlHexBinary.class */
public interface XmlHexBinary extends XmlAnySimpleType {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_hexBinary");

    byte[] byteArrayValue();

    void set(byte[] bArr);

    byte[] getByteArrayValue();

    void setByteArrayValue(byte[] bArr);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlHexBinary$Factory.class */
    public static final class Factory {
        public static XmlHexBinary newInstance() {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().newInstance(XmlHexBinary.type, null);
        }

        public static XmlHexBinary newInstance(XmlOptions options) {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().newInstance(XmlHexBinary.type, options);
        }

        public static XmlHexBinary newValue(Object obj) {
            return (XmlHexBinary) XmlHexBinary.type.newValue(obj);
        }

        public static XmlHexBinary parse(String s) throws XmlException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(s, XmlHexBinary.type, (XmlOptions) null);
        }

        public static XmlHexBinary parse(String s, XmlOptions options) throws XmlException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(s, XmlHexBinary.type, options);
        }

        public static XmlHexBinary parse(File f) throws XmlException, IOException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(f, XmlHexBinary.type, (XmlOptions) null);
        }

        public static XmlHexBinary parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(f, XmlHexBinary.type, options);
        }

        public static XmlHexBinary parse(URL u) throws XmlException, IOException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(u, XmlHexBinary.type, (XmlOptions) null);
        }

        public static XmlHexBinary parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(u, XmlHexBinary.type, options);
        }

        public static XmlHexBinary parse(InputStream is) throws XmlException, IOException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(is, XmlHexBinary.type, (XmlOptions) null);
        }

        public static XmlHexBinary parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(is, XmlHexBinary.type, options);
        }

        public static XmlHexBinary parse(Reader r) throws XmlException, IOException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(r, XmlHexBinary.type, (XmlOptions) null);
        }

        public static XmlHexBinary parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(r, XmlHexBinary.type, options);
        }

        public static XmlHexBinary parse(Node node) throws XmlException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(node, XmlHexBinary.type, (XmlOptions) null);
        }

        public static XmlHexBinary parse(Node node, XmlOptions options) throws XmlException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(node, XmlHexBinary.type, options);
        }

        public static XmlHexBinary parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(xis, XmlHexBinary.type, (XmlOptions) null);
        }

        public static XmlHexBinary parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(xis, XmlHexBinary.type, options);
        }

        public static XmlHexBinary parse(XMLStreamReader xsr) throws XmlException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(xsr, XmlHexBinary.type, (XmlOptions) null);
        }

        public static XmlHexBinary parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlHexBinary) XmlBeans.getContextTypeLoader().parse(xsr, XmlHexBinary.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlHexBinary.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlHexBinary.type, options);
        }

        private Factory() {
        }
    }
}
