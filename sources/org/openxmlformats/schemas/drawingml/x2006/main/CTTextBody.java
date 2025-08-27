package org.openxmlformats.schemas.drawingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextBody.class */
public interface CTTextBody extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextBody.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextbodya3catype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextBody$Factory.class */
    public static final class Factory {
        public static CTTextBody newInstance() {
            return (CTTextBody) POIXMLTypeLoader.newInstance(CTTextBody.type, null);
        }

        public static CTTextBody newInstance(XmlOptions xmlOptions) {
            return (CTTextBody) POIXMLTypeLoader.newInstance(CTTextBody.type, xmlOptions);
        }

        public static CTTextBody parse(String str) throws XmlException {
            return (CTTextBody) POIXMLTypeLoader.parse(str, CTTextBody.type, (XmlOptions) null);
        }

        public static CTTextBody parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextBody) POIXMLTypeLoader.parse(str, CTTextBody.type, xmlOptions);
        }

        public static CTTextBody parse(File file) throws XmlException, IOException {
            return (CTTextBody) POIXMLTypeLoader.parse(file, CTTextBody.type, (XmlOptions) null);
        }

        public static CTTextBody parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextBody) POIXMLTypeLoader.parse(file, CTTextBody.type, xmlOptions);
        }

        public static CTTextBody parse(URL url) throws XmlException, IOException {
            return (CTTextBody) POIXMLTypeLoader.parse(url, CTTextBody.type, (XmlOptions) null);
        }

        public static CTTextBody parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextBody) POIXMLTypeLoader.parse(url, CTTextBody.type, xmlOptions);
        }

        public static CTTextBody parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextBody) POIXMLTypeLoader.parse(inputStream, CTTextBody.type, (XmlOptions) null);
        }

        public static CTTextBody parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextBody) POIXMLTypeLoader.parse(inputStream, CTTextBody.type, xmlOptions);
        }

        public static CTTextBody parse(Reader reader) throws XmlException, IOException {
            return (CTTextBody) POIXMLTypeLoader.parse(reader, CTTextBody.type, (XmlOptions) null);
        }

        public static CTTextBody parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextBody) POIXMLTypeLoader.parse(reader, CTTextBody.type, xmlOptions);
        }

        public static CTTextBody parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextBody) POIXMLTypeLoader.parse(xMLStreamReader, CTTextBody.type, (XmlOptions) null);
        }

        public static CTTextBody parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextBody) POIXMLTypeLoader.parse(xMLStreamReader, CTTextBody.type, xmlOptions);
        }

        public static CTTextBody parse(Node node) throws XmlException {
            return (CTTextBody) POIXMLTypeLoader.parse(node, CTTextBody.type, (XmlOptions) null);
        }

        public static CTTextBody parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextBody) POIXMLTypeLoader.parse(node, CTTextBody.type, xmlOptions);
        }

        public static CTTextBody parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextBody) POIXMLTypeLoader.parse(xMLInputStream, CTTextBody.type, (XmlOptions) null);
        }

        public static CTTextBody parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextBody) POIXMLTypeLoader.parse(xMLInputStream, CTTextBody.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextBody.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextBody.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTextBodyProperties getBodyPr();

    void setBodyPr(CTTextBodyProperties cTTextBodyProperties);

    CTTextBodyProperties addNewBodyPr();

    CTTextListStyle getLstStyle();

    boolean isSetLstStyle();

    void setLstStyle(CTTextListStyle cTTextListStyle);

    CTTextListStyle addNewLstStyle();

    void unsetLstStyle();

    List<CTTextParagraph> getPList();

    CTTextParagraph[] getPArray();

    CTTextParagraph getPArray(int i);

    int sizeOfPArray();

    void setPArray(CTTextParagraph[] cTTextParagraphArr);

    void setPArray(int i, CTTextParagraph cTTextParagraph);

    CTTextParagraph insertNewP(int i);

    CTTextParagraph addNewP();

    void removeP(int i);
}
