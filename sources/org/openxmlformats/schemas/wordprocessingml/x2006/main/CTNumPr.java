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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTNumPr.class */
public interface CTNumPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTNumPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctnumpr16aatype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTNumPr$Factory.class */
    public static final class Factory {
        public static CTNumPr newInstance() {
            return (CTNumPr) POIXMLTypeLoader.newInstance(CTNumPr.type, null);
        }

        public static CTNumPr newInstance(XmlOptions xmlOptions) {
            return (CTNumPr) POIXMLTypeLoader.newInstance(CTNumPr.type, xmlOptions);
        }

        public static CTNumPr parse(String str) throws XmlException {
            return (CTNumPr) POIXMLTypeLoader.parse(str, CTNumPr.type, (XmlOptions) null);
        }

        public static CTNumPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTNumPr) POIXMLTypeLoader.parse(str, CTNumPr.type, xmlOptions);
        }

        public static CTNumPr parse(File file) throws XmlException, IOException {
            return (CTNumPr) POIXMLTypeLoader.parse(file, CTNumPr.type, (XmlOptions) null);
        }

        public static CTNumPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumPr) POIXMLTypeLoader.parse(file, CTNumPr.type, xmlOptions);
        }

        public static CTNumPr parse(URL url) throws XmlException, IOException {
            return (CTNumPr) POIXMLTypeLoader.parse(url, CTNumPr.type, (XmlOptions) null);
        }

        public static CTNumPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumPr) POIXMLTypeLoader.parse(url, CTNumPr.type, xmlOptions);
        }

        public static CTNumPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTNumPr) POIXMLTypeLoader.parse(inputStream, CTNumPr.type, (XmlOptions) null);
        }

        public static CTNumPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumPr) POIXMLTypeLoader.parse(inputStream, CTNumPr.type, xmlOptions);
        }

        public static CTNumPr parse(Reader reader) throws XmlException, IOException {
            return (CTNumPr) POIXMLTypeLoader.parse(reader, CTNumPr.type, (XmlOptions) null);
        }

        public static CTNumPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumPr) POIXMLTypeLoader.parse(reader, CTNumPr.type, xmlOptions);
        }

        public static CTNumPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTNumPr) POIXMLTypeLoader.parse(xMLStreamReader, CTNumPr.type, (XmlOptions) null);
        }

        public static CTNumPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTNumPr) POIXMLTypeLoader.parse(xMLStreamReader, CTNumPr.type, xmlOptions);
        }

        public static CTNumPr parse(Node node) throws XmlException {
            return (CTNumPr) POIXMLTypeLoader.parse(node, CTNumPr.type, (XmlOptions) null);
        }

        public static CTNumPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTNumPr) POIXMLTypeLoader.parse(node, CTNumPr.type, xmlOptions);
        }

        public static CTNumPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTNumPr) POIXMLTypeLoader.parse(xMLInputStream, CTNumPr.type, (XmlOptions) null);
        }

        public static CTNumPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTNumPr) POIXMLTypeLoader.parse(xMLInputStream, CTNumPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNumPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNumPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTDecimalNumber getIlvl();

    boolean isSetIlvl();

    void setIlvl(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewIlvl();

    void unsetIlvl();

    CTDecimalNumber getNumId();

    boolean isSetNumId();

    void setNumId(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewNumId();

    void unsetNumId();

    CTTrackChangeNumbering getNumberingChange();

    boolean isSetNumberingChange();

    void setNumberingChange(CTTrackChangeNumbering cTTrackChangeNumbering);

    CTTrackChangeNumbering addNewNumberingChange();

    void unsetNumberingChange();

    CTTrackChange getIns();

    boolean isSetIns();

    void setIns(CTTrackChange cTTrackChange);

    CTTrackChange addNewIns();

    void unsetIns();
}
