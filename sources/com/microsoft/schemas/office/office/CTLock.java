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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/CTLock.class */
public interface CTLock extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLock.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlock6b8etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/CTLock$Factory.class */
    public static final class Factory {
        public static CTLock newInstance() {
            return (CTLock) POIXMLTypeLoader.newInstance(CTLock.type, null);
        }

        public static CTLock newInstance(XmlOptions xmlOptions) {
            return (CTLock) POIXMLTypeLoader.newInstance(CTLock.type, xmlOptions);
        }

        public static CTLock parse(String str) throws XmlException {
            return (CTLock) POIXMLTypeLoader.parse(str, CTLock.type, (XmlOptions) null);
        }

        public static CTLock parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLock) POIXMLTypeLoader.parse(str, CTLock.type, xmlOptions);
        }

        public static CTLock parse(File file) throws XmlException, IOException {
            return (CTLock) POIXMLTypeLoader.parse(file, CTLock.type, (XmlOptions) null);
        }

        public static CTLock parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLock) POIXMLTypeLoader.parse(file, CTLock.type, xmlOptions);
        }

        public static CTLock parse(URL url) throws XmlException, IOException {
            return (CTLock) POIXMLTypeLoader.parse(url, CTLock.type, (XmlOptions) null);
        }

        public static CTLock parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLock) POIXMLTypeLoader.parse(url, CTLock.type, xmlOptions);
        }

        public static CTLock parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLock) POIXMLTypeLoader.parse(inputStream, CTLock.type, (XmlOptions) null);
        }

        public static CTLock parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLock) POIXMLTypeLoader.parse(inputStream, CTLock.type, xmlOptions);
        }

        public static CTLock parse(Reader reader) throws XmlException, IOException {
            return (CTLock) POIXMLTypeLoader.parse(reader, CTLock.type, (XmlOptions) null);
        }

        public static CTLock parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLock) POIXMLTypeLoader.parse(reader, CTLock.type, xmlOptions);
        }

        public static CTLock parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLock) POIXMLTypeLoader.parse(xMLStreamReader, CTLock.type, (XmlOptions) null);
        }

        public static CTLock parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLock) POIXMLTypeLoader.parse(xMLStreamReader, CTLock.type, xmlOptions);
        }

        public static CTLock parse(Node node) throws XmlException {
            return (CTLock) POIXMLTypeLoader.parse(node, CTLock.type, (XmlOptions) null);
        }

        public static CTLock parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLock) POIXMLTypeLoader.parse(node, CTLock.type, xmlOptions);
        }

        public static CTLock parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLock) POIXMLTypeLoader.parse(xMLInputStream, CTLock.type, (XmlOptions) null);
        }

        public static CTLock parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLock) POIXMLTypeLoader.parse(xMLInputStream, CTLock.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLock.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLock.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STExt.Enum getExt();

    STExt xgetExt();

    boolean isSetExt();

    void setExt(STExt.Enum r1);

    void xsetExt(STExt sTExt);

    void unsetExt();

    STTrueFalse$Enum getPosition();

    STTrueFalse xgetPosition();

    boolean isSetPosition();

    void setPosition(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetPosition(STTrueFalse sTTrueFalse);

    void unsetPosition();

    STTrueFalse$Enum getSelection();

    STTrueFalse xgetSelection();

    boolean isSetSelection();

    void setSelection(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetSelection(STTrueFalse sTTrueFalse);

    void unsetSelection();

    STTrueFalse$Enum getGrouping();

    STTrueFalse xgetGrouping();

    boolean isSetGrouping();

    void setGrouping(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetGrouping(STTrueFalse sTTrueFalse);

    void unsetGrouping();

    STTrueFalse$Enum getUngrouping();

    STTrueFalse xgetUngrouping();

    boolean isSetUngrouping();

    void setUngrouping(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetUngrouping(STTrueFalse sTTrueFalse);

    void unsetUngrouping();

    STTrueFalse$Enum getRotation();

    STTrueFalse xgetRotation();

    boolean isSetRotation();

    void setRotation(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetRotation(STTrueFalse sTTrueFalse);

    void unsetRotation();

    STTrueFalse$Enum getCropping();

    STTrueFalse xgetCropping();

    boolean isSetCropping();

    void setCropping(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetCropping(STTrueFalse sTTrueFalse);

    void unsetCropping();

    STTrueFalse$Enum getVerticies();

    STTrueFalse xgetVerticies();

    boolean isSetVerticies();

    void setVerticies(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetVerticies(STTrueFalse sTTrueFalse);

    void unsetVerticies();

    STTrueFalse$Enum getAdjusthandles();

    STTrueFalse xgetAdjusthandles();

    boolean isSetAdjusthandles();

    void setAdjusthandles(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetAdjusthandles(STTrueFalse sTTrueFalse);

    void unsetAdjusthandles();

    STTrueFalse$Enum getText();

    STTrueFalse xgetText();

    boolean isSetText();

    void setText(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetText(STTrueFalse sTTrueFalse);

    void unsetText();

    STTrueFalse$Enum getAspectratio();

    STTrueFalse xgetAspectratio();

    boolean isSetAspectratio();

    void setAspectratio(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetAspectratio(STTrueFalse sTTrueFalse);

    void unsetAspectratio();

    STTrueFalse$Enum getShapetype();

    STTrueFalse xgetShapetype();

    boolean isSetShapetype();

    void setShapetype(STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetShapetype(STTrueFalse sTTrueFalse);

    void unsetShapetype();
}
