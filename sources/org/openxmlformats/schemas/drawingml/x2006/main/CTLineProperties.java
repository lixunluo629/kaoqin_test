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
import org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine;
import org.openxmlformats.schemas.drawingml.x2006.main.STLineCap;
import org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTLineProperties.class */
public interface CTLineProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLineProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlinepropertiesd5e2type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTLineProperties$Factory.class */
    public static final class Factory {
        public static CTLineProperties newInstance() {
            return (CTLineProperties) POIXMLTypeLoader.newInstance(CTLineProperties.type, null);
        }

        public static CTLineProperties newInstance(XmlOptions xmlOptions) {
            return (CTLineProperties) POIXMLTypeLoader.newInstance(CTLineProperties.type, xmlOptions);
        }

        public static CTLineProperties parse(String str) throws XmlException {
            return (CTLineProperties) POIXMLTypeLoader.parse(str, CTLineProperties.type, (XmlOptions) null);
        }

        public static CTLineProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLineProperties) POIXMLTypeLoader.parse(str, CTLineProperties.type, xmlOptions);
        }

        public static CTLineProperties parse(File file) throws XmlException, IOException {
            return (CTLineProperties) POIXMLTypeLoader.parse(file, CTLineProperties.type, (XmlOptions) null);
        }

        public static CTLineProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineProperties) POIXMLTypeLoader.parse(file, CTLineProperties.type, xmlOptions);
        }

        public static CTLineProperties parse(URL url) throws XmlException, IOException {
            return (CTLineProperties) POIXMLTypeLoader.parse(url, CTLineProperties.type, (XmlOptions) null);
        }

        public static CTLineProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineProperties) POIXMLTypeLoader.parse(url, CTLineProperties.type, xmlOptions);
        }

        public static CTLineProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLineProperties) POIXMLTypeLoader.parse(inputStream, CTLineProperties.type, (XmlOptions) null);
        }

        public static CTLineProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineProperties) POIXMLTypeLoader.parse(inputStream, CTLineProperties.type, xmlOptions);
        }

        public static CTLineProperties parse(Reader reader) throws XmlException, IOException {
            return (CTLineProperties) POIXMLTypeLoader.parse(reader, CTLineProperties.type, (XmlOptions) null);
        }

        public static CTLineProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineProperties) POIXMLTypeLoader.parse(reader, CTLineProperties.type, xmlOptions);
        }

        public static CTLineProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLineProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTLineProperties.type, (XmlOptions) null);
        }

        public static CTLineProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLineProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTLineProperties.type, xmlOptions);
        }

        public static CTLineProperties parse(Node node) throws XmlException {
            return (CTLineProperties) POIXMLTypeLoader.parse(node, CTLineProperties.type, (XmlOptions) null);
        }

        public static CTLineProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLineProperties) POIXMLTypeLoader.parse(node, CTLineProperties.type, xmlOptions);
        }

        public static CTLineProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLineProperties) POIXMLTypeLoader.parse(xMLInputStream, CTLineProperties.type, (XmlOptions) null);
        }

        public static CTLineProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLineProperties) POIXMLTypeLoader.parse(xMLInputStream, CTLineProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLineProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLineProperties.type, xmlOptions);
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

    CTPatternFillProperties getPattFill();

    boolean isSetPattFill();

    void setPattFill(CTPatternFillProperties cTPatternFillProperties);

    CTPatternFillProperties addNewPattFill();

    void unsetPattFill();

    CTPresetLineDashProperties getPrstDash();

    boolean isSetPrstDash();

    void setPrstDash(CTPresetLineDashProperties cTPresetLineDashProperties);

    CTPresetLineDashProperties addNewPrstDash();

    void unsetPrstDash();

    CTDashStopList getCustDash();

    boolean isSetCustDash();

    void setCustDash(CTDashStopList cTDashStopList);

    CTDashStopList addNewCustDash();

    void unsetCustDash();

    CTLineJoinRound getRound();

    boolean isSetRound();

    void setRound(CTLineJoinRound cTLineJoinRound);

    CTLineJoinRound addNewRound();

    void unsetRound();

    CTLineJoinBevel getBevel();

    boolean isSetBevel();

    void setBevel(CTLineJoinBevel cTLineJoinBevel);

    CTLineJoinBevel addNewBevel();

    void unsetBevel();

    CTLineJoinMiterProperties getMiter();

    boolean isSetMiter();

    void setMiter(CTLineJoinMiterProperties cTLineJoinMiterProperties);

    CTLineJoinMiterProperties addNewMiter();

    void unsetMiter();

    CTLineEndProperties getHeadEnd();

    boolean isSetHeadEnd();

    void setHeadEnd(CTLineEndProperties cTLineEndProperties);

    CTLineEndProperties addNewHeadEnd();

    void unsetHeadEnd();

    CTLineEndProperties getTailEnd();

    boolean isSetTailEnd();

    void setTailEnd(CTLineEndProperties cTLineEndProperties);

    CTLineEndProperties addNewTailEnd();

    void unsetTailEnd();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    int getW();

    STLineWidth xgetW();

    boolean isSetW();

    void setW(int i);

    void xsetW(STLineWidth sTLineWidth);

    void unsetW();

    STLineCap.Enum getCap();

    STLineCap xgetCap();

    boolean isSetCap();

    void setCap(STLineCap.Enum r1);

    void xsetCap(STLineCap sTLineCap);

    void unsetCap();

    STCompoundLine.Enum getCmpd();

    STCompoundLine xgetCmpd();

    boolean isSetCmpd();

    void setCmpd(STCompoundLine.Enum r1);

    void xsetCmpd(STCompoundLine sTCompoundLine);

    void unsetCmpd();

    STPenAlignment.Enum getAlgn();

    STPenAlignment xgetAlgn();

    boolean isSetAlgn();

    void setAlgn(STPenAlignment.Enum r1);

    void xsetAlgn(STPenAlignment sTPenAlignment);

    void unsetAlgn();
}
