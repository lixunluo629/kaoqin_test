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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTLinearShadeProperties.class */
public interface CTLinearShadeProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLinearShadeProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlinearshadeproperties7f0ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTLinearShadeProperties$Factory.class */
    public static final class Factory {
        public static CTLinearShadeProperties newInstance() {
            return (CTLinearShadeProperties) POIXMLTypeLoader.newInstance(CTLinearShadeProperties.type, null);
        }

        public static CTLinearShadeProperties newInstance(XmlOptions xmlOptions) {
            return (CTLinearShadeProperties) POIXMLTypeLoader.newInstance(CTLinearShadeProperties.type, xmlOptions);
        }

        public static CTLinearShadeProperties parse(String str) throws XmlException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(str, CTLinearShadeProperties.type, (XmlOptions) null);
        }

        public static CTLinearShadeProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(str, CTLinearShadeProperties.type, xmlOptions);
        }

        public static CTLinearShadeProperties parse(File file) throws XmlException, IOException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(file, CTLinearShadeProperties.type, (XmlOptions) null);
        }

        public static CTLinearShadeProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(file, CTLinearShadeProperties.type, xmlOptions);
        }

        public static CTLinearShadeProperties parse(URL url) throws XmlException, IOException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(url, CTLinearShadeProperties.type, (XmlOptions) null);
        }

        public static CTLinearShadeProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(url, CTLinearShadeProperties.type, xmlOptions);
        }

        public static CTLinearShadeProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(inputStream, CTLinearShadeProperties.type, (XmlOptions) null);
        }

        public static CTLinearShadeProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(inputStream, CTLinearShadeProperties.type, xmlOptions);
        }

        public static CTLinearShadeProperties parse(Reader reader) throws XmlException, IOException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(reader, CTLinearShadeProperties.type, (XmlOptions) null);
        }

        public static CTLinearShadeProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(reader, CTLinearShadeProperties.type, xmlOptions);
        }

        public static CTLinearShadeProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTLinearShadeProperties.type, (XmlOptions) null);
        }

        public static CTLinearShadeProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTLinearShadeProperties.type, xmlOptions);
        }

        public static CTLinearShadeProperties parse(Node node) throws XmlException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(node, CTLinearShadeProperties.type, (XmlOptions) null);
        }

        public static CTLinearShadeProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(node, CTLinearShadeProperties.type, xmlOptions);
        }

        public static CTLinearShadeProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(xMLInputStream, CTLinearShadeProperties.type, (XmlOptions) null);
        }

        public static CTLinearShadeProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLinearShadeProperties) POIXMLTypeLoader.parse(xMLInputStream, CTLinearShadeProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLinearShadeProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLinearShadeProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getAng();

    STPositiveFixedAngle xgetAng();

    boolean isSetAng();

    void setAng(int i);

    void xsetAng(STPositiveFixedAngle sTPositiveFixedAngle);

    void unsetAng();

    boolean getScaled();

    XmlBoolean xgetScaled();

    boolean isSetScaled();

    void setScaled(boolean z);

    void xsetScaled(XmlBoolean xmlBoolean);

    void unsetScaled();
}
