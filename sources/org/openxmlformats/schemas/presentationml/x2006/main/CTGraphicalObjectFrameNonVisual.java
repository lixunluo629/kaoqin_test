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
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTGraphicalObjectFrameNonVisual.class */
public interface CTGraphicalObjectFrameNonVisual extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTGraphicalObjectFrameNonVisual.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctgraphicalobjectframenonvisual257dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTGraphicalObjectFrameNonVisual$Factory.class */
    public static final class Factory {
        public static CTGraphicalObjectFrameNonVisual newInstance() {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.newInstance(CTGraphicalObjectFrameNonVisual.type, null);
        }

        public static CTGraphicalObjectFrameNonVisual newInstance(XmlOptions xmlOptions) {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.newInstance(CTGraphicalObjectFrameNonVisual.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameNonVisual parse(String str) throws XmlException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(str, CTGraphicalObjectFrameNonVisual.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameNonVisual parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(str, CTGraphicalObjectFrameNonVisual.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameNonVisual parse(File file) throws XmlException, IOException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(file, CTGraphicalObjectFrameNonVisual.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameNonVisual parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(file, CTGraphicalObjectFrameNonVisual.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameNonVisual parse(URL url) throws XmlException, IOException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(url, CTGraphicalObjectFrameNonVisual.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameNonVisual parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(url, CTGraphicalObjectFrameNonVisual.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameNonVisual parse(InputStream inputStream) throws XmlException, IOException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(inputStream, CTGraphicalObjectFrameNonVisual.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameNonVisual parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(inputStream, CTGraphicalObjectFrameNonVisual.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameNonVisual parse(Reader reader) throws XmlException, IOException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(reader, CTGraphicalObjectFrameNonVisual.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameNonVisual parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(reader, CTGraphicalObjectFrameNonVisual.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameNonVisual parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(xMLStreamReader, CTGraphicalObjectFrameNonVisual.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameNonVisual parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(xMLStreamReader, CTGraphicalObjectFrameNonVisual.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameNonVisual parse(Node node) throws XmlException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(node, CTGraphicalObjectFrameNonVisual.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameNonVisual parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(node, CTGraphicalObjectFrameNonVisual.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameNonVisual parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(xMLInputStream, CTGraphicalObjectFrameNonVisual.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameNonVisual parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTGraphicalObjectFrameNonVisual) POIXMLTypeLoader.parse(xMLInputStream, CTGraphicalObjectFrameNonVisual.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGraphicalObjectFrameNonVisual.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGraphicalObjectFrameNonVisual.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTNonVisualDrawingProps getCNvPr();

    void setCNvPr(CTNonVisualDrawingProps cTNonVisualDrawingProps);

    CTNonVisualDrawingProps addNewCNvPr();

    CTNonVisualGraphicFrameProperties getCNvGraphicFramePr();

    void setCNvGraphicFramePr(CTNonVisualGraphicFrameProperties cTNonVisualGraphicFrameProperties);

    CTNonVisualGraphicFrameProperties addNewCNvGraphicFramePr();

    CTApplicationNonVisualDrawingProps getNvPr();

    void setNvPr(CTApplicationNonVisualDrawingProps cTApplicationNonVisualDrawingProps);

    CTApplicationNonVisualDrawingProps addNewNvPr();
}
