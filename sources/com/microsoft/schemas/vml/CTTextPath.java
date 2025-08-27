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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTTextPath.class */
public interface CTTextPath extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextPath.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextpath14f0type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTTextPath$Factory.class */
    public static final class Factory {
        public static CTTextPath newInstance() {
            return (CTTextPath) POIXMLTypeLoader.newInstance(CTTextPath.type, null);
        }

        public static CTTextPath newInstance(XmlOptions xmlOptions) {
            return (CTTextPath) POIXMLTypeLoader.newInstance(CTTextPath.type, xmlOptions);
        }

        public static CTTextPath parse(String str) throws XmlException {
            return (CTTextPath) POIXMLTypeLoader.parse(str, CTTextPath.type, (XmlOptions) null);
        }

        public static CTTextPath parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextPath) POIXMLTypeLoader.parse(str, CTTextPath.type, xmlOptions);
        }

        public static CTTextPath parse(File file) throws XmlException, IOException {
            return (CTTextPath) POIXMLTypeLoader.parse(file, CTTextPath.type, (XmlOptions) null);
        }

        public static CTTextPath parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextPath) POIXMLTypeLoader.parse(file, CTTextPath.type, xmlOptions);
        }

        public static CTTextPath parse(URL url) throws XmlException, IOException {
            return (CTTextPath) POIXMLTypeLoader.parse(url, CTTextPath.type, (XmlOptions) null);
        }

        public static CTTextPath parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextPath) POIXMLTypeLoader.parse(url, CTTextPath.type, xmlOptions);
        }

        public static CTTextPath parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextPath) POIXMLTypeLoader.parse(inputStream, CTTextPath.type, (XmlOptions) null);
        }

        public static CTTextPath parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextPath) POIXMLTypeLoader.parse(inputStream, CTTextPath.type, xmlOptions);
        }

        public static CTTextPath parse(Reader reader) throws XmlException, IOException {
            return (CTTextPath) POIXMLTypeLoader.parse(reader, CTTextPath.type, (XmlOptions) null);
        }

        public static CTTextPath parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextPath) POIXMLTypeLoader.parse(reader, CTTextPath.type, xmlOptions);
        }

        public static CTTextPath parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextPath) POIXMLTypeLoader.parse(xMLStreamReader, CTTextPath.type, (XmlOptions) null);
        }

        public static CTTextPath parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextPath) POIXMLTypeLoader.parse(xMLStreamReader, CTTextPath.type, xmlOptions);
        }

        public static CTTextPath parse(Node node) throws XmlException {
            return (CTTextPath) POIXMLTypeLoader.parse(node, CTTextPath.type, (XmlOptions) null);
        }

        public static CTTextPath parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextPath) POIXMLTypeLoader.parse(node, CTTextPath.type, xmlOptions);
        }

        public static CTTextPath parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextPath) POIXMLTypeLoader.parse(xMLInputStream, CTTextPath.type, (XmlOptions) null);
        }

        public static CTTextPath parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextPath) POIXMLTypeLoader.parse(xMLInputStream, CTTextPath.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextPath.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextPath.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getId();

    XmlString xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlString xmlString);

    void unsetId();

    String getStyle();

    XmlString xgetStyle();

    boolean isSetStyle();

    void setStyle(String str);

    void xsetStyle(XmlString xmlString);

    void unsetStyle();

    STTrueFalse.Enum getOn();

    STTrueFalse xgetOn();

    boolean isSetOn();

    void setOn(STTrueFalse.Enum r1);

    void xsetOn(STTrueFalse sTTrueFalse);

    void unsetOn();

    STTrueFalse.Enum getFitshape();

    STTrueFalse xgetFitshape();

    boolean isSetFitshape();

    void setFitshape(STTrueFalse.Enum r1);

    void xsetFitshape(STTrueFalse sTTrueFalse);

    void unsetFitshape();

    STTrueFalse.Enum getFitpath();

    STTrueFalse xgetFitpath();

    boolean isSetFitpath();

    void setFitpath(STTrueFalse.Enum r1);

    void xsetFitpath(STTrueFalse sTTrueFalse);

    void unsetFitpath();

    STTrueFalse.Enum getTrim();

    STTrueFalse xgetTrim();

    boolean isSetTrim();

    void setTrim(STTrueFalse.Enum r1);

    void xsetTrim(STTrueFalse sTTrueFalse);

    void unsetTrim();

    STTrueFalse.Enum getXscale();

    STTrueFalse xgetXscale();

    boolean isSetXscale();

    void setXscale(STTrueFalse.Enum r1);

    void xsetXscale(STTrueFalse sTTrueFalse);

    void unsetXscale();

    String getString();

    XmlString xgetString();

    boolean isSetString();

    void setString(String str);

    void xsetString(XmlString xmlString);

    void unsetString();
}
