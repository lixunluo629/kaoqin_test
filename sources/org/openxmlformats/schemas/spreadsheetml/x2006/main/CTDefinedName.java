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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDefinedName.class */
public interface CTDefinedName extends STFormula {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTDefinedName.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctdefinedname9413type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTDefinedName$Factory.class */
    public static final class Factory {
        public static CTDefinedName newInstance() {
            return (CTDefinedName) POIXMLTypeLoader.newInstance(CTDefinedName.type, null);
        }

        public static CTDefinedName newInstance(XmlOptions xmlOptions) {
            return (CTDefinedName) POIXMLTypeLoader.newInstance(CTDefinedName.type, xmlOptions);
        }

        public static CTDefinedName parse(String str) throws XmlException {
            return (CTDefinedName) POIXMLTypeLoader.parse(str, CTDefinedName.type, (XmlOptions) null);
        }

        public static CTDefinedName parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTDefinedName) POIXMLTypeLoader.parse(str, CTDefinedName.type, xmlOptions);
        }

        public static CTDefinedName parse(File file) throws XmlException, IOException {
            return (CTDefinedName) POIXMLTypeLoader.parse(file, CTDefinedName.type, (XmlOptions) null);
        }

        public static CTDefinedName parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDefinedName) POIXMLTypeLoader.parse(file, CTDefinedName.type, xmlOptions);
        }

        public static CTDefinedName parse(URL url) throws XmlException, IOException {
            return (CTDefinedName) POIXMLTypeLoader.parse(url, CTDefinedName.type, (XmlOptions) null);
        }

        public static CTDefinedName parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDefinedName) POIXMLTypeLoader.parse(url, CTDefinedName.type, xmlOptions);
        }

        public static CTDefinedName parse(InputStream inputStream) throws XmlException, IOException {
            return (CTDefinedName) POIXMLTypeLoader.parse(inputStream, CTDefinedName.type, (XmlOptions) null);
        }

        public static CTDefinedName parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDefinedName) POIXMLTypeLoader.parse(inputStream, CTDefinedName.type, xmlOptions);
        }

        public static CTDefinedName parse(Reader reader) throws XmlException, IOException {
            return (CTDefinedName) POIXMLTypeLoader.parse(reader, CTDefinedName.type, (XmlOptions) null);
        }

        public static CTDefinedName parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDefinedName) POIXMLTypeLoader.parse(reader, CTDefinedName.type, xmlOptions);
        }

        public static CTDefinedName parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTDefinedName) POIXMLTypeLoader.parse(xMLStreamReader, CTDefinedName.type, (XmlOptions) null);
        }

        public static CTDefinedName parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTDefinedName) POIXMLTypeLoader.parse(xMLStreamReader, CTDefinedName.type, xmlOptions);
        }

        public static CTDefinedName parse(Node node) throws XmlException {
            return (CTDefinedName) POIXMLTypeLoader.parse(node, CTDefinedName.type, (XmlOptions) null);
        }

        public static CTDefinedName parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTDefinedName) POIXMLTypeLoader.parse(node, CTDefinedName.type, xmlOptions);
        }

        public static CTDefinedName parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTDefinedName) POIXMLTypeLoader.parse(xMLInputStream, CTDefinedName.type, (XmlOptions) null);
        }

        public static CTDefinedName parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTDefinedName) POIXMLTypeLoader.parse(xMLInputStream, CTDefinedName.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDefinedName.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDefinedName.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getName();

    STXstring xgetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    String getComment();

    STXstring xgetComment();

    boolean isSetComment();

    void setComment(String str);

    void xsetComment(STXstring sTXstring);

    void unsetComment();

    String getCustomMenu();

    STXstring xgetCustomMenu();

    boolean isSetCustomMenu();

    void setCustomMenu(String str);

    void xsetCustomMenu(STXstring sTXstring);

    void unsetCustomMenu();

    String getDescription();

    STXstring xgetDescription();

    boolean isSetDescription();

    void setDescription(String str);

    void xsetDescription(STXstring sTXstring);

    void unsetDescription();

    String getHelp();

    STXstring xgetHelp();

    boolean isSetHelp();

    void setHelp(String str);

    void xsetHelp(STXstring sTXstring);

    void unsetHelp();

    String getStatusBar();

    STXstring xgetStatusBar();

    boolean isSetStatusBar();

    void setStatusBar(String str);

    void xsetStatusBar(STXstring sTXstring);

    void unsetStatusBar();

    long getLocalSheetId();

    XmlUnsignedInt xgetLocalSheetId();

    boolean isSetLocalSheetId();

    void setLocalSheetId(long j);

    void xsetLocalSheetId(XmlUnsignedInt xmlUnsignedInt);

    void unsetLocalSheetId();

    boolean getHidden();

    XmlBoolean xgetHidden();

    boolean isSetHidden();

    void setHidden(boolean z);

    void xsetHidden(XmlBoolean xmlBoolean);

    void unsetHidden();

    boolean getFunction();

    XmlBoolean xgetFunction();

    boolean isSetFunction();

    void setFunction(boolean z);

    void xsetFunction(XmlBoolean xmlBoolean);

    void unsetFunction();

    boolean getVbProcedure();

    XmlBoolean xgetVbProcedure();

    boolean isSetVbProcedure();

    void setVbProcedure(boolean z);

    void xsetVbProcedure(XmlBoolean xmlBoolean);

    void unsetVbProcedure();

    boolean getXlm();

    XmlBoolean xgetXlm();

    boolean isSetXlm();

    void setXlm(boolean z);

    void xsetXlm(XmlBoolean xmlBoolean);

    void unsetXlm();

    long getFunctionGroupId();

    XmlUnsignedInt xgetFunctionGroupId();

    boolean isSetFunctionGroupId();

    void setFunctionGroupId(long j);

    void xsetFunctionGroupId(XmlUnsignedInt xmlUnsignedInt);

    void unsetFunctionGroupId();

    String getShortcutKey();

    STXstring xgetShortcutKey();

    boolean isSetShortcutKey();

    void setShortcutKey(String str);

    void xsetShortcutKey(STXstring sTXstring);

    void unsetShortcutKey();

    boolean getPublishToServer();

    XmlBoolean xgetPublishToServer();

    boolean isSetPublishToServer();

    void setPublishToServer(boolean z);

    void xsetPublishToServer(XmlBoolean xmlBoolean);

    void unsetPublishToServer();

    boolean getWorkbookParameter();

    XmlBoolean xgetWorkbookParameter();

    boolean isSetWorkbookParameter();

    void setWorkbookParameter(boolean z);

    void xsetWorkbookParameter(XmlBoolean xmlBoolean);

    void unsetWorkbookParameter();
}
