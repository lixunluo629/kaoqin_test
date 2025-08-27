package org.apache.poi;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import com.alibaba.excel.constant.ExcelXmlConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.openxml4j.opc.PackageNamespaces;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.util.DocumentHelper;
import org.apache.poi.util.Removal;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/POIXMLTypeLoader.class */
public class POIXMLTypeLoader {
    private static final String MS_OFFICE_URN = "urn:schemas-microsoft-com:office:office";
    private static final String MS_EXCEL_URN = "urn:schemas-microsoft-com:office:excel";
    private static final String MS_WORD_URN = "urn:schemas-microsoft-com:office:word";
    private static final String MS_VML_URN = "urn:schemas-microsoft-com:vml";
    private static ThreadLocal<SchemaTypeLoader> typeLoader = new ThreadLocal<>();
    public static final XmlOptions DEFAULT_XML_OPTIONS = new XmlOptions();

    static {
        DEFAULT_XML_OPTIONS.setSaveOuter();
        DEFAULT_XML_OPTIONS.setUseDefaultNamespace();
        DEFAULT_XML_OPTIONS.setSaveAggressiveNamespaces();
        DEFAULT_XML_OPTIONS.setCharacterEncoding("UTF-8");
        Map<String, String> map = new HashMap<>();
        map.put(XSSFRelation.NS_DRAWINGML, "a");
        map.put(XSSFRelation.NS_CHART, ExcelXmlConstants.CELL_TAG);
        map.put("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "wp");
        map.put(PackageNamespaces.MARKUP_COMPATIBILITY, "ve");
        map.put("http://schemas.openxmlformats.org/officeDocument/2006/math", ANSIConstants.ESC_END);
        map.put(PackageRelationshipTypes.CORE_PROPERTIES_ECMA376_NS, ExcelXmlConstants.POSITION);
        map.put("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "vt");
        map.put("http://schemas.openxmlformats.org/presentationml/2006/main", "p");
        map.put("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "w");
        map.put("http://schemas.microsoft.com/office/word/2006/wordml", "wne");
        map.put(MS_OFFICE_URN, "o");
        map.put(MS_EXCEL_URN, "x");
        map.put(MS_WORD_URN, "w10");
        map.put(MS_VML_URN, ExcelXmlConstants.CELL_VALUE_TAG);
        DEFAULT_XML_OPTIONS.setSaveSuggestedPrefixes(Collections.unmodifiableMap(map));
    }

    private static XmlOptions getXmlOptions(XmlOptions options) {
        return options == null ? DEFAULT_XML_OPTIONS : options;
    }

    @Removal(version = "4.0")
    @Deprecated
    public static void setClassLoader(ClassLoader cl) {
    }

    private static SchemaTypeLoader getTypeLoader(SchemaType type) {
        SchemaTypeLoader tl = typeLoader.get();
        if (tl == null) {
            ClassLoader cl = type.getClass().getClassLoader();
            tl = XmlBeans.typeLoaderForClassLoader(cl);
            typeLoader.set(tl);
        }
        return tl;
    }

    public static XmlObject newInstance(SchemaType type, XmlOptions options) {
        return getTypeLoader(type).newInstance(type, getXmlOptions(options));
    }

    public static XmlObject parse(String xmlText, SchemaType type, XmlOptions options) throws XmlException {
        try {
            return parse(new StringReader(xmlText), type, options);
        } catch (IOException e) {
            throw new XmlException("Unable to parse xml bean", e);
        }
    }

    public static XmlObject parse(File file, SchemaType type, XmlOptions options) throws XmlException, IOException {
        InputStream is = new FileInputStream(file);
        try {
            XmlObject xmlObject = parse(is, type, options);
            is.close();
            return xmlObject;
        } catch (Throwable th) {
            is.close();
            throw th;
        }
    }

    public static XmlObject parse(URL file, SchemaType type, XmlOptions options) throws XmlException, IOException {
        InputStream is = file.openStream();
        try {
            XmlObject xmlObject = parse(is, type, options);
            is.close();
            return xmlObject;
        } catch (Throwable th) {
            is.close();
            throw th;
        }
    }

    public static XmlObject parse(InputStream jiois, SchemaType type, XmlOptions options) throws XmlException, IOException {
        try {
            Document doc = DocumentHelper.readDocument(jiois);
            return getTypeLoader(type).parse(doc.getDocumentElement(), type, getXmlOptions(options));
        } catch (SAXException e) {
            throw new IOException("Unable to parse xml bean", e);
        }
    }

    public static XmlObject parse(XMLStreamReader xsr, SchemaType type, XmlOptions options) throws XmlException {
        return getTypeLoader(type).parse(xsr, type, getXmlOptions(options));
    }

    public static XmlObject parse(Reader jior, SchemaType type, XmlOptions options) throws XmlException, IOException {
        try {
            Document doc = DocumentHelper.readDocument(new InputSource(jior));
            return getTypeLoader(type).parse(doc.getDocumentElement(), type, getXmlOptions(options));
        } catch (SAXException e) {
            throw new XmlException("Unable to parse xml bean", e);
        }
    }

    public static XmlObject parse(Node node, SchemaType type, XmlOptions options) throws XmlException {
        return getTypeLoader(type).parse(node, type, getXmlOptions(options));
    }

    public static XmlObject parse(XMLInputStream xis, SchemaType type, XmlOptions options) throws XMLStreamException, XmlException {
        return getTypeLoader(type).parse(xis, type, getXmlOptions(options));
    }

    public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, SchemaType type, XmlOptions options) throws XMLStreamException, XmlException {
        return getTypeLoader(type).newValidatingXMLInputStream(xis, type, getXmlOptions(options));
    }
}
