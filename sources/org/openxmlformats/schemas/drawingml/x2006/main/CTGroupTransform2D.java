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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGroupTransform2D.class */
public interface CTGroupTransform2D extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTGroupTransform2D.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctgrouptransform2d411atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGroupTransform2D$Factory.class */
    public static final class Factory {
        public static CTGroupTransform2D newInstance() {
            return (CTGroupTransform2D) POIXMLTypeLoader.newInstance(CTGroupTransform2D.type, null);
        }

        public static CTGroupTransform2D newInstance(XmlOptions xmlOptions) {
            return (CTGroupTransform2D) POIXMLTypeLoader.newInstance(CTGroupTransform2D.type, xmlOptions);
        }

        public static CTGroupTransform2D parse(String str) throws XmlException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(str, CTGroupTransform2D.type, (XmlOptions) null);
        }

        public static CTGroupTransform2D parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(str, CTGroupTransform2D.type, xmlOptions);
        }

        public static CTGroupTransform2D parse(File file) throws XmlException, IOException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(file, CTGroupTransform2D.type, (XmlOptions) null);
        }

        public static CTGroupTransform2D parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(file, CTGroupTransform2D.type, xmlOptions);
        }

        public static CTGroupTransform2D parse(URL url) throws XmlException, IOException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(url, CTGroupTransform2D.type, (XmlOptions) null);
        }

        public static CTGroupTransform2D parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(url, CTGroupTransform2D.type, xmlOptions);
        }

        public static CTGroupTransform2D parse(InputStream inputStream) throws XmlException, IOException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(inputStream, CTGroupTransform2D.type, (XmlOptions) null);
        }

        public static CTGroupTransform2D parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(inputStream, CTGroupTransform2D.type, xmlOptions);
        }

        public static CTGroupTransform2D parse(Reader reader) throws XmlException, IOException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(reader, CTGroupTransform2D.type, (XmlOptions) null);
        }

        public static CTGroupTransform2D parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(reader, CTGroupTransform2D.type, xmlOptions);
        }

        public static CTGroupTransform2D parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(xMLStreamReader, CTGroupTransform2D.type, (XmlOptions) null);
        }

        public static CTGroupTransform2D parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(xMLStreamReader, CTGroupTransform2D.type, xmlOptions);
        }

        public static CTGroupTransform2D parse(Node node) throws XmlException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(node, CTGroupTransform2D.type, (XmlOptions) null);
        }

        public static CTGroupTransform2D parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(node, CTGroupTransform2D.type, xmlOptions);
        }

        public static CTGroupTransform2D parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(xMLInputStream, CTGroupTransform2D.type, (XmlOptions) null);
        }

        public static CTGroupTransform2D parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTGroupTransform2D) POIXMLTypeLoader.parse(xMLInputStream, CTGroupTransform2D.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGroupTransform2D.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGroupTransform2D.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPoint2D getOff();

    boolean isSetOff();

    void setOff(CTPoint2D cTPoint2D);

    CTPoint2D addNewOff();

    void unsetOff();

    CTPositiveSize2D getExt();

    boolean isSetExt();

    void setExt(CTPositiveSize2D cTPositiveSize2D);

    CTPositiveSize2D addNewExt();

    void unsetExt();

    CTPoint2D getChOff();

    boolean isSetChOff();

    void setChOff(CTPoint2D cTPoint2D);

    CTPoint2D addNewChOff();

    void unsetChOff();

    CTPositiveSize2D getChExt();

    boolean isSetChExt();

    void setChExt(CTPositiveSize2D cTPositiveSize2D);

    CTPositiveSize2D addNewChExt();

    void unsetChExt();

    int getRot();

    STAngle xgetRot();

    boolean isSetRot();

    void setRot(int i);

    void xsetRot(STAngle sTAngle);

    void unsetRot();

    boolean getFlipH();

    XmlBoolean xgetFlipH();

    boolean isSetFlipH();

    void setFlipH(boolean z);

    void xsetFlipH(XmlBoolean xmlBoolean);

    void unsetFlipH();

    boolean getFlipV();

    XmlBoolean xgetFlipV();

    boolean isSetFlipV();

    void setFlipV(boolean z);

    void xsetFlipV(XmlBoolean xmlBoolean);

    void unsetFlipV();
}
