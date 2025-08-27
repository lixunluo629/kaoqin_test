package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCacheField.class */
public interface CTCacheField extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCacheField.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcachefieldae21type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCacheField$Factory.class */
    public static final class Factory {
        public static CTCacheField newInstance() {
            return (CTCacheField) POIXMLTypeLoader.newInstance(CTCacheField.type, null);
        }

        public static CTCacheField newInstance(XmlOptions xmlOptions) {
            return (CTCacheField) POIXMLTypeLoader.newInstance(CTCacheField.type, xmlOptions);
        }

        public static CTCacheField parse(String str) throws XmlException {
            return (CTCacheField) POIXMLTypeLoader.parse(str, CTCacheField.type, (XmlOptions) null);
        }

        public static CTCacheField parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCacheField) POIXMLTypeLoader.parse(str, CTCacheField.type, xmlOptions);
        }

        public static CTCacheField parse(File file) throws XmlException, IOException {
            return (CTCacheField) POIXMLTypeLoader.parse(file, CTCacheField.type, (XmlOptions) null);
        }

        public static CTCacheField parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCacheField) POIXMLTypeLoader.parse(file, CTCacheField.type, xmlOptions);
        }

        public static CTCacheField parse(URL url) throws XmlException, IOException {
            return (CTCacheField) POIXMLTypeLoader.parse(url, CTCacheField.type, (XmlOptions) null);
        }

        public static CTCacheField parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCacheField) POIXMLTypeLoader.parse(url, CTCacheField.type, xmlOptions);
        }

        public static CTCacheField parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCacheField) POIXMLTypeLoader.parse(inputStream, CTCacheField.type, (XmlOptions) null);
        }

        public static CTCacheField parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCacheField) POIXMLTypeLoader.parse(inputStream, CTCacheField.type, xmlOptions);
        }

        public static CTCacheField parse(Reader reader) throws XmlException, IOException {
            return (CTCacheField) POIXMLTypeLoader.parse(reader, CTCacheField.type, (XmlOptions) null);
        }

        public static CTCacheField parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCacheField) POIXMLTypeLoader.parse(reader, CTCacheField.type, xmlOptions);
        }

        public static CTCacheField parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCacheField) POIXMLTypeLoader.parse(xMLStreamReader, CTCacheField.type, (XmlOptions) null);
        }

        public static CTCacheField parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCacheField) POIXMLTypeLoader.parse(xMLStreamReader, CTCacheField.type, xmlOptions);
        }

        public static CTCacheField parse(Node node) throws XmlException {
            return (CTCacheField) POIXMLTypeLoader.parse(node, CTCacheField.type, (XmlOptions) null);
        }

        public static CTCacheField parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCacheField) POIXMLTypeLoader.parse(node, CTCacheField.type, xmlOptions);
        }

        public static CTCacheField parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCacheField) POIXMLTypeLoader.parse(xMLInputStream, CTCacheField.type, (XmlOptions) null);
        }

        public static CTCacheField parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCacheField) POIXMLTypeLoader.parse(xMLInputStream, CTCacheField.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCacheField.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCacheField.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTSharedItems getSharedItems();

    boolean isSetSharedItems();

    void setSharedItems(CTSharedItems cTSharedItems);

    CTSharedItems addNewSharedItems();

    void unsetSharedItems();

    CTFieldGroup getFieldGroup();

    boolean isSetFieldGroup();

    void setFieldGroup(CTFieldGroup cTFieldGroup);

    CTFieldGroup addNewFieldGroup();

    void unsetFieldGroup();

    List<CTX> getMpMapList();

    CTX[] getMpMapArray();

    CTX getMpMapArray(int i);

    int sizeOfMpMapArray();

    void setMpMapArray(CTX[] ctxArr);

    void setMpMapArray(int i, CTX ctx);

    CTX insertNewMpMap(int i);

    CTX addNewMpMap();

    void removeMpMap(int i);

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    String getName();

    STXstring xgetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    String getCaption();

    STXstring xgetCaption();

    boolean isSetCaption();

    void setCaption(String str);

    void xsetCaption(STXstring sTXstring);

    void unsetCaption();

    String getPropertyName();

    STXstring xgetPropertyName();

    boolean isSetPropertyName();

    void setPropertyName(String str);

    void xsetPropertyName(STXstring sTXstring);

    void unsetPropertyName();

    boolean getServerField();

    XmlBoolean xgetServerField();

    boolean isSetServerField();

    void setServerField(boolean z);

    void xsetServerField(XmlBoolean xmlBoolean);

    void unsetServerField();

    boolean getUniqueList();

    XmlBoolean xgetUniqueList();

    boolean isSetUniqueList();

    void setUniqueList(boolean z);

    void xsetUniqueList(XmlBoolean xmlBoolean);

    void unsetUniqueList();

    long getNumFmtId();

    STNumFmtId xgetNumFmtId();

    boolean isSetNumFmtId();

    void setNumFmtId(long j);

    void xsetNumFmtId(STNumFmtId sTNumFmtId);

    void unsetNumFmtId();

    String getFormula();

    STXstring xgetFormula();

    boolean isSetFormula();

    void setFormula(String str);

    void xsetFormula(STXstring sTXstring);

    void unsetFormula();

    int getSqlType();

    XmlInt xgetSqlType();

    boolean isSetSqlType();

    void setSqlType(int i);

    void xsetSqlType(XmlInt xmlInt);

    void unsetSqlType();

    int getHierarchy();

    XmlInt xgetHierarchy();

    boolean isSetHierarchy();

    void setHierarchy(int i);

    void xsetHierarchy(XmlInt xmlInt);

    void unsetHierarchy();

    long getLevel();

    XmlUnsignedInt xgetLevel();

    boolean isSetLevel();

    void setLevel(long j);

    void xsetLevel(XmlUnsignedInt xmlUnsignedInt);

    void unsetLevel();

    boolean getDatabaseField();

    XmlBoolean xgetDatabaseField();

    boolean isSetDatabaseField();

    void setDatabaseField(boolean z);

    void xsetDatabaseField(XmlBoolean xmlBoolean);

    void unsetDatabaseField();

    long getMappingCount();

    XmlUnsignedInt xgetMappingCount();

    boolean isSetMappingCount();

    void setMappingCount(long j);

    void xsetMappingCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetMappingCount();

    boolean getMemberPropertyField();

    XmlBoolean xgetMemberPropertyField();

    boolean isSetMemberPropertyField();

    void setMemberPropertyField(boolean z);

    void xsetMemberPropertyField(XmlBoolean xmlBoolean);

    void unsetMemberPropertyField();
}
