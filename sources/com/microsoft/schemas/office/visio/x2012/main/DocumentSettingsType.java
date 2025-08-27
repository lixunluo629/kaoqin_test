package com.microsoft.schemas.office.visio.x2012.main;

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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/DocumentSettingsType.class */
public interface DocumentSettingsType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(DocumentSettingsType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("documentsettingstype945btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/DocumentSettingsType$Factory.class */
    public static final class Factory {
        public static DocumentSettingsType newInstance() {
            return (DocumentSettingsType) POIXMLTypeLoader.newInstance(DocumentSettingsType.type, null);
        }

        public static DocumentSettingsType newInstance(XmlOptions xmlOptions) {
            return (DocumentSettingsType) POIXMLTypeLoader.newInstance(DocumentSettingsType.type, xmlOptions);
        }

        public static DocumentSettingsType parse(String str) throws XmlException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(str, DocumentSettingsType.type, (XmlOptions) null);
        }

        public static DocumentSettingsType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(str, DocumentSettingsType.type, xmlOptions);
        }

        public static DocumentSettingsType parse(File file) throws XmlException, IOException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(file, DocumentSettingsType.type, (XmlOptions) null);
        }

        public static DocumentSettingsType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(file, DocumentSettingsType.type, xmlOptions);
        }

        public static DocumentSettingsType parse(URL url) throws XmlException, IOException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(url, DocumentSettingsType.type, (XmlOptions) null);
        }

        public static DocumentSettingsType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(url, DocumentSettingsType.type, xmlOptions);
        }

        public static DocumentSettingsType parse(InputStream inputStream) throws XmlException, IOException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(inputStream, DocumentSettingsType.type, (XmlOptions) null);
        }

        public static DocumentSettingsType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(inputStream, DocumentSettingsType.type, xmlOptions);
        }

        public static DocumentSettingsType parse(Reader reader) throws XmlException, IOException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(reader, DocumentSettingsType.type, (XmlOptions) null);
        }

        public static DocumentSettingsType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(reader, DocumentSettingsType.type, xmlOptions);
        }

        public static DocumentSettingsType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(xMLStreamReader, DocumentSettingsType.type, (XmlOptions) null);
        }

        public static DocumentSettingsType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(xMLStreamReader, DocumentSettingsType.type, xmlOptions);
        }

        public static DocumentSettingsType parse(Node node) throws XmlException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(node, DocumentSettingsType.type, (XmlOptions) null);
        }

        public static DocumentSettingsType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(node, DocumentSettingsType.type, xmlOptions);
        }

        public static DocumentSettingsType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(xMLInputStream, DocumentSettingsType.type, (XmlOptions) null);
        }

        public static DocumentSettingsType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (DocumentSettingsType) POIXMLTypeLoader.parse(xMLInputStream, DocumentSettingsType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, DocumentSettingsType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, DocumentSettingsType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    GlueSettingsType getGlueSettings();

    boolean isSetGlueSettings();

    void setGlueSettings(GlueSettingsType glueSettingsType);

    GlueSettingsType addNewGlueSettings();

    void unsetGlueSettings();

    SnapSettingsType getSnapSettings();

    boolean isSetSnapSettings();

    void setSnapSettings(SnapSettingsType snapSettingsType);

    SnapSettingsType addNewSnapSettings();

    void unsetSnapSettings();

    SnapExtensionsType getSnapExtensions();

    boolean isSetSnapExtensions();

    void setSnapExtensions(SnapExtensionsType snapExtensionsType);

    SnapExtensionsType addNewSnapExtensions();

    void unsetSnapExtensions();

    SnapAnglesType getSnapAngles();

    boolean isSetSnapAngles();

    void setSnapAngles(SnapAnglesType snapAnglesType);

    SnapAnglesType addNewSnapAngles();

    void unsetSnapAngles();

    DynamicGridEnabledType getDynamicGridEnabled();

    boolean isSetDynamicGridEnabled();

    void setDynamicGridEnabled(DynamicGridEnabledType dynamicGridEnabledType);

    DynamicGridEnabledType addNewDynamicGridEnabled();

    void unsetDynamicGridEnabled();

    ProtectStylesType getProtectStyles();

    boolean isSetProtectStyles();

    void setProtectStyles(ProtectStylesType protectStylesType);

    ProtectStylesType addNewProtectStyles();

    void unsetProtectStyles();

    ProtectShapesType getProtectShapes();

    boolean isSetProtectShapes();

    void setProtectShapes(ProtectShapesType protectShapesType);

    ProtectShapesType addNewProtectShapes();

    void unsetProtectShapes();

    ProtectMastersType getProtectMasters();

    boolean isSetProtectMasters();

    void setProtectMasters(ProtectMastersType protectMastersType);

    ProtectMastersType addNewProtectMasters();

    void unsetProtectMasters();

    ProtectBkgndsType getProtectBkgnds();

    boolean isSetProtectBkgnds();

    void setProtectBkgnds(ProtectBkgndsType protectBkgndsType);

    ProtectBkgndsType addNewProtectBkgnds();

    void unsetProtectBkgnds();

    CustomMenusFileType getCustomMenusFile();

    boolean isSetCustomMenusFile();

    void setCustomMenusFile(CustomMenusFileType customMenusFileType);

    CustomMenusFileType addNewCustomMenusFile();

    void unsetCustomMenusFile();

    CustomToolbarsFileType getCustomToolbarsFile();

    boolean isSetCustomToolbarsFile();

    void setCustomToolbarsFile(CustomToolbarsFileType customToolbarsFileType);

    CustomToolbarsFileType addNewCustomToolbarsFile();

    void unsetCustomToolbarsFile();

    AttachedToolbarsType getAttachedToolbars();

    boolean isSetAttachedToolbars();

    void setAttachedToolbars(AttachedToolbarsType attachedToolbarsType);

    AttachedToolbarsType addNewAttachedToolbars();

    void unsetAttachedToolbars();

    long getTopPage();

    XmlUnsignedInt xgetTopPage();

    boolean isSetTopPage();

    void setTopPage(long j);

    void xsetTopPage(XmlUnsignedInt xmlUnsignedInt);

    void unsetTopPage();

    long getDefaultTextStyle();

    XmlUnsignedInt xgetDefaultTextStyle();

    boolean isSetDefaultTextStyle();

    void setDefaultTextStyle(long j);

    void xsetDefaultTextStyle(XmlUnsignedInt xmlUnsignedInt);

    void unsetDefaultTextStyle();

    long getDefaultLineStyle();

    XmlUnsignedInt xgetDefaultLineStyle();

    boolean isSetDefaultLineStyle();

    void setDefaultLineStyle(long j);

    void xsetDefaultLineStyle(XmlUnsignedInt xmlUnsignedInt);

    void unsetDefaultLineStyle();

    long getDefaultFillStyle();

    XmlUnsignedInt xgetDefaultFillStyle();

    boolean isSetDefaultFillStyle();

    void setDefaultFillStyle(long j);

    void xsetDefaultFillStyle(XmlUnsignedInt xmlUnsignedInt);

    void unsetDefaultFillStyle();

    long getDefaultGuideStyle();

    XmlUnsignedInt xgetDefaultGuideStyle();

    boolean isSetDefaultGuideStyle();

    void setDefaultGuideStyle(long j);

    void xsetDefaultGuideStyle(XmlUnsignedInt xmlUnsignedInt);

    void unsetDefaultGuideStyle();
}
