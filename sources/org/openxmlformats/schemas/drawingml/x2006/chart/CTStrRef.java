package org.openxmlformats.schemas.drawingml.x2006.chart;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTStrRef.class */
public interface CTStrRef extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTStrRef.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctstrref5d1atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTStrRef$Factory.class */
    public static final class Factory {
        public static CTStrRef newInstance() {
            return (CTStrRef) POIXMLTypeLoader.newInstance(CTStrRef.type, null);
        }

        public static CTStrRef newInstance(XmlOptions xmlOptions) {
            return (CTStrRef) POIXMLTypeLoader.newInstance(CTStrRef.type, xmlOptions);
        }

        public static CTStrRef parse(String str) throws XmlException {
            return (CTStrRef) POIXMLTypeLoader.parse(str, CTStrRef.type, (XmlOptions) null);
        }

        public static CTStrRef parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTStrRef) POIXMLTypeLoader.parse(str, CTStrRef.type, xmlOptions);
        }

        public static CTStrRef parse(File file) throws XmlException, IOException {
            return (CTStrRef) POIXMLTypeLoader.parse(file, CTStrRef.type, (XmlOptions) null);
        }

        public static CTStrRef parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStrRef) POIXMLTypeLoader.parse(file, CTStrRef.type, xmlOptions);
        }

        public static CTStrRef parse(URL url) throws XmlException, IOException {
            return (CTStrRef) POIXMLTypeLoader.parse(url, CTStrRef.type, (XmlOptions) null);
        }

        public static CTStrRef parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStrRef) POIXMLTypeLoader.parse(url, CTStrRef.type, xmlOptions);
        }

        public static CTStrRef parse(InputStream inputStream) throws XmlException, IOException {
            return (CTStrRef) POIXMLTypeLoader.parse(inputStream, CTStrRef.type, (XmlOptions) null);
        }

        public static CTStrRef parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStrRef) POIXMLTypeLoader.parse(inputStream, CTStrRef.type, xmlOptions);
        }

        public static CTStrRef parse(Reader reader) throws XmlException, IOException {
            return (CTStrRef) POIXMLTypeLoader.parse(reader, CTStrRef.type, (XmlOptions) null);
        }

        public static CTStrRef parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStrRef) POIXMLTypeLoader.parse(reader, CTStrRef.type, xmlOptions);
        }

        public static CTStrRef parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTStrRef) POIXMLTypeLoader.parse(xMLStreamReader, CTStrRef.type, (XmlOptions) null);
        }

        public static CTStrRef parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTStrRef) POIXMLTypeLoader.parse(xMLStreamReader, CTStrRef.type, xmlOptions);
        }

        public static CTStrRef parse(Node node) throws XmlException {
            return (CTStrRef) POIXMLTypeLoader.parse(node, CTStrRef.type, (XmlOptions) null);
        }

        public static CTStrRef parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTStrRef) POIXMLTypeLoader.parse(node, CTStrRef.type, xmlOptions);
        }

        public static CTStrRef parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTStrRef) POIXMLTypeLoader.parse(xMLInputStream, CTStrRef.type, (XmlOptions) null);
        }

        public static CTStrRef parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTStrRef) POIXMLTypeLoader.parse(xMLInputStream, CTStrRef.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStrRef.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStrRef.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getF();

    XmlString xgetF();

    void setF(String str);

    void xsetF(XmlString xmlString);

    CTStrData getStrCache();

    boolean isSetStrCache();

    void setStrCache(CTStrData cTStrData);

    CTStrData addNewStrCache();

    void unsetStrCache();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
