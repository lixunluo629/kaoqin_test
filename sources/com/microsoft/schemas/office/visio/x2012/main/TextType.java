package com.microsoft.schemas.office.visio.x2012.main;

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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/TextType.class */
public interface TextType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(TextType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("texttyped2ectype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/TextType$Factory.class */
    public static final class Factory {
        public static TextType newInstance() {
            return (TextType) POIXMLTypeLoader.newInstance(TextType.type, null);
        }

        public static TextType newInstance(XmlOptions xmlOptions) {
            return (TextType) POIXMLTypeLoader.newInstance(TextType.type, xmlOptions);
        }

        public static TextType parse(String str) throws XmlException {
            return (TextType) POIXMLTypeLoader.parse(str, TextType.type, (XmlOptions) null);
        }

        public static TextType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (TextType) POIXMLTypeLoader.parse(str, TextType.type, xmlOptions);
        }

        public static TextType parse(File file) throws XmlException, IOException {
            return (TextType) POIXMLTypeLoader.parse(file, TextType.type, (XmlOptions) null);
        }

        public static TextType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (TextType) POIXMLTypeLoader.parse(file, TextType.type, xmlOptions);
        }

        public static TextType parse(URL url) throws XmlException, IOException {
            return (TextType) POIXMLTypeLoader.parse(url, TextType.type, (XmlOptions) null);
        }

        public static TextType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (TextType) POIXMLTypeLoader.parse(url, TextType.type, xmlOptions);
        }

        public static TextType parse(InputStream inputStream) throws XmlException, IOException {
            return (TextType) POIXMLTypeLoader.parse(inputStream, TextType.type, (XmlOptions) null);
        }

        public static TextType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (TextType) POIXMLTypeLoader.parse(inputStream, TextType.type, xmlOptions);
        }

        public static TextType parse(Reader reader) throws XmlException, IOException {
            return (TextType) POIXMLTypeLoader.parse(reader, TextType.type, (XmlOptions) null);
        }

        public static TextType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (TextType) POIXMLTypeLoader.parse(reader, TextType.type, xmlOptions);
        }

        public static TextType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (TextType) POIXMLTypeLoader.parse(xMLStreamReader, TextType.type, (XmlOptions) null);
        }

        public static TextType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (TextType) POIXMLTypeLoader.parse(xMLStreamReader, TextType.type, xmlOptions);
        }

        public static TextType parse(Node node) throws XmlException {
            return (TextType) POIXMLTypeLoader.parse(node, TextType.type, (XmlOptions) null);
        }

        public static TextType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (TextType) POIXMLTypeLoader.parse(node, TextType.type, xmlOptions);
        }

        public static TextType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (TextType) POIXMLTypeLoader.parse(xMLInputStream, TextType.type, (XmlOptions) null);
        }

        public static TextType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (TextType) POIXMLTypeLoader.parse(xMLInputStream, TextType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, TextType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, TextType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CpType> getCpList();

    CpType[] getCpArray();

    CpType getCpArray(int i);

    int sizeOfCpArray();

    void setCpArray(CpType[] cpTypeArr);

    void setCpArray(int i, CpType cpType);

    CpType insertNewCp(int i);

    CpType addNewCp();

    void removeCp(int i);

    List<PpType> getPpList();

    PpType[] getPpArray();

    PpType getPpArray(int i);

    int sizeOfPpArray();

    void setPpArray(PpType[] ppTypeArr);

    void setPpArray(int i, PpType ppType);

    PpType insertNewPp(int i);

    PpType addNewPp();

    void removePp(int i);

    List<TpType> getTpList();

    TpType[] getTpArray();

    TpType getTpArray(int i);

    int sizeOfTpArray();

    void setTpArray(TpType[] tpTypeArr);

    void setTpArray(int i, TpType tpType);

    TpType insertNewTp(int i);

    TpType addNewTp();

    void removeTp(int i);

    List<FldType> getFldList();

    FldType[] getFldArray();

    FldType getFldArray(int i);

    int sizeOfFldArray();

    void setFldArray(FldType[] fldTypeArr);

    void setFldArray(int i, FldType fldType);

    FldType insertNewFld(int i);

    FldType addNewFld();

    void removeFld(int i);
}
