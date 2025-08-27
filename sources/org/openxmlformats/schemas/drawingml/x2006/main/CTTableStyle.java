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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTableStyle.class */
public interface CTTableStyle extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTableStyle.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttablestyled59etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTableStyle$Factory.class */
    public static final class Factory {
        public static CTTableStyle newInstance() {
            return (CTTableStyle) POIXMLTypeLoader.newInstance(CTTableStyle.type, null);
        }

        public static CTTableStyle newInstance(XmlOptions xmlOptions) {
            return (CTTableStyle) POIXMLTypeLoader.newInstance(CTTableStyle.type, xmlOptions);
        }

        public static CTTableStyle parse(String str) throws XmlException {
            return (CTTableStyle) POIXMLTypeLoader.parse(str, CTTableStyle.type, (XmlOptions) null);
        }

        public static CTTableStyle parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyle) POIXMLTypeLoader.parse(str, CTTableStyle.type, xmlOptions);
        }

        public static CTTableStyle parse(File file) throws XmlException, IOException {
            return (CTTableStyle) POIXMLTypeLoader.parse(file, CTTableStyle.type, (XmlOptions) null);
        }

        public static CTTableStyle parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyle) POIXMLTypeLoader.parse(file, CTTableStyle.type, xmlOptions);
        }

        public static CTTableStyle parse(URL url) throws XmlException, IOException {
            return (CTTableStyle) POIXMLTypeLoader.parse(url, CTTableStyle.type, (XmlOptions) null);
        }

        public static CTTableStyle parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyle) POIXMLTypeLoader.parse(url, CTTableStyle.type, xmlOptions);
        }

        public static CTTableStyle parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTableStyle) POIXMLTypeLoader.parse(inputStream, CTTableStyle.type, (XmlOptions) null);
        }

        public static CTTableStyle parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyle) POIXMLTypeLoader.parse(inputStream, CTTableStyle.type, xmlOptions);
        }

        public static CTTableStyle parse(Reader reader) throws XmlException, IOException {
            return (CTTableStyle) POIXMLTypeLoader.parse(reader, CTTableStyle.type, (XmlOptions) null);
        }

        public static CTTableStyle parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableStyle) POIXMLTypeLoader.parse(reader, CTTableStyle.type, xmlOptions);
        }

        public static CTTableStyle parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTableStyle) POIXMLTypeLoader.parse(xMLStreamReader, CTTableStyle.type, (XmlOptions) null);
        }

        public static CTTableStyle parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyle) POIXMLTypeLoader.parse(xMLStreamReader, CTTableStyle.type, xmlOptions);
        }

        public static CTTableStyle parse(Node node) throws XmlException {
            return (CTTableStyle) POIXMLTypeLoader.parse(node, CTTableStyle.type, (XmlOptions) null);
        }

        public static CTTableStyle parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTableStyle) POIXMLTypeLoader.parse(node, CTTableStyle.type, xmlOptions);
        }

        public static CTTableStyle parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTableStyle) POIXMLTypeLoader.parse(xMLInputStream, CTTableStyle.type, (XmlOptions) null);
        }

        public static CTTableStyle parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTableStyle) POIXMLTypeLoader.parse(xMLInputStream, CTTableStyle.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableStyle.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableStyle.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTableBackgroundStyle getTblBg();

    boolean isSetTblBg();

    void setTblBg(CTTableBackgroundStyle cTTableBackgroundStyle);

    CTTableBackgroundStyle addNewTblBg();

    void unsetTblBg();

    CTTablePartStyle getWholeTbl();

    boolean isSetWholeTbl();

    void setWholeTbl(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewWholeTbl();

    void unsetWholeTbl();

    CTTablePartStyle getBand1H();

    boolean isSetBand1H();

    void setBand1H(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewBand1H();

    void unsetBand1H();

    CTTablePartStyle getBand2H();

    boolean isSetBand2H();

    void setBand2H(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewBand2H();

    void unsetBand2H();

    CTTablePartStyle getBand1V();

    boolean isSetBand1V();

    void setBand1V(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewBand1V();

    void unsetBand1V();

    CTTablePartStyle getBand2V();

    boolean isSetBand2V();

    void setBand2V(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewBand2V();

    void unsetBand2V();

    CTTablePartStyle getLastCol();

    boolean isSetLastCol();

    void setLastCol(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewLastCol();

    void unsetLastCol();

    CTTablePartStyle getFirstCol();

    boolean isSetFirstCol();

    void setFirstCol(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewFirstCol();

    void unsetFirstCol();

    CTTablePartStyle getLastRow();

    boolean isSetLastRow();

    void setLastRow(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewLastRow();

    void unsetLastRow();

    CTTablePartStyle getSeCell();

    boolean isSetSeCell();

    void setSeCell(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewSeCell();

    void unsetSeCell();

    CTTablePartStyle getSwCell();

    boolean isSetSwCell();

    void setSwCell(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewSwCell();

    void unsetSwCell();

    CTTablePartStyle getFirstRow();

    boolean isSetFirstRow();

    void setFirstRow(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewFirstRow();

    void unsetFirstRow();

    CTTablePartStyle getNeCell();

    boolean isSetNeCell();

    void setNeCell(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewNeCell();

    void unsetNeCell();

    CTTablePartStyle getNwCell();

    boolean isSetNwCell();

    void setNwCell(CTTablePartStyle cTTablePartStyle);

    CTTablePartStyle addNewNwCell();

    void unsetNwCell();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    String getStyleId();

    STGuid xgetStyleId();

    void setStyleId(String str);

    void xsetStyleId(STGuid sTGuid);

    String getStyleName();

    XmlString xgetStyleName();

    void setStyleName(String str);

    void xsetStyleName(XmlString xmlString);
}
