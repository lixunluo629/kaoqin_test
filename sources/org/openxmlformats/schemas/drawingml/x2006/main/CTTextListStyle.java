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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextListStyle.class */
public interface CTTextListStyle extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextListStyle.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextliststyleab77type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextListStyle$Factory.class */
    public static final class Factory {
        public static CTTextListStyle newInstance() {
            return (CTTextListStyle) POIXMLTypeLoader.newInstance(CTTextListStyle.type, null);
        }

        public static CTTextListStyle newInstance(XmlOptions xmlOptions) {
            return (CTTextListStyle) POIXMLTypeLoader.newInstance(CTTextListStyle.type, xmlOptions);
        }

        public static CTTextListStyle parse(String str) throws XmlException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(str, CTTextListStyle.type, (XmlOptions) null);
        }

        public static CTTextListStyle parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(str, CTTextListStyle.type, xmlOptions);
        }

        public static CTTextListStyle parse(File file) throws XmlException, IOException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(file, CTTextListStyle.type, (XmlOptions) null);
        }

        public static CTTextListStyle parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(file, CTTextListStyle.type, xmlOptions);
        }

        public static CTTextListStyle parse(URL url) throws XmlException, IOException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(url, CTTextListStyle.type, (XmlOptions) null);
        }

        public static CTTextListStyle parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(url, CTTextListStyle.type, xmlOptions);
        }

        public static CTTextListStyle parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(inputStream, CTTextListStyle.type, (XmlOptions) null);
        }

        public static CTTextListStyle parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(inputStream, CTTextListStyle.type, xmlOptions);
        }

        public static CTTextListStyle parse(Reader reader) throws XmlException, IOException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(reader, CTTextListStyle.type, (XmlOptions) null);
        }

        public static CTTextListStyle parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(reader, CTTextListStyle.type, xmlOptions);
        }

        public static CTTextListStyle parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(xMLStreamReader, CTTextListStyle.type, (XmlOptions) null);
        }

        public static CTTextListStyle parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(xMLStreamReader, CTTextListStyle.type, xmlOptions);
        }

        public static CTTextListStyle parse(Node node) throws XmlException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(node, CTTextListStyle.type, (XmlOptions) null);
        }

        public static CTTextListStyle parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(node, CTTextListStyle.type, xmlOptions);
        }

        public static CTTextListStyle parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(xMLInputStream, CTTextListStyle.type, (XmlOptions) null);
        }

        public static CTTextListStyle parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextListStyle) POIXMLTypeLoader.parse(xMLInputStream, CTTextListStyle.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextListStyle.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextListStyle.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTextParagraphProperties getDefPPr();

    boolean isSetDefPPr();

    void setDefPPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewDefPPr();

    void unsetDefPPr();

    CTTextParagraphProperties getLvl1PPr();

    boolean isSetLvl1PPr();

    void setLvl1PPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewLvl1PPr();

    void unsetLvl1PPr();

    CTTextParagraphProperties getLvl2PPr();

    boolean isSetLvl2PPr();

    void setLvl2PPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewLvl2PPr();

    void unsetLvl2PPr();

    CTTextParagraphProperties getLvl3PPr();

    boolean isSetLvl3PPr();

    void setLvl3PPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewLvl3PPr();

    void unsetLvl3PPr();

    CTTextParagraphProperties getLvl4PPr();

    boolean isSetLvl4PPr();

    void setLvl4PPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewLvl4PPr();

    void unsetLvl4PPr();

    CTTextParagraphProperties getLvl5PPr();

    boolean isSetLvl5PPr();

    void setLvl5PPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewLvl5PPr();

    void unsetLvl5PPr();

    CTTextParagraphProperties getLvl6PPr();

    boolean isSetLvl6PPr();

    void setLvl6PPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewLvl6PPr();

    void unsetLvl6PPr();

    CTTextParagraphProperties getLvl7PPr();

    boolean isSetLvl7PPr();

    void setLvl7PPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewLvl7PPr();

    void unsetLvl7PPr();

    CTTextParagraphProperties getLvl8PPr();

    boolean isSetLvl8PPr();

    void setLvl8PPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewLvl8PPr();

    void unsetLvl8PPr();

    CTTextParagraphProperties getLvl9PPr();

    boolean isSetLvl9PPr();

    void setLvl9PPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewLvl9PPr();

    void unsetLvl9PPr();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();
}
