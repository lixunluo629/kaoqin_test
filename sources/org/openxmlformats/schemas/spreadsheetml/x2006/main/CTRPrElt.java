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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTRPrElt.class */
public interface CTRPrElt extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTRPrElt.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctrpreltecc2type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTRPrElt$Factory.class */
    public static final class Factory {
        public static CTRPrElt newInstance() {
            return (CTRPrElt) POIXMLTypeLoader.newInstance(CTRPrElt.type, null);
        }

        public static CTRPrElt newInstance(XmlOptions xmlOptions) {
            return (CTRPrElt) POIXMLTypeLoader.newInstance(CTRPrElt.type, xmlOptions);
        }

        public static CTRPrElt parse(String str) throws XmlException {
            return (CTRPrElt) POIXMLTypeLoader.parse(str, CTRPrElt.type, (XmlOptions) null);
        }

        public static CTRPrElt parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTRPrElt) POIXMLTypeLoader.parse(str, CTRPrElt.type, xmlOptions);
        }

        public static CTRPrElt parse(File file) throws XmlException, IOException {
            return (CTRPrElt) POIXMLTypeLoader.parse(file, CTRPrElt.type, (XmlOptions) null);
        }

        public static CTRPrElt parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPrElt) POIXMLTypeLoader.parse(file, CTRPrElt.type, xmlOptions);
        }

        public static CTRPrElt parse(URL url) throws XmlException, IOException {
            return (CTRPrElt) POIXMLTypeLoader.parse(url, CTRPrElt.type, (XmlOptions) null);
        }

        public static CTRPrElt parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPrElt) POIXMLTypeLoader.parse(url, CTRPrElt.type, xmlOptions);
        }

        public static CTRPrElt parse(InputStream inputStream) throws XmlException, IOException {
            return (CTRPrElt) POIXMLTypeLoader.parse(inputStream, CTRPrElt.type, (XmlOptions) null);
        }

        public static CTRPrElt parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPrElt) POIXMLTypeLoader.parse(inputStream, CTRPrElt.type, xmlOptions);
        }

        public static CTRPrElt parse(Reader reader) throws XmlException, IOException {
            return (CTRPrElt) POIXMLTypeLoader.parse(reader, CTRPrElt.type, (XmlOptions) null);
        }

        public static CTRPrElt parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRPrElt) POIXMLTypeLoader.parse(reader, CTRPrElt.type, xmlOptions);
        }

        public static CTRPrElt parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTRPrElt) POIXMLTypeLoader.parse(xMLStreamReader, CTRPrElt.type, (XmlOptions) null);
        }

        public static CTRPrElt parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTRPrElt) POIXMLTypeLoader.parse(xMLStreamReader, CTRPrElt.type, xmlOptions);
        }

        public static CTRPrElt parse(Node node) throws XmlException {
            return (CTRPrElt) POIXMLTypeLoader.parse(node, CTRPrElt.type, (XmlOptions) null);
        }

        public static CTRPrElt parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTRPrElt) POIXMLTypeLoader.parse(node, CTRPrElt.type, xmlOptions);
        }

        public static CTRPrElt parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTRPrElt) POIXMLTypeLoader.parse(xMLInputStream, CTRPrElt.type, (XmlOptions) null);
        }

        public static CTRPrElt parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTRPrElt) POIXMLTypeLoader.parse(xMLInputStream, CTRPrElt.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRPrElt.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRPrElt.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTFontName> getRFontList();

    CTFontName[] getRFontArray();

    CTFontName getRFontArray(int i);

    int sizeOfRFontArray();

    void setRFontArray(CTFontName[] cTFontNameArr);

    void setRFontArray(int i, CTFontName cTFontName);

    CTFontName insertNewRFont(int i);

    CTFontName addNewRFont();

    void removeRFont(int i);

    List<CTIntProperty> getCharsetList();

    CTIntProperty[] getCharsetArray();

    CTIntProperty getCharsetArray(int i);

    int sizeOfCharsetArray();

    void setCharsetArray(CTIntProperty[] cTIntPropertyArr);

    void setCharsetArray(int i, CTIntProperty cTIntProperty);

    CTIntProperty insertNewCharset(int i);

    CTIntProperty addNewCharset();

    void removeCharset(int i);

    List<CTIntProperty> getFamilyList();

    CTIntProperty[] getFamilyArray();

    CTIntProperty getFamilyArray(int i);

    int sizeOfFamilyArray();

    void setFamilyArray(CTIntProperty[] cTIntPropertyArr);

    void setFamilyArray(int i, CTIntProperty cTIntProperty);

    CTIntProperty insertNewFamily(int i);

    CTIntProperty addNewFamily();

    void removeFamily(int i);

    List<CTBooleanProperty> getBList();

    CTBooleanProperty[] getBArray();

    CTBooleanProperty getBArray(int i);

    int sizeOfBArray();

    void setBArray(CTBooleanProperty[] cTBooleanPropertyArr);

    void setBArray(int i, CTBooleanProperty cTBooleanProperty);

    CTBooleanProperty insertNewB(int i);

    CTBooleanProperty addNewB();

    void removeB(int i);

    List<CTBooleanProperty> getIList();

    CTBooleanProperty[] getIArray();

    CTBooleanProperty getIArray(int i);

    int sizeOfIArray();

    void setIArray(CTBooleanProperty[] cTBooleanPropertyArr);

    void setIArray(int i, CTBooleanProperty cTBooleanProperty);

    CTBooleanProperty insertNewI(int i);

    CTBooleanProperty addNewI();

    void removeI(int i);

    List<CTBooleanProperty> getStrikeList();

    CTBooleanProperty[] getStrikeArray();

    CTBooleanProperty getStrikeArray(int i);

    int sizeOfStrikeArray();

    void setStrikeArray(CTBooleanProperty[] cTBooleanPropertyArr);

    void setStrikeArray(int i, CTBooleanProperty cTBooleanProperty);

    CTBooleanProperty insertNewStrike(int i);

    CTBooleanProperty addNewStrike();

    void removeStrike(int i);

    List<CTBooleanProperty> getOutlineList();

    CTBooleanProperty[] getOutlineArray();

    CTBooleanProperty getOutlineArray(int i);

    int sizeOfOutlineArray();

    void setOutlineArray(CTBooleanProperty[] cTBooleanPropertyArr);

    void setOutlineArray(int i, CTBooleanProperty cTBooleanProperty);

    CTBooleanProperty insertNewOutline(int i);

    CTBooleanProperty addNewOutline();

    void removeOutline(int i);

    List<CTBooleanProperty> getShadowList();

    CTBooleanProperty[] getShadowArray();

    CTBooleanProperty getShadowArray(int i);

    int sizeOfShadowArray();

    void setShadowArray(CTBooleanProperty[] cTBooleanPropertyArr);

    void setShadowArray(int i, CTBooleanProperty cTBooleanProperty);

    CTBooleanProperty insertNewShadow(int i);

    CTBooleanProperty addNewShadow();

    void removeShadow(int i);

    List<CTBooleanProperty> getCondenseList();

    CTBooleanProperty[] getCondenseArray();

    CTBooleanProperty getCondenseArray(int i);

    int sizeOfCondenseArray();

    void setCondenseArray(CTBooleanProperty[] cTBooleanPropertyArr);

    void setCondenseArray(int i, CTBooleanProperty cTBooleanProperty);

    CTBooleanProperty insertNewCondense(int i);

    CTBooleanProperty addNewCondense();

    void removeCondense(int i);

    List<CTBooleanProperty> getExtendList();

    CTBooleanProperty[] getExtendArray();

    CTBooleanProperty getExtendArray(int i);

    int sizeOfExtendArray();

    void setExtendArray(CTBooleanProperty[] cTBooleanPropertyArr);

    void setExtendArray(int i, CTBooleanProperty cTBooleanProperty);

    CTBooleanProperty insertNewExtend(int i);

    CTBooleanProperty addNewExtend();

    void removeExtend(int i);

    List<CTColor> getColorList();

    CTColor[] getColorArray();

    CTColor getColorArray(int i);

    int sizeOfColorArray();

    void setColorArray(CTColor[] cTColorArr);

    void setColorArray(int i, CTColor cTColor);

    CTColor insertNewColor(int i);

    CTColor addNewColor();

    void removeColor(int i);

    List<CTFontSize> getSzList();

    CTFontSize[] getSzArray();

    CTFontSize getSzArray(int i);

    int sizeOfSzArray();

    void setSzArray(CTFontSize[] cTFontSizeArr);

    void setSzArray(int i, CTFontSize cTFontSize);

    CTFontSize insertNewSz(int i);

    CTFontSize addNewSz();

    void removeSz(int i);

    List<CTUnderlineProperty> getUList();

    CTUnderlineProperty[] getUArray();

    CTUnderlineProperty getUArray(int i);

    int sizeOfUArray();

    void setUArray(CTUnderlineProperty[] cTUnderlinePropertyArr);

    void setUArray(int i, CTUnderlineProperty cTUnderlineProperty);

    CTUnderlineProperty insertNewU(int i);

    CTUnderlineProperty addNewU();

    void removeU(int i);

    List<CTVerticalAlignFontProperty> getVertAlignList();

    CTVerticalAlignFontProperty[] getVertAlignArray();

    CTVerticalAlignFontProperty getVertAlignArray(int i);

    int sizeOfVertAlignArray();

    void setVertAlignArray(CTVerticalAlignFontProperty[] cTVerticalAlignFontPropertyArr);

    void setVertAlignArray(int i, CTVerticalAlignFontProperty cTVerticalAlignFontProperty);

    CTVerticalAlignFontProperty insertNewVertAlign(int i);

    CTVerticalAlignFontProperty addNewVertAlign();

    void removeVertAlign(int i);

    List<CTFontScheme> getSchemeList();

    CTFontScheme[] getSchemeArray();

    CTFontScheme getSchemeArray(int i);

    int sizeOfSchemeArray();

    void setSchemeArray(CTFontScheme[] cTFontSchemeArr);

    void setSchemeArray(int i, CTFontScheme cTFontScheme);

    CTFontScheme insertNewScheme(int i);

    CTFontScheme addNewScheme();

    void removeScheme(int i);
}
