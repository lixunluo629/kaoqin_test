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
import org.apache.xmlbeans.XmlLanguage;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DocumentationDocument.class */
public interface DocumentationDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(DocumentationDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("documentation6cdbdoctype");

    Documentation getDocumentation();

    void setDocumentation(Documentation documentation);

    Documentation addNewDocumentation();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DocumentationDocument$Documentation.class */
    public interface Documentation extends XmlObject {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Documentation.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("documentationa475elemtype");

        String getSource();

        XmlAnyURI xgetSource();

        boolean isSetSource();

        void setSource(String str);

        void xsetSource(XmlAnyURI xmlAnyURI);

        void unsetSource();

        String getLang();

        XmlLanguage xgetLang();

        boolean isSetLang();

        void setLang(String str);

        void xsetLang(XmlLanguage xmlLanguage);

        void unsetLang();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DocumentationDocument$Documentation$Factory.class */
        public static final class Factory {
            public static Documentation newInstance() {
                return (Documentation) XmlBeans.getContextTypeLoader().newInstance(Documentation.type, null);
            }

            public static Documentation newInstance(XmlOptions options) {
                return (Documentation) XmlBeans.getContextTypeLoader().newInstance(Documentation.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DocumentationDocument$Factory.class */
    public static final class Factory {
        public static DocumentationDocument newInstance() {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().newInstance(DocumentationDocument.type, null);
        }

        public static DocumentationDocument newInstance(XmlOptions options) {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().newInstance(DocumentationDocument.type, options);
        }

        public static DocumentationDocument parse(String xmlAsString) throws XmlException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, DocumentationDocument.type, (XmlOptions) null);
        }

        public static DocumentationDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, DocumentationDocument.type, options);
        }

        public static DocumentationDocument parse(File file) throws XmlException, IOException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(file, DocumentationDocument.type, (XmlOptions) null);
        }

        public static DocumentationDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(file, DocumentationDocument.type, options);
        }

        public static DocumentationDocument parse(URL u) throws XmlException, IOException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(u, DocumentationDocument.type, (XmlOptions) null);
        }

        public static DocumentationDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(u, DocumentationDocument.type, options);
        }

        public static DocumentationDocument parse(InputStream is) throws XmlException, IOException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(is, DocumentationDocument.type, (XmlOptions) null);
        }

        public static DocumentationDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(is, DocumentationDocument.type, options);
        }

        public static DocumentationDocument parse(Reader r) throws XmlException, IOException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(r, DocumentationDocument.type, (XmlOptions) null);
        }

        public static DocumentationDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(r, DocumentationDocument.type, options);
        }

        public static DocumentationDocument parse(XMLStreamReader sr) throws XmlException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(sr, DocumentationDocument.type, (XmlOptions) null);
        }

        public static DocumentationDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(sr, DocumentationDocument.type, options);
        }

        public static DocumentationDocument parse(Node node) throws XmlException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(node, DocumentationDocument.type, (XmlOptions) null);
        }

        public static DocumentationDocument parse(Node node, XmlOptions options) throws XmlException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(node, DocumentationDocument.type, options);
        }

        public static DocumentationDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(xis, DocumentationDocument.type, (XmlOptions) null);
        }

        public static DocumentationDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (DocumentationDocument) XmlBeans.getContextTypeLoader().parse(xis, DocumentationDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DocumentationDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DocumentationDocument.type, options);
        }

        private Factory() {
        }
    }
}
