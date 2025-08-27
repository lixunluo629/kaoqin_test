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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTHyperlink.class */
public interface CTHyperlink extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTHyperlink.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cthyperlink4457type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTHyperlink$Factory.class */
    public static final class Factory {
        public static CTHyperlink newInstance() {
            return (CTHyperlink) POIXMLTypeLoader.newInstance(CTHyperlink.type, null);
        }

        public static CTHyperlink newInstance(XmlOptions xmlOptions) {
            return (CTHyperlink) POIXMLTypeLoader.newInstance(CTHyperlink.type, xmlOptions);
        }

        public static CTHyperlink parse(String str) throws XmlException {
            return (CTHyperlink) POIXMLTypeLoader.parse(str, CTHyperlink.type, (XmlOptions) null);
        }

        public static CTHyperlink parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTHyperlink) POIXMLTypeLoader.parse(str, CTHyperlink.type, xmlOptions);
        }

        public static CTHyperlink parse(File file) throws XmlException, IOException {
            return (CTHyperlink) POIXMLTypeLoader.parse(file, CTHyperlink.type, (XmlOptions) null);
        }

        public static CTHyperlink parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHyperlink) POIXMLTypeLoader.parse(file, CTHyperlink.type, xmlOptions);
        }

        public static CTHyperlink parse(URL url) throws XmlException, IOException {
            return (CTHyperlink) POIXMLTypeLoader.parse(url, CTHyperlink.type, (XmlOptions) null);
        }

        public static CTHyperlink parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHyperlink) POIXMLTypeLoader.parse(url, CTHyperlink.type, xmlOptions);
        }

        public static CTHyperlink parse(InputStream inputStream) throws XmlException, IOException {
            return (CTHyperlink) POIXMLTypeLoader.parse(inputStream, CTHyperlink.type, (XmlOptions) null);
        }

        public static CTHyperlink parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHyperlink) POIXMLTypeLoader.parse(inputStream, CTHyperlink.type, xmlOptions);
        }

        public static CTHyperlink parse(Reader reader) throws XmlException, IOException {
            return (CTHyperlink) POIXMLTypeLoader.parse(reader, CTHyperlink.type, (XmlOptions) null);
        }

        public static CTHyperlink parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHyperlink) POIXMLTypeLoader.parse(reader, CTHyperlink.type, xmlOptions);
        }

        public static CTHyperlink parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTHyperlink) POIXMLTypeLoader.parse(xMLStreamReader, CTHyperlink.type, (XmlOptions) null);
        }

        public static CTHyperlink parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTHyperlink) POIXMLTypeLoader.parse(xMLStreamReader, CTHyperlink.type, xmlOptions);
        }

        public static CTHyperlink parse(Node node) throws XmlException {
            return (CTHyperlink) POIXMLTypeLoader.parse(node, CTHyperlink.type, (XmlOptions) null);
        }

        public static CTHyperlink parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTHyperlink) POIXMLTypeLoader.parse(node, CTHyperlink.type, xmlOptions);
        }

        public static CTHyperlink parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTHyperlink) POIXMLTypeLoader.parse(xMLInputStream, CTHyperlink.type, (XmlOptions) null);
        }

        public static CTHyperlink parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTHyperlink) POIXMLTypeLoader.parse(xMLInputStream, CTHyperlink.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTHyperlink.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTHyperlink.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTEmbeddedWAVAudioFile getSnd();

    boolean isSetSnd();

    void setSnd(CTEmbeddedWAVAudioFile cTEmbeddedWAVAudioFile);

    CTEmbeddedWAVAudioFile addNewSnd();

    void unsetSnd();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    String getId();

    STRelationshipId xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(STRelationshipId sTRelationshipId);

    void unsetId();

    String getInvalidUrl();

    XmlString xgetInvalidUrl();

    boolean isSetInvalidUrl();

    void setInvalidUrl(String str);

    void xsetInvalidUrl(XmlString xmlString);

    void unsetInvalidUrl();

    String getAction();

    XmlString xgetAction();

    boolean isSetAction();

    void setAction(String str);

    void xsetAction(XmlString xmlString);

    void unsetAction();

    String getTgtFrame();

    XmlString xgetTgtFrame();

    boolean isSetTgtFrame();

    void setTgtFrame(String str);

    void xsetTgtFrame(XmlString xmlString);

    void unsetTgtFrame();

    String getTooltip();

    XmlString xgetTooltip();

    boolean isSetTooltip();

    void setTooltip(String str);

    void xsetTooltip(XmlString xmlString);

    void unsetTooltip();

    boolean getHistory();

    XmlBoolean xgetHistory();

    boolean isSetHistory();

    void setHistory(boolean z);

    void xsetHistory(XmlBoolean xmlBoolean);

    void unsetHistory();

    boolean getHighlightClick();

    XmlBoolean xgetHighlightClick();

    boolean isSetHighlightClick();

    void setHighlightClick(boolean z);

    void xsetHighlightClick(XmlBoolean xmlBoolean);

    void unsetHighlightClick();

    boolean getEndSnd();

    XmlBoolean xgetEndSnd();

    boolean isSetEndSnd();

    void setEndSnd(boolean z);

    void xsetEndSnd(XmlBoolean xmlBoolean);

    void unsetEndSnd();
}
