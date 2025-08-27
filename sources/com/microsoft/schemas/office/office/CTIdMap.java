package com.microsoft.schemas.office.office;

import com.microsoft.schemas.vml.STExt;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/CTIdMap.class */
public interface CTIdMap extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTIdMap.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctidmap63fatype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/CTIdMap$Factory.class */
    public static final class Factory {
        public static CTIdMap newInstance() {
            return (CTIdMap) POIXMLTypeLoader.newInstance(CTIdMap.type, null);
        }

        public static CTIdMap newInstance(XmlOptions xmlOptions) {
            return (CTIdMap) POIXMLTypeLoader.newInstance(CTIdMap.type, xmlOptions);
        }

        public static CTIdMap parse(String str) throws XmlException {
            return (CTIdMap) POIXMLTypeLoader.parse(str, CTIdMap.type, (XmlOptions) null);
        }

        public static CTIdMap parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTIdMap) POIXMLTypeLoader.parse(str, CTIdMap.type, xmlOptions);
        }

        public static CTIdMap parse(File file) throws XmlException, IOException {
            return (CTIdMap) POIXMLTypeLoader.parse(file, CTIdMap.type, (XmlOptions) null);
        }

        public static CTIdMap parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTIdMap) POIXMLTypeLoader.parse(file, CTIdMap.type, xmlOptions);
        }

        public static CTIdMap parse(URL url) throws XmlException, IOException {
            return (CTIdMap) POIXMLTypeLoader.parse(url, CTIdMap.type, (XmlOptions) null);
        }

        public static CTIdMap parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTIdMap) POIXMLTypeLoader.parse(url, CTIdMap.type, xmlOptions);
        }

        public static CTIdMap parse(InputStream inputStream) throws XmlException, IOException {
            return (CTIdMap) POIXMLTypeLoader.parse(inputStream, CTIdMap.type, (XmlOptions) null);
        }

        public static CTIdMap parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTIdMap) POIXMLTypeLoader.parse(inputStream, CTIdMap.type, xmlOptions);
        }

        public static CTIdMap parse(Reader reader) throws XmlException, IOException {
            return (CTIdMap) POIXMLTypeLoader.parse(reader, CTIdMap.type, (XmlOptions) null);
        }

        public static CTIdMap parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTIdMap) POIXMLTypeLoader.parse(reader, CTIdMap.type, xmlOptions);
        }

        public static CTIdMap parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTIdMap) POIXMLTypeLoader.parse(xMLStreamReader, CTIdMap.type, (XmlOptions) null);
        }

        public static CTIdMap parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTIdMap) POIXMLTypeLoader.parse(xMLStreamReader, CTIdMap.type, xmlOptions);
        }

        public static CTIdMap parse(Node node) throws XmlException {
            return (CTIdMap) POIXMLTypeLoader.parse(node, CTIdMap.type, (XmlOptions) null);
        }

        public static CTIdMap parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTIdMap) POIXMLTypeLoader.parse(node, CTIdMap.type, xmlOptions);
        }

        public static CTIdMap parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTIdMap) POIXMLTypeLoader.parse(xMLInputStream, CTIdMap.type, (XmlOptions) null);
        }

        public static CTIdMap parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTIdMap) POIXMLTypeLoader.parse(xMLInputStream, CTIdMap.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTIdMap.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTIdMap.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STExt.Enum getExt();

    STExt xgetExt();

    boolean isSetExt();

    void setExt(STExt.Enum r1);

    void xsetExt(STExt sTExt);

    void unsetExt();

    String getData();

    XmlString xgetData();

    boolean isSetData();

    void setData(String str);

    void xsetData(XmlString xmlString);

    void unsetData();
}
