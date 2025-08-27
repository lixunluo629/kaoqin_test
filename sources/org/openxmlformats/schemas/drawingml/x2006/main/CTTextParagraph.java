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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextParagraph.class */
public interface CTTextParagraph extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextParagraph.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextparagraphcaf2type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextParagraph$Factory.class */
    public static final class Factory {
        public static CTTextParagraph newInstance() {
            return (CTTextParagraph) POIXMLTypeLoader.newInstance(CTTextParagraph.type, null);
        }

        public static CTTextParagraph newInstance(XmlOptions xmlOptions) {
            return (CTTextParagraph) POIXMLTypeLoader.newInstance(CTTextParagraph.type, xmlOptions);
        }

        public static CTTextParagraph parse(String str) throws XmlException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(str, CTTextParagraph.type, (XmlOptions) null);
        }

        public static CTTextParagraph parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(str, CTTextParagraph.type, xmlOptions);
        }

        public static CTTextParagraph parse(File file) throws XmlException, IOException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(file, CTTextParagraph.type, (XmlOptions) null);
        }

        public static CTTextParagraph parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(file, CTTextParagraph.type, xmlOptions);
        }

        public static CTTextParagraph parse(URL url) throws XmlException, IOException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(url, CTTextParagraph.type, (XmlOptions) null);
        }

        public static CTTextParagraph parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(url, CTTextParagraph.type, xmlOptions);
        }

        public static CTTextParagraph parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(inputStream, CTTextParagraph.type, (XmlOptions) null);
        }

        public static CTTextParagraph parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(inputStream, CTTextParagraph.type, xmlOptions);
        }

        public static CTTextParagraph parse(Reader reader) throws XmlException, IOException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(reader, CTTextParagraph.type, (XmlOptions) null);
        }

        public static CTTextParagraph parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(reader, CTTextParagraph.type, xmlOptions);
        }

        public static CTTextParagraph parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(xMLStreamReader, CTTextParagraph.type, (XmlOptions) null);
        }

        public static CTTextParagraph parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(xMLStreamReader, CTTextParagraph.type, xmlOptions);
        }

        public static CTTextParagraph parse(Node node) throws XmlException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(node, CTTextParagraph.type, (XmlOptions) null);
        }

        public static CTTextParagraph parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(node, CTTextParagraph.type, xmlOptions);
        }

        public static CTTextParagraph parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(xMLInputStream, CTTextParagraph.type, (XmlOptions) null);
        }

        public static CTTextParagraph parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextParagraph) POIXMLTypeLoader.parse(xMLInputStream, CTTextParagraph.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextParagraph.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextParagraph.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTextParagraphProperties getPPr();

    boolean isSetPPr();

    void setPPr(CTTextParagraphProperties cTTextParagraphProperties);

    CTTextParagraphProperties addNewPPr();

    void unsetPPr();

    List<CTRegularTextRun> getRList();

    CTRegularTextRun[] getRArray();

    CTRegularTextRun getRArray(int i);

    int sizeOfRArray();

    void setRArray(CTRegularTextRun[] cTRegularTextRunArr);

    void setRArray(int i, CTRegularTextRun cTRegularTextRun);

    CTRegularTextRun insertNewR(int i);

    CTRegularTextRun addNewR();

    void removeR(int i);

    List<CTTextLineBreak> getBrList();

    CTTextLineBreak[] getBrArray();

    CTTextLineBreak getBrArray(int i);

    int sizeOfBrArray();

    void setBrArray(CTTextLineBreak[] cTTextLineBreakArr);

    void setBrArray(int i, CTTextLineBreak cTTextLineBreak);

    CTTextLineBreak insertNewBr(int i);

    CTTextLineBreak addNewBr();

    void removeBr(int i);

    List<CTTextField> getFldList();

    CTTextField[] getFldArray();

    CTTextField getFldArray(int i);

    int sizeOfFldArray();

    void setFldArray(CTTextField[] cTTextFieldArr);

    void setFldArray(int i, CTTextField cTTextField);

    CTTextField insertNewFld(int i);

    CTTextField addNewFld();

    void removeFld(int i);

    CTTextCharacterProperties getEndParaRPr();

    boolean isSetEndParaRPr();

    void setEndParaRPr(CTTextCharacterProperties cTTextCharacterProperties);

    CTTextCharacterProperties addNewEndParaRPr();

    void unsetEndParaRPr();
}
