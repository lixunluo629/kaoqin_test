package org.openxmlformats.schemas.drawingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTEffectStyleItem.class */
public interface CTEffectStyleItem extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTEffectStyleItem.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cteffectstyleitem05c4type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTEffectStyleItem$Factory.class */
    public static final class Factory {
        public static CTEffectStyleItem newInstance() {
            return (CTEffectStyleItem) POIXMLTypeLoader.newInstance(CTEffectStyleItem.type, null);
        }

        public static CTEffectStyleItem newInstance(XmlOptions xmlOptions) {
            return (CTEffectStyleItem) POIXMLTypeLoader.newInstance(CTEffectStyleItem.type, xmlOptions);
        }

        public static CTEffectStyleItem parse(String str) throws XmlException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(str, CTEffectStyleItem.type, (XmlOptions) null);
        }

        public static CTEffectStyleItem parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(str, CTEffectStyleItem.type, xmlOptions);
        }

        public static CTEffectStyleItem parse(File file) throws XmlException, IOException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(file, CTEffectStyleItem.type, (XmlOptions) null);
        }

        public static CTEffectStyleItem parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(file, CTEffectStyleItem.type, xmlOptions);
        }

        public static CTEffectStyleItem parse(URL url) throws XmlException, IOException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(url, CTEffectStyleItem.type, (XmlOptions) null);
        }

        public static CTEffectStyleItem parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(url, CTEffectStyleItem.type, xmlOptions);
        }

        public static CTEffectStyleItem parse(InputStream inputStream) throws XmlException, IOException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(inputStream, CTEffectStyleItem.type, (XmlOptions) null);
        }

        public static CTEffectStyleItem parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(inputStream, CTEffectStyleItem.type, xmlOptions);
        }

        public static CTEffectStyleItem parse(Reader reader) throws XmlException, IOException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(reader, CTEffectStyleItem.type, (XmlOptions) null);
        }

        public static CTEffectStyleItem parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(reader, CTEffectStyleItem.type, xmlOptions);
        }

        public static CTEffectStyleItem parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(xMLStreamReader, CTEffectStyleItem.type, (XmlOptions) null);
        }

        public static CTEffectStyleItem parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(xMLStreamReader, CTEffectStyleItem.type, xmlOptions);
        }

        public static CTEffectStyleItem parse(Node node) throws XmlException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(node, CTEffectStyleItem.type, (XmlOptions) null);
        }

        public static CTEffectStyleItem parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(node, CTEffectStyleItem.type, xmlOptions);
        }

        public static CTEffectStyleItem parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(xMLInputStream, CTEffectStyleItem.type, (XmlOptions) null);
        }

        public static CTEffectStyleItem parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTEffectStyleItem) POIXMLTypeLoader.parse(xMLInputStream, CTEffectStyleItem.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTEffectStyleItem.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTEffectStyleItem.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTEffectList getEffectLst();

    boolean isSetEffectLst();

    void setEffectLst(CTEffectList cTEffectList);

    CTEffectList addNewEffectLst();

    void unsetEffectLst();

    CTEffectContainer getEffectDag();

    boolean isSetEffectDag();

    void setEffectDag(CTEffectContainer cTEffectContainer);

    CTEffectContainer addNewEffectDag();

    void unsetEffectDag();

    CTScene3D getScene3D();

    boolean isSetScene3D();

    void setScene3D(CTScene3D cTScene3D);

    CTScene3D addNewScene3D();

    void unsetScene3D();

    CTShape3D getSp3D();

    boolean isSetSp3D();

    void setSp3D(CTShape3D cTShape3D);

    CTShape3D addNewSp3D();

    void unsetSp3D();
}
