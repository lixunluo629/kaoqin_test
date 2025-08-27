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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTFldChar.class */
public interface CTFldChar extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTFldChar.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctfldchare83etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTFldChar$Factory.class */
    public static final class Factory {
        public static CTFldChar newInstance() {
            return (CTFldChar) POIXMLTypeLoader.newInstance(CTFldChar.type, null);
        }

        public static CTFldChar newInstance(XmlOptions xmlOptions) {
            return (CTFldChar) POIXMLTypeLoader.newInstance(CTFldChar.type, xmlOptions);
        }

        public static CTFldChar parse(String str) throws XmlException {
            return (CTFldChar) POIXMLTypeLoader.parse(str, CTFldChar.type, (XmlOptions) null);
        }

        public static CTFldChar parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTFldChar) POIXMLTypeLoader.parse(str, CTFldChar.type, xmlOptions);
        }

        public static CTFldChar parse(File file) throws XmlException, IOException {
            return (CTFldChar) POIXMLTypeLoader.parse(file, CTFldChar.type, (XmlOptions) null);
        }

        public static CTFldChar parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFldChar) POIXMLTypeLoader.parse(file, CTFldChar.type, xmlOptions);
        }

        public static CTFldChar parse(URL url) throws XmlException, IOException {
            return (CTFldChar) POIXMLTypeLoader.parse(url, CTFldChar.type, (XmlOptions) null);
        }

        public static CTFldChar parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFldChar) POIXMLTypeLoader.parse(url, CTFldChar.type, xmlOptions);
        }

        public static CTFldChar parse(InputStream inputStream) throws XmlException, IOException {
            return (CTFldChar) POIXMLTypeLoader.parse(inputStream, CTFldChar.type, (XmlOptions) null);
        }

        public static CTFldChar parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFldChar) POIXMLTypeLoader.parse(inputStream, CTFldChar.type, xmlOptions);
        }

        public static CTFldChar parse(Reader reader) throws XmlException, IOException {
            return (CTFldChar) POIXMLTypeLoader.parse(reader, CTFldChar.type, (XmlOptions) null);
        }

        public static CTFldChar parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFldChar) POIXMLTypeLoader.parse(reader, CTFldChar.type, xmlOptions);
        }

        public static CTFldChar parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTFldChar) POIXMLTypeLoader.parse(xMLStreamReader, CTFldChar.type, (XmlOptions) null);
        }

        public static CTFldChar parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTFldChar) POIXMLTypeLoader.parse(xMLStreamReader, CTFldChar.type, xmlOptions);
        }

        public static CTFldChar parse(Node node) throws XmlException {
            return (CTFldChar) POIXMLTypeLoader.parse(node, CTFldChar.type, (XmlOptions) null);
        }

        public static CTFldChar parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTFldChar) POIXMLTypeLoader.parse(node, CTFldChar.type, xmlOptions);
        }

        public static CTFldChar parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTFldChar) POIXMLTypeLoader.parse(xMLInputStream, CTFldChar.type, (XmlOptions) null);
        }

        public static CTFldChar parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTFldChar) POIXMLTypeLoader.parse(xMLInputStream, CTFldChar.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFldChar.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFldChar.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTText getFldData();

    boolean isSetFldData();

    void setFldData(CTText cTText);

    CTText addNewFldData();

    void unsetFldData();

    CTFFData getFfData();

    boolean isSetFfData();

    void setFfData(CTFFData cTFFData);

    CTFFData addNewFfData();

    void unsetFfData();

    CTTrackChangeNumbering getNumberingChange();

    boolean isSetNumberingChange();

    void setNumberingChange(CTTrackChangeNumbering cTTrackChangeNumbering);

    CTTrackChangeNumbering addNewNumberingChange();

    void unsetNumberingChange();

    STFldCharType.Enum getFldCharType();

    STFldCharType xgetFldCharType();

    void setFldCharType(STFldCharType.Enum r1);

    void xsetFldCharType(STFldCharType sTFldCharType);

    STOnOff.Enum getFldLock();

    STOnOff xgetFldLock();

    boolean isSetFldLock();

    void setFldLock(STOnOff.Enum r1);

    void xsetFldLock(STOnOff sTOnOff);

    void unsetFldLock();

    STOnOff.Enum getDirty();

    STOnOff xgetDirty();

    boolean isSetDirty();

    void setDirty(STOnOff.Enum r1);

    void xsetDirty(STOnOff sTOnOff);

    void unsetDirty();
}
