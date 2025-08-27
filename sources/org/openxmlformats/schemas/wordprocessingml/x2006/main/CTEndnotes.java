package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTEndnotes.class */
public interface CTEndnotes extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTEndnotes.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctendnotescee2type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTEndnotes$Factory.class */
    public static final class Factory {
        public static CTEndnotes newInstance() {
            return (CTEndnotes) POIXMLTypeLoader.newInstance(CTEndnotes.type, null);
        }

        public static CTEndnotes newInstance(XmlOptions xmlOptions) {
            return (CTEndnotes) POIXMLTypeLoader.newInstance(CTEndnotes.type, xmlOptions);
        }

        public static CTEndnotes parse(String str) throws XmlException {
            return (CTEndnotes) POIXMLTypeLoader.parse(str, CTEndnotes.type, (XmlOptions) null);
        }

        public static CTEndnotes parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTEndnotes) POIXMLTypeLoader.parse(str, CTEndnotes.type, xmlOptions);
        }

        public static CTEndnotes parse(File file) throws XmlException, IOException {
            return (CTEndnotes) POIXMLTypeLoader.parse(file, CTEndnotes.type, (XmlOptions) null);
        }

        public static CTEndnotes parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEndnotes) POIXMLTypeLoader.parse(file, CTEndnotes.type, xmlOptions);
        }

        public static CTEndnotes parse(URL url) throws XmlException, IOException {
            return (CTEndnotes) POIXMLTypeLoader.parse(url, CTEndnotes.type, (XmlOptions) null);
        }

        public static CTEndnotes parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEndnotes) POIXMLTypeLoader.parse(url, CTEndnotes.type, xmlOptions);
        }

        public static CTEndnotes parse(InputStream inputStream) throws XmlException, IOException {
            return (CTEndnotes) POIXMLTypeLoader.parse(inputStream, CTEndnotes.type, (XmlOptions) null);
        }

        public static CTEndnotes parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEndnotes) POIXMLTypeLoader.parse(inputStream, CTEndnotes.type, xmlOptions);
        }

        public static CTEndnotes parse(Reader reader) throws XmlException, IOException {
            return (CTEndnotes) POIXMLTypeLoader.parse(reader, CTEndnotes.type, (XmlOptions) null);
        }

        public static CTEndnotes parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEndnotes) POIXMLTypeLoader.parse(reader, CTEndnotes.type, xmlOptions);
        }

        public static CTEndnotes parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTEndnotes) POIXMLTypeLoader.parse(xMLStreamReader, CTEndnotes.type, (XmlOptions) null);
        }

        public static CTEndnotes parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTEndnotes) POIXMLTypeLoader.parse(xMLStreamReader, CTEndnotes.type, xmlOptions);
        }

        public static CTEndnotes parse(Node node) throws XmlException {
            return (CTEndnotes) POIXMLTypeLoader.parse(node, CTEndnotes.type, (XmlOptions) null);
        }

        public static CTEndnotes parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTEndnotes) POIXMLTypeLoader.parse(node, CTEndnotes.type, xmlOptions);
        }

        public static CTEndnotes parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTEndnotes) POIXMLTypeLoader.parse(xMLInputStream, CTEndnotes.type, (XmlOptions) null);
        }

        public static CTEndnotes parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTEndnotes) POIXMLTypeLoader.parse(xMLInputStream, CTEndnotes.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTEndnotes.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTEndnotes.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTFtnEdn> getEndnoteList();

    CTFtnEdn[] getEndnoteArray();

    CTFtnEdn getEndnoteArray(int i);

    int sizeOfEndnoteArray();

    void setEndnoteArray(CTFtnEdn[] cTFtnEdnArr);

    void setEndnoteArray(int i, CTFtnEdn cTFtnEdn);

    CTFtnEdn insertNewEndnote(int i);

    CTFtnEdn addNewEndnote();

    void removeEndnote(int i);
}
