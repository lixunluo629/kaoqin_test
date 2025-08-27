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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/SectionType.class */
public interface SectionType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SectionType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sectiontype30a6type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/SectionType$Factory.class */
    public static final class Factory {
        public static SectionType newInstance() {
            return (SectionType) POIXMLTypeLoader.newInstance(SectionType.type, null);
        }

        public static SectionType newInstance(XmlOptions xmlOptions) {
            return (SectionType) POIXMLTypeLoader.newInstance(SectionType.type, xmlOptions);
        }

        public static SectionType parse(String str) throws XmlException {
            return (SectionType) POIXMLTypeLoader.parse(str, SectionType.type, (XmlOptions) null);
        }

        public static SectionType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (SectionType) POIXMLTypeLoader.parse(str, SectionType.type, xmlOptions);
        }

        public static SectionType parse(File file) throws XmlException, IOException {
            return (SectionType) POIXMLTypeLoader.parse(file, SectionType.type, (XmlOptions) null);
        }

        public static SectionType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SectionType) POIXMLTypeLoader.parse(file, SectionType.type, xmlOptions);
        }

        public static SectionType parse(URL url) throws XmlException, IOException {
            return (SectionType) POIXMLTypeLoader.parse(url, SectionType.type, (XmlOptions) null);
        }

        public static SectionType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SectionType) POIXMLTypeLoader.parse(url, SectionType.type, xmlOptions);
        }

        public static SectionType parse(InputStream inputStream) throws XmlException, IOException {
            return (SectionType) POIXMLTypeLoader.parse(inputStream, SectionType.type, (XmlOptions) null);
        }

        public static SectionType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SectionType) POIXMLTypeLoader.parse(inputStream, SectionType.type, xmlOptions);
        }

        public static SectionType parse(Reader reader) throws XmlException, IOException {
            return (SectionType) POIXMLTypeLoader.parse(reader, SectionType.type, (XmlOptions) null);
        }

        public static SectionType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SectionType) POIXMLTypeLoader.parse(reader, SectionType.type, xmlOptions);
        }

        public static SectionType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (SectionType) POIXMLTypeLoader.parse(xMLStreamReader, SectionType.type, (XmlOptions) null);
        }

        public static SectionType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (SectionType) POIXMLTypeLoader.parse(xMLStreamReader, SectionType.type, xmlOptions);
        }

        public static SectionType parse(Node node) throws XmlException {
            return (SectionType) POIXMLTypeLoader.parse(node, SectionType.type, (XmlOptions) null);
        }

        public static SectionType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (SectionType) POIXMLTypeLoader.parse(node, SectionType.type, xmlOptions);
        }

        public static SectionType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (SectionType) POIXMLTypeLoader.parse(xMLInputStream, SectionType.type, (XmlOptions) null);
        }

        public static SectionType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (SectionType) POIXMLTypeLoader.parse(xMLInputStream, SectionType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SectionType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SectionType.type, xmlOptions);
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

    List<RowType> getRowList();

    RowType[] getRowArray();

    RowType getRowArray(int i);

    int sizeOfRowArray();

    void setRowArray(RowType[] rowTypeArr);

    void setRowArray(int i, RowType rowType);

    RowType insertNewRow(int i);

    RowType addNewRow();

    void removeRow(int i);

    String getN();

    XmlString xgetN();

    void setN(String str);

    void xsetN(XmlString xmlString);

    boolean getDel();

    XmlBoolean xgetDel();

    boolean isSetDel();

    void setDel(boolean z);

    void xsetDel(XmlBoolean xmlBoolean);

    void unsetDel();

    long getIX();

    XmlUnsignedInt xgetIX();

    boolean isSetIX();

    void setIX(long j);

    void xsetIX(XmlUnsignedInt xmlUnsignedInt);

    void unsetIX();
}
