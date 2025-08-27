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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheetView.class */
public interface CTSheetView extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSheetView.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsheetview0f43type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheetView$Factory.class */
    public static final class Factory {
        public static CTSheetView newInstance() {
            return (CTSheetView) POIXMLTypeLoader.newInstance(CTSheetView.type, null);
        }

        public static CTSheetView newInstance(XmlOptions xmlOptions) {
            return (CTSheetView) POIXMLTypeLoader.newInstance(CTSheetView.type, xmlOptions);
        }

        public static CTSheetView parse(String str) throws XmlException {
            return (CTSheetView) POIXMLTypeLoader.parse(str, CTSheetView.type, (XmlOptions) null);
        }

        public static CTSheetView parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetView) POIXMLTypeLoader.parse(str, CTSheetView.type, xmlOptions);
        }

        public static CTSheetView parse(File file) throws XmlException, IOException {
            return (CTSheetView) POIXMLTypeLoader.parse(file, CTSheetView.type, (XmlOptions) null);
        }

        public static CTSheetView parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetView) POIXMLTypeLoader.parse(file, CTSheetView.type, xmlOptions);
        }

        public static CTSheetView parse(URL url) throws XmlException, IOException {
            return (CTSheetView) POIXMLTypeLoader.parse(url, CTSheetView.type, (XmlOptions) null);
        }

        public static CTSheetView parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetView) POIXMLTypeLoader.parse(url, CTSheetView.type, xmlOptions);
        }

        public static CTSheetView parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSheetView) POIXMLTypeLoader.parse(inputStream, CTSheetView.type, (XmlOptions) null);
        }

        public static CTSheetView parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetView) POIXMLTypeLoader.parse(inputStream, CTSheetView.type, xmlOptions);
        }

        public static CTSheetView parse(Reader reader) throws XmlException, IOException {
            return (CTSheetView) POIXMLTypeLoader.parse(reader, CTSheetView.type, (XmlOptions) null);
        }

        public static CTSheetView parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetView) POIXMLTypeLoader.parse(reader, CTSheetView.type, xmlOptions);
        }

        public static CTSheetView parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSheetView) POIXMLTypeLoader.parse(xMLStreamReader, CTSheetView.type, (XmlOptions) null);
        }

        public static CTSheetView parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetView) POIXMLTypeLoader.parse(xMLStreamReader, CTSheetView.type, xmlOptions);
        }

        public static CTSheetView parse(Node node) throws XmlException {
            return (CTSheetView) POIXMLTypeLoader.parse(node, CTSheetView.type, (XmlOptions) null);
        }

        public static CTSheetView parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetView) POIXMLTypeLoader.parse(node, CTSheetView.type, xmlOptions);
        }

        public static CTSheetView parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSheetView) POIXMLTypeLoader.parse(xMLInputStream, CTSheetView.type, (XmlOptions) null);
        }

        public static CTSheetView parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSheetView) POIXMLTypeLoader.parse(xMLInputStream, CTSheetView.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheetView.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheetView.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPane getPane();

    boolean isSetPane();

    void setPane(CTPane cTPane);

    CTPane addNewPane();

    void unsetPane();

    List<CTSelection> getSelectionList();

    CTSelection[] getSelectionArray();

    CTSelection getSelectionArray(int i);

    int sizeOfSelectionArray();

    void setSelectionArray(CTSelection[] cTSelectionArr);

    void setSelectionArray(int i, CTSelection cTSelection);

    CTSelection insertNewSelection(int i);

    CTSelection addNewSelection();

    void removeSelection(int i);

    List<CTPivotSelection> getPivotSelectionList();

    CTPivotSelection[] getPivotSelectionArray();

    CTPivotSelection getPivotSelectionArray(int i);

    int sizeOfPivotSelectionArray();

    void setPivotSelectionArray(CTPivotSelection[] cTPivotSelectionArr);

    void setPivotSelectionArray(int i, CTPivotSelection cTPivotSelection);

    CTPivotSelection insertNewPivotSelection(int i);

    CTPivotSelection addNewPivotSelection();

    void removePivotSelection(int i);

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    boolean getWindowProtection();

    XmlBoolean xgetWindowProtection();

    boolean isSetWindowProtection();

    void setWindowProtection(boolean z);

    void xsetWindowProtection(XmlBoolean xmlBoolean);

    void unsetWindowProtection();

    boolean getShowFormulas();

    XmlBoolean xgetShowFormulas();

    boolean isSetShowFormulas();

    void setShowFormulas(boolean z);

    void xsetShowFormulas(XmlBoolean xmlBoolean);

    void unsetShowFormulas();

    boolean getShowGridLines();

    XmlBoolean xgetShowGridLines();

    boolean isSetShowGridLines();

    void setShowGridLines(boolean z);

    void xsetShowGridLines(XmlBoolean xmlBoolean);

    void unsetShowGridLines();

    boolean getShowRowColHeaders();

    XmlBoolean xgetShowRowColHeaders();

    boolean isSetShowRowColHeaders();

    void setShowRowColHeaders(boolean z);

    void xsetShowRowColHeaders(XmlBoolean xmlBoolean);

    void unsetShowRowColHeaders();

    boolean getShowZeros();

    XmlBoolean xgetShowZeros();

    boolean isSetShowZeros();

    void setShowZeros(boolean z);

    void xsetShowZeros(XmlBoolean xmlBoolean);

    void unsetShowZeros();

    boolean getRightToLeft();

    XmlBoolean xgetRightToLeft();

    boolean isSetRightToLeft();

    void setRightToLeft(boolean z);

    void xsetRightToLeft(XmlBoolean xmlBoolean);

    void unsetRightToLeft();

    boolean getTabSelected();

    XmlBoolean xgetTabSelected();

    boolean isSetTabSelected();

    void setTabSelected(boolean z);

    void xsetTabSelected(XmlBoolean xmlBoolean);

    void unsetTabSelected();

    boolean getShowRuler();

    XmlBoolean xgetShowRuler();

    boolean isSetShowRuler();

    void setShowRuler(boolean z);

    void xsetShowRuler(XmlBoolean xmlBoolean);

    void unsetShowRuler();

    boolean getShowOutlineSymbols();

    XmlBoolean xgetShowOutlineSymbols();

    boolean isSetShowOutlineSymbols();

    void setShowOutlineSymbols(boolean z);

    void xsetShowOutlineSymbols(XmlBoolean xmlBoolean);

    void unsetShowOutlineSymbols();

    boolean getDefaultGridColor();

    XmlBoolean xgetDefaultGridColor();

    boolean isSetDefaultGridColor();

    void setDefaultGridColor(boolean z);

    void xsetDefaultGridColor(XmlBoolean xmlBoolean);

    void unsetDefaultGridColor();

    boolean getShowWhiteSpace();

    XmlBoolean xgetShowWhiteSpace();

    boolean isSetShowWhiteSpace();

    void setShowWhiteSpace(boolean z);

    void xsetShowWhiteSpace(XmlBoolean xmlBoolean);

    void unsetShowWhiteSpace();

    STSheetViewType$Enum getView();

    STSheetViewType xgetView();

    boolean isSetView();

    void setView(STSheetViewType$Enum sTSheetViewType$Enum);

    void xsetView(STSheetViewType sTSheetViewType);

    void unsetView();

    String getTopLeftCell();

    STCellRef xgetTopLeftCell();

    boolean isSetTopLeftCell();

    void setTopLeftCell(String str);

    void xsetTopLeftCell(STCellRef sTCellRef);

    void unsetTopLeftCell();

    long getColorId();

    XmlUnsignedInt xgetColorId();

    boolean isSetColorId();

    void setColorId(long j);

    void xsetColorId(XmlUnsignedInt xmlUnsignedInt);

    void unsetColorId();

    long getZoomScale();

    XmlUnsignedInt xgetZoomScale();

    boolean isSetZoomScale();

    void setZoomScale(long j);

    void xsetZoomScale(XmlUnsignedInt xmlUnsignedInt);

    void unsetZoomScale();

    long getZoomScaleNormal();

    XmlUnsignedInt xgetZoomScaleNormal();

    boolean isSetZoomScaleNormal();

    void setZoomScaleNormal(long j);

    void xsetZoomScaleNormal(XmlUnsignedInt xmlUnsignedInt);

    void unsetZoomScaleNormal();

    long getZoomScaleSheetLayoutView();

    XmlUnsignedInt xgetZoomScaleSheetLayoutView();

    boolean isSetZoomScaleSheetLayoutView();

    void setZoomScaleSheetLayoutView(long j);

    void xsetZoomScaleSheetLayoutView(XmlUnsignedInt xmlUnsignedInt);

    void unsetZoomScaleSheetLayoutView();

    long getZoomScalePageLayoutView();

    XmlUnsignedInt xgetZoomScalePageLayoutView();

    boolean isSetZoomScalePageLayoutView();

    void setZoomScalePageLayoutView(long j);

    void xsetZoomScalePageLayoutView(XmlUnsignedInt xmlUnsignedInt);

    void unsetZoomScalePageLayoutView();

    long getWorkbookViewId();

    XmlUnsignedInt xgetWorkbookViewId();

    void setWorkbookViewId(long j);

    void xsetWorkbookViewId(XmlUnsignedInt xmlUnsignedInt);
}
