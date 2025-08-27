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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlIDREF.class */
public interface XmlIDREF extends XmlNCName {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_IDREF");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlIDREF$Factory.class */
    public static final class Factory {
        public static XmlIDREF newInstance() {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().newInstance(XmlIDREF.type, null);
        }

        public static XmlIDREF newInstance(XmlOptions options) {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().newInstance(XmlIDREF.type, options);
        }

        public static XmlIDREF newValue(Object obj) {
            return (XmlIDREF) XmlIDREF.type.newValue(obj);
        }

        public static XmlIDREF parse(String s) throws XmlException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(s, XmlIDREF.type, (XmlOptions) null);
        }

        public static XmlIDREF parse(String s, XmlOptions options) throws XmlException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(s, XmlIDREF.type, options);
        }

        public static XmlIDREF parse(File f) throws XmlException, IOException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(f, XmlIDREF.type, (XmlOptions) null);
        }

        public static XmlIDREF parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(f, XmlIDREF.type, options);
        }

        public static XmlIDREF parse(URL u) throws XmlException, IOException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(u, XmlIDREF.type, (XmlOptions) null);
        }

        public static XmlIDREF parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(u, XmlIDREF.type, options);
        }

        public static XmlIDREF parse(InputStream is) throws XmlException, IOException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(is, XmlIDREF.type, (XmlOptions) null);
        }

        public static XmlIDREF parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(is, XmlIDREF.type, options);
        }

        public static XmlIDREF parse(Reader r) throws XmlException, IOException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(r, XmlIDREF.type, (XmlOptions) null);
        }

        public static XmlIDREF parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(r, XmlIDREF.type, options);
        }

        public static XmlIDREF parse(Node node) throws XmlException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(node, XmlIDREF.type, (XmlOptions) null);
        }

        public static XmlIDREF parse(Node node, XmlOptions options) throws XmlException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(node, XmlIDREF.type, options);
        }

        public static XmlIDREF parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(xis, XmlIDREF.type, (XmlOptions) null);
        }

        public static XmlIDREF parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(xis, XmlIDREF.type, options);
        }

        public static XmlIDREF parse(XMLStreamReader xsr) throws XmlException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(xsr, XmlIDREF.type, (XmlOptions) null);
        }

        public static XmlIDREF parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlIDREF) XmlBeans.getContextTypeLoader().parse(xsr, XmlIDREF.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlIDREF.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlIDREF.type, options);
        }

        private Factory() {
        }
    }
}
