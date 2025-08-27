package org.openxmlformats.schemas.presentationml.x2006.main;

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
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTConnectorNonVisual.class */
public interface CTConnectorNonVisual extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTConnectorNonVisual.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctconnectornonvisual0f45type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTConnectorNonVisual$Factory.class */
    public static final class Factory {
        public static CTConnectorNonVisual newInstance() {
            return (CTConnectorNonVisual) POIXMLTypeLoader.newInstance(CTConnectorNonVisual.type, null);
        }

        public static CTConnectorNonVisual newInstance(XmlOptions xmlOptions) {
            return (CTConnectorNonVisual) POIXMLTypeLoader.newInstance(CTConnectorNonVisual.type, xmlOptions);
        }

        public static CTConnectorNonVisual parse(String str) throws XmlException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(str, CTConnectorNonVisual.type, (XmlOptions) null);
        }

        public static CTConnectorNonVisual parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(str, CTConnectorNonVisual.type, xmlOptions);
        }

        public static CTConnectorNonVisual parse(File file) throws XmlException, IOException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(file, CTConnectorNonVisual.type, (XmlOptions) null);
        }

        public static CTConnectorNonVisual parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(file, CTConnectorNonVisual.type, xmlOptions);
        }

        public static CTConnectorNonVisual parse(URL url) throws XmlException, IOException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(url, CTConnectorNonVisual.type, (XmlOptions) null);
        }

        public static CTConnectorNonVisual parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(url, CTConnectorNonVisual.type, xmlOptions);
        }

        public static CTConnectorNonVisual parse(InputStream inputStream) throws XmlException, IOException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(inputStream, CTConnectorNonVisual.type, (XmlOptions) null);
        }

        public static CTConnectorNonVisual parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(inputStream, CTConnectorNonVisual.type, xmlOptions);
        }

        public static CTConnectorNonVisual parse(Reader reader) throws XmlException, IOException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(reader, CTConnectorNonVisual.type, (XmlOptions) null);
        }

        public static CTConnectorNonVisual parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(reader, CTConnectorNonVisual.type, xmlOptions);
        }

        public static CTConnectorNonVisual parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(xMLStreamReader, CTConnectorNonVisual.type, (XmlOptions) null);
        }

        public static CTConnectorNonVisual parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(xMLStreamReader, CTConnectorNonVisual.type, xmlOptions);
        }

        public static CTConnectorNonVisual parse(Node node) throws XmlException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(node, CTConnectorNonVisual.type, (XmlOptions) null);
        }

        public static CTConnectorNonVisual parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(node, CTConnectorNonVisual.type, xmlOptions);
        }

        public static CTConnectorNonVisual parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(xMLInputStream, CTConnectorNonVisual.type, (XmlOptions) null);
        }

        public static CTConnectorNonVisual parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTConnectorNonVisual) POIXMLTypeLoader.parse(xMLInputStream, CTConnectorNonVisual.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTConnectorNonVisual.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTConnectorNonVisual.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTNonVisualDrawingProps getCNvPr();

    void setCNvPr(CTNonVisualDrawingProps cTNonVisualDrawingProps);

    CTNonVisualDrawingProps addNewCNvPr();

    CTNonVisualConnectorProperties getCNvCxnSpPr();

    void setCNvCxnSpPr(CTNonVisualConnectorProperties cTNonVisualConnectorProperties);

    CTNonVisualConnectorProperties addNewCNvCxnSpPr();

    CTApplicationNonVisualDrawingProps getNvPr();

    void setNvPr(CTApplicationNonVisualDrawingProps cTApplicationNonVisualDrawingProps);

    CTApplicationNonVisualDrawingProps addNewNvPr();
}
