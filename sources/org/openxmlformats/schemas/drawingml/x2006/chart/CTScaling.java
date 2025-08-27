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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTScaling.class */
public interface CTScaling extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTScaling.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctscaling1dfftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTScaling$Factory.class */
    public static final class Factory {
        public static CTScaling newInstance() {
            return (CTScaling) POIXMLTypeLoader.newInstance(CTScaling.type, null);
        }

        public static CTScaling newInstance(XmlOptions xmlOptions) {
            return (CTScaling) POIXMLTypeLoader.newInstance(CTScaling.type, xmlOptions);
        }

        public static CTScaling parse(String str) throws XmlException {
            return (CTScaling) POIXMLTypeLoader.parse(str, CTScaling.type, (XmlOptions) null);
        }

        public static CTScaling parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTScaling) POIXMLTypeLoader.parse(str, CTScaling.type, xmlOptions);
        }

        public static CTScaling parse(File file) throws XmlException, IOException {
            return (CTScaling) POIXMLTypeLoader.parse(file, CTScaling.type, (XmlOptions) null);
        }

        public static CTScaling parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScaling) POIXMLTypeLoader.parse(file, CTScaling.type, xmlOptions);
        }

        public static CTScaling parse(URL url) throws XmlException, IOException {
            return (CTScaling) POIXMLTypeLoader.parse(url, CTScaling.type, (XmlOptions) null);
        }

        public static CTScaling parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScaling) POIXMLTypeLoader.parse(url, CTScaling.type, xmlOptions);
        }

        public static CTScaling parse(InputStream inputStream) throws XmlException, IOException {
            return (CTScaling) POIXMLTypeLoader.parse(inputStream, CTScaling.type, (XmlOptions) null);
        }

        public static CTScaling parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScaling) POIXMLTypeLoader.parse(inputStream, CTScaling.type, xmlOptions);
        }

        public static CTScaling parse(Reader reader) throws XmlException, IOException {
            return (CTScaling) POIXMLTypeLoader.parse(reader, CTScaling.type, (XmlOptions) null);
        }

        public static CTScaling parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTScaling) POIXMLTypeLoader.parse(reader, CTScaling.type, xmlOptions);
        }

        public static CTScaling parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTScaling) POIXMLTypeLoader.parse(xMLStreamReader, CTScaling.type, (XmlOptions) null);
        }

        public static CTScaling parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTScaling) POIXMLTypeLoader.parse(xMLStreamReader, CTScaling.type, xmlOptions);
        }

        public static CTScaling parse(Node node) throws XmlException {
            return (CTScaling) POIXMLTypeLoader.parse(node, CTScaling.type, (XmlOptions) null);
        }

        public static CTScaling parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTScaling) POIXMLTypeLoader.parse(node, CTScaling.type, xmlOptions);
        }

        public static CTScaling parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTScaling) POIXMLTypeLoader.parse(xMLInputStream, CTScaling.type, (XmlOptions) null);
        }

        public static CTScaling parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTScaling) POIXMLTypeLoader.parse(xMLInputStream, CTScaling.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTScaling.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTScaling.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTLogBase getLogBase();

    boolean isSetLogBase();

    void setLogBase(CTLogBase cTLogBase);

    CTLogBase addNewLogBase();

    void unsetLogBase();

    CTOrientation getOrientation();

    boolean isSetOrientation();

    void setOrientation(CTOrientation cTOrientation);

    CTOrientation addNewOrientation();

    void unsetOrientation();

    CTDouble getMax();

    boolean isSetMax();

    void setMax(CTDouble cTDouble);

    CTDouble addNewMax();

    void unsetMax();

    CTDouble getMin();

    boolean isSetMin();

    void setMin(CTDouble cTDouble);

    CTDouble addNewMin();

    void unsetMin();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
