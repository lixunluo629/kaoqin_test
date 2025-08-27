package org.apache.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlENTITIES.class */
public interface XmlENTITIES extends XmlAnySimpleType {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_ENTITIES");

    List getListValue();

    List xgetListValue();

    void setListValue(List list);

    List listValue();

    List xlistValue();

    void set(List list);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlENTITIES$Factory.class */
    public static final class Factory {
        public static XmlENTITIES newInstance() {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().newInstance(XmlENTITIES.type, null);
        }

        public static XmlENTITIES newInstance(XmlOptions options) {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().newInstance(XmlENTITIES.type, options);
        }

        public static XmlENTITIES newValue(Object obj) {
            return (XmlENTITIES) XmlENTITIES.type.newValue(obj);
        }

        public static XmlENTITIES parse(String s) throws XmlException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(s, XmlENTITIES.type, (XmlOptions) null);
        }

        public static XmlENTITIES parse(String s, XmlOptions options) throws XmlException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(s, XmlENTITIES.type, options);
        }

        public static XmlENTITIES parse(File f) throws XmlException, IOException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(f, XmlENTITIES.type, (XmlOptions) null);
        }

        public static XmlENTITIES parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(f, XmlENTITIES.type, options);
        }

        public static XmlENTITIES parse(URL u) throws XmlException, IOException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(u, XmlENTITIES.type, (XmlOptions) null);
        }

        public static XmlENTITIES parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(u, XmlENTITIES.type, options);
        }

        public static XmlENTITIES parse(InputStream is) throws XmlException, IOException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(is, XmlENTITIES.type, (XmlOptions) null);
        }

        public static XmlENTITIES parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(is, XmlENTITIES.type, options);
        }

        public static XmlENTITIES parse(Reader r) throws XmlException, IOException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(r, XmlENTITIES.type, (XmlOptions) null);
        }

        public static XmlENTITIES parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(r, XmlENTITIES.type, options);
        }

        public static XmlENTITIES parse(Node node) throws XmlException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(node, XmlENTITIES.type, (XmlOptions) null);
        }

        public static XmlENTITIES parse(Node node, XmlOptions options) throws XmlException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(node, XmlENTITIES.type, options);
        }

        public static XmlENTITIES parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(xis, XmlENTITIES.type, (XmlOptions) null);
        }

        public static XmlENTITIES parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(xis, XmlENTITIES.type, options);
        }

        public static XmlENTITIES parse(XMLStreamReader xsr) throws XmlException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(xsr, XmlENTITIES.type, (XmlOptions) null);
        }

        public static XmlENTITIES parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlENTITIES) XmlBeans.getContextTypeLoader().parse(xsr, XmlENTITIES.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlENTITIES.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlENTITIES.type, options);
        }

        private Factory() {
        }
    }
}
