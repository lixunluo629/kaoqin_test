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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDataBar.class */
public interface CTDataBar extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTDataBar.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctdatabar4128type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDataBar$Factory.class */
    public static final class Factory {
        public static CTDataBar newInstance() {
            return (CTDataBar) POIXMLTypeLoader.newInstance(CTDataBar.type, null);
        }

        public static CTDataBar newInstance(XmlOptions xmlOptions) {
            return (CTDataBar) POIXMLTypeLoader.newInstance(CTDataBar.type, xmlOptions);
        }

        public static CTDataBar parse(String str) throws XmlException {
            return (CTDataBar) POIXMLTypeLoader.parse(str, CTDataBar.type, (XmlOptions) null);
        }

        public static CTDataBar parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTDataBar) POIXMLTypeLoader.parse(str, CTDataBar.type, xmlOptions);
        }

        public static CTDataBar parse(File file) throws XmlException, IOException {
            return (CTDataBar) POIXMLTypeLoader.parse(file, CTDataBar.type, (XmlOptions) null);
        }

        public static CTDataBar parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataBar) POIXMLTypeLoader.parse(file, CTDataBar.type, xmlOptions);
        }

        public static CTDataBar parse(URL url) throws XmlException, IOException {
            return (CTDataBar) POIXMLTypeLoader.parse(url, CTDataBar.type, (XmlOptions) null);
        }

        public static CTDataBar parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataBar) POIXMLTypeLoader.parse(url, CTDataBar.type, xmlOptions);
        }

        public static CTDataBar parse(InputStream inputStream) throws XmlException, IOException {
            return (CTDataBar) POIXMLTypeLoader.parse(inputStream, CTDataBar.type, (XmlOptions) null);
        }

        public static CTDataBar parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataBar) POIXMLTypeLoader.parse(inputStream, CTDataBar.type, xmlOptions);
        }

        public static CTDataBar parse(Reader reader) throws XmlException, IOException {
            return (CTDataBar) POIXMLTypeLoader.parse(reader, CTDataBar.type, (XmlOptions) null);
        }

        public static CTDataBar parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataBar) POIXMLTypeLoader.parse(reader, CTDataBar.type, xmlOptions);
        }

        public static CTDataBar parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTDataBar) POIXMLTypeLoader.parse(xMLStreamReader, CTDataBar.type, (XmlOptions) null);
        }

        public static CTDataBar parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTDataBar) POIXMLTypeLoader.parse(xMLStreamReader, CTDataBar.type, xmlOptions);
        }

        public static CTDataBar parse(Node node) throws XmlException {
            return (CTDataBar) POIXMLTypeLoader.parse(node, CTDataBar.type, (XmlOptions) null);
        }

        public static CTDataBar parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTDataBar) POIXMLTypeLoader.parse(node, CTDataBar.type, xmlOptions);
        }

        public static CTDataBar parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTDataBar) POIXMLTypeLoader.parse(xMLInputStream, CTDataBar.type, (XmlOptions) null);
        }

        public static CTDataBar parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTDataBar) POIXMLTypeLoader.parse(xMLInputStream, CTDataBar.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDataBar.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDataBar.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTCfvo> getCfvoList();

    CTCfvo[] getCfvoArray();

    CTCfvo getCfvoArray(int i);

    int sizeOfCfvoArray();

    void setCfvoArray(CTCfvo[] cTCfvoArr);

    void setCfvoArray(int i, CTCfvo cTCfvo);

    CTCfvo insertNewCfvo(int i);

    CTCfvo addNewCfvo();

    void removeCfvo(int i);

    CTColor getColor();

    void setColor(CTColor cTColor);

    CTColor addNewColor();

    long getMinLength();

    XmlUnsignedInt xgetMinLength();

    boolean isSetMinLength();

    void setMinLength(long j);

    void xsetMinLength(XmlUnsignedInt xmlUnsignedInt);

    void unsetMinLength();

    long getMaxLength();

    XmlUnsignedInt xgetMaxLength();

    boolean isSetMaxLength();

    void setMaxLength(long j);

    void xsetMaxLength(XmlUnsignedInt xmlUnsignedInt);

    void unsetMaxLength();

    boolean getShowValue();

    XmlBoolean xgetShowValue();

    boolean isSetShowValue();

    void setShowValue(boolean z);

    void xsetShowValue(XmlBoolean xmlBoolean);

    void unsetShowValue();
}
