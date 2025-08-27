package org.apache.xmlbeans.impl.xb.xmlconfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Usertypeconfig.class */
public interface Usertypeconfig extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Usertypeconfig.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("usertypeconfig7bbatype");

    String getStaticHandler();

    XmlString xgetStaticHandler();

    void setStaticHandler(String str);

    void xsetStaticHandler(XmlString xmlString);

    QName getName();

    XmlQName xgetName();

    boolean isSetName();

    void setName(QName qName);

    void xsetName(XmlQName xmlQName);

    void unsetName();

    String getJavaname();

    XmlString xgetJavaname();

    boolean isSetJavaname();

    void setJavaname(String str);

    void xsetJavaname(XmlString xmlString);

    void unsetJavaname();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Usertypeconfig$Factory.class */
    public static final class Factory {
        public static Usertypeconfig newInstance() {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().newInstance(Usertypeconfig.type, null);
        }

        public static Usertypeconfig newInstance(XmlOptions options) {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().newInstance(Usertypeconfig.type, options);
        }

        public static Usertypeconfig parse(String xmlAsString) throws XmlException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(xmlAsString, Usertypeconfig.type, (XmlOptions) null);
        }

        public static Usertypeconfig parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(xmlAsString, Usertypeconfig.type, options);
        }

        public static Usertypeconfig parse(File file) throws XmlException, IOException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(file, Usertypeconfig.type, (XmlOptions) null);
        }

        public static Usertypeconfig parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(file, Usertypeconfig.type, options);
        }

        public static Usertypeconfig parse(URL u) throws XmlException, IOException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(u, Usertypeconfig.type, (XmlOptions) null);
        }

        public static Usertypeconfig parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(u, Usertypeconfig.type, options);
        }

        public static Usertypeconfig parse(InputStream is) throws XmlException, IOException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(is, Usertypeconfig.type, (XmlOptions) null);
        }

        public static Usertypeconfig parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(is, Usertypeconfig.type, options);
        }

        public static Usertypeconfig parse(Reader r) throws XmlException, IOException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(r, Usertypeconfig.type, (XmlOptions) null);
        }

        public static Usertypeconfig parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(r, Usertypeconfig.type, options);
        }

        public static Usertypeconfig parse(XMLStreamReader sr) throws XmlException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(sr, Usertypeconfig.type, (XmlOptions) null);
        }

        public static Usertypeconfig parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(sr, Usertypeconfig.type, options);
        }

        public static Usertypeconfig parse(Node node) throws XmlException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(node, Usertypeconfig.type, (XmlOptions) null);
        }

        public static Usertypeconfig parse(Node node, XmlOptions options) throws XmlException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(node, Usertypeconfig.type, options);
        }

        public static Usertypeconfig parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(xis, Usertypeconfig.type, (XmlOptions) null);
        }

        public static Usertypeconfig parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Usertypeconfig) XmlBeans.getContextTypeLoader().parse(xis, Usertypeconfig.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Usertypeconfig.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Usertypeconfig.type, options);
        }

        private Factory() {
        }
    }
}
