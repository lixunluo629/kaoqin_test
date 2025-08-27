package org.apache.xmlbeans.impl.xb.xsdownload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdownload/DownloadedSchemasDocument.class */
public interface DownloadedSchemasDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(DownloadedSchemasDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLTOOLS").resolveHandle("downloadedschemas2dd7doctype");

    DownloadedSchemas getDownloadedSchemas();

    void setDownloadedSchemas(DownloadedSchemas downloadedSchemas);

    DownloadedSchemas addNewDownloadedSchemas();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdownload/DownloadedSchemasDocument$DownloadedSchemas.class */
    public interface DownloadedSchemas extends XmlObject {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(DownloadedSchemas.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLTOOLS").resolveHandle("downloadedschemasb3efelemtype");

        DownloadedSchemaEntry[] getEntryArray();

        DownloadedSchemaEntry getEntryArray(int i);

        int sizeOfEntryArray();

        void setEntryArray(DownloadedSchemaEntry[] downloadedSchemaEntryArr);

        void setEntryArray(int i, DownloadedSchemaEntry downloadedSchemaEntry);

        DownloadedSchemaEntry insertNewEntry(int i);

        DownloadedSchemaEntry addNewEntry();

        void removeEntry(int i);

        String getDefaultDirectory();

        XmlToken xgetDefaultDirectory();

        boolean isSetDefaultDirectory();

        void setDefaultDirectory(String str);

        void xsetDefaultDirectory(XmlToken xmlToken);

        void unsetDefaultDirectory();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdownload/DownloadedSchemasDocument$DownloadedSchemas$Factory.class */
        public static final class Factory {
            public static DownloadedSchemas newInstance() {
                return (DownloadedSchemas) XmlBeans.getContextTypeLoader().newInstance(DownloadedSchemas.type, null);
            }

            public static DownloadedSchemas newInstance(XmlOptions options) {
                return (DownloadedSchemas) XmlBeans.getContextTypeLoader().newInstance(DownloadedSchemas.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdownload/DownloadedSchemasDocument$Factory.class */
    public static final class Factory {
        public static DownloadedSchemasDocument newInstance() {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().newInstance(DownloadedSchemasDocument.type, null);
        }

        public static DownloadedSchemasDocument newInstance(XmlOptions options) {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().newInstance(DownloadedSchemasDocument.type, options);
        }

        public static DownloadedSchemasDocument parse(String xmlAsString) throws XmlException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, DownloadedSchemasDocument.type, (XmlOptions) null);
        }

        public static DownloadedSchemasDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, DownloadedSchemasDocument.type, options);
        }

        public static DownloadedSchemasDocument parse(File file) throws XmlException, IOException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(file, DownloadedSchemasDocument.type, (XmlOptions) null);
        }

        public static DownloadedSchemasDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(file, DownloadedSchemasDocument.type, options);
        }

        public static DownloadedSchemasDocument parse(URL u) throws XmlException, IOException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(u, DownloadedSchemasDocument.type, (XmlOptions) null);
        }

        public static DownloadedSchemasDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(u, DownloadedSchemasDocument.type, options);
        }

        public static DownloadedSchemasDocument parse(InputStream is) throws XmlException, IOException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(is, DownloadedSchemasDocument.type, (XmlOptions) null);
        }

        public static DownloadedSchemasDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(is, DownloadedSchemasDocument.type, options);
        }

        public static DownloadedSchemasDocument parse(Reader r) throws XmlException, IOException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(r, DownloadedSchemasDocument.type, (XmlOptions) null);
        }

        public static DownloadedSchemasDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(r, DownloadedSchemasDocument.type, options);
        }

        public static DownloadedSchemasDocument parse(XMLStreamReader sr) throws XmlException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(sr, DownloadedSchemasDocument.type, (XmlOptions) null);
        }

        public static DownloadedSchemasDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(sr, DownloadedSchemasDocument.type, options);
        }

        public static DownloadedSchemasDocument parse(Node node) throws XmlException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(node, DownloadedSchemasDocument.type, (XmlOptions) null);
        }

        public static DownloadedSchemasDocument parse(Node node, XmlOptions options) throws XmlException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(node, DownloadedSchemasDocument.type, options);
        }

        public static DownloadedSchemasDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(xis, DownloadedSchemasDocument.type, (XmlOptions) null);
        }

        public static DownloadedSchemasDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (DownloadedSchemasDocument) XmlBeans.getContextTypeLoader().parse(xis, DownloadedSchemasDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DownloadedSchemasDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DownloadedSchemasDocument.type, options);
        }

        private Factory() {
        }
    }
}
