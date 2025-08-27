package com.microsoft.schemas.vml;

import com.microsoft.schemas.vml.STTrueFalse;
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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTH.class */
public interface CTH extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTH.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cth4cbctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTH$Factory.class */
    public static final class Factory {
        public static CTH newInstance() {
            return (CTH) POIXMLTypeLoader.newInstance(CTH.type, null);
        }

        public static CTH newInstance(XmlOptions xmlOptions) {
            return (CTH) POIXMLTypeLoader.newInstance(CTH.type, xmlOptions);
        }

        public static CTH parse(String str) throws XmlException {
            return (CTH) POIXMLTypeLoader.parse(str, CTH.type, (XmlOptions) null);
        }

        public static CTH parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTH) POIXMLTypeLoader.parse(str, CTH.type, xmlOptions);
        }

        public static CTH parse(File file) throws XmlException, IOException {
            return (CTH) POIXMLTypeLoader.parse(file, CTH.type, (XmlOptions) null);
        }

        public static CTH parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTH) POIXMLTypeLoader.parse(file, CTH.type, xmlOptions);
        }

        public static CTH parse(URL url) throws XmlException, IOException {
            return (CTH) POIXMLTypeLoader.parse(url, CTH.type, (XmlOptions) null);
        }

        public static CTH parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTH) POIXMLTypeLoader.parse(url, CTH.type, xmlOptions);
        }

        public static CTH parse(InputStream inputStream) throws XmlException, IOException {
            return (CTH) POIXMLTypeLoader.parse(inputStream, CTH.type, (XmlOptions) null);
        }

        public static CTH parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTH) POIXMLTypeLoader.parse(inputStream, CTH.type, xmlOptions);
        }

        public static CTH parse(Reader reader) throws XmlException, IOException {
            return (CTH) POIXMLTypeLoader.parse(reader, CTH.type, (XmlOptions) null);
        }

        public static CTH parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTH) POIXMLTypeLoader.parse(reader, CTH.type, xmlOptions);
        }

        public static CTH parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTH) POIXMLTypeLoader.parse(xMLStreamReader, CTH.type, (XmlOptions) null);
        }

        public static CTH parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTH) POIXMLTypeLoader.parse(xMLStreamReader, CTH.type, xmlOptions);
        }

        public static CTH parse(Node node) throws XmlException {
            return (CTH) POIXMLTypeLoader.parse(node, CTH.type, (XmlOptions) null);
        }

        public static CTH parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTH) POIXMLTypeLoader.parse(node, CTH.type, xmlOptions);
        }

        public static CTH parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTH) POIXMLTypeLoader.parse(xMLInputStream, CTH.type, (XmlOptions) null);
        }

        public static CTH parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTH) POIXMLTypeLoader.parse(xMLInputStream, CTH.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTH.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTH.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getPosition();

    XmlString xgetPosition();

    boolean isSetPosition();

    void setPosition(String str);

    void xsetPosition(XmlString xmlString);

    void unsetPosition();

    String getPolar();

    XmlString xgetPolar();

    boolean isSetPolar();

    void setPolar(String str);

    void xsetPolar(XmlString xmlString);

    void unsetPolar();

    String getMap();

    XmlString xgetMap();

    boolean isSetMap();

    void setMap(String str);

    void xsetMap(XmlString xmlString);

    void unsetMap();

    STTrueFalse.Enum getInvx();

    STTrueFalse xgetInvx();

    boolean isSetInvx();

    void setInvx(STTrueFalse.Enum r1);

    void xsetInvx(STTrueFalse sTTrueFalse);

    void unsetInvx();

    STTrueFalse.Enum getInvy();

    STTrueFalse xgetInvy();

    boolean isSetInvy();

    void setInvy(STTrueFalse.Enum r1);

    void xsetInvy(STTrueFalse sTTrueFalse);

    void unsetInvy();

    STTrueFalseBlank$Enum getSwitch();

    STTrueFalseBlank xgetSwitch();

    boolean isSetSwitch();

    void setSwitch(STTrueFalseBlank$Enum sTTrueFalseBlank$Enum);

    void xsetSwitch(STTrueFalseBlank sTTrueFalseBlank);

    void unsetSwitch();

    String getXrange();

    XmlString xgetXrange();

    boolean isSetXrange();

    void setXrange(String str);

    void xsetXrange(XmlString xmlString);

    void unsetXrange();

    String getYrange();

    XmlString xgetYrange();

    boolean isSetYrange();

    void setYrange(String str);

    void xsetYrange(XmlString xmlString);

    void unsetYrange();

    String getRadiusrange();

    XmlString xgetRadiusrange();

    boolean isSetRadiusrange();

    void setRadiusrange(String str);

    void xsetRadiusrange(XmlString xmlString);

    void unsetRadiusrange();
}
