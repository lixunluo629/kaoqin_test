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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDialogsheet.class */
public interface CTDialogsheet extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTDialogsheet.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctdialogsheet6f36type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDialogsheet$Factory.class */
    public static final class Factory {
        public static CTDialogsheet newInstance() {
            return (CTDialogsheet) POIXMLTypeLoader.newInstance(CTDialogsheet.type, null);
        }

        public static CTDialogsheet newInstance(XmlOptions xmlOptions) {
            return (CTDialogsheet) POIXMLTypeLoader.newInstance(CTDialogsheet.type, xmlOptions);
        }

        public static CTDialogsheet parse(String str) throws XmlException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(str, CTDialogsheet.type, (XmlOptions) null);
        }

        public static CTDialogsheet parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(str, CTDialogsheet.type, xmlOptions);
        }

        public static CTDialogsheet parse(File file) throws XmlException, IOException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(file, CTDialogsheet.type, (XmlOptions) null);
        }

        public static CTDialogsheet parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(file, CTDialogsheet.type, xmlOptions);
        }

        public static CTDialogsheet parse(URL url) throws XmlException, IOException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(url, CTDialogsheet.type, (XmlOptions) null);
        }

        public static CTDialogsheet parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(url, CTDialogsheet.type, xmlOptions);
        }

        public static CTDialogsheet parse(InputStream inputStream) throws XmlException, IOException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(inputStream, CTDialogsheet.type, (XmlOptions) null);
        }

        public static CTDialogsheet parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(inputStream, CTDialogsheet.type, xmlOptions);
        }

        public static CTDialogsheet parse(Reader reader) throws XmlException, IOException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(reader, CTDialogsheet.type, (XmlOptions) null);
        }

        public static CTDialogsheet parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(reader, CTDialogsheet.type, xmlOptions);
        }

        public static CTDialogsheet parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(xMLStreamReader, CTDialogsheet.type, (XmlOptions) null);
        }

        public static CTDialogsheet parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(xMLStreamReader, CTDialogsheet.type, xmlOptions);
        }

        public static CTDialogsheet parse(Node node) throws XmlException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(node, CTDialogsheet.type, (XmlOptions) null);
        }

        public static CTDialogsheet parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(node, CTDialogsheet.type, xmlOptions);
        }

        public static CTDialogsheet parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(xMLInputStream, CTDialogsheet.type, (XmlOptions) null);
        }

        public static CTDialogsheet parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTDialogsheet) POIXMLTypeLoader.parse(xMLInputStream, CTDialogsheet.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDialogsheet.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDialogsheet.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTSheetPr getSheetPr();

    boolean isSetSheetPr();

    void setSheetPr(CTSheetPr cTSheetPr);

    CTSheetPr addNewSheetPr();

    void unsetSheetPr();

    CTSheetViews getSheetViews();

    boolean isSetSheetViews();

    void setSheetViews(CTSheetViews cTSheetViews);

    CTSheetViews addNewSheetViews();

    void unsetSheetViews();

    CTSheetFormatPr getSheetFormatPr();

    boolean isSetSheetFormatPr();

    void setSheetFormatPr(CTSheetFormatPr cTSheetFormatPr);

    CTSheetFormatPr addNewSheetFormatPr();

    void unsetSheetFormatPr();

    CTSheetProtection getSheetProtection();

    boolean isSetSheetProtection();

    void setSheetProtection(CTSheetProtection cTSheetProtection);

    CTSheetProtection addNewSheetProtection();

    void unsetSheetProtection();

    CTCustomSheetViews getCustomSheetViews();

    boolean isSetCustomSheetViews();

    void setCustomSheetViews(CTCustomSheetViews cTCustomSheetViews);

    CTCustomSheetViews addNewCustomSheetViews();

    void unsetCustomSheetViews();

    CTPrintOptions getPrintOptions();

    boolean isSetPrintOptions();

    void setPrintOptions(CTPrintOptions cTPrintOptions);

    CTPrintOptions addNewPrintOptions();

    void unsetPrintOptions();

    CTPageMargins getPageMargins();

    boolean isSetPageMargins();

    void setPageMargins(CTPageMargins cTPageMargins);

    CTPageMargins addNewPageMargins();

    void unsetPageMargins();

    CTPageSetup getPageSetup();

    boolean isSetPageSetup();

    void setPageSetup(CTPageSetup cTPageSetup);

    CTPageSetup addNewPageSetup();

    void unsetPageSetup();

    CTHeaderFooter getHeaderFooter();

    boolean isSetHeaderFooter();

    void setHeaderFooter(CTHeaderFooter cTHeaderFooter);

    CTHeaderFooter addNewHeaderFooter();

    void unsetHeaderFooter();

    CTDrawing getDrawing();

    boolean isSetDrawing();

    void setDrawing(CTDrawing cTDrawing);

    CTDrawing addNewDrawing();

    void unsetDrawing();

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

    CTOleObjects getOleObjects();

    boolean isSetOleObjects();

    void setOleObjects(CTOleObjects cTOleObjects);

    CTOleObjects addNewOleObjects();

    void unsetOleObjects();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
