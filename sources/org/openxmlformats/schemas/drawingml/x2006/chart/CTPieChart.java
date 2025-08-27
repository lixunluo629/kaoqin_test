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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTPieChart.class */
public interface CTPieChart extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPieChart.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpiechartd34atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTPieChart$Factory.class */
    public static final class Factory {
        public static CTPieChart newInstance() {
            return (CTPieChart) POIXMLTypeLoader.newInstance(CTPieChart.type, null);
        }

        public static CTPieChart newInstance(XmlOptions xmlOptions) {
            return (CTPieChart) POIXMLTypeLoader.newInstance(CTPieChart.type, xmlOptions);
        }

        public static CTPieChart parse(String str) throws XmlException {
            return (CTPieChart) POIXMLTypeLoader.parse(str, CTPieChart.type, (XmlOptions) null);
        }

        public static CTPieChart parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPieChart) POIXMLTypeLoader.parse(str, CTPieChart.type, xmlOptions);
        }

        public static CTPieChart parse(File file) throws XmlException, IOException {
            return (CTPieChart) POIXMLTypeLoader.parse(file, CTPieChart.type, (XmlOptions) null);
        }

        public static CTPieChart parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPieChart) POIXMLTypeLoader.parse(file, CTPieChart.type, xmlOptions);
        }

        public static CTPieChart parse(URL url) throws XmlException, IOException {
            return (CTPieChart) POIXMLTypeLoader.parse(url, CTPieChart.type, (XmlOptions) null);
        }

        public static CTPieChart parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPieChart) POIXMLTypeLoader.parse(url, CTPieChart.type, xmlOptions);
        }

        public static CTPieChart parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPieChart) POIXMLTypeLoader.parse(inputStream, CTPieChart.type, (XmlOptions) null);
        }

        public static CTPieChart parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPieChart) POIXMLTypeLoader.parse(inputStream, CTPieChart.type, xmlOptions);
        }

        public static CTPieChart parse(Reader reader) throws XmlException, IOException {
            return (CTPieChart) POIXMLTypeLoader.parse(reader, CTPieChart.type, (XmlOptions) null);
        }

        public static CTPieChart parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPieChart) POIXMLTypeLoader.parse(reader, CTPieChart.type, xmlOptions);
        }

        public static CTPieChart parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPieChart) POIXMLTypeLoader.parse(xMLStreamReader, CTPieChart.type, (XmlOptions) null);
        }

        public static CTPieChart parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPieChart) POIXMLTypeLoader.parse(xMLStreamReader, CTPieChart.type, xmlOptions);
        }

        public static CTPieChart parse(Node node) throws XmlException {
            return (CTPieChart) POIXMLTypeLoader.parse(node, CTPieChart.type, (XmlOptions) null);
        }

        public static CTPieChart parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPieChart) POIXMLTypeLoader.parse(node, CTPieChart.type, xmlOptions);
        }

        public static CTPieChart parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPieChart) POIXMLTypeLoader.parse(xMLInputStream, CTPieChart.type, (XmlOptions) null);
        }

        public static CTPieChart parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPieChart) POIXMLTypeLoader.parse(xMLInputStream, CTPieChart.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPieChart.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPieChart.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTBoolean getVaryColors();

    boolean isSetVaryColors();

    void setVaryColors(CTBoolean cTBoolean);

    CTBoolean addNewVaryColors();

    void unsetVaryColors();

    List<CTPieSer> getSerList();

    CTPieSer[] getSerArray();

    CTPieSer getSerArray(int i);

    int sizeOfSerArray();

    void setSerArray(CTPieSer[] cTPieSerArr);

    void setSerArray(int i, CTPieSer cTPieSer);

    CTPieSer insertNewSer(int i);

    CTPieSer addNewSer();

    void removeSer(int i);

    CTDLbls getDLbls();

    boolean isSetDLbls();

    void setDLbls(CTDLbls cTDLbls);

    CTDLbls addNewDLbls();

    void unsetDLbls();

    CTFirstSliceAng getFirstSliceAng();

    boolean isSetFirstSliceAng();

    void setFirstSliceAng(CTFirstSliceAng cTFirstSliceAng);

    CTFirstSliceAng addNewFirstSliceAng();

    void unsetFirstSliceAng();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
