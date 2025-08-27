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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextAlignment;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTextAlignment.class */
public interface CTTextAlignment extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextAlignment.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextalignment495ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTextAlignment$Factory.class */
    public static final class Factory {
        public static CTTextAlignment newInstance() {
            return (CTTextAlignment) POIXMLTypeLoader.newInstance(CTTextAlignment.type, null);
        }

        public static CTTextAlignment newInstance(XmlOptions xmlOptions) {
            return (CTTextAlignment) POIXMLTypeLoader.newInstance(CTTextAlignment.type, xmlOptions);
        }

        public static CTTextAlignment parse(String str) throws XmlException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(str, CTTextAlignment.type, (XmlOptions) null);
        }

        public static CTTextAlignment parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(str, CTTextAlignment.type, xmlOptions);
        }

        public static CTTextAlignment parse(File file) throws XmlException, IOException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(file, CTTextAlignment.type, (XmlOptions) null);
        }

        public static CTTextAlignment parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(file, CTTextAlignment.type, xmlOptions);
        }

        public static CTTextAlignment parse(URL url) throws XmlException, IOException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(url, CTTextAlignment.type, (XmlOptions) null);
        }

        public static CTTextAlignment parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(url, CTTextAlignment.type, xmlOptions);
        }

        public static CTTextAlignment parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(inputStream, CTTextAlignment.type, (XmlOptions) null);
        }

        public static CTTextAlignment parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(inputStream, CTTextAlignment.type, xmlOptions);
        }

        public static CTTextAlignment parse(Reader reader) throws XmlException, IOException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(reader, CTTextAlignment.type, (XmlOptions) null);
        }

        public static CTTextAlignment parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(reader, CTTextAlignment.type, xmlOptions);
        }

        public static CTTextAlignment parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(xMLStreamReader, CTTextAlignment.type, (XmlOptions) null);
        }

        public static CTTextAlignment parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(xMLStreamReader, CTTextAlignment.type, xmlOptions);
        }

        public static CTTextAlignment parse(Node node) throws XmlException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(node, CTTextAlignment.type, (XmlOptions) null);
        }

        public static CTTextAlignment parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(node, CTTextAlignment.type, xmlOptions);
        }

        public static CTTextAlignment parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(xMLInputStream, CTTextAlignment.type, (XmlOptions) null);
        }

        public static CTTextAlignment parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextAlignment) POIXMLTypeLoader.parse(xMLInputStream, CTTextAlignment.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextAlignment.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextAlignment.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STTextAlignment.Enum getVal();

    STTextAlignment xgetVal();

    void setVal(STTextAlignment.Enum r1);

    void xsetVal(STTextAlignment sTTextAlignment);
}
