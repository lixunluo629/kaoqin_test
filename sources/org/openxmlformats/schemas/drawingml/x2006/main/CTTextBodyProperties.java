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
import org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextBodyProperties.class */
public interface CTTextBodyProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextBodyProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextbodyproperties87ddtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextBodyProperties$Factory.class */
    public static final class Factory {
        public static CTTextBodyProperties newInstance() {
            return (CTTextBodyProperties) POIXMLTypeLoader.newInstance(CTTextBodyProperties.type, null);
        }

        public static CTTextBodyProperties newInstance(XmlOptions xmlOptions) {
            return (CTTextBodyProperties) POIXMLTypeLoader.newInstance(CTTextBodyProperties.type, xmlOptions);
        }

        public static CTTextBodyProperties parse(String str) throws XmlException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(str, CTTextBodyProperties.type, (XmlOptions) null);
        }

        public static CTTextBodyProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(str, CTTextBodyProperties.type, xmlOptions);
        }

        public static CTTextBodyProperties parse(File file) throws XmlException, IOException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(file, CTTextBodyProperties.type, (XmlOptions) null);
        }

        public static CTTextBodyProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(file, CTTextBodyProperties.type, xmlOptions);
        }

        public static CTTextBodyProperties parse(URL url) throws XmlException, IOException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(url, CTTextBodyProperties.type, (XmlOptions) null);
        }

        public static CTTextBodyProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(url, CTTextBodyProperties.type, xmlOptions);
        }

        public static CTTextBodyProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(inputStream, CTTextBodyProperties.type, (XmlOptions) null);
        }

        public static CTTextBodyProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(inputStream, CTTextBodyProperties.type, xmlOptions);
        }

        public static CTTextBodyProperties parse(Reader reader) throws XmlException, IOException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(reader, CTTextBodyProperties.type, (XmlOptions) null);
        }

        public static CTTextBodyProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(reader, CTTextBodyProperties.type, xmlOptions);
        }

        public static CTTextBodyProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTTextBodyProperties.type, (XmlOptions) null);
        }

        public static CTTextBodyProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTTextBodyProperties.type, xmlOptions);
        }

        public static CTTextBodyProperties parse(Node node) throws XmlException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(node, CTTextBodyProperties.type, (XmlOptions) null);
        }

        public static CTTextBodyProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(node, CTTextBodyProperties.type, xmlOptions);
        }

        public static CTTextBodyProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(xMLInputStream, CTTextBodyProperties.type, (XmlOptions) null);
        }

        public static CTTextBodyProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextBodyProperties) POIXMLTypeLoader.parse(xMLInputStream, CTTextBodyProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextBodyProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextBodyProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPresetTextShape getPrstTxWarp();

    boolean isSetPrstTxWarp();

    void setPrstTxWarp(CTPresetTextShape cTPresetTextShape);

    CTPresetTextShape addNewPrstTxWarp();

    void unsetPrstTxWarp();

    CTTextNoAutofit getNoAutofit();

    boolean isSetNoAutofit();

    void setNoAutofit(CTTextNoAutofit cTTextNoAutofit);

    CTTextNoAutofit addNewNoAutofit();

    void unsetNoAutofit();

    CTTextNormalAutofit getNormAutofit();

    boolean isSetNormAutofit();

    void setNormAutofit(CTTextNormalAutofit cTTextNormalAutofit);

    CTTextNormalAutofit addNewNormAutofit();

    void unsetNormAutofit();

    CTTextShapeAutofit getSpAutoFit();

    boolean isSetSpAutoFit();

    void setSpAutoFit(CTTextShapeAutofit cTTextShapeAutofit);

    CTTextShapeAutofit addNewSpAutoFit();

    void unsetSpAutoFit();

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

    CTFlatText getFlatTx();

    boolean isSetFlatTx();

    void setFlatTx(CTFlatText cTFlatText);

    CTFlatText addNewFlatTx();

    void unsetFlatTx();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    int getRot();

    STAngle xgetRot();

    boolean isSetRot();

    void setRot(int i);

    void xsetRot(STAngle sTAngle);

    void unsetRot();

    boolean getSpcFirstLastPara();

    XmlBoolean xgetSpcFirstLastPara();

    boolean isSetSpcFirstLastPara();

    void setSpcFirstLastPara(boolean z);

    void xsetSpcFirstLastPara(XmlBoolean xmlBoolean);

    void unsetSpcFirstLastPara();

    STTextVertOverflowType.Enum getVertOverflow();

    STTextVertOverflowType xgetVertOverflow();

    boolean isSetVertOverflow();

    void setVertOverflow(STTextVertOverflowType.Enum r1);

    void xsetVertOverflow(STTextVertOverflowType sTTextVertOverflowType);

    void unsetVertOverflow();

    STTextHorzOverflowType.Enum getHorzOverflow();

    STTextHorzOverflowType xgetHorzOverflow();

    boolean isSetHorzOverflow();

    void setHorzOverflow(STTextHorzOverflowType.Enum r1);

    void xsetHorzOverflow(STTextHorzOverflowType sTTextHorzOverflowType);

    void unsetHorzOverflow();

    STTextVerticalType.Enum getVert();

    STTextVerticalType xgetVert();

    boolean isSetVert();

    void setVert(STTextVerticalType.Enum r1);

    void xsetVert(STTextVerticalType sTTextVerticalType);

    void unsetVert();

    STTextWrappingType.Enum getWrap();

    STTextWrappingType xgetWrap();

    boolean isSetWrap();

    void setWrap(STTextWrappingType.Enum r1);

    void xsetWrap(STTextWrappingType sTTextWrappingType);

    void unsetWrap();

    int getLIns();

    STCoordinate32 xgetLIns();

    boolean isSetLIns();

    void setLIns(int i);

    void xsetLIns(STCoordinate32 sTCoordinate32);

    void unsetLIns();

    int getTIns();

    STCoordinate32 xgetTIns();

    boolean isSetTIns();

    void setTIns(int i);

    void xsetTIns(STCoordinate32 sTCoordinate32);

    void unsetTIns();

    int getRIns();

    STCoordinate32 xgetRIns();

    boolean isSetRIns();

    void setRIns(int i);

    void xsetRIns(STCoordinate32 sTCoordinate32);

    void unsetRIns();

    int getBIns();

    STCoordinate32 xgetBIns();

    boolean isSetBIns();

    void setBIns(int i);

    void xsetBIns(STCoordinate32 sTCoordinate32);

    void unsetBIns();

    int getNumCol();

    STTextColumnCount xgetNumCol();

    boolean isSetNumCol();

    void setNumCol(int i);

    void xsetNumCol(STTextColumnCount sTTextColumnCount);

    void unsetNumCol();

    int getSpcCol();

    STPositiveCoordinate32 xgetSpcCol();

    boolean isSetSpcCol();

    void setSpcCol(int i);

    void xsetSpcCol(STPositiveCoordinate32 sTPositiveCoordinate32);

    void unsetSpcCol();

    boolean getRtlCol();

    XmlBoolean xgetRtlCol();

    boolean isSetRtlCol();

    void setRtlCol(boolean z);

    void xsetRtlCol(XmlBoolean xmlBoolean);

    void unsetRtlCol();

    boolean getFromWordArt();

    XmlBoolean xgetFromWordArt();

    boolean isSetFromWordArt();

    void setFromWordArt(boolean z);

    void xsetFromWordArt(XmlBoolean xmlBoolean);

    void unsetFromWordArt();

    STTextAnchoringType.Enum getAnchor();

    STTextAnchoringType xgetAnchor();

    boolean isSetAnchor();

    void setAnchor(STTextAnchoringType.Enum r1);

    void xsetAnchor(STTextAnchoringType sTTextAnchoringType);

    void unsetAnchor();

    boolean getAnchorCtr();

    XmlBoolean xgetAnchorCtr();

    boolean isSetAnchorCtr();

    void setAnchorCtr(boolean z);

    void xsetAnchorCtr(XmlBoolean xmlBoolean);

    void unsetAnchorCtr();

    boolean getForceAA();

    XmlBoolean xgetForceAA();

    boolean isSetForceAA();

    void setForceAA(boolean z);

    void xsetForceAA(XmlBoolean xmlBoolean);

    void unsetForceAA();

    boolean getUpright();

    XmlBoolean xgetUpright();

    boolean isSetUpright();

    void setUpright(boolean z);

    void xsetUpright(XmlBoolean xmlBoolean);

    void unsetUpright();

    boolean getCompatLnSpc();

    XmlBoolean xgetCompatLnSpc();

    boolean isSetCompatLnSpc();

    void setCompatLnSpc(boolean z);

    void xsetCompatLnSpc(XmlBoolean xmlBoolean);

    void unsetCompatLnSpc();
}
