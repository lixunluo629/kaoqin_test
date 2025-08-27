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
import org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTColorMapping.class */
public interface CTColorMapping extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTColorMapping.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcolormapping5bc6type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTColorMapping$Factory.class */
    public static final class Factory {
        public static CTColorMapping newInstance() {
            return (CTColorMapping) POIXMLTypeLoader.newInstance(CTColorMapping.type, null);
        }

        public static CTColorMapping newInstance(XmlOptions xmlOptions) {
            return (CTColorMapping) POIXMLTypeLoader.newInstance(CTColorMapping.type, xmlOptions);
        }

        public static CTColorMapping parse(String str) throws XmlException {
            return (CTColorMapping) POIXMLTypeLoader.parse(str, CTColorMapping.type, (XmlOptions) null);
        }

        public static CTColorMapping parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTColorMapping) POIXMLTypeLoader.parse(str, CTColorMapping.type, xmlOptions);
        }

        public static CTColorMapping parse(File file) throws XmlException, IOException {
            return (CTColorMapping) POIXMLTypeLoader.parse(file, CTColorMapping.type, (XmlOptions) null);
        }

        public static CTColorMapping parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorMapping) POIXMLTypeLoader.parse(file, CTColorMapping.type, xmlOptions);
        }

        public static CTColorMapping parse(URL url) throws XmlException, IOException {
            return (CTColorMapping) POIXMLTypeLoader.parse(url, CTColorMapping.type, (XmlOptions) null);
        }

        public static CTColorMapping parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorMapping) POIXMLTypeLoader.parse(url, CTColorMapping.type, xmlOptions);
        }

        public static CTColorMapping parse(InputStream inputStream) throws XmlException, IOException {
            return (CTColorMapping) POIXMLTypeLoader.parse(inputStream, CTColorMapping.type, (XmlOptions) null);
        }

        public static CTColorMapping parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorMapping) POIXMLTypeLoader.parse(inputStream, CTColorMapping.type, xmlOptions);
        }

        public static CTColorMapping parse(Reader reader) throws XmlException, IOException {
            return (CTColorMapping) POIXMLTypeLoader.parse(reader, CTColorMapping.type, (XmlOptions) null);
        }

        public static CTColorMapping parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColorMapping) POIXMLTypeLoader.parse(reader, CTColorMapping.type, xmlOptions);
        }

        public static CTColorMapping parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTColorMapping) POIXMLTypeLoader.parse(xMLStreamReader, CTColorMapping.type, (XmlOptions) null);
        }

        public static CTColorMapping parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTColorMapping) POIXMLTypeLoader.parse(xMLStreamReader, CTColorMapping.type, xmlOptions);
        }

        public static CTColorMapping parse(Node node) throws XmlException {
            return (CTColorMapping) POIXMLTypeLoader.parse(node, CTColorMapping.type, (XmlOptions) null);
        }

        public static CTColorMapping parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTColorMapping) POIXMLTypeLoader.parse(node, CTColorMapping.type, xmlOptions);
        }

        public static CTColorMapping parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTColorMapping) POIXMLTypeLoader.parse(xMLInputStream, CTColorMapping.type, (XmlOptions) null);
        }

        public static CTColorMapping parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTColorMapping) POIXMLTypeLoader.parse(xMLInputStream, CTColorMapping.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColorMapping.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColorMapping.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    STColorSchemeIndex.Enum getBg1();

    STColorSchemeIndex xgetBg1();

    void setBg1(STColorSchemeIndex.Enum r1);

    void xsetBg1(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getTx1();

    STColorSchemeIndex xgetTx1();

    void setTx1(STColorSchemeIndex.Enum r1);

    void xsetTx1(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getBg2();

    STColorSchemeIndex xgetBg2();

    void setBg2(STColorSchemeIndex.Enum r1);

    void xsetBg2(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getTx2();

    STColorSchemeIndex xgetTx2();

    void setTx2(STColorSchemeIndex.Enum r1);

    void xsetTx2(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getAccent1();

    STColorSchemeIndex xgetAccent1();

    void setAccent1(STColorSchemeIndex.Enum r1);

    void xsetAccent1(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getAccent2();

    STColorSchemeIndex xgetAccent2();

    void setAccent2(STColorSchemeIndex.Enum r1);

    void xsetAccent2(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getAccent3();

    STColorSchemeIndex xgetAccent3();

    void setAccent3(STColorSchemeIndex.Enum r1);

    void xsetAccent3(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getAccent4();

    STColorSchemeIndex xgetAccent4();

    void setAccent4(STColorSchemeIndex.Enum r1);

    void xsetAccent4(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getAccent5();

    STColorSchemeIndex xgetAccent5();

    void setAccent5(STColorSchemeIndex.Enum r1);

    void xsetAccent5(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getAccent6();

    STColorSchemeIndex xgetAccent6();

    void setAccent6(STColorSchemeIndex.Enum r1);

    void xsetAccent6(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getHlink();

    STColorSchemeIndex xgetHlink();

    void setHlink(STColorSchemeIndex.Enum r1);

    void xsetHlink(STColorSchemeIndex sTColorSchemeIndex);

    STColorSchemeIndex.Enum getFolHlink();

    STColorSchemeIndex xgetFolHlink();

    void setFolHlink(STColorSchemeIndex.Enum r1);

    void xsetFolHlink(STColorSchemeIndex sTColorSchemeIndex);
}
