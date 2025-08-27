package org.openxmlformats.schemas.officeDocument.x2006.extendedProperties;

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
import org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/extendedProperties/CTVectorVariant.class */
public interface CTVectorVariant extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTVectorVariant.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctvectorvariant9d75type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/extendedProperties/CTVectorVariant$Factory.class */
    public static final class Factory {
        public static CTVectorVariant newInstance() {
            return (CTVectorVariant) POIXMLTypeLoader.newInstance(CTVectorVariant.type, null);
        }

        public static CTVectorVariant newInstance(XmlOptions xmlOptions) {
            return (CTVectorVariant) POIXMLTypeLoader.newInstance(CTVectorVariant.type, xmlOptions);
        }

        public static CTVectorVariant parse(String str) throws XmlException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(str, CTVectorVariant.type, (XmlOptions) null);
        }

        public static CTVectorVariant parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(str, CTVectorVariant.type, xmlOptions);
        }

        public static CTVectorVariant parse(File file) throws XmlException, IOException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(file, CTVectorVariant.type, (XmlOptions) null);
        }

        public static CTVectorVariant parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(file, CTVectorVariant.type, xmlOptions);
        }

        public static CTVectorVariant parse(URL url) throws XmlException, IOException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(url, CTVectorVariant.type, (XmlOptions) null);
        }

        public static CTVectorVariant parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(url, CTVectorVariant.type, xmlOptions);
        }

        public static CTVectorVariant parse(InputStream inputStream) throws XmlException, IOException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(inputStream, CTVectorVariant.type, (XmlOptions) null);
        }

        public static CTVectorVariant parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(inputStream, CTVectorVariant.type, xmlOptions);
        }

        public static CTVectorVariant parse(Reader reader) throws XmlException, IOException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(reader, CTVectorVariant.type, (XmlOptions) null);
        }

        public static CTVectorVariant parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(reader, CTVectorVariant.type, xmlOptions);
        }

        public static CTVectorVariant parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(xMLStreamReader, CTVectorVariant.type, (XmlOptions) null);
        }

        public static CTVectorVariant parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(xMLStreamReader, CTVectorVariant.type, xmlOptions);
        }

        public static CTVectorVariant parse(Node node) throws XmlException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(node, CTVectorVariant.type, (XmlOptions) null);
        }

        public static CTVectorVariant parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(node, CTVectorVariant.type, xmlOptions);
        }

        public static CTVectorVariant parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(xMLInputStream, CTVectorVariant.type, (XmlOptions) null);
        }

        public static CTVectorVariant parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTVectorVariant) POIXMLTypeLoader.parse(xMLInputStream, CTVectorVariant.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTVectorVariant.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTVectorVariant.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTVector getVector();

    void setVector(CTVector cTVector);

    CTVector addNewVector();
}
