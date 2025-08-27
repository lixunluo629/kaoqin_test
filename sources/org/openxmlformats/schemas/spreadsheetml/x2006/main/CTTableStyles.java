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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTableStyles.class */
public interface CTTableStyles extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTableStyles.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttablestyles872ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTableStyles$Factory.class */
    public static final class Factory {
        public static CTTableStyles newInstance() {
            return (CTTableStyles) POIXMLTypeLoader.newInstance(CTTableStyles.type, null);
        }

        public static CTTableStyles newInstance(XmlOptions xmlOptions) {
            return (CTTableStyles) POIXMLTypeLoader.newInstance(CTTableStyles.type, xmlOptions);
        }

        public static CTTableStyles parse(String str) throws XmlException {
            return (CTTableStyles) POIXMLTypeLoader.parse(str, CTTableStyles.type, (XmlOptions) null);
        }

        public static CTTableStyles parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyles) POIXMLTypeLoader.parse(str, CTTableStyles.type, xmlOptions);
        }

        public static CTTableStyles parse(File file) throws XmlException, IOException {
            return (CTTableStyles) POIXMLTypeLoader.parse(file, CTTableStyles.type, (XmlOptions) null);
        }

        public static CTTableStyles parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyles) POIXMLTypeLoader.parse(file, CTTableStyles.type, xmlOptions);
        }

        public static CTTableStyles parse(URL url) throws XmlException, IOException {
            return (CTTableStyles) POIXMLTypeLoader.parse(url, CTTableStyles.type, (XmlOptions) null);
        }

        public static CTTableStyles parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyles) POIXMLTypeLoader.parse(url, CTTableStyles.type, xmlOptions);
        }

        public static CTTableStyles parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTableStyles) POIXMLTypeLoader.parse(inputStream, CTTableStyles.type, (XmlOptions) null);
        }

        public static CTTableStyles parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyles) POIXMLTypeLoader.parse(inputStream, CTTableStyles.type, xmlOptions);
        }

        public static CTTableStyles parse(Reader reader) throws XmlException, IOException {
            return (CTTableStyles) POIXMLTypeLoader.parse(reader, CTTableStyles.type, (XmlOptions) null);
        }

        public static CTTableStyles parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyles) POIXMLTypeLoader.parse(reader, CTTableStyles.type, xmlOptions);
        }

        public static CTTableStyles parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTableStyles) POIXMLTypeLoader.parse(xMLStreamReader, CTTableStyles.type, (XmlOptions) null);
        }

        public static CTTableStyles parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyles) POIXMLTypeLoader.parse(xMLStreamReader, CTTableStyles.type, xmlOptions);
        }

        public static CTTableStyles parse(Node node) throws XmlException {
            return (CTTableStyles) POIXMLTypeLoader.parse(node, CTTableStyles.type, (XmlOptions) null);
        }

        public static CTTableStyles parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyles) POIXMLTypeLoader.parse(node, CTTableStyles.type, xmlOptions);
        }

        public static CTTableStyles parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTableStyles) POIXMLTypeLoader.parse(xMLInputStream, CTTableStyles.type, (XmlOptions) null);
        }

        public static CTTableStyles parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTableStyles) POIXMLTypeLoader.parse(xMLInputStream, CTTableStyles.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableStyles.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableStyles.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTTableStyle> getTableStyleList();

    CTTableStyle[] getTableStyleArray();

    CTTableStyle getTableStyleArray(int i);

    int sizeOfTableStyleArray();

    void setTableStyleArray(CTTableStyle[] cTTableStyleArr);

    void setTableStyleArray(int i, CTTableStyle cTTableStyle);

    CTTableStyle insertNewTableStyle(int i);

    CTTableStyle addNewTableStyle();

    void removeTableStyle(int i);

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();

    String getDefaultTableStyle();

    XmlString xgetDefaultTableStyle();

    boolean isSetDefaultTableStyle();

    void setDefaultTableStyle(String str);

    void xsetDefaultTableStyle(XmlString xmlString);

    void unsetDefaultTableStyle();

    String getDefaultPivotStyle();

    XmlString xgetDefaultPivotStyle();

    boolean isSetDefaultPivotStyle();

    void setDefaultPivotStyle(String str);

    void xsetDefaultPivotStyle(XmlString xmlString);

    void unsetDefaultPivotStyle();
}
