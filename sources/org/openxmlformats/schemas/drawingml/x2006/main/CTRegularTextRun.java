package org.openxmlformats.schemas.drawingml.x2006.main;

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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTRegularTextRun.class */
public interface CTRegularTextRun extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTRegularTextRun.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctregulartextrun7e3dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTRegularTextRun$Factory.class */
    public static final class Factory {
        public static CTRegularTextRun newInstance() {
            return (CTRegularTextRun) POIXMLTypeLoader.newInstance(CTRegularTextRun.type, null);
        }

        public static CTRegularTextRun newInstance(XmlOptions xmlOptions) {
            return (CTRegularTextRun) POIXMLTypeLoader.newInstance(CTRegularTextRun.type, xmlOptions);
        }

        public static CTRegularTextRun parse(String str) throws XmlException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(str, CTRegularTextRun.type, (XmlOptions) null);
        }

        public static CTRegularTextRun parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(str, CTRegularTextRun.type, xmlOptions);
        }

        public static CTRegularTextRun parse(File file) throws XmlException, IOException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(file, CTRegularTextRun.type, (XmlOptions) null);
        }

        public static CTRegularTextRun parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(file, CTRegularTextRun.type, xmlOptions);
        }

        public static CTRegularTextRun parse(URL url) throws XmlException, IOException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(url, CTRegularTextRun.type, (XmlOptions) null);
        }

        public static CTRegularTextRun parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(url, CTRegularTextRun.type, xmlOptions);
        }

        public static CTRegularTextRun parse(InputStream inputStream) throws XmlException, IOException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(inputStream, CTRegularTextRun.type, (XmlOptions) null);
        }

        public static CTRegularTextRun parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(inputStream, CTRegularTextRun.type, xmlOptions);
        }

        public static CTRegularTextRun parse(Reader reader) throws XmlException, IOException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(reader, CTRegularTextRun.type, (XmlOptions) null);
        }

        public static CTRegularTextRun parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(reader, CTRegularTextRun.type, xmlOptions);
        }

        public static CTRegularTextRun parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(xMLStreamReader, CTRegularTextRun.type, (XmlOptions) null);
        }

        public static CTRegularTextRun parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(xMLStreamReader, CTRegularTextRun.type, xmlOptions);
        }

        public static CTRegularTextRun parse(Node node) throws XmlException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(node, CTRegularTextRun.type, (XmlOptions) null);
        }

        public static CTRegularTextRun parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(node, CTRegularTextRun.type, xmlOptions);
        }

        public static CTRegularTextRun parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(xMLInputStream, CTRegularTextRun.type, (XmlOptions) null);
        }

        public static CTRegularTextRun parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTRegularTextRun) POIXMLTypeLoader.parse(xMLInputStream, CTRegularTextRun.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRegularTextRun.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRegularTextRun.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTextCharacterProperties getRPr();

    boolean isSetRPr();

    void setRPr(CTTextCharacterProperties cTTextCharacterProperties);

    CTTextCharacterProperties addNewRPr();

    void unsetRPr();

    String getT();

    XmlString xgetT();

    void setT(String str);

    void xsetT(XmlString xmlString);
}
