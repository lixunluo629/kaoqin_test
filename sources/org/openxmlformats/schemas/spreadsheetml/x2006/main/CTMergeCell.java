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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTMergeCell.class */
public interface CTMergeCell extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTMergeCell.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctmergecelle8d9type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTMergeCell$Factory.class */
    public static final class Factory {
        public static CTMergeCell newInstance() {
            return (CTMergeCell) POIXMLTypeLoader.newInstance(CTMergeCell.type, null);
        }

        public static CTMergeCell newInstance(XmlOptions xmlOptions) {
            return (CTMergeCell) POIXMLTypeLoader.newInstance(CTMergeCell.type, xmlOptions);
        }

        public static CTMergeCell parse(String str) throws XmlException {
            return (CTMergeCell) POIXMLTypeLoader.parse(str, CTMergeCell.type, (XmlOptions) null);
        }

        public static CTMergeCell parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTMergeCell) POIXMLTypeLoader.parse(str, CTMergeCell.type, xmlOptions);
        }

        public static CTMergeCell parse(File file) throws XmlException, IOException {
            return (CTMergeCell) POIXMLTypeLoader.parse(file, CTMergeCell.type, (XmlOptions) null);
        }

        public static CTMergeCell parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMergeCell) POIXMLTypeLoader.parse(file, CTMergeCell.type, xmlOptions);
        }

        public static CTMergeCell parse(URL url) throws XmlException, IOException {
            return (CTMergeCell) POIXMLTypeLoader.parse(url, CTMergeCell.type, (XmlOptions) null);
        }

        public static CTMergeCell parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMergeCell) POIXMLTypeLoader.parse(url, CTMergeCell.type, xmlOptions);
        }

        public static CTMergeCell parse(InputStream inputStream) throws XmlException, IOException {
            return (CTMergeCell) POIXMLTypeLoader.parse(inputStream, CTMergeCell.type, (XmlOptions) null);
        }

        public static CTMergeCell parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMergeCell) POIXMLTypeLoader.parse(inputStream, CTMergeCell.type, xmlOptions);
        }

        public static CTMergeCell parse(Reader reader) throws XmlException, IOException {
            return (CTMergeCell) POIXMLTypeLoader.parse(reader, CTMergeCell.type, (XmlOptions) null);
        }

        public static CTMergeCell parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMergeCell) POIXMLTypeLoader.parse(reader, CTMergeCell.type, xmlOptions);
        }

        public static CTMergeCell parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTMergeCell) POIXMLTypeLoader.parse(xMLStreamReader, CTMergeCell.type, (XmlOptions) null);
        }

        public static CTMergeCell parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTMergeCell) POIXMLTypeLoader.parse(xMLStreamReader, CTMergeCell.type, xmlOptions);
        }

        public static CTMergeCell parse(Node node) throws XmlException {
            return (CTMergeCell) POIXMLTypeLoader.parse(node, CTMergeCell.type, (XmlOptions) null);
        }

        public static CTMergeCell parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTMergeCell) POIXMLTypeLoader.parse(node, CTMergeCell.type, xmlOptions);
        }

        public static CTMergeCell parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTMergeCell) POIXMLTypeLoader.parse(xMLInputStream, CTMergeCell.type, (XmlOptions) null);
        }

        public static CTMergeCell parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTMergeCell) POIXMLTypeLoader.parse(xMLInputStream, CTMergeCell.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTMergeCell.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTMergeCell.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getRef();

    STRef xgetRef();

    void setRef(String str);

    void xsetRef(STRef sTRef);
}
