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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheetViews.class */
public interface CTSheetViews extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSheetViews.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsheetviewsb918type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheetViews$Factory.class */
    public static final class Factory {
        public static CTSheetViews newInstance() {
            return (CTSheetViews) POIXMLTypeLoader.newInstance(CTSheetViews.type, null);
        }

        public static CTSheetViews newInstance(XmlOptions xmlOptions) {
            return (CTSheetViews) POIXMLTypeLoader.newInstance(CTSheetViews.type, xmlOptions);
        }

        public static CTSheetViews parse(String str) throws XmlException {
            return (CTSheetViews) POIXMLTypeLoader.parse(str, CTSheetViews.type, (XmlOptions) null);
        }

        public static CTSheetViews parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetViews) POIXMLTypeLoader.parse(str, CTSheetViews.type, xmlOptions);
        }

        public static CTSheetViews parse(File file) throws XmlException, IOException {
            return (CTSheetViews) POIXMLTypeLoader.parse(file, CTSheetViews.type, (XmlOptions) null);
        }

        public static CTSheetViews parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetViews) POIXMLTypeLoader.parse(file, CTSheetViews.type, xmlOptions);
        }

        public static CTSheetViews parse(URL url) throws XmlException, IOException {
            return (CTSheetViews) POIXMLTypeLoader.parse(url, CTSheetViews.type, (XmlOptions) null);
        }

        public static CTSheetViews parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetViews) POIXMLTypeLoader.parse(url, CTSheetViews.type, xmlOptions);
        }

        public static CTSheetViews parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSheetViews) POIXMLTypeLoader.parse(inputStream, CTSheetViews.type, (XmlOptions) null);
        }

        public static CTSheetViews parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetViews) POIXMLTypeLoader.parse(inputStream, CTSheetViews.type, xmlOptions);
        }

        public static CTSheetViews parse(Reader reader) throws XmlException, IOException {
            return (CTSheetViews) POIXMLTypeLoader.parse(reader, CTSheetViews.type, (XmlOptions) null);
        }

        public static CTSheetViews parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetViews) POIXMLTypeLoader.parse(reader, CTSheetViews.type, xmlOptions);
        }

        public static CTSheetViews parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSheetViews) POIXMLTypeLoader.parse(xMLStreamReader, CTSheetViews.type, (XmlOptions) null);
        }

        public static CTSheetViews parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetViews) POIXMLTypeLoader.parse(xMLStreamReader, CTSheetViews.type, xmlOptions);
        }

        public static CTSheetViews parse(Node node) throws XmlException {
            return (CTSheetViews) POIXMLTypeLoader.parse(node, CTSheetViews.type, (XmlOptions) null);
        }

        public static CTSheetViews parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetViews) POIXMLTypeLoader.parse(node, CTSheetViews.type, xmlOptions);
        }

        public static CTSheetViews parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSheetViews) POIXMLTypeLoader.parse(xMLInputStream, CTSheetViews.type, (XmlOptions) null);
        }

        public static CTSheetViews parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSheetViews) POIXMLTypeLoader.parse(xMLInputStream, CTSheetViews.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheetViews.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheetViews.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTSheetView> getSheetViewList();

    CTSheetView[] getSheetViewArray();

    CTSheetView getSheetViewArray(int i);

    int sizeOfSheetViewArray();

    void setSheetViewArray(CTSheetView[] cTSheetViewArr);

    void setSheetViewArray(int i, CTSheetView cTSheetView);

    CTSheetView insertNewSheetView(int i);

    CTSheetView addNewSheetView();

    void removeSheetView(int i);

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
