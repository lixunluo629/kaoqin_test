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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTBreak.class */
public interface CTBreak extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBreak.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctbreak815etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTBreak$Factory.class */
    public static final class Factory {
        public static CTBreak newInstance() {
            return (CTBreak) POIXMLTypeLoader.newInstance(CTBreak.type, null);
        }

        public static CTBreak newInstance(XmlOptions xmlOptions) {
            return (CTBreak) POIXMLTypeLoader.newInstance(CTBreak.type, xmlOptions);
        }

        public static CTBreak parse(String str) throws XmlException {
            return (CTBreak) POIXMLTypeLoader.parse(str, CTBreak.type, (XmlOptions) null);
        }

        public static CTBreak parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBreak) POIXMLTypeLoader.parse(str, CTBreak.type, xmlOptions);
        }

        public static CTBreak parse(File file) throws XmlException, IOException {
            return (CTBreak) POIXMLTypeLoader.parse(file, CTBreak.type, (XmlOptions) null);
        }

        public static CTBreak parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBreak) POIXMLTypeLoader.parse(file, CTBreak.type, xmlOptions);
        }

        public static CTBreak parse(URL url) throws XmlException, IOException {
            return (CTBreak) POIXMLTypeLoader.parse(url, CTBreak.type, (XmlOptions) null);
        }

        public static CTBreak parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBreak) POIXMLTypeLoader.parse(url, CTBreak.type, xmlOptions);
        }

        public static CTBreak parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBreak) POIXMLTypeLoader.parse(inputStream, CTBreak.type, (XmlOptions) null);
        }

        public static CTBreak parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBreak) POIXMLTypeLoader.parse(inputStream, CTBreak.type, xmlOptions);
        }

        public static CTBreak parse(Reader reader) throws XmlException, IOException {
            return (CTBreak) POIXMLTypeLoader.parse(reader, CTBreak.type, (XmlOptions) null);
        }

        public static CTBreak parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBreak) POIXMLTypeLoader.parse(reader, CTBreak.type, xmlOptions);
        }

        public static CTBreak parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBreak) POIXMLTypeLoader.parse(xMLStreamReader, CTBreak.type, (XmlOptions) null);
        }

        public static CTBreak parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBreak) POIXMLTypeLoader.parse(xMLStreamReader, CTBreak.type, xmlOptions);
        }

        public static CTBreak parse(Node node) throws XmlException {
            return (CTBreak) POIXMLTypeLoader.parse(node, CTBreak.type, (XmlOptions) null);
        }

        public static CTBreak parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBreak) POIXMLTypeLoader.parse(node, CTBreak.type, xmlOptions);
        }

        public static CTBreak parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBreak) POIXMLTypeLoader.parse(xMLInputStream, CTBreak.type, (XmlOptions) null);
        }

        public static CTBreak parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBreak) POIXMLTypeLoader.parse(xMLInputStream, CTBreak.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBreak.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBreak.type, xmlOptions);
        }

        private Factory() {
        }
    }

    long getId();

    XmlUnsignedInt xgetId();

    boolean isSetId();

    void setId(long j);

    void xsetId(XmlUnsignedInt xmlUnsignedInt);

    void unsetId();

    long getMin();

    XmlUnsignedInt xgetMin();

    boolean isSetMin();

    void setMin(long j);

    void xsetMin(XmlUnsignedInt xmlUnsignedInt);

    void unsetMin();

    long getMax();

    XmlUnsignedInt xgetMax();

    boolean isSetMax();

    void setMax(long j);

    void xsetMax(XmlUnsignedInt xmlUnsignedInt);

    void unsetMax();

    boolean getMan();

    XmlBoolean xgetMan();

    boolean isSetMan();

    void setMan(boolean z);

    void xsetMan(XmlBoolean xmlBoolean);

    void unsetMan();

    boolean getPt();

    XmlBoolean xgetPt();

    boolean isSetPt();

    void setPt(boolean z);

    void xsetPt(XmlBoolean xmlBoolean);

    void unsetPt();
}
