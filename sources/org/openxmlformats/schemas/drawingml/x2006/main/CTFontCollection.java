package org.openxmlformats.schemas.drawingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTFontCollection.class */
public interface CTFontCollection extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTFontCollection.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctfontcollectiondd68type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTFontCollection$Factory.class */
    public static final class Factory {
        public static CTFontCollection newInstance() {
            return (CTFontCollection) POIXMLTypeLoader.newInstance(CTFontCollection.type, null);
        }

        public static CTFontCollection newInstance(XmlOptions xmlOptions) {
            return (CTFontCollection) POIXMLTypeLoader.newInstance(CTFontCollection.type, xmlOptions);
        }

        public static CTFontCollection parse(String str) throws XmlException {
            return (CTFontCollection) POIXMLTypeLoader.parse(str, CTFontCollection.type, (XmlOptions) null);
        }

        public static CTFontCollection parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTFontCollection) POIXMLTypeLoader.parse(str, CTFontCollection.type, xmlOptions);
        }

        public static CTFontCollection parse(File file) throws XmlException, IOException {
            return (CTFontCollection) POIXMLTypeLoader.parse(file, CTFontCollection.type, (XmlOptions) null);
        }

        public static CTFontCollection parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontCollection) POIXMLTypeLoader.parse(file, CTFontCollection.type, xmlOptions);
        }

        public static CTFontCollection parse(URL url) throws XmlException, IOException {
            return (CTFontCollection) POIXMLTypeLoader.parse(url, CTFontCollection.type, (XmlOptions) null);
        }

        public static CTFontCollection parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontCollection) POIXMLTypeLoader.parse(url, CTFontCollection.type, xmlOptions);
        }

        public static CTFontCollection parse(InputStream inputStream) throws XmlException, IOException {
            return (CTFontCollection) POIXMLTypeLoader.parse(inputStream, CTFontCollection.type, (XmlOptions) null);
        }

        public static CTFontCollection parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontCollection) POIXMLTypeLoader.parse(inputStream, CTFontCollection.type, xmlOptions);
        }

        public static CTFontCollection parse(Reader reader) throws XmlException, IOException {
            return (CTFontCollection) POIXMLTypeLoader.parse(reader, CTFontCollection.type, (XmlOptions) null);
        }

        public static CTFontCollection parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFontCollection) POIXMLTypeLoader.parse(reader, CTFontCollection.type, xmlOptions);
        }

        public static CTFontCollection parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTFontCollection) POIXMLTypeLoader.parse(xMLStreamReader, CTFontCollection.type, (XmlOptions) null);
        }

        public static CTFontCollection parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTFontCollection) POIXMLTypeLoader.parse(xMLStreamReader, CTFontCollection.type, xmlOptions);
        }

        public static CTFontCollection parse(Node node) throws XmlException {
            return (CTFontCollection) POIXMLTypeLoader.parse(node, CTFontCollection.type, (XmlOptions) null);
        }

        public static CTFontCollection parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTFontCollection) POIXMLTypeLoader.parse(node, CTFontCollection.type, xmlOptions);
        }

        public static CTFontCollection parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTFontCollection) POIXMLTypeLoader.parse(xMLInputStream, CTFontCollection.type, (XmlOptions) null);
        }

        public static CTFontCollection parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTFontCollection) POIXMLTypeLoader.parse(xMLInputStream, CTFontCollection.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFontCollection.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFontCollection.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTextFont getLatin();

    void setLatin(CTTextFont cTTextFont);

    CTTextFont addNewLatin();

    CTTextFont getEa();

    void setEa(CTTextFont cTTextFont);

    CTTextFont addNewEa();

    CTTextFont getCs();

    void setCs(CTTextFont cTTextFont);

    CTTextFont addNewCs();

    List<CTSupplementalFont> getFontList();

    CTSupplementalFont[] getFontArray();

    CTSupplementalFont getFontArray(int i);

    int sizeOfFontArray();

    void setFontArray(CTSupplementalFont[] cTSupplementalFontArr);

    void setFontArray(int i, CTSupplementalFont cTSupplementalFont);

    CTSupplementalFont insertNewFont(int i);

    CTSupplementalFont addNewFont();

    void removeFont(int i);

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();
}
