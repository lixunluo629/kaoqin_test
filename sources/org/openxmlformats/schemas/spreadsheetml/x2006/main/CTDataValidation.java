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
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDataValidation.class */
public interface CTDataValidation extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTDataValidation.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctdatavalidation9d0ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDataValidation$Factory.class */
    public static final class Factory {
        public static CTDataValidation newInstance() {
            return (CTDataValidation) POIXMLTypeLoader.newInstance(CTDataValidation.type, null);
        }

        public static CTDataValidation newInstance(XmlOptions xmlOptions) {
            return (CTDataValidation) POIXMLTypeLoader.newInstance(CTDataValidation.type, xmlOptions);
        }

        public static CTDataValidation parse(String str) throws XmlException {
            return (CTDataValidation) POIXMLTypeLoader.parse(str, CTDataValidation.type, (XmlOptions) null);
        }

        public static CTDataValidation parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTDataValidation) POIXMLTypeLoader.parse(str, CTDataValidation.type, xmlOptions);
        }

        public static CTDataValidation parse(File file) throws XmlException, IOException {
            return (CTDataValidation) POIXMLTypeLoader.parse(file, CTDataValidation.type, (XmlOptions) null);
        }

        public static CTDataValidation parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataValidation) POIXMLTypeLoader.parse(file, CTDataValidation.type, xmlOptions);
        }

        public static CTDataValidation parse(URL url) throws XmlException, IOException {
            return (CTDataValidation) POIXMLTypeLoader.parse(url, CTDataValidation.type, (XmlOptions) null);
        }

        public static CTDataValidation parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataValidation) POIXMLTypeLoader.parse(url, CTDataValidation.type, xmlOptions);
        }

        public static CTDataValidation parse(InputStream inputStream) throws XmlException, IOException {
            return (CTDataValidation) POIXMLTypeLoader.parse(inputStream, CTDataValidation.type, (XmlOptions) null);
        }

        public static CTDataValidation parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataValidation) POIXMLTypeLoader.parse(inputStream, CTDataValidation.type, xmlOptions);
        }

        public static CTDataValidation parse(Reader reader) throws XmlException, IOException {
            return (CTDataValidation) POIXMLTypeLoader.parse(reader, CTDataValidation.type, (XmlOptions) null);
        }

        public static CTDataValidation parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataValidation) POIXMLTypeLoader.parse(reader, CTDataValidation.type, xmlOptions);
        }

        public static CTDataValidation parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTDataValidation) POIXMLTypeLoader.parse(xMLStreamReader, CTDataValidation.type, (XmlOptions) null);
        }

        public static CTDataValidation parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTDataValidation) POIXMLTypeLoader.parse(xMLStreamReader, CTDataValidation.type, xmlOptions);
        }

        public static CTDataValidation parse(Node node) throws XmlException {
            return (CTDataValidation) POIXMLTypeLoader.parse(node, CTDataValidation.type, (XmlOptions) null);
        }

        public static CTDataValidation parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTDataValidation) POIXMLTypeLoader.parse(node, CTDataValidation.type, xmlOptions);
        }

        public static CTDataValidation parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTDataValidation) POIXMLTypeLoader.parse(xMLInputStream, CTDataValidation.type, (XmlOptions) null);
        }

        public static CTDataValidation parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTDataValidation) POIXMLTypeLoader.parse(xMLInputStream, CTDataValidation.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDataValidation.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDataValidation.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getFormula1();

    STFormula xgetFormula1();

    boolean isSetFormula1();

    void setFormula1(String str);

    void xsetFormula1(STFormula sTFormula);

    void unsetFormula1();

    String getFormula2();

    STFormula xgetFormula2();

    boolean isSetFormula2();

    void setFormula2(String str);

    void xsetFormula2(STFormula sTFormula);

    void unsetFormula2();

    STDataValidationType.Enum getType();

    STDataValidationType xgetType();

    boolean isSetType();

    void setType(STDataValidationType.Enum r1);

    void xsetType(STDataValidationType sTDataValidationType);

    void unsetType();

    STDataValidationErrorStyle.Enum getErrorStyle();

    STDataValidationErrorStyle xgetErrorStyle();

    boolean isSetErrorStyle();

    void setErrorStyle(STDataValidationErrorStyle.Enum r1);

    void xsetErrorStyle(STDataValidationErrorStyle sTDataValidationErrorStyle);

    void unsetErrorStyle();

    STDataValidationImeMode$Enum getImeMode();

    STDataValidationImeMode xgetImeMode();

    boolean isSetImeMode();

    void setImeMode(STDataValidationImeMode$Enum sTDataValidationImeMode$Enum);

    void xsetImeMode(STDataValidationImeMode sTDataValidationImeMode);

    void unsetImeMode();

    STDataValidationOperator.Enum getOperator();

    STDataValidationOperator xgetOperator();

    boolean isSetOperator();

    void setOperator(STDataValidationOperator.Enum r1);

    void xsetOperator(STDataValidationOperator sTDataValidationOperator);

    void unsetOperator();

    boolean getAllowBlank();

    XmlBoolean xgetAllowBlank();

    boolean isSetAllowBlank();

    void setAllowBlank(boolean z);

    void xsetAllowBlank(XmlBoolean xmlBoolean);

    void unsetAllowBlank();

    boolean getShowDropDown();

    XmlBoolean xgetShowDropDown();

    boolean isSetShowDropDown();

    void setShowDropDown(boolean z);

    void xsetShowDropDown(XmlBoolean xmlBoolean);

    void unsetShowDropDown();

    boolean getShowInputMessage();

    XmlBoolean xgetShowInputMessage();

    boolean isSetShowInputMessage();

    void setShowInputMessage(boolean z);

    void xsetShowInputMessage(XmlBoolean xmlBoolean);

    void unsetShowInputMessage();

    boolean getShowErrorMessage();

    XmlBoolean xgetShowErrorMessage();

    boolean isSetShowErrorMessage();

    void setShowErrorMessage(boolean z);

    void xsetShowErrorMessage(XmlBoolean xmlBoolean);

    void unsetShowErrorMessage();

    String getErrorTitle();

    STXstring xgetErrorTitle();

    boolean isSetErrorTitle();

    void setErrorTitle(String str);

    void xsetErrorTitle(STXstring sTXstring);

    void unsetErrorTitle();

    String getError();

    STXstring xgetError();

    boolean isSetError();

    void setError(String str);

    void xsetError(STXstring sTXstring);

    void unsetError();

    String getPromptTitle();

    STXstring xgetPromptTitle();

    boolean isSetPromptTitle();

    void setPromptTitle(String str);

    void xsetPromptTitle(STXstring sTXstring);

    void unsetPromptTitle();

    String getPrompt();

    STXstring xgetPrompt();

    boolean isSetPrompt();

    void setPrompt(String str);

    void xsetPrompt(STXstring sTXstring);

    void unsetPrompt();

    List getSqref();

    STSqref xgetSqref();

    void setSqref(List list);

    void xsetSqref(STSqref sTSqref);
}
