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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTWorkbookPr.class */
public interface CTWorkbookPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTWorkbookPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctworkbookpr03a5type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTWorkbookPr$Factory.class */
    public static final class Factory {
        public static CTWorkbookPr newInstance() {
            return (CTWorkbookPr) POIXMLTypeLoader.newInstance(CTWorkbookPr.type, null);
        }

        public static CTWorkbookPr newInstance(XmlOptions xmlOptions) {
            return (CTWorkbookPr) POIXMLTypeLoader.newInstance(CTWorkbookPr.type, xmlOptions);
        }

        public static CTWorkbookPr parse(String str) throws XmlException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(str, CTWorkbookPr.type, (XmlOptions) null);
        }

        public static CTWorkbookPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(str, CTWorkbookPr.type, xmlOptions);
        }

        public static CTWorkbookPr parse(File file) throws XmlException, IOException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(file, CTWorkbookPr.type, (XmlOptions) null);
        }

        public static CTWorkbookPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(file, CTWorkbookPr.type, xmlOptions);
        }

        public static CTWorkbookPr parse(URL url) throws XmlException, IOException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(url, CTWorkbookPr.type, (XmlOptions) null);
        }

        public static CTWorkbookPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(url, CTWorkbookPr.type, xmlOptions);
        }

        public static CTWorkbookPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(inputStream, CTWorkbookPr.type, (XmlOptions) null);
        }

        public static CTWorkbookPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(inputStream, CTWorkbookPr.type, xmlOptions);
        }

        public static CTWorkbookPr parse(Reader reader) throws XmlException, IOException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(reader, CTWorkbookPr.type, (XmlOptions) null);
        }

        public static CTWorkbookPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(reader, CTWorkbookPr.type, xmlOptions);
        }

        public static CTWorkbookPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(xMLStreamReader, CTWorkbookPr.type, (XmlOptions) null);
        }

        public static CTWorkbookPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(xMLStreamReader, CTWorkbookPr.type, xmlOptions);
        }

        public static CTWorkbookPr parse(Node node) throws XmlException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(node, CTWorkbookPr.type, (XmlOptions) null);
        }

        public static CTWorkbookPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(node, CTWorkbookPr.type, xmlOptions);
        }

        public static CTWorkbookPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(xMLInputStream, CTWorkbookPr.type, (XmlOptions) null);
        }

        public static CTWorkbookPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTWorkbookPr) POIXMLTypeLoader.parse(xMLInputStream, CTWorkbookPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTWorkbookPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTWorkbookPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    boolean getDate1904();

    XmlBoolean xgetDate1904();

    boolean isSetDate1904();

    void setDate1904(boolean z);

    void xsetDate1904(XmlBoolean xmlBoolean);

    void unsetDate1904();

    STObjects$Enum getShowObjects();

    STObjects xgetShowObjects();

    boolean isSetShowObjects();

    void setShowObjects(STObjects$Enum sTObjects$Enum);

    void xsetShowObjects(STObjects sTObjects);

    void unsetShowObjects();

    boolean getShowBorderUnselectedTables();

    XmlBoolean xgetShowBorderUnselectedTables();

    boolean isSetShowBorderUnselectedTables();

    void setShowBorderUnselectedTables(boolean z);

    void xsetShowBorderUnselectedTables(XmlBoolean xmlBoolean);

    void unsetShowBorderUnselectedTables();

    boolean getFilterPrivacy();

    XmlBoolean xgetFilterPrivacy();

    boolean isSetFilterPrivacy();

    void setFilterPrivacy(boolean z);

    void xsetFilterPrivacy(XmlBoolean xmlBoolean);

    void unsetFilterPrivacy();

    boolean getPromptedSolutions();

    XmlBoolean xgetPromptedSolutions();

    boolean isSetPromptedSolutions();

    void setPromptedSolutions(boolean z);

    void xsetPromptedSolutions(XmlBoolean xmlBoolean);

    void unsetPromptedSolutions();

    boolean getShowInkAnnotation();

    XmlBoolean xgetShowInkAnnotation();

    boolean isSetShowInkAnnotation();

    void setShowInkAnnotation(boolean z);

    void xsetShowInkAnnotation(XmlBoolean xmlBoolean);

    void unsetShowInkAnnotation();

    boolean getBackupFile();

    XmlBoolean xgetBackupFile();

    boolean isSetBackupFile();

    void setBackupFile(boolean z);

    void xsetBackupFile(XmlBoolean xmlBoolean);

    void unsetBackupFile();

    boolean getSaveExternalLinkValues();

    XmlBoolean xgetSaveExternalLinkValues();

    boolean isSetSaveExternalLinkValues();

    void setSaveExternalLinkValues(boolean z);

    void xsetSaveExternalLinkValues(XmlBoolean xmlBoolean);

    void unsetSaveExternalLinkValues();

    STUpdateLinks$Enum getUpdateLinks();

    STUpdateLinks xgetUpdateLinks();

    boolean isSetUpdateLinks();

    void setUpdateLinks(STUpdateLinks$Enum sTUpdateLinks$Enum);

    void xsetUpdateLinks(STUpdateLinks sTUpdateLinks);

    void unsetUpdateLinks();

    String getCodeName();

    XmlString xgetCodeName();

    boolean isSetCodeName();

    void setCodeName(String str);

    void xsetCodeName(XmlString xmlString);

    void unsetCodeName();

    boolean getHidePivotFieldList();

    XmlBoolean xgetHidePivotFieldList();

    boolean isSetHidePivotFieldList();

    void setHidePivotFieldList(boolean z);

    void xsetHidePivotFieldList(XmlBoolean xmlBoolean);

    void unsetHidePivotFieldList();

    boolean getShowPivotChartFilter();

    XmlBoolean xgetShowPivotChartFilter();

    boolean isSetShowPivotChartFilter();

    void setShowPivotChartFilter(boolean z);

    void xsetShowPivotChartFilter(XmlBoolean xmlBoolean);

    void unsetShowPivotChartFilter();

    boolean getAllowRefreshQuery();

    XmlBoolean xgetAllowRefreshQuery();

    boolean isSetAllowRefreshQuery();

    void setAllowRefreshQuery(boolean z);

    void xsetAllowRefreshQuery(XmlBoolean xmlBoolean);

    void unsetAllowRefreshQuery();

    boolean getPublishItems();

    XmlBoolean xgetPublishItems();

    boolean isSetPublishItems();

    void setPublishItems(boolean z);

    void xsetPublishItems(XmlBoolean xmlBoolean);

    void unsetPublishItems();

    boolean getCheckCompatibility();

    XmlBoolean xgetCheckCompatibility();

    boolean isSetCheckCompatibility();

    void setCheckCompatibility(boolean z);

    void xsetCheckCompatibility(XmlBoolean xmlBoolean);

    void unsetCheckCompatibility();

    boolean getAutoCompressPictures();

    XmlBoolean xgetAutoCompressPictures();

    boolean isSetAutoCompressPictures();

    void setAutoCompressPictures(boolean z);

    void xsetAutoCompressPictures(XmlBoolean xmlBoolean);

    void unsetAutoCompressPictures();

    boolean getRefreshAllConnections();

    XmlBoolean xgetRefreshAllConnections();

    boolean isSetRefreshAllConnections();

    void setRefreshAllConnections(boolean z);

    void xsetRefreshAllConnections(XmlBoolean xmlBoolean);

    void unsetRefreshAllConnections();

    long getDefaultThemeVersion();

    XmlUnsignedInt xgetDefaultThemeVersion();

    boolean isSetDefaultThemeVersion();

    void setDefaultThemeVersion(long j);

    void xsetDefaultThemeVersion(XmlUnsignedInt xmlUnsignedInt);

    void unsetDefaultThemeVersion();
}
