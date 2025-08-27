package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTStyles.class */
public interface CTStyles extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTStyles.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctstyles8506type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTStyles$Factory.class */
    public static final class Factory {
        public static CTStyles newInstance() {
            return (CTStyles) POIXMLTypeLoader.newInstance(CTStyles.type, null);
        }

        public static CTStyles newInstance(XmlOptions xmlOptions) {
            return (CTStyles) POIXMLTypeLoader.newInstance(CTStyles.type, xmlOptions);
        }

        public static CTStyles parse(String str) throws XmlException {
            return (CTStyles) POIXMLTypeLoader.parse(str, CTStyles.type, (XmlOptions) null);
        }

        public static CTStyles parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTStyles) POIXMLTypeLoader.parse(str, CTStyles.type, xmlOptions);
        }

        public static CTStyles parse(File file) throws XmlException, IOException {
            return (CTStyles) POIXMLTypeLoader.parse(file, CTStyles.type, (XmlOptions) null);
        }

        public static CTStyles parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyles) POIXMLTypeLoader.parse(file, CTStyles.type, xmlOptions);
        }

        public static CTStyles parse(URL url) throws XmlException, IOException {
            return (CTStyles) POIXMLTypeLoader.parse(url, CTStyles.type, (XmlOptions) null);
        }

        public static CTStyles parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyles) POIXMLTypeLoader.parse(url, CTStyles.type, xmlOptions);
        }

        public static CTStyles parse(InputStream inputStream) throws XmlException, IOException {
            return (CTStyles) POIXMLTypeLoader.parse(inputStream, CTStyles.type, (XmlOptions) null);
        }

        public static CTStyles parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyles) POIXMLTypeLoader.parse(inputStream, CTStyles.type, xmlOptions);
        }

        public static CTStyles parse(Reader reader) throws XmlException, IOException {
            return (CTStyles) POIXMLTypeLoader.parse(reader, CTStyles.type, (XmlOptions) null);
        }

        public static CTStyles parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyles) POIXMLTypeLoader.parse(reader, CTStyles.type, xmlOptions);
        }

        public static CTStyles parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTStyles) POIXMLTypeLoader.parse(xMLStreamReader, CTStyles.type, (XmlOptions) null);
        }

        public static CTStyles parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTStyles) POIXMLTypeLoader.parse(xMLStreamReader, CTStyles.type, xmlOptions);
        }

        public static CTStyles parse(Node node) throws XmlException {
            return (CTStyles) POIXMLTypeLoader.parse(node, CTStyles.type, (XmlOptions) null);
        }

        public static CTStyles parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTStyles) POIXMLTypeLoader.parse(node, CTStyles.type, xmlOptions);
        }

        public static CTStyles parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTStyles) POIXMLTypeLoader.parse(xMLInputStream, CTStyles.type, (XmlOptions) null);
        }

        public static CTStyles parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTStyles) POIXMLTypeLoader.parse(xMLInputStream, CTStyles.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStyles.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStyles.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTDocDefaults getDocDefaults();

    boolean isSetDocDefaults();

    void setDocDefaults(CTDocDefaults cTDocDefaults);

    CTDocDefaults addNewDocDefaults();

    void unsetDocDefaults();

    CTLatentStyles getLatentStyles();

    boolean isSetLatentStyles();

    void setLatentStyles(CTLatentStyles cTLatentStyles);

    CTLatentStyles addNewLatentStyles();

    void unsetLatentStyles();

    List<CTStyle> getStyleList();

    CTStyle[] getStyleArray();

    CTStyle getStyleArray(int i);

    int sizeOfStyleArray();

    void setStyleArray(CTStyle[] cTStyleArr);

    void setStyleArray(int i, CTStyle cTStyle);

    CTStyle insertNewStyle(int i);

    CTStyle addNewStyle();

    void removeStyle(int i);
}
