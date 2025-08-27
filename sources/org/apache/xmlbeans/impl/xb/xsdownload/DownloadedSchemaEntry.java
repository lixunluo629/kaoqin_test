package org.apache.xmlbeans.impl.xb.xsdownload;

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
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdownload/DownloadedSchemaEntry.class */
public interface DownloadedSchemaEntry extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(DownloadedSchemaEntry.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLTOOLS").resolveHandle("downloadedschemaentry1c75type");

    String getFilename();

    XmlToken xgetFilename();

    void setFilename(String str);

    void xsetFilename(XmlToken xmlToken);

    String getSha1();

    XmlToken xgetSha1();

    void setSha1(String str);

    void xsetSha1(XmlToken xmlToken);

    String[] getSchemaLocationArray();

    String getSchemaLocationArray(int i);

    XmlAnyURI[] xgetSchemaLocationArray();

    XmlAnyURI xgetSchemaLocationArray(int i);

    int sizeOfSchemaLocationArray();

    void setSchemaLocationArray(String[] strArr);

    void setSchemaLocationArray(int i, String str);

    void xsetSchemaLocationArray(XmlAnyURI[] xmlAnyURIArr);

    void xsetSchemaLocationArray(int i, XmlAnyURI xmlAnyURI);

    void insertSchemaLocation(int i, String str);

    void addSchemaLocation(String str);

    XmlAnyURI insertNewSchemaLocation(int i);

    XmlAnyURI addNewSchemaLocation();

    void removeSchemaLocation(int i);

    String getNamespace();

    XmlAnyURI xgetNamespace();

    boolean isSetNamespace();

    void setNamespace(String str);

    void xsetNamespace(XmlAnyURI xmlAnyURI);

    void unsetNamespace();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdownload/DownloadedSchemaEntry$Factory.class */
    public static final class Factory {
        public static DownloadedSchemaEntry newInstance() {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().newInstance(DownloadedSchemaEntry.type, null);
        }

        public static DownloadedSchemaEntry newInstance(XmlOptions options) {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().newInstance(DownloadedSchemaEntry.type, options);
        }

        public static DownloadedSchemaEntry parse(String xmlAsString) throws XmlException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(xmlAsString, DownloadedSchemaEntry.type, (XmlOptions) null);
        }

        public static DownloadedSchemaEntry parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(xmlAsString, DownloadedSchemaEntry.type, options);
        }

        public static DownloadedSchemaEntry parse(File file) throws XmlException, IOException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(file, DownloadedSchemaEntry.type, (XmlOptions) null);
        }

        public static DownloadedSchemaEntry parse(File file, XmlOptions options) throws XmlException, IOException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(file, DownloadedSchemaEntry.type, options);
        }

        public static DownloadedSchemaEntry parse(URL u) throws XmlException, IOException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(u, DownloadedSchemaEntry.type, (XmlOptions) null);
        }

        public static DownloadedSchemaEntry parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(u, DownloadedSchemaEntry.type, options);
        }

        public static DownloadedSchemaEntry parse(InputStream is) throws XmlException, IOException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(is, DownloadedSchemaEntry.type, (XmlOptions) null);
        }

        public static DownloadedSchemaEntry parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(is, DownloadedSchemaEntry.type, options);
        }

        public static DownloadedSchemaEntry parse(Reader r) throws XmlException, IOException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(r, DownloadedSchemaEntry.type, (XmlOptions) null);
        }

        public static DownloadedSchemaEntry parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(r, DownloadedSchemaEntry.type, options);
        }

        public static DownloadedSchemaEntry parse(XMLStreamReader sr) throws XmlException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(sr, DownloadedSchemaEntry.type, (XmlOptions) null);
        }

        public static DownloadedSchemaEntry parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(sr, DownloadedSchemaEntry.type, options);
        }

        public static DownloadedSchemaEntry parse(Node node) throws XmlException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(node, DownloadedSchemaEntry.type, (XmlOptions) null);
        }

        public static DownloadedSchemaEntry parse(Node node, XmlOptions options) throws XmlException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(node, DownloadedSchemaEntry.type, options);
        }

        public static DownloadedSchemaEntry parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(xis, DownloadedSchemaEntry.type, (XmlOptions) null);
        }

        public static DownloadedSchemaEntry parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (DownloadedSchemaEntry) XmlBeans.getContextTypeLoader().parse(xis, DownloadedSchemaEntry.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DownloadedSchemaEntry.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DownloadedSchemaEntry.type, options);
        }

        private Factory() {
        }
    }
}
