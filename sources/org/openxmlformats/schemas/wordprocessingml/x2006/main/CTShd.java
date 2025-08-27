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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTShd.class */
public interface CTShd extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTShd.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctshd58c3type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTShd$Factory.class */
    public static final class Factory {
        public static CTShd newInstance() {
            return (CTShd) POIXMLTypeLoader.newInstance(CTShd.type, null);
        }

        public static CTShd newInstance(XmlOptions xmlOptions) {
            return (CTShd) POIXMLTypeLoader.newInstance(CTShd.type, xmlOptions);
        }

        public static CTShd parse(String str) throws XmlException {
            return (CTShd) POIXMLTypeLoader.parse(str, CTShd.type, (XmlOptions) null);
        }

        public static CTShd parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTShd) POIXMLTypeLoader.parse(str, CTShd.type, xmlOptions);
        }

        public static CTShd parse(File file) throws XmlException, IOException {
            return (CTShd) POIXMLTypeLoader.parse(file, CTShd.type, (XmlOptions) null);
        }

        public static CTShd parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShd) POIXMLTypeLoader.parse(file, CTShd.type, xmlOptions);
        }

        public static CTShd parse(URL url) throws XmlException, IOException {
            return (CTShd) POIXMLTypeLoader.parse(url, CTShd.type, (XmlOptions) null);
        }

        public static CTShd parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShd) POIXMLTypeLoader.parse(url, CTShd.type, xmlOptions);
        }

        public static CTShd parse(InputStream inputStream) throws XmlException, IOException {
            return (CTShd) POIXMLTypeLoader.parse(inputStream, CTShd.type, (XmlOptions) null);
        }

        public static CTShd parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShd) POIXMLTypeLoader.parse(inputStream, CTShd.type, xmlOptions);
        }

        public static CTShd parse(Reader reader) throws XmlException, IOException {
            return (CTShd) POIXMLTypeLoader.parse(reader, CTShd.type, (XmlOptions) null);
        }

        public static CTShd parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShd) POIXMLTypeLoader.parse(reader, CTShd.type, xmlOptions);
        }

        public static CTShd parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTShd) POIXMLTypeLoader.parse(xMLStreamReader, CTShd.type, (XmlOptions) null);
        }

        public static CTShd parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTShd) POIXMLTypeLoader.parse(xMLStreamReader, CTShd.type, xmlOptions);
        }

        public static CTShd parse(Node node) throws XmlException {
            return (CTShd) POIXMLTypeLoader.parse(node, CTShd.type, (XmlOptions) null);
        }

        public static CTShd parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTShd) POIXMLTypeLoader.parse(node, CTShd.type, xmlOptions);
        }

        public static CTShd parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTShd) POIXMLTypeLoader.parse(xMLInputStream, CTShd.type, (XmlOptions) null);
        }

        public static CTShd parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTShd) POIXMLTypeLoader.parse(xMLInputStream, CTShd.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTShd.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTShd.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STShd.Enum getVal();

    STShd xgetVal();

    void setVal(STShd.Enum r1);

    void xsetVal(STShd sTShd);

    Object getColor();

    STHexColor xgetColor();

    boolean isSetColor();

    void setColor(Object obj);

    void xsetColor(STHexColor sTHexColor);

    void unsetColor();

    STThemeColor$Enum getThemeColor();

    STThemeColor xgetThemeColor();

    boolean isSetThemeColor();

    void setThemeColor(STThemeColor$Enum sTThemeColor$Enum);

    void xsetThemeColor(STThemeColor sTThemeColor);

    void unsetThemeColor();

    byte[] getThemeTint();

    STUcharHexNumber xgetThemeTint();

    boolean isSetThemeTint();

    void setThemeTint(byte[] bArr);

    void xsetThemeTint(STUcharHexNumber sTUcharHexNumber);

    void unsetThemeTint();

    byte[] getThemeShade();

    STUcharHexNumber xgetThemeShade();

    boolean isSetThemeShade();

    void setThemeShade(byte[] bArr);

    void xsetThemeShade(STUcharHexNumber sTUcharHexNumber);

    void unsetThemeShade();

    Object getFill();

    STHexColor xgetFill();

    boolean isSetFill();

    void setFill(Object obj);

    void xsetFill(STHexColor sTHexColor);

    void unsetFill();

    STThemeColor$Enum getThemeFill();

    STThemeColor xgetThemeFill();

    boolean isSetThemeFill();

    void setThemeFill(STThemeColor$Enum sTThemeColor$Enum);

    void xsetThemeFill(STThemeColor sTThemeColor);

    void unsetThemeFill();

    byte[] getThemeFillTint();

    STUcharHexNumber xgetThemeFillTint();

    boolean isSetThemeFillTint();

    void setThemeFillTint(byte[] bArr);

    void xsetThemeFillTint(STUcharHexNumber sTUcharHexNumber);

    void unsetThemeFillTint();

    byte[] getThemeFillShade();

    STUcharHexNumber xgetThemeFillShade();

    boolean isSetThemeFillShade();

    void setThemeFillShade(byte[] bArr);

    void xsetThemeFillShade(STUcharHexNumber sTUcharHexNumber);

    void unsetThemeFillShade();
}
