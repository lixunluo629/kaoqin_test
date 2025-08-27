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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTNonVisualPictureProperties.class */
public interface CTNonVisualPictureProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTNonVisualPictureProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctnonvisualpicturepropertiesee3ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTNonVisualPictureProperties$Factory.class */
    public static final class Factory {
        public static CTNonVisualPictureProperties newInstance() {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.newInstance(CTNonVisualPictureProperties.type, null);
        }

        public static CTNonVisualPictureProperties newInstance(XmlOptions xmlOptions) {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.newInstance(CTNonVisualPictureProperties.type, xmlOptions);
        }

        public static CTNonVisualPictureProperties parse(String str) throws XmlException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(str, CTNonVisualPictureProperties.type, (XmlOptions) null);
        }

        public static CTNonVisualPictureProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(str, CTNonVisualPictureProperties.type, xmlOptions);
        }

        public static CTNonVisualPictureProperties parse(File file) throws XmlException, IOException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(file, CTNonVisualPictureProperties.type, (XmlOptions) null);
        }

        public static CTNonVisualPictureProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(file, CTNonVisualPictureProperties.type, xmlOptions);
        }

        public static CTNonVisualPictureProperties parse(URL url) throws XmlException, IOException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(url, CTNonVisualPictureProperties.type, (XmlOptions) null);
        }

        public static CTNonVisualPictureProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(url, CTNonVisualPictureProperties.type, xmlOptions);
        }

        public static CTNonVisualPictureProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(inputStream, CTNonVisualPictureProperties.type, (XmlOptions) null);
        }

        public static CTNonVisualPictureProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(inputStream, CTNonVisualPictureProperties.type, xmlOptions);
        }

        public static CTNonVisualPictureProperties parse(Reader reader) throws XmlException, IOException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(reader, CTNonVisualPictureProperties.type, (XmlOptions) null);
        }

        public static CTNonVisualPictureProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(reader, CTNonVisualPictureProperties.type, xmlOptions);
        }

        public static CTNonVisualPictureProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTNonVisualPictureProperties.type, (XmlOptions) null);
        }

        public static CTNonVisualPictureProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTNonVisualPictureProperties.type, xmlOptions);
        }

        public static CTNonVisualPictureProperties parse(Node node) throws XmlException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(node, CTNonVisualPictureProperties.type, (XmlOptions) null);
        }

        public static CTNonVisualPictureProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(node, CTNonVisualPictureProperties.type, xmlOptions);
        }

        public static CTNonVisualPictureProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(xMLInputStream, CTNonVisualPictureProperties.type, (XmlOptions) null);
        }

        public static CTNonVisualPictureProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTNonVisualPictureProperties) POIXMLTypeLoader.parse(xMLInputStream, CTNonVisualPictureProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNonVisualPictureProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNonVisualPictureProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPictureLocking getPicLocks();

    boolean isSetPicLocks();

    void setPicLocks(CTPictureLocking cTPictureLocking);

    CTPictureLocking addNewPicLocks();

    void unsetPicLocks();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    boolean getPreferRelativeResize();

    XmlBoolean xgetPreferRelativeResize();

    boolean isSetPreferRelativeResize();

    void setPreferRelativeResize(boolean z);

    void xsetPreferRelativeResize(XmlBoolean xmlBoolean);

    void unsetPreferRelativeResize();
}
