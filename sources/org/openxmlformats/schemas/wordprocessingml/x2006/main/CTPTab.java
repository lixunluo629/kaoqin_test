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
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTPTab.class */
public interface CTPTab extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPTab.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctptaba283type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTPTab$Factory.class */
    public static final class Factory {
        public static CTPTab newInstance() {
            return (CTPTab) POIXMLTypeLoader.newInstance(CTPTab.type, null);
        }

        public static CTPTab newInstance(XmlOptions xmlOptions) {
            return (CTPTab) POIXMLTypeLoader.newInstance(CTPTab.type, xmlOptions);
        }

        public static CTPTab parse(String str) throws XmlException {
            return (CTPTab) POIXMLTypeLoader.parse(str, CTPTab.type, (XmlOptions) null);
        }

        public static CTPTab parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPTab) POIXMLTypeLoader.parse(str, CTPTab.type, xmlOptions);
        }

        public static CTPTab parse(File file) throws XmlException, IOException {
            return (CTPTab) POIXMLTypeLoader.parse(file, CTPTab.type, (XmlOptions) null);
        }

        public static CTPTab parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPTab) POIXMLTypeLoader.parse(file, CTPTab.type, xmlOptions);
        }

        public static CTPTab parse(URL url) throws XmlException, IOException {
            return (CTPTab) POIXMLTypeLoader.parse(url, CTPTab.type, (XmlOptions) null);
        }

        public static CTPTab parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPTab) POIXMLTypeLoader.parse(url, CTPTab.type, xmlOptions);
        }

        public static CTPTab parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPTab) POIXMLTypeLoader.parse(inputStream, CTPTab.type, (XmlOptions) null);
        }

        public static CTPTab parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPTab) POIXMLTypeLoader.parse(inputStream, CTPTab.type, xmlOptions);
        }

        public static CTPTab parse(Reader reader) throws XmlException, IOException {
            return (CTPTab) POIXMLTypeLoader.parse(reader, CTPTab.type, (XmlOptions) null);
        }

        public static CTPTab parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPTab) POIXMLTypeLoader.parse(reader, CTPTab.type, xmlOptions);
        }

        public static CTPTab parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPTab) POIXMLTypeLoader.parse(xMLStreamReader, CTPTab.type, (XmlOptions) null);
        }

        public static CTPTab parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPTab) POIXMLTypeLoader.parse(xMLStreamReader, CTPTab.type, xmlOptions);
        }

        public static CTPTab parse(Node node) throws XmlException {
            return (CTPTab) POIXMLTypeLoader.parse(node, CTPTab.type, (XmlOptions) null);
        }

        public static CTPTab parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPTab) POIXMLTypeLoader.parse(node, CTPTab.type, xmlOptions);
        }

        public static CTPTab parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPTab) POIXMLTypeLoader.parse(xMLInputStream, CTPTab.type, (XmlOptions) null);
        }

        public static CTPTab parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPTab) POIXMLTypeLoader.parse(xMLInputStream, CTPTab.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPTab.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPTab.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STPTabAlignment$Enum getAlignment();

    STPTabAlignment xgetAlignment();

    void setAlignment(STPTabAlignment$Enum sTPTabAlignment$Enum);

    void xsetAlignment(STPTabAlignment sTPTabAlignment);

    STPTabRelativeTo$Enum getRelativeTo();

    STPTabRelativeTo xgetRelativeTo();

    void setRelativeTo(STPTabRelativeTo$Enum sTPTabRelativeTo$Enum);

    void xsetRelativeTo(STPTabRelativeTo sTPTabRelativeTo);

    STPTabLeader$Enum getLeader();

    STPTabLeader xgetLeader();

    void setLeader(STPTabLeader$Enum sTPTabLeader$Enum);

    void xsetLeader(STPTabLeader sTPTabLeader);
}
