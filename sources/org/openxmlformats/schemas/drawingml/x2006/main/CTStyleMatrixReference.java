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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTStyleMatrixReference.class */
public interface CTStyleMatrixReference extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTStyleMatrixReference.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctstylematrixreference6ef4type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTStyleMatrixReference$Factory.class */
    public static final class Factory {
        public static CTStyleMatrixReference newInstance() {
            return (CTStyleMatrixReference) POIXMLTypeLoader.newInstance(CTStyleMatrixReference.type, null);
        }

        public static CTStyleMatrixReference newInstance(XmlOptions xmlOptions) {
            return (CTStyleMatrixReference) POIXMLTypeLoader.newInstance(CTStyleMatrixReference.type, xmlOptions);
        }

        public static CTStyleMatrixReference parse(String str) throws XmlException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(str, CTStyleMatrixReference.type, (XmlOptions) null);
        }

        public static CTStyleMatrixReference parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(str, CTStyleMatrixReference.type, xmlOptions);
        }

        public static CTStyleMatrixReference parse(File file) throws XmlException, IOException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(file, CTStyleMatrixReference.type, (XmlOptions) null);
        }

        public static CTStyleMatrixReference parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(file, CTStyleMatrixReference.type, xmlOptions);
        }

        public static CTStyleMatrixReference parse(URL url) throws XmlException, IOException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(url, CTStyleMatrixReference.type, (XmlOptions) null);
        }

        public static CTStyleMatrixReference parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(url, CTStyleMatrixReference.type, xmlOptions);
        }

        public static CTStyleMatrixReference parse(InputStream inputStream) throws XmlException, IOException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(inputStream, CTStyleMatrixReference.type, (XmlOptions) null);
        }

        public static CTStyleMatrixReference parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(inputStream, CTStyleMatrixReference.type, xmlOptions);
        }

        public static CTStyleMatrixReference parse(Reader reader) throws XmlException, IOException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(reader, CTStyleMatrixReference.type, (XmlOptions) null);
        }

        public static CTStyleMatrixReference parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(reader, CTStyleMatrixReference.type, xmlOptions);
        }

        public static CTStyleMatrixReference parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(xMLStreamReader, CTStyleMatrixReference.type, (XmlOptions) null);
        }

        public static CTStyleMatrixReference parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(xMLStreamReader, CTStyleMatrixReference.type, xmlOptions);
        }

        public static CTStyleMatrixReference parse(Node node) throws XmlException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(node, CTStyleMatrixReference.type, (XmlOptions) null);
        }

        public static CTStyleMatrixReference parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(node, CTStyleMatrixReference.type, xmlOptions);
        }

        public static CTStyleMatrixReference parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(xMLInputStream, CTStyleMatrixReference.type, (XmlOptions) null);
        }

        public static CTStyleMatrixReference parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTStyleMatrixReference) POIXMLTypeLoader.parse(xMLInputStream, CTStyleMatrixReference.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStyleMatrixReference.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStyleMatrixReference.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTScRgbColor getScrgbClr();

    boolean isSetScrgbClr();

    void setScrgbClr(CTScRgbColor cTScRgbColor);

    CTScRgbColor addNewScrgbClr();

    void unsetScrgbClr();

    CTSRgbColor getSrgbClr();

    boolean isSetSrgbClr();

    void setSrgbClr(CTSRgbColor cTSRgbColor);

    CTSRgbColor addNewSrgbClr();

    void unsetSrgbClr();

    CTHslColor getHslClr();

    boolean isSetHslClr();

    void setHslClr(CTHslColor cTHslColor);

    CTHslColor addNewHslClr();

    void unsetHslClr();

    CTSystemColor getSysClr();

    boolean isSetSysClr();

    void setSysClr(CTSystemColor cTSystemColor);

    CTSystemColor addNewSysClr();

    void unsetSysClr();

    CTSchemeColor getSchemeClr();

    boolean isSetSchemeClr();

    void setSchemeClr(CTSchemeColor cTSchemeColor);

    CTSchemeColor addNewSchemeClr();

    void unsetSchemeClr();

    CTPresetColor getPrstClr();

    boolean isSetPrstClr();

    void setPrstClr(CTPresetColor cTPresetColor);

    CTPresetColor addNewPrstClr();

    void unsetPrstClr();

    long getIdx();

    STStyleMatrixColumnIndex xgetIdx();

    void setIdx(long j);

    void xsetIdx(STStyleMatrixColumnIndex sTStyleMatrixColumnIndex);
}
