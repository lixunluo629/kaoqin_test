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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/RowType.class */
public interface RowType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(RowType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("rowtype03d1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/RowType$Factory.class */
    public static final class Factory {
        public static RowType newInstance() {
            return (RowType) POIXMLTypeLoader.newInstance(RowType.type, null);
        }

        public static RowType newInstance(XmlOptions xmlOptions) {
            return (RowType) POIXMLTypeLoader.newInstance(RowType.type, xmlOptions);
        }

        public static RowType parse(String str) throws XmlException {
            return (RowType) POIXMLTypeLoader.parse(str, RowType.type, (XmlOptions) null);
        }

        public static RowType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (RowType) POIXMLTypeLoader.parse(str, RowType.type, xmlOptions);
        }

        public static RowType parse(File file) throws XmlException, IOException {
            return (RowType) POIXMLTypeLoader.parse(file, RowType.type, (XmlOptions) null);
        }

        public static RowType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (RowType) POIXMLTypeLoader.parse(file, RowType.type, xmlOptions);
        }

        public static RowType parse(URL url) throws XmlException, IOException {
            return (RowType) POIXMLTypeLoader.parse(url, RowType.type, (XmlOptions) null);
        }

        public static RowType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (RowType) POIXMLTypeLoader.parse(url, RowType.type, xmlOptions);
        }

        public static RowType parse(InputStream inputStream) throws XmlException, IOException {
            return (RowType) POIXMLTypeLoader.parse(inputStream, RowType.type, (XmlOptions) null);
        }

        public static RowType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (RowType) POIXMLTypeLoader.parse(inputStream, RowType.type, xmlOptions);
        }

        public static RowType parse(Reader reader) throws XmlException, IOException {
            return (RowType) POIXMLTypeLoader.parse(reader, RowType.type, (XmlOptions) null);
        }

        public static RowType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (RowType) POIXMLTypeLoader.parse(reader, RowType.type, xmlOptions);
        }

        public static RowType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (RowType) POIXMLTypeLoader.parse(xMLStreamReader, RowType.type, (XmlOptions) null);
        }

        public static RowType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (RowType) POIXMLTypeLoader.parse(xMLStreamReader, RowType.type, xmlOptions);
        }

        public static RowType parse(Node node) throws XmlException {
            return (RowType) POIXMLTypeLoader.parse(node, RowType.type, (XmlOptions) null);
        }

        public static RowType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (RowType) POIXMLTypeLoader.parse(node, RowType.type, xmlOptions);
        }

        public static RowType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (RowType) POIXMLTypeLoader.parse(xMLInputStream, RowType.type, (XmlOptions) null);
        }

        public static RowType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (RowType) POIXMLTypeLoader.parse(xMLInputStream, RowType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, RowType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, RowType.type, xmlOptions);
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

    String getN();

    XmlString xgetN();

    boolean isSetN();

    void setN(String str);

    void xsetN(XmlString xmlString);

    void unsetN();

    String getLocalName();

    XmlString xgetLocalName();

    boolean isSetLocalName();

    void setLocalName(String str);

    void xsetLocalName(XmlString xmlString);

    void unsetLocalName();

    long getIX();

    XmlUnsignedInt xgetIX();

    boolean isSetIX();

    void setIX(long j);

    void xsetIX(XmlUnsignedInt xmlUnsignedInt);

    void unsetIX();

    String getT();

    XmlString xgetT();

    boolean isSetT();

    void setT(String str);

    void xsetT(XmlString xmlString);

    void unsetT();

    boolean getDel();

    XmlBoolean xgetDel();

    boolean isSetDel();

    void setDel(boolean z);

    void xsetDel(XmlBoolean xmlBoolean);

    void unsetDel();
}
