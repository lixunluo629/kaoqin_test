package com.microsoft.schemas.office.visio.x2012.main;

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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/SheetType.class */
public interface SheetType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SheetType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sheettype25actype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/SheetType$Factory.class */
    public static final class Factory {
        public static SheetType newInstance() {
            return (SheetType) POIXMLTypeLoader.newInstance(SheetType.type, null);
        }

        public static SheetType newInstance(XmlOptions xmlOptions) {
            return (SheetType) POIXMLTypeLoader.newInstance(SheetType.type, xmlOptions);
        }

        public static SheetType parse(String str) throws XmlException {
            return (SheetType) POIXMLTypeLoader.parse(str, SheetType.type, (XmlOptions) null);
        }

        public static SheetType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (SheetType) POIXMLTypeLoader.parse(str, SheetType.type, xmlOptions);
        }

        public static SheetType parse(File file) throws XmlException, IOException {
            return (SheetType) POIXMLTypeLoader.parse(file, SheetType.type, (XmlOptions) null);
        }

        public static SheetType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SheetType) POIXMLTypeLoader.parse(file, SheetType.type, xmlOptions);
        }

        public static SheetType parse(URL url) throws XmlException, IOException {
            return (SheetType) POIXMLTypeLoader.parse(url, SheetType.type, (XmlOptions) null);
        }

        public static SheetType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SheetType) POIXMLTypeLoader.parse(url, SheetType.type, xmlOptions);
        }

        public static SheetType parse(InputStream inputStream) throws XmlException, IOException {
            return (SheetType) POIXMLTypeLoader.parse(inputStream, SheetType.type, (XmlOptions) null);
        }

        public static SheetType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SheetType) POIXMLTypeLoader.parse(inputStream, SheetType.type, xmlOptions);
        }

        public static SheetType parse(Reader reader) throws XmlException, IOException {
            return (SheetType) POIXMLTypeLoader.parse(reader, SheetType.type, (XmlOptions) null);
        }

        public static SheetType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SheetType) POIXMLTypeLoader.parse(reader, SheetType.type, xmlOptions);
        }

        public static SheetType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (SheetType) POIXMLTypeLoader.parse(xMLStreamReader, SheetType.type, (XmlOptions) null);
        }

        public static SheetType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (SheetType) POIXMLTypeLoader.parse(xMLStreamReader, SheetType.type, xmlOptions);
        }

        public static SheetType parse(Node node) throws XmlException {
            return (SheetType) POIXMLTypeLoader.parse(node, SheetType.type, (XmlOptions) null);
        }

        public static SheetType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (SheetType) POIXMLTypeLoader.parse(node, SheetType.type, xmlOptions);
        }

        public static SheetType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (SheetType) POIXMLTypeLoader.parse(xMLInputStream, SheetType.type, (XmlOptions) null);
        }

        public static SheetType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (SheetType) POIXMLTypeLoader.parse(xMLInputStream, SheetType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SheetType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SheetType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CellType> getCellList();

    CellType[] getCellArray();

    CellType getCellArray(int i);

    int sizeOfCellArray();

    void setCellArray(CellType[] cellTypeArr);

    void setCellArray(int i, CellType cellType);

    CellType insertNewCell(int i);

    CellType addNewCell();

    void removeCell(int i);

    List<TriggerType> getTriggerList();

    TriggerType[] getTriggerArray();

    TriggerType getTriggerArray(int i);

    int sizeOfTriggerArray();

    void setTriggerArray(TriggerType[] triggerTypeArr);

    void setTriggerArray(int i, TriggerType triggerType);

    TriggerType insertNewTrigger(int i);

    TriggerType addNewTrigger();

    void removeTrigger(int i);

    List<SectionType> getSectionList();

    SectionType[] getSectionArray();

    SectionType getSectionArray(int i);

    int sizeOfSectionArray();

    void setSectionArray(SectionType[] sectionTypeArr);

    void setSectionArray(int i, SectionType sectionType);

    SectionType insertNewSection(int i);

    SectionType addNewSection();

    void removeSection(int i);

    long getLineStyle();

    XmlUnsignedInt xgetLineStyle();

    boolean isSetLineStyle();

    void setLineStyle(long j);

    void xsetLineStyle(XmlUnsignedInt xmlUnsignedInt);

    void unsetLineStyle();

    long getFillStyle();

    XmlUnsignedInt xgetFillStyle();

    boolean isSetFillStyle();

    void setFillStyle(long j);

    void xsetFillStyle(XmlUnsignedInt xmlUnsignedInt);

    void unsetFillStyle();

    long getTextStyle();

    XmlUnsignedInt xgetTextStyle();

    boolean isSetTextStyle();

    void setTextStyle(long j);

    void xsetTextStyle(XmlUnsignedInt xmlUnsignedInt);

    void unsetTextStyle();
}
