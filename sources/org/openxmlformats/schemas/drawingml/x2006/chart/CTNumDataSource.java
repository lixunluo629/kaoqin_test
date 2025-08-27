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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTNumDataSource.class */
public interface CTNumDataSource extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTNumDataSource.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctnumdatasourcef0bbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTNumDataSource$Factory.class */
    public static final class Factory {
        public static CTNumDataSource newInstance() {
            return (CTNumDataSource) POIXMLTypeLoader.newInstance(CTNumDataSource.type, null);
        }

        public static CTNumDataSource newInstance(XmlOptions xmlOptions) {
            return (CTNumDataSource) POIXMLTypeLoader.newInstance(CTNumDataSource.type, xmlOptions);
        }

        public static CTNumDataSource parse(String str) throws XmlException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(str, CTNumDataSource.type, (XmlOptions) null);
        }

        public static CTNumDataSource parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(str, CTNumDataSource.type, xmlOptions);
        }

        public static CTNumDataSource parse(File file) throws XmlException, IOException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(file, CTNumDataSource.type, (XmlOptions) null);
        }

        public static CTNumDataSource parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(file, CTNumDataSource.type, xmlOptions);
        }

        public static CTNumDataSource parse(URL url) throws XmlException, IOException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(url, CTNumDataSource.type, (XmlOptions) null);
        }

        public static CTNumDataSource parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(url, CTNumDataSource.type, xmlOptions);
        }

        public static CTNumDataSource parse(InputStream inputStream) throws XmlException, IOException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(inputStream, CTNumDataSource.type, (XmlOptions) null);
        }

        public static CTNumDataSource parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(inputStream, CTNumDataSource.type, xmlOptions);
        }

        public static CTNumDataSource parse(Reader reader) throws XmlException, IOException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(reader, CTNumDataSource.type, (XmlOptions) null);
        }

        public static CTNumDataSource parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(reader, CTNumDataSource.type, xmlOptions);
        }

        public static CTNumDataSource parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(xMLStreamReader, CTNumDataSource.type, (XmlOptions) null);
        }

        public static CTNumDataSource parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(xMLStreamReader, CTNumDataSource.type, xmlOptions);
        }

        public static CTNumDataSource parse(Node node) throws XmlException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(node, CTNumDataSource.type, (XmlOptions) null);
        }

        public static CTNumDataSource parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(node, CTNumDataSource.type, xmlOptions);
        }

        public static CTNumDataSource parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(xMLInputStream, CTNumDataSource.type, (XmlOptions) null);
        }

        public static CTNumDataSource parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTNumDataSource) POIXMLTypeLoader.parse(xMLInputStream, CTNumDataSource.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNumDataSource.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNumDataSource.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTNumRef getNumRef();

    boolean isSetNumRef();

    void setNumRef(CTNumRef cTNumRef);

    CTNumRef addNewNumRef();

    void unsetNumRef();

    CTNumData getNumLit();

    boolean isSetNumLit();

    void setNumLit(CTNumData cTNumData);

    CTNumData addNewNumLit();

    void unsetNumLit();
}
