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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/wordprocessingDrawing/CTAnchor.class */
public interface CTAnchor extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTAnchor.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctanchorff8atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/wordprocessingDrawing/CTAnchor$Factory.class */
    public static final class Factory {
        public static CTAnchor newInstance() {
            return (CTAnchor) POIXMLTypeLoader.newInstance(CTAnchor.type, null);
        }

        public static CTAnchor newInstance(XmlOptions xmlOptions) {
            return (CTAnchor) POIXMLTypeLoader.newInstance(CTAnchor.type, xmlOptions);
        }

        public static CTAnchor parse(String str) throws XmlException {
            return (CTAnchor) POIXMLTypeLoader.parse(str, CTAnchor.type, (XmlOptions) null);
        }

        public static CTAnchor parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTAnchor) POIXMLTypeLoader.parse(str, CTAnchor.type, xmlOptions);
        }

        public static CTAnchor parse(File file) throws XmlException, IOException {
            return (CTAnchor) POIXMLTypeLoader.parse(file, CTAnchor.type, (XmlOptions) null);
        }

        public static CTAnchor parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAnchor) POIXMLTypeLoader.parse(file, CTAnchor.type, xmlOptions);
        }

        public static CTAnchor parse(URL url) throws XmlException, IOException {
            return (CTAnchor) POIXMLTypeLoader.parse(url, CTAnchor.type, (XmlOptions) null);
        }

        public static CTAnchor parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAnchor) POIXMLTypeLoader.parse(url, CTAnchor.type, xmlOptions);
        }

        public static CTAnchor parse(InputStream inputStream) throws XmlException, IOException {
            return (CTAnchor) POIXMLTypeLoader.parse(inputStream, CTAnchor.type, (XmlOptions) null);
        }

        public static CTAnchor parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAnchor) POIXMLTypeLoader.parse(inputStream, CTAnchor.type, xmlOptions);
        }

        public static CTAnchor parse(Reader reader) throws XmlException, IOException {
            return (CTAnchor) POIXMLTypeLoader.parse(reader, CTAnchor.type, (XmlOptions) null);
        }

        public static CTAnchor parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAnchor) POIXMLTypeLoader.parse(reader, CTAnchor.type, xmlOptions);
        }

        public static CTAnchor parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTAnchor) POIXMLTypeLoader.parse(xMLStreamReader, CTAnchor.type, (XmlOptions) null);
        }

        public static CTAnchor parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTAnchor) POIXMLTypeLoader.parse(xMLStreamReader, CTAnchor.type, xmlOptions);
        }

        public static CTAnchor parse(Node node) throws XmlException {
            return (CTAnchor) POIXMLTypeLoader.parse(node, CTAnchor.type, (XmlOptions) null);
        }

        public static CTAnchor parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTAnchor) POIXMLTypeLoader.parse(node, CTAnchor.type, xmlOptions);
        }

        public static CTAnchor parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTAnchor) POIXMLTypeLoader.parse(xMLInputStream, CTAnchor.type, (XmlOptions) null);
        }

        public static CTAnchor parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTAnchor) POIXMLTypeLoader.parse(xMLInputStream, CTAnchor.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAnchor.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAnchor.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPoint2D getSimplePos();

    void setSimplePos(CTPoint2D cTPoint2D);

    CTPoint2D addNewSimplePos();

    CTPosH getPositionH();

    void setPositionH(CTPosH cTPosH);

    CTPosH addNewPositionH();

    CTPosV getPositionV();

    void setPositionV(CTPosV cTPosV);

    CTPosV addNewPositionV();

    CTPositiveSize2D getExtent();

    void setExtent(CTPositiveSize2D cTPositiveSize2D);

    CTPositiveSize2D addNewExtent();

    CTEffectExtent getEffectExtent();

    boolean isSetEffectExtent();

    void setEffectExtent(CTEffectExtent cTEffectExtent);

    CTEffectExtent addNewEffectExtent();

    void unsetEffectExtent();

    CTWrapNone getWrapNone();

    boolean isSetWrapNone();

    void setWrapNone(CTWrapNone cTWrapNone);

    CTWrapNone addNewWrapNone();

    void unsetWrapNone();

    CTWrapSquare getWrapSquare();

    boolean isSetWrapSquare();

    void setWrapSquare(CTWrapSquare cTWrapSquare);

    CTWrapSquare addNewWrapSquare();

    void unsetWrapSquare();

    CTWrapTight getWrapTight();

    boolean isSetWrapTight();

    void setWrapTight(CTWrapTight cTWrapTight);

    CTWrapTight addNewWrapTight();

    void unsetWrapTight();

    CTWrapThrough getWrapThrough();

    boolean isSetWrapThrough();

    void setWrapThrough(CTWrapThrough cTWrapThrough);

    CTWrapThrough addNewWrapThrough();

    void unsetWrapThrough();

    CTWrapTopBottom getWrapTopAndBottom();

    boolean isSetWrapTopAndBottom();

    void setWrapTopAndBottom(CTWrapTopBottom cTWrapTopBottom);

    CTWrapTopBottom addNewWrapTopAndBottom();

    void unsetWrapTopAndBottom();

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

    boolean getSimplePos2();

    XmlBoolean xgetSimplePos2();

    boolean isSetSimplePos2();

    void setSimplePos2(boolean z);

    void xsetSimplePos2(XmlBoolean xmlBoolean);

    void unsetSimplePos2();

    long getRelativeHeight();

    XmlUnsignedInt xgetRelativeHeight();

    void setRelativeHeight(long j);

    void xsetRelativeHeight(XmlUnsignedInt xmlUnsignedInt);

    boolean getBehindDoc();

    XmlBoolean xgetBehindDoc();

    void setBehindDoc(boolean z);

    void xsetBehindDoc(XmlBoolean xmlBoolean);

    boolean getLocked();

    XmlBoolean xgetLocked();

    void setLocked(boolean z);

    void xsetLocked(XmlBoolean xmlBoolean);

    boolean getLayoutInCell();

    XmlBoolean xgetLayoutInCell();

    void setLayoutInCell(boolean z);

    void xsetLayoutInCell(XmlBoolean xmlBoolean);

    boolean getHidden();

    XmlBoolean xgetHidden();

    boolean isSetHidden();

    void setHidden(boolean z);

    void xsetHidden(XmlBoolean xmlBoolean);

    void unsetHidden();

    boolean getAllowOverlap();

    XmlBoolean xgetAllowOverlap();

    void setAllowOverlap(boolean z);

    void xsetAllowOverlap(XmlBoolean xmlBoolean);
}
