package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTHeaderFooter.class */
public interface CTHeaderFooter extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTHeaderFooter.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctheaderfooter90d1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTHeaderFooter$Factory.class */
    public static final class Factory {
        public static CTHeaderFooter newInstance() {
            return (CTHeaderFooter) POIXMLTypeLoader.newInstance(CTHeaderFooter.type, null);
        }

        public static CTHeaderFooter newInstance(XmlOptions xmlOptions) {
            return (CTHeaderFooter) POIXMLTypeLoader.newInstance(CTHeaderFooter.type, xmlOptions);
        }

        public static CTHeaderFooter parse(String str) throws XmlException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(str, CTHeaderFooter.type, (XmlOptions) null);
        }

        public static CTHeaderFooter parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(str, CTHeaderFooter.type, xmlOptions);
        }

        public static CTHeaderFooter parse(File file) throws XmlException, IOException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(file, CTHeaderFooter.type, (XmlOptions) null);
        }

        public static CTHeaderFooter parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(file, CTHeaderFooter.type, xmlOptions);
        }

        public static CTHeaderFooter parse(URL url) throws XmlException, IOException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(url, CTHeaderFooter.type, (XmlOptions) null);
        }

        public static CTHeaderFooter parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(url, CTHeaderFooter.type, xmlOptions);
        }

        public static CTHeaderFooter parse(InputStream inputStream) throws XmlException, IOException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(inputStream, CTHeaderFooter.type, (XmlOptions) null);
        }

        public static CTHeaderFooter parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(inputStream, CTHeaderFooter.type, xmlOptions);
        }

        public static CTHeaderFooter parse(Reader reader) throws XmlException, IOException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(reader, CTHeaderFooter.type, (XmlOptions) null);
        }

        public static CTHeaderFooter parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(reader, CTHeaderFooter.type, xmlOptions);
        }

        public static CTHeaderFooter parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(xMLStreamReader, CTHeaderFooter.type, (XmlOptions) null);
        }

        public static CTHeaderFooter parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(xMLStreamReader, CTHeaderFooter.type, xmlOptions);
        }

        public static CTHeaderFooter parse(Node node) throws XmlException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(node, CTHeaderFooter.type, (XmlOptions) null);
        }

        public static CTHeaderFooter parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(node, CTHeaderFooter.type, xmlOptions);
        }

        public static CTHeaderFooter parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(xMLInputStream, CTHeaderFooter.type, (XmlOptions) null);
        }

        public static CTHeaderFooter parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTHeaderFooter) POIXMLTypeLoader.parse(xMLInputStream, CTHeaderFooter.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTHeaderFooter.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTHeaderFooter.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getOddHeader();

    STXstring xgetOddHeader();

    boolean isSetOddHeader();

    void setOddHeader(String str);

    void xsetOddHeader(STXstring sTXstring);

    void unsetOddHeader();

    String getOddFooter();

    STXstring xgetOddFooter();

    boolean isSetOddFooter();

    void setOddFooter(String str);

    void xsetOddFooter(STXstring sTXstring);

    void unsetOddFooter();

    String getEvenHeader();

    STXstring xgetEvenHeader();

    boolean isSetEvenHeader();

    void setEvenHeader(String str);

    void xsetEvenHeader(STXstring sTXstring);

    void unsetEvenHeader();

    String getEvenFooter();

    STXstring xgetEvenFooter();

    boolean isSetEvenFooter();

    void setEvenFooter(String str);

    void xsetEvenFooter(STXstring sTXstring);

    void unsetEvenFooter();

    String getFirstHeader();

    STXstring xgetFirstHeader();

    boolean isSetFirstHeader();

    void setFirstHeader(String str);

    void xsetFirstHeader(STXstring sTXstring);

    void unsetFirstHeader();

    String getFirstFooter();

    STXstring xgetFirstFooter();

    boolean isSetFirstFooter();

    void setFirstFooter(String str);

    void xsetFirstFooter(STXstring sTXstring);

    void unsetFirstFooter();

    boolean getDifferentOddEven();

    XmlBoolean xgetDifferentOddEven();

    boolean isSetDifferentOddEven();

    void setDifferentOddEven(boolean z);

    void xsetDifferentOddEven(XmlBoolean xmlBoolean);

    void unsetDifferentOddEven();

    boolean getDifferentFirst();

    XmlBoolean xgetDifferentFirst();

    boolean isSetDifferentFirst();

    void setDifferentFirst(boolean z);

    void xsetDifferentFirst(XmlBoolean xmlBoolean);

    void unsetDifferentFirst();

    boolean getScaleWithDoc();

    XmlBoolean xgetScaleWithDoc();

    boolean isSetScaleWithDoc();

    void setScaleWithDoc(boolean z);

    void xsetScaleWithDoc(XmlBoolean xmlBoolean);

    void unsetScaleWithDoc();

    boolean getAlignWithMargins();

    XmlBoolean xgetAlignWithMargins();

    boolean isSetAlignWithMargins();

    void setAlignWithMargins(boolean z);

    void xsetAlignWithMargins(XmlBoolean xmlBoolean);

    void unsetAlignWithMargins();
}
