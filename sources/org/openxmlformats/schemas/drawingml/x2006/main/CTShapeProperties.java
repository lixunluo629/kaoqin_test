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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTShapeProperties.class */
public interface CTShapeProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTShapeProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctshapeproperties30e5type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTShapeProperties$Factory.class */
    public static final class Factory {
        public static CTShapeProperties newInstance() {
            return (CTShapeProperties) POIXMLTypeLoader.newInstance(CTShapeProperties.type, null);
        }

        public static CTShapeProperties newInstance(XmlOptions xmlOptions) {
            return (CTShapeProperties) POIXMLTypeLoader.newInstance(CTShapeProperties.type, xmlOptions);
        }

        public static CTShapeProperties parse(String str) throws XmlException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(str, CTShapeProperties.type, (XmlOptions) null);
        }

        public static CTShapeProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(str, CTShapeProperties.type, xmlOptions);
        }

        public static CTShapeProperties parse(File file) throws XmlException, IOException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(file, CTShapeProperties.type, (XmlOptions) null);
        }

        public static CTShapeProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(file, CTShapeProperties.type, xmlOptions);
        }

        public static CTShapeProperties parse(URL url) throws XmlException, IOException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(url, CTShapeProperties.type, (XmlOptions) null);
        }

        public static CTShapeProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(url, CTShapeProperties.type, xmlOptions);
        }

        public static CTShapeProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(inputStream, CTShapeProperties.type, (XmlOptions) null);
        }

        public static CTShapeProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(inputStream, CTShapeProperties.type, xmlOptions);
        }

        public static CTShapeProperties parse(Reader reader) throws XmlException, IOException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(reader, CTShapeProperties.type, (XmlOptions) null);
        }

        public static CTShapeProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(reader, CTShapeProperties.type, xmlOptions);
        }

        public static CTShapeProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTShapeProperties.type, (XmlOptions) null);
        }

        public static CTShapeProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTShapeProperties.type, xmlOptions);
        }

        public static CTShapeProperties parse(Node node) throws XmlException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(node, CTShapeProperties.type, (XmlOptions) null);
        }

        public static CTShapeProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(node, CTShapeProperties.type, xmlOptions);
        }

        public static CTShapeProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(xMLInputStream, CTShapeProperties.type, (XmlOptions) null);
        }

        public static CTShapeProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTShapeProperties) POIXMLTypeLoader.parse(xMLInputStream, CTShapeProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTShapeProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTShapeProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTransform2D getXfrm();

    boolean isSetXfrm();

    void setXfrm(CTTransform2D cTTransform2D);

    CTTransform2D addNewXfrm();

    void unsetXfrm();

    CTCustomGeometry2D getCustGeom();

    boolean isSetCustGeom();

    void setCustGeom(CTCustomGeometry2D cTCustomGeometry2D);

    CTCustomGeometry2D addNewCustGeom();

    void unsetCustGeom();

    CTPresetGeometry2D getPrstGeom();

    boolean isSetPrstGeom();

    void setPrstGeom(CTPresetGeometry2D cTPresetGeometry2D);

    CTPresetGeometry2D addNewPrstGeom();

    void unsetPrstGeom();

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

    CTLineProperties getLn();

    boolean isSetLn();

    void setLn(CTLineProperties cTLineProperties);

    CTLineProperties addNewLn();

    void unsetLn();

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

    CTShape3D getSp3D();

    boolean isSetSp3D();

    void setSp3D(CTShape3D cTShape3D);

    CTShape3D addNewSp3D();

    void unsetSp3D();

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
