package org.openxmlformats.schemas.drawingml.x2006.main;

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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTLineStyleList.class */
public interface CTLineStyleList extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLineStyleList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlinestylelist510ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTLineStyleList$Factory.class */
    public static final class Factory {
        public static CTLineStyleList newInstance() {
            return (CTLineStyleList) POIXMLTypeLoader.newInstance(CTLineStyleList.type, null);
        }

        public static CTLineStyleList newInstance(XmlOptions xmlOptions) {
            return (CTLineStyleList) POIXMLTypeLoader.newInstance(CTLineStyleList.type, xmlOptions);
        }

        public static CTLineStyleList parse(String str) throws XmlException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(str, CTLineStyleList.type, (XmlOptions) null);
        }

        public static CTLineStyleList parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(str, CTLineStyleList.type, xmlOptions);
        }

        public static CTLineStyleList parse(File file) throws XmlException, IOException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(file, CTLineStyleList.type, (XmlOptions) null);
        }

        public static CTLineStyleList parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(file, CTLineStyleList.type, xmlOptions);
        }

        public static CTLineStyleList parse(URL url) throws XmlException, IOException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(url, CTLineStyleList.type, (XmlOptions) null);
        }

        public static CTLineStyleList parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(url, CTLineStyleList.type, xmlOptions);
        }

        public static CTLineStyleList parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(inputStream, CTLineStyleList.type, (XmlOptions) null);
        }

        public static CTLineStyleList parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(inputStream, CTLineStyleList.type, xmlOptions);
        }

        public static CTLineStyleList parse(Reader reader) throws XmlException, IOException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(reader, CTLineStyleList.type, (XmlOptions) null);
        }

        public static CTLineStyleList parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(reader, CTLineStyleList.type, xmlOptions);
        }

        public static CTLineStyleList parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(xMLStreamReader, CTLineStyleList.type, (XmlOptions) null);
        }

        public static CTLineStyleList parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(xMLStreamReader, CTLineStyleList.type, xmlOptions);
        }

        public static CTLineStyleList parse(Node node) throws XmlException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(node, CTLineStyleList.type, (XmlOptions) null);
        }

        public static CTLineStyleList parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(node, CTLineStyleList.type, xmlOptions);
        }

        public static CTLineStyleList parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(xMLInputStream, CTLineStyleList.type, (XmlOptions) null);
        }

        public static CTLineStyleList parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLineStyleList) POIXMLTypeLoader.parse(xMLInputStream, CTLineStyleList.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLineStyleList.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLineStyleList.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTLineProperties> getLnList();

    CTLineProperties[] getLnArray();

    CTLineProperties getLnArray(int i);

    int sizeOfLnArray();

    void setLnArray(CTLineProperties[] cTLinePropertiesArr);

    void setLnArray(int i, CTLineProperties cTLineProperties);

    CTLineProperties insertNewLn(int i);

    CTLineProperties addNewLn();

    void removeLn(int i);
}
