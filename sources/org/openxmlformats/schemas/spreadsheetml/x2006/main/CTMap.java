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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTMap.class */
public interface CTMap extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTMap.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctmap023btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTMap$Factory.class */
    public static final class Factory {
        public static CTMap newInstance() {
            return (CTMap) POIXMLTypeLoader.newInstance(CTMap.type, null);
        }

        public static CTMap newInstance(XmlOptions xmlOptions) {
            return (CTMap) POIXMLTypeLoader.newInstance(CTMap.type, xmlOptions);
        }

        public static CTMap parse(String str) throws XmlException {
            return (CTMap) POIXMLTypeLoader.parse(str, CTMap.type, (XmlOptions) null);
        }

        public static CTMap parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTMap) POIXMLTypeLoader.parse(str, CTMap.type, xmlOptions);
        }

        public static CTMap parse(File file) throws XmlException, IOException {
            return (CTMap) POIXMLTypeLoader.parse(file, CTMap.type, (XmlOptions) null);
        }

        public static CTMap parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMap) POIXMLTypeLoader.parse(file, CTMap.type, xmlOptions);
        }

        public static CTMap parse(URL url) throws XmlException, IOException {
            return (CTMap) POIXMLTypeLoader.parse(url, CTMap.type, (XmlOptions) null);
        }

        public static CTMap parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMap) POIXMLTypeLoader.parse(url, CTMap.type, xmlOptions);
        }

        public static CTMap parse(InputStream inputStream) throws XmlException, IOException {
            return (CTMap) POIXMLTypeLoader.parse(inputStream, CTMap.type, (XmlOptions) null);
        }

        public static CTMap parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMap) POIXMLTypeLoader.parse(inputStream, CTMap.type, xmlOptions);
        }

        public static CTMap parse(Reader reader) throws XmlException, IOException {
            return (CTMap) POIXMLTypeLoader.parse(reader, CTMap.type, (XmlOptions) null);
        }

        public static CTMap parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMap) POIXMLTypeLoader.parse(reader, CTMap.type, xmlOptions);
        }

        public static CTMap parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTMap) POIXMLTypeLoader.parse(xMLStreamReader, CTMap.type, (XmlOptions) null);
        }

        public static CTMap parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTMap) POIXMLTypeLoader.parse(xMLStreamReader, CTMap.type, xmlOptions);
        }

        public static CTMap parse(Node node) throws XmlException {
            return (CTMap) POIXMLTypeLoader.parse(node, CTMap.type, (XmlOptions) null);
        }

        public static CTMap parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTMap) POIXMLTypeLoader.parse(node, CTMap.type, xmlOptions);
        }

        public static CTMap parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTMap) POIXMLTypeLoader.parse(xMLInputStream, CTMap.type, (XmlOptions) null);
        }

        public static CTMap parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTMap) POIXMLTypeLoader.parse(xMLInputStream, CTMap.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTMap.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTMap.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTDataBinding getDataBinding();

    boolean isSetDataBinding();

    void setDataBinding(CTDataBinding cTDataBinding);

    CTDataBinding addNewDataBinding();

    void unsetDataBinding();

    long getID();

    XmlUnsignedInt xgetID();

    void setID(long j);

    void xsetID(XmlUnsignedInt xmlUnsignedInt);

    String getName();

    XmlString xgetName();

    void setName(String str);

    void xsetName(XmlString xmlString);

    String getRootElement();

    XmlString xgetRootElement();

    void setRootElement(String str);

    void xsetRootElement(XmlString xmlString);

    String getSchemaID();

    XmlString xgetSchemaID();

    void setSchemaID(String str);

    void xsetSchemaID(XmlString xmlString);

    boolean getShowImportExportValidationErrors();

    XmlBoolean xgetShowImportExportValidationErrors();

    void setShowImportExportValidationErrors(boolean z);

    void xsetShowImportExportValidationErrors(XmlBoolean xmlBoolean);

    boolean getAutoFit();

    XmlBoolean xgetAutoFit();

    void setAutoFit(boolean z);

    void xsetAutoFit(XmlBoolean xmlBoolean);

    boolean getAppend();

    XmlBoolean xgetAppend();

    void setAppend(boolean z);

    void xsetAppend(XmlBoolean xmlBoolean);

    boolean getPreserveSortAFLayout();

    XmlBoolean xgetPreserveSortAFLayout();

    void setPreserveSortAFLayout(boolean z);

    void xsetPreserveSortAFLayout(XmlBoolean xmlBoolean);

    boolean getPreserveFormat();

    XmlBoolean xgetPreserveFormat();

    void setPreserveFormat(boolean z);

    void xsetPreserveFormat(XmlBoolean xmlBoolean);
}
