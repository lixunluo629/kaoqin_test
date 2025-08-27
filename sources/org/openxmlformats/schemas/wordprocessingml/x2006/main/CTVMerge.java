package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTVMerge.class */
public interface CTVMerge extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTVMerge.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctvmergee086type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTVMerge$Factory.class */
    public static final class Factory {
        public static CTVMerge newInstance() {
            return (CTVMerge) POIXMLTypeLoader.newInstance(CTVMerge.type, null);
        }

        public static CTVMerge newInstance(XmlOptions xmlOptions) {
            return (CTVMerge) POIXMLTypeLoader.newInstance(CTVMerge.type, xmlOptions);
        }

        public static CTVMerge parse(String str) throws XmlException {
            return (CTVMerge) POIXMLTypeLoader.parse(str, CTVMerge.type, (XmlOptions) null);
        }

        public static CTVMerge parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTVMerge) POIXMLTypeLoader.parse(str, CTVMerge.type, xmlOptions);
        }

        public static CTVMerge parse(File file) throws XmlException, IOException {
            return (CTVMerge) POIXMLTypeLoader.parse(file, CTVMerge.type, (XmlOptions) null);
        }

        public static CTVMerge parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVMerge) POIXMLTypeLoader.parse(file, CTVMerge.type, xmlOptions);
        }

        public static CTVMerge parse(URL url) throws XmlException, IOException {
            return (CTVMerge) POIXMLTypeLoader.parse(url, CTVMerge.type, (XmlOptions) null);
        }

        public static CTVMerge parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVMerge) POIXMLTypeLoader.parse(url, CTVMerge.type, xmlOptions);
        }

        public static CTVMerge parse(InputStream inputStream) throws XmlException, IOException {
            return (CTVMerge) POIXMLTypeLoader.parse(inputStream, CTVMerge.type, (XmlOptions) null);
        }

        public static CTVMerge parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVMerge) POIXMLTypeLoader.parse(inputStream, CTVMerge.type, xmlOptions);
        }

        public static CTVMerge parse(Reader reader) throws XmlException, IOException {
            return (CTVMerge) POIXMLTypeLoader.parse(reader, CTVMerge.type, (XmlOptions) null);
        }

        public static CTVMerge parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVMerge) POIXMLTypeLoader.parse(reader, CTVMerge.type, xmlOptions);
        }

        public static CTVMerge parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTVMerge) POIXMLTypeLoader.parse(xMLStreamReader, CTVMerge.type, (XmlOptions) null);
        }

        public static CTVMerge parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTVMerge) POIXMLTypeLoader.parse(xMLStreamReader, CTVMerge.type, xmlOptions);
        }

        public static CTVMerge parse(Node node) throws XmlException {
            return (CTVMerge) POIXMLTypeLoader.parse(node, CTVMerge.type, (XmlOptions) null);
        }

        public static CTVMerge parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTVMerge) POIXMLTypeLoader.parse(node, CTVMerge.type, xmlOptions);
        }

        public static CTVMerge parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTVMerge) POIXMLTypeLoader.parse(xMLInputStream, CTVMerge.type, (XmlOptions) null);
        }

        public static CTVMerge parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTVMerge) POIXMLTypeLoader.parse(xMLInputStream, CTVMerge.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTVMerge.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTVMerge.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STMerge.Enum getVal();

    STMerge xgetVal();

    boolean isSetVal();

    void setVal(STMerge.Enum r1);

    void xsetVal(STMerge sTMerge);

    void unsetVal();
}
