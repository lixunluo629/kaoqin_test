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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTSdtCell.class */
public interface CTSdtCell extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSdtCell.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsdtcell626dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTSdtCell$Factory.class */
    public static final class Factory {
        public static CTSdtCell newInstance() {
            return (CTSdtCell) POIXMLTypeLoader.newInstance(CTSdtCell.type, null);
        }

        public static CTSdtCell newInstance(XmlOptions xmlOptions) {
            return (CTSdtCell) POIXMLTypeLoader.newInstance(CTSdtCell.type, xmlOptions);
        }

        public static CTSdtCell parse(String str) throws XmlException {
            return (CTSdtCell) POIXMLTypeLoader.parse(str, CTSdtCell.type, (XmlOptions) null);
        }

        public static CTSdtCell parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSdtCell) POIXMLTypeLoader.parse(str, CTSdtCell.type, xmlOptions);
        }

        public static CTSdtCell parse(File file) throws XmlException, IOException {
            return (CTSdtCell) POIXMLTypeLoader.parse(file, CTSdtCell.type, (XmlOptions) null);
        }

        public static CTSdtCell parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSdtCell) POIXMLTypeLoader.parse(file, CTSdtCell.type, xmlOptions);
        }

        public static CTSdtCell parse(URL url) throws XmlException, IOException {
            return (CTSdtCell) POIXMLTypeLoader.parse(url, CTSdtCell.type, (XmlOptions) null);
        }

        public static CTSdtCell parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSdtCell) POIXMLTypeLoader.parse(url, CTSdtCell.type, xmlOptions);
        }

        public static CTSdtCell parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSdtCell) POIXMLTypeLoader.parse(inputStream, CTSdtCell.type, (XmlOptions) null);
        }

        public static CTSdtCell parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSdtCell) POIXMLTypeLoader.parse(inputStream, CTSdtCell.type, xmlOptions);
        }

        public static CTSdtCell parse(Reader reader) throws XmlException, IOException {
            return (CTSdtCell) POIXMLTypeLoader.parse(reader, CTSdtCell.type, (XmlOptions) null);
        }

        public static CTSdtCell parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSdtCell) POIXMLTypeLoader.parse(reader, CTSdtCell.type, xmlOptions);
        }

        public static CTSdtCell parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSdtCell) POIXMLTypeLoader.parse(xMLStreamReader, CTSdtCell.type, (XmlOptions) null);
        }

        public static CTSdtCell parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSdtCell) POIXMLTypeLoader.parse(xMLStreamReader, CTSdtCell.type, xmlOptions);
        }

        public static CTSdtCell parse(Node node) throws XmlException {
            return (CTSdtCell) POIXMLTypeLoader.parse(node, CTSdtCell.type, (XmlOptions) null);
        }

        public static CTSdtCell parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSdtCell) POIXMLTypeLoader.parse(node, CTSdtCell.type, xmlOptions);
        }

        public static CTSdtCell parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSdtCell) POIXMLTypeLoader.parse(xMLInputStream, CTSdtCell.type, (XmlOptions) null);
        }

        public static CTSdtCell parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSdtCell) POIXMLTypeLoader.parse(xMLInputStream, CTSdtCell.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSdtCell.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSdtCell.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTSdtPr getSdtPr();

    boolean isSetSdtPr();

    void setSdtPr(CTSdtPr cTSdtPr);

    CTSdtPr addNewSdtPr();

    void unsetSdtPr();

    CTSdtEndPr getSdtEndPr();

    boolean isSetSdtEndPr();

    void setSdtEndPr(CTSdtEndPr cTSdtEndPr);

    CTSdtEndPr addNewSdtEndPr();

    void unsetSdtEndPr();

    CTSdtContentCell getSdtContent();

    boolean isSetSdtContent();

    void setSdtContent(CTSdtContentCell cTSdtContentCell);

    CTSdtContentCell addNewSdtContent();

    void unsetSdtContent();
}
