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
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPane.class */
public interface CTPane extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPane.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpaneaab1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPane$Factory.class */
    public static final class Factory {
        public static CTPane newInstance() {
            return (CTPane) POIXMLTypeLoader.newInstance(CTPane.type, null);
        }

        public static CTPane newInstance(XmlOptions xmlOptions) {
            return (CTPane) POIXMLTypeLoader.newInstance(CTPane.type, xmlOptions);
        }

        public static CTPane parse(String str) throws XmlException {
            return (CTPane) POIXMLTypeLoader.parse(str, CTPane.type, (XmlOptions) null);
        }

        public static CTPane parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPane) POIXMLTypeLoader.parse(str, CTPane.type, xmlOptions);
        }

        public static CTPane parse(File file) throws XmlException, IOException {
            return (CTPane) POIXMLTypeLoader.parse(file, CTPane.type, (XmlOptions) null);
        }

        public static CTPane parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPane) POIXMLTypeLoader.parse(file, CTPane.type, xmlOptions);
        }

        public static CTPane parse(URL url) throws XmlException, IOException {
            return (CTPane) POIXMLTypeLoader.parse(url, CTPane.type, (XmlOptions) null);
        }

        public static CTPane parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPane) POIXMLTypeLoader.parse(url, CTPane.type, xmlOptions);
        }

        public static CTPane parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPane) POIXMLTypeLoader.parse(inputStream, CTPane.type, (XmlOptions) null);
        }

        public static CTPane parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPane) POIXMLTypeLoader.parse(inputStream, CTPane.type, xmlOptions);
        }

        public static CTPane parse(Reader reader) throws XmlException, IOException {
            return (CTPane) POIXMLTypeLoader.parse(reader, CTPane.type, (XmlOptions) null);
        }

        public static CTPane parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPane) POIXMLTypeLoader.parse(reader, CTPane.type, xmlOptions);
        }

        public static CTPane parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPane) POIXMLTypeLoader.parse(xMLStreamReader, CTPane.type, (XmlOptions) null);
        }

        public static CTPane parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPane) POIXMLTypeLoader.parse(xMLStreamReader, CTPane.type, xmlOptions);
        }

        public static CTPane parse(Node node) throws XmlException {
            return (CTPane) POIXMLTypeLoader.parse(node, CTPane.type, (XmlOptions) null);
        }

        public static CTPane parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPane) POIXMLTypeLoader.parse(node, CTPane.type, xmlOptions);
        }

        public static CTPane parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPane) POIXMLTypeLoader.parse(xMLInputStream, CTPane.type, (XmlOptions) null);
        }

        public static CTPane parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPane) POIXMLTypeLoader.parse(xMLInputStream, CTPane.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPane.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPane.type, xmlOptions);
        }

        private Factory() {
        }
    }

    double getXSplit();

    XmlDouble xgetXSplit();

    boolean isSetXSplit();

    void setXSplit(double d);

    void xsetXSplit(XmlDouble xmlDouble);

    void unsetXSplit();

    double getYSplit();

    XmlDouble xgetYSplit();

    boolean isSetYSplit();

    void setYSplit(double d);

    void xsetYSplit(XmlDouble xmlDouble);

    void unsetYSplit();

    String getTopLeftCell();

    STCellRef xgetTopLeftCell();

    boolean isSetTopLeftCell();

    void setTopLeftCell(String str);

    void xsetTopLeftCell(STCellRef sTCellRef);

    void unsetTopLeftCell();

    STPane.Enum getActivePane();

    STPane xgetActivePane();

    boolean isSetActivePane();

    void setActivePane(STPane.Enum r1);

    void xsetActivePane(STPane sTPane);

    void unsetActivePane();

    STPaneState.Enum getState();

    STPaneState xgetState();

    boolean isSetState();

    void setState(STPaneState.Enum r1);

    void xsetState(STPaneState sTPaneState);

    void unsetState();
}
