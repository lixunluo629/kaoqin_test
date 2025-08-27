package org.apache.xmlbeans.impl.schema;

import com.alibaba.excel.constant.ExcelXmlConstants;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.BindingConfig;
import org.apache.xmlbeans.SchemaAttributeGroup;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.ResolverUtil;
import org.apache.xmlbeans.impl.util.HexBin;
import org.apache.xmlbeans.impl.values.XmlStringImpl;
import org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.xml.sax.EntityResolver;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscState.class */
public class StscState {
    private String _givenStsName;
    private Collection _errorListener;
    private SchemaTypeSystemImpl _target;
    private BindingConfig _config;
    private Map _compatMap;
    private boolean _doingDownloads;
    private byte[] _digest;
    private boolean _noDigest;
    private boolean _allowPartial;
    private int _recoveredErrors;
    private SchemaTypeLoader _importingLoader;
    private Map _containers;
    private SchemaDependencies _dependencies;
    private Map _redefinedGlobalTypes;
    private Map _redefinedModelGroups;
    private Map _redefinedAttributeGroups;
    private Map _globalTypes;
    private Map _globalElements;
    private Map _globalAttributes;
    private Map _modelGroups;
    private Map _attributeGroups;
    private Map _documentTypes;
    private Map _attributeTypes;
    private Map _typesByClassname;
    private Map _misspelledNames;
    private Set _processingGroups;
    private Map _idConstraints;
    private Set _namespaces;
    private List _annotations;
    private boolean _noUpa;
    private boolean _noPvr;
    private boolean _noAnn;
    private boolean _mdefAll;
    private Set _mdefNamespaces;
    private EntityResolver _entityResolver;
    private File _schemasDir;
    public static final Object CHAMELEON_INCLUDE_URI;
    private static ThreadLocal tl_stscStack;
    private static final XmlValueRef XMLSTR_PRESERVE;
    private static final XmlValueRef XMLSTR_REPLACE;
    private static final XmlValueRef XMLSTR_COLLAPSE;
    static final SchemaType[] EMPTY_ST_ARRAY;
    static final SchemaType.Ref[] EMPTY_STREF_ARRAY;
    private static final XmlValueRef[] FACETS_NONE;
    private static final boolean[] FIXED_FACETS_NONE;
    private static final XmlValueRef[] FACETS_WS_COLLAPSE;
    private static final boolean[] FIXED_FACETS_WS;
    static final XmlValueRef[] FACETS_UNION;
    static final boolean[] FIXED_FACETS_UNION;
    static final XmlValueRef[] FACETS_LIST;
    static final boolean[] FIXED_FACETS_LIST;
    private static final String PROJECT_URL_PREFIX = "project://local";
    Map _sourceForUri;
    URI _baseURI;
    SchemaTypeLoader _s4sloader;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StscState.class.desiredAssertionStatus();
        CHAMELEON_INCLUDE_URI = new Object();
        tl_stscStack = new ThreadLocal();
        XMLSTR_PRESERVE = buildString("preserve");
        XMLSTR_REPLACE = buildString("preserve");
        XMLSTR_COLLAPSE = buildString("preserve");
        EMPTY_ST_ARRAY = new SchemaType[0];
        EMPTY_STREF_ARRAY = new SchemaType.Ref[0];
        FACETS_NONE = new XmlValueRef[]{null, null, null, null, null, null, null, null, null, null, null, null};
        FIXED_FACETS_NONE = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false};
        FACETS_WS_COLLAPSE = new XmlValueRef[]{null, null, null, null, null, null, null, null, null, build_wsstring(3), null, null};
        FIXED_FACETS_WS = new boolean[]{false, false, false, false, false, false, false, false, false, true, false, false};
        FACETS_UNION = FACETS_NONE;
        FIXED_FACETS_UNION = FIXED_FACETS_NONE;
        FACETS_LIST = FACETS_WS_COLLAPSE;
        FIXED_FACETS_LIST = FIXED_FACETS_WS;
    }

    private static Set buildDefaultMdefNamespaces() {
        return new HashSet(Arrays.asList("http://www.openuri.org/2002/04/soap/conversation/"));
    }

    private StscState() {
        this._digest = null;
        this._noDigest = false;
        this._allowPartial = false;
        this._recoveredErrors = 0;
        this._containers = new LinkedHashMap();
        this._redefinedGlobalTypes = new LinkedHashMap();
        this._redefinedModelGroups = new LinkedHashMap();
        this._redefinedAttributeGroups = new LinkedHashMap();
        this._globalTypes = new LinkedHashMap();
        this._globalElements = new LinkedHashMap();
        this._globalAttributes = new LinkedHashMap();
        this._modelGroups = new LinkedHashMap();
        this._attributeGroups = new LinkedHashMap();
        this._documentTypes = new LinkedHashMap();
        this._attributeTypes = new LinkedHashMap();
        this._typesByClassname = new LinkedHashMap();
        this._misspelledNames = new HashMap();
        this._processingGroups = new LinkedHashSet();
        this._idConstraints = new LinkedHashMap();
        this._namespaces = new HashSet();
        this._annotations = new ArrayList();
        this._mdefNamespaces = buildDefaultMdefNamespaces();
        this._sourceForUri = new HashMap();
        this._baseURI = URI.create("project://local/");
        this._s4sloader = XmlBeans.typeLoaderForClassLoader(SchemaDocument.class.getClassLoader());
    }

    public void initFromTypeSystem(SchemaTypeSystemImpl system, Set newNamespaces) {
        SchemaContainer[] containers = system.containers();
        for (int i = 0; i < containers.length; i++) {
            if (!newNamespaces.contains(containers[i].getNamespace())) {
                addContainer(containers[i]);
            }
        }
    }

    void addNewContainer(String namespace) {
        if (this._containers.containsKey(namespace)) {
            return;
        }
        SchemaContainer container = new SchemaContainer(namespace);
        container.setTypeSystem(sts());
        addNamespace(namespace);
        this._containers.put(namespace, container);
    }

    private void addContainer(SchemaContainer container) {
        this._containers.put(container.getNamespace(), container);
        List redefModelGroups = container.redefinedModelGroups();
        for (int i = 0; i < redefModelGroups.size(); i++) {
            QName name = ((SchemaModelGroup) redefModelGroups.get(i)).getName();
            this._redefinedModelGroups.put(name, redefModelGroups.get(i));
        }
        List redefAttrGroups = container.redefinedAttributeGroups();
        for (int i2 = 0; i2 < redefAttrGroups.size(); i2++) {
            QName name2 = ((SchemaAttributeGroup) redefAttrGroups.get(i2)).getName();
            this._redefinedAttributeGroups.put(name2, redefAttrGroups.get(i2));
        }
        List redefTypes = container.redefinedGlobalTypes();
        for (int i3 = 0; i3 < redefTypes.size(); i3++) {
            QName name3 = ((SchemaType) redefTypes.get(i3)).getName();
            this._redefinedGlobalTypes.put(name3, redefTypes.get(i3));
        }
        List globalElems = container.globalElements();
        for (int i4 = 0; i4 < globalElems.size(); i4++) {
            QName name4 = ((SchemaGlobalElement) globalElems.get(i4)).getName();
            this._globalElements.put(name4, globalElems.get(i4));
        }
        List globalAtts = container.globalAttributes();
        for (int i5 = 0; i5 < globalAtts.size(); i5++) {
            QName name5 = ((SchemaGlobalAttribute) globalAtts.get(i5)).getName();
            this._globalAttributes.put(name5, globalAtts.get(i5));
        }
        List modelGroups = container.modelGroups();
        for (int i6 = 0; i6 < modelGroups.size(); i6++) {
            QName name6 = ((SchemaModelGroup) modelGroups.get(i6)).getName();
            this._modelGroups.put(name6, modelGroups.get(i6));
        }
        List attrGroups = container.attributeGroups();
        for (int i7 = 0; i7 < attrGroups.size(); i7++) {
            QName name7 = ((SchemaAttributeGroup) attrGroups.get(i7)).getName();
            this._attributeGroups.put(name7, attrGroups.get(i7));
        }
        List globalTypes = container.globalTypes();
        for (int i8 = 0; i8 < globalTypes.size(); i8++) {
            SchemaType t = (SchemaType) globalTypes.get(i8);
            QName name8 = t.getName();
            this._globalTypes.put(name8, t);
            if (t.getFullJavaName() != null) {
                addClassname(t.getFullJavaName(), t);
            }
        }
        List documentTypes = container.documentTypes();
        for (int i9 = 0; i9 < documentTypes.size(); i9++) {
            SchemaType t2 = (SchemaType) documentTypes.get(i9);
            QName name9 = t2.getProperties()[0].getName();
            this._documentTypes.put(name9, t2);
            if (t2.getFullJavaName() != null) {
                addClassname(t2.getFullJavaName(), t2);
            }
        }
        List attributeTypes = container.attributeTypes();
        for (int i10 = 0; i10 < attributeTypes.size(); i10++) {
            SchemaType t3 = (SchemaType) attributeTypes.get(i10);
            QName name10 = t3.getProperties()[0].getName();
            this._attributeTypes.put(name10, t3);
            if (t3.getFullJavaName() != null) {
                addClassname(t3.getFullJavaName(), t3);
            }
        }
        List identityConstraints = container.identityConstraints();
        for (int i11 = 0; i11 < identityConstraints.size(); i11++) {
            QName name11 = ((SchemaIdentityConstraint) identityConstraints.get(i11)).getName();
            this._idConstraints.put(name11, identityConstraints.get(i11));
        }
        this._annotations.addAll(container.annotations());
        this._namespaces.add(container.getNamespace());
        container.unsetImmutable();
    }

    SchemaContainer getContainer(String namespace) {
        return (SchemaContainer) this._containers.get(namespace);
    }

    Map getContainerMap() {
        return Collections.unmodifiableMap(this._containers);
    }

    void registerDependency(String sourceNs, String targetNs) {
        this._dependencies.registerDependency(sourceNs, targetNs);
    }

    void registerContribution(String ns, String fileUrl) {
        this._dependencies.registerContribution(ns, fileUrl);
    }

    SchemaDependencies getDependencies() {
        return this._dependencies;
    }

    void setDependencies(SchemaDependencies deps) {
        this._dependencies = deps;
    }

    boolean isFileProcessed(String url) {
        return this._dependencies.isFileRepresented(url);
    }

    public void setImportingTypeLoader(SchemaTypeLoader loader) {
        this._importingLoader = loader;
    }

    public void setErrorListener(Collection errorListener) {
        this._errorListener = errorListener;
    }

    public void error(String message, int code, XmlObject loc) {
        addError(this._errorListener, message, code, loc);
    }

    public void error(String code, Object[] args, XmlObject loc) {
        addError(this._errorListener, code, args, loc);
    }

    public void recover(String code, Object[] args, XmlObject loc) {
        addError(this._errorListener, code, args, loc);
        this._recoveredErrors++;
    }

    public void warning(String message, int code, XmlObject loc) {
        addWarning(this._errorListener, message, code, loc);
    }

    public void warning(String code, Object[] args, XmlObject loc) {
        if (code == XmlErrorCodes.RESERVED_TYPE_NAME && loc.documentProperties().getSourceName() != null && loc.documentProperties().getSourceName().indexOf("XMLSchema.xsd") > 0) {
            return;
        }
        addWarning(this._errorListener, code, args, loc);
    }

    public void info(String message) {
        addInfo(this._errorListener, message);
    }

    public void info(String code, Object[] args) {
        addInfo(this._errorListener, code, args);
    }

    public static void addError(Collection errorListener, String message, int code, XmlObject location) {
        XmlError err = XmlError.forObject(message, 0, location);
        errorListener.add(err);
    }

    public static void addError(Collection errorListener, String code, Object[] args, XmlObject location) {
        XmlError err = XmlError.forObject(code, args, 0, location);
        errorListener.add(err);
    }

    public static void addError(Collection errorListener, String code, Object[] args, File location) {
        XmlError err = XmlError.forLocation(code, args, 0, location.toURI().toString(), 0, 0, 0);
        errorListener.add(err);
    }

    public static void addError(Collection errorListener, String code, Object[] args, URL location) {
        XmlError err = XmlError.forLocation(code, args, 0, location.toString(), 0, 0, 0);
        errorListener.add(err);
    }

    public static void addWarning(Collection errorListener, String message, int code, XmlObject location) {
        XmlError err = XmlError.forObject(message, 1, location);
        errorListener.add(err);
    }

    public static void addWarning(Collection errorListener, String code, Object[] args, XmlObject location) {
        XmlError err = XmlError.forObject(code, args, 1, location);
        errorListener.add(err);
    }

    public static void addInfo(Collection errorListener, String message) {
        XmlError err = XmlError.forMessage(message, 2);
        errorListener.add(err);
    }

    public static void addInfo(Collection errorListener, String code, Object[] args) {
        XmlError err = XmlError.forMessage(code, args, 2);
        errorListener.add(err);
    }

    public void setGivenTypeSystemName(String name) {
        this._givenStsName = name;
    }

    public void setTargetSchemaTypeSystem(SchemaTypeSystemImpl target) {
        this._target = target;
    }

    public void addSchemaDigest(byte[] digest) {
        if (this._noDigest) {
            return;
        }
        if (digest == null) {
            this._noDigest = true;
            this._digest = null;
            return;
        }
        if (this._digest == null) {
            this._digest = new byte[16];
        }
        int len = this._digest.length;
        if (digest.length < len) {
            len = digest.length;
        }
        for (int i = 0; i < len; i++) {
            byte[] bArr = this._digest;
            int i2 = i;
            bArr[i2] = (byte) (bArr[i2] ^ digest[i]);
        }
    }

    public SchemaTypeSystemImpl sts() {
        if (this._target != null) {
            return this._target;
        }
        String name = this._givenStsName;
        if (name == null && this._digest != null) {
            name = ExcelXmlConstants.CELL_DATA_FORMAT_TAG + new String(HexBin.encode(this._digest));
        }
        this._target = new SchemaTypeSystemImpl(name);
        return this._target;
    }

    public boolean shouldDownloadURI(String uriString) {
        if (this._doingDownloads) {
            return true;
        }
        if (uriString == null) {
            return false;
        }
        try {
            URI uri = new URI(uriString);
            if (uri.getScheme().equalsIgnoreCase("jar") || uri.getScheme().equalsIgnoreCase("zip")) {
                String s = uri.getSchemeSpecificPart();
                int i = s.lastIndexOf(33);
                return shouldDownloadURI(i > 0 ? s.substring(0, i) : s);
            }
            return uri.getScheme().equalsIgnoreCase("file");
        } catch (Exception e) {
            return false;
        }
    }

    public void setOptions(XmlOptions options) {
        if (options == null) {
            return;
        }
        this._allowPartial = options.hasOption("COMPILE_PARTIAL_TYPESYSTEM");
        this._compatMap = (Map) options.get(XmlOptions.COMPILE_SUBSTITUTE_NAMES);
        boolean z = options.hasOption(XmlOptions.COMPILE_NO_UPA_RULE) || !"true".equals(SystemProperties.getProperty("xmlbean.uniqueparticleattribution", "true"));
        this._noUpa = z;
        boolean z2 = options.hasOption(XmlOptions.COMPILE_NO_PVR_RULE) || !"true".equals(SystemProperties.getProperty("xmlbean.particlerestriction", "true"));
        this._noPvr = z2;
        boolean z3 = options.hasOption(XmlOptions.COMPILE_NO_ANNOTATIONS) || !"true".equals(SystemProperties.getProperty("xmlbean.schemaannotations", "true"));
        this._noAnn = z3;
        this._doingDownloads = options.hasOption(XmlOptions.COMPILE_DOWNLOAD_URLS) ? true : "true".equals(SystemProperties.getProperty("xmlbean.downloadurls", "false"));
        this._entityResolver = (EntityResolver) options.get(XmlOptions.ENTITY_RESOLVER);
        if (this._entityResolver == null) {
            this._entityResolver = ResolverUtil.getGlobalEntityResolver();
        }
        if (this._entityResolver != null) {
            this._doingDownloads = true;
        }
        if (options.hasOption(XmlOptions.COMPILE_MDEF_NAMESPACES)) {
            this._mdefNamespaces.addAll((Collection) options.get(XmlOptions.COMPILE_MDEF_NAMESPACES));
            if (this._mdefNamespaces.contains("##local")) {
                this._mdefNamespaces.remove("##local");
                this._mdefNamespaces.add("");
            }
            if (this._mdefNamespaces.contains("##any")) {
                this._mdefNamespaces.remove("##any");
                this._mdefAll = true;
            }
        }
    }

    public EntityResolver getEntityResolver() {
        return this._entityResolver;
    }

    public boolean noUpa() {
        return this._noUpa;
    }

    public boolean noPvr() {
        return this._noPvr;
    }

    public boolean noAnn() {
        return this._noAnn;
    }

    public boolean allowPartial() {
        return this._allowPartial;
    }

    public int getRecovered() {
        return this._recoveredErrors;
    }

    private QName compatName(QName name, String chameleonNamespace) {
        if (name.getNamespaceURI().length() == 0 && chameleonNamespace != null && chameleonNamespace.length() > 0) {
            name = new QName(chameleonNamespace, name.getLocalPart());
        }
        if (this._compatMap == null) {
            return name;
        }
        QName subst = (QName) this._compatMap.get(name);
        if (subst == null) {
            return name;
        }
        return subst;
    }

    public void setBindingConfig(BindingConfig config) throws IllegalArgumentException {
        this._config = config;
    }

    public BindingConfig getBindingConfig() throws IllegalArgumentException {
        return this._config;
    }

    public String getPackageOverride(String namespace) {
        if (this._config == null) {
            return null;
        }
        return this._config.lookupPackageForNamespace(namespace);
    }

    public String getJavaPrefix(String namespace) {
        if (this._config == null) {
            return null;
        }
        return this._config.lookupPrefixForNamespace(namespace);
    }

    public String getJavaSuffix(String namespace) {
        if (this._config == null) {
            return null;
        }
        return this._config.lookupSuffixForNamespace(namespace);
    }

    public String getJavaname(QName qname, int kind) {
        if (this._config == null) {
            return null;
        }
        return this._config.lookupJavanameForQName(qname, kind);
    }

    private static String crunchName(QName name) {
        return name.getLocalPart().toLowerCase();
    }

    void addSpelling(QName name, SchemaComponent comp) {
        this._misspelledNames.put(crunchName(name), comp);
    }

    SchemaComponent findSpelling(QName name) {
        return (SchemaComponent) this._misspelledNames.get(crunchName(name));
    }

    void addNamespace(String targetNamespace) {
        this._namespaces.add(targetNamespace);
    }

    String[] getNamespaces() {
        return (String[]) this._namespaces.toArray(new String[this._namespaces.size()]);
    }

    boolean linkerDefinesNamespace(String namespace) {
        return this._importingLoader.isNamespaceDefined(namespace);
    }

    SchemaTypeImpl findGlobalType(QName name, String chameleonNamespace, String sourceNamespace) {
        QName name2 = compatName(name, chameleonNamespace);
        SchemaTypeImpl result = (SchemaTypeImpl) this._globalTypes.get(name2);
        boolean foundOnLoader = false;
        if (result == null) {
            result = (SchemaTypeImpl) this._importingLoader.findType(name2);
            foundOnLoader = result != null;
        }
        if (!foundOnLoader && sourceNamespace != null) {
            registerDependency(sourceNamespace, name2.getNamespaceURI());
        }
        return result;
    }

    SchemaTypeImpl findRedefinedGlobalType(QName name, String chameleonNamespace, SchemaTypeImpl redefinedBy) {
        QName redefinedName = redefinedBy.getName();
        QName name2 = compatName(name, chameleonNamespace);
        if (name2.equals(redefinedName)) {
            return (SchemaTypeImpl) this._redefinedGlobalTypes.get(redefinedBy);
        }
        SchemaTypeImpl result = (SchemaTypeImpl) this._globalTypes.get(name2);
        if (result == null) {
            result = (SchemaTypeImpl) this._importingLoader.findType(name2);
        }
        return result;
    }

    void addGlobalType(SchemaTypeImpl type, SchemaTypeImpl redefined) {
        if (type != null) {
            QName name = type.getName();
            SchemaContainer container = getContainer(name.getNamespaceURI());
            if (!$assertionsDisabled && (container == null || container != type.getContainer())) {
                throw new AssertionError();
            }
            if (redefined != null) {
                if (this._redefinedGlobalTypes.containsKey(redefined)) {
                    if (!ignoreMdef(name)) {
                        if (this._mdefAll) {
                            warning(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"global type", QNameHelper.pretty(name), ((SchemaType) this._redefinedGlobalTypes.get(redefined)).getSourceName()}, type.getParseObject());
                            return;
                        } else {
                            error(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"global type", QNameHelper.pretty(name), ((SchemaType) this._redefinedGlobalTypes.get(redefined)).getSourceName()}, type.getParseObject());
                            return;
                        }
                    }
                    return;
                }
                this._redefinedGlobalTypes.put(redefined, type);
                container.addRedefinedType(type.getRef());
                return;
            }
            if (this._globalTypes.containsKey(name)) {
                if (!ignoreMdef(name)) {
                    if (this._mdefAll) {
                        warning(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"global type", QNameHelper.pretty(name), ((SchemaType) this._globalTypes.get(name)).getSourceName()}, type.getParseObject());
                        return;
                    } else {
                        error(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"global type", QNameHelper.pretty(name), ((SchemaType) this._globalTypes.get(name)).getSourceName()}, type.getParseObject());
                        return;
                    }
                }
                return;
            }
            this._globalTypes.put(name, type);
            container.addGlobalType(type.getRef());
            addSpelling(name, type);
        }
    }

    private boolean ignoreMdef(QName name) {
        return this._mdefNamespaces.contains(name.getNamespaceURI());
    }

    SchemaType[] globalTypes() {
        return (SchemaType[]) this._globalTypes.values().toArray(new SchemaType[this._globalTypes.size()]);
    }

    SchemaType[] redefinedGlobalTypes() {
        return (SchemaType[]) this._redefinedGlobalTypes.values().toArray(new SchemaType[this._redefinedGlobalTypes.size()]);
    }

    SchemaTypeImpl findDocumentType(QName name, String chameleonNamespace, String sourceNamespace) {
        QName name2 = compatName(name, chameleonNamespace);
        SchemaTypeImpl result = (SchemaTypeImpl) this._documentTypes.get(name2);
        boolean foundOnLoader = false;
        if (result == null) {
            result = (SchemaTypeImpl) this._importingLoader.findDocumentType(name2);
            foundOnLoader = result != null;
        }
        if (!foundOnLoader && sourceNamespace != null) {
            registerDependency(sourceNamespace, name2.getNamespaceURI());
        }
        return result;
    }

    void addDocumentType(SchemaTypeImpl type, QName name) {
        if (this._documentTypes.containsKey(name)) {
            if (!ignoreMdef(name)) {
                if (this._mdefAll) {
                    warning(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"global element", QNameHelper.pretty(name), ((SchemaComponent) this._documentTypes.get(name)).getSourceName()}, type.getParseObject());
                    return;
                } else {
                    error(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"global element", QNameHelper.pretty(name), ((SchemaComponent) this._documentTypes.get(name)).getSourceName()}, type.getParseObject());
                    return;
                }
            }
            return;
        }
        this._documentTypes.put(name, type);
        SchemaContainer container = getContainer(name.getNamespaceURI());
        if (!$assertionsDisabled && (container == null || container != type.getContainer())) {
            throw new AssertionError();
        }
        container.addDocumentType(type.getRef());
    }

    SchemaType[] documentTypes() {
        return (SchemaType[]) this._documentTypes.values().toArray(new SchemaType[this._documentTypes.size()]);
    }

    SchemaTypeImpl findAttributeType(QName name, String chameleonNamespace, String sourceNamespace) {
        QName name2 = compatName(name, chameleonNamespace);
        SchemaTypeImpl result = (SchemaTypeImpl) this._attributeTypes.get(name2);
        boolean foundOnLoader = false;
        if (result == null) {
            result = (SchemaTypeImpl) this._importingLoader.findAttributeType(name2);
            foundOnLoader = result != null;
        }
        if (!foundOnLoader && sourceNamespace != null) {
            registerDependency(sourceNamespace, name2.getNamespaceURI());
        }
        return result;
    }

    void addAttributeType(SchemaTypeImpl type, QName name) {
        if (this._attributeTypes.containsKey(name)) {
            if (!ignoreMdef(name)) {
                if (this._mdefAll) {
                    warning(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"global attribute", QNameHelper.pretty(name), ((SchemaComponent) this._attributeTypes.get(name)).getSourceName()}, type.getParseObject());
                    return;
                } else {
                    error(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"global attribute", QNameHelper.pretty(name), ((SchemaComponent) this._attributeTypes.get(name)).getSourceName()}, type.getParseObject());
                    return;
                }
            }
            return;
        }
        this._attributeTypes.put(name, type);
        SchemaContainer container = getContainer(name.getNamespaceURI());
        if (!$assertionsDisabled && (container == null || container != type.getContainer())) {
            throw new AssertionError();
        }
        container.addAttributeType(type.getRef());
    }

    SchemaType[] attributeTypes() {
        return (SchemaType[]) this._attributeTypes.values().toArray(new SchemaType[this._attributeTypes.size()]);
    }

    SchemaGlobalAttributeImpl findGlobalAttribute(QName name, String chameleonNamespace, String sourceNamespace) {
        QName name2 = compatName(name, chameleonNamespace);
        SchemaGlobalAttributeImpl result = (SchemaGlobalAttributeImpl) this._globalAttributes.get(name2);
        boolean foundOnLoader = false;
        if (result == null) {
            result = (SchemaGlobalAttributeImpl) this._importingLoader.findAttribute(name2);
            foundOnLoader = result != null;
        }
        if (!foundOnLoader && sourceNamespace != null) {
            registerDependency(sourceNamespace, name2.getNamespaceURI());
        }
        return result;
    }

    void addGlobalAttribute(SchemaGlobalAttributeImpl attribute) {
        if (attribute != null) {
            QName name = attribute.getName();
            this._globalAttributes.put(name, attribute);
            addSpelling(name, attribute);
            SchemaContainer container = getContainer(name.getNamespaceURI());
            if (!$assertionsDisabled && (container == null || container != attribute.getContainer())) {
                throw new AssertionError();
            }
            container.addGlobalAttribute(attribute.getRef());
        }
    }

    SchemaGlobalAttribute[] globalAttributes() {
        return (SchemaGlobalAttribute[]) this._globalAttributes.values().toArray(new SchemaGlobalAttribute[this._globalAttributes.size()]);
    }

    SchemaGlobalElementImpl findGlobalElement(QName name, String chameleonNamespace, String sourceNamespace) {
        QName name2 = compatName(name, chameleonNamespace);
        SchemaGlobalElementImpl result = (SchemaGlobalElementImpl) this._globalElements.get(name2);
        boolean foundOnLoader = false;
        if (result == null) {
            result = (SchemaGlobalElementImpl) this._importingLoader.findElement(name2);
            foundOnLoader = result != null;
        }
        if (!foundOnLoader && sourceNamespace != null) {
            registerDependency(sourceNamespace, name2.getNamespaceURI());
        }
        return result;
    }

    void addGlobalElement(SchemaGlobalElementImpl element) {
        if (element != null) {
            QName name = element.getName();
            this._globalElements.put(name, element);
            SchemaContainer container = getContainer(name.getNamespaceURI());
            if (!$assertionsDisabled && (container == null || container != element.getContainer())) {
                throw new AssertionError();
            }
            container.addGlobalElement(element.getRef());
            addSpelling(name, element);
        }
    }

    SchemaGlobalElement[] globalElements() {
        return (SchemaGlobalElement[]) this._globalElements.values().toArray(new SchemaGlobalElement[this._globalElements.size()]);
    }

    SchemaAttributeGroupImpl findAttributeGroup(QName name, String chameleonNamespace, String sourceNamespace) {
        QName name2 = compatName(name, chameleonNamespace);
        SchemaAttributeGroupImpl result = (SchemaAttributeGroupImpl) this._attributeGroups.get(name2);
        boolean foundOnLoader = false;
        if (result == null) {
            result = (SchemaAttributeGroupImpl) this._importingLoader.findAttributeGroup(name2);
            foundOnLoader = result != null;
        }
        if (!foundOnLoader && sourceNamespace != null) {
            registerDependency(sourceNamespace, name2.getNamespaceURI());
        }
        return result;
    }

    SchemaAttributeGroupImpl findRedefinedAttributeGroup(QName name, String chameleonNamespace, SchemaAttributeGroupImpl redefinedBy) {
        QName redefinitionFor = redefinedBy.getName();
        QName name2 = compatName(name, chameleonNamespace);
        if (name2.equals(redefinitionFor)) {
            return (SchemaAttributeGroupImpl) this._redefinedAttributeGroups.get(redefinedBy);
        }
        SchemaAttributeGroupImpl result = (SchemaAttributeGroupImpl) this._attributeGroups.get(name2);
        if (result == null) {
            result = (SchemaAttributeGroupImpl) this._importingLoader.findAttributeGroup(name2);
        }
        return result;
    }

    void addAttributeGroup(SchemaAttributeGroupImpl attributeGroup, SchemaAttributeGroupImpl redefined) {
        if (attributeGroup != null) {
            QName name = attributeGroup.getName();
            SchemaContainer container = getContainer(name.getNamespaceURI());
            if (!$assertionsDisabled && (container == null || container != attributeGroup.getContainer())) {
                throw new AssertionError();
            }
            if (redefined != null) {
                if (this._redefinedAttributeGroups.containsKey(redefined)) {
                    if (!ignoreMdef(name)) {
                        if (this._mdefAll) {
                            warning(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"attribute group", QNameHelper.pretty(name), ((SchemaComponent) this._redefinedAttributeGroups.get(redefined)).getSourceName()}, attributeGroup.getParseObject());
                            return;
                        } else {
                            error(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"attribute group", QNameHelper.pretty(name), ((SchemaComponent) this._redefinedAttributeGroups.get(redefined)).getSourceName()}, attributeGroup.getParseObject());
                            return;
                        }
                    }
                    return;
                }
                this._redefinedAttributeGroups.put(redefined, attributeGroup);
                container.addRedefinedAttributeGroup(attributeGroup.getRef());
                return;
            }
            if (this._attributeGroups.containsKey(name)) {
                if (!ignoreMdef(name)) {
                    if (this._mdefAll) {
                        warning(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"attribute group", QNameHelper.pretty(name), ((SchemaComponent) this._attributeGroups.get(name)).getSourceName()}, attributeGroup.getParseObject());
                        return;
                    } else {
                        error(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"attribute group", QNameHelper.pretty(name), ((SchemaComponent) this._attributeGroups.get(name)).getSourceName()}, attributeGroup.getParseObject());
                        return;
                    }
                }
                return;
            }
            this._attributeGroups.put(attributeGroup.getName(), attributeGroup);
            addSpelling(attributeGroup.getName(), attributeGroup);
            container.addAttributeGroup(attributeGroup.getRef());
        }
    }

    SchemaAttributeGroup[] attributeGroups() {
        return (SchemaAttributeGroup[]) this._attributeGroups.values().toArray(new SchemaAttributeGroup[this._attributeGroups.size()]);
    }

    SchemaAttributeGroup[] redefinedAttributeGroups() {
        return (SchemaAttributeGroup[]) this._redefinedAttributeGroups.values().toArray(new SchemaAttributeGroup[this._redefinedAttributeGroups.size()]);
    }

    SchemaModelGroupImpl findModelGroup(QName name, String chameleonNamespace, String sourceNamespace) {
        QName name2 = compatName(name, chameleonNamespace);
        SchemaModelGroupImpl result = (SchemaModelGroupImpl) this._modelGroups.get(name2);
        boolean foundOnLoader = false;
        if (result == null) {
            result = (SchemaModelGroupImpl) this._importingLoader.findModelGroup(name2);
            foundOnLoader = result != null;
        }
        if (!foundOnLoader && sourceNamespace != null) {
            registerDependency(sourceNamespace, name2.getNamespaceURI());
        }
        return result;
    }

    SchemaModelGroupImpl findRedefinedModelGroup(QName name, String chameleonNamespace, SchemaModelGroupImpl redefinedBy) {
        QName redefinitionFor = redefinedBy.getName();
        QName name2 = compatName(name, chameleonNamespace);
        if (name2.equals(redefinitionFor)) {
            return (SchemaModelGroupImpl) this._redefinedModelGroups.get(redefinedBy);
        }
        SchemaModelGroupImpl result = (SchemaModelGroupImpl) this._modelGroups.get(name2);
        if (result == null) {
            result = (SchemaModelGroupImpl) this._importingLoader.findModelGroup(name2);
        }
        return result;
    }

    void addModelGroup(SchemaModelGroupImpl modelGroup, SchemaModelGroupImpl redefined) {
        if (modelGroup != null) {
            QName name = modelGroup.getName();
            SchemaContainer container = getContainer(name.getNamespaceURI());
            if (!$assertionsDisabled && (container == null || container != modelGroup.getContainer())) {
                throw new AssertionError();
            }
            if (redefined != null) {
                if (this._redefinedModelGroups.containsKey(redefined)) {
                    if (!ignoreMdef(name)) {
                        if (this._mdefAll) {
                            warning(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"model group", QNameHelper.pretty(name), ((SchemaComponent) this._redefinedModelGroups.get(redefined)).getSourceName()}, modelGroup.getParseObject());
                            return;
                        } else {
                            error(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"model group", QNameHelper.pretty(name), ((SchemaComponent) this._redefinedModelGroups.get(redefined)).getSourceName()}, modelGroup.getParseObject());
                            return;
                        }
                    }
                    return;
                }
                this._redefinedModelGroups.put(redefined, modelGroup);
                container.addRedefinedModelGroup(modelGroup.getRef());
                return;
            }
            if (this._modelGroups.containsKey(name)) {
                if (!ignoreMdef(name)) {
                    if (this._mdefAll) {
                        warning(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"model group", QNameHelper.pretty(name), ((SchemaComponent) this._modelGroups.get(name)).getSourceName()}, modelGroup.getParseObject());
                        return;
                    } else {
                        error(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"model group", QNameHelper.pretty(name), ((SchemaComponent) this._modelGroups.get(name)).getSourceName()}, modelGroup.getParseObject());
                        return;
                    }
                }
                return;
            }
            this._modelGroups.put(modelGroup.getName(), modelGroup);
            addSpelling(modelGroup.getName(), modelGroup);
            container.addModelGroup(modelGroup.getRef());
        }
    }

    SchemaModelGroup[] modelGroups() {
        return (SchemaModelGroup[]) this._modelGroups.values().toArray(new SchemaModelGroup[this._modelGroups.size()]);
    }

    SchemaModelGroup[] redefinedModelGroups() {
        return (SchemaModelGroup[]) this._redefinedModelGroups.values().toArray(new SchemaModelGroup[this._redefinedModelGroups.size()]);
    }

    SchemaIdentityConstraintImpl findIdConstraint(QName name, String chameleonNamespace, String sourceNamespace) {
        QName name2 = compatName(name, chameleonNamespace);
        if (sourceNamespace != null) {
            registerDependency(sourceNamespace, name2.getNamespaceURI());
        }
        return (SchemaIdentityConstraintImpl) this._idConstraints.get(name2);
    }

    void addIdConstraint(SchemaIdentityConstraintImpl idc) {
        if (idc != null) {
            QName name = idc.getName();
            SchemaContainer container = getContainer(name.getNamespaceURI());
            if (!$assertionsDisabled && (container == null || container != idc.getContainer())) {
                throw new AssertionError();
            }
            if (this._idConstraints.containsKey(name)) {
                if (!ignoreMdef(name)) {
                    warning(XmlErrorCodes.SCHEMA_PROPERTIES$DUPLICATE, new Object[]{"identity constraint", QNameHelper.pretty(name), ((SchemaComponent) this._idConstraints.get(name)).getSourceName()}, idc.getParseObject());
                }
            } else {
                this._idConstraints.put(name, idc);
                addSpelling(idc.getName(), idc);
                container.addIdentityConstraint(idc.getRef());
            }
        }
    }

    SchemaIdentityConstraintImpl[] idConstraints() {
        return (SchemaIdentityConstraintImpl[]) this._idConstraints.values().toArray(new SchemaIdentityConstraintImpl[this._idConstraints.size()]);
    }

    void addAnnotation(SchemaAnnotationImpl ann, String targetNamespace) {
        if (ann != null) {
            SchemaContainer container = getContainer(targetNamespace);
            if (!$assertionsDisabled && (container == null || container != ann.getContainer())) {
                throw new AssertionError();
            }
            this._annotations.add(ann);
            container.addAnnotation(ann);
        }
    }

    List annotations() {
        return this._annotations;
    }

    boolean isProcessing(Object obj) {
        return this._processingGroups.contains(obj);
    }

    void startProcessing(Object obj) {
        if (!$assertionsDisabled && this._processingGroups.contains(obj)) {
            throw new AssertionError();
        }
        this._processingGroups.add(obj);
    }

    void finishProcessing(Object obj) {
        if (!$assertionsDisabled && !this._processingGroups.contains(obj)) {
            throw new AssertionError();
        }
        this._processingGroups.remove(obj);
    }

    Object[] getCurrentProcessing() {
        return this._processingGroups.toArray();
    }

    Map typesByClassname() {
        return Collections.unmodifiableMap(this._typesByClassname);
    }

    void addClassname(String classname, SchemaType type) {
        this._typesByClassname.put(classname, type);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscState$StscStack.class */
    private static final class StscStack {
        StscState current;
        ArrayList stack;

        private StscStack() {
            this.stack = new ArrayList();
        }

        final StscState push() {
            this.stack.add(this.current);
            this.current = new StscState();
            return this.current;
        }

        final void pop() {
            this.current = (StscState) this.stack.get(this.stack.size() - 1);
            this.stack.remove(this.stack.size() - 1);
        }
    }

    public static void clearThreadLocals() {
        tl_stscStack.remove();
    }

    public static StscState start() {
        StscStack stscStack = (StscStack) tl_stscStack.get();
        if (stscStack == null) {
            stscStack = new StscStack();
            tl_stscStack.set(stscStack);
        }
        return stscStack.push();
    }

    public static StscState get() {
        return ((StscStack) tl_stscStack.get()).current;
    }

    public static void end() {
        StscStack stscStack = (StscStack) tl_stscStack.get();
        stscStack.pop();
        if (stscStack.stack.size() == 0) {
            tl_stscStack.set(null);
        }
    }

    static XmlValueRef build_wsstring(int wsr) {
        switch (wsr) {
            case 1:
                return XMLSTR_PRESERVE;
            case 2:
                return XMLSTR_REPLACE;
            case 3:
                return XMLSTR_COLLAPSE;
            default:
                return null;
        }
    }

    static XmlValueRef buildString(String str) {
        if (str == null) {
            return null;
        }
        try {
            XmlStringImpl i = new XmlStringImpl();
            i.set(str);
            i.setImmutable();
            return new XmlValueRef(i);
        } catch (XmlValueOutOfRangeException e) {
            return null;
        }
    }

    public void notFoundError(QName itemName, int code, XmlObject loc, boolean recovered) {
        String expected;
        QName name;
        String expectedName = QNameHelper.pretty(itemName);
        String found = null;
        String foundName = null;
        String sourceName = null;
        if (recovered) {
            this._recoveredErrors++;
        }
        switch (code) {
            case 0:
                expected = "type";
                break;
            case 1:
                expected = "element";
                break;
            case 2:
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                expected = "definition";
                break;
            case 3:
                expected = BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT;
                break;
            case 4:
                expected = "attribute group";
                break;
            case 5:
                expected = "identity constraint";
                break;
            case 6:
                expected = "model group";
                break;
        }
        SchemaComponent foundComponent = findSpelling(itemName);
        if (foundComponent != null && (name = foundComponent.getName()) != null) {
            switch (foundComponent.getComponentType()) {
                case 0:
                    found = "type";
                    sourceName = ((SchemaType) foundComponent).getSourceName();
                    break;
                case 1:
                    found = "element";
                    sourceName = ((SchemaGlobalElement) foundComponent).getSourceName();
                    break;
                case 3:
                    found = BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT;
                    sourceName = ((SchemaGlobalAttribute) foundComponent).getSourceName();
                    break;
                case 4:
                    found = "attribute group";
                    break;
                case 6:
                    found = "model group";
                    break;
            }
            if (sourceName != null) {
                sourceName = sourceName.substring(sourceName.lastIndexOf(47) + 1);
            }
            if (!name.equals(itemName)) {
                foundName = QNameHelper.pretty(name);
            }
        }
        if (found == null) {
            error(XmlErrorCodes.SCHEMA_QNAME_RESOLVE, new Object[]{expected, expectedName}, loc);
            return;
        }
        Object[] objArr = new Object[7];
        objArr[0] = expected;
        objArr[1] = expectedName;
        objArr[2] = found;
        objArr[3] = foundName == null ? new Integer(0) : new Integer(1);
        objArr[4] = foundName;
        objArr[5] = sourceName == null ? new Integer(0) : new Integer(1);
        objArr[6] = sourceName;
        error(XmlErrorCodes.SCHEMA_QNAME_RESOLVE$HELP, objArr, loc);
    }

    public String sourceNameForUri(String uri) {
        return (String) this._sourceForUri.get(uri);
    }

    public Map sourceCopyMap() {
        return Collections.unmodifiableMap(this._sourceForUri);
    }

    public void setBaseUri(URI uri) {
        this._baseURI = uri;
    }

    public String relativize(String uri) {
        return relativize(uri, false);
    }

    public String computeSavedFilename(String uri) {
        return relativize(uri, true);
    }

    private String relativize(String uri, boolean forSavedFilename) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (uri == null) {
            return null;
        }
        if (uri.startsWith("/")) {
            uri = PROJECT_URL_PREFIX + uri.replace('\\', '/');
        } else {
            int colon = uri.indexOf(58);
            if (colon <= 1 || !uri.substring(0, colon).matches("^\\w+$")) {
                uri = "project://local/" + uri.replace('\\', '/');
            }
        }
        if (this._baseURI != null) {
            try {
                URI relative = this._baseURI.relativize(new URI(uri));
                if (!relative.isAbsolute()) {
                    return relative.toString();
                }
                uri = relative.toString();
            } catch (URISyntaxException e) {
            }
        }
        if (!forSavedFilename) {
            return uri;
        }
        int lastslash = uri.lastIndexOf(47);
        String dir = QNameHelper.hexsafe(lastslash == -1 ? "" : uri.substring(0, lastslash));
        int question = uri.indexOf(63, lastslash + 1);
        if (question == -1) {
            return dir + "/" + uri.substring(lastslash + 1);
        }
        String query = QNameHelper.hexsafe(question == -1 ? "" : uri.substring(question));
        if (query.startsWith(QNameHelper.URI_SHA1_PREFIX)) {
            return dir + "/" + uri.substring(lastslash + 1, question);
        }
        return dir + "/" + uri.substring(lastslash + 1, question) + query;
    }

    public void addSourceUri(String uri, String nameToUse) {
        if (uri == null) {
            return;
        }
        if (nameToUse == null) {
            nameToUse = computeSavedFilename(uri);
        }
        this._sourceForUri.put(uri, nameToUse);
    }

    public Collection getErrorListener() {
        return this._errorListener;
    }

    public SchemaTypeLoader getS4SLoader() {
        return this._s4sloader;
    }

    public File getSchemasDir() {
        return this._schemasDir;
    }

    public void setSchemasDir(File _schemasDir) {
        this._schemasDir = _schemasDir;
    }
}
