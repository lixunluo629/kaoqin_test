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
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGradientFillProperties.class */
public interface CTGradientFillProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTGradientFillProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctgradientfillproperties81c1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGradientFillProperties$Factory.class */
    public static final class Factory {
        public static CTGradientFillProperties newInstance() {
            return (CTGradientFillProperties) POIXMLTypeLoader.newInstance(CTGradientFillProperties.type, null);
        }

        public static CTGradientFillProperties newInstance(XmlOptions xmlOptions) {
            return (CTGradientFillProperties) POIXMLTypeLoader.newInstance(CTGradientFillProperties.type, xmlOptions);
        }

        public static CTGradientFillProperties parse(String str) throws XmlException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(str, CTGradientFillProperties.type, (XmlOptions) null);
        }

        public static CTGradientFillProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(str, CTGradientFillProperties.type, xmlOptions);
        }

        public static CTGradientFillProperties parse(File file) throws XmlException, IOException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(file, CTGradientFillProperties.type, (XmlOptions) null);
        }

        public static CTGradientFillProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(file, CTGradientFillProperties.type, xmlOptions);
        }

        public static CTGradientFillProperties parse(URL url) throws XmlException, IOException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(url, CTGradientFillProperties.type, (XmlOptions) null);
        }

        public static CTGradientFillProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(url, CTGradientFillProperties.type, xmlOptions);
        }

        public static CTGradientFillProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(inputStream, CTGradientFillProperties.type, (XmlOptions) null);
        }

        public static CTGradientFillProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(inputStream, CTGradientFillProperties.type, xmlOptions);
        }

        public static CTGradientFillProperties parse(Reader reader) throws XmlException, IOException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(reader, CTGradientFillProperties.type, (XmlOptions) null);
        }

        public static CTGradientFillProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(reader, CTGradientFillProperties.type, xmlOptions);
        }

        public static CTGradientFillProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTGradientFillProperties.type, (XmlOptions) null);
        }

        public static CTGradientFillProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTGradientFillProperties.type, xmlOptions);
        }

        public static CTGradientFillProperties parse(Node node) throws XmlException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(node, CTGradientFillProperties.type, (XmlOptions) null);
        }

        public static CTGradientFillProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(node, CTGradientFillProperties.type, xmlOptions);
        }

        public static CTGradientFillProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(xMLInputStream, CTGradientFillProperties.type, (XmlOptions) null);
        }

        public static CTGradientFillProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTGradientFillProperties) POIXMLTypeLoader.parse(xMLInputStream, CTGradientFillProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGradientFillProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGradientFillProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTGradientStopList getGsLst();

    boolean isSetGsLst();

    void setGsLst(CTGradientStopList cTGradientStopList);

    CTGradientStopList addNewGsLst();

    void unsetGsLst();

    CTLinearShadeProperties getLin();

    boolean isSetLin();

    void setLin(CTLinearShadeProperties cTLinearShadeProperties);

    CTLinearShadeProperties addNewLin();

    void unsetLin();

    CTPathShadeProperties getPath();

    boolean isSetPath();

    void setPath(CTPathShadeProperties cTPathShadeProperties);

    CTPathShadeProperties addNewPath();

    void unsetPath();

    CTRelativeRect getTileRect();

    boolean isSetTileRect();

    void setTileRect(CTRelativeRect cTRelativeRect);

    CTRelativeRect addNewTileRect();

    void unsetTileRect();

    STTileFlipMode$Enum getFlip();

    STTileFlipMode xgetFlip();

    boolean isSetFlip();

    void setFlip(STTileFlipMode$Enum sTTileFlipMode$Enum);

    void xsetFlip(STTileFlipMode sTTileFlipMode);

    void unsetFlip();

    boolean getRotWithShape();

    XmlBoolean xgetRotWithShape();

    boolean isSetRotWithShape();

    void setRotWithShape(boolean z);

    void xsetRotWithShape(XmlBoolean xmlBoolean);

    void unsetRotWithShape();
}
