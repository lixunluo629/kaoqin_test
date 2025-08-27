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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTableStyleInfo.class */
public interface CTTableStyleInfo extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTableStyleInfo.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttablestyleinfo499atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTableStyleInfo$Factory.class */
    public static final class Factory {
        public static CTTableStyleInfo newInstance() {
            return (CTTableStyleInfo) POIXMLTypeLoader.newInstance(CTTableStyleInfo.type, null);
        }

        public static CTTableStyleInfo newInstance(XmlOptions xmlOptions) {
            return (CTTableStyleInfo) POIXMLTypeLoader.newInstance(CTTableStyleInfo.type, xmlOptions);
        }

        public static CTTableStyleInfo parse(String str) throws XmlException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(str, CTTableStyleInfo.type, (XmlOptions) null);
        }

        public static CTTableStyleInfo parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(str, CTTableStyleInfo.type, xmlOptions);
        }

        public static CTTableStyleInfo parse(File file) throws XmlException, IOException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(file, CTTableStyleInfo.type, (XmlOptions) null);
        }

        public static CTTableStyleInfo parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(file, CTTableStyleInfo.type, xmlOptions);
        }

        public static CTTableStyleInfo parse(URL url) throws XmlException, IOException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(url, CTTableStyleInfo.type, (XmlOptions) null);
        }

        public static CTTableStyleInfo parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(url, CTTableStyleInfo.type, xmlOptions);
        }

        public static CTTableStyleInfo parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(inputStream, CTTableStyleInfo.type, (XmlOptions) null);
        }

        public static CTTableStyleInfo parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(inputStream, CTTableStyleInfo.type, xmlOptions);
        }

        public static CTTableStyleInfo parse(Reader reader) throws XmlException, IOException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(reader, CTTableStyleInfo.type, (XmlOptions) null);
        }

        public static CTTableStyleInfo parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(reader, CTTableStyleInfo.type, xmlOptions);
        }

        public static CTTableStyleInfo parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(xMLStreamReader, CTTableStyleInfo.type, (XmlOptions) null);
        }

        public static CTTableStyleInfo parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(xMLStreamReader, CTTableStyleInfo.type, xmlOptions);
        }

        public static CTTableStyleInfo parse(Node node) throws XmlException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(node, CTTableStyleInfo.type, (XmlOptions) null);
        }

        public static CTTableStyleInfo parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(node, CTTableStyleInfo.type, xmlOptions);
        }

        public static CTTableStyleInfo parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(xMLInputStream, CTTableStyleInfo.type, (XmlOptions) null);
        }

        public static CTTableStyleInfo parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTableStyleInfo) POIXMLTypeLoader.parse(xMLInputStream, CTTableStyleInfo.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableStyleInfo.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableStyleInfo.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getName();

    STXstring xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    void unsetName();

    boolean getShowFirstColumn();

    XmlBoolean xgetShowFirstColumn();

    boolean isSetShowFirstColumn();

    void setShowFirstColumn(boolean z);

    void xsetShowFirstColumn(XmlBoolean xmlBoolean);

    void unsetShowFirstColumn();

    boolean getShowLastColumn();

    XmlBoolean xgetShowLastColumn();

    boolean isSetShowLastColumn();

    void setShowLastColumn(boolean z);

    void xsetShowLastColumn(XmlBoolean xmlBoolean);

    void unsetShowLastColumn();

    boolean getShowRowStripes();

    XmlBoolean xgetShowRowStripes();

    boolean isSetShowRowStripes();

    void setShowRowStripes(boolean z);

    void xsetShowRowStripes(XmlBoolean xmlBoolean);

    void unsetShowRowStripes();

    boolean getShowColumnStripes();

    XmlBoolean xgetShowColumnStripes();

    boolean isSetShowColumnStripes();

    void setShowColumnStripes(boolean z);

    void xsetShowColumnStripes(XmlBoolean xmlBoolean);

    void unsetShowColumnStripes();
}
