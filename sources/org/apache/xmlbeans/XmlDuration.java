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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlDuration.class */
public interface XmlDuration extends XmlAnySimpleType {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_duration");

    GDuration getGDurationValue();

    void setGDurationValue(GDuration gDuration);

    GDuration gDurationValue();

    void set(GDurationSpecification gDurationSpecification);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlDuration$Factory.class */
    public static final class Factory {
        public static XmlDuration newInstance() {
            return (XmlDuration) XmlBeans.getContextTypeLoader().newInstance(XmlDuration.type, null);
        }

        public static XmlDuration newInstance(XmlOptions options) {
            return (XmlDuration) XmlBeans.getContextTypeLoader().newInstance(XmlDuration.type, options);
        }

        public static XmlDuration newValue(Object obj) {
            return (XmlDuration) XmlDuration.type.newValue(obj);
        }

        public static XmlDuration parse(String s) throws XmlException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(s, XmlDuration.type, (XmlOptions) null);
        }

        public static XmlDuration parse(String s, XmlOptions options) throws XmlException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(s, XmlDuration.type, options);
        }

        public static XmlDuration parse(File f) throws XmlException, IOException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(f, XmlDuration.type, (XmlOptions) null);
        }

        public static XmlDuration parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(f, XmlDuration.type, options);
        }

        public static XmlDuration parse(URL u) throws XmlException, IOException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(u, XmlDuration.type, (XmlOptions) null);
        }

        public static XmlDuration parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(u, XmlDuration.type, options);
        }

        public static XmlDuration parse(InputStream is) throws XmlException, IOException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(is, XmlDuration.type, (XmlOptions) null);
        }

        public static XmlDuration parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(is, XmlDuration.type, options);
        }

        public static XmlDuration parse(Reader r) throws XmlException, IOException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(r, XmlDuration.type, (XmlOptions) null);
        }

        public static XmlDuration parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(r, XmlDuration.type, options);
        }

        public static XmlDuration parse(Node node) throws XmlException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(node, XmlDuration.type, (XmlOptions) null);
        }

        public static XmlDuration parse(Node node, XmlOptions options) throws XmlException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(node, XmlDuration.type, options);
        }

        public static XmlDuration parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(xis, XmlDuration.type, (XmlOptions) null);
        }

        public static XmlDuration parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(xis, XmlDuration.type, options);
        }

        public static XmlDuration parse(XMLStreamReader xsr) throws XmlException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(xsr, XmlDuration.type, (XmlOptions) null);
        }

        public static XmlDuration parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlDuration) XmlBeans.getContextTypeLoader().parse(xsr, XmlDuration.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlDuration.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlDuration.type, options);
        }

        private Factory() {
        }
    }
}
