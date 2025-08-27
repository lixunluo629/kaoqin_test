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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTScatterChart.class */
public interface CTScatterChart extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTScatterChart.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctscatterchart2bfctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTScatterChart$Factory.class */
    public static final class Factory {
        public static CTScatterChart newInstance() {
            return (CTScatterChart) POIXMLTypeLoader.newInstance(CTScatterChart.type, null);
        }

        public static CTScatterChart newInstance(XmlOptions xmlOptions) {
            return (CTScatterChart) POIXMLTypeLoader.newInstance(CTScatterChart.type, xmlOptions);
        }

        public static CTScatterChart parse(String str) throws XmlException {
            return (CTScatterChart) POIXMLTypeLoader.parse(str, CTScatterChart.type, (XmlOptions) null);
        }

        public static CTScatterChart parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTScatterChart) POIXMLTypeLoader.parse(str, CTScatterChart.type, xmlOptions);
        }

        public static CTScatterChart parse(File file) throws XmlException, IOException {
            return (CTScatterChart) POIXMLTypeLoader.parse(file, CTScatterChart.type, (XmlOptions) null);
        }

        public static CTScatterChart parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScatterChart) POIXMLTypeLoader.parse(file, CTScatterChart.type, xmlOptions);
        }

        public static CTScatterChart parse(URL url) throws XmlException, IOException {
            return (CTScatterChart) POIXMLTypeLoader.parse(url, CTScatterChart.type, (XmlOptions) null);
        }

        public static CTScatterChart parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScatterChart) POIXMLTypeLoader.parse(url, CTScatterChart.type, xmlOptions);
        }

        public static CTScatterChart parse(InputStream inputStream) throws XmlException, IOException {
            return (CTScatterChart) POIXMLTypeLoader.parse(inputStream, CTScatterChart.type, (XmlOptions) null);
        }

        public static CTScatterChart parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScatterChart) POIXMLTypeLoader.parse(inputStream, CTScatterChart.type, xmlOptions);
        }

        public static CTScatterChart parse(Reader reader) throws XmlException, IOException {
            return (CTScatterChart) POIXMLTypeLoader.parse(reader, CTScatterChart.type, (XmlOptions) null);
        }

        public static CTScatterChart parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScatterChart) POIXMLTypeLoader.parse(reader, CTScatterChart.type, xmlOptions);
        }

        public static CTScatterChart parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTScatterChart) POIXMLTypeLoader.parse(xMLStreamReader, CTScatterChart.type, (XmlOptions) null);
        }

        public static CTScatterChart parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTScatterChart) POIXMLTypeLoader.parse(xMLStreamReader, CTScatterChart.type, xmlOptions);
        }

        public static CTScatterChart parse(Node node) throws XmlException {
            return (CTScatterChart) POIXMLTypeLoader.parse(node, CTScatterChart.type, (XmlOptions) null);
        }

        public static CTScatterChart parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTScatterChart) POIXMLTypeLoader.parse(node, CTScatterChart.type, xmlOptions);
        }

        public static CTScatterChart parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTScatterChart) POIXMLTypeLoader.parse(xMLInputStream, CTScatterChart.type, (XmlOptions) null);
        }

        public static CTScatterChart parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTScatterChart) POIXMLTypeLoader.parse(xMLInputStream, CTScatterChart.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTScatterChart.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTScatterChart.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTScatterStyle getScatterStyle();

    void setScatterStyle(CTScatterStyle cTScatterStyle);

    CTScatterStyle addNewScatterStyle();

    CTBoolean getVaryColors();

    boolean isSetVaryColors();

    void setVaryColors(CTBoolean cTBoolean);

    CTBoolean addNewVaryColors();

    void unsetVaryColors();

    List<CTScatterSer> getSerList();

    CTScatterSer[] getSerArray();

    CTScatterSer getSerArray(int i);

    int sizeOfSerArray();

    void setSerArray(CTScatterSer[] cTScatterSerArr);

    void setSerArray(int i, CTScatterSer cTScatterSer);

    CTScatterSer insertNewSer(int i);

    CTScatterSer addNewSer();

    void removeSer(int i);

    CTDLbls getDLbls();

    boolean isSetDLbls();

    void setDLbls(CTDLbls cTDLbls);

    CTDLbls addNewDLbls();

    void unsetDLbls();

    List<CTUnsignedInt> getAxIdList();

    CTUnsignedInt[] getAxIdArray();

    CTUnsignedInt getAxIdArray(int i);

    int sizeOfAxIdArray();

    void setAxIdArray(CTUnsignedInt[] cTUnsignedIntArr);

    void setAxIdArray(int i, CTUnsignedInt cTUnsignedInt);

    CTUnsignedInt insertNewAxId(int i);

    CTUnsignedInt addNewAxId();

    void removeAxId(int i);

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
