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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataConsolidateFunction;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDataField.class */
public interface CTDataField extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTDataField.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctdatafield6f0ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDataField$Factory.class */
    public static final class Factory {
        public static CTDataField newInstance() {
            return (CTDataField) POIXMLTypeLoader.newInstance(CTDataField.type, null);
        }

        public static CTDataField newInstance(XmlOptions xmlOptions) {
            return (CTDataField) POIXMLTypeLoader.newInstance(CTDataField.type, xmlOptions);
        }

        public static CTDataField parse(String str) throws XmlException {
            return (CTDataField) POIXMLTypeLoader.parse(str, CTDataField.type, (XmlOptions) null);
        }

        public static CTDataField parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTDataField) POIXMLTypeLoader.parse(str, CTDataField.type, xmlOptions);
        }

        public static CTDataField parse(File file) throws XmlException, IOException {
            return (CTDataField) POIXMLTypeLoader.parse(file, CTDataField.type, (XmlOptions) null);
        }

        public static CTDataField parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataField) POIXMLTypeLoader.parse(file, CTDataField.type, xmlOptions);
        }

        public static CTDataField parse(URL url) throws XmlException, IOException {
            return (CTDataField) POIXMLTypeLoader.parse(url, CTDataField.type, (XmlOptions) null);
        }

        public static CTDataField parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataField) POIXMLTypeLoader.parse(url, CTDataField.type, xmlOptions);
        }

        public static CTDataField parse(InputStream inputStream) throws XmlException, IOException {
            return (CTDataField) POIXMLTypeLoader.parse(inputStream, CTDataField.type, (XmlOptions) null);
        }

        public static CTDataField parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataField) POIXMLTypeLoader.parse(inputStream, CTDataField.type, xmlOptions);
        }

        public static CTDataField parse(Reader reader) throws XmlException, IOException {
            return (CTDataField) POIXMLTypeLoader.parse(reader, CTDataField.type, (XmlOptions) null);
        }

        public static CTDataField parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataField) POIXMLTypeLoader.parse(reader, CTDataField.type, xmlOptions);
        }

        public static CTDataField parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTDataField) POIXMLTypeLoader.parse(xMLStreamReader, CTDataField.type, (XmlOptions) null);
        }

        public static CTDataField parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTDataField) POIXMLTypeLoader.parse(xMLStreamReader, CTDataField.type, xmlOptions);
        }

        public static CTDataField parse(Node node) throws XmlException {
            return (CTDataField) POIXMLTypeLoader.parse(node, CTDataField.type, (XmlOptions) null);
        }

        public static CTDataField parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTDataField) POIXMLTypeLoader.parse(node, CTDataField.type, xmlOptions);
        }

        public static CTDataField parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTDataField) POIXMLTypeLoader.parse(xMLInputStream, CTDataField.type, (XmlOptions) null);
        }

        public static CTDataField parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTDataField) POIXMLTypeLoader.parse(xMLInputStream, CTDataField.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDataField.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDataField.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    String getName();

    STXstring xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    void unsetName();

    long getFld();

    XmlUnsignedInt xgetFld();

    void setFld(long j);

    void xsetFld(XmlUnsignedInt xmlUnsignedInt);

    STDataConsolidateFunction.Enum getSubtotal();

    STDataConsolidateFunction xgetSubtotal();

    boolean isSetSubtotal();

    void setSubtotal(STDataConsolidateFunction.Enum r1);

    void xsetSubtotal(STDataConsolidateFunction sTDataConsolidateFunction);

    void unsetSubtotal();

    STShowDataAs$Enum getShowDataAs();

    STShowDataAs xgetShowDataAs();

    boolean isSetShowDataAs();

    void setShowDataAs(STShowDataAs$Enum sTShowDataAs$Enum);

    void xsetShowDataAs(STShowDataAs sTShowDataAs);

    void unsetShowDataAs();

    int getBaseField();

    XmlInt xgetBaseField();

    boolean isSetBaseField();

    void setBaseField(int i);

    void xsetBaseField(XmlInt xmlInt);

    void unsetBaseField();

    long getBaseItem();

    XmlUnsignedInt xgetBaseItem();

    boolean isSetBaseItem();

    void setBaseItem(long j);

    void xsetBaseItem(XmlUnsignedInt xmlUnsignedInt);

    void unsetBaseItem();

    long getNumFmtId();

    STNumFmtId xgetNumFmtId();

    boolean isSetNumFmtId();

    void setNumFmtId(long j);

    void xsetNumFmtId(STNumFmtId sTNumFmtId);

    void unsetNumFmtId();
}
