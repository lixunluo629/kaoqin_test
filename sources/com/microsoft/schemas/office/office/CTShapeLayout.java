package com.microsoft.schemas.office.office;

import com.microsoft.schemas.vml.STExt;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/CTShapeLayout.class */
public interface CTShapeLayout extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTShapeLayout.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctshapelayoutbda4type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/CTShapeLayout$Factory.class */
    public static final class Factory {
        public static CTShapeLayout newInstance() {
            return (CTShapeLayout) POIXMLTypeLoader.newInstance(CTShapeLayout.type, null);
        }

        public static CTShapeLayout newInstance(XmlOptions xmlOptions) {
            return (CTShapeLayout) POIXMLTypeLoader.newInstance(CTShapeLayout.type, xmlOptions);
        }

        public static CTShapeLayout parse(String str) throws XmlException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(str, CTShapeLayout.type, (XmlOptions) null);
        }

        public static CTShapeLayout parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(str, CTShapeLayout.type, xmlOptions);
        }

        public static CTShapeLayout parse(File file) throws XmlException, IOException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(file, CTShapeLayout.type, (XmlOptions) null);
        }

        public static CTShapeLayout parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(file, CTShapeLayout.type, xmlOptions);
        }

        public static CTShapeLayout parse(URL url) throws XmlException, IOException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(url, CTShapeLayout.type, (XmlOptions) null);
        }

        public static CTShapeLayout parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(url, CTShapeLayout.type, xmlOptions);
        }

        public static CTShapeLayout parse(InputStream inputStream) throws XmlException, IOException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(inputStream, CTShapeLayout.type, (XmlOptions) null);
        }

        public static CTShapeLayout parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(inputStream, CTShapeLayout.type, xmlOptions);
        }

        public static CTShapeLayout parse(Reader reader) throws XmlException, IOException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(reader, CTShapeLayout.type, (XmlOptions) null);
        }

        public static CTShapeLayout parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(reader, CTShapeLayout.type, xmlOptions);
        }

        public static CTShapeLayout parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(xMLStreamReader, CTShapeLayout.type, (XmlOptions) null);
        }

        public static CTShapeLayout parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(xMLStreamReader, CTShapeLayout.type, xmlOptions);
        }

        public static CTShapeLayout parse(Node node) throws XmlException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(node, CTShapeLayout.type, (XmlOptions) null);
        }

        public static CTShapeLayout parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(node, CTShapeLayout.type, xmlOptions);
        }

        public static CTShapeLayout parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(xMLInputStream, CTShapeLayout.type, (XmlOptions) null);
        }

        public static CTShapeLayout parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTShapeLayout) POIXMLTypeLoader.parse(xMLInputStream, CTShapeLayout.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTShapeLayout.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTShapeLayout.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTIdMap getIdmap();

    boolean isSetIdmap();

    void setIdmap(CTIdMap cTIdMap);

    CTIdMap addNewIdmap();

    void unsetIdmap();

    CTRegroupTable getRegrouptable();

    boolean isSetRegrouptable();

    void setRegrouptable(CTRegroupTable cTRegroupTable);

    CTRegroupTable addNewRegrouptable();

    void unsetRegrouptable();

    CTRules getRules();

    boolean isSetRules();

    void setRules(CTRules cTRules);

    CTRules addNewRules();

    void unsetRules();

    STExt.Enum getExt();

    STExt xgetExt();

    boolean isSetExt();

    void setExt(STExt.Enum r1);

    void xsetExt(STExt sTExt);

    void unsetExt();
}
