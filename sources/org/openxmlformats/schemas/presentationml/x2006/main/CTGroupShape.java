package org.openxmlformats.schemas.presentationml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTGroupShape.class */
public interface CTGroupShape extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTGroupShape.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctgroupshape5b43type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTGroupShape$Factory.class */
    public static final class Factory {
        public static CTGroupShape newInstance() {
            return (CTGroupShape) POIXMLTypeLoader.newInstance(CTGroupShape.type, null);
        }

        public static CTGroupShape newInstance(XmlOptions xmlOptions) {
            return (CTGroupShape) POIXMLTypeLoader.newInstance(CTGroupShape.type, xmlOptions);
        }

        public static CTGroupShape parse(String str) throws XmlException {
            return (CTGroupShape) POIXMLTypeLoader.parse(str, CTGroupShape.type, (XmlOptions) null);
        }

        public static CTGroupShape parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTGroupShape) POIXMLTypeLoader.parse(str, CTGroupShape.type, xmlOptions);
        }

        public static CTGroupShape parse(File file) throws XmlException, IOException {
            return (CTGroupShape) POIXMLTypeLoader.parse(file, CTGroupShape.type, (XmlOptions) null);
        }

        public static CTGroupShape parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupShape) POIXMLTypeLoader.parse(file, CTGroupShape.type, xmlOptions);
        }

        public static CTGroupShape parse(URL url) throws XmlException, IOException {
            return (CTGroupShape) POIXMLTypeLoader.parse(url, CTGroupShape.type, (XmlOptions) null);
        }

        public static CTGroupShape parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupShape) POIXMLTypeLoader.parse(url, CTGroupShape.type, xmlOptions);
        }

        public static CTGroupShape parse(InputStream inputStream) throws XmlException, IOException {
            return (CTGroupShape) POIXMLTypeLoader.parse(inputStream, CTGroupShape.type, (XmlOptions) null);
        }

        public static CTGroupShape parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupShape) POIXMLTypeLoader.parse(inputStream, CTGroupShape.type, xmlOptions);
        }

        public static CTGroupShape parse(Reader reader) throws XmlException, IOException {
            return (CTGroupShape) POIXMLTypeLoader.parse(reader, CTGroupShape.type, (XmlOptions) null);
        }

        public static CTGroupShape parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupShape) POIXMLTypeLoader.parse(reader, CTGroupShape.type, xmlOptions);
        }

        public static CTGroupShape parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTGroupShape) POIXMLTypeLoader.parse(xMLStreamReader, CTGroupShape.type, (XmlOptions) null);
        }

        public static CTGroupShape parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTGroupShape) POIXMLTypeLoader.parse(xMLStreamReader, CTGroupShape.type, xmlOptions);
        }

        public static CTGroupShape parse(Node node) throws XmlException {
            return (CTGroupShape) POIXMLTypeLoader.parse(node, CTGroupShape.type, (XmlOptions) null);
        }

        public static CTGroupShape parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTGroupShape) POIXMLTypeLoader.parse(node, CTGroupShape.type, xmlOptions);
        }

        public static CTGroupShape parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTGroupShape) POIXMLTypeLoader.parse(xMLInputStream, CTGroupShape.type, (XmlOptions) null);
        }

        public static CTGroupShape parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTGroupShape) POIXMLTypeLoader.parse(xMLInputStream, CTGroupShape.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGroupShape.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGroupShape.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTGroupShapeNonVisual getNvGrpSpPr();

    void setNvGrpSpPr(CTGroupShapeNonVisual cTGroupShapeNonVisual);

    CTGroupShapeNonVisual addNewNvGrpSpPr();

    CTGroupShapeProperties getGrpSpPr();

    void setGrpSpPr(CTGroupShapeProperties cTGroupShapeProperties);

    CTGroupShapeProperties addNewGrpSpPr();

    List<CTShape> getSpList();

    CTShape[] getSpArray();

    CTShape getSpArray(int i);

    int sizeOfSpArray();

    void setSpArray(CTShape[] cTShapeArr);

    void setSpArray(int i, CTShape cTShape);

    CTShape insertNewSp(int i);

    CTShape addNewSp();

    void removeSp(int i);

    List<CTGroupShape> getGrpSpList();

    CTGroupShape[] getGrpSpArray();

    CTGroupShape getGrpSpArray(int i);

    int sizeOfGrpSpArray();

    void setGrpSpArray(CTGroupShape[] cTGroupShapeArr);

    void setGrpSpArray(int i, CTGroupShape cTGroupShape);

    CTGroupShape insertNewGrpSp(int i);

    CTGroupShape addNewGrpSp();

    void removeGrpSp(int i);

    List<CTGraphicalObjectFrame> getGraphicFrameList();

    CTGraphicalObjectFrame[] getGraphicFrameArray();

    CTGraphicalObjectFrame getGraphicFrameArray(int i);

    int sizeOfGraphicFrameArray();

    void setGraphicFrameArray(CTGraphicalObjectFrame[] cTGraphicalObjectFrameArr);

    void setGraphicFrameArray(int i, CTGraphicalObjectFrame cTGraphicalObjectFrame);

    CTGraphicalObjectFrame insertNewGraphicFrame(int i);

    CTGraphicalObjectFrame addNewGraphicFrame();

    void removeGraphicFrame(int i);

    List<CTConnector> getCxnSpList();

    CTConnector[] getCxnSpArray();

    CTConnector getCxnSpArray(int i);

    int sizeOfCxnSpArray();

    void setCxnSpArray(CTConnector[] cTConnectorArr);

    void setCxnSpArray(int i, CTConnector cTConnector);

    CTConnector insertNewCxnSp(int i);

    CTConnector addNewCxnSp();

    void removeCxnSp(int i);

    List<CTPicture> getPicList();

    CTPicture[] getPicArray();

    CTPicture getPicArray(int i);

    int sizeOfPicArray();

    void setPicArray(CTPicture[] cTPictureArr);

    void setPicArray(int i, CTPicture cTPicture);

    CTPicture insertNewPic(int i);

    CTPicture addNewPic();

    void removePic(int i);

    CTExtensionListModify getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionListModify cTExtensionListModify);

    CTExtensionListModify addNewExtLst();

    void unsetExtLst();
}
