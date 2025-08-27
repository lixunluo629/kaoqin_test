package org.openxmlformats.schemas.officeDocument.x2006.extendedProperties;

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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/extendedProperties/CTProperties.class */
public interface CTProperties extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTProperties.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctproperties3f10type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/extendedProperties/CTProperties$Factory.class */
    public static final class Factory {
        public static CTProperties newInstance() {
            return (CTProperties) POIXMLTypeLoader.newInstance(CTProperties.type, null);
        }

        public static CTProperties newInstance(XmlOptions xmlOptions) {
            return (CTProperties) POIXMLTypeLoader.newInstance(CTProperties.type, xmlOptions);
        }

        public static CTProperties parse(String str) throws XmlException {
            return (CTProperties) POIXMLTypeLoader.parse(str, CTProperties.type, (XmlOptions) null);
        }

        public static CTProperties parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTProperties) POIXMLTypeLoader.parse(str, CTProperties.type, xmlOptions);
        }

        public static CTProperties parse(File file) throws XmlException, IOException {
            return (CTProperties) POIXMLTypeLoader.parse(file, CTProperties.type, (XmlOptions) null);
        }

        public static CTProperties parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTProperties) POIXMLTypeLoader.parse(file, CTProperties.type, xmlOptions);
        }

        public static CTProperties parse(URL url) throws XmlException, IOException {
            return (CTProperties) POIXMLTypeLoader.parse(url, CTProperties.type, (XmlOptions) null);
        }

        public static CTProperties parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTProperties) POIXMLTypeLoader.parse(url, CTProperties.type, xmlOptions);
        }

        public static CTProperties parse(InputStream inputStream) throws XmlException, IOException {
            return (CTProperties) POIXMLTypeLoader.parse(inputStream, CTProperties.type, (XmlOptions) null);
        }

        public static CTProperties parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTProperties) POIXMLTypeLoader.parse(inputStream, CTProperties.type, xmlOptions);
        }

        public static CTProperties parse(Reader reader) throws XmlException, IOException {
            return (CTProperties) POIXMLTypeLoader.parse(reader, CTProperties.type, (XmlOptions) null);
        }

        public static CTProperties parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTProperties) POIXMLTypeLoader.parse(reader, CTProperties.type, xmlOptions);
        }

        public static CTProperties parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTProperties.type, (XmlOptions) null);
        }

        public static CTProperties parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTProperties) POIXMLTypeLoader.parse(xMLStreamReader, CTProperties.type, xmlOptions);
        }

        public static CTProperties parse(Node node) throws XmlException {
            return (CTProperties) POIXMLTypeLoader.parse(node, CTProperties.type, (XmlOptions) null);
        }

        public static CTProperties parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTProperties) POIXMLTypeLoader.parse(node, CTProperties.type, xmlOptions);
        }

        public static CTProperties parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTProperties) POIXMLTypeLoader.parse(xMLInputStream, CTProperties.type, (XmlOptions) null);
        }

        public static CTProperties parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTProperties) POIXMLTypeLoader.parse(xMLInputStream, CTProperties.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTProperties.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTProperties.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getTemplate();

    XmlString xgetTemplate();

    boolean isSetTemplate();

    void setTemplate(String str);

    void xsetTemplate(XmlString xmlString);

    void unsetTemplate();

    String getManager();

    XmlString xgetManager();

    boolean isSetManager();

    void setManager(String str);

    void xsetManager(XmlString xmlString);

    void unsetManager();

    String getCompany();

    XmlString xgetCompany();

    boolean isSetCompany();

    void setCompany(String str);

    void xsetCompany(XmlString xmlString);

    void unsetCompany();

    int getPages();

    XmlInt xgetPages();

    boolean isSetPages();

    void setPages(int i);

    void xsetPages(XmlInt xmlInt);

    void unsetPages();

    int getWords();

    XmlInt xgetWords();

    boolean isSetWords();

    void setWords(int i);

    void xsetWords(XmlInt xmlInt);

    void unsetWords();

    int getCharacters();

    XmlInt xgetCharacters();

    boolean isSetCharacters();

    void setCharacters(int i);

    void xsetCharacters(XmlInt xmlInt);

    void unsetCharacters();

    String getPresentationFormat();

    XmlString xgetPresentationFormat();

    boolean isSetPresentationFormat();

    void setPresentationFormat(String str);

    void xsetPresentationFormat(XmlString xmlString);

    void unsetPresentationFormat();

    int getLines();

    XmlInt xgetLines();

    boolean isSetLines();

    void setLines(int i);

    void xsetLines(XmlInt xmlInt);

    void unsetLines();

    int getParagraphs();

    XmlInt xgetParagraphs();

    boolean isSetParagraphs();

    void setParagraphs(int i);

    void xsetParagraphs(XmlInt xmlInt);

    void unsetParagraphs();

    int getSlides();

    XmlInt xgetSlides();

    boolean isSetSlides();

    void setSlides(int i);

    void xsetSlides(XmlInt xmlInt);

    void unsetSlides();

    int getNotes();

    XmlInt xgetNotes();

    boolean isSetNotes();

    void setNotes(int i);

    void xsetNotes(XmlInt xmlInt);

    void unsetNotes();

    int getTotalTime();

    XmlInt xgetTotalTime();

    boolean isSetTotalTime();

    void setTotalTime(int i);

    void xsetTotalTime(XmlInt xmlInt);

    void unsetTotalTime();

    int getHiddenSlides();

    XmlInt xgetHiddenSlides();

    boolean isSetHiddenSlides();

    void setHiddenSlides(int i);

    void xsetHiddenSlides(XmlInt xmlInt);

    void unsetHiddenSlides();

    int getMMClips();

    XmlInt xgetMMClips();

    boolean isSetMMClips();

    void setMMClips(int i);

    void xsetMMClips(XmlInt xmlInt);

    void unsetMMClips();

    boolean getScaleCrop();

    XmlBoolean xgetScaleCrop();

    boolean isSetScaleCrop();

    void setScaleCrop(boolean z);

    void xsetScaleCrop(XmlBoolean xmlBoolean);

    void unsetScaleCrop();

    CTVectorVariant getHeadingPairs();

    boolean isSetHeadingPairs();

    void setHeadingPairs(CTVectorVariant cTVectorVariant);

    CTVectorVariant addNewHeadingPairs();

    void unsetHeadingPairs();

    CTVectorLpstr getTitlesOfParts();

    boolean isSetTitlesOfParts();

    void setTitlesOfParts(CTVectorLpstr cTVectorLpstr);

    CTVectorLpstr addNewTitlesOfParts();

    void unsetTitlesOfParts();

    boolean getLinksUpToDate();

    XmlBoolean xgetLinksUpToDate();

    boolean isSetLinksUpToDate();

    void setLinksUpToDate(boolean z);

    void xsetLinksUpToDate(XmlBoolean xmlBoolean);

    void unsetLinksUpToDate();

    int getCharactersWithSpaces();

    XmlInt xgetCharactersWithSpaces();

    boolean isSetCharactersWithSpaces();

    void setCharactersWithSpaces(int i);

    void xsetCharactersWithSpaces(XmlInt xmlInt);

    void unsetCharactersWithSpaces();

    boolean getSharedDoc();

    XmlBoolean xgetSharedDoc();

    boolean isSetSharedDoc();

    void setSharedDoc(boolean z);

    void xsetSharedDoc(XmlBoolean xmlBoolean);

    void unsetSharedDoc();

    String getHyperlinkBase();

    XmlString xgetHyperlinkBase();

    boolean isSetHyperlinkBase();

    void setHyperlinkBase(String str);

    void xsetHyperlinkBase(XmlString xmlString);

    void unsetHyperlinkBase();

    CTVectorVariant getHLinks();

    boolean isSetHLinks();

    void setHLinks(CTVectorVariant cTVectorVariant);

    CTVectorVariant addNewHLinks();

    void unsetHLinks();

    boolean getHyperlinksChanged();

    XmlBoolean xgetHyperlinksChanged();

    boolean isSetHyperlinksChanged();

    void setHyperlinksChanged(boolean z);

    void xsetHyperlinksChanged(XmlBoolean xmlBoolean);

    void unsetHyperlinksChanged();

    CTDigSigBlob getDigSig();

    boolean isSetDigSig();

    void setDigSig(CTDigSigBlob cTDigSigBlob);

    CTDigSigBlob addNewDigSig();

    void unsetDigSig();

    String getApplication();

    XmlString xgetApplication();

    boolean isSetApplication();

    void setApplication(String str);

    void xsetApplication(XmlString xmlString);

    void unsetApplication();

    String getAppVersion();

    XmlString xgetAppVersion();

    boolean isSetAppVersion();

    void setAppVersion(String str);

    void xsetAppVersion(XmlString xmlString);

    void unsetAppVersion();

    int getDocSecurity();

    XmlInt xgetDocSecurity();

    boolean isSetDocSecurity();

    void setDocSecurity(int i);

    void xsetDocSecurity(XmlInt xmlInt);

    void unsetDocSecurity();
}
