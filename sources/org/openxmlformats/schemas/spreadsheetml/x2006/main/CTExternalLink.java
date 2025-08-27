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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTExternalLink.class */
public interface CTExternalLink extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTExternalLink.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctexternallink966etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTExternalLink$Factory.class */
    public static final class Factory {
        public static CTExternalLink newInstance() {
            return (CTExternalLink) POIXMLTypeLoader.newInstance(CTExternalLink.type, null);
        }

        public static CTExternalLink newInstance(XmlOptions xmlOptions) {
            return (CTExternalLink) POIXMLTypeLoader.newInstance(CTExternalLink.type, xmlOptions);
        }

        public static CTExternalLink parse(String str) throws XmlException {
            return (CTExternalLink) POIXMLTypeLoader.parse(str, CTExternalLink.type, (XmlOptions) null);
        }

        public static CTExternalLink parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTExternalLink) POIXMLTypeLoader.parse(str, CTExternalLink.type, xmlOptions);
        }

        public static CTExternalLink parse(File file) throws XmlException, IOException {
            return (CTExternalLink) POIXMLTypeLoader.parse(file, CTExternalLink.type, (XmlOptions) null);
        }

        public static CTExternalLink parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTExternalLink) POIXMLTypeLoader.parse(file, CTExternalLink.type, xmlOptions);
        }

        public static CTExternalLink parse(URL url) throws XmlException, IOException {
            return (CTExternalLink) POIXMLTypeLoader.parse(url, CTExternalLink.type, (XmlOptions) null);
        }

        public static CTExternalLink parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTExternalLink) POIXMLTypeLoader.parse(url, CTExternalLink.type, xmlOptions);
        }

        public static CTExternalLink parse(InputStream inputStream) throws XmlException, IOException {
            return (CTExternalLink) POIXMLTypeLoader.parse(inputStream, CTExternalLink.type, (XmlOptions) null);
        }

        public static CTExternalLink parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTExternalLink) POIXMLTypeLoader.parse(inputStream, CTExternalLink.type, xmlOptions);
        }

        public static CTExternalLink parse(Reader reader) throws XmlException, IOException {
            return (CTExternalLink) POIXMLTypeLoader.parse(reader, CTExternalLink.type, (XmlOptions) null);
        }

        public static CTExternalLink parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTExternalLink) POIXMLTypeLoader.parse(reader, CTExternalLink.type, xmlOptions);
        }

        public static CTExternalLink parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTExternalLink) POIXMLTypeLoader.parse(xMLStreamReader, CTExternalLink.type, (XmlOptions) null);
        }

        public static CTExternalLink parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTExternalLink) POIXMLTypeLoader.parse(xMLStreamReader, CTExternalLink.type, xmlOptions);
        }

        public static CTExternalLink parse(Node node) throws XmlException {
            return (CTExternalLink) POIXMLTypeLoader.parse(node, CTExternalLink.type, (XmlOptions) null);
        }

        public static CTExternalLink parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTExternalLink) POIXMLTypeLoader.parse(node, CTExternalLink.type, xmlOptions);
        }

        public static CTExternalLink parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTExternalLink) POIXMLTypeLoader.parse(xMLInputStream, CTExternalLink.type, (XmlOptions) null);
        }

        public static CTExternalLink parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTExternalLink) POIXMLTypeLoader.parse(xMLInputStream, CTExternalLink.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTExternalLink.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTExternalLink.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTExternalBook getExternalBook();

    boolean isSetExternalBook();

    void setExternalBook(CTExternalBook cTExternalBook);

    CTExternalBook addNewExternalBook();

    void unsetExternalBook();

    CTDdeLink getDdeLink();

    boolean isSetDdeLink();

    void setDdeLink(CTDdeLink cTDdeLink);

    CTDdeLink addNewDdeLink();

    void unsetDdeLink();

    CTOleLink getOleLink();

    boolean isSetOleLink();

    void setOleLink(CTOleLink cTOleLink);

    CTOleLink addNewOleLink();

    void unsetOleLink();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
