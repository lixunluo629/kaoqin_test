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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTPicture.class */
public interface CTPicture extends CTPictureBase {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPicture.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpicture1054type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTPicture$Factory.class */
    public static final class Factory {
        public static CTPicture newInstance() {
            return (CTPicture) POIXMLTypeLoader.newInstance(CTPicture.type, null);
        }

        public static CTPicture newInstance(XmlOptions xmlOptions) {
            return (CTPicture) POIXMLTypeLoader.newInstance(CTPicture.type, xmlOptions);
        }

        public static CTPicture parse(String str) throws XmlException {
            return (CTPicture) POIXMLTypeLoader.parse(str, CTPicture.type, (XmlOptions) null);
        }

        public static CTPicture parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPicture) POIXMLTypeLoader.parse(str, CTPicture.type, xmlOptions);
        }

        public static CTPicture parse(File file) throws XmlException, IOException {
            return (CTPicture) POIXMLTypeLoader.parse(file, CTPicture.type, (XmlOptions) null);
        }

        public static CTPicture parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPicture) POIXMLTypeLoader.parse(file, CTPicture.type, xmlOptions);
        }

        public static CTPicture parse(URL url) throws XmlException, IOException {
            return (CTPicture) POIXMLTypeLoader.parse(url, CTPicture.type, (XmlOptions) null);
        }

        public static CTPicture parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPicture) POIXMLTypeLoader.parse(url, CTPicture.type, xmlOptions);
        }

        public static CTPicture parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPicture) POIXMLTypeLoader.parse(inputStream, CTPicture.type, (XmlOptions) null);
        }

        public static CTPicture parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPicture) POIXMLTypeLoader.parse(inputStream, CTPicture.type, xmlOptions);
        }

        public static CTPicture parse(Reader reader) throws XmlException, IOException {
            return (CTPicture) POIXMLTypeLoader.parse(reader, CTPicture.type, (XmlOptions) null);
        }

        public static CTPicture parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPicture) POIXMLTypeLoader.parse(reader, CTPicture.type, xmlOptions);
        }

        public static CTPicture parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPicture) POIXMLTypeLoader.parse(xMLStreamReader, CTPicture.type, (XmlOptions) null);
        }

        public static CTPicture parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPicture) POIXMLTypeLoader.parse(xMLStreamReader, CTPicture.type, xmlOptions);
        }

        public static CTPicture parse(Node node) throws XmlException {
            return (CTPicture) POIXMLTypeLoader.parse(node, CTPicture.type, (XmlOptions) null);
        }

        public static CTPicture parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPicture) POIXMLTypeLoader.parse(node, CTPicture.type, xmlOptions);
        }

        public static CTPicture parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPicture) POIXMLTypeLoader.parse(xMLInputStream, CTPicture.type, (XmlOptions) null);
        }

        public static CTPicture parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPicture) POIXMLTypeLoader.parse(xMLInputStream, CTPicture.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPicture.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPicture.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTRel getMovie();

    boolean isSetMovie();

    void setMovie(CTRel cTRel);

    CTRel addNewMovie();

    void unsetMovie();

    CTControl getControl();

    boolean isSetControl();

    void setControl(CTControl cTControl);

    CTControl addNewControl();

    void unsetControl();
}
