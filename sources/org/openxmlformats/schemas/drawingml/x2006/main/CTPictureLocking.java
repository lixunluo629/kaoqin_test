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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPictureLocking.class */
public interface CTPictureLocking extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPictureLocking.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpicturelockinga414type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPictureLocking$Factory.class */
    public static final class Factory {
        public static CTPictureLocking newInstance() {
            return (CTPictureLocking) POIXMLTypeLoader.newInstance(CTPictureLocking.type, null);
        }

        public static CTPictureLocking newInstance(XmlOptions xmlOptions) {
            return (CTPictureLocking) POIXMLTypeLoader.newInstance(CTPictureLocking.type, xmlOptions);
        }

        public static CTPictureLocking parse(String str) throws XmlException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(str, CTPictureLocking.type, (XmlOptions) null);
        }

        public static CTPictureLocking parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(str, CTPictureLocking.type, xmlOptions);
        }

        public static CTPictureLocking parse(File file) throws XmlException, IOException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(file, CTPictureLocking.type, (XmlOptions) null);
        }

        public static CTPictureLocking parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(file, CTPictureLocking.type, xmlOptions);
        }

        public static CTPictureLocking parse(URL url) throws XmlException, IOException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(url, CTPictureLocking.type, (XmlOptions) null);
        }

        public static CTPictureLocking parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(url, CTPictureLocking.type, xmlOptions);
        }

        public static CTPictureLocking parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(inputStream, CTPictureLocking.type, (XmlOptions) null);
        }

        public static CTPictureLocking parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(inputStream, CTPictureLocking.type, xmlOptions);
        }

        public static CTPictureLocking parse(Reader reader) throws XmlException, IOException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(reader, CTPictureLocking.type, (XmlOptions) null);
        }

        public static CTPictureLocking parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(reader, CTPictureLocking.type, xmlOptions);
        }

        public static CTPictureLocking parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(xMLStreamReader, CTPictureLocking.type, (XmlOptions) null);
        }

        public static CTPictureLocking parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(xMLStreamReader, CTPictureLocking.type, xmlOptions);
        }

        public static CTPictureLocking parse(Node node) throws XmlException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(node, CTPictureLocking.type, (XmlOptions) null);
        }

        public static CTPictureLocking parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(node, CTPictureLocking.type, xmlOptions);
        }

        public static CTPictureLocking parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(xMLInputStream, CTPictureLocking.type, (XmlOptions) null);
        }

        public static CTPictureLocking parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPictureLocking) POIXMLTypeLoader.parse(xMLInputStream, CTPictureLocking.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPictureLocking.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPictureLocking.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    boolean getNoGrp();

    XmlBoolean xgetNoGrp();

    boolean isSetNoGrp();

    void setNoGrp(boolean z);

    void xsetNoGrp(XmlBoolean xmlBoolean);

    void unsetNoGrp();

    boolean getNoSelect();

    XmlBoolean xgetNoSelect();

    boolean isSetNoSelect();

    void setNoSelect(boolean z);

    void xsetNoSelect(XmlBoolean xmlBoolean);

    void unsetNoSelect();

    boolean getNoRot();

    XmlBoolean xgetNoRot();

    boolean isSetNoRot();

    void setNoRot(boolean z);

    void xsetNoRot(XmlBoolean xmlBoolean);

    void unsetNoRot();

    boolean getNoChangeAspect();

    XmlBoolean xgetNoChangeAspect();

    boolean isSetNoChangeAspect();

    void setNoChangeAspect(boolean z);

    void xsetNoChangeAspect(XmlBoolean xmlBoolean);

    void unsetNoChangeAspect();

    boolean getNoMove();

    XmlBoolean xgetNoMove();

    boolean isSetNoMove();

    void setNoMove(boolean z);

    void xsetNoMove(XmlBoolean xmlBoolean);

    void unsetNoMove();

    boolean getNoResize();

    XmlBoolean xgetNoResize();

    boolean isSetNoResize();

    void setNoResize(boolean z);

    void xsetNoResize(XmlBoolean xmlBoolean);

    void unsetNoResize();

    boolean getNoEditPoints();

    XmlBoolean xgetNoEditPoints();

    boolean isSetNoEditPoints();

    void setNoEditPoints(boolean z);

    void xsetNoEditPoints(XmlBoolean xmlBoolean);

    void unsetNoEditPoints();

    boolean getNoAdjustHandles();

    XmlBoolean xgetNoAdjustHandles();

    boolean isSetNoAdjustHandles();

    void setNoAdjustHandles(boolean z);

    void xsetNoAdjustHandles(XmlBoolean xmlBoolean);

    void unsetNoAdjustHandles();

    boolean getNoChangeArrowheads();

    XmlBoolean xgetNoChangeArrowheads();

    boolean isSetNoChangeArrowheads();

    void setNoChangeArrowheads(boolean z);

    void xsetNoChangeArrowheads(XmlBoolean xmlBoolean);

    void unsetNoChangeArrowheads();

    boolean getNoChangeShapeType();

    XmlBoolean xgetNoChangeShapeType();

    boolean isSetNoChangeShapeType();

    void setNoChangeShapeType(boolean z);

    void xsetNoChangeShapeType(XmlBoolean xmlBoolean);

    void unsetNoChangeShapeType();

    boolean getNoCrop();

    XmlBoolean xgetNoCrop();

    boolean isSetNoCrop();

    void setNoCrop(boolean z);

    void xsetNoCrop(XmlBoolean xmlBoolean);

    void unsetNoCrop();
}
