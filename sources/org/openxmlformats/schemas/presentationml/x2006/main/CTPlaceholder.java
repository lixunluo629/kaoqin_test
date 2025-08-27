package org.openxmlformats.schemas.presentationml.x2006.main;

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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTPlaceholder.class */
public interface CTPlaceholder extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPlaceholder.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctplaceholder9efctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTPlaceholder$Factory.class */
    public static final class Factory {
        public static CTPlaceholder newInstance() {
            return (CTPlaceholder) POIXMLTypeLoader.newInstance(CTPlaceholder.type, null);
        }

        public static CTPlaceholder newInstance(XmlOptions xmlOptions) {
            return (CTPlaceholder) POIXMLTypeLoader.newInstance(CTPlaceholder.type, xmlOptions);
        }

        public static CTPlaceholder parse(String str) throws XmlException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(str, CTPlaceholder.type, (XmlOptions) null);
        }

        public static CTPlaceholder parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(str, CTPlaceholder.type, xmlOptions);
        }

        public static CTPlaceholder parse(File file) throws XmlException, IOException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(file, CTPlaceholder.type, (XmlOptions) null);
        }

        public static CTPlaceholder parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(file, CTPlaceholder.type, xmlOptions);
        }

        public static CTPlaceholder parse(URL url) throws XmlException, IOException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(url, CTPlaceholder.type, (XmlOptions) null);
        }

        public static CTPlaceholder parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(url, CTPlaceholder.type, xmlOptions);
        }

        public static CTPlaceholder parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(inputStream, CTPlaceholder.type, (XmlOptions) null);
        }

        public static CTPlaceholder parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(inputStream, CTPlaceholder.type, xmlOptions);
        }

        public static CTPlaceholder parse(Reader reader) throws XmlException, IOException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(reader, CTPlaceholder.type, (XmlOptions) null);
        }

        public static CTPlaceholder parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(reader, CTPlaceholder.type, xmlOptions);
        }

        public static CTPlaceholder parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(xMLStreamReader, CTPlaceholder.type, (XmlOptions) null);
        }

        public static CTPlaceholder parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(xMLStreamReader, CTPlaceholder.type, xmlOptions);
        }

        public static CTPlaceholder parse(Node node) throws XmlException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(node, CTPlaceholder.type, (XmlOptions) null);
        }

        public static CTPlaceholder parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(node, CTPlaceholder.type, xmlOptions);
        }

        public static CTPlaceholder parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(xMLInputStream, CTPlaceholder.type, (XmlOptions) null);
        }

        public static CTPlaceholder parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPlaceholder) POIXMLTypeLoader.parse(xMLInputStream, CTPlaceholder.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPlaceholder.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPlaceholder.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTExtensionListModify getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionListModify cTExtensionListModify);

    CTExtensionListModify addNewExtLst();

    void unsetExtLst();

    STPlaceholderType.Enum getType();

    STPlaceholderType xgetType();

    boolean isSetType();

    void setType(STPlaceholderType.Enum r1);

    void xsetType(STPlaceholderType sTPlaceholderType);

    void unsetType();

    STDirection$Enum getOrient();

    STDirection xgetOrient();

    boolean isSetOrient();

    void setOrient(STDirection$Enum sTDirection$Enum);

    void xsetOrient(STDirection sTDirection);

    void unsetOrient();

    STPlaceholderSize$Enum getSz();

    STPlaceholderSize xgetSz();

    boolean isSetSz();

    void setSz(STPlaceholderSize$Enum sTPlaceholderSize$Enum);

    void xsetSz(STPlaceholderSize sTPlaceholderSize);

    void unsetSz();

    long getIdx();

    XmlUnsignedInt xgetIdx();

    boolean isSetIdx();

    void setIdx(long j);

    void xsetIdx(XmlUnsignedInt xmlUnsignedInt);

    void unsetIdx();

    boolean getHasCustomPrompt();

    XmlBoolean xgetHasCustomPrompt();

    boolean isSetHasCustomPrompt();

    void setHasCustomPrompt(boolean z);

    void xsetHasCustomPrompt(XmlBoolean xmlBoolean);

    void unsetHasCustomPrompt();
}
