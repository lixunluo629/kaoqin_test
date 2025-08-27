package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AppinfoDocument.class */
public interface AppinfoDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(AppinfoDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("appinfo2ea6doctype");

    Appinfo getAppinfo();

    void setAppinfo(Appinfo appinfo);

    Appinfo addNewAppinfo();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AppinfoDocument$Appinfo.class */
    public interface Appinfo extends XmlObject {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Appinfo.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("appinfo650belemtype");

        String getSource();

        XmlAnyURI xgetSource();

        boolean isSetSource();

        void setSource(String str);

        void xsetSource(XmlAnyURI xmlAnyURI);

        void unsetSource();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AppinfoDocument$Appinfo$Factory.class */
        public static final class Factory {
            public static Appinfo newInstance() {
                return (Appinfo) XmlBeans.getContextTypeLoader().newInstance(Appinfo.type, null);
            }

            public static Appinfo newInstance(XmlOptions options) {
                return (Appinfo) XmlBeans.getContextTypeLoader().newInstance(Appinfo.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AppinfoDocument$Factory.class */
    public static final class Factory {
        public static AppinfoDocument newInstance() {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().newInstance(AppinfoDocument.type, null);
        }

        public static AppinfoDocument newInstance(XmlOptions options) {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().newInstance(AppinfoDocument.type, options);
        }

        public static AppinfoDocument parse(String xmlAsString) throws XmlException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, AppinfoDocument.type, (XmlOptions) null);
        }

        public static AppinfoDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, AppinfoDocument.type, options);
        }

        public static AppinfoDocument parse(File file) throws XmlException, IOException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(file, AppinfoDocument.type, (XmlOptions) null);
        }

        public static AppinfoDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(file, AppinfoDocument.type, options);
        }

        public static AppinfoDocument parse(URL u) throws XmlException, IOException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(u, AppinfoDocument.type, (XmlOptions) null);
        }

        public static AppinfoDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(u, AppinfoDocument.type, options);
        }

        public static AppinfoDocument parse(InputStream is) throws XmlException, IOException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(is, AppinfoDocument.type, (XmlOptions) null);
        }

        public static AppinfoDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(is, AppinfoDocument.type, options);
        }

        public static AppinfoDocument parse(Reader r) throws XmlException, IOException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(r, AppinfoDocument.type, (XmlOptions) null);
        }

        public static AppinfoDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(r, AppinfoDocument.type, options);
        }

        public static AppinfoDocument parse(XMLStreamReader sr) throws XmlException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(sr, AppinfoDocument.type, (XmlOptions) null);
        }

        public static AppinfoDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(sr, AppinfoDocument.type, options);
        }

        public static AppinfoDocument parse(Node node) throws XmlException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(node, AppinfoDocument.type, (XmlOptions) null);
        }

        public static AppinfoDocument parse(Node node, XmlOptions options) throws XmlException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(node, AppinfoDocument.type, options);
        }

        public static AppinfoDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(xis, AppinfoDocument.type, (XmlOptions) null);
        }

        public static AppinfoDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (AppinfoDocument) XmlBeans.getContextTypeLoader().parse(xis, AppinfoDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AppinfoDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AppinfoDocument.type, options);
        }

        private Factory() {
        }
    }
}
