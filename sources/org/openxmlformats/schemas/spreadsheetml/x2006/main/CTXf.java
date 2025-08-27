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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTXf.class */
public interface CTXf extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTXf.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctxf97f7type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTXf$Factory.class */
    public static final class Factory {
        public static CTXf newInstance() {
            return (CTXf) POIXMLTypeLoader.newInstance(CTXf.type, null);
        }

        public static CTXf newInstance(XmlOptions xmlOptions) {
            return (CTXf) POIXMLTypeLoader.newInstance(CTXf.type, xmlOptions);
        }

        public static CTXf parse(String str) throws XmlException {
            return (CTXf) POIXMLTypeLoader.parse(str, CTXf.type, (XmlOptions) null);
        }

        public static CTXf parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTXf) POIXMLTypeLoader.parse(str, CTXf.type, xmlOptions);
        }

        public static CTXf parse(File file) throws XmlException, IOException {
            return (CTXf) POIXMLTypeLoader.parse(file, CTXf.type, (XmlOptions) null);
        }

        public static CTXf parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXf) POIXMLTypeLoader.parse(file, CTXf.type, xmlOptions);
        }

        public static CTXf parse(URL url) throws XmlException, IOException {
            return (CTXf) POIXMLTypeLoader.parse(url, CTXf.type, (XmlOptions) null);
        }

        public static CTXf parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXf) POIXMLTypeLoader.parse(url, CTXf.type, xmlOptions);
        }

        public static CTXf parse(InputStream inputStream) throws XmlException, IOException {
            return (CTXf) POIXMLTypeLoader.parse(inputStream, CTXf.type, (XmlOptions) null);
        }

        public static CTXf parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXf) POIXMLTypeLoader.parse(inputStream, CTXf.type, xmlOptions);
        }

        public static CTXf parse(Reader reader) throws XmlException, IOException {
            return (CTXf) POIXMLTypeLoader.parse(reader, CTXf.type, (XmlOptions) null);
        }

        public static CTXf parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXf) POIXMLTypeLoader.parse(reader, CTXf.type, xmlOptions);
        }

        public static CTXf parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTXf) POIXMLTypeLoader.parse(xMLStreamReader, CTXf.type, (XmlOptions) null);
        }

        public static CTXf parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTXf) POIXMLTypeLoader.parse(xMLStreamReader, CTXf.type, xmlOptions);
        }

        public static CTXf parse(Node node) throws XmlException {
            return (CTXf) POIXMLTypeLoader.parse(node, CTXf.type, (XmlOptions) null);
        }

        public static CTXf parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTXf) POIXMLTypeLoader.parse(node, CTXf.type, xmlOptions);
        }

        public static CTXf parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTXf) POIXMLTypeLoader.parse(xMLInputStream, CTXf.type, (XmlOptions) null);
        }

        public static CTXf parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTXf) POIXMLTypeLoader.parse(xMLInputStream, CTXf.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTXf.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTXf.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTCellAlignment getAlignment();

    boolean isSetAlignment();

    void setAlignment(CTCellAlignment cTCellAlignment);

    CTCellAlignment addNewAlignment();

    void unsetAlignment();

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

    long getNumFmtId();

    STNumFmtId xgetNumFmtId();

    boolean isSetNumFmtId();

    void setNumFmtId(long j);

    void xsetNumFmtId(STNumFmtId sTNumFmtId);

    void unsetNumFmtId();

    long getFontId();

    STFontId xgetFontId();

    boolean isSetFontId();

    void setFontId(long j);

    void xsetFontId(STFontId sTFontId);

    void unsetFontId();

    long getFillId();

    STFillId xgetFillId();

    boolean isSetFillId();

    void setFillId(long j);

    void xsetFillId(STFillId sTFillId);

    void unsetFillId();

    long getBorderId();

    STBorderId xgetBorderId();

    boolean isSetBorderId();

    void setBorderId(long j);

    void xsetBorderId(STBorderId sTBorderId);

    void unsetBorderId();

    long getXfId();

    STCellStyleXfId xgetXfId();

    boolean isSetXfId();

    void setXfId(long j);

    void xsetXfId(STCellStyleXfId sTCellStyleXfId);

    void unsetXfId();

    boolean getQuotePrefix();

    XmlBoolean xgetQuotePrefix();

    boolean isSetQuotePrefix();

    void setQuotePrefix(boolean z);

    void xsetQuotePrefix(XmlBoolean xmlBoolean);

    void unsetQuotePrefix();

    boolean getPivotButton();

    XmlBoolean xgetPivotButton();

    boolean isSetPivotButton();

    void setPivotButton(boolean z);

    void xsetPivotButton(XmlBoolean xmlBoolean);

    void unsetPivotButton();

    boolean getApplyNumberFormat();

    XmlBoolean xgetApplyNumberFormat();

    boolean isSetApplyNumberFormat();

    void setApplyNumberFormat(boolean z);

    void xsetApplyNumberFormat(XmlBoolean xmlBoolean);

    void unsetApplyNumberFormat();

    boolean getApplyFont();

    XmlBoolean xgetApplyFont();

    boolean isSetApplyFont();

    void setApplyFont(boolean z);

    void xsetApplyFont(XmlBoolean xmlBoolean);

    void unsetApplyFont();

    boolean getApplyFill();

    XmlBoolean xgetApplyFill();

    boolean isSetApplyFill();

    void setApplyFill(boolean z);

    void xsetApplyFill(XmlBoolean xmlBoolean);

    void unsetApplyFill();

    boolean getApplyBorder();

    XmlBoolean xgetApplyBorder();

    boolean isSetApplyBorder();

    void setApplyBorder(boolean z);

    void xsetApplyBorder(XmlBoolean xmlBoolean);

    void unsetApplyBorder();

    boolean getApplyAlignment();

    XmlBoolean xgetApplyAlignment();

    boolean isSetApplyAlignment();

    void setApplyAlignment(boolean z);

    void xsetApplyAlignment(XmlBoolean xmlBoolean);

    void unsetApplyAlignment();

    boolean getApplyProtection();

    XmlBoolean xgetApplyProtection();

    boolean isSetApplyProtection();

    void setApplyProtection(boolean z);

    void xsetApplyProtection(XmlBoolean xmlBoolean);

    void unsetApplyProtection();
}
