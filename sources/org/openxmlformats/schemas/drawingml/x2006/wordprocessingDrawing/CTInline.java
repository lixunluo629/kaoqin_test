package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

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
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/wordprocessingDrawing/CTInline.class */
public interface CTInline extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTInline.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctinline5726type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/wordprocessingDrawing/CTInline$Factory.class */
    public static final class Factory {
        public static CTInline newInstance() {
            return (CTInline) POIXMLTypeLoader.newInstance(CTInline.type, null);
        }

        public static CTInline newInstance(XmlOptions xmlOptions) {
            return (CTInline) POIXMLTypeLoader.newInstance(CTInline.type, xmlOptions);
        }

        public static CTInline parse(String str) throws XmlException {
            return (CTInline) POIXMLTypeLoader.parse(str, CTInline.type, (XmlOptions) null);
        }

        public static CTInline parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTInline) POIXMLTypeLoader.parse(str, CTInline.type, xmlOptions);
        }

        public static CTInline parse(File file) throws XmlException, IOException {
            return (CTInline) POIXMLTypeLoader.parse(file, CTInline.type, (XmlOptions) null);
        }

        public static CTInline parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTInline) POIXMLTypeLoader.parse(file, CTInline.type, xmlOptions);
        }

        public static CTInline parse(URL url) throws XmlException, IOException {
            return (CTInline) POIXMLTypeLoader.parse(url, CTInline.type, (XmlOptions) null);
        }

        public static CTInline parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTInline) POIXMLTypeLoader.parse(url, CTInline.type, xmlOptions);
        }

        public static CTInline parse(InputStream inputStream) throws XmlException, IOException {
            return (CTInline) POIXMLTypeLoader.parse(inputStream, CTInline.type, (XmlOptions) null);
        }

        public static CTInline parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTInline) POIXMLTypeLoader.parse(inputStream, CTInline.type, xmlOptions);
        }

        public static CTInline parse(Reader reader) throws XmlException, IOException {
            return (CTInline) POIXMLTypeLoader.parse(reader, CTInline.type, (XmlOptions) null);
        }

        public static CTInline parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTInline) POIXMLTypeLoader.parse(reader, CTInline.type, xmlOptions);
        }

        public static CTInline parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTInline) POIXMLTypeLoader.parse(xMLStreamReader, CTInline.type, (XmlOptions) null);
        }

        public static CTInline parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTInline) POIXMLTypeLoader.parse(xMLStreamReader, CTInline.type, xmlOptions);
        }

        public static CTInline parse(Node node) throws XmlException {
            return (CTInline) POIXMLTypeLoader.parse(node, CTInline.type, (XmlOptions) null);
        }

        public static CTInline parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTInline) POIXMLTypeLoader.parse(node, CTInline.type, xmlOptions);
        }

        public static CTInline parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTInline) POIXMLTypeLoader.parse(xMLInputStream, CTInline.type, (XmlOptions) null);
        }

        public static CTInline parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTInline) POIXMLTypeLoader.parse(xMLInputStream, CTInline.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTInline.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTInline.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPositiveSize2D getExtent();

    void setExtent(CTPositiveSize2D cTPositiveSize2D);

    CTPositiveSize2D addNewExtent();

    CTEffectExtent getEffectExtent();

    boolean isSetEffectExtent();

    void setEffectExtent(CTEffectExtent cTEffectExtent);

    CTEffectExtent addNewEffectExtent();

    void unsetEffectExtent();

    CTNonVisualDrawingProps getDocPr();

    void setDocPr(CTNonVisualDrawingProps cTNonVisualDrawingProps);

    CTNonVisualDrawingProps addNewDocPr();

    CTNonVisualGraphicFrameProperties getCNvGraphicFramePr();

    boolean isSetCNvGraphicFramePr();

    void setCNvGraphicFramePr(CTNonVisualGraphicFrameProperties cTNonVisualGraphicFrameProperties);

    CTNonVisualGraphicFrameProperties addNewCNvGraphicFramePr();

    void unsetCNvGraphicFramePr();

    CTGraphicalObject getGraphic();

    void setGraphic(CTGraphicalObject cTGraphicalObject);

    CTGraphicalObject addNewGraphic();

    long getDistT();

    STWrapDistance xgetDistT();

    boolean isSetDistT();

    void setDistT(long j);

    void xsetDistT(STWrapDistance sTWrapDistance);

    void unsetDistT();

    long getDistB();

    STWrapDistance xgetDistB();

    boolean isSetDistB();

    void setDistB(long j);

    void xsetDistB(STWrapDistance sTWrapDistance);

    void unsetDistB();

    long getDistL();

    STWrapDistance xgetDistL();

    boolean isSetDistL();

    void setDistL(long j);

    void xsetDistL(STWrapDistance sTWrapDistance);

    void unsetDistL();

    long getDistR();

    STWrapDistance xgetDistR();

    boolean isSetDistR();

    void setDistR(long j);

    void xsetDistR(STWrapDistance sTWrapDistance);

    void unsetDistR();
}
