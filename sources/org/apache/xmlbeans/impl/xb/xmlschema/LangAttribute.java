package org.apache.xmlbeans.impl.xb.xmlschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlLanguage;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlschema/LangAttribute.class */
public interface LangAttribute extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(LangAttribute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLLANG").resolveHandle("lange126attrtypetype");

    String getLang();

    XmlLanguage xgetLang();

    boolean isSetLang();

    void setLang(String str);

    void xsetLang(XmlLanguage xmlLanguage);

    void unsetLang();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlschema/LangAttribute$Factory.class */
    public static final class Factory {
        public static LangAttribute newInstance() {
            return (LangAttribute) XmlBeans.getContextTypeLoader().newInstance(LangAttribute.type, null);
        }

        public static LangAttribute newInstance(XmlOptions options) {
            return (LangAttribute) XmlBeans.getContextTypeLoader().newInstance(LangAttribute.type, options);
        }

        public static LangAttribute parse(String xmlAsString) throws XmlException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(xmlAsString, LangAttribute.type, (XmlOptions) null);
        }

        public static LangAttribute parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(xmlAsString, LangAttribute.type, options);
        }

        public static LangAttribute parse(File file) throws XmlException, IOException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(file, LangAttribute.type, (XmlOptions) null);
        }

        public static LangAttribute parse(File file, XmlOptions options) throws XmlException, IOException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(file, LangAttribute.type, options);
        }

        public static LangAttribute parse(URL u) throws XmlException, IOException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(u, LangAttribute.type, (XmlOptions) null);
        }

        public static LangAttribute parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(u, LangAttribute.type, options);
        }

        public static LangAttribute parse(InputStream is) throws XmlException, IOException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(is, LangAttribute.type, (XmlOptions) null);
        }

        public static LangAttribute parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(is, LangAttribute.type, options);
        }

        public static LangAttribute parse(Reader r) throws XmlException, IOException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(r, LangAttribute.type, (XmlOptions) null);
        }

        public static LangAttribute parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(r, LangAttribute.type, options);
        }

        public static LangAttribute parse(XMLStreamReader sr) throws XmlException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(sr, LangAttribute.type, (XmlOptions) null);
        }

        public static LangAttribute parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(sr, LangAttribute.type, options);
        }

        public static LangAttribute parse(Node node) throws XmlException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(node, LangAttribute.type, (XmlOptions) null);
        }

        public static LangAttribute parse(Node node, XmlOptions options) throws XmlException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(node, LangAttribute.type, options);
        }

        public static LangAttribute parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(xis, LangAttribute.type, (XmlOptions) null);
        }

        public static LangAttribute parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (LangAttribute) XmlBeans.getContextTypeLoader().parse(xis, LangAttribute.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, LangAttribute.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, LangAttribute.type, options);
        }

        private Factory() {
        }
    }
}
