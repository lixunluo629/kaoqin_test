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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTblPrExBase.class */
public interface CTTblPrExBase extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTblPrExBase.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttblprexbasee7eetype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTblPrExBase$Factory.class */
    public static final class Factory {
        public static CTTblPrExBase newInstance() {
            return (CTTblPrExBase) POIXMLTypeLoader.newInstance(CTTblPrExBase.type, null);
        }

        public static CTTblPrExBase newInstance(XmlOptions xmlOptions) {
            return (CTTblPrExBase) POIXMLTypeLoader.newInstance(CTTblPrExBase.type, xmlOptions);
        }

        public static CTTblPrExBase parse(String str) throws XmlException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(str, CTTblPrExBase.type, (XmlOptions) null);
        }

        public static CTTblPrExBase parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(str, CTTblPrExBase.type, xmlOptions);
        }

        public static CTTblPrExBase parse(File file) throws XmlException, IOException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(file, CTTblPrExBase.type, (XmlOptions) null);
        }

        public static CTTblPrExBase parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(file, CTTblPrExBase.type, xmlOptions);
        }

        public static CTTblPrExBase parse(URL url) throws XmlException, IOException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(url, CTTblPrExBase.type, (XmlOptions) null);
        }

        public static CTTblPrExBase parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(url, CTTblPrExBase.type, xmlOptions);
        }

        public static CTTblPrExBase parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(inputStream, CTTblPrExBase.type, (XmlOptions) null);
        }

        public static CTTblPrExBase parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(inputStream, CTTblPrExBase.type, xmlOptions);
        }

        public static CTTblPrExBase parse(Reader reader) throws XmlException, IOException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(reader, CTTblPrExBase.type, (XmlOptions) null);
        }

        public static CTTblPrExBase parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(reader, CTTblPrExBase.type, xmlOptions);
        }

        public static CTTblPrExBase parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(xMLStreamReader, CTTblPrExBase.type, (XmlOptions) null);
        }

        public static CTTblPrExBase parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(xMLStreamReader, CTTblPrExBase.type, xmlOptions);
        }

        public static CTTblPrExBase parse(Node node) throws XmlException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(node, CTTblPrExBase.type, (XmlOptions) null);
        }

        public static CTTblPrExBase parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(node, CTTblPrExBase.type, xmlOptions);
        }

        public static CTTblPrExBase parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(xMLInputStream, CTTblPrExBase.type, (XmlOptions) null);
        }

        public static CTTblPrExBase parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTblPrExBase) POIXMLTypeLoader.parse(xMLInputStream, CTTblPrExBase.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTblPrExBase.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTblPrExBase.type, xmlOptions);
        }

        private Factory() {
        }
    }

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
