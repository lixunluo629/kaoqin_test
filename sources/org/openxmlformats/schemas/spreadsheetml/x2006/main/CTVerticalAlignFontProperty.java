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
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignRun;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTVerticalAlignFontProperty.class */
public interface CTVerticalAlignFontProperty extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTVerticalAlignFontProperty.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctverticalalignfontproperty89f2type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTVerticalAlignFontProperty$Factory.class */
    public static final class Factory {
        public static CTVerticalAlignFontProperty newInstance() {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.newInstance(CTVerticalAlignFontProperty.type, null);
        }

        public static CTVerticalAlignFontProperty newInstance(XmlOptions xmlOptions) {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.newInstance(CTVerticalAlignFontProperty.type, xmlOptions);
        }

        public static CTVerticalAlignFontProperty parse(String str) throws XmlException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(str, CTVerticalAlignFontProperty.type, (XmlOptions) null);
        }

        public static CTVerticalAlignFontProperty parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(str, CTVerticalAlignFontProperty.type, xmlOptions);
        }

        public static CTVerticalAlignFontProperty parse(File file) throws XmlException, IOException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(file, CTVerticalAlignFontProperty.type, (XmlOptions) null);
        }

        public static CTVerticalAlignFontProperty parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(file, CTVerticalAlignFontProperty.type, xmlOptions);
        }

        public static CTVerticalAlignFontProperty parse(URL url) throws XmlException, IOException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(url, CTVerticalAlignFontProperty.type, (XmlOptions) null);
        }

        public static CTVerticalAlignFontProperty parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(url, CTVerticalAlignFontProperty.type, xmlOptions);
        }

        public static CTVerticalAlignFontProperty parse(InputStream inputStream) throws XmlException, IOException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(inputStream, CTVerticalAlignFontProperty.type, (XmlOptions) null);
        }

        public static CTVerticalAlignFontProperty parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(inputStream, CTVerticalAlignFontProperty.type, xmlOptions);
        }

        public static CTVerticalAlignFontProperty parse(Reader reader) throws XmlException, IOException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(reader, CTVerticalAlignFontProperty.type, (XmlOptions) null);
        }

        public static CTVerticalAlignFontProperty parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(reader, CTVerticalAlignFontProperty.type, xmlOptions);
        }

        public static CTVerticalAlignFontProperty parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(xMLStreamReader, CTVerticalAlignFontProperty.type, (XmlOptions) null);
        }

        public static CTVerticalAlignFontProperty parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(xMLStreamReader, CTVerticalAlignFontProperty.type, xmlOptions);
        }

        public static CTVerticalAlignFontProperty parse(Node node) throws XmlException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(node, CTVerticalAlignFontProperty.type, (XmlOptions) null);
        }

        public static CTVerticalAlignFontProperty parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(node, CTVerticalAlignFontProperty.type, xmlOptions);
        }

        public static CTVerticalAlignFontProperty parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(xMLInputStream, CTVerticalAlignFontProperty.type, (XmlOptions) null);
        }

        public static CTVerticalAlignFontProperty parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTVerticalAlignFontProperty) POIXMLTypeLoader.parse(xMLInputStream, CTVerticalAlignFontProperty.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTVerticalAlignFontProperty.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTVerticalAlignFontProperty.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STVerticalAlignRun.Enum getVal();

    STVerticalAlignRun xgetVal();

    void setVal(STVerticalAlignRun.Enum r1);

    void xsetVal(STVerticalAlignRun sTVerticalAlignRun);
}
