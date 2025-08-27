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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STTableStyleType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTableStyleElement.class */
public interface CTTableStyleElement extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTableStyleElement.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttablestyleelementa658type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTableStyleElement$Factory.class */
    public static final class Factory {
        public static CTTableStyleElement newInstance() {
            return (CTTableStyleElement) POIXMLTypeLoader.newInstance(CTTableStyleElement.type, null);
        }

        public static CTTableStyleElement newInstance(XmlOptions xmlOptions) {
            return (CTTableStyleElement) POIXMLTypeLoader.newInstance(CTTableStyleElement.type, xmlOptions);
        }

        public static CTTableStyleElement parse(String str) throws XmlException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(str, CTTableStyleElement.type, (XmlOptions) null);
        }

        public static CTTableStyleElement parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(str, CTTableStyleElement.type, xmlOptions);
        }

        public static CTTableStyleElement parse(File file) throws XmlException, IOException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(file, CTTableStyleElement.type, (XmlOptions) null);
        }

        public static CTTableStyleElement parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(file, CTTableStyleElement.type, xmlOptions);
        }

        public static CTTableStyleElement parse(URL url) throws XmlException, IOException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(url, CTTableStyleElement.type, (XmlOptions) null);
        }

        public static CTTableStyleElement parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(url, CTTableStyleElement.type, xmlOptions);
        }

        public static CTTableStyleElement parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(inputStream, CTTableStyleElement.type, (XmlOptions) null);
        }

        public static CTTableStyleElement parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(inputStream, CTTableStyleElement.type, xmlOptions);
        }

        public static CTTableStyleElement parse(Reader reader) throws XmlException, IOException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(reader, CTTableStyleElement.type, (XmlOptions) null);
        }

        public static CTTableStyleElement parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(reader, CTTableStyleElement.type, xmlOptions);
        }

        public static CTTableStyleElement parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(xMLStreamReader, CTTableStyleElement.type, (XmlOptions) null);
        }

        public static CTTableStyleElement parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(xMLStreamReader, CTTableStyleElement.type, xmlOptions);
        }

        public static CTTableStyleElement parse(Node node) throws XmlException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(node, CTTableStyleElement.type, (XmlOptions) null);
        }

        public static CTTableStyleElement parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(node, CTTableStyleElement.type, xmlOptions);
        }

        public static CTTableStyleElement parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(xMLInputStream, CTTableStyleElement.type, (XmlOptions) null);
        }

        public static CTTableStyleElement parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTableStyleElement) POIXMLTypeLoader.parse(xMLInputStream, CTTableStyleElement.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableStyleElement.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableStyleElement.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STTableStyleType.Enum getType();

    STTableStyleType xgetType();

    void setType(STTableStyleType.Enum r1);

    void xsetType(STTableStyleType sTTableStyleType);

    long getSize();

    XmlUnsignedInt xgetSize();

    boolean isSetSize();

    void setSize(long j);

    void xsetSize(XmlUnsignedInt xmlUnsignedInt);

    void unsetSize();

    long getDxfId();

    STDxfId xgetDxfId();

    boolean isSetDxfId();

    void setDxfId(long j);

    void xsetDxfId(STDxfId sTDxfId);

    void unsetDxfId();
}
