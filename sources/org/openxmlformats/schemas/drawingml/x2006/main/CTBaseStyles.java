package org.openxmlformats.schemas.drawingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTBaseStyles.class */
public interface CTBaseStyles extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBaseStyles.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctbasestyles122etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTBaseStyles$Factory.class */
    public static final class Factory {
        public static CTBaseStyles newInstance() {
            return (CTBaseStyles) POIXMLTypeLoader.newInstance(CTBaseStyles.type, null);
        }

        public static CTBaseStyles newInstance(XmlOptions xmlOptions) {
            return (CTBaseStyles) POIXMLTypeLoader.newInstance(CTBaseStyles.type, xmlOptions);
        }

        public static CTBaseStyles parse(String str) throws XmlException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(str, CTBaseStyles.type, (XmlOptions) null);
        }

        public static CTBaseStyles parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(str, CTBaseStyles.type, xmlOptions);
        }

        public static CTBaseStyles parse(File file) throws XmlException, IOException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(file, CTBaseStyles.type, (XmlOptions) null);
        }

        public static CTBaseStyles parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(file, CTBaseStyles.type, xmlOptions);
        }

        public static CTBaseStyles parse(URL url) throws XmlException, IOException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(url, CTBaseStyles.type, (XmlOptions) null);
        }

        public static CTBaseStyles parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(url, CTBaseStyles.type, xmlOptions);
        }

        public static CTBaseStyles parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(inputStream, CTBaseStyles.type, (XmlOptions) null);
        }

        public static CTBaseStyles parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(inputStream, CTBaseStyles.type, xmlOptions);
        }

        public static CTBaseStyles parse(Reader reader) throws XmlException, IOException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(reader, CTBaseStyles.type, (XmlOptions) null);
        }

        public static CTBaseStyles parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(reader, CTBaseStyles.type, xmlOptions);
        }

        public static CTBaseStyles parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(xMLStreamReader, CTBaseStyles.type, (XmlOptions) null);
        }

        public static CTBaseStyles parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(xMLStreamReader, CTBaseStyles.type, xmlOptions);
        }

        public static CTBaseStyles parse(Node node) throws XmlException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(node, CTBaseStyles.type, (XmlOptions) null);
        }

        public static CTBaseStyles parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(node, CTBaseStyles.type, xmlOptions);
        }

        public static CTBaseStyles parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(xMLInputStream, CTBaseStyles.type, (XmlOptions) null);
        }

        public static CTBaseStyles parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBaseStyles) POIXMLTypeLoader.parse(xMLInputStream, CTBaseStyles.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBaseStyles.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBaseStyles.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTColorScheme getClrScheme();

    void setClrScheme(CTColorScheme cTColorScheme);

    CTColorScheme addNewClrScheme();

    CTFontScheme getFontScheme();

    void setFontScheme(CTFontScheme cTFontScheme);

    CTFontScheme addNewFontScheme();

    CTStyleMatrix getFmtScheme();

    void setFmtScheme(CTStyleMatrix cTStyleMatrix);

    CTStyleMatrix addNewFmtScheme();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();
}
