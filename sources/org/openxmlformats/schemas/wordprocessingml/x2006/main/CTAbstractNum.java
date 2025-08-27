package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTAbstractNum.class */
public interface CTAbstractNum extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTAbstractNum.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctabstractnum588etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTAbstractNum$Factory.class */
    public static final class Factory {
        public static CTAbstractNum newInstance() {
            return (CTAbstractNum) POIXMLTypeLoader.newInstance(CTAbstractNum.type, null);
        }

        public static CTAbstractNum newInstance(XmlOptions xmlOptions) {
            return (CTAbstractNum) POIXMLTypeLoader.newInstance(CTAbstractNum.type, xmlOptions);
        }

        public static CTAbstractNum parse(String str) throws XmlException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(str, CTAbstractNum.type, (XmlOptions) null);
        }

        public static CTAbstractNum parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(str, CTAbstractNum.type, xmlOptions);
        }

        public static CTAbstractNum parse(File file) throws XmlException, IOException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(file, CTAbstractNum.type, (XmlOptions) null);
        }

        public static CTAbstractNum parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(file, CTAbstractNum.type, xmlOptions);
        }

        public static CTAbstractNum parse(URL url) throws XmlException, IOException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(url, CTAbstractNum.type, (XmlOptions) null);
        }

        public static CTAbstractNum parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(url, CTAbstractNum.type, xmlOptions);
        }

        public static CTAbstractNum parse(InputStream inputStream) throws XmlException, IOException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(inputStream, CTAbstractNum.type, (XmlOptions) null);
        }

        public static CTAbstractNum parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(inputStream, CTAbstractNum.type, xmlOptions);
        }

        public static CTAbstractNum parse(Reader reader) throws XmlException, IOException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(reader, CTAbstractNum.type, (XmlOptions) null);
        }

        public static CTAbstractNum parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(reader, CTAbstractNum.type, xmlOptions);
        }

        public static CTAbstractNum parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(xMLStreamReader, CTAbstractNum.type, (XmlOptions) null);
        }

        public static CTAbstractNum parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(xMLStreamReader, CTAbstractNum.type, xmlOptions);
        }

        public static CTAbstractNum parse(Node node) throws XmlException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(node, CTAbstractNum.type, (XmlOptions) null);
        }

        public static CTAbstractNum parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(node, CTAbstractNum.type, xmlOptions);
        }

        public static CTAbstractNum parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(xMLInputStream, CTAbstractNum.type, (XmlOptions) null);
        }

        public static CTAbstractNum parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTAbstractNum) POIXMLTypeLoader.parse(xMLInputStream, CTAbstractNum.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAbstractNum.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAbstractNum.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTLongHexNumber getNsid();

    boolean isSetNsid();

    void setNsid(CTLongHexNumber cTLongHexNumber);

    CTLongHexNumber addNewNsid();

    void unsetNsid();

    CTMultiLevelType getMultiLevelType();

    boolean isSetMultiLevelType();

    void setMultiLevelType(CTMultiLevelType cTMultiLevelType);

    CTMultiLevelType addNewMultiLevelType();

    void unsetMultiLevelType();

    CTLongHexNumber getTmpl();

    boolean isSetTmpl();

    void setTmpl(CTLongHexNumber cTLongHexNumber);

    CTLongHexNumber addNewTmpl();

    void unsetTmpl();

    CTString getName();

    boolean isSetName();

    void setName(CTString cTString);

    CTString addNewName();

    void unsetName();

    CTString getStyleLink();

    boolean isSetStyleLink();

    void setStyleLink(CTString cTString);

    CTString addNewStyleLink();

    void unsetStyleLink();

    CTString getNumStyleLink();

    boolean isSetNumStyleLink();

    void setNumStyleLink(CTString cTString);

    CTString addNewNumStyleLink();

    void unsetNumStyleLink();

    List<CTLvl> getLvlList();

    CTLvl[] getLvlArray();

    CTLvl getLvlArray(int i);

    int sizeOfLvlArray();

    void setLvlArray(CTLvl[] cTLvlArr);

    void setLvlArray(int i, CTLvl cTLvl);

    CTLvl insertNewLvl(int i);

    CTLvl addNewLvl();

    void removeLvl(int i);

    BigInteger getAbstractNumId();

    STDecimalNumber xgetAbstractNumId();

    void setAbstractNumId(BigInteger bigInteger);

    void xsetAbstractNumId(STDecimalNumber sTDecimalNumber);
}
