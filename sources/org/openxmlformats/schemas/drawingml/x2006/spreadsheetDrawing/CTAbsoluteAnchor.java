package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing;

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
import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/CTAbsoluteAnchor.class */
public interface CTAbsoluteAnchor extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTAbsoluteAnchor.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctabsoluteanchore360type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/CTAbsoluteAnchor$Factory.class */
    public static final class Factory {
        public static CTAbsoluteAnchor newInstance() {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.newInstance(CTAbsoluteAnchor.type, null);
        }

        public static CTAbsoluteAnchor newInstance(XmlOptions xmlOptions) {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.newInstance(CTAbsoluteAnchor.type, xmlOptions);
        }

        public static CTAbsoluteAnchor parse(String str) throws XmlException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(str, CTAbsoluteAnchor.type, (XmlOptions) null);
        }

        public static CTAbsoluteAnchor parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(str, CTAbsoluteAnchor.type, xmlOptions);
        }

        public static CTAbsoluteAnchor parse(File file) throws XmlException, IOException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(file, CTAbsoluteAnchor.type, (XmlOptions) null);
        }

        public static CTAbsoluteAnchor parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(file, CTAbsoluteAnchor.type, xmlOptions);
        }

        public static CTAbsoluteAnchor parse(URL url) throws XmlException, IOException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(url, CTAbsoluteAnchor.type, (XmlOptions) null);
        }

        public static CTAbsoluteAnchor parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(url, CTAbsoluteAnchor.type, xmlOptions);
        }

        public static CTAbsoluteAnchor parse(InputStream inputStream) throws XmlException, IOException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(inputStream, CTAbsoluteAnchor.type, (XmlOptions) null);
        }

        public static CTAbsoluteAnchor parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(inputStream, CTAbsoluteAnchor.type, xmlOptions);
        }

        public static CTAbsoluteAnchor parse(Reader reader) throws XmlException, IOException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(reader, CTAbsoluteAnchor.type, (XmlOptions) null);
        }

        public static CTAbsoluteAnchor parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(reader, CTAbsoluteAnchor.type, xmlOptions);
        }

        public static CTAbsoluteAnchor parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(xMLStreamReader, CTAbsoluteAnchor.type, (XmlOptions) null);
        }

        public static CTAbsoluteAnchor parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(xMLStreamReader, CTAbsoluteAnchor.type, xmlOptions);
        }

        public static CTAbsoluteAnchor parse(Node node) throws XmlException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(node, CTAbsoluteAnchor.type, (XmlOptions) null);
        }

        public static CTAbsoluteAnchor parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(node, CTAbsoluteAnchor.type, xmlOptions);
        }

        public static CTAbsoluteAnchor parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(xMLInputStream, CTAbsoluteAnchor.type, (XmlOptions) null);
        }

        public static CTAbsoluteAnchor parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTAbsoluteAnchor) POIXMLTypeLoader.parse(xMLInputStream, CTAbsoluteAnchor.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAbsoluteAnchor.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAbsoluteAnchor.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPoint2D getPos();

    void setPos(CTPoint2D cTPoint2D);

    CTPoint2D addNewPos();

    CTPositiveSize2D getExt();

    void setExt(CTPositiveSize2D cTPositiveSize2D);

    CTPositiveSize2D addNewExt();

    CTShape getSp();

    boolean isSetSp();

    void setSp(CTShape cTShape);

    CTShape addNewSp();

    void unsetSp();

    CTGroupShape getGrpSp();

    boolean isSetGrpSp();

    void setGrpSp(CTGroupShape cTGroupShape);

    CTGroupShape addNewGrpSp();

    void unsetGrpSp();

    CTGraphicalObjectFrame getGraphicFrame();

    boolean isSetGraphicFrame();

    void setGraphicFrame(CTGraphicalObjectFrame cTGraphicalObjectFrame);

    CTGraphicalObjectFrame addNewGraphicFrame();

    void unsetGraphicFrame();

    CTConnector getCxnSp();

    boolean isSetCxnSp();

    void setCxnSp(CTConnector cTConnector);

    CTConnector addNewCxnSp();

    void unsetCxnSp();

    CTPicture getPic();

    boolean isSetPic();

    void setPic(CTPicture cTPicture);

    CTPicture addNewPic();

    void unsetPic();

    CTAnchorClientData getClientData();

    void setClientData(CTAnchorClientData cTAnchorClientData);

    CTAnchorClientData addNewClientData();
}
