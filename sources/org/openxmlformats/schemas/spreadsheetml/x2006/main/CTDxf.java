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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDxf.class */
public interface CTDxf extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTDxf.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctdxfa3b1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDxf$Factory.class */
    public static final class Factory {
        public static CTDxf newInstance() {
            return (CTDxf) POIXMLTypeLoader.newInstance(CTDxf.type, null);
        }

        public static CTDxf newInstance(XmlOptions xmlOptions) {
            return (CTDxf) POIXMLTypeLoader.newInstance(CTDxf.type, xmlOptions);
        }

        public static CTDxf parse(String str) throws XmlException {
            return (CTDxf) POIXMLTypeLoader.parse(str, CTDxf.type, (XmlOptions) null);
        }

        public static CTDxf parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTDxf) POIXMLTypeLoader.parse(str, CTDxf.type, xmlOptions);
        }

        public static CTDxf parse(File file) throws XmlException, IOException {
            return (CTDxf) POIXMLTypeLoader.parse(file, CTDxf.type, (XmlOptions) null);
        }

        public static CTDxf parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDxf) POIXMLTypeLoader.parse(file, CTDxf.type, xmlOptions);
        }

        public static CTDxf parse(URL url) throws XmlException, IOException {
            return (CTDxf) POIXMLTypeLoader.parse(url, CTDxf.type, (XmlOptions) null);
        }

        public static CTDxf parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDxf) POIXMLTypeLoader.parse(url, CTDxf.type, xmlOptions);
        }

        public static CTDxf parse(InputStream inputStream) throws XmlException, IOException {
            return (CTDxf) POIXMLTypeLoader.parse(inputStream, CTDxf.type, (XmlOptions) null);
        }

        public static CTDxf parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDxf) POIXMLTypeLoader.parse(inputStream, CTDxf.type, xmlOptions);
        }

        public static CTDxf parse(Reader reader) throws XmlException, IOException {
            return (CTDxf) POIXMLTypeLoader.parse(reader, CTDxf.type, (XmlOptions) null);
        }

        public static CTDxf parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDxf) POIXMLTypeLoader.parse(reader, CTDxf.type, xmlOptions);
        }

        public static CTDxf parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTDxf) POIXMLTypeLoader.parse(xMLStreamReader, CTDxf.type, (XmlOptions) null);
        }

        public static CTDxf parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTDxf) POIXMLTypeLoader.parse(xMLStreamReader, CTDxf.type, xmlOptions);
        }

        public static CTDxf parse(Node node) throws XmlException {
            return (CTDxf) POIXMLTypeLoader.parse(node, CTDxf.type, (XmlOptions) null);
        }

        public static CTDxf parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTDxf) POIXMLTypeLoader.parse(node, CTDxf.type, xmlOptions);
        }

        public static CTDxf parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTDxf) POIXMLTypeLoader.parse(xMLInputStream, CTDxf.type, (XmlOptions) null);
        }

        public static CTDxf parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTDxf) POIXMLTypeLoader.parse(xMLInputStream, CTDxf.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDxf.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDxf.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTFont getFont();

    boolean isSetFont();

    void setFont(CTFont cTFont);

    CTFont addNewFont();

    void unsetFont();

    CTNumFmt getNumFmt();

    boolean isSetNumFmt();

    void setNumFmt(CTNumFmt cTNumFmt);

    CTNumFmt addNewNumFmt();

    void unsetNumFmt();

    CTFill getFill();

    boolean isSetFill();

    void setFill(CTFill cTFill);

    CTFill addNewFill();

    void unsetFill();

    CTCellAlignment getAlignment();

    boolean isSetAlignment();

    void setAlignment(CTCellAlignment cTCellAlignment);

    CTCellAlignment addNewAlignment();

    void unsetAlignment();

    CTBorder getBorder();

    boolean isSetBorder();

    void setBorder(CTBorder cTBorder);

    CTBorder addNewBorder();

    void unsetBorder();

    CTCellProtection getProtection();

    boolean isSetProtection();

    void setProtection(CTCellProtection cTCellProtection);

    CTCellProtection addNewProtection();

    void unsetProtection();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
