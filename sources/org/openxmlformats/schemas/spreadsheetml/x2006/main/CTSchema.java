package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSchema.class */
public interface CTSchema extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSchema.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctschema0e6atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSchema$Factory.class */
    public static final class Factory {
        public static CTSchema newInstance() {
            return (CTSchema) POIXMLTypeLoader.newInstance(CTSchema.type, null);
        }

        public static CTSchema newInstance(XmlOptions xmlOptions) {
            return (CTSchema) POIXMLTypeLoader.newInstance(CTSchema.type, xmlOptions);
        }

        public static CTSchema parse(String str) throws XmlException {
            return (CTSchema) POIXMLTypeLoader.parse(str, CTSchema.type, (XmlOptions) null);
        }

        public static CTSchema parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSchema) POIXMLTypeLoader.parse(str, CTSchema.type, xmlOptions);
        }

        public static CTSchema parse(File file) throws XmlException, IOException {
            return (CTSchema) POIXMLTypeLoader.parse(file, CTSchema.type, (XmlOptions) null);
        }

        public static CTSchema parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSchema) POIXMLTypeLoader.parse(file, CTSchema.type, xmlOptions);
        }

        public static CTSchema parse(URL url) throws XmlException, IOException {
            return (CTSchema) POIXMLTypeLoader.parse(url, CTSchema.type, (XmlOptions) null);
        }

        public static CTSchema parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSchema) POIXMLTypeLoader.parse(url, CTSchema.type, xmlOptions);
        }

        public static CTSchema parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSchema) POIXMLTypeLoader.parse(inputStream, CTSchema.type, (XmlOptions) null);
        }

        public static CTSchema parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSchema) POIXMLTypeLoader.parse(inputStream, CTSchema.type, xmlOptions);
        }

        public static CTSchema parse(Reader reader) throws XmlException, IOException {
            return (CTSchema) POIXMLTypeLoader.parse(reader, CTSchema.type, (XmlOptions) null);
        }

        public static CTSchema parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSchema) POIXMLTypeLoader.parse(reader, CTSchema.type, xmlOptions);
        }

        public static CTSchema parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSchema) POIXMLTypeLoader.parse(xMLStreamReader, CTSchema.type, (XmlOptions) null);
        }

        public static CTSchema parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSchema) POIXMLTypeLoader.parse(xMLStreamReader, CTSchema.type, xmlOptions);
        }

        public static CTSchema parse(Node node) throws XmlException {
            return (CTSchema) POIXMLTypeLoader.parse(node, CTSchema.type, (XmlOptions) null);
        }

        public static CTSchema parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSchema) POIXMLTypeLoader.parse(node, CTSchema.type, xmlOptions);
        }

        public static CTSchema parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSchema) POIXMLTypeLoader.parse(xMLInputStream, CTSchema.type, (XmlOptions) null);
        }

        public static CTSchema parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSchema) POIXMLTypeLoader.parse(xMLInputStream, CTSchema.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSchema.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSchema.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getID();

    XmlString xgetID();

    void setID(String str);

    void xsetID(XmlString xmlString);

    String getSchemaRef();

    XmlString xgetSchemaRef();

    boolean isSetSchemaRef();

    void setSchemaRef(String str);

    void xsetSchemaRef(XmlString xmlString);

    void unsetSchemaRef();

    String getNamespace();

    XmlString xgetNamespace();

    boolean isSetNamespace();

    void setNamespace(String str);

    void xsetNamespace(XmlString xmlString);

    void unsetNamespace();
}
