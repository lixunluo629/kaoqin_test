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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheetPr.class */
public interface CTSheetPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSheetPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsheetpr3ae0type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheetPr$Factory.class */
    public static final class Factory {
        public static CTSheetPr newInstance() {
            return (CTSheetPr) POIXMLTypeLoader.newInstance(CTSheetPr.type, null);
        }

        public static CTSheetPr newInstance(XmlOptions xmlOptions) {
            return (CTSheetPr) POIXMLTypeLoader.newInstance(CTSheetPr.type, xmlOptions);
        }

        public static CTSheetPr parse(String str) throws XmlException {
            return (CTSheetPr) POIXMLTypeLoader.parse(str, CTSheetPr.type, (XmlOptions) null);
        }

        public static CTSheetPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetPr) POIXMLTypeLoader.parse(str, CTSheetPr.type, xmlOptions);
        }

        public static CTSheetPr parse(File file) throws XmlException, IOException {
            return (CTSheetPr) POIXMLTypeLoader.parse(file, CTSheetPr.type, (XmlOptions) null);
        }

        public static CTSheetPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetPr) POIXMLTypeLoader.parse(file, CTSheetPr.type, xmlOptions);
        }

        public static CTSheetPr parse(URL url) throws XmlException, IOException {
            return (CTSheetPr) POIXMLTypeLoader.parse(url, CTSheetPr.type, (XmlOptions) null);
        }

        public static CTSheetPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetPr) POIXMLTypeLoader.parse(url, CTSheetPr.type, xmlOptions);
        }

        public static CTSheetPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSheetPr) POIXMLTypeLoader.parse(inputStream, CTSheetPr.type, (XmlOptions) null);
        }

        public static CTSheetPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetPr) POIXMLTypeLoader.parse(inputStream, CTSheetPr.type, xmlOptions);
        }

        public static CTSheetPr parse(Reader reader) throws XmlException, IOException {
            return (CTSheetPr) POIXMLTypeLoader.parse(reader, CTSheetPr.type, (XmlOptions) null);
        }

        public static CTSheetPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetPr) POIXMLTypeLoader.parse(reader, CTSheetPr.type, xmlOptions);
        }

        public static CTSheetPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSheetPr) POIXMLTypeLoader.parse(xMLStreamReader, CTSheetPr.type, (XmlOptions) null);
        }

        public static CTSheetPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetPr) POIXMLTypeLoader.parse(xMLStreamReader, CTSheetPr.type, xmlOptions);
        }

        public static CTSheetPr parse(Node node) throws XmlException {
            return (CTSheetPr) POIXMLTypeLoader.parse(node, CTSheetPr.type, (XmlOptions) null);
        }

        public static CTSheetPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetPr) POIXMLTypeLoader.parse(node, CTSheetPr.type, xmlOptions);
        }

        public static CTSheetPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSheetPr) POIXMLTypeLoader.parse(xMLInputStream, CTSheetPr.type, (XmlOptions) null);
        }

        public static CTSheetPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSheetPr) POIXMLTypeLoader.parse(xMLInputStream, CTSheetPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheetPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheetPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTColor getTabColor();

    boolean isSetTabColor();

    void setTabColor(CTColor cTColor);

    CTColor addNewTabColor();

    void unsetTabColor();

    CTOutlinePr getOutlinePr();

    boolean isSetOutlinePr();

    void setOutlinePr(CTOutlinePr cTOutlinePr);

    CTOutlinePr addNewOutlinePr();

    void unsetOutlinePr();

    CTPageSetUpPr getPageSetUpPr();

    boolean isSetPageSetUpPr();

    void setPageSetUpPr(CTPageSetUpPr cTPageSetUpPr);

    CTPageSetUpPr addNewPageSetUpPr();

    void unsetPageSetUpPr();

    boolean getSyncHorizontal();

    XmlBoolean xgetSyncHorizontal();

    boolean isSetSyncHorizontal();

    void setSyncHorizontal(boolean z);

    void xsetSyncHorizontal(XmlBoolean xmlBoolean);

    void unsetSyncHorizontal();

    boolean getSyncVertical();

    XmlBoolean xgetSyncVertical();

    boolean isSetSyncVertical();

    void setSyncVertical(boolean z);

    void xsetSyncVertical(XmlBoolean xmlBoolean);

    void unsetSyncVertical();

    String getSyncRef();

    STRef xgetSyncRef();

    boolean isSetSyncRef();

    void setSyncRef(String str);

    void xsetSyncRef(STRef sTRef);

    void unsetSyncRef();

    boolean getTransitionEvaluation();

    XmlBoolean xgetTransitionEvaluation();

    boolean isSetTransitionEvaluation();

    void setTransitionEvaluation(boolean z);

    void xsetTransitionEvaluation(XmlBoolean xmlBoolean);

    void unsetTransitionEvaluation();

    boolean getTransitionEntry();

    XmlBoolean xgetTransitionEntry();

    boolean isSetTransitionEntry();

    void setTransitionEntry(boolean z);

    void xsetTransitionEntry(XmlBoolean xmlBoolean);

    void unsetTransitionEntry();

    boolean getPublished();

    XmlBoolean xgetPublished();

    boolean isSetPublished();

    void setPublished(boolean z);

    void xsetPublished(XmlBoolean xmlBoolean);

    void unsetPublished();

    String getCodeName();

    XmlString xgetCodeName();

    boolean isSetCodeName();

    void setCodeName(String str);

    void xsetCodeName(XmlString xmlString);

    void unsetCodeName();

    boolean getFilterMode();

    XmlBoolean xgetFilterMode();

    boolean isSetFilterMode();

    void setFilterMode(boolean z);

    void xsetFilterMode(XmlBoolean xmlBoolean);

    void unsetFilterMode();

    boolean getEnableFormatConditionsCalculation();

    XmlBoolean xgetEnableFormatConditionsCalculation();

    boolean isSetEnableFormatConditionsCalculation();

    void setEnableFormatConditionsCalculation(boolean z);

    void xsetEnableFormatConditionsCalculation(XmlBoolean xmlBoolean);

    void unsetEnableFormatConditionsCalculation();
}
