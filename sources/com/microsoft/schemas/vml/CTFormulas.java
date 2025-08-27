package com.microsoft.schemas.vml;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTFormulas.class */
public interface CTFormulas extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTFormulas.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctformulas808btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTFormulas$Factory.class */
    public static final class Factory {
        public static CTFormulas newInstance() {
            return (CTFormulas) POIXMLTypeLoader.newInstance(CTFormulas.type, null);
        }

        public static CTFormulas newInstance(XmlOptions xmlOptions) {
            return (CTFormulas) POIXMLTypeLoader.newInstance(CTFormulas.type, xmlOptions);
        }

        public static CTFormulas parse(String str) throws XmlException {
            return (CTFormulas) POIXMLTypeLoader.parse(str, CTFormulas.type, (XmlOptions) null);
        }

        public static CTFormulas parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTFormulas) POIXMLTypeLoader.parse(str, CTFormulas.type, xmlOptions);
        }

        public static CTFormulas parse(File file) throws XmlException, IOException {
            return (CTFormulas) POIXMLTypeLoader.parse(file, CTFormulas.type, (XmlOptions) null);
        }

        public static CTFormulas parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFormulas) POIXMLTypeLoader.parse(file, CTFormulas.type, xmlOptions);
        }

        public static CTFormulas parse(URL url) throws XmlException, IOException {
            return (CTFormulas) POIXMLTypeLoader.parse(url, CTFormulas.type, (XmlOptions) null);
        }

        public static CTFormulas parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFormulas) POIXMLTypeLoader.parse(url, CTFormulas.type, xmlOptions);
        }

        public static CTFormulas parse(InputStream inputStream) throws XmlException, IOException {
            return (CTFormulas) POIXMLTypeLoader.parse(inputStream, CTFormulas.type, (XmlOptions) null);
        }

        public static CTFormulas parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFormulas) POIXMLTypeLoader.parse(inputStream, CTFormulas.type, xmlOptions);
        }

        public static CTFormulas parse(Reader reader) throws XmlException, IOException {
            return (CTFormulas) POIXMLTypeLoader.parse(reader, CTFormulas.type, (XmlOptions) null);
        }

        public static CTFormulas parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFormulas) POIXMLTypeLoader.parse(reader, CTFormulas.type, xmlOptions);
        }

        public static CTFormulas parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTFormulas) POIXMLTypeLoader.parse(xMLStreamReader, CTFormulas.type, (XmlOptions) null);
        }

        public static CTFormulas parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTFormulas) POIXMLTypeLoader.parse(xMLStreamReader, CTFormulas.type, xmlOptions);
        }

        public static CTFormulas parse(Node node) throws XmlException {
            return (CTFormulas) POIXMLTypeLoader.parse(node, CTFormulas.type, (XmlOptions) null);
        }

        public static CTFormulas parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTFormulas) POIXMLTypeLoader.parse(node, CTFormulas.type, xmlOptions);
        }

        public static CTFormulas parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTFormulas) POIXMLTypeLoader.parse(xMLInputStream, CTFormulas.type, (XmlOptions) null);
        }

        public static CTFormulas parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTFormulas) POIXMLTypeLoader.parse(xMLInputStream, CTFormulas.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFormulas.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFormulas.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTF> getFList();

    CTF[] getFArray();

    CTF getFArray(int i);

    int sizeOfFArray();

    void setFArray(CTF[] ctfArr);

    void setFArray(int i, CTF ctf);

    CTF insertNewF(int i);

    CTF addNewF();

    void removeF(int i);
}
