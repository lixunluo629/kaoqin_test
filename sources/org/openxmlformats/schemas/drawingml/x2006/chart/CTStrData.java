package org.openxmlformats.schemas.drawingml.x2006.chart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTStrData.class */
public interface CTStrData extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTStrData.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctstrdatad58btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTStrData$Factory.class */
    public static final class Factory {
        public static CTStrData newInstance() {
            return (CTStrData) POIXMLTypeLoader.newInstance(CTStrData.type, null);
        }

        public static CTStrData newInstance(XmlOptions xmlOptions) {
            return (CTStrData) POIXMLTypeLoader.newInstance(CTStrData.type, xmlOptions);
        }

        public static CTStrData parse(String str) throws XmlException {
            return (CTStrData) POIXMLTypeLoader.parse(str, CTStrData.type, (XmlOptions) null);
        }

        public static CTStrData parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTStrData) POIXMLTypeLoader.parse(str, CTStrData.type, xmlOptions);
        }

        public static CTStrData parse(File file) throws XmlException, IOException {
            return (CTStrData) POIXMLTypeLoader.parse(file, CTStrData.type, (XmlOptions) null);
        }

        public static CTStrData parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStrData) POIXMLTypeLoader.parse(file, CTStrData.type, xmlOptions);
        }

        public static CTStrData parse(URL url) throws XmlException, IOException {
            return (CTStrData) POIXMLTypeLoader.parse(url, CTStrData.type, (XmlOptions) null);
        }

        public static CTStrData parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStrData) POIXMLTypeLoader.parse(url, CTStrData.type, xmlOptions);
        }

        public static CTStrData parse(InputStream inputStream) throws XmlException, IOException {
            return (CTStrData) POIXMLTypeLoader.parse(inputStream, CTStrData.type, (XmlOptions) null);
        }

        public static CTStrData parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStrData) POIXMLTypeLoader.parse(inputStream, CTStrData.type, xmlOptions);
        }

        public static CTStrData parse(Reader reader) throws XmlException, IOException {
            return (CTStrData) POIXMLTypeLoader.parse(reader, CTStrData.type, (XmlOptions) null);
        }

        public static CTStrData parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStrData) POIXMLTypeLoader.parse(reader, CTStrData.type, xmlOptions);
        }

        public static CTStrData parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTStrData) POIXMLTypeLoader.parse(xMLStreamReader, CTStrData.type, (XmlOptions) null);
        }

        public static CTStrData parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTStrData) POIXMLTypeLoader.parse(xMLStreamReader, CTStrData.type, xmlOptions);
        }

        public static CTStrData parse(Node node) throws XmlException {
            return (CTStrData) POIXMLTypeLoader.parse(node, CTStrData.type, (XmlOptions) null);
        }

        public static CTStrData parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTStrData) POIXMLTypeLoader.parse(node, CTStrData.type, xmlOptions);
        }

        public static CTStrData parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTStrData) POIXMLTypeLoader.parse(xMLInputStream, CTStrData.type, (XmlOptions) null);
        }

        public static CTStrData parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTStrData) POIXMLTypeLoader.parse(xMLInputStream, CTStrData.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStrData.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStrData.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTUnsignedInt getPtCount();

    boolean isSetPtCount();

    void setPtCount(CTUnsignedInt cTUnsignedInt);

    CTUnsignedInt addNewPtCount();

    void unsetPtCount();

    List<CTStrVal> getPtList();

    CTStrVal[] getPtArray();

    CTStrVal getPtArray(int i);

    int sizeOfPtArray();

    void setPtArray(CTStrVal[] cTStrValArr);

    void setPtArray(int i, CTStrVal cTStrVal);

    CTStrVal insertNewPt(int i);

    CTStrVal addNewPt();

    void removePt(int i);

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
