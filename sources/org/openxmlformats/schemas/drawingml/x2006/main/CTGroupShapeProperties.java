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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGroupShapeProperties.class */
public interface CTGroupShapeProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTGroupShapeProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctgroupshapeproperties8690type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGroupShapeProperties$Factory.class */
    public static final class Factory {
        public static CTGroupShapeProperties newInstance() {
            return (CTGroupShapeProperties) POIXMLTypeLoader.newInstance(CTGroupShapeProperties.type, null);
        }

        public static CTGroupShapeProperties newInstance(XmlOptions xmlOptions) {
            return (CTGroupShapeProperties) POIXMLTypeLoader.newInstance(CTGroupShapeProperties.type, xmlOptions);
        }

        public static CTGroupShapeProperties parse(String str) throws XmlException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(str, CTGroupShapeProperties.type, (XmlOptions) null);
        }

        public static CTGroupShapeProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(str, CTGroupShapeProperties.type, xmlOptions);
        }

        public static CTGroupShapeProperties parse(File file) throws XmlException, IOException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(file, CTGroupShapeProperties.type, (XmlOptions) null);
        }

        public static CTGroupShapeProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(file, CTGroupShapeProperties.type, xmlOptions);
        }

        public static CTGroupShapeProperties parse(URL url) throws XmlException, IOException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(url, CTGroupShapeProperties.type, (XmlOptions) null);
        }

        public static CTGroupShapeProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(url, CTGroupShapeProperties.type, xmlOptions);
        }

        public static CTGroupShapeProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(inputStream, CTGroupShapeProperties.type, (XmlOptions) null);
        }

        public static CTGroupShapeProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(inputStream, CTGroupShapeProperties.type, xmlOptions);
        }

        public static CTGroupShapeProperties parse(Reader reader) throws XmlException, IOException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(reader, CTGroupShapeProperties.type, (XmlOptions) null);
        }

        public static CTGroupShapeProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(reader, CTGroupShapeProperties.type, xmlOptions);
        }

        public static CTGroupShapeProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTGroupShapeProperties.type, (XmlOptions) null);
        }

        public static CTGroupShapeProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTGroupShapeProperties.type, xmlOptions);
        }

        public static CTGroupShapeProperties parse(Node node) throws XmlException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(node, CTGroupShapeProperties.type, (XmlOptions) null);
        }

        public static CTGroupShapeProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(node, CTGroupShapeProperties.type, xmlOptions);
        }

        public static CTGroupShapeProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(xMLInputStream, CTGroupShapeProperties.type, (XmlOptions) null);
        }

        public static CTGroupShapeProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTGroupShapeProperties) POIXMLTypeLoader.parse(xMLInputStream, CTGroupShapeProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGroupShapeProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGroupShapeProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTGroupTransform2D getXfrm();

    boolean isSetXfrm();

    void setXfrm(CTGroupTransform2D cTGroupTransform2D);

    CTGroupTransform2D addNewXfrm();

    void unsetXfrm();

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

    CTScene3D getScene3D();

    boolean isSetScene3D();

    void setScene3D(CTScene3D cTScene3D);

    CTScene3D addNewScene3D();

    void unsetScene3D();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    STBlackWhiteMode$Enum getBwMode();

    STBlackWhiteMode xgetBwMode();

    boolean isSetBwMode();

    void setBwMode(STBlackWhiteMode$Enum sTBlackWhiteMode$Enum);

    void xsetBwMode(STBlackWhiteMode sTBlackWhiteMode);

    void unsetBwMode();
}
