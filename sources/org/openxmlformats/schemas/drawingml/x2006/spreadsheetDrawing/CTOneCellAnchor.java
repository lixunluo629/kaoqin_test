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
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/CTOneCellAnchor.class */
public interface CTOneCellAnchor extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTOneCellAnchor.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctonecellanchor0527type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/CTOneCellAnchor$Factory.class */
    public static final class Factory {
        public static CTOneCellAnchor newInstance() {
            return (CTOneCellAnchor) POIXMLTypeLoader.newInstance(CTOneCellAnchor.type, null);
        }

        public static CTOneCellAnchor newInstance(XmlOptions xmlOptions) {
            return (CTOneCellAnchor) POIXMLTypeLoader.newInstance(CTOneCellAnchor.type, xmlOptions);
        }

        public static CTOneCellAnchor parse(String str) throws XmlException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(str, CTOneCellAnchor.type, (XmlOptions) null);
        }

        public static CTOneCellAnchor parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(str, CTOneCellAnchor.type, xmlOptions);
        }

        public static CTOneCellAnchor parse(File file) throws XmlException, IOException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(file, CTOneCellAnchor.type, (XmlOptions) null);
        }

        public static CTOneCellAnchor parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(file, CTOneCellAnchor.type, xmlOptions);
        }

        public static CTOneCellAnchor parse(URL url) throws XmlException, IOException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(url, CTOneCellAnchor.type, (XmlOptions) null);
        }

        public static CTOneCellAnchor parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(url, CTOneCellAnchor.type, xmlOptions);
        }

        public static CTOneCellAnchor parse(InputStream inputStream) throws XmlException, IOException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(inputStream, CTOneCellAnchor.type, (XmlOptions) null);
        }

        public static CTOneCellAnchor parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(inputStream, CTOneCellAnchor.type, xmlOptions);
        }

        public static CTOneCellAnchor parse(Reader reader) throws XmlException, IOException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(reader, CTOneCellAnchor.type, (XmlOptions) null);
        }

        public static CTOneCellAnchor parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(reader, CTOneCellAnchor.type, xmlOptions);
        }

        public static CTOneCellAnchor parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(xMLStreamReader, CTOneCellAnchor.type, (XmlOptions) null);
        }

        public static CTOneCellAnchor parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(xMLStreamReader, CTOneCellAnchor.type, xmlOptions);
        }

        public static CTOneCellAnchor parse(Node node) throws XmlException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(node, CTOneCellAnchor.type, (XmlOptions) null);
        }

        public static CTOneCellAnchor parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(node, CTOneCellAnchor.type, xmlOptions);
        }

        public static CTOneCellAnchor parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(xMLInputStream, CTOneCellAnchor.type, (XmlOptions) null);
        }

        public static CTOneCellAnchor parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTOneCellAnchor) POIXMLTypeLoader.parse(xMLInputStream, CTOneCellAnchor.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTOneCellAnchor.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTOneCellAnchor.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTMarker getFrom();

    void setFrom(CTMarker cTMarker);

    CTMarker addNewFrom();

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
