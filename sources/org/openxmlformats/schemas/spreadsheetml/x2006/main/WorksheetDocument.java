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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/WorksheetDocument.class */
public interface WorksheetDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(WorksheetDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("worksheetf539doctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/WorksheetDocument$Factory.class */
    public static final class Factory {
        public static WorksheetDocument newInstance() {
            return (WorksheetDocument) POIXMLTypeLoader.newInstance(WorksheetDocument.type, null);
        }

        public static WorksheetDocument newInstance(XmlOptions xmlOptions) {
            return (WorksheetDocument) POIXMLTypeLoader.newInstance(WorksheetDocument.type, xmlOptions);
        }

        public static WorksheetDocument parse(String str) throws XmlException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(str, WorksheetDocument.type, (XmlOptions) null);
        }

        public static WorksheetDocument parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(str, WorksheetDocument.type, xmlOptions);
        }

        public static WorksheetDocument parse(File file) throws XmlException, IOException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(file, WorksheetDocument.type, (XmlOptions) null);
        }

        public static WorksheetDocument parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(file, WorksheetDocument.type, xmlOptions);
        }

        public static WorksheetDocument parse(URL url) throws XmlException, IOException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(url, WorksheetDocument.type, (XmlOptions) null);
        }

        public static WorksheetDocument parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(url, WorksheetDocument.type, xmlOptions);
        }

        public static WorksheetDocument parse(InputStream inputStream) throws XmlException, IOException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(inputStream, WorksheetDocument.type, (XmlOptions) null);
        }

        public static WorksheetDocument parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(inputStream, WorksheetDocument.type, xmlOptions);
        }

        public static WorksheetDocument parse(Reader reader) throws XmlException, IOException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(reader, WorksheetDocument.type, (XmlOptions) null);
        }

        public static WorksheetDocument parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(reader, WorksheetDocument.type, xmlOptions);
        }

        public static WorksheetDocument parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(xMLStreamReader, WorksheetDocument.type, (XmlOptions) null);
        }

        public static WorksheetDocument parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(xMLStreamReader, WorksheetDocument.type, xmlOptions);
        }

        public static WorksheetDocument parse(Node node) throws XmlException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(node, WorksheetDocument.type, (XmlOptions) null);
        }

        public static WorksheetDocument parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(node, WorksheetDocument.type, xmlOptions);
        }

        public static WorksheetDocument parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(xMLInputStream, WorksheetDocument.type, (XmlOptions) null);
        }

        public static WorksheetDocument parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (WorksheetDocument) POIXMLTypeLoader.parse(xMLInputStream, WorksheetDocument.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, WorksheetDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, WorksheetDocument.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTWorksheet getWorksheet();

    void setWorksheet(CTWorksheet cTWorksheet);

    CTWorksheet addNewWorksheet();
}
