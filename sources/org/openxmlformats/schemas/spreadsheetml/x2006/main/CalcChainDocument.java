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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CalcChainDocument.class */
public interface CalcChainDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CalcChainDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("calcchainfc37doctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CalcChainDocument$Factory.class */
    public static final class Factory {
        public static CalcChainDocument newInstance() {
            return (CalcChainDocument) POIXMLTypeLoader.newInstance(CalcChainDocument.type, null);
        }

        public static CalcChainDocument newInstance(XmlOptions xmlOptions) {
            return (CalcChainDocument) POIXMLTypeLoader.newInstance(CalcChainDocument.type, xmlOptions);
        }

        public static CalcChainDocument parse(String str) throws XmlException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(str, CalcChainDocument.type, (XmlOptions) null);
        }

        public static CalcChainDocument parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(str, CalcChainDocument.type, xmlOptions);
        }

        public static CalcChainDocument parse(File file) throws XmlException, IOException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(file, CalcChainDocument.type, (XmlOptions) null);
        }

        public static CalcChainDocument parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(file, CalcChainDocument.type, xmlOptions);
        }

        public static CalcChainDocument parse(URL url) throws XmlException, IOException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(url, CalcChainDocument.type, (XmlOptions) null);
        }

        public static CalcChainDocument parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(url, CalcChainDocument.type, xmlOptions);
        }

        public static CalcChainDocument parse(InputStream inputStream) throws XmlException, IOException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(inputStream, CalcChainDocument.type, (XmlOptions) null);
        }

        public static CalcChainDocument parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(inputStream, CalcChainDocument.type, xmlOptions);
        }

        public static CalcChainDocument parse(Reader reader) throws XmlException, IOException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(reader, CalcChainDocument.type, (XmlOptions) null);
        }

        public static CalcChainDocument parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(reader, CalcChainDocument.type, xmlOptions);
        }

        public static CalcChainDocument parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(xMLStreamReader, CalcChainDocument.type, (XmlOptions) null);
        }

        public static CalcChainDocument parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(xMLStreamReader, CalcChainDocument.type, xmlOptions);
        }

        public static CalcChainDocument parse(Node node) throws XmlException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(node, CalcChainDocument.type, (XmlOptions) null);
        }

        public static CalcChainDocument parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(node, CalcChainDocument.type, xmlOptions);
        }

        public static CalcChainDocument parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(xMLInputStream, CalcChainDocument.type, (XmlOptions) null);
        }

        public static CalcChainDocument parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CalcChainDocument) POIXMLTypeLoader.parse(xMLInputStream, CalcChainDocument.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CalcChainDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CalcChainDocument.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTCalcChain getCalcChain();

    void setCalcChain(CTCalcChain cTCalcChain);

    CTCalcChain addNewCalcChain();
}
