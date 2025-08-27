package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTRPrDefault.class */
public interface CTRPrDefault extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTRPrDefault.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctrprdefault5ebbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTRPrDefault$Factory.class */
    public static final class Factory {
        public static CTRPrDefault newInstance() {
            return (CTRPrDefault) POIXMLTypeLoader.newInstance(CTRPrDefault.type, null);
        }

        public static CTRPrDefault newInstance(XmlOptions xmlOptions) {
            return (CTRPrDefault) POIXMLTypeLoader.newInstance(CTRPrDefault.type, xmlOptions);
        }

        public static CTRPrDefault parse(String str) throws XmlException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(str, CTRPrDefault.type, (XmlOptions) null);
        }

        public static CTRPrDefault parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(str, CTRPrDefault.type, xmlOptions);
        }

        public static CTRPrDefault parse(File file) throws XmlException, IOException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(file, CTRPrDefault.type, (XmlOptions) null);
        }

        public static CTRPrDefault parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(file, CTRPrDefault.type, xmlOptions);
        }

        public static CTRPrDefault parse(URL url) throws XmlException, IOException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(url, CTRPrDefault.type, (XmlOptions) null);
        }

        public static CTRPrDefault parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(url, CTRPrDefault.type, xmlOptions);
        }

        public static CTRPrDefault parse(InputStream inputStream) throws XmlException, IOException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(inputStream, CTRPrDefault.type, (XmlOptions) null);
        }

        public static CTRPrDefault parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(inputStream, CTRPrDefault.type, xmlOptions);
        }

        public static CTRPrDefault parse(Reader reader) throws XmlException, IOException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(reader, CTRPrDefault.type, (XmlOptions) null);
        }

        public static CTRPrDefault parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(reader, CTRPrDefault.type, xmlOptions);
        }

        public static CTRPrDefault parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(xMLStreamReader, CTRPrDefault.type, (XmlOptions) null);
        }

        public static CTRPrDefault parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(xMLStreamReader, CTRPrDefault.type, xmlOptions);
        }

        public static CTRPrDefault parse(Node node) throws XmlException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(node, CTRPrDefault.type, (XmlOptions) null);
        }

        public static CTRPrDefault parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(node, CTRPrDefault.type, xmlOptions);
        }

        public static CTRPrDefault parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(xMLInputStream, CTRPrDefault.type, (XmlOptions) null);
        }

        public static CTRPrDefault parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTRPrDefault) POIXMLTypeLoader.parse(xMLInputStream, CTRPrDefault.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRPrDefault.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRPrDefault.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTRPr getRPr();

    boolean isSetRPr();

    void setRPr(CTRPr cTRPr);

    CTRPr addNewRPr();

    void unsetRPr();
}
