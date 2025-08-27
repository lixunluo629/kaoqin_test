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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTFonts.class */
public interface CTFonts extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTFonts.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctfonts124etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTFonts$Factory.class */
    public static final class Factory {
        public static CTFonts newInstance() {
            return (CTFonts) POIXMLTypeLoader.newInstance(CTFonts.type, null);
        }

        public static CTFonts newInstance(XmlOptions xmlOptions) {
            return (CTFonts) POIXMLTypeLoader.newInstance(CTFonts.type, xmlOptions);
        }

        public static CTFonts parse(String str) throws XmlException {
            return (CTFonts) POIXMLTypeLoader.parse(str, CTFonts.type, (XmlOptions) null);
        }

        public static CTFonts parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTFonts) POIXMLTypeLoader.parse(str, CTFonts.type, xmlOptions);
        }

        public static CTFonts parse(File file) throws XmlException, IOException {
            return (CTFonts) POIXMLTypeLoader.parse(file, CTFonts.type, (XmlOptions) null);
        }

        public static CTFonts parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFonts) POIXMLTypeLoader.parse(file, CTFonts.type, xmlOptions);
        }

        public static CTFonts parse(URL url) throws XmlException, IOException {
            return (CTFonts) POIXMLTypeLoader.parse(url, CTFonts.type, (XmlOptions) null);
        }

        public static CTFonts parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFonts) POIXMLTypeLoader.parse(url, CTFonts.type, xmlOptions);
        }

        public static CTFonts parse(InputStream inputStream) throws XmlException, IOException {
            return (CTFonts) POIXMLTypeLoader.parse(inputStream, CTFonts.type, (XmlOptions) null);
        }

        public static CTFonts parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFonts) POIXMLTypeLoader.parse(inputStream, CTFonts.type, xmlOptions);
        }

        public static CTFonts parse(Reader reader) throws XmlException, IOException {
            return (CTFonts) POIXMLTypeLoader.parse(reader, CTFonts.type, (XmlOptions) null);
        }

        public static CTFonts parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFonts) POIXMLTypeLoader.parse(reader, CTFonts.type, xmlOptions);
        }

        public static CTFonts parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTFonts) POIXMLTypeLoader.parse(xMLStreamReader, CTFonts.type, (XmlOptions) null);
        }

        public static CTFonts parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTFonts) POIXMLTypeLoader.parse(xMLStreamReader, CTFonts.type, xmlOptions);
        }

        public static CTFonts parse(Node node) throws XmlException {
            return (CTFonts) POIXMLTypeLoader.parse(node, CTFonts.type, (XmlOptions) null);
        }

        public static CTFonts parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTFonts) POIXMLTypeLoader.parse(node, CTFonts.type, xmlOptions);
        }

        public static CTFonts parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTFonts) POIXMLTypeLoader.parse(xMLInputStream, CTFonts.type, (XmlOptions) null);
        }

        public static CTFonts parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTFonts) POIXMLTypeLoader.parse(xMLInputStream, CTFonts.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFonts.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFonts.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STHint$Enum getHint();

    STHint xgetHint();

    boolean isSetHint();

    void setHint(STHint$Enum sTHint$Enum);

    void xsetHint(STHint sTHint);

    void unsetHint();

    String getAscii();

    STString xgetAscii();

    boolean isSetAscii();

    void setAscii(String str);

    void xsetAscii(STString sTString);

    void unsetAscii();

    String getHAnsi();

    STString xgetHAnsi();

    boolean isSetHAnsi();

    void setHAnsi(String str);

    void xsetHAnsi(STString sTString);

    void unsetHAnsi();

    String getEastAsia();

    STString xgetEastAsia();

    boolean isSetEastAsia();

    void setEastAsia(String str);

    void xsetEastAsia(STString sTString);

    void unsetEastAsia();

    String getCs();

    STString xgetCs();

    boolean isSetCs();

    void setCs(String str);

    void xsetCs(STString sTString);

    void unsetCs();

    STTheme.Enum getAsciiTheme();

    STTheme xgetAsciiTheme();

    boolean isSetAsciiTheme();

    void setAsciiTheme(STTheme.Enum r1);

    void xsetAsciiTheme(STTheme sTTheme);

    void unsetAsciiTheme();

    STTheme.Enum getHAnsiTheme();

    STTheme xgetHAnsiTheme();

    boolean isSetHAnsiTheme();

    void setHAnsiTheme(STTheme.Enum r1);

    void xsetHAnsiTheme(STTheme sTTheme);

    void unsetHAnsiTheme();

    STTheme.Enum getEastAsiaTheme();

    STTheme xgetEastAsiaTheme();

    boolean isSetEastAsiaTheme();

    void setEastAsiaTheme(STTheme.Enum r1);

    void xsetEastAsiaTheme(STTheme sTTheme);

    void unsetEastAsiaTheme();

    STTheme.Enum getCstheme();

    STTheme xgetCstheme();

    boolean isSetCstheme();

    void setCstheme(STTheme.Enum r1);

    void xsetCstheme(STTheme sTTheme);

    void unsetCstheme();
}
