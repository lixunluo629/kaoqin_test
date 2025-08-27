package org.openxmlformats.schemas.drawingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTStyleMatrix.class */
public interface CTStyleMatrix extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTStyleMatrix.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctstylematrix1903type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTStyleMatrix$Factory.class */
    public static final class Factory {
        public static CTStyleMatrix newInstance() {
            return (CTStyleMatrix) POIXMLTypeLoader.newInstance(CTStyleMatrix.type, null);
        }

        public static CTStyleMatrix newInstance(XmlOptions xmlOptions) {
            return (CTStyleMatrix) POIXMLTypeLoader.newInstance(CTStyleMatrix.type, xmlOptions);
        }

        public static CTStyleMatrix parse(String str) throws XmlException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(str, CTStyleMatrix.type, (XmlOptions) null);
        }

        public static CTStyleMatrix parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(str, CTStyleMatrix.type, xmlOptions);
        }

        public static CTStyleMatrix parse(File file) throws XmlException, IOException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(file, CTStyleMatrix.type, (XmlOptions) null);
        }

        public static CTStyleMatrix parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(file, CTStyleMatrix.type, xmlOptions);
        }

        public static CTStyleMatrix parse(URL url) throws XmlException, IOException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(url, CTStyleMatrix.type, (XmlOptions) null);
        }

        public static CTStyleMatrix parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(url, CTStyleMatrix.type, xmlOptions);
        }

        public static CTStyleMatrix parse(InputStream inputStream) throws XmlException, IOException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(inputStream, CTStyleMatrix.type, (XmlOptions) null);
        }

        public static CTStyleMatrix parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(inputStream, CTStyleMatrix.type, xmlOptions);
        }

        public static CTStyleMatrix parse(Reader reader) throws XmlException, IOException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(reader, CTStyleMatrix.type, (XmlOptions) null);
        }

        public static CTStyleMatrix parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(reader, CTStyleMatrix.type, xmlOptions);
        }

        public static CTStyleMatrix parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(xMLStreamReader, CTStyleMatrix.type, (XmlOptions) null);
        }

        public static CTStyleMatrix parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(xMLStreamReader, CTStyleMatrix.type, xmlOptions);
        }

        public static CTStyleMatrix parse(Node node) throws XmlException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(node, CTStyleMatrix.type, (XmlOptions) null);
        }

        public static CTStyleMatrix parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(node, CTStyleMatrix.type, xmlOptions);
        }

        public static CTStyleMatrix parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(xMLInputStream, CTStyleMatrix.type, (XmlOptions) null);
        }

        public static CTStyleMatrix parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTStyleMatrix) POIXMLTypeLoader.parse(xMLInputStream, CTStyleMatrix.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStyleMatrix.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStyleMatrix.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTFillStyleList getFillStyleLst();

    void setFillStyleLst(CTFillStyleList cTFillStyleList);

    CTFillStyleList addNewFillStyleLst();

    CTLineStyleList getLnStyleLst();

    void setLnStyleLst(CTLineStyleList cTLineStyleList);

    CTLineStyleList addNewLnStyleLst();

    CTEffectStyleList getEffectStyleLst();

    void setEffectStyleLst(CTEffectStyleList cTEffectStyleList);

    CTEffectStyleList addNewEffectStyleLst();

    CTBackgroundFillStyleList getBgFillStyleLst();

    void setBgFillStyleLst(CTBackgroundFillStyleList cTBackgroundFillStyleList);

    CTBackgroundFillStyleList addNewBgFillStyleLst();

    String getName();

    XmlString xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(XmlString xmlString);

    void unsetName();
}
