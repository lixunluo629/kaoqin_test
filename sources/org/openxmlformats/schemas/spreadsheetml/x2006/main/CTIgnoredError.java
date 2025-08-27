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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTIgnoredError.class */
public interface CTIgnoredError extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTIgnoredError.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctignorederrorc51ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTIgnoredError$Factory.class */
    public static final class Factory {
        public static CTIgnoredError newInstance() {
            return (CTIgnoredError) POIXMLTypeLoader.newInstance(CTIgnoredError.type, null);
        }

        public static CTIgnoredError newInstance(XmlOptions xmlOptions) {
            return (CTIgnoredError) POIXMLTypeLoader.newInstance(CTIgnoredError.type, xmlOptions);
        }

        public static CTIgnoredError parse(String str) throws XmlException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(str, CTIgnoredError.type, (XmlOptions) null);
        }

        public static CTIgnoredError parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(str, CTIgnoredError.type, xmlOptions);
        }

        public static CTIgnoredError parse(File file) throws XmlException, IOException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(file, CTIgnoredError.type, (XmlOptions) null);
        }

        public static CTIgnoredError parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(file, CTIgnoredError.type, xmlOptions);
        }

        public static CTIgnoredError parse(URL url) throws XmlException, IOException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(url, CTIgnoredError.type, (XmlOptions) null);
        }

        public static CTIgnoredError parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(url, CTIgnoredError.type, xmlOptions);
        }

        public static CTIgnoredError parse(InputStream inputStream) throws XmlException, IOException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(inputStream, CTIgnoredError.type, (XmlOptions) null);
        }

        public static CTIgnoredError parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(inputStream, CTIgnoredError.type, xmlOptions);
        }

        public static CTIgnoredError parse(Reader reader) throws XmlException, IOException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(reader, CTIgnoredError.type, (XmlOptions) null);
        }

        public static CTIgnoredError parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(reader, CTIgnoredError.type, xmlOptions);
        }

        public static CTIgnoredError parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(xMLStreamReader, CTIgnoredError.type, (XmlOptions) null);
        }

        public static CTIgnoredError parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(xMLStreamReader, CTIgnoredError.type, xmlOptions);
        }

        public static CTIgnoredError parse(Node node) throws XmlException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(node, CTIgnoredError.type, (XmlOptions) null);
        }

        public static CTIgnoredError parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(node, CTIgnoredError.type, xmlOptions);
        }

        public static CTIgnoredError parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(xMLInputStream, CTIgnoredError.type, (XmlOptions) null);
        }

        public static CTIgnoredError parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTIgnoredError) POIXMLTypeLoader.parse(xMLInputStream, CTIgnoredError.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTIgnoredError.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTIgnoredError.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List getSqref();

    STSqref xgetSqref();

    void setSqref(List list);

    void xsetSqref(STSqref sTSqref);

    boolean getEvalError();

    XmlBoolean xgetEvalError();

    boolean isSetEvalError();

    void setEvalError(boolean z);

    void xsetEvalError(XmlBoolean xmlBoolean);

    void unsetEvalError();

    boolean getTwoDigitTextYear();

    XmlBoolean xgetTwoDigitTextYear();

    boolean isSetTwoDigitTextYear();

    void setTwoDigitTextYear(boolean z);

    void xsetTwoDigitTextYear(XmlBoolean xmlBoolean);

    void unsetTwoDigitTextYear();

    boolean getNumberStoredAsText();

    XmlBoolean xgetNumberStoredAsText();

    boolean isSetNumberStoredAsText();

    void setNumberStoredAsText(boolean z);

    void xsetNumberStoredAsText(XmlBoolean xmlBoolean);

    void unsetNumberStoredAsText();

    boolean getFormula();

    XmlBoolean xgetFormula();

    boolean isSetFormula();

    void setFormula(boolean z);

    void xsetFormula(XmlBoolean xmlBoolean);

    void unsetFormula();

    boolean getFormulaRange();

    XmlBoolean xgetFormulaRange();

    boolean isSetFormulaRange();

    void setFormulaRange(boolean z);

    void xsetFormulaRange(XmlBoolean xmlBoolean);

    void unsetFormulaRange();

    boolean getUnlockedFormula();

    XmlBoolean xgetUnlockedFormula();

    boolean isSetUnlockedFormula();

    void setUnlockedFormula(boolean z);

    void xsetUnlockedFormula(XmlBoolean xmlBoolean);

    void unsetUnlockedFormula();

    boolean getEmptyCellReference();

    XmlBoolean xgetEmptyCellReference();

    boolean isSetEmptyCellReference();

    void setEmptyCellReference(boolean z);

    void xsetEmptyCellReference(XmlBoolean xmlBoolean);

    void unsetEmptyCellReference();

    boolean getListDataValidation();

    XmlBoolean xgetListDataValidation();

    boolean isSetListDataValidation();

    void setListDataValidation(boolean z);

    void xsetListDataValidation(XmlBoolean xmlBoolean);

    void unsetListDataValidation();

    boolean getCalculatedColumn();

    XmlBoolean xgetCalculatedColumn();

    boolean isSetCalculatedColumn();

    void setCalculatedColumn(boolean z);

    void xsetCalculatedColumn(XmlBoolean xmlBoolean);

    void unsetCalculatedColumn();
}
