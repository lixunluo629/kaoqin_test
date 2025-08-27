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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTJc.class */
public interface CTJc extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTJc.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctjc158ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTJc$Factory.class */
    public static final class Factory {
        public static CTJc newInstance() {
            return (CTJc) POIXMLTypeLoader.newInstance(CTJc.type, null);
        }

        public static CTJc newInstance(XmlOptions xmlOptions) {
            return (CTJc) POIXMLTypeLoader.newInstance(CTJc.type, xmlOptions);
        }

        public static CTJc parse(String str) throws XmlException {
            return (CTJc) POIXMLTypeLoader.parse(str, CTJc.type, (XmlOptions) null);
        }

        public static CTJc parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTJc) POIXMLTypeLoader.parse(str, CTJc.type, xmlOptions);
        }

        public static CTJc parse(File file) throws XmlException, IOException {
            return (CTJc) POIXMLTypeLoader.parse(file, CTJc.type, (XmlOptions) null);
        }

        public static CTJc parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTJc) POIXMLTypeLoader.parse(file, CTJc.type, xmlOptions);
        }

        public static CTJc parse(URL url) throws XmlException, IOException {
            return (CTJc) POIXMLTypeLoader.parse(url, CTJc.type, (XmlOptions) null);
        }

        public static CTJc parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTJc) POIXMLTypeLoader.parse(url, CTJc.type, xmlOptions);
        }

        public static CTJc parse(InputStream inputStream) throws XmlException, IOException {
            return (CTJc) POIXMLTypeLoader.parse(inputStream, CTJc.type, (XmlOptions) null);
        }

        public static CTJc parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTJc) POIXMLTypeLoader.parse(inputStream, CTJc.type, xmlOptions);
        }

        public static CTJc parse(Reader reader) throws XmlException, IOException {
            return (CTJc) POIXMLTypeLoader.parse(reader, CTJc.type, (XmlOptions) null);
        }

        public static CTJc parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTJc) POIXMLTypeLoader.parse(reader, CTJc.type, xmlOptions);
        }

        public static CTJc parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTJc) POIXMLTypeLoader.parse(xMLStreamReader, CTJc.type, (XmlOptions) null);
        }

        public static CTJc parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTJc) POIXMLTypeLoader.parse(xMLStreamReader, CTJc.type, xmlOptions);
        }

        public static CTJc parse(Node node) throws XmlException {
            return (CTJc) POIXMLTypeLoader.parse(node, CTJc.type, (XmlOptions) null);
        }

        public static CTJc parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTJc) POIXMLTypeLoader.parse(node, CTJc.type, xmlOptions);
        }

        public static CTJc parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTJc) POIXMLTypeLoader.parse(xMLInputStream, CTJc.type, (XmlOptions) null);
        }

        public static CTJc parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTJc) POIXMLTypeLoader.parse(xMLInputStream, CTJc.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTJc.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTJc.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STJc.Enum getVal();

    STJc xgetVal();

    void setVal(STJc.Enum r1);

    void xsetVal(STJc sTJc);
}
