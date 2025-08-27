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
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/CTTwoCellAnchor.class */
public interface CTTwoCellAnchor extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTwoCellAnchor.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttwocellanchor1e8dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/CTTwoCellAnchor$Factory.class */
    public static final class Factory {
        public static CTTwoCellAnchor newInstance() {
            return (CTTwoCellAnchor) POIXMLTypeLoader.newInstance(CTTwoCellAnchor.type, null);
        }

        public static CTTwoCellAnchor newInstance(XmlOptions xmlOptions) {
            return (CTTwoCellAnchor) POIXMLTypeLoader.newInstance(CTTwoCellAnchor.type, xmlOptions);
        }

        public static CTTwoCellAnchor parse(String str) throws XmlException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(str, CTTwoCellAnchor.type, (XmlOptions) null);
        }

        public static CTTwoCellAnchor parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(str, CTTwoCellAnchor.type, xmlOptions);
        }

        public static CTTwoCellAnchor parse(File file) throws XmlException, IOException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(file, CTTwoCellAnchor.type, (XmlOptions) null);
        }

        public static CTTwoCellAnchor parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(file, CTTwoCellAnchor.type, xmlOptions);
        }

        public static CTTwoCellAnchor parse(URL url) throws XmlException, IOException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(url, CTTwoCellAnchor.type, (XmlOptions) null);
        }

        public static CTTwoCellAnchor parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(url, CTTwoCellAnchor.type, xmlOptions);
        }

        public static CTTwoCellAnchor parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(inputStream, CTTwoCellAnchor.type, (XmlOptions) null);
        }

        public static CTTwoCellAnchor parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(inputStream, CTTwoCellAnchor.type, xmlOptions);
        }

        public static CTTwoCellAnchor parse(Reader reader) throws XmlException, IOException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(reader, CTTwoCellAnchor.type, (XmlOptions) null);
        }

        public static CTTwoCellAnchor parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(reader, CTTwoCellAnchor.type, xmlOptions);
        }

        public static CTTwoCellAnchor parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(xMLStreamReader, CTTwoCellAnchor.type, (XmlOptions) null);
        }

        public static CTTwoCellAnchor parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(xMLStreamReader, CTTwoCellAnchor.type, xmlOptions);
        }

        public static CTTwoCellAnchor parse(Node node) throws XmlException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(node, CTTwoCellAnchor.type, (XmlOptions) null);
        }

        public static CTTwoCellAnchor parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(node, CTTwoCellAnchor.type, xmlOptions);
        }

        public static CTTwoCellAnchor parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(xMLInputStream, CTTwoCellAnchor.type, (XmlOptions) null);
        }

        public static CTTwoCellAnchor parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTwoCellAnchor) POIXMLTypeLoader.parse(xMLInputStream, CTTwoCellAnchor.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTwoCellAnchor.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTwoCellAnchor.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTMarker getFrom();

    void setFrom(CTMarker cTMarker);

    CTMarker addNewFrom();

    CTMarker getTo();

    void setTo(CTMarker cTMarker);

    CTMarker addNewTo();

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

    STEditAs.Enum getEditAs();

    STEditAs xgetEditAs();

    boolean isSetEditAs();

    void setEditAs(STEditAs.Enum r1);

    void xsetEditAs(STEditAs sTEditAs);

    void unsetEditAs();
}
