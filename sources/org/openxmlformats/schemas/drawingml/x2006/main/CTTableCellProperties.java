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
import org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTableCellProperties.class */
public interface CTTableCellProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTableCellProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttablecellproperties1614type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTableCellProperties$Factory.class */
    public static final class Factory {
        public static CTTableCellProperties newInstance() {
            return (CTTableCellProperties) POIXMLTypeLoader.newInstance(CTTableCellProperties.type, null);
        }

        public static CTTableCellProperties newInstance(XmlOptions xmlOptions) {
            return (CTTableCellProperties) POIXMLTypeLoader.newInstance(CTTableCellProperties.type, xmlOptions);
        }

        public static CTTableCellProperties parse(String str) throws XmlException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(str, CTTableCellProperties.type, (XmlOptions) null);
        }

        public static CTTableCellProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(str, CTTableCellProperties.type, xmlOptions);
        }

        public static CTTableCellProperties parse(File file) throws XmlException, IOException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(file, CTTableCellProperties.type, (XmlOptions) null);
        }

        public static CTTableCellProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(file, CTTableCellProperties.type, xmlOptions);
        }

        public static CTTableCellProperties parse(URL url) throws XmlException, IOException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(url, CTTableCellProperties.type, (XmlOptions) null);
        }

        public static CTTableCellProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(url, CTTableCellProperties.type, xmlOptions);
        }

        public static CTTableCellProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(inputStream, CTTableCellProperties.type, (XmlOptions) null);
        }

        public static CTTableCellProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(inputStream, CTTableCellProperties.type, xmlOptions);
        }

        public static CTTableCellProperties parse(Reader reader) throws XmlException, IOException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(reader, CTTableCellProperties.type, (XmlOptions) null);
        }

        public static CTTableCellProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(reader, CTTableCellProperties.type, xmlOptions);
        }

        public static CTTableCellProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTTableCellProperties.type, (XmlOptions) null);
        }

        public static CTTableCellProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTTableCellProperties.type, xmlOptions);
        }

        public static CTTableCellProperties parse(Node node) throws XmlException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(node, CTTableCellProperties.type, (XmlOptions) null);
        }

        public static CTTableCellProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(node, CTTableCellProperties.type, xmlOptions);
        }

        public static CTTableCellProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(xMLInputStream, CTTableCellProperties.type, (XmlOptions) null);
        }

        public static CTTableCellProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTableCellProperties) POIXMLTypeLoader.parse(xMLInputStream, CTTableCellProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableCellProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableCellProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTLineProperties getLnL();

    boolean isSetLnL();

    void setLnL(CTLineProperties cTLineProperties);

    CTLineProperties addNewLnL();

    void unsetLnL();

    CTLineProperties getLnR();

    boolean isSetLnR();

    void setLnR(CTLineProperties cTLineProperties);

    CTLineProperties addNewLnR();

    void unsetLnR();

    CTLineProperties getLnT();

    boolean isSetLnT();

    void setLnT(CTLineProperties cTLineProperties);

    CTLineProperties addNewLnT();

    void unsetLnT();

    CTLineProperties getLnB();

    boolean isSetLnB();

    void setLnB(CTLineProperties cTLineProperties);

    CTLineProperties addNewLnB();

    void unsetLnB();

    CTLineProperties getLnTlToBr();

    boolean isSetLnTlToBr();

    void setLnTlToBr(CTLineProperties cTLineProperties);

    CTLineProperties addNewLnTlToBr();

    void unsetLnTlToBr();

    CTLineProperties getLnBlToTr();

    boolean isSetLnBlToTr();

    void setLnBlToTr(CTLineProperties cTLineProperties);

    CTLineProperties addNewLnBlToTr();

    void unsetLnBlToTr();

    CTCell3D getCell3D();

    boolean isSetCell3D();

    void setCell3D(CTCell3D cTCell3D);

    CTCell3D addNewCell3D();

    void unsetCell3D();

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

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    int getMarL();

    STCoordinate32 xgetMarL();

    boolean isSetMarL();

    void setMarL(int i);

    void xsetMarL(STCoordinate32 sTCoordinate32);

    void unsetMarL();

    int getMarR();

    STCoordinate32 xgetMarR();

    boolean isSetMarR();

    void setMarR(int i);

    void xsetMarR(STCoordinate32 sTCoordinate32);

    void unsetMarR();

    int getMarT();

    STCoordinate32 xgetMarT();

    boolean isSetMarT();

    void setMarT(int i);

    void xsetMarT(STCoordinate32 sTCoordinate32);

    void unsetMarT();

    int getMarB();

    STCoordinate32 xgetMarB();

    boolean isSetMarB();

    void setMarB(int i);

    void xsetMarB(STCoordinate32 sTCoordinate32);

    void unsetMarB();

    STTextVerticalType.Enum getVert();

    STTextVerticalType xgetVert();

    boolean isSetVert();

    void setVert(STTextVerticalType.Enum r1);

    void xsetVert(STTextVerticalType sTTextVerticalType);

    void unsetVert();

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

    STTextHorzOverflowType.Enum getHorzOverflow();

    STTextHorzOverflowType xgetHorzOverflow();

    boolean isSetHorzOverflow();

    void setHorzOverflow(STTextHorzOverflowType.Enum r1);

    void xsetHorzOverflow(STTextHorzOverflowType sTTextHorzOverflowType);

    void unsetHorzOverflow();
}
