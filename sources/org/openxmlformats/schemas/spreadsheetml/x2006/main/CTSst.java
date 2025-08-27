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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSst.class */
public interface CTSst extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSst.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsst44f3type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSst$Factory.class */
    public static final class Factory {
        public static CTSst newInstance() {
            return (CTSst) POIXMLTypeLoader.newInstance(CTSst.type, null);
        }

        public static CTSst newInstance(XmlOptions xmlOptions) {
            return (CTSst) POIXMLTypeLoader.newInstance(CTSst.type, xmlOptions);
        }

        public static CTSst parse(String str) throws XmlException {
            return (CTSst) POIXMLTypeLoader.parse(str, CTSst.type, (XmlOptions) null);
        }

        public static CTSst parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSst) POIXMLTypeLoader.parse(str, CTSst.type, xmlOptions);
        }

        public static CTSst parse(File file) throws XmlException, IOException {
            return (CTSst) POIXMLTypeLoader.parse(file, CTSst.type, (XmlOptions) null);
        }

        public static CTSst parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSst) POIXMLTypeLoader.parse(file, CTSst.type, xmlOptions);
        }

        public static CTSst parse(URL url) throws XmlException, IOException {
            return (CTSst) POIXMLTypeLoader.parse(url, CTSst.type, (XmlOptions) null);
        }

        public static CTSst parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSst) POIXMLTypeLoader.parse(url, CTSst.type, xmlOptions);
        }

        public static CTSst parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSst) POIXMLTypeLoader.parse(inputStream, CTSst.type, (XmlOptions) null);
        }

        public static CTSst parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSst) POIXMLTypeLoader.parse(inputStream, CTSst.type, xmlOptions);
        }

        public static CTSst parse(Reader reader) throws XmlException, IOException {
            return (CTSst) POIXMLTypeLoader.parse(reader, CTSst.type, (XmlOptions) null);
        }

        public static CTSst parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSst) POIXMLTypeLoader.parse(reader, CTSst.type, xmlOptions);
        }

        public static CTSst parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSst) POIXMLTypeLoader.parse(xMLStreamReader, CTSst.type, (XmlOptions) null);
        }

        public static CTSst parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSst) POIXMLTypeLoader.parse(xMLStreamReader, CTSst.type, xmlOptions);
        }

        public static CTSst parse(Node node) throws XmlException {
            return (CTSst) POIXMLTypeLoader.parse(node, CTSst.type, (XmlOptions) null);
        }

        public static CTSst parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSst) POIXMLTypeLoader.parse(node, CTSst.type, xmlOptions);
        }

        public static CTSst parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSst) POIXMLTypeLoader.parse(xMLInputStream, CTSst.type, (XmlOptions) null);
        }

        public static CTSst parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSst) POIXMLTypeLoader.parse(xMLInputStream, CTSst.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSst.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSst.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTRst> getSiList();

    CTRst[] getSiArray();

    CTRst getSiArray(int i);

    int sizeOfSiArray();

    void setSiArray(CTRst[] cTRstArr);

    void setSiArray(int i, CTRst cTRst);

    CTRst insertNewSi(int i);

    CTRst addNewSi();

    void removeSi(int i);

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();

    long getUniqueCount();

    XmlUnsignedInt xgetUniqueCount();

    boolean isSetUniqueCount();

    void setUniqueCount(long j);

    void xsetUniqueCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetUniqueCount();
}
