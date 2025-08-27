package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCalcChain.class */
public interface CTCalcChain extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCalcChain.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcalcchain5a0btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCalcChain$Factory.class */
    public static final class Factory {
        public static CTCalcChain newInstance() {
            return (CTCalcChain) POIXMLTypeLoader.newInstance(CTCalcChain.type, null);
        }

        public static CTCalcChain newInstance(XmlOptions xmlOptions) {
            return (CTCalcChain) POIXMLTypeLoader.newInstance(CTCalcChain.type, xmlOptions);
        }

        public static CTCalcChain parse(String str) throws XmlException {
            return (CTCalcChain) POIXMLTypeLoader.parse(str, CTCalcChain.type, (XmlOptions) null);
        }

        public static CTCalcChain parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCalcChain) POIXMLTypeLoader.parse(str, CTCalcChain.type, xmlOptions);
        }

        public static CTCalcChain parse(File file) throws XmlException, IOException {
            return (CTCalcChain) POIXMLTypeLoader.parse(file, CTCalcChain.type, (XmlOptions) null);
        }

        public static CTCalcChain parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcChain) POIXMLTypeLoader.parse(file, CTCalcChain.type, xmlOptions);
        }

        public static CTCalcChain parse(URL url) throws XmlException, IOException {
            return (CTCalcChain) POIXMLTypeLoader.parse(url, CTCalcChain.type, (XmlOptions) null);
        }

        public static CTCalcChain parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcChain) POIXMLTypeLoader.parse(url, CTCalcChain.type, xmlOptions);
        }

        public static CTCalcChain parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCalcChain) POIXMLTypeLoader.parse(inputStream, CTCalcChain.type, (XmlOptions) null);
        }

        public static CTCalcChain parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcChain) POIXMLTypeLoader.parse(inputStream, CTCalcChain.type, xmlOptions);
        }

        public static CTCalcChain parse(Reader reader) throws XmlException, IOException {
            return (CTCalcChain) POIXMLTypeLoader.parse(reader, CTCalcChain.type, (XmlOptions) null);
        }

        public static CTCalcChain parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcChain) POIXMLTypeLoader.parse(reader, CTCalcChain.type, xmlOptions);
        }

        public static CTCalcChain parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCalcChain) POIXMLTypeLoader.parse(xMLStreamReader, CTCalcChain.type, (XmlOptions) null);
        }

        public static CTCalcChain parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCalcChain) POIXMLTypeLoader.parse(xMLStreamReader, CTCalcChain.type, xmlOptions);
        }

        public static CTCalcChain parse(Node node) throws XmlException {
            return (CTCalcChain) POIXMLTypeLoader.parse(node, CTCalcChain.type, (XmlOptions) null);
        }

        public static CTCalcChain parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCalcChain) POIXMLTypeLoader.parse(node, CTCalcChain.type, xmlOptions);
        }

        public static CTCalcChain parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCalcChain) POIXMLTypeLoader.parse(xMLInputStream, CTCalcChain.type, (XmlOptions) null);
        }

        public static CTCalcChain parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCalcChain) POIXMLTypeLoader.parse(xMLInputStream, CTCalcChain.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCalcChain.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCalcChain.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTCalcCell> getCList();

    CTCalcCell[] getCArray();

    CTCalcCell getCArray(int i);

    int sizeOfCArray();

    void setCArray(CTCalcCell[] cTCalcCellArr);

    void setCArray(int i, CTCalcCell cTCalcCell);

    CTCalcCell insertNewC(int i);

    CTCalcCell addNewC();

    void removeC(int i);

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
