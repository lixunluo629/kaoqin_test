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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTText.class */
public interface CTText extends STString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTText.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttext7f5btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTText$Factory.class */
    public static final class Factory {
        public static CTText newInstance() {
            return (CTText) POIXMLTypeLoader.newInstance(CTText.type, null);
        }

        public static CTText newInstance(XmlOptions xmlOptions) {
            return (CTText) POIXMLTypeLoader.newInstance(CTText.type, xmlOptions);
        }

        public static CTText parse(String str) throws XmlException {
            return (CTText) POIXMLTypeLoader.parse(str, CTText.type, (XmlOptions) null);
        }

        public static CTText parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTText) POIXMLTypeLoader.parse(str, CTText.type, xmlOptions);
        }

        public static CTText parse(File file) throws XmlException, IOException {
            return (CTText) POIXMLTypeLoader.parse(file, CTText.type, (XmlOptions) null);
        }

        public static CTText parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTText) POIXMLTypeLoader.parse(file, CTText.type, xmlOptions);
        }

        public static CTText parse(URL url) throws XmlException, IOException {
            return (CTText) POIXMLTypeLoader.parse(url, CTText.type, (XmlOptions) null);
        }

        public static CTText parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTText) POIXMLTypeLoader.parse(url, CTText.type, xmlOptions);
        }

        public static CTText parse(InputStream inputStream) throws XmlException, IOException {
            return (CTText) POIXMLTypeLoader.parse(inputStream, CTText.type, (XmlOptions) null);
        }

        public static CTText parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTText) POIXMLTypeLoader.parse(inputStream, CTText.type, xmlOptions);
        }

        public static CTText parse(Reader reader) throws XmlException, IOException {
            return (CTText) POIXMLTypeLoader.parse(reader, CTText.type, (XmlOptions) null);
        }

        public static CTText parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTText) POIXMLTypeLoader.parse(reader, CTText.type, xmlOptions);
        }

        public static CTText parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTText) POIXMLTypeLoader.parse(xMLStreamReader, CTText.type, (XmlOptions) null);
        }

        public static CTText parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTText) POIXMLTypeLoader.parse(xMLStreamReader, CTText.type, xmlOptions);
        }

        public static CTText parse(Node node) throws XmlException {
            return (CTText) POIXMLTypeLoader.parse(node, CTText.type, (XmlOptions) null);
        }

        public static CTText parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTText) POIXMLTypeLoader.parse(node, CTText.type, xmlOptions);
        }

        public static CTText parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTText) POIXMLTypeLoader.parse(xMLInputStream, CTText.type, (XmlOptions) null);
        }

        public static CTText parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTText) POIXMLTypeLoader.parse(xMLInputStream, CTText.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTText.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTText.type, xmlOptions);
        }

        private Factory() {
        }
    }

    SpaceAttribute.Space.Enum getSpace();

    SpaceAttribute.Space xgetSpace();

    boolean isSetSpace();

    void setSpace(SpaceAttribute.Space.Enum r1);

    void xsetSpace(SpaceAttribute.Space space);

    void unsetSpace();
}
