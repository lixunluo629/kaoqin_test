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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPageField.class */
public interface CTPageField extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPageField.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpagefield338atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPageField$Factory.class */
    public static final class Factory {
        public static CTPageField newInstance() {
            return (CTPageField) POIXMLTypeLoader.newInstance(CTPageField.type, null);
        }

        public static CTPageField newInstance(XmlOptions xmlOptions) {
            return (CTPageField) POIXMLTypeLoader.newInstance(CTPageField.type, xmlOptions);
        }

        public static CTPageField parse(String str) throws XmlException {
            return (CTPageField) POIXMLTypeLoader.parse(str, CTPageField.type, (XmlOptions) null);
        }

        public static CTPageField parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPageField) POIXMLTypeLoader.parse(str, CTPageField.type, xmlOptions);
        }

        public static CTPageField parse(File file) throws XmlException, IOException {
            return (CTPageField) POIXMLTypeLoader.parse(file, CTPageField.type, (XmlOptions) null);
        }

        public static CTPageField parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageField) POIXMLTypeLoader.parse(file, CTPageField.type, xmlOptions);
        }

        public static CTPageField parse(URL url) throws XmlException, IOException {
            return (CTPageField) POIXMLTypeLoader.parse(url, CTPageField.type, (XmlOptions) null);
        }

        public static CTPageField parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageField) POIXMLTypeLoader.parse(url, CTPageField.type, xmlOptions);
        }

        public static CTPageField parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPageField) POIXMLTypeLoader.parse(inputStream, CTPageField.type, (XmlOptions) null);
        }

        public static CTPageField parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageField) POIXMLTypeLoader.parse(inputStream, CTPageField.type, xmlOptions);
        }

        public static CTPageField parse(Reader reader) throws XmlException, IOException {
            return (CTPageField) POIXMLTypeLoader.parse(reader, CTPageField.type, (XmlOptions) null);
        }

        public static CTPageField parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageField) POIXMLTypeLoader.parse(reader, CTPageField.type, xmlOptions);
        }

        public static CTPageField parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPageField) POIXMLTypeLoader.parse(xMLStreamReader, CTPageField.type, (XmlOptions) null);
        }

        public static CTPageField parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPageField) POIXMLTypeLoader.parse(xMLStreamReader, CTPageField.type, xmlOptions);
        }

        public static CTPageField parse(Node node) throws XmlException {
            return (CTPageField) POIXMLTypeLoader.parse(node, CTPageField.type, (XmlOptions) null);
        }

        public static CTPageField parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPageField) POIXMLTypeLoader.parse(node, CTPageField.type, xmlOptions);
        }

        public static CTPageField parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPageField) POIXMLTypeLoader.parse(xMLInputStream, CTPageField.type, (XmlOptions) null);
        }

        public static CTPageField parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPageField) POIXMLTypeLoader.parse(xMLInputStream, CTPageField.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPageField.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPageField.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    int getFld();

    XmlInt xgetFld();

    void setFld(int i);

    void xsetFld(XmlInt xmlInt);

    long getItem();

    XmlUnsignedInt xgetItem();

    boolean isSetItem();

    void setItem(long j);

    void xsetItem(XmlUnsignedInt xmlUnsignedInt);

    void unsetItem();

    int getHier();

    XmlInt xgetHier();

    boolean isSetHier();

    void setHier(int i);

    void xsetHier(XmlInt xmlInt);

    void unsetHier();

    String getName();

    STXstring xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    void unsetName();

    String getCap();

    STXstring xgetCap();

    boolean isSetCap();

    void setCap(String str);

    void xsetCap(STXstring sTXstring);

    void unsetCap();
}
