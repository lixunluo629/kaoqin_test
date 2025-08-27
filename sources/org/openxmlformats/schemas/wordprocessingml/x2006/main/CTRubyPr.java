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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTRubyPr.class */
public interface CTRubyPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTRubyPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctrubyprb2actype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTRubyPr$Factory.class */
    public static final class Factory {
        public static CTRubyPr newInstance() {
            return (CTRubyPr) POIXMLTypeLoader.newInstance(CTRubyPr.type, null);
        }

        public static CTRubyPr newInstance(XmlOptions xmlOptions) {
            return (CTRubyPr) POIXMLTypeLoader.newInstance(CTRubyPr.type, xmlOptions);
        }

        public static CTRubyPr parse(String str) throws XmlException {
            return (CTRubyPr) POIXMLTypeLoader.parse(str, CTRubyPr.type, (XmlOptions) null);
        }

        public static CTRubyPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTRubyPr) POIXMLTypeLoader.parse(str, CTRubyPr.type, xmlOptions);
        }

        public static CTRubyPr parse(File file) throws XmlException, IOException {
            return (CTRubyPr) POIXMLTypeLoader.parse(file, CTRubyPr.type, (XmlOptions) null);
        }

        public static CTRubyPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRubyPr) POIXMLTypeLoader.parse(file, CTRubyPr.type, xmlOptions);
        }

        public static CTRubyPr parse(URL url) throws XmlException, IOException {
            return (CTRubyPr) POIXMLTypeLoader.parse(url, CTRubyPr.type, (XmlOptions) null);
        }

        public static CTRubyPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRubyPr) POIXMLTypeLoader.parse(url, CTRubyPr.type, xmlOptions);
        }

        public static CTRubyPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTRubyPr) POIXMLTypeLoader.parse(inputStream, CTRubyPr.type, (XmlOptions) null);
        }

        public static CTRubyPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRubyPr) POIXMLTypeLoader.parse(inputStream, CTRubyPr.type, xmlOptions);
        }

        public static CTRubyPr parse(Reader reader) throws XmlException, IOException {
            return (CTRubyPr) POIXMLTypeLoader.parse(reader, CTRubyPr.type, (XmlOptions) null);
        }

        public static CTRubyPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRubyPr) POIXMLTypeLoader.parse(reader, CTRubyPr.type, xmlOptions);
        }

        public static CTRubyPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTRubyPr) POIXMLTypeLoader.parse(xMLStreamReader, CTRubyPr.type, (XmlOptions) null);
        }

        public static CTRubyPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTRubyPr) POIXMLTypeLoader.parse(xMLStreamReader, CTRubyPr.type, xmlOptions);
        }

        public static CTRubyPr parse(Node node) throws XmlException {
            return (CTRubyPr) POIXMLTypeLoader.parse(node, CTRubyPr.type, (XmlOptions) null);
        }

        public static CTRubyPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTRubyPr) POIXMLTypeLoader.parse(node, CTRubyPr.type, xmlOptions);
        }

        public static CTRubyPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTRubyPr) POIXMLTypeLoader.parse(xMLInputStream, CTRubyPr.type, (XmlOptions) null);
        }

        public static CTRubyPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTRubyPr) POIXMLTypeLoader.parse(xMLInputStream, CTRubyPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRubyPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRubyPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTRubyAlign getRubyAlign();

    void setRubyAlign(CTRubyAlign cTRubyAlign);

    CTRubyAlign addNewRubyAlign();

    CTHpsMeasure getHps();

    void setHps(CTHpsMeasure cTHpsMeasure);

    CTHpsMeasure addNewHps();

    CTHpsMeasure getHpsRaise();

    void setHpsRaise(CTHpsMeasure cTHpsMeasure);

    CTHpsMeasure addNewHpsRaise();

    CTHpsMeasure getHpsBaseText();

    void setHpsBaseText(CTHpsMeasure cTHpsMeasure);

    CTHpsMeasure addNewHpsBaseText();

    CTLang getLid();

    void setLid(CTLang cTLang);

    CTLang addNewLid();

    CTOnOff getDirty();

    boolean isSetDirty();

    void setDirty(CTOnOff cTOnOff);

    CTOnOff addNewDirty();

    void unsetDirty();
}
