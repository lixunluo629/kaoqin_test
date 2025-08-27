package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTStyle.class */
public interface CTStyle extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTStyle.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctstyle41c1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTStyle$Factory.class */
    public static final class Factory {
        public static CTStyle newInstance() {
            return (CTStyle) POIXMLTypeLoader.newInstance(CTStyle.type, null);
        }

        public static CTStyle newInstance(XmlOptions xmlOptions) {
            return (CTStyle) POIXMLTypeLoader.newInstance(CTStyle.type, xmlOptions);
        }

        public static CTStyle parse(String str) throws XmlException {
            return (CTStyle) POIXMLTypeLoader.parse(str, CTStyle.type, (XmlOptions) null);
        }

        public static CTStyle parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTStyle) POIXMLTypeLoader.parse(str, CTStyle.type, xmlOptions);
        }

        public static CTStyle parse(File file) throws XmlException, IOException {
            return (CTStyle) POIXMLTypeLoader.parse(file, CTStyle.type, (XmlOptions) null);
        }

        public static CTStyle parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyle) POIXMLTypeLoader.parse(file, CTStyle.type, xmlOptions);
        }

        public static CTStyle parse(URL url) throws XmlException, IOException {
            return (CTStyle) POIXMLTypeLoader.parse(url, CTStyle.type, (XmlOptions) null);
        }

        public static CTStyle parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyle) POIXMLTypeLoader.parse(url, CTStyle.type, xmlOptions);
        }

        public static CTStyle parse(InputStream inputStream) throws XmlException, IOException {
            return (CTStyle) POIXMLTypeLoader.parse(inputStream, CTStyle.type, (XmlOptions) null);
        }

        public static CTStyle parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyle) POIXMLTypeLoader.parse(inputStream, CTStyle.type, xmlOptions);
        }

        public static CTStyle parse(Reader reader) throws XmlException, IOException {
            return (CTStyle) POIXMLTypeLoader.parse(reader, CTStyle.type, (XmlOptions) null);
        }

        public static CTStyle parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyle) POIXMLTypeLoader.parse(reader, CTStyle.type, xmlOptions);
        }

        public static CTStyle parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTStyle) POIXMLTypeLoader.parse(xMLStreamReader, CTStyle.type, (XmlOptions) null);
        }

        public static CTStyle parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTStyle) POIXMLTypeLoader.parse(xMLStreamReader, CTStyle.type, xmlOptions);
        }

        public static CTStyle parse(Node node) throws XmlException {
            return (CTStyle) POIXMLTypeLoader.parse(node, CTStyle.type, (XmlOptions) null);
        }

        public static CTStyle parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTStyle) POIXMLTypeLoader.parse(node, CTStyle.type, xmlOptions);
        }

        public static CTStyle parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTStyle) POIXMLTypeLoader.parse(xMLInputStream, CTStyle.type, (XmlOptions) null);
        }

        public static CTStyle parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTStyle) POIXMLTypeLoader.parse(xMLInputStream, CTStyle.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStyle.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStyle.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTString getName();

    boolean isSetName();

    void setName(CTString cTString);

    CTString addNewName();

    void unsetName();

    CTString getAliases();

    boolean isSetAliases();

    void setAliases(CTString cTString);

    CTString addNewAliases();

    void unsetAliases();

    CTString getBasedOn();

    boolean isSetBasedOn();

    void setBasedOn(CTString cTString);

    CTString addNewBasedOn();

    void unsetBasedOn();

    CTString getNext();

    boolean isSetNext();

    void setNext(CTString cTString);

    CTString addNewNext();

    void unsetNext();

    CTString getLink();

    boolean isSetLink();

    void setLink(CTString cTString);

    CTString addNewLink();

    void unsetLink();

    CTOnOff getAutoRedefine();

    boolean isSetAutoRedefine();

    void setAutoRedefine(CTOnOff cTOnOff);

    CTOnOff addNewAutoRedefine();

    void unsetAutoRedefine();

    CTOnOff getHidden();

    boolean isSetHidden();

    void setHidden(CTOnOff cTOnOff);

    CTOnOff addNewHidden();

    void unsetHidden();

    CTDecimalNumber getUiPriority();

    boolean isSetUiPriority();

    void setUiPriority(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewUiPriority();

    void unsetUiPriority();

    CTOnOff getSemiHidden();

    boolean isSetSemiHidden();

    void setSemiHidden(CTOnOff cTOnOff);

    CTOnOff addNewSemiHidden();

    void unsetSemiHidden();

    CTOnOff getUnhideWhenUsed();

    boolean isSetUnhideWhenUsed();

    void setUnhideWhenUsed(CTOnOff cTOnOff);

    CTOnOff addNewUnhideWhenUsed();

    void unsetUnhideWhenUsed();

    CTOnOff getQFormat();

    boolean isSetQFormat();

    void setQFormat(CTOnOff cTOnOff);

    CTOnOff addNewQFormat();

    void unsetQFormat();

    CTOnOff getLocked();

    boolean isSetLocked();

    void setLocked(CTOnOff cTOnOff);

    CTOnOff addNewLocked();

    void unsetLocked();

    CTOnOff getPersonal();

    boolean isSetPersonal();

    void setPersonal(CTOnOff cTOnOff);

    CTOnOff addNewPersonal();

    void unsetPersonal();

    CTOnOff getPersonalCompose();

    boolean isSetPersonalCompose();

    void setPersonalCompose(CTOnOff cTOnOff);

    CTOnOff addNewPersonalCompose();

    void unsetPersonalCompose();

    CTOnOff getPersonalReply();

    boolean isSetPersonalReply();

    void setPersonalReply(CTOnOff cTOnOff);

    CTOnOff addNewPersonalReply();

    void unsetPersonalReply();

    CTLongHexNumber getRsid();

    boolean isSetRsid();

    void setRsid(CTLongHexNumber cTLongHexNumber);

    CTLongHexNumber addNewRsid();

    void unsetRsid();

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

    CTTblPrBase getTblPr();

    boolean isSetTblPr();

    void setTblPr(CTTblPrBase cTTblPrBase);

    CTTblPrBase addNewTblPr();

    void unsetTblPr();

    CTTrPr getTrPr();

    boolean isSetTrPr();

    void setTrPr(CTTrPr cTTrPr);

    CTTrPr addNewTrPr();

    void unsetTrPr();

    CTTcPr getTcPr();

    boolean isSetTcPr();

    void setTcPr(CTTcPr cTTcPr);

    CTTcPr addNewTcPr();

    void unsetTcPr();

    List<CTTblStylePr> getTblStylePrList();

    CTTblStylePr[] getTblStylePrArray();

    CTTblStylePr getTblStylePrArray(int i);

    int sizeOfTblStylePrArray();

    void setTblStylePrArray(CTTblStylePr[] cTTblStylePrArr);

    void setTblStylePrArray(int i, CTTblStylePr cTTblStylePr);

    CTTblStylePr insertNewTblStylePr(int i);

    CTTblStylePr addNewTblStylePr();

    void removeTblStylePr(int i);

    STStyleType.Enum getType();

    STStyleType xgetType();

    boolean isSetType();

    void setType(STStyleType.Enum r1);

    void xsetType(STStyleType sTStyleType);

    void unsetType();

    String getStyleId();

    STString xgetStyleId();

    boolean isSetStyleId();

    void setStyleId(String str);

    void xsetStyleId(STString sTString);

    void unsetStyleId();

    STOnOff.Enum getDefault();

    STOnOff xgetDefault();

    boolean isSetDefault();

    void setDefault(STOnOff.Enum r1);

    void xsetDefault(STOnOff sTOnOff);

    void unsetDefault();

    STOnOff.Enum getCustomStyle();

    STOnOff xgetCustomStyle();

    boolean isSetCustomStyle();

    void setCustomStyle(STOnOff.Enum r1);

    void xsetCustomStyle(STOnOff sTOnOff);

    void unsetCustomStyle();
}
