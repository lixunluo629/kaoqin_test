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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDataValidations.class */
public interface CTDataValidations extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTDataValidations.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctdatavalidationse46ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDataValidations$Factory.class */
    public static final class Factory {
        public static CTDataValidations newInstance() {
            return (CTDataValidations) POIXMLTypeLoader.newInstance(CTDataValidations.type, null);
        }

        public static CTDataValidations newInstance(XmlOptions xmlOptions) {
            return (CTDataValidations) POIXMLTypeLoader.newInstance(CTDataValidations.type, xmlOptions);
        }

        public static CTDataValidations parse(String str) throws XmlException {
            return (CTDataValidations) POIXMLTypeLoader.parse(str, CTDataValidations.type, (XmlOptions) null);
        }

        public static CTDataValidations parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTDataValidations) POIXMLTypeLoader.parse(str, CTDataValidations.type, xmlOptions);
        }

        public static CTDataValidations parse(File file) throws XmlException, IOException {
            return (CTDataValidations) POIXMLTypeLoader.parse(file, CTDataValidations.type, (XmlOptions) null);
        }

        public static CTDataValidations parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataValidations) POIXMLTypeLoader.parse(file, CTDataValidations.type, xmlOptions);
        }

        public static CTDataValidations parse(URL url) throws XmlException, IOException {
            return (CTDataValidations) POIXMLTypeLoader.parse(url, CTDataValidations.type, (XmlOptions) null);
        }

        public static CTDataValidations parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataValidations) POIXMLTypeLoader.parse(url, CTDataValidations.type, xmlOptions);
        }

        public static CTDataValidations parse(InputStream inputStream) throws XmlException, IOException {
            return (CTDataValidations) POIXMLTypeLoader.parse(inputStream, CTDataValidations.type, (XmlOptions) null);
        }

        public static CTDataValidations parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataValidations) POIXMLTypeLoader.parse(inputStream, CTDataValidations.type, xmlOptions);
        }

        public static CTDataValidations parse(Reader reader) throws XmlException, IOException {
            return (CTDataValidations) POIXMLTypeLoader.parse(reader, CTDataValidations.type, (XmlOptions) null);
        }

        public static CTDataValidations parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDataValidations) POIXMLTypeLoader.parse(reader, CTDataValidations.type, xmlOptions);
        }

        public static CTDataValidations parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTDataValidations) POIXMLTypeLoader.parse(xMLStreamReader, CTDataValidations.type, (XmlOptions) null);
        }

        public static CTDataValidations parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTDataValidations) POIXMLTypeLoader.parse(xMLStreamReader, CTDataValidations.type, xmlOptions);
        }

        public static CTDataValidations parse(Node node) throws XmlException {
            return (CTDataValidations) POIXMLTypeLoader.parse(node, CTDataValidations.type, (XmlOptions) null);
        }

        public static CTDataValidations parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTDataValidations) POIXMLTypeLoader.parse(node, CTDataValidations.type, xmlOptions);
        }

        public static CTDataValidations parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTDataValidations) POIXMLTypeLoader.parse(xMLInputStream, CTDataValidations.type, (XmlOptions) null);
        }

        public static CTDataValidations parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTDataValidations) POIXMLTypeLoader.parse(xMLInputStream, CTDataValidations.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDataValidations.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDataValidations.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTDataValidation> getDataValidationList();

    CTDataValidation[] getDataValidationArray();

    CTDataValidation getDataValidationArray(int i);

    int sizeOfDataValidationArray();

    void setDataValidationArray(CTDataValidation[] cTDataValidationArr);

    void setDataValidationArray(int i, CTDataValidation cTDataValidation);

    CTDataValidation insertNewDataValidation(int i);

    CTDataValidation addNewDataValidation();

    void removeDataValidation(int i);

    boolean getDisablePrompts();

    XmlBoolean xgetDisablePrompts();

    boolean isSetDisablePrompts();

    void setDisablePrompts(boolean z);

    void xsetDisablePrompts(XmlBoolean xmlBoolean);

    void unsetDisablePrompts();

    long getXWindow();

    XmlUnsignedInt xgetXWindow();

    boolean isSetXWindow();

    void setXWindow(long j);

    void xsetXWindow(XmlUnsignedInt xmlUnsignedInt);

    void unsetXWindow();

    long getYWindow();

    XmlUnsignedInt xgetYWindow();

    boolean isSetYWindow();

    void setYWindow(long j);

    void xsetYWindow(XmlUnsignedInt xmlUnsignedInt);

    void unsetYWindow();

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();
}
