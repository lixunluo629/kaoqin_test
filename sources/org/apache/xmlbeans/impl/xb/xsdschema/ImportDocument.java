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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ImportDocument.class */
public interface ImportDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ImportDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("import99fedoctype");

    Import getImport();

    void setImport(Import r1);

    Import addNewImport();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ImportDocument$Import.class */
    public interface Import extends Annotated {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Import.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("importe2ffelemtype");

        String getNamespace();

        XmlAnyURI xgetNamespace();

        boolean isSetNamespace();

        void setNamespace(String str);

        void xsetNamespace(XmlAnyURI xmlAnyURI);

        void unsetNamespace();

        String getSchemaLocation();

        XmlAnyURI xgetSchemaLocation();

        boolean isSetSchemaLocation();

        void setSchemaLocation(String str);

        void xsetSchemaLocation(XmlAnyURI xmlAnyURI);

        void unsetSchemaLocation();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ImportDocument$Import$Factory.class */
        public static final class Factory {
            public static Import newInstance() {
                return (Import) XmlBeans.getContextTypeLoader().newInstance(Import.type, null);
            }

            public static Import newInstance(XmlOptions options) {
                return (Import) XmlBeans.getContextTypeLoader().newInstance(Import.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ImportDocument$Factory.class */
    public static final class Factory {
        public static ImportDocument newInstance() {
            return (ImportDocument) XmlBeans.getContextTypeLoader().newInstance(ImportDocument.type, null);
        }

        public static ImportDocument newInstance(XmlOptions options) {
            return (ImportDocument) XmlBeans.getContextTypeLoader().newInstance(ImportDocument.type, options);
        }

        public static ImportDocument parse(String xmlAsString) throws XmlException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ImportDocument.type, (XmlOptions) null);
        }

        public static ImportDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ImportDocument.type, options);
        }

        public static ImportDocument parse(File file) throws XmlException, IOException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(file, ImportDocument.type, (XmlOptions) null);
        }

        public static ImportDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(file, ImportDocument.type, options);
        }

        public static ImportDocument parse(URL u) throws XmlException, IOException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(u, ImportDocument.type, (XmlOptions) null);
        }

        public static ImportDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(u, ImportDocument.type, options);
        }

        public static ImportDocument parse(InputStream is) throws XmlException, IOException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(is, ImportDocument.type, (XmlOptions) null);
        }

        public static ImportDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(is, ImportDocument.type, options);
        }

        public static ImportDocument parse(Reader r) throws XmlException, IOException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(r, ImportDocument.type, (XmlOptions) null);
        }

        public static ImportDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(r, ImportDocument.type, options);
        }

        public static ImportDocument parse(XMLStreamReader sr) throws XmlException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(sr, ImportDocument.type, (XmlOptions) null);
        }

        public static ImportDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(sr, ImportDocument.type, options);
        }

        public static ImportDocument parse(Node node) throws XmlException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(node, ImportDocument.type, (XmlOptions) null);
        }

        public static ImportDocument parse(Node node, XmlOptions options) throws XmlException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(node, ImportDocument.type, options);
        }

        public static ImportDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(xis, ImportDocument.type, (XmlOptions) null);
        }

        public static ImportDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (ImportDocument) XmlBeans.getContextTypeLoader().parse(xis, ImportDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ImportDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ImportDocument.type, options);
        }

        private Factory() {
        }
    }
}
