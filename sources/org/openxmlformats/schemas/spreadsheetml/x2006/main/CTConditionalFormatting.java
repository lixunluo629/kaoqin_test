package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTConditionalFormatting.class */
public interface CTConditionalFormatting extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTConditionalFormatting.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctconditionalformatting0deatype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTConditionalFormatting$Factory.class */
    public static final class Factory {
        public static CTConditionalFormatting newInstance() {
            return (CTConditionalFormatting) POIXMLTypeLoader.newInstance(CTConditionalFormatting.type, null);
        }

        public static CTConditionalFormatting newInstance(XmlOptions xmlOptions) {
            return (CTConditionalFormatting) POIXMLTypeLoader.newInstance(CTConditionalFormatting.type, xmlOptions);
        }

        public static CTConditionalFormatting parse(String str) throws XmlException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(str, CTConditionalFormatting.type, (XmlOptions) null);
        }

        public static CTConditionalFormatting parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(str, CTConditionalFormatting.type, xmlOptions);
        }

        public static CTConditionalFormatting parse(File file) throws XmlException, IOException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(file, CTConditionalFormatting.type, (XmlOptions) null);
        }

        public static CTConditionalFormatting parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(file, CTConditionalFormatting.type, xmlOptions);
        }

        public static CTConditionalFormatting parse(URL url) throws XmlException, IOException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(url, CTConditionalFormatting.type, (XmlOptions) null);
        }

        public static CTConditionalFormatting parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(url, CTConditionalFormatting.type, xmlOptions);
        }

        public static CTConditionalFormatting parse(InputStream inputStream) throws XmlException, IOException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(inputStream, CTConditionalFormatting.type, (XmlOptions) null);
        }

        public static CTConditionalFormatting parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(inputStream, CTConditionalFormatting.type, xmlOptions);
        }

        public static CTConditionalFormatting parse(Reader reader) throws XmlException, IOException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(reader, CTConditionalFormatting.type, (XmlOptions) null);
        }

        public static CTConditionalFormatting parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(reader, CTConditionalFormatting.type, xmlOptions);
        }

        public static CTConditionalFormatting parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(xMLStreamReader, CTConditionalFormatting.type, (XmlOptions) null);
        }

        public static CTConditionalFormatting parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(xMLStreamReader, CTConditionalFormatting.type, xmlOptions);
        }

        public static CTConditionalFormatting parse(Node node) throws XmlException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(node, CTConditionalFormatting.type, (XmlOptions) null);
        }

        public static CTConditionalFormatting parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(node, CTConditionalFormatting.type, xmlOptions);
        }

        public static CTConditionalFormatting parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(xMLInputStream, CTConditionalFormatting.type, (XmlOptions) null);
        }

        public static CTConditionalFormatting parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTConditionalFormatting) POIXMLTypeLoader.parse(xMLInputStream, CTConditionalFormatting.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTConditionalFormatting.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTConditionalFormatting.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTCfRule> getCfRuleList();

    CTCfRule[] getCfRuleArray();

    CTCfRule getCfRuleArray(int i);

    int sizeOfCfRuleArray();

    void setCfRuleArray(CTCfRule[] cTCfRuleArr);

    void setCfRuleArray(int i, CTCfRule cTCfRule);

    CTCfRule insertNewCfRule(int i);

    CTCfRule addNewCfRule();

    void removeCfRule(int i);

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    boolean getPivot();

    XmlBoolean xgetPivot();

    boolean isSetPivot();

    void setPivot(boolean z);

    void xsetPivot(XmlBoolean xmlBoolean);

    void unsetPivot();

    List getSqref();

    STSqref xgetSqref();

    boolean isSetSqref();

    void setSqref(List list);

    void xsetSqref(STSqref sTSqref);

    void unsetSqref();
}
