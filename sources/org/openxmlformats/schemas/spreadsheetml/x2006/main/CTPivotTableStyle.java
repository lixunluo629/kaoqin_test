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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPivotTableStyle.class */
public interface CTPivotTableStyle extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPivotTableStyle.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpivottablestyle0f84type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPivotTableStyle$Factory.class */
    public static final class Factory {
        public static CTPivotTableStyle newInstance() {
            return (CTPivotTableStyle) POIXMLTypeLoader.newInstance(CTPivotTableStyle.type, null);
        }

        public static CTPivotTableStyle newInstance(XmlOptions xmlOptions) {
            return (CTPivotTableStyle) POIXMLTypeLoader.newInstance(CTPivotTableStyle.type, xmlOptions);
        }

        public static CTPivotTableStyle parse(String str) throws XmlException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(str, CTPivotTableStyle.type, (XmlOptions) null);
        }

        public static CTPivotTableStyle parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(str, CTPivotTableStyle.type, xmlOptions);
        }

        public static CTPivotTableStyle parse(File file) throws XmlException, IOException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(file, CTPivotTableStyle.type, (XmlOptions) null);
        }

        public static CTPivotTableStyle parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(file, CTPivotTableStyle.type, xmlOptions);
        }

        public static CTPivotTableStyle parse(URL url) throws XmlException, IOException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(url, CTPivotTableStyle.type, (XmlOptions) null);
        }

        public static CTPivotTableStyle parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(url, CTPivotTableStyle.type, xmlOptions);
        }

        public static CTPivotTableStyle parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(inputStream, CTPivotTableStyle.type, (XmlOptions) null);
        }

        public static CTPivotTableStyle parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(inputStream, CTPivotTableStyle.type, xmlOptions);
        }

        public static CTPivotTableStyle parse(Reader reader) throws XmlException, IOException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(reader, CTPivotTableStyle.type, (XmlOptions) null);
        }

        public static CTPivotTableStyle parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(reader, CTPivotTableStyle.type, xmlOptions);
        }

        public static CTPivotTableStyle parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(xMLStreamReader, CTPivotTableStyle.type, (XmlOptions) null);
        }

        public static CTPivotTableStyle parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(xMLStreamReader, CTPivotTableStyle.type, xmlOptions);
        }

        public static CTPivotTableStyle parse(Node node) throws XmlException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(node, CTPivotTableStyle.type, (XmlOptions) null);
        }

        public static CTPivotTableStyle parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(node, CTPivotTableStyle.type, xmlOptions);
        }

        public static CTPivotTableStyle parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(xMLInputStream, CTPivotTableStyle.type, (XmlOptions) null);
        }

        public static CTPivotTableStyle parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPivotTableStyle) POIXMLTypeLoader.parse(xMLInputStream, CTPivotTableStyle.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPivotTableStyle.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPivotTableStyle.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getName();

    XmlString xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(XmlString xmlString);

    void unsetName();

    boolean getShowRowHeaders();

    XmlBoolean xgetShowRowHeaders();

    boolean isSetShowRowHeaders();

    void setShowRowHeaders(boolean z);

    void xsetShowRowHeaders(XmlBoolean xmlBoolean);

    void unsetShowRowHeaders();

    boolean getShowColHeaders();

    XmlBoolean xgetShowColHeaders();

    boolean isSetShowColHeaders();

    void setShowColHeaders(boolean z);

    void xsetShowColHeaders(XmlBoolean xmlBoolean);

    void unsetShowColHeaders();

    boolean getShowRowStripes();

    XmlBoolean xgetShowRowStripes();

    boolean isSetShowRowStripes();

    void setShowRowStripes(boolean z);

    void xsetShowRowStripes(XmlBoolean xmlBoolean);

    void unsetShowRowStripes();

    boolean getShowColStripes();

    XmlBoolean xgetShowColStripes();

    boolean isSetShowColStripes();

    void setShowColStripes(boolean z);

    void xsetShowColStripes(XmlBoolean xmlBoolean);

    void unsetShowColStripes();

    boolean getShowLastColumn();

    XmlBoolean xgetShowLastColumn();

    boolean isSetShowLastColumn();

    void setShowLastColumn(boolean z);

    void xsetShowLastColumn(XmlBoolean xmlBoolean);

    void unsetShowLastColumn();
}
