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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTExternalDefinedName.class */
public interface CTExternalDefinedName extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTExternalDefinedName.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctexternaldefinedname9408type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTExternalDefinedName$Factory.class */
    public static final class Factory {
        public static CTExternalDefinedName newInstance() {
            return (CTExternalDefinedName) POIXMLTypeLoader.newInstance(CTExternalDefinedName.type, null);
        }

        public static CTExternalDefinedName newInstance(XmlOptions xmlOptions) {
            return (CTExternalDefinedName) POIXMLTypeLoader.newInstance(CTExternalDefinedName.type, xmlOptions);
        }

        public static CTExternalDefinedName parse(String str) throws XmlException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(str, CTExternalDefinedName.type, (XmlOptions) null);
        }

        public static CTExternalDefinedName parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(str, CTExternalDefinedName.type, xmlOptions);
        }

        public static CTExternalDefinedName parse(File file) throws XmlException, IOException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(file, CTExternalDefinedName.type, (XmlOptions) null);
        }

        public static CTExternalDefinedName parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(file, CTExternalDefinedName.type, xmlOptions);
        }

        public static CTExternalDefinedName parse(URL url) throws XmlException, IOException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(url, CTExternalDefinedName.type, (XmlOptions) null);
        }

        public static CTExternalDefinedName parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(url, CTExternalDefinedName.type, xmlOptions);
        }

        public static CTExternalDefinedName parse(InputStream inputStream) throws XmlException, IOException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(inputStream, CTExternalDefinedName.type, (XmlOptions) null);
        }

        public static CTExternalDefinedName parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(inputStream, CTExternalDefinedName.type, xmlOptions);
        }

        public static CTExternalDefinedName parse(Reader reader) throws XmlException, IOException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(reader, CTExternalDefinedName.type, (XmlOptions) null);
        }

        public static CTExternalDefinedName parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(reader, CTExternalDefinedName.type, xmlOptions);
        }

        public static CTExternalDefinedName parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(xMLStreamReader, CTExternalDefinedName.type, (XmlOptions) null);
        }

        public static CTExternalDefinedName parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(xMLStreamReader, CTExternalDefinedName.type, xmlOptions);
        }

        public static CTExternalDefinedName parse(Node node) throws XmlException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(node, CTExternalDefinedName.type, (XmlOptions) null);
        }

        public static CTExternalDefinedName parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(node, CTExternalDefinedName.type, xmlOptions);
        }

        public static CTExternalDefinedName parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(xMLInputStream, CTExternalDefinedName.type, (XmlOptions) null);
        }

        public static CTExternalDefinedName parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTExternalDefinedName) POIXMLTypeLoader.parse(xMLInputStream, CTExternalDefinedName.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTExternalDefinedName.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTExternalDefinedName.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getName();

    STXstring xgetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    String getRefersTo();

    STXstring xgetRefersTo();

    boolean isSetRefersTo();

    void setRefersTo(String str);

    void xsetRefersTo(STXstring sTXstring);

    void unsetRefersTo();

    long getSheetId();

    XmlUnsignedInt xgetSheetId();

    boolean isSetSheetId();

    void setSheetId(long j);

    void xsetSheetId(XmlUnsignedInt xmlUnsignedInt);

    void unsetSheetId();
}
