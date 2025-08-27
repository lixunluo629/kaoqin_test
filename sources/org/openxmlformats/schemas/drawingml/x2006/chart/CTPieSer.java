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
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTPieSer.class */
public interface CTPieSer extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPieSer.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpieser5248type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTPieSer$Factory.class */
    public static final class Factory {
        public static CTPieSer newInstance() {
            return (CTPieSer) POIXMLTypeLoader.newInstance(CTPieSer.type, null);
        }

        public static CTPieSer newInstance(XmlOptions xmlOptions) {
            return (CTPieSer) POIXMLTypeLoader.newInstance(CTPieSer.type, xmlOptions);
        }

        public static CTPieSer parse(String str) throws XmlException {
            return (CTPieSer) POIXMLTypeLoader.parse(str, CTPieSer.type, (XmlOptions) null);
        }

        public static CTPieSer parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPieSer) POIXMLTypeLoader.parse(str, CTPieSer.type, xmlOptions);
        }

        public static CTPieSer parse(File file) throws XmlException, IOException {
            return (CTPieSer) POIXMLTypeLoader.parse(file, CTPieSer.type, (XmlOptions) null);
        }

        public static CTPieSer parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPieSer) POIXMLTypeLoader.parse(file, CTPieSer.type, xmlOptions);
        }

        public static CTPieSer parse(URL url) throws XmlException, IOException {
            return (CTPieSer) POIXMLTypeLoader.parse(url, CTPieSer.type, (XmlOptions) null);
        }

        public static CTPieSer parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPieSer) POIXMLTypeLoader.parse(url, CTPieSer.type, xmlOptions);
        }

        public static CTPieSer parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPieSer) POIXMLTypeLoader.parse(inputStream, CTPieSer.type, (XmlOptions) null);
        }

        public static CTPieSer parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPieSer) POIXMLTypeLoader.parse(inputStream, CTPieSer.type, xmlOptions);
        }

        public static CTPieSer parse(Reader reader) throws XmlException, IOException {
            return (CTPieSer) POIXMLTypeLoader.parse(reader, CTPieSer.type, (XmlOptions) null);
        }

        public static CTPieSer parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPieSer) POIXMLTypeLoader.parse(reader, CTPieSer.type, xmlOptions);
        }

        public static CTPieSer parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPieSer) POIXMLTypeLoader.parse(xMLStreamReader, CTPieSer.type, (XmlOptions) null);
        }

        public static CTPieSer parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPieSer) POIXMLTypeLoader.parse(xMLStreamReader, CTPieSer.type, xmlOptions);
        }

        public static CTPieSer parse(Node node) throws XmlException {
            return (CTPieSer) POIXMLTypeLoader.parse(node, CTPieSer.type, (XmlOptions) null);
        }

        public static CTPieSer parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPieSer) POIXMLTypeLoader.parse(node, CTPieSer.type, xmlOptions);
        }

        public static CTPieSer parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPieSer) POIXMLTypeLoader.parse(xMLInputStream, CTPieSer.type, (XmlOptions) null);
        }

        public static CTPieSer parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPieSer) POIXMLTypeLoader.parse(xMLInputStream, CTPieSer.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPieSer.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPieSer.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTUnsignedInt getIdx();

    void setIdx(CTUnsignedInt cTUnsignedInt);

    CTUnsignedInt addNewIdx();

    CTUnsignedInt getOrder();

    void setOrder(CTUnsignedInt cTUnsignedInt);

    CTUnsignedInt addNewOrder();

    CTSerTx getTx();

    boolean isSetTx();

    void setTx(CTSerTx cTSerTx);

    CTSerTx addNewTx();

    void unsetTx();

    CTShapeProperties getSpPr();

    boolean isSetSpPr();

    void setSpPr(CTShapeProperties cTShapeProperties);

    CTShapeProperties addNewSpPr();

    void unsetSpPr();

    CTUnsignedInt getExplosion();

    boolean isSetExplosion();

    void setExplosion(CTUnsignedInt cTUnsignedInt);

    CTUnsignedInt addNewExplosion();

    void unsetExplosion();

    List<CTDPt> getDPtList();

    CTDPt[] getDPtArray();

    CTDPt getDPtArray(int i);

    int sizeOfDPtArray();

    void setDPtArray(CTDPt[] cTDPtArr);

    void setDPtArray(int i, CTDPt cTDPt);

    CTDPt insertNewDPt(int i);

    CTDPt addNewDPt();

    void removeDPt(int i);

    CTDLbls getDLbls();

    boolean isSetDLbls();

    void setDLbls(CTDLbls cTDLbls);

    CTDLbls addNewDLbls();

    void unsetDLbls();

    CTAxDataSource getCat();

    boolean isSetCat();

    void setCat(CTAxDataSource cTAxDataSource);

    CTAxDataSource addNewCat();

    void unsetCat();

    CTNumDataSource getVal();

    boolean isSetVal();

    void setVal(CTNumDataSource cTNumDataSource);

    CTNumDataSource addNewVal();

    void unsetVal();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
