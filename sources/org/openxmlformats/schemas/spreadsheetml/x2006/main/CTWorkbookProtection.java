package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTWorkbookProtection.class */
public interface CTWorkbookProtection extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTWorkbookProtection.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctworkbookprotection56bctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTWorkbookProtection$Factory.class */
    public static final class Factory {
        public static CTWorkbookProtection newInstance() {
            return (CTWorkbookProtection) POIXMLTypeLoader.newInstance(CTWorkbookProtection.type, null);
        }

        public static CTWorkbookProtection newInstance(XmlOptions xmlOptions) {
            return (CTWorkbookProtection) POIXMLTypeLoader.newInstance(CTWorkbookProtection.type, xmlOptions);
        }

        public static CTWorkbookProtection parse(String str) throws XmlException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(str, CTWorkbookProtection.type, (XmlOptions) null);
        }

        public static CTWorkbookProtection parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(str, CTWorkbookProtection.type, xmlOptions);
        }

        public static CTWorkbookProtection parse(File file) throws XmlException, IOException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(file, CTWorkbookProtection.type, (XmlOptions) null);
        }

        public static CTWorkbookProtection parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(file, CTWorkbookProtection.type, xmlOptions);
        }

        public static CTWorkbookProtection parse(URL url) throws XmlException, IOException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(url, CTWorkbookProtection.type, (XmlOptions) null);
        }

        public static CTWorkbookProtection parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(url, CTWorkbookProtection.type, xmlOptions);
        }

        public static CTWorkbookProtection parse(InputStream inputStream) throws XmlException, IOException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(inputStream, CTWorkbookProtection.type, (XmlOptions) null);
        }

        public static CTWorkbookProtection parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(inputStream, CTWorkbookProtection.type, xmlOptions);
        }

        public static CTWorkbookProtection parse(Reader reader) throws XmlException, IOException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(reader, CTWorkbookProtection.type, (XmlOptions) null);
        }

        public static CTWorkbookProtection parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(reader, CTWorkbookProtection.type, xmlOptions);
        }

        public static CTWorkbookProtection parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(xMLStreamReader, CTWorkbookProtection.type, (XmlOptions) null);
        }

        public static CTWorkbookProtection parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(xMLStreamReader, CTWorkbookProtection.type, xmlOptions);
        }

        public static CTWorkbookProtection parse(Node node) throws XmlException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(node, CTWorkbookProtection.type, (XmlOptions) null);
        }

        public static CTWorkbookProtection parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(node, CTWorkbookProtection.type, xmlOptions);
        }

        public static CTWorkbookProtection parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(xMLInputStream, CTWorkbookProtection.type, (XmlOptions) null);
        }

        public static CTWorkbookProtection parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTWorkbookProtection) POIXMLTypeLoader.parse(xMLInputStream, CTWorkbookProtection.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTWorkbookProtection.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTWorkbookProtection.type, xmlOptions);
        }

        private Factory() {
        }
    }

    byte[] getWorkbookPassword();

    STUnsignedShortHex xgetWorkbookPassword();

    boolean isSetWorkbookPassword();

    void setWorkbookPassword(byte[] bArr);

    void xsetWorkbookPassword(STUnsignedShortHex sTUnsignedShortHex);

    void unsetWorkbookPassword();

    byte[] getRevisionsPassword();

    STUnsignedShortHex xgetRevisionsPassword();

    boolean isSetRevisionsPassword();

    void setRevisionsPassword(byte[] bArr);

    void xsetRevisionsPassword(STUnsignedShortHex sTUnsignedShortHex);

    void unsetRevisionsPassword();

    boolean getLockStructure();

    XmlBoolean xgetLockStructure();

    boolean isSetLockStructure();

    void setLockStructure(boolean z);

    void xsetLockStructure(XmlBoolean xmlBoolean);

    void unsetLockStructure();

    boolean getLockWindows();

    XmlBoolean xgetLockWindows();

    boolean isSetLockWindows();

    void setLockWindows(boolean z);

    void xsetLockWindows(XmlBoolean xmlBoolean);

    void unsetLockWindows();

    boolean getLockRevision();

    XmlBoolean xgetLockRevision();

    boolean isSetLockRevision();

    void setLockRevision(boolean z);

    void xsetLockRevision(XmlBoolean xmlBoolean);

    void unsetLockRevision();
}
