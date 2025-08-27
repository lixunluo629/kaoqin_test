package org.apache.xmlbeans;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.xml.sax.EntityResolver;
import org.xml.sax.XMLReader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlOptions.class */
public class XmlOptions implements Serializable {
    private static final long serialVersionUID = 1;
    private Map _map = new HashMap();
    public static final String GENERATE_JAVA_14 = "1.4";
    public static final String GENERATE_JAVA_15 = "1.5";
    public static final String SAVE_NAMESPACES_FIRST = "SAVE_NAMESPACES_FIRST";
    public static final String SAVE_SYNTHETIC_DOCUMENT_ELEMENT = "SAVE_SYNTHETIC_DOCUMENT_ELEMENT";
    public static final String SAVE_PRETTY_PRINT = "SAVE_PRETTY_PRINT";
    public static final String SAVE_PRETTY_PRINT_INDENT = "SAVE_PRETTY_PRINT_INDENT";
    public static final String SAVE_PRETTY_PRINT_OFFSET = "SAVE_PRETTY_PRINT_OFFSET";
    public static final String SAVE_AGGRESSIVE_NAMESPACES = "SAVE_AGGRESSIVE_NAMESPACES";
    public static final String SAVE_USE_DEFAULT_NAMESPACE = "SAVE_USE_DEFAULT_NAMESPACE";
    public static final String SAVE_IMPLICIT_NAMESPACES = "SAVE_IMPLICIT_NAMESPACES";
    public static final String SAVE_SUGGESTED_PREFIXES = "SAVE_SUGGESTED_PREFIXES";
    public static final String SAVE_FILTER_PROCINST = "SAVE_FILTER_PROCINST";
    public static final String SAVE_USE_OPEN_FRAGMENT = "SAVE_USE_OPEN_FRAGMENT";
    public static final String SAVE_OUTER = "SAVE_OUTER";
    public static final String SAVE_INNER = "SAVE_INNER";
    public static final String SAVE_NO_XML_DECL = "SAVE_NO_XML_DECL";
    public static final String SAVE_SUBSTITUTE_CHARACTERS = "SAVE_SUBSTITUTE_CHARACTERS";
    public static final String SAVE_OPTIMIZE_FOR_SPEED = "SAVE_OPTIMIZE_FOR_SPEED";
    public static final String SAVE_CDATA_LENGTH_THRESHOLD = "SAVE_CDATA_LENGTH_THRESHOLD";
    public static final String SAVE_CDATA_ENTITY_COUNT_THRESHOLD = "SAVE_CDATA_ENTITY_COUNT_THRESHOLD";
    public static final String SAVE_SAX_NO_NSDECLS_IN_ATTRIBUTES = "SAVE_SAX_NO_NSDECLS_IN_ATTRIBUTES";
    public static final String LOAD_REPLACE_DOCUMENT_ELEMENT = "LOAD_REPLACE_DOCUMENT_ELEMENT";
    public static final String LOAD_STRIP_WHITESPACE = "LOAD_STRIP_WHITESPACE";
    public static final String LOAD_STRIP_COMMENTS = "LOAD_STRIP_COMMENTS";
    public static final String LOAD_STRIP_PROCINSTS = "LOAD_STRIP_PROCINSTS";
    public static final String LOAD_LINE_NUMBERS = "LOAD_LINE_NUMBERS";
    public static final String LOAD_LINE_NUMBERS_END_ELEMENT = "LOAD_LINE_NUMBERS_END_ELEMENT";
    public static final String LOAD_SAVE_CDATA_BOOKMARKS = "LOAD_SAVE_CDATA_BOOKMARKS";
    public static final String LOAD_SUBSTITUTE_NAMESPACES = "LOAD_SUBSTITUTE_NAMESPACES";
    public static final String LOAD_TRIM_TEXT_BUFFER = "LOAD_TRIM_TEXT_BUFFER";
    public static final String LOAD_ADDITIONAL_NAMESPACES = "LOAD_ADDITIONAL_NAMESPACES";
    public static final String LOAD_MESSAGE_DIGEST = "LOAD_MESSAGE_DIGEST";
    public static final String LOAD_USE_DEFAULT_RESOLVER = "LOAD_USE_DEFAULT_RESOLVER";
    public static final String LOAD_USE_XMLREADER = "LOAD_USE_XMLREADER";
    public static final String XQUERY_CURRENT_NODE_VAR = "XQUERY_CURRENT_NODE_VAR";
    public static final String XQUERY_VARIABLE_MAP = "XQUERY_VARIABLE_MAP";
    public static final String CHARACTER_ENCODING = "CHARACTER_ENCODING";
    public static final String ERROR_LISTENER = "ERROR_LISTENER";
    public static final String DOCUMENT_TYPE = "DOCUMENT_TYPE";
    public static final String DOCUMENT_SOURCE_NAME = "DOCUMENT_SOURCE_NAME";
    public static final String COMPILE_SUBSTITUTE_NAMES = "COMPILE_SUBSTITUTE_NAMES";
    public static final String COMPILE_NO_VALIDATION = "COMPILE_NO_VALIDATION";
    public static final String COMPILE_NO_UPA_RULE = "COMPILE_NO_UPA_RULE";
    public static final String COMPILE_NO_PVR_RULE = "COMPILE_NO_PVR_RULE";
    public static final String COMPILE_NO_ANNOTATIONS = "COMPILE_NO_ANNOTATIONS";
    public static final String COMPILE_DOWNLOAD_URLS = "COMPILE_DOWNLOAD_URLS";
    public static final String COMPILE_MDEF_NAMESPACES = "COMPILE_MDEF_NAMESPACES";
    public static final String VALIDATE_ON_SET = "VALIDATE_ON_SET";
    public static final String VALIDATE_TREAT_LAX_AS_SKIP = "VALIDATE_TREAT_LAX_AS_SKIP";
    public static final String VALIDATE_STRICT = "VALIDATE_STRICT";
    public static final String VALIDATE_TEXT_ONLY = "VALIDATE_TEXT_ONLY";
    public static final String UNSYNCHRONIZED = "UNSYNCHRONIZED";
    public static final String ENTITY_RESOLVER = "ENTITY_RESOLVER";
    public static final String BASE_URI = "BASE_URI";
    public static final String SCHEMA_CODE_PRINTER = "SCHEMA_CODE_PRINTER";
    public static final String GENERATE_JAVA_VERSION = "GENERATE_JAVA_VERSION";
    public static final String COPY_USE_NEW_SYNC_DOMAIN = "COPY_USE_NEW_LOCALE";
    public static final String LOAD_ENTITY_BYTES_LIMIT = "LOAD_ENTITY_BYTES_LIMIT";
    public static final String ENTITY_EXPANSION_LIMIT = "ENTITY_EXPANSION_LIMIT";
    public static final String LOAD_DTD_GRAMMAR = "LOAD_DTD_GRAMMAR";
    public static final String LOAD_EXTERNAL_DTD = "LOAD_EXTERNAL_DTD";
    public static final int DEFAULT_ENTITY_EXPANSION_LIMIT = 2048;
    private static final XmlOptions EMPTY_OPTIONS = new XmlOptions();

    public XmlOptions() {
    }

    public XmlOptions(XmlOptions other) {
        if (other != null) {
            this._map.putAll(other._map);
        }
    }

    public XmlOptions setSaveNamespacesFirst() {
        return set(SAVE_NAMESPACES_FIRST);
    }

    public XmlOptions setSavePrettyPrint() {
        return set(SAVE_PRETTY_PRINT);
    }

    public XmlOptions setSavePrettyPrintIndent(int indent) {
        return set(SAVE_PRETTY_PRINT_INDENT, indent);
    }

    public XmlOptions setSavePrettyPrintOffset(int offset) {
        return set(SAVE_PRETTY_PRINT_OFFSET, offset);
    }

    public XmlOptions setCharacterEncoding(String encoding) {
        return set(CHARACTER_ENCODING, encoding);
    }

    public XmlOptions setDocumentType(SchemaType type) {
        return set(DOCUMENT_TYPE, type);
    }

    public XmlOptions setErrorListener(Collection c) {
        return set(ERROR_LISTENER, c);
    }

    public XmlOptions setSaveAggressiveNamespaces() {
        return set(SAVE_AGGRESSIVE_NAMESPACES);
    }

    public XmlOptions setSaveAggresiveNamespaces() {
        return setSaveAggressiveNamespaces();
    }

    public XmlOptions setSaveSyntheticDocumentElement(QName name) {
        return set(SAVE_SYNTHETIC_DOCUMENT_ELEMENT, name);
    }

    public XmlOptions setUseDefaultNamespace() {
        return set(SAVE_USE_DEFAULT_NAMESPACE);
    }

    public XmlOptions setSaveImplicitNamespaces(Map implicitNamespaces) {
        return set(SAVE_IMPLICIT_NAMESPACES, implicitNamespaces);
    }

    public XmlOptions setSaveSuggestedPrefixes(Map suggestedPrefixes) {
        return set(SAVE_SUGGESTED_PREFIXES, suggestedPrefixes);
    }

    public XmlOptions setSaveFilterProcinst(String filterProcinst) {
        return set(SAVE_FILTER_PROCINST, filterProcinst);
    }

    public XmlOptions setSaveSubstituteCharacters(XmlOptionCharEscapeMap characterReplacementMap) {
        return set(SAVE_SUBSTITUTE_CHARACTERS, characterReplacementMap);
    }

    public XmlOptions setSaveUseOpenFrag() {
        return set(SAVE_USE_OPEN_FRAGMENT);
    }

    public XmlOptions setSaveOuter() {
        return set(SAVE_OUTER);
    }

    public XmlOptions setSaveInner() {
        return set(SAVE_INNER);
    }

    public XmlOptions setSaveNoXmlDecl() {
        return set(SAVE_NO_XML_DECL);
    }

    public XmlOptions setSaveCDataLengthThreshold(int cdataLengthThreshold) {
        return set(SAVE_CDATA_LENGTH_THRESHOLD, cdataLengthThreshold);
    }

    public XmlOptions setSaveCDataEntityCountThreshold(int cdataEntityCountThreshold) {
        return set(SAVE_CDATA_ENTITY_COUNT_THRESHOLD, cdataEntityCountThreshold);
    }

    public XmlOptions setUseCDataBookmarks() {
        return set(LOAD_SAVE_CDATA_BOOKMARKS);
    }

    public XmlOptions setSaveSaxNoNSDeclsInAttributes() {
        return set(SAVE_SAX_NO_NSDECLS_IN_ATTRIBUTES);
    }

    public XmlOptions setLoadReplaceDocumentElement(QName replacement) {
        return set(LOAD_REPLACE_DOCUMENT_ELEMENT, replacement);
    }

    public XmlOptions setLoadStripWhitespace() {
        return set(LOAD_STRIP_WHITESPACE);
    }

    public XmlOptions setLoadStripComments() {
        return set(LOAD_STRIP_COMMENTS);
    }

    public XmlOptions setLoadStripProcinsts() {
        return set(LOAD_STRIP_PROCINSTS);
    }

    public XmlOptions setLoadLineNumbers() {
        return set(LOAD_LINE_NUMBERS);
    }

    public XmlOptions setLoadLineNumbers(String option) {
        XmlOptions temp = setLoadLineNumbers();
        return temp.set(option);
    }

    public XmlOptions setLoadSubstituteNamespaces(Map substNamespaces) {
        return set(LOAD_SUBSTITUTE_NAMESPACES, substNamespaces);
    }

    public XmlOptions setLoadTrimTextBuffer() {
        return set(LOAD_TRIM_TEXT_BUFFER);
    }

    public XmlOptions setLoadAdditionalNamespaces(Map nses) {
        return set(LOAD_ADDITIONAL_NAMESPACES, nses);
    }

    public XmlOptions setLoadMessageDigest() {
        return set(LOAD_MESSAGE_DIGEST);
    }

    public XmlOptions setLoadUseDefaultResolver() {
        return set(LOAD_USE_DEFAULT_RESOLVER);
    }

    public XmlOptions setLoadUseXMLReader(XMLReader xmlReader) {
        return set(LOAD_USE_XMLREADER, xmlReader);
    }

    public XmlOptions setXqueryCurrentNodeVar(String varName) {
        return set(XQUERY_CURRENT_NODE_VAR, varName);
    }

    public XmlOptions setXqueryVariables(Map varMap) {
        return set(XQUERY_VARIABLE_MAP, varMap);
    }

    public XmlOptions setDocumentSourceName(String documentSourceName) {
        return set(DOCUMENT_SOURCE_NAME, documentSourceName);
    }

    public XmlOptions setCompileSubstituteNames(Map nameMap) {
        return set(COMPILE_SUBSTITUTE_NAMES, nameMap);
    }

    public XmlOptions setCompileNoValidation() {
        return set(COMPILE_NO_VALIDATION);
    }

    public XmlOptions setCompileNoUpaRule() {
        return set(COMPILE_NO_UPA_RULE);
    }

    public XmlOptions setCompileNoPvrRule() {
        return set(COMPILE_NO_PVR_RULE);
    }

    public XmlOptions setCompileNoAnnotations() {
        return set(COMPILE_NO_ANNOTATIONS);
    }

    public XmlOptions setCompileDownloadUrls() {
        return set(COMPILE_DOWNLOAD_URLS);
    }

    public XmlOptions setCompileMdefNamespaces(Set mdefNamespaces) {
        return set(COMPILE_MDEF_NAMESPACES, mdefNamespaces);
    }

    public XmlOptions setValidateOnSet() {
        return set(VALIDATE_ON_SET);
    }

    public XmlOptions setValidateTreatLaxAsSkip() {
        return set(VALIDATE_TREAT_LAX_AS_SKIP);
    }

    public XmlOptions setValidateStrict() {
        return set(VALIDATE_STRICT);
    }

    public XmlOptions setUnsynchronized() {
        return set(UNSYNCHRONIZED);
    }

    public XmlOptions setEntityResolver(EntityResolver resolver) {
        return set(ENTITY_RESOLVER, resolver);
    }

    public XmlOptions setBaseURI(URI baseURI) {
        return set(BASE_URI, baseURI);
    }

    public XmlOptions setSchemaCodePrinter(SchemaCodePrinter printer) {
        return set(SCHEMA_CODE_PRINTER, printer);
    }

    public XmlOptions setGenerateJavaVersion(String source) {
        return set(GENERATE_JAVA_VERSION, source);
    }

    public XmlOptions setCopyUseNewSynchronizationDomain(boolean useNewSyncDomain) {
        return set("COPY_USE_NEW_LOCALE", Boolean.valueOf(useNewSyncDomain));
    }

    public XmlOptions setLoadEntityBytesLimit(int entityBytesLimit) {
        return set(LOAD_ENTITY_BYTES_LIMIT, entityBytesLimit);
    }

    public XmlOptions setEntityExpansionLimit(int entityExpansionLimit) {
        return set(ENTITY_EXPANSION_LIMIT, entityExpansionLimit);
    }

    public XmlOptions setLoadDTDGrammar(boolean loadDTDGrammar) {
        return set(LOAD_DTD_GRAMMAR, Boolean.valueOf(loadDTDGrammar));
    }

    public XmlOptions setLoadExternalDTD(boolean loadExternalDTD) {
        return set(LOAD_EXTERNAL_DTD, Boolean.valueOf(loadExternalDTD));
    }

    static {
        EMPTY_OPTIONS._map = Collections.unmodifiableMap(EMPTY_OPTIONS._map);
    }

    public static XmlOptions maskNull(XmlOptions o) {
        return o == null ? EMPTY_OPTIONS : o;
    }

    public void put(Object option) {
        put(option, (Object) null);
    }

    public void put(Object option, Object value) {
        this._map.put(option, value);
    }

    public void put(Object option, int value) {
        put(option, new Integer(value));
    }

    private XmlOptions set(Object option) {
        return set(option, (Object) null);
    }

    private XmlOptions set(Object option, Object value) {
        this._map.put(option, value);
        return this;
    }

    private XmlOptions set(Object option, int value) {
        return set(option, new Integer(value));
    }

    public boolean hasOption(Object option) {
        return this._map.containsKey(option);
    }

    public static boolean hasOption(XmlOptions options, Object option) {
        if (options == null) {
            return false;
        }
        return options.hasOption(option);
    }

    public Object get(Object option) {
        return this._map.get(option);
    }

    public void remove(Object option) {
        this._map.remove(option);
    }

    public static Object safeGet(XmlOptions o, Object option) {
        if (o == null) {
            return null;
        }
        return o.get(option);
    }
}
