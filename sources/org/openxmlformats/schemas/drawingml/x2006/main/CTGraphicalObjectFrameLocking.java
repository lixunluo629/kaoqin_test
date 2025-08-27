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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGraphicalObjectFrameLocking.class */
public interface CTGraphicalObjectFrameLocking extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTGraphicalObjectFrameLocking.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctgraphicalobjectframelocking42adtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTGraphicalObjectFrameLocking$Factory.class */
    public static final class Factory {
        public static CTGraphicalObjectFrameLocking newInstance() {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.newInstance(CTGraphicalObjectFrameLocking.type, null);
        }

        public static CTGraphicalObjectFrameLocking newInstance(XmlOptions xmlOptions) {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.newInstance(CTGraphicalObjectFrameLocking.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameLocking parse(String str) throws XmlException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(str, CTGraphicalObjectFrameLocking.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameLocking parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(str, CTGraphicalObjectFrameLocking.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameLocking parse(File file) throws XmlException, IOException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(file, CTGraphicalObjectFrameLocking.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameLocking parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(file, CTGraphicalObjectFrameLocking.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameLocking parse(URL url) throws XmlException, IOException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(url, CTGraphicalObjectFrameLocking.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameLocking parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(url, CTGraphicalObjectFrameLocking.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameLocking parse(InputStream inputStream) throws XmlException, IOException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(inputStream, CTGraphicalObjectFrameLocking.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameLocking parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(inputStream, CTGraphicalObjectFrameLocking.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameLocking parse(Reader reader) throws XmlException, IOException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(reader, CTGraphicalObjectFrameLocking.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameLocking parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(reader, CTGraphicalObjectFrameLocking.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameLocking parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(xMLStreamReader, CTGraphicalObjectFrameLocking.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameLocking parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(xMLStreamReader, CTGraphicalObjectFrameLocking.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameLocking parse(Node node) throws XmlException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(node, CTGraphicalObjectFrameLocking.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameLocking parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(node, CTGraphicalObjectFrameLocking.type, xmlOptions);
        }

        public static CTGraphicalObjectFrameLocking parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(xMLInputStream, CTGraphicalObjectFrameLocking.type, (XmlOptions) null);
        }

        public static CTGraphicalObjectFrameLocking parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTGraphicalObjectFrameLocking) POIXMLTypeLoader.parse(xMLInputStream, CTGraphicalObjectFrameLocking.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGraphicalObjectFrameLocking.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTGraphicalObjectFrameLocking.type, xmlOptions);
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

    boolean getNoDrilldown();

    XmlBoolean xgetNoDrilldown();

    boolean isSetNoDrilldown();

    void setNoDrilldown(boolean z);

    void xsetNoDrilldown(XmlBoolean xmlBoolean);

    void unsetNoDrilldown();

    boolean getNoSelect();

    XmlBoolean xgetNoSelect();

    boolean isSetNoSelect();

    void setNoSelect(boolean z);

    void xsetNoSelect(XmlBoolean xmlBoolean);

    void unsetNoSelect();

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
}
