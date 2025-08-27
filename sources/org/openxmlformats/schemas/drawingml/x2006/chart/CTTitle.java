package org.openxmlformats.schemas.drawingml.x2006.chart;

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
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTTitle.class */
public interface CTTitle extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTitle.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttitleb54etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/CTTitle$Factory.class */
    public static final class Factory {
        public static CTTitle newInstance() {
            return (CTTitle) POIXMLTypeLoader.newInstance(CTTitle.type, null);
        }

        public static CTTitle newInstance(XmlOptions xmlOptions) {
            return (CTTitle) POIXMLTypeLoader.newInstance(CTTitle.type, xmlOptions);
        }

        public static CTTitle parse(String str) throws XmlException {
            return (CTTitle) POIXMLTypeLoader.parse(str, CTTitle.type, (XmlOptions) null);
        }

        public static CTTitle parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTitle) POIXMLTypeLoader.parse(str, CTTitle.type, xmlOptions);
        }

        public static CTTitle parse(File file) throws XmlException, IOException {
            return (CTTitle) POIXMLTypeLoader.parse(file, CTTitle.type, (XmlOptions) null);
        }

        public static CTTitle parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTitle) POIXMLTypeLoader.parse(file, CTTitle.type, xmlOptions);
        }

        public static CTTitle parse(URL url) throws XmlException, IOException {
            return (CTTitle) POIXMLTypeLoader.parse(url, CTTitle.type, (XmlOptions) null);
        }

        public static CTTitle parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTitle) POIXMLTypeLoader.parse(url, CTTitle.type, xmlOptions);
        }

        public static CTTitle parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTitle) POIXMLTypeLoader.parse(inputStream, CTTitle.type, (XmlOptions) null);
        }

        public static CTTitle parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTitle) POIXMLTypeLoader.parse(inputStream, CTTitle.type, xmlOptions);
        }

        public static CTTitle parse(Reader reader) throws XmlException, IOException {
            return (CTTitle) POIXMLTypeLoader.parse(reader, CTTitle.type, (XmlOptions) null);
        }

        public static CTTitle parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTitle) POIXMLTypeLoader.parse(reader, CTTitle.type, xmlOptions);
        }

        public static CTTitle parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTitle) POIXMLTypeLoader.parse(xMLStreamReader, CTTitle.type, (XmlOptions) null);
        }

        public static CTTitle parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTitle) POIXMLTypeLoader.parse(xMLStreamReader, CTTitle.type, xmlOptions);
        }

        public static CTTitle parse(Node node) throws XmlException {
            return (CTTitle) POIXMLTypeLoader.parse(node, CTTitle.type, (XmlOptions) null);
        }

        public static CTTitle parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTitle) POIXMLTypeLoader.parse(node, CTTitle.type, xmlOptions);
        }

        public static CTTitle parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTitle) POIXMLTypeLoader.parse(xMLInputStream, CTTitle.type, (XmlOptions) null);
        }

        public static CTTitle parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTitle) POIXMLTypeLoader.parse(xMLInputStream, CTTitle.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTitle.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTitle.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTx getTx();

    boolean isSetTx();

    void setTx(CTTx cTTx);

    CTTx addNewTx();

    void unsetTx();

    CTLayout getLayout();

    boolean isSetLayout();

    void setLayout(CTLayout cTLayout);

    CTLayout addNewLayout();

    void unsetLayout();

    CTBoolean getOverlay();

    boolean isSetOverlay();

    void setOverlay(CTBoolean cTBoolean);

    CTBoolean addNewOverlay();

    void unsetOverlay();

    CTShapeProperties getSpPr();

    boolean isSetSpPr();

    void setSpPr(CTShapeProperties cTShapeProperties);

    CTShapeProperties addNewSpPr();

    void unsetSpPr();

    CTTextBody getTxPr();

    boolean isSetTxPr();

    void setTxPr(CTTextBody cTTextBody);

    CTTextBody addNewTxPr();

    void unsetTxPr();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
