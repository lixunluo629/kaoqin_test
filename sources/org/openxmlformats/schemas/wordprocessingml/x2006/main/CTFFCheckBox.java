package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTFFCheckBox.class */
public interface CTFFCheckBox extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTFFCheckBox.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctffcheckboxf3a5type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTFFCheckBox$Factory.class */
    public static final class Factory {
        public static CTFFCheckBox newInstance() {
            return (CTFFCheckBox) POIXMLTypeLoader.newInstance(CTFFCheckBox.type, null);
        }

        public static CTFFCheckBox newInstance(XmlOptions xmlOptions) {
            return (CTFFCheckBox) POIXMLTypeLoader.newInstance(CTFFCheckBox.type, xmlOptions);
        }

        public static CTFFCheckBox parse(String str) throws XmlException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(str, CTFFCheckBox.type, (XmlOptions) null);
        }

        public static CTFFCheckBox parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(str, CTFFCheckBox.type, xmlOptions);
        }

        public static CTFFCheckBox parse(File file) throws XmlException, IOException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(file, CTFFCheckBox.type, (XmlOptions) null);
        }

        public static CTFFCheckBox parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(file, CTFFCheckBox.type, xmlOptions);
        }

        public static CTFFCheckBox parse(URL url) throws XmlException, IOException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(url, CTFFCheckBox.type, (XmlOptions) null);
        }

        public static CTFFCheckBox parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(url, CTFFCheckBox.type, xmlOptions);
        }

        public static CTFFCheckBox parse(InputStream inputStream) throws XmlException, IOException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(inputStream, CTFFCheckBox.type, (XmlOptions) null);
        }

        public static CTFFCheckBox parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(inputStream, CTFFCheckBox.type, xmlOptions);
        }

        public static CTFFCheckBox parse(Reader reader) throws XmlException, IOException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(reader, CTFFCheckBox.type, (XmlOptions) null);
        }

        public static CTFFCheckBox parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(reader, CTFFCheckBox.type, xmlOptions);
        }

        public static CTFFCheckBox parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(xMLStreamReader, CTFFCheckBox.type, (XmlOptions) null);
        }

        public static CTFFCheckBox parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(xMLStreamReader, CTFFCheckBox.type, xmlOptions);
        }

        public static CTFFCheckBox parse(Node node) throws XmlException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(node, CTFFCheckBox.type, (XmlOptions) null);
        }

        public static CTFFCheckBox parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(node, CTFFCheckBox.type, xmlOptions);
        }

        public static CTFFCheckBox parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(xMLInputStream, CTFFCheckBox.type, (XmlOptions) null);
        }

        public static CTFFCheckBox parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTFFCheckBox) POIXMLTypeLoader.parse(xMLInputStream, CTFFCheckBox.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFFCheckBox.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFFCheckBox.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTHpsMeasure getSize();

    boolean isSetSize();

    void setSize(CTHpsMeasure cTHpsMeasure);

    CTHpsMeasure addNewSize();

    void unsetSize();

    CTOnOff getSizeAuto();

    boolean isSetSizeAuto();

    void setSizeAuto(CTOnOff cTOnOff);

    CTOnOff addNewSizeAuto();

    void unsetSizeAuto();

    CTOnOff getDefault();

    boolean isSetDefault();

    void setDefault(CTOnOff cTOnOff);

    CTOnOff addNewDefault();

    void unsetDefault();

    CTOnOff getChecked();

    boolean isSetChecked();

    void setChecked(CTOnOff cTOnOff);

    CTOnOff addNewChecked();

    void unsetChecked();
}
