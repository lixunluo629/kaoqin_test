package org.apache.xmlbeans.impl.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.BindingConfig;
import org.apache.xmlbeans.InterfaceExtension;
import org.apache.xmlbeans.PrePostExtension;
import org.apache.xmlbeans.UserType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.config.InterfaceExtensionImpl;
import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.apache.xmlbeans.impl.jam.JamService;
import org.apache.xmlbeans.impl.jam.JamServiceFactory;
import org.apache.xmlbeans.impl.jam.JamServiceParams;
import org.apache.xmlbeans.impl.schema.StscState;
import org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig;
import org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig;
import org.apache.xmlbeans.impl.xb.xmlconfig.Qnametargetenum;
import org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/config/BindingConfigImpl.class */
public class BindingConfigImpl extends BindingConfig {
    private Map _packageMap;
    private Map _prefixMap;
    private Map _suffixMap;
    private Map _packageMapByUriPrefix;
    private Map _prefixMapByUriPrefix;
    private Map _suffixMapByUriPrefix;
    private Map _qnameTypeMap;
    private Map _qnameDocTypeMap;
    private Map _qnameElemMap;
    private Map _qnameAttMap;
    private List _interfaceExtensions;
    private List _prePostExtensions;
    private Map _userTypes;

    private BindingConfigImpl() {
        this._packageMap = Collections.EMPTY_MAP;
        this._prefixMap = Collections.EMPTY_MAP;
        this._suffixMap = Collections.EMPTY_MAP;
        this._packageMapByUriPrefix = Collections.EMPTY_MAP;
        this._prefixMapByUriPrefix = Collections.EMPTY_MAP;
        this._suffixMapByUriPrefix = Collections.EMPTY_MAP;
        this._qnameTypeMap = Collections.EMPTY_MAP;
        this._qnameDocTypeMap = Collections.EMPTY_MAP;
        this._qnameElemMap = Collections.EMPTY_MAP;
        this._qnameAttMap = Collections.EMPTY_MAP;
        this._interfaceExtensions = new ArrayList();
        this._prePostExtensions = new ArrayList();
        this._userTypes = Collections.EMPTY_MAP;
    }

    public static BindingConfig forConfigDocuments(ConfigDocument.Config[] configs, File[] javaFiles, File[] classpath) {
        return new BindingConfigImpl(configs, javaFiles, classpath);
    }

    private BindingConfigImpl(ConfigDocument.Config[] configs, File[] javaFiles, File[] classpath) {
        this._packageMap = new LinkedHashMap();
        this._prefixMap = new LinkedHashMap();
        this._suffixMap = new LinkedHashMap();
        this._packageMapByUriPrefix = new LinkedHashMap();
        this._prefixMapByUriPrefix = new LinkedHashMap();
        this._suffixMapByUriPrefix = new LinkedHashMap();
        this._qnameTypeMap = new LinkedHashMap();
        this._qnameDocTypeMap = new LinkedHashMap();
        this._qnameElemMap = new LinkedHashMap();
        this._qnameAttMap = new LinkedHashMap();
        this._interfaceExtensions = new ArrayList();
        this._prePostExtensions = new ArrayList();
        this._userTypes = new LinkedHashMap();
        for (ConfigDocument.Config config : configs) {
            Nsconfig[] nsa = config.getNamespaceArray();
            for (int j = 0; j < nsa.length; j++) {
                recordNamespaceSetting(nsa[j].getUri(), nsa[j].getPackage(), this._packageMap);
                recordNamespaceSetting(nsa[j].getUri(), nsa[j].getPrefix(), this._prefixMap);
                recordNamespaceSetting(nsa[j].getUri(), nsa[j].getSuffix(), this._suffixMap);
                recordNamespacePrefixSetting(nsa[j].getUriprefix(), nsa[j].getPackage(), this._packageMapByUriPrefix);
                recordNamespacePrefixSetting(nsa[j].getUriprefix(), nsa[j].getPrefix(), this._prefixMapByUriPrefix);
                recordNamespacePrefixSetting(nsa[j].getUriprefix(), nsa[j].getSuffix(), this._suffixMapByUriPrefix);
            }
            Qnameconfig[] qnc = config.getQnameArray();
            for (int j2 = 0; j2 < qnc.length; j2++) {
                List applyto = qnc[j2].xgetTarget().xgetListValue();
                QName name = qnc[j2].getName();
                String javaname = qnc[j2].getJavaname();
                for (int k = 0; k < applyto.size(); k++) {
                    Qnametargetenum a = (Qnametargetenum) applyto.get(k);
                    switch (a.enumValue().intValue()) {
                        case 1:
                            this._qnameTypeMap.put(name, javaname);
                            break;
                        case 2:
                            this._qnameDocTypeMap.put(name, javaname);
                            break;
                        case 3:
                            this._qnameElemMap.put(name, javaname);
                            break;
                        case 4:
                            this._qnameAttMap.put(name, javaname);
                            break;
                    }
                }
            }
            Extensionconfig[] ext = config.getExtensionArray();
            for (Extensionconfig extensionconfig : ext) {
                recordExtensionSetting(javaFiles, classpath, extensionconfig);
            }
            Usertypeconfig[] utypes = config.getUsertypeArray();
            for (Usertypeconfig usertypeconfig : utypes) {
                recordUserTypeSetting(javaFiles, classpath, usertypeconfig);
            }
        }
        secondPhaseValidation();
    }

    void addInterfaceExtension(InterfaceExtensionImpl ext) {
        if (ext == null) {
            return;
        }
        this._interfaceExtensions.add(ext);
    }

    void addPrePostExtension(PrePostExtensionImpl ext) {
        if (ext == null) {
            return;
        }
        this._prePostExtensions.add(ext);
    }

    void secondPhaseValidation() {
        Map methodSignatures = new HashMap();
        for (int i = 0; i < this._interfaceExtensions.size(); i++) {
            InterfaceExtensionImpl interfaceExtension = (InterfaceExtensionImpl) this._interfaceExtensions.get(i);
            InterfaceExtensionImpl.MethodSignatureImpl[] methods = (InterfaceExtensionImpl.MethodSignatureImpl[]) interfaceExtension.getMethods();
            for (int j = 0; j < methods.length; j++) {
                InterfaceExtensionImpl.MethodSignatureImpl ms = methods[j];
                if (methodSignatures.containsKey(methods[j])) {
                    InterfaceExtensionImpl.MethodSignatureImpl ms2 = (InterfaceExtensionImpl.MethodSignatureImpl) methodSignatures.get(methods[j]);
                    if (!ms.getReturnType().equals(ms2.getReturnType())) {
                        error("Colliding methods '" + ms.getSignature() + "' in interfaces " + ms.getInterfaceName() + " and " + ms2.getInterfaceName() + ".", null);
                        return;
                    }
                    return;
                }
                methodSignatures.put(methods[j], methods[j]);
            }
        }
        for (int i2 = 0; i2 < this._prePostExtensions.size() - 1; i2++) {
            PrePostExtensionImpl a = (PrePostExtensionImpl) this._prePostExtensions.get(i2);
            for (int j2 = 1; j2 < this._prePostExtensions.size(); j2++) {
                PrePostExtensionImpl b = (PrePostExtensionImpl) this._prePostExtensions.get(j2);
                if (a.hasNameSetIntersection(b)) {
                    error("The applicable domain for handler '" + a.getHandlerNameForJavaSource() + "' intersects with the one for '" + b.getHandlerNameForJavaSource() + "'.", null);
                }
            }
        }
    }

    private static void recordNamespaceSetting(Object key, String value, Map result) {
        if (value == null) {
            return;
        }
        if (key == null) {
            result.put("", value);
            return;
        }
        if ((key instanceof String) && "##any".equals(key)) {
            result.put(key, value);
            return;
        }
        if (key instanceof List) {
            for (String uri : (List) key) {
                if ("##local".equals(uri)) {
                    uri = "";
                }
                result.put(uri, value);
            }
        }
    }

    private static void recordNamespacePrefixSetting(List list, String value, Map result) {
        if (value == null || list == null) {
            return;
        }
        Iterator i = list.iterator();
        while (i.hasNext()) {
            result.put(i.next(), value);
        }
    }

    private void recordExtensionSetting(File[] javaFiles, File[] classpath, Extensionconfig ext) {
        NameSet xbeanSet = null;
        Object key = ext.getFor();
        if ((key instanceof String) && "*".equals(key)) {
            xbeanSet = NameSet.EVERYTHING;
        } else if (key instanceof List) {
            NameSetBuilder xbeanSetBuilder = new NameSetBuilder();
            for (String xbeanName : (List) key) {
                xbeanSetBuilder.add(xbeanName);
            }
            xbeanSet = xbeanSetBuilder.toNameSet();
        }
        if (xbeanSet == null) {
            error("Invalid value of attribute 'for' : '" + key + "'.", ext);
        }
        Extensionconfig.Interface[] intfXO = ext.getInterfaceArray();
        Extensionconfig.PrePostSet ppXO = ext.getPrePostSet();
        if (intfXO.length > 0 || ppXO != null) {
            JamClassLoader jamLoader = getJamLoader(javaFiles, classpath);
            for (Extensionconfig.Interface r0 : intfXO) {
                addInterfaceExtension(InterfaceExtensionImpl.newInstance(jamLoader, xbeanSet, r0));
            }
            addPrePostExtension(PrePostExtensionImpl.newInstance(jamLoader, xbeanSet, ppXO));
        }
    }

    private void recordUserTypeSetting(File[] javaFiles, File[] classpath, Usertypeconfig usertypeconfig) {
        JamClassLoader jamLoader = getJamLoader(javaFiles, classpath);
        UserTypeImpl userType = UserTypeImpl.newInstance(jamLoader, usertypeconfig);
        this._userTypes.put(userType.getName(), userType);
    }

    private String lookup(Map map, Map mapByUriPrefix, String uri) {
        String result;
        if (uri == null) {
            uri = "";
        }
        String result2 = (String) map.get(uri);
        if (result2 != null) {
            return result2;
        }
        if (mapByUriPrefix != null && (result = lookupByUriPrefix(mapByUriPrefix, uri)) != null) {
            return result;
        }
        return (String) map.get("##any");
    }

    private String lookupByUriPrefix(Map mapByUriPrefix, String uri) {
        if (uri != null && !mapByUriPrefix.isEmpty()) {
            String uriprefix = null;
            for (String nextprefix : mapByUriPrefix.keySet()) {
                if (uriprefix == null || nextprefix.length() >= uriprefix.length()) {
                    if (uri.startsWith(nextprefix)) {
                        uriprefix = nextprefix;
                    }
                }
            }
            if (uriprefix != null) {
                return (String) mapByUriPrefix.get(uriprefix);
            }
            return null;
        }
        return null;
    }

    static void warning(String s, XmlObject xo) {
        StscState.get().error(s, 1, xo);
    }

    static void error(String s, XmlObject xo) {
        StscState.get().error(s, 0, xo);
    }

    @Override // org.apache.xmlbeans.BindingConfig
    public String lookupPackageForNamespace(String uri) {
        return lookup(this._packageMap, this._packageMapByUriPrefix, uri);
    }

    @Override // org.apache.xmlbeans.BindingConfig
    public String lookupPrefixForNamespace(String uri) {
        return lookup(this._prefixMap, this._prefixMapByUriPrefix, uri);
    }

    @Override // org.apache.xmlbeans.BindingConfig
    public String lookupSuffixForNamespace(String uri) {
        return lookup(this._suffixMap, this._suffixMapByUriPrefix, uri);
    }

    @Override // org.apache.xmlbeans.BindingConfig
    public String lookupJavanameForQName(QName qname) {
        String result = (String) this._qnameTypeMap.get(qname);
        if (result != null) {
            return result;
        }
        return (String) this._qnameDocTypeMap.get(qname);
    }

    @Override // org.apache.xmlbeans.BindingConfig
    public String lookupJavanameForQName(QName qname, int kind) {
        switch (kind) {
            case 1:
                return (String) this._qnameTypeMap.get(qname);
            case 2:
                return (String) this._qnameDocTypeMap.get(qname);
            case 3:
                return (String) this._qnameElemMap.get(qname);
            case 4:
                return (String) this._qnameAttMap.get(qname);
            default:
                return null;
        }
    }

    @Override // org.apache.xmlbeans.BindingConfig
    public UserType lookupUserTypeForQName(QName qname) {
        if (qname == null) {
            return null;
        }
        return (UserType) this._userTypes.get(qname);
    }

    @Override // org.apache.xmlbeans.BindingConfig
    public InterfaceExtension[] getInterfaceExtensions() {
        return (InterfaceExtension[]) this._interfaceExtensions.toArray(new InterfaceExtension[this._interfaceExtensions.size()]);
    }

    @Override // org.apache.xmlbeans.BindingConfig
    public InterfaceExtension[] getInterfaceExtensions(String fullJavaName) {
        List result = new ArrayList();
        for (int i = 0; i < this._interfaceExtensions.size(); i++) {
            InterfaceExtensionImpl intfExt = (InterfaceExtensionImpl) this._interfaceExtensions.get(i);
            if (intfExt.contains(fullJavaName)) {
                result.add(intfExt);
            }
        }
        return (InterfaceExtension[]) result.toArray(new InterfaceExtension[result.size()]);
    }

    @Override // org.apache.xmlbeans.BindingConfig
    public PrePostExtension[] getPrePostExtensions() {
        return (PrePostExtension[]) this._prePostExtensions.toArray(new PrePostExtension[this._prePostExtensions.size()]);
    }

    @Override // org.apache.xmlbeans.BindingConfig
    public PrePostExtension getPrePostExtension(String fullJavaName) {
        for (int i = 0; i < this._prePostExtensions.size(); i++) {
            PrePostExtensionImpl prePostExt = (PrePostExtensionImpl) this._prePostExtensions.get(i);
            if (prePostExt.contains(fullJavaName)) {
                return prePostExt;
            }
        }
        return null;
    }

    private JamClassLoader getJamLoader(File[] javaFiles, File[] classpath) {
        JamServiceFactory jf = JamServiceFactory.getInstance();
        JamServiceParams params = jf.createServiceParams();
        params.set14WarningsEnabled(false);
        params.setShowWarnings(false);
        if (javaFiles != null) {
            for (File file : javaFiles) {
                params.includeSourceFile(file);
            }
        }
        params.addClassLoader(getClass().getClassLoader());
        if (classpath != null) {
            for (File file2 : classpath) {
                params.addClasspath(file2);
            }
        }
        try {
            JamService service = jf.createService(params);
            return service.getClassLoader();
        } catch (IOException e) {
            error("Error when accessing .java files.", null);
            return null;
        }
    }
}
