package com.microsoft.schemas.vml;

import com.microsoft.schemas.office.office.STInsetMode;
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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTxbxContent;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTTextbox.class */
public interface CTTextbox extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextbox.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextboxf712type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTTextbox$Factory.class */
    public static final class Factory {
        public static CTTextbox newInstance() {
            return (CTTextbox) POIXMLTypeLoader.newInstance(CTTextbox.type, null);
        }

        public static CTTextbox newInstance(XmlOptions xmlOptions) {
            return (CTTextbox) POIXMLTypeLoader.newInstance(CTTextbox.type, xmlOptions);
        }

        public static CTTextbox parse(String str) throws XmlException {
            return (CTTextbox) POIXMLTypeLoader.parse(str, CTTextbox.type, (XmlOptions) null);
        }

        public static CTTextbox parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextbox) POIXMLTypeLoader.parse(str, CTTextbox.type, xmlOptions);
        }

        public static CTTextbox parse(File file) throws XmlException, IOException {
            return (CTTextbox) POIXMLTypeLoader.parse(file, CTTextbox.type, (XmlOptions) null);
        }

        public static CTTextbox parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextbox) POIXMLTypeLoader.parse(file, CTTextbox.type, xmlOptions);
        }

        public static CTTextbox parse(URL url) throws XmlException, IOException {
            return (CTTextbox) POIXMLTypeLoader.parse(url, CTTextbox.type, (XmlOptions) null);
        }

        public static CTTextbox parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextbox) POIXMLTypeLoader.parse(url, CTTextbox.type, xmlOptions);
        }

        public static CTTextbox parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextbox) POIXMLTypeLoader.parse(inputStream, CTTextbox.type, (XmlOptions) null);
        }

        public static CTTextbox parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextbox) POIXMLTypeLoader.parse(inputStream, CTTextbox.type, xmlOptions);
        }

        public static CTTextbox parse(Reader reader) throws XmlException, IOException {
            return (CTTextbox) POIXMLTypeLoader.parse(reader, CTTextbox.type, (XmlOptions) null);
        }

        public static CTTextbox parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextbox) POIXMLTypeLoader.parse(reader, CTTextbox.type, xmlOptions);
        }

        public static CTTextbox parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextbox) POIXMLTypeLoader.parse(xMLStreamReader, CTTextbox.type, (XmlOptions) null);
        }

        public static CTTextbox parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextbox) POIXMLTypeLoader.parse(xMLStreamReader, CTTextbox.type, xmlOptions);
        }

        public static CTTextbox parse(Node node) throws XmlException {
            return (CTTextbox) POIXMLTypeLoader.parse(node, CTTextbox.type, (XmlOptions) null);
        }

        public static CTTextbox parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextbox) POIXMLTypeLoader.parse(node, CTTextbox.type, xmlOptions);
        }

        public static CTTextbox parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextbox) POIXMLTypeLoader.parse(xMLInputStream, CTTextbox.type, (XmlOptions) null);
        }

        public static CTTextbox parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextbox) POIXMLTypeLoader.parse(xMLInputStream, CTTextbox.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextbox.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextbox.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTxbxContent getTxbxContent();

    boolean isSetTxbxContent();

    void setTxbxContent(CTTxbxContent cTTxbxContent);

    CTTxbxContent addNewTxbxContent();

    void unsetTxbxContent();

    String getId();

    XmlString xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlString xmlString);

    void unsetId();

    String getStyle();

    XmlString xgetStyle();

    boolean isSetStyle();

    void setStyle(String str);

    void xsetStyle(XmlString xmlString);

    void unsetStyle();

    String getInset();

    XmlString xgetInset();

    boolean isSetInset();

    void setInset(String str);

    void xsetInset(XmlString xmlString);

    void unsetInset();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getSingleclick();

    com.microsoft.schemas.office.office.STTrueFalse xgetSingleclick();

    boolean isSetSingleclick();

    void setSingleclick(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetSingleclick(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetSingleclick();

    STInsetMode.Enum getInsetmode();

    STInsetMode xgetInsetmode();

    boolean isSetInsetmode();

    void setInsetmode(STInsetMode.Enum r1);

    void xsetInsetmode(STInsetMode sTInsetMode);

    void unsetInsetmode();
}
