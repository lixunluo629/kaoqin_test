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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTColorScheme.class */
public interface CTColorScheme extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTColorScheme.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcolorscheme0e99type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTColorScheme$Factory.class */
    public static final class Factory {
        public static CTColorScheme newInstance() {
            return (CTColorScheme) POIXMLTypeLoader.newInstance(CTColorScheme.type, null);
        }

        public static CTColorScheme newInstance(XmlOptions xmlOptions) {
            return (CTColorScheme) POIXMLTypeLoader.newInstance(CTColorScheme.type, xmlOptions);
        }

        public static CTColorScheme parse(String str) throws XmlException {
            return (CTColorScheme) POIXMLTypeLoader.parse(str, CTColorScheme.type, (XmlOptions) null);
        }

        public static CTColorScheme parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTColorScheme) POIXMLTypeLoader.parse(str, CTColorScheme.type, xmlOptions);
        }

        public static CTColorScheme parse(File file) throws XmlException, IOException {
            return (CTColorScheme) POIXMLTypeLoader.parse(file, CTColorScheme.type, (XmlOptions) null);
        }

        public static CTColorScheme parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorScheme) POIXMLTypeLoader.parse(file, CTColorScheme.type, xmlOptions);
        }

        public static CTColorScheme parse(URL url) throws XmlException, IOException {
            return (CTColorScheme) POIXMLTypeLoader.parse(url, CTColorScheme.type, (XmlOptions) null);
        }

        public static CTColorScheme parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorScheme) POIXMLTypeLoader.parse(url, CTColorScheme.type, xmlOptions);
        }

        public static CTColorScheme parse(InputStream inputStream) throws XmlException, IOException {
            return (CTColorScheme) POIXMLTypeLoader.parse(inputStream, CTColorScheme.type, (XmlOptions) null);
        }

        public static CTColorScheme parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorScheme) POIXMLTypeLoader.parse(inputStream, CTColorScheme.type, xmlOptions);
        }

        public static CTColorScheme parse(Reader reader) throws XmlException, IOException {
            return (CTColorScheme) POIXMLTypeLoader.parse(reader, CTColorScheme.type, (XmlOptions) null);
        }

        public static CTColorScheme parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorScheme) POIXMLTypeLoader.parse(reader, CTColorScheme.type, xmlOptions);
        }

        public static CTColorScheme parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTColorScheme) POIXMLTypeLoader.parse(xMLStreamReader, CTColorScheme.type, (XmlOptions) null);
        }

        public static CTColorScheme parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTColorScheme) POIXMLTypeLoader.parse(xMLStreamReader, CTColorScheme.type, xmlOptions);
        }

        public static CTColorScheme parse(Node node) throws XmlException {
            return (CTColorScheme) POIXMLTypeLoader.parse(node, CTColorScheme.type, (XmlOptions) null);
        }

        public static CTColorScheme parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTColorScheme) POIXMLTypeLoader.parse(node, CTColorScheme.type, xmlOptions);
        }

        public static CTColorScheme parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTColorScheme) POIXMLTypeLoader.parse(xMLInputStream, CTColorScheme.type, (XmlOptions) null);
        }

        public static CTColorScheme parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTColorScheme) POIXMLTypeLoader.parse(xMLInputStream, CTColorScheme.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColorScheme.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColorScheme.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTColor getDk1();

    void setDk1(CTColor cTColor);

    CTColor addNewDk1();

    CTColor getLt1();

    void setLt1(CTColor cTColor);

    CTColor addNewLt1();

    CTColor getDk2();

    void setDk2(CTColor cTColor);

    CTColor addNewDk2();

    CTColor getLt2();

    void setLt2(CTColor cTColor);

    CTColor addNewLt2();

    CTColor getAccent1();

    void setAccent1(CTColor cTColor);

    CTColor addNewAccent1();

    CTColor getAccent2();

    void setAccent2(CTColor cTColor);

    CTColor addNewAccent2();

    CTColor getAccent3();

    void setAccent3(CTColor cTColor);

    CTColor addNewAccent3();

    CTColor getAccent4();

    void setAccent4(CTColor cTColor);

    CTColor addNewAccent4();

    CTColor getAccent5();

    void setAccent5(CTColor cTColor);

    CTColor addNewAccent5();

    CTColor getAccent6();

    void setAccent6(CTColor cTColor);

    CTColor addNewAccent6();

    CTColor getHlink();

    void setHlink(CTColor cTColor);

    CTColor addNewHlink();

    CTColor getFolHlink();

    void setFolHlink(CTColor cTColor);

    CTColor addNewFolHlink();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    String getName();

    XmlString xgetName();

    void setName(String str);

    void xsetName(XmlString xmlString);
}
