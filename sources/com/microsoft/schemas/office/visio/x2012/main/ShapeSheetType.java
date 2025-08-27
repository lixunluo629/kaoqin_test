package com.microsoft.schemas.office.visio.x2012.main;

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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/ShapeSheetType.class */
public interface ShapeSheetType extends SheetType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ShapeSheetType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("shapesheettype59bbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/ShapeSheetType$Factory.class */
    public static final class Factory {
        public static ShapeSheetType newInstance() {
            return (ShapeSheetType) POIXMLTypeLoader.newInstance(ShapeSheetType.type, null);
        }

        public static ShapeSheetType newInstance(XmlOptions xmlOptions) {
            return (ShapeSheetType) POIXMLTypeLoader.newInstance(ShapeSheetType.type, xmlOptions);
        }

        public static ShapeSheetType parse(String str) throws XmlException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(str, ShapeSheetType.type, (XmlOptions) null);
        }

        public static ShapeSheetType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(str, ShapeSheetType.type, xmlOptions);
        }

        public static ShapeSheetType parse(File file) throws XmlException, IOException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(file, ShapeSheetType.type, (XmlOptions) null);
        }

        public static ShapeSheetType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(file, ShapeSheetType.type, xmlOptions);
        }

        public static ShapeSheetType parse(URL url) throws XmlException, IOException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(url, ShapeSheetType.type, (XmlOptions) null);
        }

        public static ShapeSheetType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(url, ShapeSheetType.type, xmlOptions);
        }

        public static ShapeSheetType parse(InputStream inputStream) throws XmlException, IOException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(inputStream, ShapeSheetType.type, (XmlOptions) null);
        }

        public static ShapeSheetType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(inputStream, ShapeSheetType.type, xmlOptions);
        }

        public static ShapeSheetType parse(Reader reader) throws XmlException, IOException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(reader, ShapeSheetType.type, (XmlOptions) null);
        }

        public static ShapeSheetType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(reader, ShapeSheetType.type, xmlOptions);
        }

        public static ShapeSheetType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(xMLStreamReader, ShapeSheetType.type, (XmlOptions) null);
        }

        public static ShapeSheetType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(xMLStreamReader, ShapeSheetType.type, xmlOptions);
        }

        public static ShapeSheetType parse(Node node) throws XmlException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(node, ShapeSheetType.type, (XmlOptions) null);
        }

        public static ShapeSheetType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(node, ShapeSheetType.type, xmlOptions);
        }

        public static ShapeSheetType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(xMLInputStream, ShapeSheetType.type, (XmlOptions) null);
        }

        public static ShapeSheetType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (ShapeSheetType) POIXMLTypeLoader.parse(xMLInputStream, ShapeSheetType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, ShapeSheetType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, ShapeSheetType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    TextType getText();

    boolean isSetText();

    void setText(TextType textType);

    TextType addNewText();

    void unsetText();

    DataType getData1();

    boolean isSetData1();

    void setData1(DataType dataType);

    DataType addNewData1();

    void unsetData1();

    DataType getData2();

    boolean isSetData2();

    void setData2(DataType dataType);

    DataType addNewData2();

    void unsetData2();

    DataType getData3();

    boolean isSetData3();

    void setData3(DataType dataType);

    DataType addNewData3();

    void unsetData3();

    ForeignDataType getForeignData();

    boolean isSetForeignData();

    void setForeignData(ForeignDataType foreignDataType);

    ForeignDataType addNewForeignData();

    void unsetForeignData();

    ShapesType getShapes();

    boolean isSetShapes();

    void setShapes(ShapesType shapesType);

    ShapesType addNewShapes();

    void unsetShapes();

    long getID();

    XmlUnsignedInt xgetID();

    void setID(long j);

    void xsetID(XmlUnsignedInt xmlUnsignedInt);

    long getOriginalID();

    XmlUnsignedInt xgetOriginalID();

    boolean isSetOriginalID();

    void setOriginalID(long j);

    void xsetOriginalID(XmlUnsignedInt xmlUnsignedInt);

    void unsetOriginalID();

    boolean getDel();

    XmlBoolean xgetDel();

    boolean isSetDel();

    void setDel(boolean z);

    void xsetDel(XmlBoolean xmlBoolean);

    void unsetDel();

    long getMasterShape();

    XmlUnsignedInt xgetMasterShape();

    boolean isSetMasterShape();

    void setMasterShape(long j);

    void xsetMasterShape(XmlUnsignedInt xmlUnsignedInt);

    void unsetMasterShape();

    String getUniqueID();

    XmlString xgetUniqueID();

    boolean isSetUniqueID();

    void setUniqueID(String str);

    void xsetUniqueID(XmlString xmlString);

    void unsetUniqueID();

    String getName();

    XmlString xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(XmlString xmlString);

    void unsetName();

    String getNameU();

    XmlString xgetNameU();

    boolean isSetNameU();

    void setNameU(String str);

    void xsetNameU(XmlString xmlString);

    void unsetNameU();

    boolean getIsCustomName();

    XmlBoolean xgetIsCustomName();

    boolean isSetIsCustomName();

    void setIsCustomName(boolean z);

    void xsetIsCustomName(XmlBoolean xmlBoolean);

    void unsetIsCustomName();

    boolean getIsCustomNameU();

    XmlBoolean xgetIsCustomNameU();

    boolean isSetIsCustomNameU();

    void setIsCustomNameU(boolean z);

    void xsetIsCustomNameU(XmlBoolean xmlBoolean);

    void unsetIsCustomNameU();

    long getMaster();

    XmlUnsignedInt xgetMaster();

    boolean isSetMaster();

    void setMaster(long j);

    void xsetMaster(XmlUnsignedInt xmlUnsignedInt);

    void unsetMaster();

    String getType();

    XmlToken xgetType();

    boolean isSetType();

    void setType(String str);

    void xsetType(XmlToken xmlToken);

    void unsetType();
}
