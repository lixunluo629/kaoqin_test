package org.apache.xmlbeans.impl.schema;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.ResourceLoader;
import org.apache.xmlbeans.SchemaAttributeGroup;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.SystemCache;
import org.apache.xmlbeans.impl.common.XBeanDebug;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeLoaderImpl.class */
public class SchemaTypeLoaderImpl extends SchemaTypeLoaderBase {
    private ResourceLoader _resourceLoader;
    private ClassLoader _classLoader;
    private SchemaTypeLoader[] _searchPath;
    private Map _classpathTypeSystems;
    private Map _classLoaderTypeSystems;
    private Map _elementCache;
    private Map _attributeCache;
    private Map _modelGroupCache;
    private Map _attributeGroupCache;
    private Map _idConstraintCache;
    private Map _typeCache;
    private Map _documentCache;
    private Map _attributeTypeCache;
    private Map _classnameCache;
    private final String _metadataPath;
    public static String METADATA_PACKAGE_LOAD;
    private static final Object CACHED_NOT_FOUND;
    private static final SchemaTypeLoader[] EMPTY_SCHEMATYPELOADER_ARRAY;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SchemaTypeLoaderImpl.class.desiredAssertionStatus();
        METADATA_PACKAGE_LOAD = SchemaTypeSystemImpl.METADATA_PACKAGE_GEN;
        CACHED_NOT_FOUND = new Object();
        EMPTY_SCHEMATYPELOADER_ARRAY = new SchemaTypeLoader[0];
        if (SystemCache.get() instanceof SystemCache) {
            SystemCache.set(new SchemaTypeLoaderCache());
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeLoaderImpl$SchemaTypeLoaderCache.class */
    private static class SchemaTypeLoaderCache extends SystemCache {
        private ThreadLocal _cachedTypeSystems;
        static final /* synthetic */ boolean $assertionsDisabled;

        private SchemaTypeLoaderCache() {
            this._cachedTypeSystems = new ThreadLocal() { // from class: org.apache.xmlbeans.impl.schema.SchemaTypeLoaderImpl.SchemaTypeLoaderCache.1
                @Override // java.lang.ThreadLocal
                protected Object initialValue() {
                    return new ArrayList();
                }
            };
        }

        static {
            $assertionsDisabled = !SchemaTypeLoaderImpl.class.desiredAssertionStatus();
        }

        @Override // org.apache.xmlbeans.impl.common.SystemCache
        public void clearThreadLocals() {
            this._cachedTypeSystems.remove();
            super.clearThreadLocals();
        }

        @Override // org.apache.xmlbeans.impl.common.SystemCache
        public SchemaTypeLoader getFromTypeLoaderCache(ClassLoader cl) {
            ArrayList a = (ArrayList) this._cachedTypeSystems.get();
            int candidate = -1;
            SchemaTypeLoaderImpl result = null;
            int i = 0;
            while (true) {
                if (i >= a.size()) {
                    break;
                }
                SchemaTypeLoaderImpl tl = (SchemaTypeLoaderImpl) ((SoftReference) a.get(i)).get();
                if (tl != null) {
                    if (tl._classLoader == cl) {
                        if (!$assertionsDisabled && (-1 != -1 || 0 != 0)) {
                            throw new AssertionError();
                        }
                        candidate = i;
                        result = tl;
                    }
                } else {
                    if (!$assertionsDisabled && i <= -1) {
                        throw new AssertionError();
                    }
                    int i2 = i;
                    i--;
                    a.remove(i2);
                }
                i++;
            }
            if (candidate > 0) {
                Object t = a.get(0);
                a.set(0, a.get(candidate));
                a.set(candidate, t);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.common.SystemCache
        public void addToTypeLoaderCache(SchemaTypeLoader stl, ClassLoader cl) {
            if (!$assertionsDisabled && (!(stl instanceof SchemaTypeLoaderImpl) || ((SchemaTypeLoaderImpl) stl)._classLoader != cl)) {
                throw new AssertionError();
            }
            ArrayList a = (ArrayList) this._cachedTypeSystems.get();
            if (a.size() > 0) {
                Object t = a.get(0);
                a.set(0, new SoftReference(stl));
                a.add(t);
                return;
            }
            a.add(new SoftReference(stl));
        }
    }

    public static SchemaTypeLoaderImpl getContextTypeLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        SchemaTypeLoaderImpl result = (SchemaTypeLoaderImpl) SystemCache.get().getFromTypeLoaderCache(cl);
        if (result == null) {
            result = new SchemaTypeLoaderImpl(new SchemaTypeLoader[]{BuiltinSchemaTypeSystem.get()}, null, cl, null);
            SystemCache.get().addToTypeLoaderCache(result, cl);
        }
        return result;
    }

    public static SchemaTypeLoader build(SchemaTypeLoader[] searchPath, ResourceLoader resourceLoader, ClassLoader classLoader) {
        return build(searchPath, resourceLoader, classLoader, null);
    }

    public static SchemaTypeLoader build(SchemaTypeLoader[] searchPath, ResourceLoader resourceLoader, ClassLoader classLoader, String metadataPath) throws ClassNotFoundException {
        SchemaTypeLoader[] sp;
        if (searchPath == null) {
            boolean isDefaultPath = metadataPath == null || new StringBuilder().append("schema").append(SchemaTypeSystemImpl.METADATA_PACKAGE_GEN).toString().equals(metadataPath);
            if (isDefaultPath) {
                sp = null;
            } else {
                String[] baseHolder = {"schemaorg_apache_xmlbeans.system.sXMLCONFIG.TypeSystemHolder", "schemaorg_apache_xmlbeans.system.sXMLLANG.TypeSystemHolder", "schemaorg_apache_xmlbeans.system.sXMLSCHEMA.TypeSystemHolder", "schemaorg_apache_xmlbeans.system.sXMLTOOLS.TypeSystemHolder"};
                sp = new SchemaTypeLoader[baseHolder.length];
                for (int i = 0; i < baseHolder.length; i++) {
                    try {
                        Class cls = Class.forName(baseHolder[i]);
                        sp[i] = (SchemaTypeLoader) cls.getDeclaredField("typeSystem").get(null);
                    } catch (Exception e) {
                        System.out.println("throw runtime: " + e.toString());
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            SubLoaderList list = new SubLoaderList();
            list.add(searchPath);
            sp = list.toArray();
        }
        if (sp != null && sp.length == 1 && resourceLoader == null && classLoader == null) {
            return sp[0];
        }
        return new SchemaTypeLoaderImpl(sp, resourceLoader, classLoader, metadataPath);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeLoaderImpl$SubLoaderList.class */
    private static class SubLoaderList {
        private final List<SchemaTypeLoader> theList;
        private final Map<SchemaTypeLoader, Object> seen;

        private SubLoaderList() {
            this.theList = new ArrayList();
            this.seen = new IdentityHashMap();
        }

        void add(SchemaTypeLoader[] searchPath) {
            if (searchPath == null) {
                return;
            }
            for (SchemaTypeLoader stl : searchPath) {
                if (stl instanceof SchemaTypeLoaderImpl) {
                    SchemaTypeLoaderImpl sub = (SchemaTypeLoaderImpl) stl;
                    if (sub._classLoader != null || sub._resourceLoader != null) {
                        add(sub);
                    } else {
                        add(sub._searchPath);
                    }
                } else {
                    add(stl);
                }
            }
        }

        void add(SchemaTypeLoader loader) {
            if (loader != null && !this.seen.containsKey(loader)) {
                this.theList.add(loader);
                this.seen.put(loader, null);
            }
        }

        SchemaTypeLoader[] toArray() {
            return (SchemaTypeLoader[]) this.theList.toArray(SchemaTypeLoaderImpl.EMPTY_SCHEMATYPELOADER_ARRAY);
        }
    }

    private SchemaTypeLoaderImpl(SchemaTypeLoader[] searchPath, ResourceLoader resourceLoader, ClassLoader classLoader, String metadataPath) {
        if (searchPath == null) {
            this._searchPath = EMPTY_SCHEMATYPELOADER_ARRAY;
        } else {
            this._searchPath = searchPath;
        }
        this._resourceLoader = resourceLoader;
        this._classLoader = classLoader;
        this._metadataPath = metadataPath == null ? "schema" + METADATA_PACKAGE_LOAD : metadataPath;
        initCaches();
    }

    private final void initCaches() {
        this._classpathTypeSystems = Collections.synchronizedMap(new HashMap());
        this._classLoaderTypeSystems = Collections.synchronizedMap(new HashMap());
        this._elementCache = Collections.synchronizedMap(new HashMap());
        this._attributeCache = Collections.synchronizedMap(new HashMap());
        this._modelGroupCache = Collections.synchronizedMap(new HashMap());
        this._attributeGroupCache = Collections.synchronizedMap(new HashMap());
        this._idConstraintCache = Collections.synchronizedMap(new HashMap());
        this._typeCache = Collections.synchronizedMap(new HashMap());
        this._documentCache = Collections.synchronizedMap(new HashMap());
        this._attributeTypeCache = Collections.synchronizedMap(new HashMap());
        this._classnameCache = Collections.synchronizedMap(new HashMap());
    }

    SchemaTypeSystemImpl typeSystemForComponent(String searchdir, QName name) {
        String searchfor = searchdir + QNameHelper.hexsafedir(name) + ".xsb";
        String tsname = null;
        if (this._resourceLoader != null) {
            tsname = crackEntry(this._resourceLoader, searchfor);
        }
        if (this._classLoader != null) {
            tsname = crackEntry(this._classLoader, searchfor);
        }
        if (tsname != null) {
            return (SchemaTypeSystemImpl) typeSystemForName(tsname);
        }
        return null;
    }

    public SchemaTypeSystem typeSystemForName(String name) {
        SchemaTypeSystem result;
        SchemaTypeSystem result2;
        if (this._resourceLoader != null && (result2 = getTypeSystemOnClasspath(name)) != null) {
            return result2;
        }
        if (this._classLoader != null && (result = getTypeSystemOnClassloader(name)) != null) {
            return result;
        }
        return null;
    }

    SchemaTypeSystemImpl typeSystemForClassname(String searchdir, String name) {
        String tsname;
        String tsname2;
        String searchfor = searchdir + name.replace('.', '/') + ".xsb";
        if (this._resourceLoader != null && (tsname2 = crackEntry(this._resourceLoader, searchfor)) != null) {
            return getTypeSystemOnClasspath(tsname2);
        }
        if (this._classLoader != null && (tsname = crackEntry(this._classLoader, searchfor)) != null) {
            return getTypeSystemOnClassloader(tsname);
        }
        return null;
    }

    SchemaTypeSystemImpl getTypeSystemOnClasspath(String name) {
        SchemaTypeSystemImpl result = (SchemaTypeSystemImpl) this._classpathTypeSystems.get(name);
        if (result == null) {
            result = new SchemaTypeSystemImpl(this._resourceLoader, name, this);
            this._classpathTypeSystems.put(name, result);
        }
        return result;
    }

    SchemaTypeSystemImpl getTypeSystemOnClassloader(String name) {
        XBeanDebug.trace(1, "Finding type system " + name + " on classloader", 0);
        SchemaTypeSystemImpl result = (SchemaTypeSystemImpl) this._classLoaderTypeSystems.get(name);
        if (result == null) {
            XBeanDebug.trace(1, "Type system " + name + " not cached - consulting field", 0);
            result = SchemaTypeSystemImpl.forName(name, this._classLoader);
            this._classLoaderTypeSystems.put(name, result);
        }
        return result;
    }

    static String crackEntry(ResourceLoader loader, String searchfor) {
        InputStream is = loader.getResourceAsStream(searchfor);
        if (is == null) {
            return null;
        }
        return crackPointer(is);
    }

    static String crackEntry(ClassLoader loader, String searchfor) {
        InputStream stream = loader.getResourceAsStream(searchfor);
        if (stream == null) {
            return null;
        }
        return crackPointer(stream);
    }

    static String crackPointer(InputStream stream) {
        return SchemaTypeSystemImpl.crackPointer(stream);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public boolean isNamespaceDefined(String namespace) {
        for (int i = 0; i < this._searchPath.length; i++) {
            if (this._searchPath[i].isNamespaceDefined(namespace)) {
                return true;
            }
        }
        SchemaTypeSystem sts = typeSystemForComponent(this._metadataPath + "/namespace/", new QName(namespace, "xmlns"));
        return sts != null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findTypeRef(QName name) {
        SchemaTypeSystem ts;
        Object cached = this._typeCache.get(name);
        if (cached == CACHED_NOT_FOUND) {
            return null;
        }
        SchemaType.Ref result = (SchemaType.Ref) cached;
        if (result == null) {
            for (int i = 0; i < this._searchPath.length; i++) {
                SchemaType.Ref refFindTypeRef = this._searchPath[i].findTypeRef(name);
                result = refFindTypeRef;
                if (null != refFindTypeRef) {
                    break;
                }
            }
            if (result == null && (ts = typeSystemForComponent(this._metadataPath + "/type/", name)) != null) {
                result = ts.findTypeRef(name);
                if (!$assertionsDisabled && result == null) {
                    throw new AssertionError("Type system registered type " + QNameHelper.pretty(name) + " but does not return it");
                }
            }
            this._typeCache.put(name, result == null ? CACHED_NOT_FOUND : result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType typeForClassname(String classname) {
        SchemaTypeSystem ts;
        String classname2 = classname.replace('$', '.');
        Object cached = this._classnameCache.get(classname2);
        if (cached == CACHED_NOT_FOUND) {
            return null;
        }
        SchemaType result = (SchemaType) cached;
        if (result == null) {
            for (int i = 0; i < this._searchPath.length; i++) {
                SchemaType schemaTypeTypeForClassname = this._searchPath[i].typeForClassname(classname2);
                result = schemaTypeTypeForClassname;
                if (null != schemaTypeTypeForClassname) {
                    break;
                }
            }
            if (result == null && (ts = typeSystemForClassname(this._metadataPath + "/javaname/", classname2)) != null) {
                result = ts.typeForClassname(classname2);
                if (!$assertionsDisabled && result == null) {
                    throw new AssertionError("Type system registered type " + classname2 + " but does not return it");
                }
            }
            this._classnameCache.put(classname2, result == null ? CACHED_NOT_FOUND : result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findDocumentTypeRef(QName name) {
        SchemaTypeSystem ts;
        Object cached = this._documentCache.get(name);
        if (cached == CACHED_NOT_FOUND) {
            return null;
        }
        SchemaType.Ref result = (SchemaType.Ref) cached;
        if (result == null) {
            for (int i = 0; i < this._searchPath.length; i++) {
                SchemaType.Ref refFindDocumentTypeRef = this._searchPath[i].findDocumentTypeRef(name);
                result = refFindDocumentTypeRef;
                if (null != refFindDocumentTypeRef) {
                    break;
                }
            }
            if (result == null && (ts = typeSystemForComponent(this._metadataPath + "/element/", name)) != null) {
                result = ts.findDocumentTypeRef(name);
                if (!$assertionsDisabled && result == null) {
                    throw new AssertionError("Type system registered element " + QNameHelper.pretty(name) + " but does not contain document type");
                }
            }
            this._documentCache.put(name, result == null ? CACHED_NOT_FOUND : result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findAttributeTypeRef(QName name) {
        SchemaTypeSystem ts;
        Object cached = this._attributeTypeCache.get(name);
        if (cached == CACHED_NOT_FOUND) {
            return null;
        }
        SchemaType.Ref result = (SchemaType.Ref) cached;
        if (result == null) {
            for (int i = 0; i < this._searchPath.length; i++) {
                SchemaType.Ref refFindAttributeTypeRef = this._searchPath[i].findAttributeTypeRef(name);
                result = refFindAttributeTypeRef;
                if (null != refFindAttributeTypeRef) {
                    break;
                }
            }
            if (result == null && (ts = typeSystemForComponent(this._metadataPath + "/attribute/", name)) != null) {
                result = ts.findAttributeTypeRef(name);
                if (!$assertionsDisabled && result == null) {
                    throw new AssertionError("Type system registered attribute " + QNameHelper.pretty(name) + " but does not contain attribute type");
                }
            }
            this._attributeTypeCache.put(name, result == null ? CACHED_NOT_FOUND : result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalElement.Ref findElementRef(QName name) {
        SchemaTypeSystem ts;
        Object cached = this._elementCache.get(name);
        if (cached == CACHED_NOT_FOUND) {
            return null;
        }
        SchemaGlobalElement.Ref result = (SchemaGlobalElement.Ref) cached;
        if (result == null) {
            for (int i = 0; i < this._searchPath.length; i++) {
                SchemaGlobalElement.Ref refFindElementRef = this._searchPath[i].findElementRef(name);
                result = refFindElementRef;
                if (null != refFindElementRef) {
                    break;
                }
            }
            if (result == null && (ts = typeSystemForComponent(this._metadataPath + "/element/", name)) != null) {
                result = ts.findElementRef(name);
                if (!$assertionsDisabled && result == null) {
                    throw new AssertionError("Type system registered element " + QNameHelper.pretty(name) + " but does not return it");
                }
            }
            this._elementCache.put(name, result == null ? CACHED_NOT_FOUND : result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalAttribute.Ref findAttributeRef(QName name) {
        SchemaTypeSystem ts;
        Object cached = this._attributeCache.get(name);
        if (cached == CACHED_NOT_FOUND) {
            return null;
        }
        SchemaGlobalAttribute.Ref result = (SchemaGlobalAttribute.Ref) cached;
        if (result == null) {
            for (int i = 0; i < this._searchPath.length; i++) {
                SchemaGlobalAttribute.Ref refFindAttributeRef = this._searchPath[i].findAttributeRef(name);
                result = refFindAttributeRef;
                if (null != refFindAttributeRef) {
                    break;
                }
            }
            if (result == null && (ts = typeSystemForComponent(this._metadataPath + "/attribute/", name)) != null) {
                result = ts.findAttributeRef(name);
                if (!$assertionsDisabled && result == null) {
                    throw new AssertionError("Type system registered attribute " + QNameHelper.pretty(name) + " but does not return it");
                }
            }
            this._attributeCache.put(name, result == null ? CACHED_NOT_FOUND : result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaModelGroup.Ref findModelGroupRef(QName name) {
        SchemaTypeSystem ts;
        Object cached = this._modelGroupCache.get(name);
        if (cached == CACHED_NOT_FOUND) {
            return null;
        }
        SchemaModelGroup.Ref result = (SchemaModelGroup.Ref) cached;
        if (result == null) {
            for (int i = 0; i < this._searchPath.length; i++) {
                SchemaModelGroup.Ref refFindModelGroupRef = this._searchPath[i].findModelGroupRef(name);
                result = refFindModelGroupRef;
                if (null != refFindModelGroupRef) {
                    break;
                }
            }
            if (result == null && (ts = typeSystemForComponent(this._metadataPath + "/modelgroup/", name)) != null) {
                result = ts.findModelGroupRef(name);
                if (!$assertionsDisabled && result == null) {
                    throw new AssertionError("Type system registered model group " + QNameHelper.pretty(name) + " but does not return it");
                }
            }
            this._modelGroupCache.put(name, result == null ? CACHED_NOT_FOUND : result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaAttributeGroup.Ref findAttributeGroupRef(QName name) {
        SchemaTypeSystem ts;
        Object cached = this._attributeGroupCache.get(name);
        if (cached == CACHED_NOT_FOUND) {
            return null;
        }
        SchemaAttributeGroup.Ref result = (SchemaAttributeGroup.Ref) cached;
        if (result == null) {
            for (int i = 0; i < this._searchPath.length; i++) {
                SchemaAttributeGroup.Ref refFindAttributeGroupRef = this._searchPath[i].findAttributeGroupRef(name);
                result = refFindAttributeGroupRef;
                if (null != refFindAttributeGroupRef) {
                    break;
                }
            }
            if (result == null && (ts = typeSystemForComponent(this._metadataPath + "/attributegroup/", name)) != null) {
                result = ts.findAttributeGroupRef(name);
                if (!$assertionsDisabled && result == null) {
                    throw new AssertionError("Type system registered attribute group " + QNameHelper.pretty(name) + " but does not return it");
                }
            }
            this._attributeGroupCache.put(name, result == null ? CACHED_NOT_FOUND : result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaIdentityConstraint.Ref findIdentityConstraintRef(QName name) {
        SchemaTypeSystem ts;
        Object cached = this._idConstraintCache.get(name);
        if (cached == CACHED_NOT_FOUND) {
            return null;
        }
        SchemaIdentityConstraint.Ref result = (SchemaIdentityConstraint.Ref) cached;
        if (result == null) {
            for (int i = 0; i < this._searchPath.length; i++) {
                SchemaIdentityConstraint.Ref refFindIdentityConstraintRef = this._searchPath[i].findIdentityConstraintRef(name);
                result = refFindIdentityConstraintRef;
                if (null != refFindIdentityConstraintRef) {
                    break;
                }
            }
            if (result == null && (ts = typeSystemForComponent(this._metadataPath + "/identityconstraint/", name)) != null) {
                result = ts.findIdentityConstraintRef(name);
                if (!$assertionsDisabled && result == null) {
                    throw new AssertionError("Type system registered identity constraint " + QNameHelper.pretty(name) + " but does not return it");
                }
            }
            this._idConstraintCache.put(name, result == null ? CACHED_NOT_FOUND : result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public InputStream getSourceAsStream(String sourceName) {
        InputStream result = null;
        if (!sourceName.startsWith("/")) {
            sourceName = "/" + sourceName;
        }
        if (this._resourceLoader != null) {
            result = this._resourceLoader.getResourceAsStream(this._metadataPath + "/src" + sourceName);
        }
        if (result == null && this._classLoader != null) {
            return this._classLoader.getResourceAsStream(this._metadataPath + "/src" + sourceName);
        }
        return result;
    }
}
