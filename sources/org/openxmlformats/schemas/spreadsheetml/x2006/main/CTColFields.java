package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTColFields.class */
public interface CTColFields extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTColFields.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcolfields9ab8type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTColFields$Factory.class */
    public static final class Factory {
        public static CTColFields newInstance() {
            return (CTColFields) POIXMLTypeLoader.newInstance(CTColFields.type, null);
        }

        public static CTColFields newInstance(XmlOptions xmlOptions) {
            return (CTColFields) POIXMLTypeLoader.newInstance(CTColFields.type, xmlOptions);
        }

        public static CTColFields parse(String str) throws XmlException {
            return (CTColFields) POIXMLTypeLoader.parse(str, CTColFields.type, (XmlOptions) null);
        }

        public static CTColFields parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTColFields) POIXMLTypeLoader.parse(str, CTColFields.type, xmlOptions);
        }

        public static CTColFields parse(File file) throws XmlException, IOException {
            return (CTColFields) POIXMLTypeLoader.parse(file, CTColFields.type, (XmlOptions) null);
        }

        public static CTColFields parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColFields) POIXMLTypeLoader.parse(file, CTColFields.type, xmlOptions);
        }

        public static CTColFields parse(URL url) throws XmlException, IOException {
            return (CTColFields) POIXMLTypeLoader.parse(url, CTColFields.type, (XmlOptions) null);
        }

        public static CTColFields parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColFields) POIXMLTypeLoader.parse(url, CTColFields.type, xmlOptions);
        }

        public static CTColFields parse(InputStream inputStream) throws XmlException, IOException {
            return (CTColFields) POIXMLTypeLoader.parse(inputStream, CTColFields.type, (XmlOptions) null);
        }

        public static CTColFields parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColFields) POIXMLTypeLoader.parse(inputStream, CTColFields.type, xmlOptions);
        }

        public static CTColFields parse(Reader reader) throws XmlException, IOException {
            return (CTColFields) POIXMLTypeLoader.parse(reader, CTColFields.type, (XmlOptions) null);
        }

        public static CTColFields parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColFields) POIXMLTypeLoader.parse(reader, CTColFields.type, xmlOptions);
        }

        public static CTColFields parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTColFields) POIXMLTypeLoader.parse(xMLStreamReader, CTColFields.type, (XmlOptions) null);
        }

        public static CTColFields parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTColFields) POIXMLTypeLoader.parse(xMLStreamReader, CTColFields.type, xmlOptions);
        }

        public static CTColFields parse(Node node) throws XmlException {
            return (CTColFields) POIXMLTypeLoader.parse(node, CTColFields.type, (XmlOptions) null);
        }

        public static CTColFields parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTColFields) POIXMLTypeLoader.parse(node, CTColFields.type, xmlOptions);
        }

        public static CTColFields parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTColFields) POIXMLTypeLoader.parse(xMLInputStream, CTColFields.type, (XmlOptions) null);
        }

        public static CTColFields parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTColFields) POIXMLTypeLoader.parse(xMLInputStream, CTColFields.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColFields.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColFields.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTField> getFieldList();

    CTField[] getFieldArray();

    CTField getFieldArray(int i);

    int sizeOfFieldArray();

    void setFieldArray(CTField[] cTFieldArr);

    void setFieldArray(int i, CTField cTField);

    CTField insertNewField(int i);

    CTField addNewField();

    void removeField(int i);

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();
}
