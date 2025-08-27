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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTChartsheet.class */
public interface CTChartsheet extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTChartsheet.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctchartsheetf68atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTChartsheet$Factory.class */
    public static final class Factory {
        public static CTChartsheet newInstance() {
            return (CTChartsheet) POIXMLTypeLoader.newInstance(CTChartsheet.type, null);
        }

        public static CTChartsheet newInstance(XmlOptions xmlOptions) {
            return (CTChartsheet) POIXMLTypeLoader.newInstance(CTChartsheet.type, xmlOptions);
        }

        public static CTChartsheet parse(String str) throws XmlException {
            return (CTChartsheet) POIXMLTypeLoader.parse(str, CTChartsheet.type, (XmlOptions) null);
        }

        public static CTChartsheet parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTChartsheet) POIXMLTypeLoader.parse(str, CTChartsheet.type, xmlOptions);
        }

        public static CTChartsheet parse(File file) throws XmlException, IOException {
            return (CTChartsheet) POIXMLTypeLoader.parse(file, CTChartsheet.type, (XmlOptions) null);
        }

        public static CTChartsheet parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChartsheet) POIXMLTypeLoader.parse(file, CTChartsheet.type, xmlOptions);
        }

        public static CTChartsheet parse(URL url) throws XmlException, IOException {
            return (CTChartsheet) POIXMLTypeLoader.parse(url, CTChartsheet.type, (XmlOptions) null);
        }

        public static CTChartsheet parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChartsheet) POIXMLTypeLoader.parse(url, CTChartsheet.type, xmlOptions);
        }

        public static CTChartsheet parse(InputStream inputStream) throws XmlException, IOException {
            return (CTChartsheet) POIXMLTypeLoader.parse(inputStream, CTChartsheet.type, (XmlOptions) null);
        }

        public static CTChartsheet parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChartsheet) POIXMLTypeLoader.parse(inputStream, CTChartsheet.type, xmlOptions);
        }

        public static CTChartsheet parse(Reader reader) throws XmlException, IOException {
            return (CTChartsheet) POIXMLTypeLoader.parse(reader, CTChartsheet.type, (XmlOptions) null);
        }

        public static CTChartsheet parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTChartsheet) POIXMLTypeLoader.parse(reader, CTChartsheet.type, xmlOptions);
        }

        public static CTChartsheet parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTChartsheet) POIXMLTypeLoader.parse(xMLStreamReader, CTChartsheet.type, (XmlOptions) null);
        }

        public static CTChartsheet parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTChartsheet) POIXMLTypeLoader.parse(xMLStreamReader, CTChartsheet.type, xmlOptions);
        }

        public static CTChartsheet parse(Node node) throws XmlException {
            return (CTChartsheet) POIXMLTypeLoader.parse(node, CTChartsheet.type, (XmlOptions) null);
        }

        public static CTChartsheet parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTChartsheet) POIXMLTypeLoader.parse(node, CTChartsheet.type, xmlOptions);
        }

        public static CTChartsheet parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTChartsheet) POIXMLTypeLoader.parse(xMLInputStream, CTChartsheet.type, (XmlOptions) null);
        }

        public static CTChartsheet parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTChartsheet) POIXMLTypeLoader.parse(xMLInputStream, CTChartsheet.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTChartsheet.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTChartsheet.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTChartsheetPr getSheetPr();

    boolean isSetSheetPr();

    void setSheetPr(CTChartsheetPr cTChartsheetPr);

    CTChartsheetPr addNewSheetPr();

    void unsetSheetPr();

    CTChartsheetViews getSheetViews();

    void setSheetViews(CTChartsheetViews cTChartsheetViews);

    CTChartsheetViews addNewSheetViews();

    CTChartsheetProtection getSheetProtection();

    boolean isSetSheetProtection();

    void setSheetProtection(CTChartsheetProtection cTChartsheetProtection);

    CTChartsheetProtection addNewSheetProtection();

    void unsetSheetProtection();

    CTCustomChartsheetViews getCustomSheetViews();

    boolean isSetCustomSheetViews();

    void setCustomSheetViews(CTCustomChartsheetViews cTCustomChartsheetViews);

    CTCustomChartsheetViews addNewCustomSheetViews();

    void unsetCustomSheetViews();

    CTPageMargins getPageMargins();

    boolean isSetPageMargins();

    void setPageMargins(CTPageMargins cTPageMargins);

    CTPageMargins addNewPageMargins();

    void unsetPageMargins();

    CTCsPageSetup getPageSetup();

    boolean isSetPageSetup();

    void setPageSetup(CTCsPageSetup cTCsPageSetup);

    CTCsPageSetup addNewPageSetup();

    void unsetPageSetup();

    CTHeaderFooter getHeaderFooter();

    boolean isSetHeaderFooter();

    void setHeaderFooter(CTHeaderFooter cTHeaderFooter);

    CTHeaderFooter addNewHeaderFooter();

    void unsetHeaderFooter();

    CTDrawing getDrawing();

    void setDrawing(CTDrawing cTDrawing);

    CTDrawing addNewDrawing();

    CTLegacyDrawing getLegacyDrawing();

    boolean isSetLegacyDrawing();

    void setLegacyDrawing(CTLegacyDrawing cTLegacyDrawing);

    CTLegacyDrawing addNewLegacyDrawing();

    void unsetLegacyDrawing();

    CTLegacyDrawing getLegacyDrawingHF();

    boolean isSetLegacyDrawingHF();

    void setLegacyDrawingHF(CTLegacyDrawing cTLegacyDrawing);

    CTLegacyDrawing addNewLegacyDrawingHF();

    void unsetLegacyDrawingHF();

    CTSheetBackgroundPicture getPicture();

    boolean isSetPicture();

    void setPicture(CTSheetBackgroundPicture cTSheetBackgroundPicture);

    CTSheetBackgroundPicture addNewPicture();

    void unsetPicture();

    CTWebPublishItems getWebPublishItems();

    boolean isSetWebPublishItems();

    void setWebPublishItems(CTWebPublishItems cTWebPublishItems);

    CTWebPublishItems addNewWebPublishItems();

    void unsetWebPublishItems();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
