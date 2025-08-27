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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.XmlUnsignedShort;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/MasterType.class */
public interface MasterType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(MasterType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("mastertype2d97type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/MasterType$Factory.class */
    public static final class Factory {
        public static MasterType newInstance() {
            return (MasterType) POIXMLTypeLoader.newInstance(MasterType.type, null);
        }

        public static MasterType newInstance(XmlOptions xmlOptions) {
            return (MasterType) POIXMLTypeLoader.newInstance(MasterType.type, xmlOptions);
        }

        public static MasterType parse(String str) throws XmlException {
            return (MasterType) POIXMLTypeLoader.parse(str, MasterType.type, (XmlOptions) null);
        }

        public static MasterType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (MasterType) POIXMLTypeLoader.parse(str, MasterType.type, xmlOptions);
        }

        public static MasterType parse(File file) throws XmlException, IOException {
            return (MasterType) POIXMLTypeLoader.parse(file, MasterType.type, (XmlOptions) null);
        }

        public static MasterType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (MasterType) POIXMLTypeLoader.parse(file, MasterType.type, xmlOptions);
        }

        public static MasterType parse(URL url) throws XmlException, IOException {
            return (MasterType) POIXMLTypeLoader.parse(url, MasterType.type, (XmlOptions) null);
        }

        public static MasterType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (MasterType) POIXMLTypeLoader.parse(url, MasterType.type, xmlOptions);
        }

        public static MasterType parse(InputStream inputStream) throws XmlException, IOException {
            return (MasterType) POIXMLTypeLoader.parse(inputStream, MasterType.type, (XmlOptions) null);
        }

        public static MasterType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (MasterType) POIXMLTypeLoader.parse(inputStream, MasterType.type, xmlOptions);
        }

        public static MasterType parse(Reader reader) throws XmlException, IOException {
            return (MasterType) POIXMLTypeLoader.parse(reader, MasterType.type, (XmlOptions) null);
        }

        public static MasterType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (MasterType) POIXMLTypeLoader.parse(reader, MasterType.type, xmlOptions);
        }

        public static MasterType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (MasterType) POIXMLTypeLoader.parse(xMLStreamReader, MasterType.type, (XmlOptions) null);
        }

        public static MasterType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (MasterType) POIXMLTypeLoader.parse(xMLStreamReader, MasterType.type, xmlOptions);
        }

        public static MasterType parse(Node node) throws XmlException {
            return (MasterType) POIXMLTypeLoader.parse(node, MasterType.type, (XmlOptions) null);
        }

        public static MasterType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (MasterType) POIXMLTypeLoader.parse(node, MasterType.type, xmlOptions);
        }

        public static MasterType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (MasterType) POIXMLTypeLoader.parse(xMLInputStream, MasterType.type, (XmlOptions) null);
        }

        public static MasterType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (MasterType) POIXMLTypeLoader.parse(xMLInputStream, MasterType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, MasterType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, MasterType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    PageSheetType getPageSheet();

    boolean isSetPageSheet();

    void setPageSheet(PageSheetType pageSheetType);

    PageSheetType addNewPageSheet();

    void unsetPageSheet();

    RelType getRel();

    void setRel(RelType relType);

    RelType addNewRel();

    IconType getIcon();

    boolean isSetIcon();

    void setIcon(IconType iconType);

    IconType addNewIcon();

    void unsetIcon();

    long getID();

    XmlUnsignedInt xgetID();

    void setID(long j);

    void xsetID(XmlUnsignedInt xmlUnsignedInt);

    String getBaseID();

    XmlString xgetBaseID();

    boolean isSetBaseID();

    void setBaseID(String str);

    void xsetBaseID(XmlString xmlString);

    void unsetBaseID();

    String getUniqueID();

    XmlString xgetUniqueID();

    boolean isSetUniqueID();

    void setUniqueID(String str);

    void xsetUniqueID(XmlString xmlString);

    void unsetUniqueID();

    boolean getMatchByName();

    XmlBoolean xgetMatchByName();

    boolean isSetMatchByName();

    void setMatchByName(boolean z);

    void xsetMatchByName(XmlBoolean xmlBoolean);

    void unsetMatchByName();

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

    int getIconSize();

    XmlUnsignedShort xgetIconSize();

    boolean isSetIconSize();

    void setIconSize(int i);

    void xsetIconSize(XmlUnsignedShort xmlUnsignedShort);

    void unsetIconSize();

    int getPatternFlags();

    XmlUnsignedShort xgetPatternFlags();

    boolean isSetPatternFlags();

    void setPatternFlags(int i);

    void xsetPatternFlags(XmlUnsignedShort xmlUnsignedShort);

    void unsetPatternFlags();

    String getPrompt();

    XmlString xgetPrompt();

    boolean isSetPrompt();

    void setPrompt(String str);

    void xsetPrompt(XmlString xmlString);

    void unsetPrompt();

    boolean getHidden();

    XmlBoolean xgetHidden();

    boolean isSetHidden();

    void setHidden(boolean z);

    void xsetHidden(XmlBoolean xmlBoolean);

    void unsetHidden();

    boolean getIconUpdate();

    XmlBoolean xgetIconUpdate();

    boolean isSetIconUpdate();

    void setIconUpdate(boolean z);

    void xsetIconUpdate(XmlBoolean xmlBoolean);

    void unsetIconUpdate();

    int getAlignName();

    XmlUnsignedShort xgetAlignName();

    boolean isSetAlignName();

    void setAlignName(int i);

    void xsetAlignName(XmlUnsignedShort xmlUnsignedShort);

    void unsetAlignName();

    int getMasterType();

    XmlUnsignedShort xgetMasterType();

    boolean isSetMasterType();

    void setMasterType(int i);

    void xsetMasterType(XmlUnsignedShort xmlUnsignedShort);

    void unsetMasterType();
}
