package org.apache.xmlbeans.impl.xb.xmlconfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Nsconfig.class */
public interface Nsconfig extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Nsconfig.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("nsconfigaebatype");

    String getPackage();

    XmlString xgetPackage();

    boolean isSetPackage();

    void setPackage(String str);

    void xsetPackage(XmlString xmlString);

    void unsetPackage();

    String getPrefix();

    XmlString xgetPrefix();

    boolean isSetPrefix();

    void setPrefix(String str);

    void xsetPrefix(XmlString xmlString);

    void unsetPrefix();

    String getSuffix();

    XmlString xgetSuffix();

    boolean isSetSuffix();

    void setSuffix(String str);

    void xsetSuffix(XmlString xmlString);

    void unsetSuffix();

    Object getUri();

    NamespaceList xgetUri();

    boolean isSetUri();

    void setUri(Object obj);

    void xsetUri(NamespaceList namespaceList);

    void unsetUri();

    List getUriprefix();

    NamespacePrefixList xgetUriprefix();

    boolean isSetUriprefix();

    void setUriprefix(List list);

    void xsetUriprefix(NamespacePrefixList namespacePrefixList);

    void unsetUriprefix();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Nsconfig$Factory.class */
    public static final class Factory {
        public static Nsconfig newInstance() {
            return (Nsconfig) XmlBeans.getContextTypeLoader().newInstance(Nsconfig.type, null);
        }

        public static Nsconfig newInstance(XmlOptions options) {
            return (Nsconfig) XmlBeans.getContextTypeLoader().newInstance(Nsconfig.type, options);
        }

        public static Nsconfig parse(String xmlAsString) throws XmlException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(xmlAsString, Nsconfig.type, (XmlOptions) null);
        }

        public static Nsconfig parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(xmlAsString, Nsconfig.type, options);
        }

        public static Nsconfig parse(File file) throws XmlException, IOException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(file, Nsconfig.type, (XmlOptions) null);
        }

        public static Nsconfig parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(file, Nsconfig.type, options);
        }

        public static Nsconfig parse(URL u) throws XmlException, IOException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(u, Nsconfig.type, (XmlOptions) null);
        }

        public static Nsconfig parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(u, Nsconfig.type, options);
        }

        public static Nsconfig parse(InputStream is) throws XmlException, IOException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(is, Nsconfig.type, (XmlOptions) null);
        }

        public static Nsconfig parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(is, Nsconfig.type, options);
        }

        public static Nsconfig parse(Reader r) throws XmlException, IOException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(r, Nsconfig.type, (XmlOptions) null);
        }

        public static Nsconfig parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(r, Nsconfig.type, options);
        }

        public static Nsconfig parse(XMLStreamReader sr) throws XmlException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(sr, Nsconfig.type, (XmlOptions) null);
        }

        public static Nsconfig parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(sr, Nsconfig.type, options);
        }

        public static Nsconfig parse(Node node) throws XmlException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(node, Nsconfig.type, (XmlOptions) null);
        }

        public static Nsconfig parse(Node node, XmlOptions options) throws XmlException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(node, Nsconfig.type, options);
        }

        public static Nsconfig parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(xis, Nsconfig.type, (XmlOptions) null);
        }

        public static Nsconfig parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Nsconfig) XmlBeans.getContextTypeLoader().parse(xis, Nsconfig.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Nsconfig.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Nsconfig.type, options);
        }

        private Factory() {
        }
    }
}
