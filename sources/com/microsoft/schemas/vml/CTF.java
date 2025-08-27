package com.microsoft.schemas.vml;

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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTF.class */
public interface CTF extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTF.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctfbc3atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTF$Factory.class */
    public static final class Factory {
        public static CTF newInstance() {
            return (CTF) POIXMLTypeLoader.newInstance(CTF.type, null);
        }

        public static CTF newInstance(XmlOptions xmlOptions) {
            return (CTF) POIXMLTypeLoader.newInstance(CTF.type, xmlOptions);
        }

        public static CTF parse(String str) throws XmlException {
            return (CTF) POIXMLTypeLoader.parse(str, CTF.type, (XmlOptions) null);
        }

        public static CTF parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTF) POIXMLTypeLoader.parse(str, CTF.type, xmlOptions);
        }

        public static CTF parse(File file) throws XmlException, IOException {
            return (CTF) POIXMLTypeLoader.parse(file, CTF.type, (XmlOptions) null);
        }

        public static CTF parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTF) POIXMLTypeLoader.parse(file, CTF.type, xmlOptions);
        }

        public static CTF parse(URL url) throws XmlException, IOException {
            return (CTF) POIXMLTypeLoader.parse(url, CTF.type, (XmlOptions) null);
        }

        public static CTF parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTF) POIXMLTypeLoader.parse(url, CTF.type, xmlOptions);
        }

        public static CTF parse(InputStream inputStream) throws XmlException, IOException {
            return (CTF) POIXMLTypeLoader.parse(inputStream, CTF.type, (XmlOptions) null);
        }

        public static CTF parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTF) POIXMLTypeLoader.parse(inputStream, CTF.type, xmlOptions);
        }

        public static CTF parse(Reader reader) throws XmlException, IOException {
            return (CTF) POIXMLTypeLoader.parse(reader, CTF.type, (XmlOptions) null);
        }

        public static CTF parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTF) POIXMLTypeLoader.parse(reader, CTF.type, xmlOptions);
        }

        public static CTF parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTF) POIXMLTypeLoader.parse(xMLStreamReader, CTF.type, (XmlOptions) null);
        }

        public static CTF parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTF) POIXMLTypeLoader.parse(xMLStreamReader, CTF.type, xmlOptions);
        }

        public static CTF parse(Node node) throws XmlException {
            return (CTF) POIXMLTypeLoader.parse(node, CTF.type, (XmlOptions) null);
        }

        public static CTF parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTF) POIXMLTypeLoader.parse(node, CTF.type, xmlOptions);
        }

        public static CTF parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTF) POIXMLTypeLoader.parse(xMLInputStream, CTF.type, (XmlOptions) null);
        }

        public static CTF parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTF) POIXMLTypeLoader.parse(xMLInputStream, CTF.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTF.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTF.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getEqn();

    XmlString xgetEqn();

    boolean isSetEqn();

    void setEqn(String str);

    void xsetEqn(XmlString xmlString);

    void unsetEqn();
}
