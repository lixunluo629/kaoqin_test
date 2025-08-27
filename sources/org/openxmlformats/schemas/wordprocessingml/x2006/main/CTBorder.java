package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTBorder.class */
public interface CTBorder extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBorder.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctbordercdfctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTBorder$Factory.class */
    public static final class Factory {
        public static CTBorder newInstance() {
            return (CTBorder) POIXMLTypeLoader.newInstance(CTBorder.type, null);
        }

        public static CTBorder newInstance(XmlOptions xmlOptions) {
            return (CTBorder) POIXMLTypeLoader.newInstance(CTBorder.type, xmlOptions);
        }

        public static CTBorder parse(String str) throws XmlException {
            return (CTBorder) POIXMLTypeLoader.parse(str, CTBorder.type, (XmlOptions) null);
        }

        public static CTBorder parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBorder) POIXMLTypeLoader.parse(str, CTBorder.type, xmlOptions);
        }

        public static CTBorder parse(File file) throws XmlException, IOException {
            return (CTBorder) POIXMLTypeLoader.parse(file, CTBorder.type, (XmlOptions) null);
        }

        public static CTBorder parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorder) POIXMLTypeLoader.parse(file, CTBorder.type, xmlOptions);
        }

        public static CTBorder parse(URL url) throws XmlException, IOException {
            return (CTBorder) POIXMLTypeLoader.parse(url, CTBorder.type, (XmlOptions) null);
        }

        public static CTBorder parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorder) POIXMLTypeLoader.parse(url, CTBorder.type, xmlOptions);
        }

        public static CTBorder parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBorder) POIXMLTypeLoader.parse(inputStream, CTBorder.type, (XmlOptions) null);
        }

        public static CTBorder parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorder) POIXMLTypeLoader.parse(inputStream, CTBorder.type, xmlOptions);
        }

        public static CTBorder parse(Reader reader) throws XmlException, IOException {
            return (CTBorder) POIXMLTypeLoader.parse(reader, CTBorder.type, (XmlOptions) null);
        }

        public static CTBorder parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBorder) POIXMLTypeLoader.parse(reader, CTBorder.type, xmlOptions);
        }

        public static CTBorder parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBorder) POIXMLTypeLoader.parse(xMLStreamReader, CTBorder.type, (XmlOptions) null);
        }

        public static CTBorder parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBorder) POIXMLTypeLoader.parse(xMLStreamReader, CTBorder.type, xmlOptions);
        }

        public static CTBorder parse(Node node) throws XmlException {
            return (CTBorder) POIXMLTypeLoader.parse(node, CTBorder.type, (XmlOptions) null);
        }

        public static CTBorder parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBorder) POIXMLTypeLoader.parse(node, CTBorder.type, xmlOptions);
        }

        public static CTBorder parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBorder) POIXMLTypeLoader.parse(xMLInputStream, CTBorder.type, (XmlOptions) null);
        }

        public static CTBorder parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBorder) POIXMLTypeLoader.parse(xMLInputStream, CTBorder.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBorder.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBorder.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STBorder.Enum getVal();

    STBorder xgetVal();

    void setVal(STBorder.Enum r1);

    void xsetVal(STBorder sTBorder);

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

    BigInteger getSz();

    STEighthPointMeasure xgetSz();

    boolean isSetSz();

    void setSz(BigInteger bigInteger);

    void xsetSz(STEighthPointMeasure sTEighthPointMeasure);

    void unsetSz();

    BigInteger getSpace();

    STPointMeasure xgetSpace();

    boolean isSetSpace();

    void setSpace(BigInteger bigInteger);

    void xsetSpace(STPointMeasure sTPointMeasure);

    void unsetSpace();

    STOnOff.Enum getShadow();

    STOnOff xgetShadow();

    boolean isSetShadow();

    void setShadow(STOnOff.Enum r1);

    void xsetShadow(STOnOff sTOnOff);

    void unsetShadow();

    STOnOff.Enum getFrame();

    STOnOff xgetFrame();

    boolean isSetFrame();

    void setFrame(STOnOff.Enum r1);

    void xsetFrame(STOnOff sTOnOff);

    void unsetFrame();
}
