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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTWorkbook.class */
public interface CTWorkbook extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTWorkbook.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctworkbook83c3type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTWorkbook$Factory.class */
    public static final class Factory {
        public static CTWorkbook newInstance() {
            return (CTWorkbook) POIXMLTypeLoader.newInstance(CTWorkbook.type, null);
        }

        public static CTWorkbook newInstance(XmlOptions xmlOptions) {
            return (CTWorkbook) POIXMLTypeLoader.newInstance(CTWorkbook.type, xmlOptions);
        }

        public static CTWorkbook parse(String str) throws XmlException {
            return (CTWorkbook) POIXMLTypeLoader.parse(str, CTWorkbook.type, (XmlOptions) null);
        }

        public static CTWorkbook parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTWorkbook) POIXMLTypeLoader.parse(str, CTWorkbook.type, xmlOptions);
        }

        public static CTWorkbook parse(File file) throws XmlException, IOException {
            return (CTWorkbook) POIXMLTypeLoader.parse(file, CTWorkbook.type, (XmlOptions) null);
        }

        public static CTWorkbook parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbook) POIXMLTypeLoader.parse(file, CTWorkbook.type, xmlOptions);
        }

        public static CTWorkbook parse(URL url) throws XmlException, IOException {
            return (CTWorkbook) POIXMLTypeLoader.parse(url, CTWorkbook.type, (XmlOptions) null);
        }

        public static CTWorkbook parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbook) POIXMLTypeLoader.parse(url, CTWorkbook.type, xmlOptions);
        }

        public static CTWorkbook parse(InputStream inputStream) throws XmlException, IOException {
            return (CTWorkbook) POIXMLTypeLoader.parse(inputStream, CTWorkbook.type, (XmlOptions) null);
        }

        public static CTWorkbook parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbook) POIXMLTypeLoader.parse(inputStream, CTWorkbook.type, xmlOptions);
        }

        public static CTWorkbook parse(Reader reader) throws XmlException, IOException {
            return (CTWorkbook) POIXMLTypeLoader.parse(reader, CTWorkbook.type, (XmlOptions) null);
        }

        public static CTWorkbook parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbook) POIXMLTypeLoader.parse(reader, CTWorkbook.type, xmlOptions);
        }

        public static CTWorkbook parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTWorkbook) POIXMLTypeLoader.parse(xMLStreamReader, CTWorkbook.type, (XmlOptions) null);
        }

        public static CTWorkbook parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTWorkbook) POIXMLTypeLoader.parse(xMLStreamReader, CTWorkbook.type, xmlOptions);
        }

        public static CTWorkbook parse(Node node) throws XmlException {
            return (CTWorkbook) POIXMLTypeLoader.parse(node, CTWorkbook.type, (XmlOptions) null);
        }

        public static CTWorkbook parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTWorkbook) POIXMLTypeLoader.parse(node, CTWorkbook.type, xmlOptions);
        }

        public static CTWorkbook parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTWorkbook) POIXMLTypeLoader.parse(xMLInputStream, CTWorkbook.type, (XmlOptions) null);
        }

        public static CTWorkbook parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTWorkbook) POIXMLTypeLoader.parse(xMLInputStream, CTWorkbook.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTWorkbook.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTWorkbook.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTFileVersion getFileVersion();

    boolean isSetFileVersion();

    void setFileVersion(CTFileVersion cTFileVersion);

    CTFileVersion addNewFileVersion();

    void unsetFileVersion();

    CTFileSharing getFileSharing();

    boolean isSetFileSharing();

    void setFileSharing(CTFileSharing cTFileSharing);

    CTFileSharing addNewFileSharing();

    void unsetFileSharing();

    CTWorkbookPr getWorkbookPr();

    boolean isSetWorkbookPr();

    void setWorkbookPr(CTWorkbookPr cTWorkbookPr);

    CTWorkbookPr addNewWorkbookPr();

    void unsetWorkbookPr();

    CTWorkbookProtection getWorkbookProtection();

    boolean isSetWorkbookProtection();

    void setWorkbookProtection(CTWorkbookProtection cTWorkbookProtection);

    CTWorkbookProtection addNewWorkbookProtection();

    void unsetWorkbookProtection();

    CTBookViews getBookViews();

    boolean isSetBookViews();

    void setBookViews(CTBookViews cTBookViews);

    CTBookViews addNewBookViews();

    void unsetBookViews();

    CTSheets getSheets();

    void setSheets(CTSheets cTSheets);

    CTSheets addNewSheets();

    CTFunctionGroups getFunctionGroups();

    boolean isSetFunctionGroups();

    void setFunctionGroups(CTFunctionGroups cTFunctionGroups);

    CTFunctionGroups addNewFunctionGroups();

    void unsetFunctionGroups();

    CTExternalReferences getExternalReferences();

    boolean isSetExternalReferences();

    void setExternalReferences(CTExternalReferences cTExternalReferences);

    CTExternalReferences addNewExternalReferences();

    void unsetExternalReferences();

    CTDefinedNames getDefinedNames();

    boolean isSetDefinedNames();

    void setDefinedNames(CTDefinedNames cTDefinedNames);

    CTDefinedNames addNewDefinedNames();

    void unsetDefinedNames();

    CTCalcPr getCalcPr();

    boolean isSetCalcPr();

    void setCalcPr(CTCalcPr cTCalcPr);

    CTCalcPr addNewCalcPr();

    void unsetCalcPr();

    CTOleSize getOleSize();

    boolean isSetOleSize();

    void setOleSize(CTOleSize cTOleSize);

    CTOleSize addNewOleSize();

    void unsetOleSize();

    CTCustomWorkbookViews getCustomWorkbookViews();

    boolean isSetCustomWorkbookViews();

    void setCustomWorkbookViews(CTCustomWorkbookViews cTCustomWorkbookViews);

    CTCustomWorkbookViews addNewCustomWorkbookViews();

    void unsetCustomWorkbookViews();

    CTPivotCaches getPivotCaches();

    boolean isSetPivotCaches();

    void setPivotCaches(CTPivotCaches cTPivotCaches);

    CTPivotCaches addNewPivotCaches();

    void unsetPivotCaches();

    CTSmartTagPr getSmartTagPr();

    boolean isSetSmartTagPr();

    void setSmartTagPr(CTSmartTagPr cTSmartTagPr);

    CTSmartTagPr addNewSmartTagPr();

    void unsetSmartTagPr();

    CTSmartTagTypes getSmartTagTypes();

    boolean isSetSmartTagTypes();

    void setSmartTagTypes(CTSmartTagTypes cTSmartTagTypes);

    CTSmartTagTypes addNewSmartTagTypes();

    void unsetSmartTagTypes();

    CTWebPublishing getWebPublishing();

    boolean isSetWebPublishing();

    void setWebPublishing(CTWebPublishing cTWebPublishing);

    CTWebPublishing addNewWebPublishing();

    void unsetWebPublishing();

    List<CTFileRecoveryPr> getFileRecoveryPrList();

    CTFileRecoveryPr[] getFileRecoveryPrArray();

    CTFileRecoveryPr getFileRecoveryPrArray(int i);

    int sizeOfFileRecoveryPrArray();

    void setFileRecoveryPrArray(CTFileRecoveryPr[] cTFileRecoveryPrArr);

    void setFileRecoveryPrArray(int i, CTFileRecoveryPr cTFileRecoveryPr);

    CTFileRecoveryPr insertNewFileRecoveryPr(int i);

    CTFileRecoveryPr addNewFileRecoveryPr();

    void removeFileRecoveryPr(int i);

    CTWebPublishObjects getWebPublishObjects();

    boolean isSetWebPublishObjects();

    void setWebPublishObjects(CTWebPublishObjects cTWebPublishObjects);

    CTWebPublishObjects addNewWebPublishObjects();

    void unsetWebPublishObjects();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
