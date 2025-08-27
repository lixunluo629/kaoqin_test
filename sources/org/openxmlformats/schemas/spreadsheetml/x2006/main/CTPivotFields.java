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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPivotFields.class */
public interface CTPivotFields extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPivotFields.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpivotfields12batype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPivotFields$Factory.class */
    public static final class Factory {
        public static CTPivotFields newInstance() {
            return (CTPivotFields) POIXMLTypeLoader.newInstance(CTPivotFields.type, null);
        }

        public static CTPivotFields newInstance(XmlOptions xmlOptions) {
            return (CTPivotFields) POIXMLTypeLoader.newInstance(CTPivotFields.type, xmlOptions);
        }

        public static CTPivotFields parse(String str) throws XmlException {
            return (CTPivotFields) POIXMLTypeLoader.parse(str, CTPivotFields.type, (XmlOptions) null);
        }

        public static CTPivotFields parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotFields) POIXMLTypeLoader.parse(str, CTPivotFields.type, xmlOptions);
        }

        public static CTPivotFields parse(File file) throws XmlException, IOException {
            return (CTPivotFields) POIXMLTypeLoader.parse(file, CTPivotFields.type, (XmlOptions) null);
        }

        public static CTPivotFields parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotFields) POIXMLTypeLoader.parse(file, CTPivotFields.type, xmlOptions);
        }

        public static CTPivotFields parse(URL url) throws XmlException, IOException {
            return (CTPivotFields) POIXMLTypeLoader.parse(url, CTPivotFields.type, (XmlOptions) null);
        }

        public static CTPivotFields parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotFields) POIXMLTypeLoader.parse(url, CTPivotFields.type, xmlOptions);
        }

        public static CTPivotFields parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPivotFields) POIXMLTypeLoader.parse(inputStream, CTPivotFields.type, (XmlOptions) null);
        }

        public static CTPivotFields parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotFields) POIXMLTypeLoader.parse(inputStream, CTPivotFields.type, xmlOptions);
        }

        public static CTPivotFields parse(Reader reader) throws XmlException, IOException {
            return (CTPivotFields) POIXMLTypeLoader.parse(reader, CTPivotFields.type, (XmlOptions) null);
        }

        public static CTPivotFields parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotFields) POIXMLTypeLoader.parse(reader, CTPivotFields.type, xmlOptions);
        }

        public static CTPivotFields parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPivotFields) POIXMLTypeLoader.parse(xMLStreamReader, CTPivotFields.type, (XmlOptions) null);
        }

        public static CTPivotFields parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotFields) POIXMLTypeLoader.parse(xMLStreamReader, CTPivotFields.type, xmlOptions);
        }

        public static CTPivotFields parse(Node node) throws XmlException {
            return (CTPivotFields) POIXMLTypeLoader.parse(node, CTPivotFields.type, (XmlOptions) null);
        }

        public static CTPivotFields parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotFields) POIXMLTypeLoader.parse(node, CTPivotFields.type, xmlOptions);
        }

        public static CTPivotFields parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPivotFields) POIXMLTypeLoader.parse(xMLInputStream, CTPivotFields.type, (XmlOptions) null);
        }

        public static CTPivotFields parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPivotFields) POIXMLTypeLoader.parse(xMLInputStream, CTPivotFields.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPivotFields.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPivotFields.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTPivotField> getPivotFieldList();

    CTPivotField[] getPivotFieldArray();

    CTPivotField getPivotFieldArray(int i);

    int sizeOfPivotFieldArray();

    void setPivotFieldArray(CTPivotField[] cTPivotFieldArr);

    void setPivotFieldArray(int i, CTPivotField cTPivotField);

    CTPivotField insertNewPivotField(int i);

    CTPivotField addNewPivotField();

    void removePivotField(int i);

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();
}
