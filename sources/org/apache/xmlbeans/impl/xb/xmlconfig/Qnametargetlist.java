package org.apache.xmlbeans.impl.xb.xmlconfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Qnametargetlist.class */
public interface Qnametargetlist extends XmlAnySimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Qnametargetlist.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("qnametargetlist16actype");

    List getListValue();

    List xgetListValue();

    void setListValue(List list);

    List listValue();

    List xlistValue();

    void set(List list);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Qnametargetlist$Factory.class */
    public static final class Factory {
        public static Qnametargetlist newValue(Object obj) {
            return (Qnametargetlist) Qnametargetlist.type.newValue(obj);
        }

        public static Qnametargetlist newInstance() {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().newInstance(Qnametargetlist.type, null);
        }

        public static Qnametargetlist newInstance(XmlOptions options) {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().newInstance(Qnametargetlist.type, options);
        }

        public static Qnametargetlist parse(String xmlAsString) throws XmlException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(xmlAsString, Qnametargetlist.type, (XmlOptions) null);
        }

        public static Qnametargetlist parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(xmlAsString, Qnametargetlist.type, options);
        }

        public static Qnametargetlist parse(File file) throws XmlException, IOException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(file, Qnametargetlist.type, (XmlOptions) null);
        }

        public static Qnametargetlist parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(file, Qnametargetlist.type, options);
        }

        public static Qnametargetlist parse(URL u) throws XmlException, IOException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(u, Qnametargetlist.type, (XmlOptions) null);
        }

        public static Qnametargetlist parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(u, Qnametargetlist.type, options);
        }

        public static Qnametargetlist parse(InputStream is) throws XmlException, IOException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(is, Qnametargetlist.type, (XmlOptions) null);
        }

        public static Qnametargetlist parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(is, Qnametargetlist.type, options);
        }

        public static Qnametargetlist parse(Reader r) throws XmlException, IOException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(r, Qnametargetlist.type, (XmlOptions) null);
        }

        public static Qnametargetlist parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(r, Qnametargetlist.type, options);
        }

        public static Qnametargetlist parse(XMLStreamReader sr) throws XmlException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(sr, Qnametargetlist.type, (XmlOptions) null);
        }

        public static Qnametargetlist parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(sr, Qnametargetlist.type, options);
        }

        public static Qnametargetlist parse(Node node) throws XmlException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(node, Qnametargetlist.type, (XmlOptions) null);
        }

        public static Qnametargetlist parse(Node node, XmlOptions options) throws XmlException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(node, Qnametargetlist.type, options);
        }

        public static Qnametargetlist parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(xis, Qnametargetlist.type, (XmlOptions) null);
        }

        public static Qnametargetlist parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Qnametargetlist) XmlBeans.getContextTypeLoader().parse(xis, Qnametargetlist.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Qnametargetlist.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Qnametargetlist.type, options);
        }

        private Factory() {
        }
    }
}
