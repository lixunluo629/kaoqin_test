package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTLvl.class */
public interface CTLvl extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLvl.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlvlf630type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTLvl$Factory.class */
    public static final class Factory {
        public static CTLvl newInstance() {
            return (CTLvl) POIXMLTypeLoader.newInstance(CTLvl.type, null);
        }

        public static CTLvl newInstance(XmlOptions xmlOptions) {
            return (CTLvl) POIXMLTypeLoader.newInstance(CTLvl.type, xmlOptions);
        }

        public static CTLvl parse(String str) throws XmlException {
            return (CTLvl) POIXMLTypeLoader.parse(str, CTLvl.type, (XmlOptions) null);
        }

        public static CTLvl parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLvl) POIXMLTypeLoader.parse(str, CTLvl.type, xmlOptions);
        }

        public static CTLvl parse(File file) throws XmlException, IOException {
            return (CTLvl) POIXMLTypeLoader.parse(file, CTLvl.type, (XmlOptions) null);
        }

        public static CTLvl parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLvl) POIXMLTypeLoader.parse(file, CTLvl.type, xmlOptions);
        }

        public static CTLvl parse(URL url) throws XmlException, IOException {
            return (CTLvl) POIXMLTypeLoader.parse(url, CTLvl.type, (XmlOptions) null);
        }

        public static CTLvl parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLvl) POIXMLTypeLoader.parse(url, CTLvl.type, xmlOptions);
        }

        public static CTLvl parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLvl) POIXMLTypeLoader.parse(inputStream, CTLvl.type, (XmlOptions) null);
        }

        public static CTLvl parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLvl) POIXMLTypeLoader.parse(inputStream, CTLvl.type, xmlOptions);
        }

        public static CTLvl parse(Reader reader) throws XmlException, IOException {
            return (CTLvl) POIXMLTypeLoader.parse(reader, CTLvl.type, (XmlOptions) null);
        }

        public static CTLvl parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLvl) POIXMLTypeLoader.parse(reader, CTLvl.type, xmlOptions);
        }

        public static CTLvl parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLvl) POIXMLTypeLoader.parse(xMLStreamReader, CTLvl.type, (XmlOptions) null);
        }

        public static CTLvl parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLvl) POIXMLTypeLoader.parse(xMLStreamReader, CTLvl.type, xmlOptions);
        }

        public static CTLvl parse(Node node) throws XmlException {
            return (CTLvl) POIXMLTypeLoader.parse(node, CTLvl.type, (XmlOptions) null);
        }

        public static CTLvl parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLvl) POIXMLTypeLoader.parse(node, CTLvl.type, xmlOptions);
        }

        public static CTLvl parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLvl) POIXMLTypeLoader.parse(xMLInputStream, CTLvl.type, (XmlOptions) null);
        }

        public static CTLvl parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLvl) POIXMLTypeLoader.parse(xMLInputStream, CTLvl.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLvl.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLvl.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTDecimalNumber getStart();

    boolean isSetStart();

    void setStart(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewStart();

    void unsetStart();

    CTNumFmt getNumFmt();

    boolean isSetNumFmt();

    void setNumFmt(CTNumFmt cTNumFmt);

    CTNumFmt addNewNumFmt();

    void unsetNumFmt();

    CTDecimalNumber getLvlRestart();

    boolean isSetLvlRestart();

    void setLvlRestart(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewLvlRestart();

    void unsetLvlRestart();

    CTString getPStyle();

    boolean isSetPStyle();

    void setPStyle(CTString cTString);

    CTString addNewPStyle();

    void unsetPStyle();

    CTOnOff getIsLgl();

    boolean isSetIsLgl();

    void setIsLgl(CTOnOff cTOnOff);

    CTOnOff addNewIsLgl();

    void unsetIsLgl();

    CTLevelSuffix getSuff();

    boolean isSetSuff();

    void setSuff(CTLevelSuffix cTLevelSuffix);

    CTLevelSuffix addNewSuff();

    void unsetSuff();

    CTLevelText getLvlText();

    boolean isSetLvlText();

    void setLvlText(CTLevelText cTLevelText);

    CTLevelText addNewLvlText();

    void unsetLvlText();

    CTDecimalNumber getLvlPicBulletId();

    boolean isSetLvlPicBulletId();

    void setLvlPicBulletId(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewLvlPicBulletId();

    void unsetLvlPicBulletId();

    CTLvlLegacy getLegacy();

    boolean isSetLegacy();

    void setLegacy(CTLvlLegacy cTLvlLegacy);

    CTLvlLegacy addNewLegacy();

    void unsetLegacy();

    CTJc getLvlJc();

    boolean isSetLvlJc();

    void setLvlJc(CTJc cTJc);

    CTJc addNewLvlJc();

    void unsetLvlJc();

    CTPPr getPPr();

    boolean isSetPPr();

    void setPPr(CTPPr cTPPr);

    CTPPr addNewPPr();

    void unsetPPr();

    CTRPr getRPr();

    boolean isSetRPr();

    void setRPr(CTRPr cTRPr);

    CTRPr addNewRPr();

    void unsetRPr();

    BigInteger getIlvl();

    STDecimalNumber xgetIlvl();

    void setIlvl(BigInteger bigInteger);

    void xsetIlvl(STDecimalNumber sTDecimalNumber);

    byte[] getTplc();

    STLongHexNumber xgetTplc();

    boolean isSetTplc();

    void setTplc(byte[] bArr);

    void xsetTplc(STLongHexNumber sTLongHexNumber);

    void unsetTplc();

    STOnOff.Enum getTentative();

    STOnOff xgetTentative();

    boolean isSetTentative();

    void setTentative(STOnOff.Enum r1);

    void xsetTentative(STOnOff sTOnOff);

    void unsetTentative();
}
