package org.apache.xmlbeans.impl.xb.xmlconfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Qnameconfig.class */
public interface Qnameconfig extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Qnameconfig.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("qnameconfig463ftype");

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

    List getTarget();

    Qnametargetlist xgetTarget();

    boolean isSetTarget();

    void setTarget(List list);

    void xsetTarget(Qnametargetlist qnametargetlist);

    void unsetTarget();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/Qnameconfig$Factory.class */
    public static final class Factory {
        public static Qnameconfig newInstance() {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().newInstance(Qnameconfig.type, null);
        }

        public static Qnameconfig newInstance(XmlOptions options) {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().newInstance(Qnameconfig.type, options);
        }

        public static Qnameconfig parse(String xmlAsString) throws XmlException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(xmlAsString, Qnameconfig.type, (XmlOptions) null);
        }

        public static Qnameconfig parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(xmlAsString, Qnameconfig.type, options);
        }

        public static Qnameconfig parse(File file) throws XmlException, IOException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(file, Qnameconfig.type, (XmlOptions) null);
        }

        public static Qnameconfig parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(file, Qnameconfig.type, options);
        }

        public static Qnameconfig parse(URL u) throws XmlException, IOException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(u, Qnameconfig.type, (XmlOptions) null);
        }

        public static Qnameconfig parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(u, Qnameconfig.type, options);
        }

        public static Qnameconfig parse(InputStream is) throws XmlException, IOException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(is, Qnameconfig.type, (XmlOptions) null);
        }

        public static Qnameconfig parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(is, Qnameconfig.type, options);
        }

        public static Qnameconfig parse(Reader r) throws XmlException, IOException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(r, Qnameconfig.type, (XmlOptions) null);
        }

        public static Qnameconfig parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(r, Qnameconfig.type, options);
        }

        public static Qnameconfig parse(XMLStreamReader sr) throws XmlException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(sr, Qnameconfig.type, (XmlOptions) null);
        }

        public static Qnameconfig parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(sr, Qnameconfig.type, options);
        }

        public static Qnameconfig parse(Node node) throws XmlException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(node, Qnameconfig.type, (XmlOptions) null);
        }

        public static Qnameconfig parse(Node node, XmlOptions options) throws XmlException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(node, Qnameconfig.type, options);
        }

        public static Qnameconfig parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(xis, Qnameconfig.type, (XmlOptions) null);
        }

        public static Qnameconfig parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Qnameconfig) XmlBeans.getContextTypeLoader().parse(xis, Qnameconfig.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Qnameconfig.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Qnameconfig.type, options);
        }

        private Factory() {
        }
    }
}
