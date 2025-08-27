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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextNormalAutofit.class */
public interface CTTextNormalAutofit extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextNormalAutofit.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextnormalautofitbbdftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextNormalAutofit$Factory.class */
    public static final class Factory {
        public static CTTextNormalAutofit newInstance() {
            return (CTTextNormalAutofit) POIXMLTypeLoader.newInstance(CTTextNormalAutofit.type, null);
        }

        public static CTTextNormalAutofit newInstance(XmlOptions xmlOptions) {
            return (CTTextNormalAutofit) POIXMLTypeLoader.newInstance(CTTextNormalAutofit.type, xmlOptions);
        }

        public static CTTextNormalAutofit parse(String str) throws XmlException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(str, CTTextNormalAutofit.type, (XmlOptions) null);
        }

        public static CTTextNormalAutofit parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(str, CTTextNormalAutofit.type, xmlOptions);
        }

        public static CTTextNormalAutofit parse(File file) throws XmlException, IOException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(file, CTTextNormalAutofit.type, (XmlOptions) null);
        }

        public static CTTextNormalAutofit parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(file, CTTextNormalAutofit.type, xmlOptions);
        }

        public static CTTextNormalAutofit parse(URL url) throws XmlException, IOException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(url, CTTextNormalAutofit.type, (XmlOptions) null);
        }

        public static CTTextNormalAutofit parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(url, CTTextNormalAutofit.type, xmlOptions);
        }

        public static CTTextNormalAutofit parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(inputStream, CTTextNormalAutofit.type, (XmlOptions) null);
        }

        public static CTTextNormalAutofit parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(inputStream, CTTextNormalAutofit.type, xmlOptions);
        }

        public static CTTextNormalAutofit parse(Reader reader) throws XmlException, IOException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(reader, CTTextNormalAutofit.type, (XmlOptions) null);
        }

        public static CTTextNormalAutofit parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(reader, CTTextNormalAutofit.type, xmlOptions);
        }

        public static CTTextNormalAutofit parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(xMLStreamReader, CTTextNormalAutofit.type, (XmlOptions) null);
        }

        public static CTTextNormalAutofit parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(xMLStreamReader, CTTextNormalAutofit.type, xmlOptions);
        }

        public static CTTextNormalAutofit parse(Node node) throws XmlException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(node, CTTextNormalAutofit.type, (XmlOptions) null);
        }

        public static CTTextNormalAutofit parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(node, CTTextNormalAutofit.type, xmlOptions);
        }

        public static CTTextNormalAutofit parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(xMLInputStream, CTTextNormalAutofit.type, (XmlOptions) null);
        }

        public static CTTextNormalAutofit parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextNormalAutofit) POIXMLTypeLoader.parse(xMLInputStream, CTTextNormalAutofit.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextNormalAutofit.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextNormalAutofit.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getFontScale();

    STTextFontScalePercent xgetFontScale();

    boolean isSetFontScale();

    void setFontScale(int i);

    void xsetFontScale(STTextFontScalePercent sTTextFontScalePercent);

    void unsetFontScale();

    int getLnSpcReduction();

    STTextSpacingPercent xgetLnSpcReduction();

    boolean isSetLnSpcReduction();

    void setLnSpcReduction(int i);

    void xsetLnSpcReduction(STTextSpacingPercent sTTextSpacingPercent);

    void unsetLnSpcReduction();
}
