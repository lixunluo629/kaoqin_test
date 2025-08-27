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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/ChartsheetDocument.class */
public interface ChartsheetDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ChartsheetDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("chartsheet99dedoctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/ChartsheetDocument$Factory.class */
    public static final class Factory {
        public static ChartsheetDocument newInstance() {
            return (ChartsheetDocument) POIXMLTypeLoader.newInstance(ChartsheetDocument.type, null);
        }

        public static ChartsheetDocument newInstance(XmlOptions xmlOptions) {
            return (ChartsheetDocument) POIXMLTypeLoader.newInstance(ChartsheetDocument.type, xmlOptions);
        }

        public static ChartsheetDocument parse(String str) throws XmlException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(str, ChartsheetDocument.type, (XmlOptions) null);
        }

        public static ChartsheetDocument parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(str, ChartsheetDocument.type, xmlOptions);
        }

        public static ChartsheetDocument parse(File file) throws XmlException, IOException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(file, ChartsheetDocument.type, (XmlOptions) null);
        }

        public static ChartsheetDocument parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(file, ChartsheetDocument.type, xmlOptions);
        }

        public static ChartsheetDocument parse(URL url) throws XmlException, IOException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(url, ChartsheetDocument.type, (XmlOptions) null);
        }

        public static ChartsheetDocument parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(url, ChartsheetDocument.type, xmlOptions);
        }

        public static ChartsheetDocument parse(InputStream inputStream) throws XmlException, IOException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(inputStream, ChartsheetDocument.type, (XmlOptions) null);
        }

        public static ChartsheetDocument parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(inputStream, ChartsheetDocument.type, xmlOptions);
        }

        public static ChartsheetDocument parse(Reader reader) throws XmlException, IOException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(reader, ChartsheetDocument.type, (XmlOptions) null);
        }

        public static ChartsheetDocument parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(reader, ChartsheetDocument.type, xmlOptions);
        }

        public static ChartsheetDocument parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(xMLStreamReader, ChartsheetDocument.type, (XmlOptions) null);
        }

        public static ChartsheetDocument parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(xMLStreamReader, ChartsheetDocument.type, xmlOptions);
        }

        public static ChartsheetDocument parse(Node node) throws XmlException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(node, ChartsheetDocument.type, (XmlOptions) null);
        }

        public static ChartsheetDocument parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(node, ChartsheetDocument.type, xmlOptions);
        }

        public static ChartsheetDocument parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(xMLInputStream, ChartsheetDocument.type, (XmlOptions) null);
        }

        public static ChartsheetDocument parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (ChartsheetDocument) POIXMLTypeLoader.parse(xMLInputStream, ChartsheetDocument.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, ChartsheetDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, ChartsheetDocument.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTChartsheet getChartsheet();

    void setChartsheet(CTChartsheet cTChartsheet);

    CTChartsheet addNewChartsheet();
}
