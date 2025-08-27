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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTBookView.class */
public interface CTBookView extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBookView.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctbookviewf677type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTBookView$Factory.class */
    public static final class Factory {
        public static CTBookView newInstance() {
            return (CTBookView) POIXMLTypeLoader.newInstance(CTBookView.type, null);
        }

        public static CTBookView newInstance(XmlOptions xmlOptions) {
            return (CTBookView) POIXMLTypeLoader.newInstance(CTBookView.type, xmlOptions);
        }

        public static CTBookView parse(String str) throws XmlException {
            return (CTBookView) POIXMLTypeLoader.parse(str, CTBookView.type, (XmlOptions) null);
        }

        public static CTBookView parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBookView) POIXMLTypeLoader.parse(str, CTBookView.type, xmlOptions);
        }

        public static CTBookView parse(File file) throws XmlException, IOException {
            return (CTBookView) POIXMLTypeLoader.parse(file, CTBookView.type, (XmlOptions) null);
        }

        public static CTBookView parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBookView) POIXMLTypeLoader.parse(file, CTBookView.type, xmlOptions);
        }

        public static CTBookView parse(URL url) throws XmlException, IOException {
            return (CTBookView) POIXMLTypeLoader.parse(url, CTBookView.type, (XmlOptions) null);
        }

        public static CTBookView parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBookView) POIXMLTypeLoader.parse(url, CTBookView.type, xmlOptions);
        }

        public static CTBookView parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBookView) POIXMLTypeLoader.parse(inputStream, CTBookView.type, (XmlOptions) null);
        }

        public static CTBookView parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBookView) POIXMLTypeLoader.parse(inputStream, CTBookView.type, xmlOptions);
        }

        public static CTBookView parse(Reader reader) throws XmlException, IOException {
            return (CTBookView) POIXMLTypeLoader.parse(reader, CTBookView.type, (XmlOptions) null);
        }

        public static CTBookView parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBookView) POIXMLTypeLoader.parse(reader, CTBookView.type, xmlOptions);
        }

        public static CTBookView parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBookView) POIXMLTypeLoader.parse(xMLStreamReader, CTBookView.type, (XmlOptions) null);
        }

        public static CTBookView parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBookView) POIXMLTypeLoader.parse(xMLStreamReader, CTBookView.type, xmlOptions);
        }

        public static CTBookView parse(Node node) throws XmlException {
            return (CTBookView) POIXMLTypeLoader.parse(node, CTBookView.type, (XmlOptions) null);
        }

        public static CTBookView parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBookView) POIXMLTypeLoader.parse(node, CTBookView.type, xmlOptions);
        }

        public static CTBookView parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBookView) POIXMLTypeLoader.parse(xMLInputStream, CTBookView.type, (XmlOptions) null);
        }

        public static CTBookView parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBookView) POIXMLTypeLoader.parse(xMLInputStream, CTBookView.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBookView.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBookView.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    STVisibility$Enum getVisibility();

    STVisibility xgetVisibility();

    boolean isSetVisibility();

    void setVisibility(STVisibility$Enum sTVisibility$Enum);

    void xsetVisibility(STVisibility sTVisibility);

    void unsetVisibility();

    boolean getMinimized();

    XmlBoolean xgetMinimized();

    boolean isSetMinimized();

    void setMinimized(boolean z);

    void xsetMinimized(XmlBoolean xmlBoolean);

    void unsetMinimized();

    boolean getShowHorizontalScroll();

    XmlBoolean xgetShowHorizontalScroll();

    boolean isSetShowHorizontalScroll();

    void setShowHorizontalScroll(boolean z);

    void xsetShowHorizontalScroll(XmlBoolean xmlBoolean);

    void unsetShowHorizontalScroll();

    boolean getShowVerticalScroll();

    XmlBoolean xgetShowVerticalScroll();

    boolean isSetShowVerticalScroll();

    void setShowVerticalScroll(boolean z);

    void xsetShowVerticalScroll(XmlBoolean xmlBoolean);

    void unsetShowVerticalScroll();

    boolean getShowSheetTabs();

    XmlBoolean xgetShowSheetTabs();

    boolean isSetShowSheetTabs();

    void setShowSheetTabs(boolean z);

    void xsetShowSheetTabs(XmlBoolean xmlBoolean);

    void unsetShowSheetTabs();

    int getXWindow();

    XmlInt xgetXWindow();

    boolean isSetXWindow();

    void setXWindow(int i);

    void xsetXWindow(XmlInt xmlInt);

    void unsetXWindow();

    int getYWindow();

    XmlInt xgetYWindow();

    boolean isSetYWindow();

    void setYWindow(int i);

    void xsetYWindow(XmlInt xmlInt);

    void unsetYWindow();

    long getWindowWidth();

    XmlUnsignedInt xgetWindowWidth();

    boolean isSetWindowWidth();

    void setWindowWidth(long j);

    void xsetWindowWidth(XmlUnsignedInt xmlUnsignedInt);

    void unsetWindowWidth();

    long getWindowHeight();

    XmlUnsignedInt xgetWindowHeight();

    boolean isSetWindowHeight();

    void setWindowHeight(long j);

    void xsetWindowHeight(XmlUnsignedInt xmlUnsignedInt);

    void unsetWindowHeight();

    long getTabRatio();

    XmlUnsignedInt xgetTabRatio();

    boolean isSetTabRatio();

    void setTabRatio(long j);

    void xsetTabRatio(XmlUnsignedInt xmlUnsignedInt);

    void unsetTabRatio();

    long getFirstSheet();

    XmlUnsignedInt xgetFirstSheet();

    boolean isSetFirstSheet();

    void setFirstSheet(long j);

    void xsetFirstSheet(XmlUnsignedInt xmlUnsignedInt);

    void unsetFirstSheet();

    long getActiveTab();

    XmlUnsignedInt xgetActiveTab();

    boolean isSetActiveTab();

    void setActiveTab(long j);

    void xsetActiveTab(XmlUnsignedInt xmlUnsignedInt);

    void unsetActiveTab();

    boolean getAutoFilterDateGrouping();

    XmlBoolean xgetAutoFilterDateGrouping();

    boolean isSetAutoFilterDateGrouping();

    void setAutoFilterDateGrouping(boolean z);

    void xsetAutoFilterDateGrouping(XmlBoolean xmlBoolean);

    void unsetAutoFilterDateGrouping();
}
