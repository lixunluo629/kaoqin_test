package org.openxmlformats.schemas.drawingml.x2006.chart;

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
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTManualLayout.class */
public interface CTManualLayout extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTManualLayout.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctmanuallayout872ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTManualLayout$Factory.class */
    public static final class Factory {
        public static CTManualLayout newInstance() {
            return (CTManualLayout) POIXMLTypeLoader.newInstance(CTManualLayout.type, null);
        }

        public static CTManualLayout newInstance(XmlOptions xmlOptions) {
            return (CTManualLayout) POIXMLTypeLoader.newInstance(CTManualLayout.type, xmlOptions);
        }

        public static CTManualLayout parse(String str) throws XmlException {
            return (CTManualLayout) POIXMLTypeLoader.parse(str, CTManualLayout.type, (XmlOptions) null);
        }

        public static CTManualLayout parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTManualLayout) POIXMLTypeLoader.parse(str, CTManualLayout.type, xmlOptions);
        }

        public static CTManualLayout parse(File file) throws XmlException, IOException {
            return (CTManualLayout) POIXMLTypeLoader.parse(file, CTManualLayout.type, (XmlOptions) null);
        }

        public static CTManualLayout parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTManualLayout) POIXMLTypeLoader.parse(file, CTManualLayout.type, xmlOptions);
        }

        public static CTManualLayout parse(URL url) throws XmlException, IOException {
            return (CTManualLayout) POIXMLTypeLoader.parse(url, CTManualLayout.type, (XmlOptions) null);
        }

        public static CTManualLayout parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTManualLayout) POIXMLTypeLoader.parse(url, CTManualLayout.type, xmlOptions);
        }

        public static CTManualLayout parse(InputStream inputStream) throws XmlException, IOException {
            return (CTManualLayout) POIXMLTypeLoader.parse(inputStream, CTManualLayout.type, (XmlOptions) null);
        }

        public static CTManualLayout parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTManualLayout) POIXMLTypeLoader.parse(inputStream, CTManualLayout.type, xmlOptions);
        }

        public static CTManualLayout parse(Reader reader) throws XmlException, IOException {
            return (CTManualLayout) POIXMLTypeLoader.parse(reader, CTManualLayout.type, (XmlOptions) null);
        }

        public static CTManualLayout parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTManualLayout) POIXMLTypeLoader.parse(reader, CTManualLayout.type, xmlOptions);
        }

        public static CTManualLayout parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTManualLayout) POIXMLTypeLoader.parse(xMLStreamReader, CTManualLayout.type, (XmlOptions) null);
        }

        public static CTManualLayout parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTManualLayout) POIXMLTypeLoader.parse(xMLStreamReader, CTManualLayout.type, xmlOptions);
        }

        public static CTManualLayout parse(Node node) throws XmlException {
            return (CTManualLayout) POIXMLTypeLoader.parse(node, CTManualLayout.type, (XmlOptions) null);
        }

        public static CTManualLayout parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTManualLayout) POIXMLTypeLoader.parse(node, CTManualLayout.type, xmlOptions);
        }

        public static CTManualLayout parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTManualLayout) POIXMLTypeLoader.parse(xMLInputStream, CTManualLayout.type, (XmlOptions) null);
        }

        public static CTManualLayout parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTManualLayout) POIXMLTypeLoader.parse(xMLInputStream, CTManualLayout.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTManualLayout.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTManualLayout.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTLayoutTarget getLayoutTarget();

    boolean isSetLayoutTarget();

    void setLayoutTarget(CTLayoutTarget cTLayoutTarget);

    CTLayoutTarget addNewLayoutTarget();

    void unsetLayoutTarget();

    CTLayoutMode getXMode();

    boolean isSetXMode();

    void setXMode(CTLayoutMode cTLayoutMode);

    CTLayoutMode addNewXMode();

    void unsetXMode();

    CTLayoutMode getYMode();

    boolean isSetYMode();

    void setYMode(CTLayoutMode cTLayoutMode);

    CTLayoutMode addNewYMode();

    void unsetYMode();

    CTLayoutMode getWMode();

    boolean isSetWMode();

    void setWMode(CTLayoutMode cTLayoutMode);

    CTLayoutMode addNewWMode();

    void unsetWMode();

    CTLayoutMode getHMode();

    boolean isSetHMode();

    void setHMode(CTLayoutMode cTLayoutMode);

    CTLayoutMode addNewHMode();

    void unsetHMode();

    CTDouble getX();

    boolean isSetX();

    void setX(CTDouble cTDouble);

    CTDouble addNewX();

    void unsetX();

    CTDouble getY();

    boolean isSetY();

    void setY(CTDouble cTDouble);

    CTDouble addNewY();

    void unsetY();

    CTDouble getW();

    boolean isSetW();

    void setW(CTDouble cTDouble);

    CTDouble addNewW();

    void unsetW();

    CTDouble getH();

    boolean isSetH();

    void setH(CTDouble cTDouble);

    CTDouble addNewH();

    void unsetH();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
