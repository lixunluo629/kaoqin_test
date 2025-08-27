package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedByte;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPivotCacheDefinition.class */
public interface CTPivotCacheDefinition extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPivotCacheDefinition.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpivotcachedefinition575ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPivotCacheDefinition$Factory.class */
    public static final class Factory {
        public static CTPivotCacheDefinition newInstance() {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.newInstance(CTPivotCacheDefinition.type, null);
        }

        public static CTPivotCacheDefinition newInstance(XmlOptions xmlOptions) {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.newInstance(CTPivotCacheDefinition.type, xmlOptions);
        }

        public static CTPivotCacheDefinition parse(String str) throws XmlException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(str, CTPivotCacheDefinition.type, (XmlOptions) null);
        }

        public static CTPivotCacheDefinition parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(str, CTPivotCacheDefinition.type, xmlOptions);
        }

        public static CTPivotCacheDefinition parse(File file) throws XmlException, IOException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(file, CTPivotCacheDefinition.type, (XmlOptions) null);
        }

        public static CTPivotCacheDefinition parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(file, CTPivotCacheDefinition.type, xmlOptions);
        }

        public static CTPivotCacheDefinition parse(URL url) throws XmlException, IOException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(url, CTPivotCacheDefinition.type, (XmlOptions) null);
        }

        public static CTPivotCacheDefinition parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(url, CTPivotCacheDefinition.type, xmlOptions);
        }

        public static CTPivotCacheDefinition parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(inputStream, CTPivotCacheDefinition.type, (XmlOptions) null);
        }

        public static CTPivotCacheDefinition parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(inputStream, CTPivotCacheDefinition.type, xmlOptions);
        }

        public static CTPivotCacheDefinition parse(Reader reader) throws XmlException, IOException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(reader, CTPivotCacheDefinition.type, (XmlOptions) null);
        }

        public static CTPivotCacheDefinition parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(reader, CTPivotCacheDefinition.type, xmlOptions);
        }

        public static CTPivotCacheDefinition parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(xMLStreamReader, CTPivotCacheDefinition.type, (XmlOptions) null);
        }

        public static CTPivotCacheDefinition parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(xMLStreamReader, CTPivotCacheDefinition.type, xmlOptions);
        }

        public static CTPivotCacheDefinition parse(Node node) throws XmlException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(node, CTPivotCacheDefinition.type, (XmlOptions) null);
        }

        public static CTPivotCacheDefinition parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(node, CTPivotCacheDefinition.type, xmlOptions);
        }

        public static CTPivotCacheDefinition parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(xMLInputStream, CTPivotCacheDefinition.type, (XmlOptions) null);
        }

        public static CTPivotCacheDefinition parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPivotCacheDefinition) POIXMLTypeLoader.parse(xMLInputStream, CTPivotCacheDefinition.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPivotCacheDefinition.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPivotCacheDefinition.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTCacheSource getCacheSource();

    void setCacheSource(CTCacheSource cTCacheSource);

    CTCacheSource addNewCacheSource();

    CTCacheFields getCacheFields();

    void setCacheFields(CTCacheFields cTCacheFields);

    CTCacheFields addNewCacheFields();

    CTCacheHierarchies getCacheHierarchies();

    boolean isSetCacheHierarchies();

    void setCacheHierarchies(CTCacheHierarchies cTCacheHierarchies);

    CTCacheHierarchies addNewCacheHierarchies();

    void unsetCacheHierarchies();

    CTPCDKPIs getKpis();

    boolean isSetKpis();

    void setKpis(CTPCDKPIs cTPCDKPIs);

    CTPCDKPIs addNewKpis();

    void unsetKpis();

    CTTupleCache getTupleCache();

    boolean isSetTupleCache();

    void setTupleCache(CTTupleCache cTTupleCache);

    CTTupleCache addNewTupleCache();

    void unsetTupleCache();

    CTCalculatedItems getCalculatedItems();

    boolean isSetCalculatedItems();

    void setCalculatedItems(CTCalculatedItems cTCalculatedItems);

    CTCalculatedItems addNewCalculatedItems();

    void unsetCalculatedItems();

    CTCalculatedMembers getCalculatedMembers();

    boolean isSetCalculatedMembers();

    void setCalculatedMembers(CTCalculatedMembers cTCalculatedMembers);

    CTCalculatedMembers addNewCalculatedMembers();

    void unsetCalculatedMembers();

    CTDimensions getDimensions();

    boolean isSetDimensions();

    void setDimensions(CTDimensions cTDimensions);

    CTDimensions addNewDimensions();

    void unsetDimensions();

    CTMeasureGroups getMeasureGroups();

    boolean isSetMeasureGroups();

    void setMeasureGroups(CTMeasureGroups cTMeasureGroups);

    CTMeasureGroups addNewMeasureGroups();

    void unsetMeasureGroups();

    CTMeasureDimensionMaps getMaps();

    boolean isSetMaps();

    void setMaps(CTMeasureDimensionMaps cTMeasureDimensionMaps);

    CTMeasureDimensionMaps addNewMaps();

    void unsetMaps();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    String getId();

    STRelationshipId xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(STRelationshipId sTRelationshipId);

    void unsetId();

    boolean getInvalid();

    XmlBoolean xgetInvalid();

    boolean isSetInvalid();

    void setInvalid(boolean z);

    void xsetInvalid(XmlBoolean xmlBoolean);

    void unsetInvalid();

    boolean getSaveData();

    XmlBoolean xgetSaveData();

    boolean isSetSaveData();

    void setSaveData(boolean z);

    void xsetSaveData(XmlBoolean xmlBoolean);

    void unsetSaveData();

    boolean getRefreshOnLoad();

    XmlBoolean xgetRefreshOnLoad();

    boolean isSetRefreshOnLoad();

    void setRefreshOnLoad(boolean z);

    void xsetRefreshOnLoad(XmlBoolean xmlBoolean);

    void unsetRefreshOnLoad();

    boolean getOptimizeMemory();

    XmlBoolean xgetOptimizeMemory();

    boolean isSetOptimizeMemory();

    void setOptimizeMemory(boolean z);

    void xsetOptimizeMemory(XmlBoolean xmlBoolean);

    void unsetOptimizeMemory();

    boolean getEnableRefresh();

    XmlBoolean xgetEnableRefresh();

    boolean isSetEnableRefresh();

    void setEnableRefresh(boolean z);

    void xsetEnableRefresh(XmlBoolean xmlBoolean);

    void unsetEnableRefresh();

    String getRefreshedBy();

    STXstring xgetRefreshedBy();

    boolean isSetRefreshedBy();

    void setRefreshedBy(String str);

    void xsetRefreshedBy(STXstring sTXstring);

    void unsetRefreshedBy();

    double getRefreshedDate();

    XmlDouble xgetRefreshedDate();

    boolean isSetRefreshedDate();

    void setRefreshedDate(double d);

    void xsetRefreshedDate(XmlDouble xmlDouble);

    void unsetRefreshedDate();

    boolean getBackgroundQuery();

    XmlBoolean xgetBackgroundQuery();

    boolean isSetBackgroundQuery();

    void setBackgroundQuery(boolean z);

    void xsetBackgroundQuery(XmlBoolean xmlBoolean);

    void unsetBackgroundQuery();

    long getMissingItemsLimit();

    XmlUnsignedInt xgetMissingItemsLimit();

    boolean isSetMissingItemsLimit();

    void setMissingItemsLimit(long j);

    void xsetMissingItemsLimit(XmlUnsignedInt xmlUnsignedInt);

    void unsetMissingItemsLimit();

    short getCreatedVersion();

    XmlUnsignedByte xgetCreatedVersion();

    boolean isSetCreatedVersion();

    void setCreatedVersion(short s);

    void xsetCreatedVersion(XmlUnsignedByte xmlUnsignedByte);

    void unsetCreatedVersion();

    short getRefreshedVersion();

    XmlUnsignedByte xgetRefreshedVersion();

    boolean isSetRefreshedVersion();

    void setRefreshedVersion(short s);

    void xsetRefreshedVersion(XmlUnsignedByte xmlUnsignedByte);

    void unsetRefreshedVersion();

    short getMinRefreshableVersion();

    XmlUnsignedByte xgetMinRefreshableVersion();

    boolean isSetMinRefreshableVersion();

    void setMinRefreshableVersion(short s);

    void xsetMinRefreshableVersion(XmlUnsignedByte xmlUnsignedByte);

    void unsetMinRefreshableVersion();

    long getRecordCount();

    XmlUnsignedInt xgetRecordCount();

    boolean isSetRecordCount();

    void setRecordCount(long j);

    void xsetRecordCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetRecordCount();

    boolean getUpgradeOnRefresh();

    XmlBoolean xgetUpgradeOnRefresh();

    boolean isSetUpgradeOnRefresh();

    void setUpgradeOnRefresh(boolean z);

    void xsetUpgradeOnRefresh(XmlBoolean xmlBoolean);

    void unsetUpgradeOnRefresh();

    boolean getTupleCache2();

    XmlBoolean xgetTupleCache2();

    boolean isSetTupleCache2();

    void setTupleCache2(boolean z);

    void xsetTupleCache2(XmlBoolean xmlBoolean);

    void unsetTupleCache2();

    boolean getSupportSubquery();

    XmlBoolean xgetSupportSubquery();

    boolean isSetSupportSubquery();

    void setSupportSubquery(boolean z);

    void xsetSupportSubquery(XmlBoolean xmlBoolean);

    void unsetSupportSubquery();

    boolean getSupportAdvancedDrill();

    XmlBoolean xgetSupportAdvancedDrill();

    boolean isSetSupportAdvancedDrill();

    void setSupportAdvancedDrill(boolean z);

    void xsetSupportAdvancedDrill(XmlBoolean xmlBoolean);

    void unsetSupportAdvancedDrill();
}
