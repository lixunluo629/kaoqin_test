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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextSpacingPercent.class */
public interface CTTextSpacingPercent extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextSpacingPercent.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextspacingpercent322atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextSpacingPercent$Factory.class */
    public static final class Factory {
        public static CTTextSpacingPercent newInstance() {
            return (CTTextSpacingPercent) POIXMLTypeLoader.newInstance(CTTextSpacingPercent.type, null);
        }

        public static CTTextSpacingPercent newInstance(XmlOptions xmlOptions) {
            return (CTTextSpacingPercent) POIXMLTypeLoader.newInstance(CTTextSpacingPercent.type, xmlOptions);
        }

        public static CTTextSpacingPercent parse(String str) throws XmlException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(str, CTTextSpacingPercent.type, (XmlOptions) null);
        }

        public static CTTextSpacingPercent parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(str, CTTextSpacingPercent.type, xmlOptions);
        }

        public static CTTextSpacingPercent parse(File file) throws XmlException, IOException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(file, CTTextSpacingPercent.type, (XmlOptions) null);
        }

        public static CTTextSpacingPercent parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(file, CTTextSpacingPercent.type, xmlOptions);
        }

        public static CTTextSpacingPercent parse(URL url) throws XmlException, IOException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(url, CTTextSpacingPercent.type, (XmlOptions) null);
        }

        public static CTTextSpacingPercent parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(url, CTTextSpacingPercent.type, xmlOptions);
        }

        public static CTTextSpacingPercent parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(inputStream, CTTextSpacingPercent.type, (XmlOptions) null);
        }

        public static CTTextSpacingPercent parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(inputStream, CTTextSpacingPercent.type, xmlOptions);
        }

        public static CTTextSpacingPercent parse(Reader reader) throws XmlException, IOException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(reader, CTTextSpacingPercent.type, (XmlOptions) null);
        }

        public static CTTextSpacingPercent parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(reader, CTTextSpacingPercent.type, xmlOptions);
        }

        public static CTTextSpacingPercent parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(xMLStreamReader, CTTextSpacingPercent.type, (XmlOptions) null);
        }

        public static CTTextSpacingPercent parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(xMLStreamReader, CTTextSpacingPercent.type, xmlOptions);
        }

        public static CTTextSpacingPercent parse(Node node) throws XmlException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(node, CTTextSpacingPercent.type, (XmlOptions) null);
        }

        public static CTTextSpacingPercent parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(node, CTTextSpacingPercent.type, xmlOptions);
        }

        public static CTTextSpacingPercent parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(xMLInputStream, CTTextSpacingPercent.type, (XmlOptions) null);
        }

        public static CTTextSpacingPercent parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextSpacingPercent) POIXMLTypeLoader.parse(xMLInputStream, CTTextSpacingPercent.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextSpacingPercent.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextSpacingPercent.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getVal();

    STTextSpacingPercent xgetVal();

    void setVal(int i);

    void xsetVal(STTextSpacingPercent sTTextSpacingPercent);
}
