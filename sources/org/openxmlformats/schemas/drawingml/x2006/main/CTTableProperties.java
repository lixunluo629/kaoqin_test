package org.openxmlformats.schemas.drawingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTableProperties.class */
public interface CTTableProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTableProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttableproperties3512type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTableProperties$Factory.class */
    public static final class Factory {
        public static CTTableProperties newInstance() {
            return (CTTableProperties) POIXMLTypeLoader.newInstance(CTTableProperties.type, null);
        }

        public static CTTableProperties newInstance(XmlOptions xmlOptions) {
            return (CTTableProperties) POIXMLTypeLoader.newInstance(CTTableProperties.type, xmlOptions);
        }

        public static CTTableProperties parse(String str) throws XmlException {
            return (CTTableProperties) POIXMLTypeLoader.parse(str, CTTableProperties.type, (XmlOptions) null);
        }

        public static CTTableProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTableProperties) POIXMLTypeLoader.parse(str, CTTableProperties.type, xmlOptions);
        }

        public static CTTableProperties parse(File file) throws XmlException, IOException {
            return (CTTableProperties) POIXMLTypeLoader.parse(file, CTTableProperties.type, (XmlOptions) null);
        }

        public static CTTableProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableProperties) POIXMLTypeLoader.parse(file, CTTableProperties.type, xmlOptions);
        }

        public static CTTableProperties parse(URL url) throws XmlException, IOException {
            return (CTTableProperties) POIXMLTypeLoader.parse(url, CTTableProperties.type, (XmlOptions) null);
        }

        public static CTTableProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableProperties) POIXMLTypeLoader.parse(url, CTTableProperties.type, xmlOptions);
        }

        public static CTTableProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTableProperties) POIXMLTypeLoader.parse(inputStream, CTTableProperties.type, (XmlOptions) null);
        }

        public static CTTableProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableProperties) POIXMLTypeLoader.parse(inputStream, CTTableProperties.type, xmlOptions);
        }

        public static CTTableProperties parse(Reader reader) throws XmlException, IOException {
            return (CTTableProperties) POIXMLTypeLoader.parse(reader, CTTableProperties.type, (XmlOptions) null);
        }

        public static CTTableProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableProperties) POIXMLTypeLoader.parse(reader, CTTableProperties.type, xmlOptions);
        }

        public static CTTableProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTableProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTTableProperties.type, (XmlOptions) null);
        }

        public static CTTableProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTableProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTTableProperties.type, xmlOptions);
        }

        public static CTTableProperties parse(Node node) throws XmlException {
            return (CTTableProperties) POIXMLTypeLoader.parse(node, CTTableProperties.type, (XmlOptions) null);
        }

        public static CTTableProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTableProperties) POIXMLTypeLoader.parse(node, CTTableProperties.type, xmlOptions);
        }

        public static CTTableProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTableProperties) POIXMLTypeLoader.parse(xMLInputStream, CTTableProperties.type, (XmlOptions) null);
        }

        public static CTTableProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTableProperties) POIXMLTypeLoader.parse(xMLInputStream, CTTableProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTNoFillProperties getNoFill();

    boolean isSetNoFill();

    void setNoFill(CTNoFillProperties cTNoFillProperties);

    CTNoFillProperties addNewNoFill();

    void unsetNoFill();

    CTSolidColorFillProperties getSolidFill();

    boolean isSetSolidFill();

    void setSolidFill(CTSolidColorFillProperties cTSolidColorFillProperties);

    CTSolidColorFillProperties addNewSolidFill();

    void unsetSolidFill();

    CTGradientFillProperties getGradFill();

    boolean isSetGradFill();

    void setGradFill(CTGradientFillProperties cTGradientFillProperties);

    CTGradientFillProperties addNewGradFill();

    void unsetGradFill();

    CTBlipFillProperties getBlipFill();

    boolean isSetBlipFill();

    void setBlipFill(CTBlipFillProperties cTBlipFillProperties);

    CTBlipFillProperties addNewBlipFill();

    void unsetBlipFill();

    CTPatternFillProperties getPattFill();

    boolean isSetPattFill();

    void setPattFill(CTPatternFillProperties cTPatternFillProperties);

    CTPatternFillProperties addNewPattFill();

    void unsetPattFill();

    CTGroupFillProperties getGrpFill();

    boolean isSetGrpFill();

    void setGrpFill(CTGroupFillProperties cTGroupFillProperties);

    CTGroupFillProperties addNewGrpFill();

    void unsetGrpFill();

    CTEffectList getEffectLst();

    boolean isSetEffectLst();

    void setEffectLst(CTEffectList cTEffectList);

    CTEffectList addNewEffectLst();

    void unsetEffectLst();

    CTEffectContainer getEffectDag();

    boolean isSetEffectDag();

    void setEffectDag(CTEffectContainer cTEffectContainer);

    CTEffectContainer addNewEffectDag();

    void unsetEffectDag();

    CTTableStyle getTableStyle();

    boolean isSetTableStyle();

    void setTableStyle(CTTableStyle cTTableStyle);

    CTTableStyle addNewTableStyle();

    void unsetTableStyle();

    String getTableStyleId();

    STGuid xgetTableStyleId();

    boolean isSetTableStyleId();

    void setTableStyleId(String str);

    void xsetTableStyleId(STGuid sTGuid);

    void unsetTableStyleId();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    boolean getRtl();

    XmlBoolean xgetRtl();

    boolean isSetRtl();

    void setRtl(boolean z);

    void xsetRtl(XmlBoolean xmlBoolean);

    void unsetRtl();

    boolean getFirstRow();

    XmlBoolean xgetFirstRow();

    boolean isSetFirstRow();

    void setFirstRow(boolean z);

    void xsetFirstRow(XmlBoolean xmlBoolean);

    void unsetFirstRow();

    boolean getFirstCol();

    XmlBoolean xgetFirstCol();

    boolean isSetFirstCol();

    void setFirstCol(boolean z);

    void xsetFirstCol(XmlBoolean xmlBoolean);

    void unsetFirstCol();

    boolean getLastRow();

    XmlBoolean xgetLastRow();

    boolean isSetLastRow();

    void setLastRow(boolean z);

    void xsetLastRow(XmlBoolean xmlBoolean);

    void unsetLastRow();

    boolean getLastCol();

    XmlBoolean xgetLastCol();

    boolean isSetLastCol();

    void setLastCol(boolean z);

    void xsetLastCol(XmlBoolean xmlBoolean);

    void unsetLastCol();

    boolean getBandRow();

    XmlBoolean xgetBandRow();

    boolean isSetBandRow();

    void setBandRow(boolean z);

    void xsetBandRow(XmlBoolean xmlBoolean);

    void unsetBandRow();

    boolean getBandCol();

    XmlBoolean xgetBandCol();

    boolean isSetBandCol();

    void setBandCol(boolean z);

    void xsetBandCol(XmlBoolean xmlBoolean);

    void unsetBandCol();
}
