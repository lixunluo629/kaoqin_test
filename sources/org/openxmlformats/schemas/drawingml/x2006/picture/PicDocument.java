package org.openxmlformats.schemas.drawingml.x2006.picture;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/picture/PicDocument.class */
public interface PicDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(PicDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("pic8010doctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/picture/PicDocument$Factory.class */
    public static final class Factory {
        public static PicDocument newInstance() {
            return (PicDocument) POIXMLTypeLoader.newInstance(PicDocument.type, null);
        }

        public static PicDocument newInstance(XmlOptions xmlOptions) {
            return (PicDocument) POIXMLTypeLoader.newInstance(PicDocument.type, xmlOptions);
        }

        public static PicDocument parse(String str) throws XmlException {
            return (PicDocument) POIXMLTypeLoader.parse(str, PicDocument.type, (XmlOptions) null);
        }

        public static PicDocument parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (PicDocument) POIXMLTypeLoader.parse(str, PicDocument.type, xmlOptions);
        }

        public static PicDocument parse(File file) throws XmlException, IOException {
            return (PicDocument) POIXMLTypeLoader.parse(file, PicDocument.type, (XmlOptions) null);
        }

        public static PicDocument parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PicDocument) POIXMLTypeLoader.parse(file, PicDocument.type, xmlOptions);
        }

        public static PicDocument parse(URL url) throws XmlException, IOException {
            return (PicDocument) POIXMLTypeLoader.parse(url, PicDocument.type, (XmlOptions) null);
        }

        public static PicDocument parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PicDocument) POIXMLTypeLoader.parse(url, PicDocument.type, xmlOptions);
        }

        public static PicDocument parse(InputStream inputStream) throws XmlException, IOException {
            return (PicDocument) POIXMLTypeLoader.parse(inputStream, PicDocument.type, (XmlOptions) null);
        }

        public static PicDocument parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PicDocument) POIXMLTypeLoader.parse(inputStream, PicDocument.type, xmlOptions);
        }

        public static PicDocument parse(Reader reader) throws XmlException, IOException {
            return (PicDocument) POIXMLTypeLoader.parse(reader, PicDocument.type, (XmlOptions) null);
        }

        public static PicDocument parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PicDocument) POIXMLTypeLoader.parse(reader, PicDocument.type, xmlOptions);
        }

        public static PicDocument parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (PicDocument) POIXMLTypeLoader.parse(xMLStreamReader, PicDocument.type, (XmlOptions) null);
        }

        public static PicDocument parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (PicDocument) POIXMLTypeLoader.parse(xMLStreamReader, PicDocument.type, xmlOptions);
        }

        public static PicDocument parse(Node node) throws XmlException {
            return (PicDocument) POIXMLTypeLoader.parse(node, PicDocument.type, (XmlOptions) null);
        }

        public static PicDocument parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (PicDocument) POIXMLTypeLoader.parse(node, PicDocument.type, xmlOptions);
        }

        public static PicDocument parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (PicDocument) POIXMLTypeLoader.parse(xMLInputStream, PicDocument.type, (XmlOptions) null);
        }

        public static PicDocument parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (PicDocument) POIXMLTypeLoader.parse(xMLInputStream, PicDocument.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, PicDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, PicDocument.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPicture getPic();

    void setPic(CTPicture cTPicture);

    CTPicture addNewPic();
}
