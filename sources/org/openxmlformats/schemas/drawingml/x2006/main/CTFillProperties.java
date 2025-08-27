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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTFillProperties.class */
public interface CTFillProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTFillProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctfillproperties2371type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTFillProperties$Factory.class */
    public static final class Factory {
        public static CTFillProperties newInstance() {
            return (CTFillProperties) POIXMLTypeLoader.newInstance(CTFillProperties.type, null);
        }

        public static CTFillProperties newInstance(XmlOptions xmlOptions) {
            return (CTFillProperties) POIXMLTypeLoader.newInstance(CTFillProperties.type, xmlOptions);
        }

        public static CTFillProperties parse(String str) throws XmlException {
            return (CTFillProperties) POIXMLTypeLoader.parse(str, CTFillProperties.type, (XmlOptions) null);
        }

        public static CTFillProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTFillProperties) POIXMLTypeLoader.parse(str, CTFillProperties.type, xmlOptions);
        }

        public static CTFillProperties parse(File file) throws XmlException, IOException {
            return (CTFillProperties) POIXMLTypeLoader.parse(file, CTFillProperties.type, (XmlOptions) null);
        }

        public static CTFillProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFillProperties) POIXMLTypeLoader.parse(file, CTFillProperties.type, xmlOptions);
        }

        public static CTFillProperties parse(URL url) throws XmlException, IOException {
            return (CTFillProperties) POIXMLTypeLoader.parse(url, CTFillProperties.type, (XmlOptions) null);
        }

        public static CTFillProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFillProperties) POIXMLTypeLoader.parse(url, CTFillProperties.type, xmlOptions);
        }

        public static CTFillProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTFillProperties) POIXMLTypeLoader.parse(inputStream, CTFillProperties.type, (XmlOptions) null);
        }

        public static CTFillProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFillProperties) POIXMLTypeLoader.parse(inputStream, CTFillProperties.type, xmlOptions);
        }

        public static CTFillProperties parse(Reader reader) throws XmlException, IOException {
            return (CTFillProperties) POIXMLTypeLoader.parse(reader, CTFillProperties.type, (XmlOptions) null);
        }

        public static CTFillProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFillProperties) POIXMLTypeLoader.parse(reader, CTFillProperties.type, xmlOptions);
        }

        public static CTFillProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTFillProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTFillProperties.type, (XmlOptions) null);
        }

        public static CTFillProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTFillProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTFillProperties.type, xmlOptions);
        }

        public static CTFillProperties parse(Node node) throws XmlException {
            return (CTFillProperties) POIXMLTypeLoader.parse(node, CTFillProperties.type, (XmlOptions) null);
        }

        public static CTFillProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTFillProperties) POIXMLTypeLoader.parse(node, CTFillProperties.type, xmlOptions);
        }

        public static CTFillProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTFillProperties) POIXMLTypeLoader.parse(xMLInputStream, CTFillProperties.type, (XmlOptions) null);
        }

        public static CTFillProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTFillProperties) POIXMLTypeLoader.parse(xMLInputStream, CTFillProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFillProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFillProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTNoFillProperties getNoFill();

    boolean isSetNoFill();

    void setNoFill(CTNoFillProperties cTNoFillProperties);

    CTNoFillProperties addNewNoFill();

    void unsetNoFill();

    CTSolidColorFillProperties getSolidFill();

    boolean isSetSolidFill();

    void setSolidFill(CTSolidColorFillProperties cTSolidColorFillProperties);

    CTSolidColorFillProperties addNewSolidFill();

    void unsetSolidFill();

    CTGradientFillProperties getGradFill();

    boolean isSetGradFill();

    void setGradFill(CTGradientFillProperties cTGradientFillProperties);

    CTGradientFillProperties addNewGradFill();

    void unsetGradFill();

    CTBlipFillProperties getBlipFill();

    boolean isSetBlipFill();

    void setBlipFill(CTBlipFillProperties cTBlipFillProperties);

    CTBlipFillProperties addNewBlipFill();

    void unsetBlipFill();

    CTPatternFillProperties getPattFill();

    boolean isSetPattFill();

    void setPattFill(CTPatternFillProperties cTPatternFillProperties);

    CTPatternFillProperties addNewPattFill();

    void unsetPattFill();

    CTGroupFillProperties getGrpFill();

    boolean isSetGrpFill();

    void setGrpFill(CTGroupFillProperties cTGroupFillProperties);

    CTGroupFillProperties addNewGrpFill();

    void unsetGrpFill();
}
