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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTblPrBase.class */
public interface CTTblPrBase extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTblPrBase.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttblprbaseeba1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTblPrBase$Factory.class */
    public static final class Factory {
        public static CTTblPrBase newInstance() {
            return (CTTblPrBase) POIXMLTypeLoader.newInstance(CTTblPrBase.type, null);
        }

        public static CTTblPrBase newInstance(XmlOptions xmlOptions) {
            return (CTTblPrBase) POIXMLTypeLoader.newInstance(CTTblPrBase.type, xmlOptions);
        }

        public static CTTblPrBase parse(String str) throws XmlException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(str, CTTblPrBase.type, (XmlOptions) null);
        }

        public static CTTblPrBase parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(str, CTTblPrBase.type, xmlOptions);
        }

        public static CTTblPrBase parse(File file) throws XmlException, IOException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(file, CTTblPrBase.type, (XmlOptions) null);
        }

        public static CTTblPrBase parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(file, CTTblPrBase.type, xmlOptions);
        }

        public static CTTblPrBase parse(URL url) throws XmlException, IOException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(url, CTTblPrBase.type, (XmlOptions) null);
        }

        public static CTTblPrBase parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(url, CTTblPrBase.type, xmlOptions);
        }

        public static CTTblPrBase parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(inputStream, CTTblPrBase.type, (XmlOptions) null);
        }

        public static CTTblPrBase parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(inputStream, CTTblPrBase.type, xmlOptions);
        }

        public static CTTblPrBase parse(Reader reader) throws XmlException, IOException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(reader, CTTblPrBase.type, (XmlOptions) null);
        }

        public static CTTblPrBase parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(reader, CTTblPrBase.type, xmlOptions);
        }

        public static CTTblPrBase parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(xMLStreamReader, CTTblPrBase.type, (XmlOptions) null);
        }

        public static CTTblPrBase parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(xMLStreamReader, CTTblPrBase.type, xmlOptions);
        }

        public static CTTblPrBase parse(Node node) throws XmlException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(node, CTTblPrBase.type, (XmlOptions) null);
        }

        public static CTTblPrBase parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(node, CTTblPrBase.type, xmlOptions);
        }

        public static CTTblPrBase parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(xMLInputStream, CTTblPrBase.type, (XmlOptions) null);
        }

        public static CTTblPrBase parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTblPrBase) POIXMLTypeLoader.parse(xMLInputStream, CTTblPrBase.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTblPrBase.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTblPrBase.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTString getTblStyle();

    boolean isSetTblStyle();

    void setTblStyle(CTString cTString);

    CTString addNewTblStyle();

    void unsetTblStyle();

    CTTblPPr getTblpPr();

    boolean isSetTblpPr();

    void setTblpPr(CTTblPPr cTTblPPr);

    CTTblPPr addNewTblpPr();

    void unsetTblpPr();

    CTTblOverlap getTblOverlap();

    boolean isSetTblOverlap();

    void setTblOverlap(CTTblOverlap cTTblOverlap);

    CTTblOverlap addNewTblOverlap();

    void unsetTblOverlap();

    CTOnOff getBidiVisual();

    boolean isSetBidiVisual();

    void setBidiVisual(CTOnOff cTOnOff);

    CTOnOff addNewBidiVisual();

    void unsetBidiVisual();

    CTDecimalNumber getTblStyleRowBandSize();

    boolean isSetTblStyleRowBandSize();

    void setTblStyleRowBandSize(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewTblStyleRowBandSize();

    void unsetTblStyleRowBandSize();

    CTDecimalNumber getTblStyleColBandSize();

    boolean isSetTblStyleColBandSize();

    void setTblStyleColBandSize(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewTblStyleColBandSize();

    void unsetTblStyleColBandSize();

    CTTblWidth getTblW();

    boolean isSetTblW();

    void setTblW(CTTblWidth cTTblWidth);

    CTTblWidth addNewTblW();

    void unsetTblW();

    CTJc getJc();

    boolean isSetJc();

    void setJc(CTJc cTJc);

    CTJc addNewJc();

    void unsetJc();

    CTTblWidth getTblCellSpacing();

    boolean isSetTblCellSpacing();

    void setTblCellSpacing(CTTblWidth cTTblWidth);

    CTTblWidth addNewTblCellSpacing();

    void unsetTblCellSpacing();

    CTTblWidth getTblInd();

    boolean isSetTblInd();

    void setTblInd(CTTblWidth cTTblWidth);

    CTTblWidth addNewTblInd();

    void unsetTblInd();

    CTTblBorders getTblBorders();

    boolean isSetTblBorders();

    void setTblBorders(CTTblBorders cTTblBorders);

    CTTblBorders addNewTblBorders();

    void unsetTblBorders();

    CTShd getShd();

    boolean isSetShd();

    void setShd(CTShd cTShd);

    CTShd addNewShd();

    void unsetShd();

    CTTblLayoutType getTblLayout();

    boolean isSetTblLayout();

    void setTblLayout(CTTblLayoutType cTTblLayoutType);

    CTTblLayoutType addNewTblLayout();

    void unsetTblLayout();

    CTTblCellMar getTblCellMar();

    boolean isSetTblCellMar();

    void setTblCellMar(CTTblCellMar cTTblCellMar);

    CTTblCellMar addNewTblCellMar();

    void unsetTblCellMar();

    CTShortHexNumber getTblLook();

    boolean isSetTblLook();

    void setTblLook(CTShortHexNumber cTShortHexNumber);

    CTShortHexNumber addNewTblLook();

    void unsetTblLook();
}
