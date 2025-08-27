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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTcPrBase.class */
public interface CTTcPrBase extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTcPrBase.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttcprbase93e6type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTcPrBase$Factory.class */
    public static final class Factory {
        public static CTTcPrBase newInstance() {
            return (CTTcPrBase) POIXMLTypeLoader.newInstance(CTTcPrBase.type, null);
        }

        public static CTTcPrBase newInstance(XmlOptions xmlOptions) {
            return (CTTcPrBase) POIXMLTypeLoader.newInstance(CTTcPrBase.type, xmlOptions);
        }

        public static CTTcPrBase parse(String str) throws XmlException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(str, CTTcPrBase.type, (XmlOptions) null);
        }

        public static CTTcPrBase parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(str, CTTcPrBase.type, xmlOptions);
        }

        public static CTTcPrBase parse(File file) throws XmlException, IOException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(file, CTTcPrBase.type, (XmlOptions) null);
        }

        public static CTTcPrBase parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(file, CTTcPrBase.type, xmlOptions);
        }

        public static CTTcPrBase parse(URL url) throws XmlException, IOException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(url, CTTcPrBase.type, (XmlOptions) null);
        }

        public static CTTcPrBase parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(url, CTTcPrBase.type, xmlOptions);
        }

        public static CTTcPrBase parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(inputStream, CTTcPrBase.type, (XmlOptions) null);
        }

        public static CTTcPrBase parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(inputStream, CTTcPrBase.type, xmlOptions);
        }

        public static CTTcPrBase parse(Reader reader) throws XmlException, IOException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(reader, CTTcPrBase.type, (XmlOptions) null);
        }

        public static CTTcPrBase parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(reader, CTTcPrBase.type, xmlOptions);
        }

        public static CTTcPrBase parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(xMLStreamReader, CTTcPrBase.type, (XmlOptions) null);
        }

        public static CTTcPrBase parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(xMLStreamReader, CTTcPrBase.type, xmlOptions);
        }

        public static CTTcPrBase parse(Node node) throws XmlException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(node, CTTcPrBase.type, (XmlOptions) null);
        }

        public static CTTcPrBase parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(node, CTTcPrBase.type, xmlOptions);
        }

        public static CTTcPrBase parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(xMLInputStream, CTTcPrBase.type, (XmlOptions) null);
        }

        public static CTTcPrBase parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTcPrBase) POIXMLTypeLoader.parse(xMLInputStream, CTTcPrBase.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTcPrBase.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTcPrBase.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTCnf getCnfStyle();

    boolean isSetCnfStyle();

    void setCnfStyle(CTCnf cTCnf);

    CTCnf addNewCnfStyle();

    void unsetCnfStyle();

    CTTblWidth getTcW();

    boolean isSetTcW();

    void setTcW(CTTblWidth cTTblWidth);

    CTTblWidth addNewTcW();

    void unsetTcW();

    CTDecimalNumber getGridSpan();

    boolean isSetGridSpan();

    void setGridSpan(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewGridSpan();

    void unsetGridSpan();

    CTHMerge getHMerge();

    boolean isSetHMerge();

    void setHMerge(CTHMerge cTHMerge);

    CTHMerge addNewHMerge();

    void unsetHMerge();

    CTVMerge getVMerge();

    boolean isSetVMerge();

    void setVMerge(CTVMerge cTVMerge);

    CTVMerge addNewVMerge();

    void unsetVMerge();

    CTTcBorders getTcBorders();

    boolean isSetTcBorders();

    void setTcBorders(CTTcBorders cTTcBorders);

    CTTcBorders addNewTcBorders();

    void unsetTcBorders();

    CTShd getShd();

    boolean isSetShd();

    void setShd(CTShd cTShd);

    CTShd addNewShd();

    void unsetShd();

    CTOnOff getNoWrap();

    boolean isSetNoWrap();

    void setNoWrap(CTOnOff cTOnOff);

    CTOnOff addNewNoWrap();

    void unsetNoWrap();

    CTTcMar getTcMar();

    boolean isSetTcMar();

    void setTcMar(CTTcMar cTTcMar);

    CTTcMar addNewTcMar();

    void unsetTcMar();

    CTTextDirection getTextDirection();

    boolean isSetTextDirection();

    void setTextDirection(CTTextDirection cTTextDirection);

    CTTextDirection addNewTextDirection();

    void unsetTextDirection();

    CTOnOff getTcFitText();

    boolean isSetTcFitText();

    void setTcFitText(CTOnOff cTOnOff);

    CTOnOff addNewTcFitText();

    void unsetTcFitText();

    CTVerticalJc getVAlign();

    boolean isSetVAlign();

    void setVAlign(CTVerticalJc cTVerticalJc);

    CTVerticalJc addNewVAlign();

    void unsetVAlign();

    CTOnOff getHideMark();

    boolean isSetHideMark();

    void setHideMark(CTOnOff cTOnOff);

    CTOnOff addNewHideMark();

    void unsetHideMark();
}
