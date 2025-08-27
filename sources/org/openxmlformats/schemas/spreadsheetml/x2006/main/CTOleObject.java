package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTOleObject.class */
public interface CTOleObject extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTOleObject.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctoleobjectd866type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTOleObject$Factory.class */
    public static final class Factory {
        public static CTOleObject newInstance() {
            return (CTOleObject) POIXMLTypeLoader.newInstance(CTOleObject.type, null);
        }

        public static CTOleObject newInstance(XmlOptions xmlOptions) {
            return (CTOleObject) POIXMLTypeLoader.newInstance(CTOleObject.type, xmlOptions);
        }

        public static CTOleObject parse(String str) throws XmlException {
            return (CTOleObject) POIXMLTypeLoader.parse(str, CTOleObject.type, (XmlOptions) null);
        }

        public static CTOleObject parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTOleObject) POIXMLTypeLoader.parse(str, CTOleObject.type, xmlOptions);
        }

        public static CTOleObject parse(File file) throws XmlException, IOException {
            return (CTOleObject) POIXMLTypeLoader.parse(file, CTOleObject.type, (XmlOptions) null);
        }

        public static CTOleObject parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOleObject) POIXMLTypeLoader.parse(file, CTOleObject.type, xmlOptions);
        }

        public static CTOleObject parse(URL url) throws XmlException, IOException {
            return (CTOleObject) POIXMLTypeLoader.parse(url, CTOleObject.type, (XmlOptions) null);
        }

        public static CTOleObject parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOleObject) POIXMLTypeLoader.parse(url, CTOleObject.type, xmlOptions);
        }

        public static CTOleObject parse(InputStream inputStream) throws XmlException, IOException {
            return (CTOleObject) POIXMLTypeLoader.parse(inputStream, CTOleObject.type, (XmlOptions) null);
        }

        public static CTOleObject parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOleObject) POIXMLTypeLoader.parse(inputStream, CTOleObject.type, xmlOptions);
        }

        public static CTOleObject parse(Reader reader) throws XmlException, IOException {
            return (CTOleObject) POIXMLTypeLoader.parse(reader, CTOleObject.type, (XmlOptions) null);
        }

        public static CTOleObject parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOleObject) POIXMLTypeLoader.parse(reader, CTOleObject.type, xmlOptions);
        }

        public static CTOleObject parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTOleObject) POIXMLTypeLoader.parse(xMLStreamReader, CTOleObject.type, (XmlOptions) null);
        }

        public static CTOleObject parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTOleObject) POIXMLTypeLoader.parse(xMLStreamReader, CTOleObject.type, xmlOptions);
        }

        public static CTOleObject parse(Node node) throws XmlException {
            return (CTOleObject) POIXMLTypeLoader.parse(node, CTOleObject.type, (XmlOptions) null);
        }

        public static CTOleObject parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTOleObject) POIXMLTypeLoader.parse(node, CTOleObject.type, xmlOptions);
        }

        public static CTOleObject parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTOleObject) POIXMLTypeLoader.parse(xMLInputStream, CTOleObject.type, (XmlOptions) null);
        }

        public static CTOleObject parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTOleObject) POIXMLTypeLoader.parse(xMLInputStream, CTOleObject.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTOleObject.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTOleObject.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getProgId();

    XmlString xgetProgId();

    boolean isSetProgId();

    void setProgId(String str);

    void xsetProgId(XmlString xmlString);

    void unsetProgId();

    STDvAspect$Enum getDvAspect();

    STDvAspect xgetDvAspect();

    boolean isSetDvAspect();

    void setDvAspect(STDvAspect$Enum sTDvAspect$Enum);

    void xsetDvAspect(STDvAspect sTDvAspect);

    void unsetDvAspect();

    String getLink();

    STXstring xgetLink();

    boolean isSetLink();

    void setLink(String str);

    void xsetLink(STXstring sTXstring);

    void unsetLink();

    STOleUpdate$Enum getOleUpdate();

    STOleUpdate xgetOleUpdate();

    boolean isSetOleUpdate();

    void setOleUpdate(STOleUpdate$Enum sTOleUpdate$Enum);

    void xsetOleUpdate(STOleUpdate sTOleUpdate);

    void unsetOleUpdate();

    boolean getAutoLoad();

    XmlBoolean xgetAutoLoad();

    boolean isSetAutoLoad();

    void setAutoLoad(boolean z);

    void xsetAutoLoad(XmlBoolean xmlBoolean);

    void unsetAutoLoad();

    long getShapeId();

    XmlUnsignedInt xgetShapeId();

    void setShapeId(long j);

    void xsetShapeId(XmlUnsignedInt xmlUnsignedInt);

    String getId();

    STRelationshipId xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(STRelationshipId sTRelationshipId);

    void unsetId();
}
