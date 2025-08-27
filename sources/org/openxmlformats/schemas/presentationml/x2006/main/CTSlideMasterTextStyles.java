package org.openxmlformats.schemas.presentationml.x2006.main;

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
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideMasterTextStyles.class */
public interface CTSlideMasterTextStyles extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSlideMasterTextStyles.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctslidemastertextstylesb48dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideMasterTextStyles$Factory.class */
    public static final class Factory {
        public static CTSlideMasterTextStyles newInstance() {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.newInstance(CTSlideMasterTextStyles.type, null);
        }

        public static CTSlideMasterTextStyles newInstance(XmlOptions xmlOptions) {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.newInstance(CTSlideMasterTextStyles.type, xmlOptions);
        }

        public static CTSlideMasterTextStyles parse(String str) throws XmlException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(str, CTSlideMasterTextStyles.type, (XmlOptions) null);
        }

        public static CTSlideMasterTextStyles parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(str, CTSlideMasterTextStyles.type, xmlOptions);
        }

        public static CTSlideMasterTextStyles parse(File file) throws XmlException, IOException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(file, CTSlideMasterTextStyles.type, (XmlOptions) null);
        }

        public static CTSlideMasterTextStyles parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(file, CTSlideMasterTextStyles.type, xmlOptions);
        }

        public static CTSlideMasterTextStyles parse(URL url) throws XmlException, IOException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(url, CTSlideMasterTextStyles.type, (XmlOptions) null);
        }

        public static CTSlideMasterTextStyles parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(url, CTSlideMasterTextStyles.type, xmlOptions);
        }

        public static CTSlideMasterTextStyles parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(inputStream, CTSlideMasterTextStyles.type, (XmlOptions) null);
        }

        public static CTSlideMasterTextStyles parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(inputStream, CTSlideMasterTextStyles.type, xmlOptions);
        }

        public static CTSlideMasterTextStyles parse(Reader reader) throws XmlException, IOException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(reader, CTSlideMasterTextStyles.type, (XmlOptions) null);
        }

        public static CTSlideMasterTextStyles parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(reader, CTSlideMasterTextStyles.type, xmlOptions);
        }

        public static CTSlideMasterTextStyles parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideMasterTextStyles.type, (XmlOptions) null);
        }

        public static CTSlideMasterTextStyles parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideMasterTextStyles.type, xmlOptions);
        }

        public static CTSlideMasterTextStyles parse(Node node) throws XmlException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(node, CTSlideMasterTextStyles.type, (XmlOptions) null);
        }

        public static CTSlideMasterTextStyles parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(node, CTSlideMasterTextStyles.type, xmlOptions);
        }

        public static CTSlideMasterTextStyles parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(xMLInputStream, CTSlideMasterTextStyles.type, (XmlOptions) null);
        }

        public static CTSlideMasterTextStyles parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSlideMasterTextStyles) POIXMLTypeLoader.parse(xMLInputStream, CTSlideMasterTextStyles.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideMasterTextStyles.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideMasterTextStyles.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTextListStyle getTitleStyle();

    boolean isSetTitleStyle();

    void setTitleStyle(CTTextListStyle cTTextListStyle);

    CTTextListStyle addNewTitleStyle();

    void unsetTitleStyle();

    CTTextListStyle getBodyStyle();

    boolean isSetBodyStyle();

    void setBodyStyle(CTTextListStyle cTTextListStyle);

    CTTextListStyle addNewBodyStyle();

    void unsetBodyStyle();

    CTTextListStyle getOtherStyle();

    boolean isSetOtherStyle();

    void setOtherStyle(CTTextListStyle cTTextListStyle);

    CTTextListStyle addNewOtherStyle();

    void unsetOtherStyle();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
