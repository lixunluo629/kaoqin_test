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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespacePrefixList.class */
public interface NamespacePrefixList extends XmlAnySimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(NamespacePrefixList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("namespaceprefixlistec0ctype");

    List getListValue();

    List xgetListValue();

    void setListValue(List list);

    List listValue();

    List xlistValue();

    void set(List list);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespacePrefixList$Factory.class */
    public static final class Factory {
        public static NamespacePrefixList newValue(Object obj) {
            return (NamespacePrefixList) NamespacePrefixList.type.newValue(obj);
        }

        public static NamespacePrefixList newInstance() {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().newInstance(NamespacePrefixList.type, null);
        }

        public static NamespacePrefixList newInstance(XmlOptions options) {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().newInstance(NamespacePrefixList.type, options);
        }

        public static NamespacePrefixList parse(String xmlAsString) throws XmlException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(xmlAsString, NamespacePrefixList.type, (XmlOptions) null);
        }

        public static NamespacePrefixList parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(xmlAsString, NamespacePrefixList.type, options);
        }

        public static NamespacePrefixList parse(File file) throws XmlException, IOException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(file, NamespacePrefixList.type, (XmlOptions) null);
        }

        public static NamespacePrefixList parse(File file, XmlOptions options) throws XmlException, IOException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(file, NamespacePrefixList.type, options);
        }

        public static NamespacePrefixList parse(URL u) throws XmlException, IOException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(u, NamespacePrefixList.type, (XmlOptions) null);
        }

        public static NamespacePrefixList parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(u, NamespacePrefixList.type, options);
        }

        public static NamespacePrefixList parse(InputStream is) throws XmlException, IOException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(is, NamespacePrefixList.type, (XmlOptions) null);
        }

        public static NamespacePrefixList parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(is, NamespacePrefixList.type, options);
        }

        public static NamespacePrefixList parse(Reader r) throws XmlException, IOException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(r, NamespacePrefixList.type, (XmlOptions) null);
        }

        public static NamespacePrefixList parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(r, NamespacePrefixList.type, options);
        }

        public static NamespacePrefixList parse(XMLStreamReader sr) throws XmlException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(sr, NamespacePrefixList.type, (XmlOptions) null);
        }

        public static NamespacePrefixList parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(sr, NamespacePrefixList.type, options);
        }

        public static NamespacePrefixList parse(Node node) throws XmlException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(node, NamespacePrefixList.type, (XmlOptions) null);
        }

        public static NamespacePrefixList parse(Node node, XmlOptions options) throws XmlException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(node, NamespacePrefixList.type, options);
        }

        public static NamespacePrefixList parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(xis, NamespacePrefixList.type, (XmlOptions) null);
        }

        public static NamespacePrefixList parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (NamespacePrefixList) XmlBeans.getContextTypeLoader().parse(xis, NamespacePrefixList.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NamespacePrefixList.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NamespacePrefixList.type, options);
        }

        private Factory() {
        }
    }
}
