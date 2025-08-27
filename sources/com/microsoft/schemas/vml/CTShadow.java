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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTShadow.class */
public interface CTShadow extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTShadow.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctshadowdfdetype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTShadow$Factory.class */
    public static final class Factory {
        public static CTShadow newInstance() {
            return (CTShadow) POIXMLTypeLoader.newInstance(CTShadow.type, null);
        }

        public static CTShadow newInstance(XmlOptions xmlOptions) {
            return (CTShadow) POIXMLTypeLoader.newInstance(CTShadow.type, xmlOptions);
        }

        public static CTShadow parse(String str) throws XmlException {
            return (CTShadow) POIXMLTypeLoader.parse(str, CTShadow.type, (XmlOptions) null);
        }

        public static CTShadow parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTShadow) POIXMLTypeLoader.parse(str, CTShadow.type, xmlOptions);
        }

        public static CTShadow parse(File file) throws XmlException, IOException {
            return (CTShadow) POIXMLTypeLoader.parse(file, CTShadow.type, (XmlOptions) null);
        }

        public static CTShadow parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShadow) POIXMLTypeLoader.parse(file, CTShadow.type, xmlOptions);
        }

        public static CTShadow parse(URL url) throws XmlException, IOException {
            return (CTShadow) POIXMLTypeLoader.parse(url, CTShadow.type, (XmlOptions) null);
        }

        public static CTShadow parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShadow) POIXMLTypeLoader.parse(url, CTShadow.type, xmlOptions);
        }

        public static CTShadow parse(InputStream inputStream) throws XmlException, IOException {
            return (CTShadow) POIXMLTypeLoader.parse(inputStream, CTShadow.type, (XmlOptions) null);
        }

        public static CTShadow parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShadow) POIXMLTypeLoader.parse(inputStream, CTShadow.type, xmlOptions);
        }

        public static CTShadow parse(Reader reader) throws XmlException, IOException {
            return (CTShadow) POIXMLTypeLoader.parse(reader, CTShadow.type, (XmlOptions) null);
        }

        public static CTShadow parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTShadow) POIXMLTypeLoader.parse(reader, CTShadow.type, xmlOptions);
        }

        public static CTShadow parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTShadow) POIXMLTypeLoader.parse(xMLStreamReader, CTShadow.type, (XmlOptions) null);
        }

        public static CTShadow parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTShadow) POIXMLTypeLoader.parse(xMLStreamReader, CTShadow.type, xmlOptions);
        }

        public static CTShadow parse(Node node) throws XmlException {
            return (CTShadow) POIXMLTypeLoader.parse(node, CTShadow.type, (XmlOptions) null);
        }

        public static CTShadow parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTShadow) POIXMLTypeLoader.parse(node, CTShadow.type, xmlOptions);
        }

        public static CTShadow parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTShadow) POIXMLTypeLoader.parse(xMLInputStream, CTShadow.type, (XmlOptions) null);
        }

        public static CTShadow parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTShadow) POIXMLTypeLoader.parse(xMLInputStream, CTShadow.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTShadow.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTShadow.type, xmlOptions);
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

    STTrueFalse.Enum getOn();

    STTrueFalse xgetOn();

    boolean isSetOn();

    void setOn(STTrueFalse.Enum r1);

    void xsetOn(STTrueFalse sTTrueFalse);

    void unsetOn();

    STShadowType$Enum getType();

    STShadowType xgetType();

    boolean isSetType();

    void setType(STShadowType$Enum sTShadowType$Enum);

    void xsetType(STShadowType sTShadowType);

    void unsetType();

    STTrueFalse.Enum getObscured();

    STTrueFalse xgetObscured();

    boolean isSetObscured();

    void setObscured(STTrueFalse.Enum r1);

    void xsetObscured(STTrueFalse sTTrueFalse);

    void unsetObscured();

    String getColor();

    STColorType xgetColor();

    boolean isSetColor();

    void setColor(String str);

    void xsetColor(STColorType sTColorType);

    void unsetColor();

    String getOpacity();

    XmlString xgetOpacity();

    boolean isSetOpacity();

    void setOpacity(String str);

    void xsetOpacity(XmlString xmlString);

    void unsetOpacity();

    String getOffset();

    XmlString xgetOffset();

    boolean isSetOffset();

    void setOffset(String str);

    void xsetOffset(XmlString xmlString);

    void unsetOffset();

    String getColor2();

    STColorType xgetColor2();

    boolean isSetColor2();

    void setColor2(String str);

    void xsetColor2(STColorType sTColorType);

    void unsetColor2();

    String getOffset2();

    XmlString xgetOffset2();

    boolean isSetOffset2();

    void setOffset2(String str);

    void xsetOffset2(XmlString xmlString);

    void unsetOffset2();

    String getOrigin();

    XmlString xgetOrigin();

    boolean isSetOrigin();

    void setOrigin(String str);

    void xsetOrigin(XmlString xmlString);

    void unsetOrigin();

    String getMatrix();

    XmlString xgetMatrix();

    boolean isSetMatrix();

    void setMatrix(String str);

    void xsetMatrix(XmlString xmlString);

    void unsetMatrix();
}
