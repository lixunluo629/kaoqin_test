package org.openxmlformats.schemas.presentationml.x2006.main;

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
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer;
import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTBackgroundProperties.class */
public interface CTBackgroundProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBackgroundProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctbackgroundpropertiesb184type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTBackgroundProperties$Factory.class */
    public static final class Factory {
        public static CTBackgroundProperties newInstance() {
            return (CTBackgroundProperties) POIXMLTypeLoader.newInstance(CTBackgroundProperties.type, null);
        }

        public static CTBackgroundProperties newInstance(XmlOptions xmlOptions) {
            return (CTBackgroundProperties) POIXMLTypeLoader.newInstance(CTBackgroundProperties.type, xmlOptions);
        }

        public static CTBackgroundProperties parse(String str) throws XmlException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(str, CTBackgroundProperties.type, (XmlOptions) null);
        }

        public static CTBackgroundProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(str, CTBackgroundProperties.type, xmlOptions);
        }

        public static CTBackgroundProperties parse(File file) throws XmlException, IOException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(file, CTBackgroundProperties.type, (XmlOptions) null);
        }

        public static CTBackgroundProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(file, CTBackgroundProperties.type, xmlOptions);
        }

        public static CTBackgroundProperties parse(URL url) throws XmlException, IOException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(url, CTBackgroundProperties.type, (XmlOptions) null);
        }

        public static CTBackgroundProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(url, CTBackgroundProperties.type, xmlOptions);
        }

        public static CTBackgroundProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(inputStream, CTBackgroundProperties.type, (XmlOptions) null);
        }

        public static CTBackgroundProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(inputStream, CTBackgroundProperties.type, xmlOptions);
        }

        public static CTBackgroundProperties parse(Reader reader) throws XmlException, IOException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(reader, CTBackgroundProperties.type, (XmlOptions) null);
        }

        public static CTBackgroundProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(reader, CTBackgroundProperties.type, xmlOptions);
        }

        public static CTBackgroundProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTBackgroundProperties.type, (XmlOptions) null);
        }

        public static CTBackgroundProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTBackgroundProperties.type, xmlOptions);
        }

        public static CTBackgroundProperties parse(Node node) throws XmlException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(node, CTBackgroundProperties.type, (XmlOptions) null);
        }

        public static CTBackgroundProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(node, CTBackgroundProperties.type, xmlOptions);
        }

        public static CTBackgroundProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(xMLInputStream, CTBackgroundProperties.type, (XmlOptions) null);
        }

        public static CTBackgroundProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBackgroundProperties) POIXMLTypeLoader.parse(xMLInputStream, CTBackgroundProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBackgroundProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBackgroundProperties.type, xmlOptions);
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

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    boolean getShadeToTitle();

    XmlBoolean xgetShadeToTitle();

    boolean isSetShadeToTitle();

    void setShadeToTitle(boolean z);

    void xsetShadeToTitle(XmlBoolean xmlBoolean);

    void unsetShadeToTitle();
}
