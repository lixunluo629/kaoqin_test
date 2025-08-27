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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTGraphicalObjectFrame.class */
public interface CTGraphicalObjectFrame extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTGraphicalObjectFrame.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctgraphicalobjectframebfeatype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTGraphicalObjectFrame$Factory.class */
    public static final class Factory {
        public static CTGraphicalObjectFrame newInstance() {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.newInstance(CTGraphicalObjectFrame.type, null);
        }

        public static CTGraphicalObjectFrame newInstance(XmlOptions xmlOptions) {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.newInstance(CTGraphicalObjectFrame.type, xmlOptions);
        }

        public static CTGraphicalObjectFrame parse(String str) throws XmlException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(str, CTGraphicalObjectFrame.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrame parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(str, CTGraphicalObjectFrame.type, xmlOptions);
        }

        public static CTGraphicalObjectFrame parse(File file) throws XmlException, IOException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(file, CTGraphicalObjectFrame.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrame parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(file, CTGraphicalObjectFrame.type, xmlOptions);
        }

        public static CTGraphicalObjectFrame parse(URL url) throws XmlException, IOException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(url, CTGraphicalObjectFrame.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrame parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(url, CTGraphicalObjectFrame.type, xmlOptions);
        }

        public static CTGraphicalObjectFrame parse(InputStream inputStream) throws XmlException, IOException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(inputStream, CTGraphicalObjectFrame.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrame parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(inputStream, CTGraphicalObjectFrame.type, xmlOptions);
        }

        public static CTGraphicalObjectFrame parse(Reader reader) throws XmlException, IOException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(reader, CTGraphicalObjectFrame.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrame parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(reader, CTGraphicalObjectFrame.type, xmlOptions);
        }

        public static CTGraphicalObjectFrame parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(xMLStreamReader, CTGraphicalObjectFrame.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrame parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(xMLStreamReader, CTGraphicalObjectFrame.type, xmlOptions);
        }

        public static CTGraphicalObjectFrame parse(Node node) throws XmlException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(node, CTGraphicalObjectFrame.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrame parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(node, CTGraphicalObjectFrame.type, xmlOptions);
        }

        public static CTGraphicalObjectFrame parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(xMLInputStream, CTGraphicalObjectFrame.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrame parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTGraphicalObjectFrame) POIXMLTypeLoader.parse(xMLInputStream, CTGraphicalObjectFrame.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGraphicalObjectFrame.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGraphicalObjectFrame.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTGraphicalObjectFrameNonVisual getNvGraphicFramePr();

    void setNvGraphicFramePr(CTGraphicalObjectFrameNonVisual cTGraphicalObjectFrameNonVisual);

    CTGraphicalObjectFrameNonVisual addNewNvGraphicFramePr();

    CTTransform2D getXfrm();

    void setXfrm(CTTransform2D cTTransform2D);

    CTTransform2D addNewXfrm();

    CTGraphicalObject getGraphic();

    void setGraphic(CTGraphicalObject cTGraphicalObject);

    CTGraphicalObject addNewGraphic();

    CTExtensionListModify getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionListModify cTExtensionListModify);

    CTExtensionListModify addNewExtLst();

    void unsetExtLst();
}
