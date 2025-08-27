package org.apache.xmlbeans.impl.schema;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.Filer;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.ResourceLoader;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaAttributeGroup;
import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeLoaderException;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.DefaultClassLoaderResourceLoader;
import org.apache.xmlbeans.impl.common.NameUtil;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.XBeanDebug;
import org.apache.xmlbeans.impl.regex.RegularExpression;
import org.apache.xmlbeans.impl.repackage.Repackager;
import org.apache.xmlbeans.impl.schema.SchemaAnnotationImpl;
import org.apache.xmlbeans.impl.schema.StscComplexTypeResolver;
import org.apache.xmlbeans.impl.util.FilerImpl;
import org.apache.xmlbeans.impl.util.HexBin;
import org.apache.xmlbeans.impl.values.XmlObjectBase;
import org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.GroupDocument;
import org.apache.xmlbeans.soap.SOAPArrayType;
import org.apache.xmlbeans.soap.SchemaWSDLArrayType;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.ClassUtils;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeSystemImpl.class */
public class SchemaTypeSystemImpl extends SchemaTypeLoaderBase implements SchemaTypeSystem {
    public static final int DATA_BABE = -629491010;
    public static final int MAJOR_VERSION = 2;
    public static final int MINOR_VERSION = 24;
    public static final int RELEASE_NUMBER = 0;
    public static final int FILETYPE_SCHEMAINDEX = 1;
    public static final int FILETYPE_SCHEMATYPE = 2;
    public static final int FILETYPE_SCHEMAELEMENT = 3;
    public static final int FILETYPE_SCHEMAATTRIBUTE = 4;
    public static final int FILETYPE_SCHEMAPOINTER = 5;
    public static final int FILETYPE_SCHEMAMODELGROUP = 6;
    public static final int FILETYPE_SCHEMAATTRIBUTEGROUP = 7;
    public static final int FILETYPE_SCHEMAIDENTITYCONSTRAINT = 8;
    public static final int FLAG_PART_SKIPPABLE = 1;
    public static final int FLAG_PART_FIXED = 4;
    public static final int FLAG_PART_NILLABLE = 8;
    public static final int FLAG_PART_BLOCKEXT = 16;
    public static final int FLAG_PART_BLOCKREST = 32;
    public static final int FLAG_PART_BLOCKSUBST = 64;
    public static final int FLAG_PART_ABSTRACT = 128;
    public static final int FLAG_PART_FINALEXT = 256;
    public static final int FLAG_PART_FINALREST = 512;
    public static final int FLAG_PROP_ISATTR = 1;
    public static final int FLAG_PROP_JAVASINGLETON = 2;
    public static final int FLAG_PROP_JAVAOPTIONAL = 4;
    public static final int FLAG_PROP_JAVAARRAY = 8;
    public static final int FIELD_NONE = 0;
    public static final int FIELD_GLOBAL = 1;
    public static final int FIELD_LOCALATTR = 2;
    public static final int FIELD_LOCALELT = 3;
    static final int FLAG_SIMPLE_TYPE = 1;
    static final int FLAG_DOCUMENT_TYPE = 2;
    static final int FLAG_ORDERED = 4;
    static final int FLAG_BOUNDED = 8;
    static final int FLAG_FINITE = 16;
    static final int FLAG_NUMERIC = 32;
    static final int FLAG_STRINGENUM = 64;
    static final int FLAG_UNION_OF_LISTS = 128;
    static final int FLAG_HAS_PATTERN = 256;
    static final int FLAG_ORDER_SENSITIVE = 512;
    static final int FLAG_TOTAL_ORDER = 1024;
    static final int FLAG_COMPILED = 2048;
    static final int FLAG_BLOCK_EXT = 4096;
    static final int FLAG_BLOCK_REST = 8192;
    static final int FLAG_FINAL_EXT = 16384;
    static final int FLAG_FINAL_REST = 32768;
    static final int FLAG_FINAL_UNION = 65536;
    static final int FLAG_FINAL_LIST = 131072;
    static final int FLAG_ABSTRACT = 262144;
    static final int FLAG_ATTRIBUTE_TYPE = 524288;
    public static String METADATA_PACKAGE_GEN;
    private static final String HOLDER_TEMPLATE_CLASS = "org.apache.xmlbeans.impl.schema.TypeSystemHolder";
    private static final String HOLDER_TEMPLATE_CLASSFILE = "TypeSystemHolder.template";
    private static final String[] HOLDER_TEMPLATE_NAMES;
    private static final int CONSTANT_UTF8 = 1;
    private static final int CONSTANT_UNICODE = 2;
    private static final int CONSTANT_INTEGER = 3;
    private static final int CONSTANT_FLOAT = 4;
    private static final int CONSTANT_LONG = 5;
    private static final int CONSTANT_DOUBLE = 6;
    private static final int CONSTANT_CLASS = 7;
    private static final int CONSTANT_STRING = 8;
    private static final int CONSTANT_FIELD = 9;
    private static final int CONSTANT_METHOD = 10;
    private static final int CONSTANT_INTERFACEMETHOD = 11;
    private static final int CONSTANT_NAMEANDTYPE = 12;
    private static final int MAX_UNSIGNED_SHORT = 65535;
    private static Random _random;
    private static byte[] _mask;
    private String _name;
    private String _basePackage;
    private ClassLoader _classloader;
    private ResourceLoader _resourceLoader;
    SchemaTypeLoader _linker;
    private HandlePool _localHandles;
    private Filer _filer;
    private List _annotations;
    private SchemaDependencies _deps;
    private List _redefinedModelGroups;
    private List _redefinedAttributeGroups;
    private List _redefinedGlobalTypes;
    private Map _globalElements;
    private Map _globalAttributes;
    private Map _modelGroups;
    private Map _attributeGroups;
    private Map _globalTypes;
    private Map _documentTypes;
    private Map _attributeTypes;
    private Set _namespaces;
    private static final SchemaType[] EMPTY_ST_ARRAY;
    private static final SchemaGlobalElement[] EMPTY_GE_ARRAY;
    private static final SchemaGlobalAttribute[] EMPTY_GA_ARRAY;
    private static final SchemaModelGroup[] EMPTY_MG_ARRAY;
    private static final SchemaAttributeGroup[] EMPTY_AG_ARRAY;
    private static final SchemaIdentityConstraint[] EMPTY_IC_ARRAY;
    private static final SchemaAnnotation[] EMPTY_ANN_ARRAY;
    static final byte[] SINGLE_ZERO_BYTE;
    static final /* synthetic */ boolean $assertionsDisabled;
    private boolean _incomplete = false;
    private Map _containers = new HashMap();
    private Map _identityConstraints = Collections.EMPTY_MAP;
    private Map _typeRefsByClassname = new HashMap();
    private final Map _resolvedHandles = new HashMap();
    private boolean _allNonGroupHandlesResolved = false;

    static {
        $assertionsDisabled = !SchemaTypeSystemImpl.class.desiredAssertionStatus();
        Package stsPackage = SchemaTypeSystem.class.getPackage();
        String stsPackageName = stsPackage == null ? SchemaTypeSystem.class.getName().substring(0, SchemaTypeSystem.class.getName().lastIndexOf(".")) : stsPackage.getName();
        METADATA_PACKAGE_GEN = stsPackageName.replaceAll("\\.", "_");
        HOLDER_TEMPLATE_NAMES = makeClassStrings(HOLDER_TEMPLATE_CLASS);
        _mask = new byte[16];
        EMPTY_ST_ARRAY = new SchemaType[0];
        EMPTY_GE_ARRAY = new SchemaGlobalElement[0];
        EMPTY_GA_ARRAY = new SchemaGlobalAttribute[0];
        EMPTY_MG_ARRAY = new SchemaModelGroup[0];
        EMPTY_AG_ARRAY = new SchemaAttributeGroup[0];
        EMPTY_IC_ARRAY = new SchemaIdentityConstraint[0];
        EMPTY_ANN_ARRAY = new SchemaAnnotation[0];
        SINGLE_ZERO_BYTE = new byte[]{0};
    }

    private static String nameToPathString(String nameForSystem) {
        String nameForSystem2 = nameForSystem.replace('.', '/');
        if (!nameForSystem2.endsWith("/") && nameForSystem2.length() > 0) {
            nameForSystem2 = nameForSystem2 + "/";
        }
        return nameForSystem2;
    }

    public SchemaTypeSystemImpl(Class indexclass) throws IOException {
        String fullname = indexclass.getName();
        this._name = fullname.substring(0, fullname.lastIndexOf(46));
        XBeanDebug.trace(1, "Loading type system " + this._name, 1);
        this._basePackage = nameToPathString(this._name);
        this._classloader = indexclass.getClassLoader();
        this._linker = SchemaTypeLoaderImpl.build(null, null, this._classloader, getMetadataPath());
        this._resourceLoader = new ClassLoaderResourceLoader(this._classloader);
        try {
            initFromHeader();
            XBeanDebug.trace(1, "Finished loading type system " + this._name, -1);
        } catch (Error e) {
            XBeanDebug.logException(e);
            throw e;
        } catch (RuntimeException e2) {
            XBeanDebug.logException(e2);
            throw e2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x004e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean fileContainsTypeSystem(java.io.File r7, java.lang.String r8) throws java.io.IOException {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r1 = r8
            java.lang.String r1 = nameToPathString(r1)
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = "index.xsb"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r9 = r0
            r0 = r7
            boolean r0 = r0.isDirectory()
            if (r0 == 0) goto L2b
            java.io.File r0 = new java.io.File
            r1 = r0
            r2 = r7
            r3 = r9
            r1.<init>(r2, r3)
            boolean r0 = r0.isFile()
            return r0
        L2b:
            r0 = 0
            r10 = r0
            java.util.zip.ZipFile r0 = new java.util.zip.ZipFile     // Catch: java.io.IOException -> L61 java.lang.Throwable -> L92
            r1 = r0
            r2 = r7
            r1.<init>(r2)     // Catch: java.io.IOException -> L61 java.lang.Throwable -> L92
            r10 = r0
            r0 = r10
            r1 = r9
            java.util.zip.ZipEntry r0 = r0.getEntry(r1)     // Catch: java.io.IOException -> L61 java.lang.Throwable -> L92
            r11 = r0
            r0 = r11
            if (r0 == 0) goto L4e
            r0 = r11
            boolean r0 = r0.isDirectory()     // Catch: java.io.IOException -> L61 java.lang.Throwable -> L92
            if (r0 != 0) goto L4e
            r0 = 1
            goto L4f
        L4e:
            r0 = 0
        L4f:
            r12 = r0
            r0 = r10
            if (r0 == 0) goto L5e
            r0 = r10
            r0.close()     // Catch: java.io.IOException -> L5c
            goto L5e
        L5c:
            r13 = move-exception
        L5e:
            r0 = r12
            return r0
        L61:
            r11 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L92
            r1 = r0
            r1.<init>()     // Catch: java.lang.Throwable -> L92
            java.lang.String r1 = "Problem loading SchemaTypeSystem, zipfilename "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> L92
            r1 = r7
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> L92
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L92
            java.lang.String r0 = org.apache.xmlbeans.impl.common.XBeanDebug.log(r0)     // Catch: java.lang.Throwable -> L92
            r0 = r11
            java.lang.Throwable r0 = org.apache.xmlbeans.impl.common.XBeanDebug.logException(r0)     // Catch: java.lang.Throwable -> L92
            org.apache.xmlbeans.SchemaTypeLoaderException r0 = new org.apache.xmlbeans.SchemaTypeLoaderException     // Catch: java.lang.Throwable -> L92
            r1 = r0
            r2 = r11
            java.lang.String r2 = r2.getMessage()     // Catch: java.lang.Throwable -> L92
            r3 = r8
            java.lang.String r4 = "index"
            r5 = 9
            r1.<init>(r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L92
            throw r0     // Catch: java.lang.Throwable -> L92
        L92:
            r14 = move-exception
            r0 = r10
            if (r0 == 0) goto La1
            r0 = r10
            r0.close()     // Catch: java.io.IOException -> L9f
            goto La1
        L9f:
            r15 = move-exception
        La1:
            r0 = r14
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl.fileContainsTypeSystem(java.io.File, java.lang.String):boolean");
    }

    public static SchemaTypeSystemImpl forName(String name, ClassLoader loader) {
        try {
            Class c = Class.forName(name + "." + SchemaTypeCodePrinter.INDEX_CLASSNAME, true, loader);
            return (SchemaTypeSystemImpl) c.getField("typeSystem").get(null);
        } catch (Exception e) {
            return null;
        }
    }

    public SchemaTypeSystemImpl(ResourceLoader resourceLoader, String name, SchemaTypeLoader linker) throws IOException {
        this._name = name;
        this._basePackage = nameToPathString(this._name);
        this._linker = linker;
        this._resourceLoader = resourceLoader;
        try {
            initFromHeader();
        } catch (Error e) {
            XBeanDebug.logException(e);
            throw e;
        } catch (RuntimeException e2) {
            XBeanDebug.logException(e2);
            throw e2;
        }
    }

    private void initFromHeader() {
        XBeanDebug.trace(1, "Reading unresolved handles for type system " + this._name, 0);
        XsbReader reader = null;
        try {
            reader = new XsbReader(BeanDefinitionParserDelegate.INDEX_ATTRIBUTE, 1);
            this._localHandles = new HandlePool();
            reader.readHandlePool(this._localHandles);
            this._globalElements = reader.readQNameRefMap();
            this._globalAttributes = reader.readQNameRefMap();
            this._modelGroups = reader.readQNameRefMap();
            this._attributeGroups = reader.readQNameRefMap();
            this._identityConstraints = reader.readQNameRefMap();
            this._globalTypes = reader.readQNameRefMap();
            this._documentTypes = reader.readQNameRefMap();
            this._attributeTypes = reader.readQNameRefMap();
            this._typeRefsByClassname = reader.readClassnameRefMap();
            this._namespaces = reader.readNamespaces();
            List typeNames = new ArrayList();
            List modelGroupNames = new ArrayList();
            List attributeGroupNames = new ArrayList();
            if (reader.atLeast(2, 15, 0)) {
                this._redefinedGlobalTypes = reader.readQNameRefMapAsList(typeNames);
                this._redefinedModelGroups = reader.readQNameRefMapAsList(modelGroupNames);
                this._redefinedAttributeGroups = reader.readQNameRefMapAsList(attributeGroupNames);
            }
            if (reader.atLeast(2, 19, 0)) {
                this._annotations = reader.readAnnotations();
            }
            buildContainers(typeNames, modelGroupNames, attributeGroupNames);
            if (reader != null) {
                reader.readEnd();
            }
        } catch (Throwable th) {
            if (reader != null) {
                reader.readEnd();
            }
            throw th;
        }
    }

    void saveIndex() throws IOException {
        XsbReader saver = new XsbReader(BeanDefinitionParserDelegate.INDEX_ATTRIBUTE);
        saver.writeIndexData();
        saver.writeRealHeader(BeanDefinitionParserDelegate.INDEX_ATTRIBUTE, 1);
        saver.writeIndexData();
        saver.writeEnd();
    }

    void savePointers() throws IOException {
        savePointersForComponents(globalElements(), getMetadataPath() + "/element/");
        savePointersForComponents(globalAttributes(), getMetadataPath() + "/attribute/");
        savePointersForComponents(modelGroups(), getMetadataPath() + "/modelgroup/");
        savePointersForComponents(attributeGroups(), getMetadataPath() + "/attributegroup/");
        savePointersForComponents(globalTypes(), getMetadataPath() + "/type/");
        savePointersForComponents(identityConstraints(), getMetadataPath() + "/identityconstraint/");
        savePointersForNamespaces(this._namespaces, getMetadataPath() + "/namespace/");
        savePointersForClassnames(this._typeRefsByClassname.keySet(), getMetadataPath() + "/javaname/");
        savePointersForComponents(redefinedModelGroups(), getMetadataPath() + "/redefinedmodelgroup/");
        savePointersForComponents(redefinedAttributeGroups(), getMetadataPath() + "/redefinedattributegroup/");
        savePointersForComponents(redefinedGlobalTypes(), getMetadataPath() + "/redefinedtype/");
    }

    void savePointersForComponents(SchemaComponent[] components, String dir) throws IOException {
        for (SchemaComponent schemaComponent : components) {
            savePointerFile(dir + QNameHelper.hexsafedir(schemaComponent.getName()), this._name);
        }
    }

    void savePointersForClassnames(Set classnames, String dir) throws IOException {
        Iterator i = classnames.iterator();
        while (i.hasNext()) {
            String classname = (String) i.next();
            savePointerFile(dir + classname.replace('.', '/'), this._name);
        }
    }

    void savePointersForNamespaces(Set namespaces, String dir) throws IOException {
        Iterator i = namespaces.iterator();
        while (i.hasNext()) {
            String ns = (String) i.next();
            savePointerFile(dir + QNameHelper.hexsafedir(new QName(ns, "xmlns")), this._name);
        }
    }

    void savePointerFile(String filename, String name) throws IOException {
        XsbReader saver = new XsbReader(filename);
        saver.writeString(name);
        saver.writeRealHeader(filename, 5);
        saver.writeString(name);
        saver.writeEnd();
    }

    void saveLoader() throws IOException {
        String indexClassName = SchemaTypeCodePrinter.indexClassForSystem(this);
        String[] replace = makeClassStrings(indexClassName);
        if (!$assertionsDisabled && replace.length != HOLDER_TEMPLATE_NAMES.length) {
            throw new AssertionError();
        }
        InputStream is = null;
        OutputStream os = null;
        Repackager repackager = null;
        if (this._filer instanceof FilerImpl) {
            repackager = ((FilerImpl) this._filer).getRepackager();
        }
        try {
            InputStream is2 = SchemaTypeSystemImpl.class.getResourceAsStream(HOLDER_TEMPLATE_CLASSFILE);
            if (is2 == null) {
                DefaultClassLoaderResourceLoader clLoader = new DefaultClassLoaderResourceLoader();
                is2 = clLoader.getResourceAsStream(HOLDER_TEMPLATE_CLASSFILE);
            }
            if (is2 == null) {
                throw new SchemaTypeLoaderException("couldn't find resource: TypeSystemHolder.template", this._name, null, 9);
            }
            DataInputStream in = new DataInputStream(is2);
            OutputStream os2 = this._filer.createBinaryFile(indexClassName.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX);
            DataOutputStream out = new DataOutputStream(os2);
            out.writeInt(in.readInt());
            out.writeShort(in.readUnsignedShort());
            out.writeShort(in.readUnsignedShort());
            int poolsize = in.readUnsignedShort();
            out.writeShort(poolsize);
            for (int i = 1; i < poolsize; i++) {
                int tag = in.readUnsignedByte();
                out.writeByte(tag);
                switch (tag) {
                    case 1:
                        String value = in.readUTF();
                        out.writeUTF(repackageConstant(value, replace, repackager));
                        break;
                    case 2:
                    default:
                        throw new RuntimeException("Unexpected constant type: " + tag);
                    case 3:
                    case 4:
                        out.writeInt(in.readInt());
                        break;
                    case 5:
                    case 6:
                        out.writeInt(in.readInt());
                        out.writeInt(in.readInt());
                        break;
                    case 7:
                    case 8:
                        out.writeShort(in.readUnsignedShort());
                        break;
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        out.writeShort(in.readUnsignedShort());
                        out.writeShort(in.readUnsignedShort());
                        break;
                }
            }
            while (true) {
                try {
                    out.writeByte(in.readByte());
                } catch (EOFException e) {
                    if (is2 != null) {
                        try {
                            is2.close();
                        } catch (Exception e2) {
                        }
                    }
                    if (os2 != null) {
                        try {
                            os2.close();
                            return;
                        } catch (Exception e3) {
                            return;
                        }
                    }
                    return;
                }
            }
        } catch (IOException e4) {
            if (0 != 0) {
                try {
                    is.close();
                } catch (Exception e5) {
                }
            }
            if (0 != 0) {
                try {
                    os.close();
                } catch (Exception e6) {
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    is.close();
                } catch (Exception e7) {
                }
            }
            if (0 != 0) {
                try {
                    os.close();
                } catch (Exception e8) {
                }
            }
            throw th;
        }
    }

    private static String repackageConstant(String value, String[] replace, Repackager repackager) {
        for (int i = 0; i < HOLDER_TEMPLATE_NAMES.length; i++) {
            if (HOLDER_TEMPLATE_NAMES[i].equals(value)) {
                return replace[i];
            }
        }
        if (repackager != null) {
            return repackager.repackage(new StringBuffer(value)).toString();
        }
        return value;
    }

    private static String[] makeClassStrings(String classname) {
        String[] result = new String[4];
        result[0] = classname;
        result[1] = classname.replace('.', '/');
        result[2] = StandardRoles.L + result[1] + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;
        result[3] = "class$" + classname.replace('.', '$');
        return result;
    }

    private Map buildTypeRefsByClassname() {
        List allSeenTypes = new ArrayList();
        Map result = new LinkedHashMap();
        allSeenTypes.addAll(Arrays.asList(documentTypes()));
        allSeenTypes.addAll(Arrays.asList(attributeTypes()));
        allSeenTypes.addAll(Arrays.asList(globalTypes()));
        for (int i = 0; i < allSeenTypes.size(); i++) {
            SchemaType gType = (SchemaType) allSeenTypes.get(i);
            String className = gType.getFullJavaName();
            if (className != null) {
                result.put(className.replace('$', '.'), gType.getRef());
            }
            allSeenTypes.addAll(Arrays.asList(gType.getAnonymousTypes()));
        }
        return result;
    }

    private Map buildTypeRefsByClassname(Map typesByClassname) {
        Map result = new LinkedHashMap();
        for (String className : typesByClassname.keySet()) {
            result.put(className, ((SchemaType) typesByClassname.get(className)).getRef());
        }
        return result;
    }

    private static Map buildComponentRefMap(SchemaComponent[] components) {
        Map result = new LinkedHashMap();
        for (int i = 0; i < components.length; i++) {
            result.put(components[i].getName(), components[i].getComponentRef());
        }
        return result;
    }

    private static List buildComponentRefList(SchemaComponent[] components) {
        List result = new ArrayList();
        for (SchemaComponent schemaComponent : components) {
            result.add(schemaComponent.getComponentRef());
        }
        return result;
    }

    private static Map buildDocumentMap(SchemaType[] types) {
        Map result = new LinkedHashMap();
        for (int i = 0; i < types.length; i++) {
            result.put(types[i].getDocumentElementName(), types[i].getRef());
        }
        return result;
    }

    private static Map buildAttributeTypeMap(SchemaType[] types) {
        Map result = new LinkedHashMap();
        for (int i = 0; i < types.length; i++) {
            result.put(types[i].getAttributeTypeAttributeName(), types[i].getRef());
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SchemaContainer getContainer(String namespace) {
        return (SchemaContainer) this._containers.get(namespace);
    }

    private void addContainer(String namespace) {
        SchemaContainer c = new SchemaContainer(namespace);
        c.setTypeSystem(this);
        this._containers.put(namespace, c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SchemaContainer getContainerNonNull(String namespace) {
        SchemaContainer result = getContainer(namespace);
        if (result == null) {
            addContainer(namespace);
            result = getContainer(namespace);
        }
        return result;
    }

    private void buildContainers(List redefTypeNames, List redefModelGroupNames, List redefAttributeGroupNames) {
        for (Map.Entry entry : this._globalElements.entrySet()) {
            String ns = ((QName) entry.getKey()).getNamespaceURI();
            getContainerNonNull(ns).addGlobalElement((SchemaGlobalElement.Ref) entry.getValue());
        }
        for (Map.Entry entry2 : this._globalAttributes.entrySet()) {
            String ns2 = ((QName) entry2.getKey()).getNamespaceURI();
            getContainerNonNull(ns2).addGlobalAttribute((SchemaGlobalAttribute.Ref) entry2.getValue());
        }
        for (Map.Entry entry3 : this._modelGroups.entrySet()) {
            String ns3 = ((QName) entry3.getKey()).getNamespaceURI();
            getContainerNonNull(ns3).addModelGroup((SchemaModelGroup.Ref) entry3.getValue());
        }
        for (Map.Entry entry4 : this._attributeGroups.entrySet()) {
            String ns4 = ((QName) entry4.getKey()).getNamespaceURI();
            getContainerNonNull(ns4).addAttributeGroup((SchemaAttributeGroup.Ref) entry4.getValue());
        }
        for (Map.Entry entry5 : this._identityConstraints.entrySet()) {
            String ns5 = ((QName) entry5.getKey()).getNamespaceURI();
            getContainerNonNull(ns5).addIdentityConstraint((SchemaIdentityConstraint.Ref) entry5.getValue());
        }
        for (Map.Entry entry6 : this._globalTypes.entrySet()) {
            String ns6 = ((QName) entry6.getKey()).getNamespaceURI();
            getContainerNonNull(ns6).addGlobalType((SchemaType.Ref) entry6.getValue());
        }
        for (Map.Entry entry7 : this._documentTypes.entrySet()) {
            String ns7 = ((QName) entry7.getKey()).getNamespaceURI();
            getContainerNonNull(ns7).addDocumentType((SchemaType.Ref) entry7.getValue());
        }
        for (Map.Entry entry8 : this._attributeTypes.entrySet()) {
            String ns8 = ((QName) entry8.getKey()).getNamespaceURI();
            getContainerNonNull(ns8).addAttributeType((SchemaType.Ref) entry8.getValue());
        }
        if (this._redefinedGlobalTypes != null && this._redefinedModelGroups != null && this._redefinedAttributeGroups != null) {
            if (!$assertionsDisabled && this._redefinedGlobalTypes.size() != redefTypeNames.size()) {
                throw new AssertionError();
            }
            Iterator it = this._redefinedGlobalTypes.iterator();
            Iterator itname = redefTypeNames.iterator();
            while (it.hasNext()) {
                String ns9 = ((QName) itname.next()).getNamespaceURI();
                getContainerNonNull(ns9).addRedefinedType((SchemaType.Ref) it.next());
            }
            Iterator it2 = this._redefinedModelGroups.iterator();
            Iterator itname2 = redefModelGroupNames.iterator();
            while (it2.hasNext()) {
                String ns10 = ((QName) itname2.next()).getNamespaceURI();
                getContainerNonNull(ns10).addRedefinedModelGroup((SchemaModelGroup.Ref) it2.next());
            }
            Iterator it3 = this._redefinedAttributeGroups.iterator();
            Iterator itname3 = redefAttributeGroupNames.iterator();
            while (it3.hasNext()) {
                String ns11 = ((QName) itname3.next()).getNamespaceURI();
                getContainerNonNull(ns11).addRedefinedAttributeGroup((SchemaAttributeGroup.Ref) it3.next());
            }
        }
        if (this._annotations != null) {
            for (SchemaAnnotation ann : this._annotations) {
                getContainerNonNull("").addAnnotation(ann);
            }
        }
        Iterator it4 = this._containers.values().iterator();
        while (it4.hasNext()) {
            ((SchemaContainer) it4.next()).setImmutable();
        }
    }

    private void fixupContainers() {
        for (SchemaContainer container : this._containers.values()) {
            container.setTypeSystem(this);
            container.setImmutable();
        }
    }

    private void assertContainersSynchronized() {
        boolean assertEnabled = false;
        if (!$assertionsDisabled) {
            assertEnabled = true;
            if (1 == 0) {
                throw new AssertionError();
            }
        }
        if (!assertEnabled) {
            return;
        }
        Map temp = new HashMap();
        Iterator it = this._containers.values().iterator();
        while (it.hasNext()) {
            temp.putAll(buildComponentRefMap((SchemaComponent[]) ((SchemaContainer) it.next()).globalElements().toArray(new SchemaComponent[0])));
        }
        if (!$assertionsDisabled && !this._globalElements.equals(temp)) {
            throw new AssertionError();
        }
        Map temp2 = new HashMap();
        Iterator it2 = this._containers.values().iterator();
        while (it2.hasNext()) {
            temp2.putAll(buildComponentRefMap((SchemaComponent[]) ((SchemaContainer) it2.next()).globalAttributes().toArray(new SchemaComponent[0])));
        }
        if (!$assertionsDisabled && !this._globalAttributes.equals(temp2)) {
            throw new AssertionError();
        }
        Map temp3 = new HashMap();
        Iterator it3 = this._containers.values().iterator();
        while (it3.hasNext()) {
            temp3.putAll(buildComponentRefMap((SchemaComponent[]) ((SchemaContainer) it3.next()).modelGroups().toArray(new SchemaComponent[0])));
        }
        if (!$assertionsDisabled && !this._modelGroups.equals(temp3)) {
            throw new AssertionError();
        }
        Set temp22 = new HashSet();
        Iterator it4 = this._containers.values().iterator();
        while (it4.hasNext()) {
            temp22.addAll(buildComponentRefList((SchemaComponent[]) ((SchemaContainer) it4.next()).redefinedModelGroups().toArray(new SchemaComponent[0])));
        }
        if (!$assertionsDisabled && !new HashSet(this._redefinedModelGroups).equals(temp22)) {
            throw new AssertionError();
        }
        Map temp4 = new HashMap();
        Iterator it5 = this._containers.values().iterator();
        while (it5.hasNext()) {
            temp4.putAll(buildComponentRefMap((SchemaComponent[]) ((SchemaContainer) it5.next()).attributeGroups().toArray(new SchemaComponent[0])));
        }
        if (!$assertionsDisabled && !this._attributeGroups.equals(temp4)) {
            throw new AssertionError();
        }
        Set temp23 = new HashSet();
        Iterator it6 = this._containers.values().iterator();
        while (it6.hasNext()) {
            temp23.addAll(buildComponentRefList((SchemaComponent[]) ((SchemaContainer) it6.next()).redefinedAttributeGroups().toArray(new SchemaComponent[0])));
        }
        if (!$assertionsDisabled && !new HashSet(this._redefinedAttributeGroups).equals(temp23)) {
            throw new AssertionError();
        }
        Map temp5 = new HashMap();
        Iterator it7 = this._containers.values().iterator();
        while (it7.hasNext()) {
            temp5.putAll(buildComponentRefMap((SchemaComponent[]) ((SchemaContainer) it7.next()).globalTypes().toArray(new SchemaComponent[0])));
        }
        if (!$assertionsDisabled && !this._globalTypes.equals(temp5)) {
            throw new AssertionError();
        }
        Set temp24 = new HashSet();
        Iterator it8 = this._containers.values().iterator();
        while (it8.hasNext()) {
            temp24.addAll(buildComponentRefList((SchemaComponent[]) ((SchemaContainer) it8.next()).redefinedGlobalTypes().toArray(new SchemaComponent[0])));
        }
        if (!$assertionsDisabled && !new HashSet(this._redefinedGlobalTypes).equals(temp24)) {
            throw new AssertionError();
        }
        Map temp6 = new HashMap();
        Iterator it9 = this._containers.values().iterator();
        while (it9.hasNext()) {
            temp6.putAll(buildDocumentMap((SchemaType[]) ((SchemaContainer) it9.next()).documentTypes().toArray(new SchemaType[0])));
        }
        if (!$assertionsDisabled && !this._documentTypes.equals(temp6)) {
            throw new AssertionError();
        }
        Map temp7 = new HashMap();
        Iterator it10 = this._containers.values().iterator();
        while (it10.hasNext()) {
            temp7.putAll(buildAttributeTypeMap((SchemaType[]) ((SchemaContainer) it10.next()).attributeTypes().toArray(new SchemaType[0])));
        }
        if (!$assertionsDisabled && !this._attributeTypes.equals(temp7)) {
            throw new AssertionError();
        }
        Map temp8 = new HashMap();
        Iterator it11 = this._containers.values().iterator();
        while (it11.hasNext()) {
            temp8.putAll(buildComponentRefMap((SchemaComponent[]) ((SchemaContainer) it11.next()).identityConstraints().toArray(new SchemaComponent[0])));
        }
        if (!$assertionsDisabled && !this._identityConstraints.equals(temp8)) {
            throw new AssertionError();
        }
        Set temp25 = new HashSet();
        Iterator it12 = this._containers.values().iterator();
        while (it12.hasNext()) {
            temp25.addAll(((SchemaContainer) it12.next()).annotations());
        }
        if (!$assertionsDisabled && !new HashSet(this._annotations).equals(temp25)) {
            throw new AssertionError();
        }
        Set temp26 = new HashSet();
        Iterator it13 = this._containers.values().iterator();
        while (it13.hasNext()) {
            temp26.add(((SchemaContainer) it13.next()).getNamespace());
        }
        if (!$assertionsDisabled && !this._namespaces.equals(temp26)) {
            throw new AssertionError();
        }
    }

    private static synchronized void nextBytes(byte[] result) throws IOException {
        if (_random == null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream daos = new DataOutputStream(baos);
                daos.writeInt(System.identityHashCode(SchemaTypeSystemImpl.class));
                String[] props = {"user.name", "user.dir", "user.timezone", "user.country", "java.class.path", "java.home", "java.vendor", "java.version", "os.version"};
                for (String str : props) {
                    String prop = SystemProperties.getProperty(str);
                    if (prop != null) {
                        daos.writeUTF(prop);
                        daos.writeInt(System.identityHashCode(prop));
                    }
                }
                daos.writeLong(Runtime.getRuntime().freeMemory());
                daos.close();
                byte[] bytes = baos.toByteArray();
                for (int i = 0; i < bytes.length; i++) {
                    int j = i % _mask.length;
                    byte[] bArr = _mask;
                    bArr[j] = (byte) (bArr[j] * 21);
                    byte[] bArr2 = _mask;
                    bArr2[j] = (byte) (bArr2[j] + i);
                }
            } catch (IOException e) {
                XBeanDebug.logException(e);
            }
            _random = new Random(System.currentTimeMillis());
        }
        _random.nextBytes(result);
        for (int i2 = 0; i2 < result.length; i2++) {
            int i3 = i2;
            result[i3] = (byte) (result[i3] ^ _mask[i2 & _mask.length]);
        }
    }

    public SchemaTypeSystemImpl(String nameForSystem) throws IOException {
        if (nameForSystem == null) {
            byte[] bytes = new byte[16];
            nextBytes(bytes);
            nameForSystem = ExcelXmlConstants.CELL_DATA_FORMAT_TAG + new String(HexBin.encode(bytes));
        }
        this._name = getMetadataPath().replace('/', '.') + ".system." + nameForSystem;
        this._basePackage = nameToPathString(this._name);
        this._classloader = null;
    }

    public void loadFromBuilder(SchemaGlobalElement[] globalElements, SchemaGlobalAttribute[] globalAttributes, SchemaType[] globalTypes, SchemaType[] documentTypes, SchemaType[] attributeTypes) {
        if (!$assertionsDisabled && this._classloader != null) {
            throw new AssertionError();
        }
        this._localHandles = new HandlePool();
        this._globalElements = buildComponentRefMap(globalElements);
        this._globalAttributes = buildComponentRefMap(globalAttributes);
        this._globalTypes = buildComponentRefMap(globalTypes);
        this._documentTypes = buildDocumentMap(documentTypes);
        this._attributeTypes = buildAttributeTypeMap(attributeTypes);
        this._typeRefsByClassname = buildTypeRefsByClassname();
        buildContainers(Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        this._namespaces = new HashSet();
    }

    public void loadFromStscState(StscState state) {
        if (!$assertionsDisabled && this._classloader != null) {
            throw new AssertionError();
        }
        this._localHandles = new HandlePool();
        this._globalElements = buildComponentRefMap(state.globalElements());
        this._globalAttributes = buildComponentRefMap(state.globalAttributes());
        this._modelGroups = buildComponentRefMap(state.modelGroups());
        this._redefinedModelGroups = buildComponentRefList(state.redefinedModelGroups());
        this._attributeGroups = buildComponentRefMap(state.attributeGroups());
        this._redefinedAttributeGroups = buildComponentRefList(state.redefinedAttributeGroups());
        this._globalTypes = buildComponentRefMap(state.globalTypes());
        this._redefinedGlobalTypes = buildComponentRefList(state.redefinedGlobalTypes());
        this._documentTypes = buildDocumentMap(state.documentTypes());
        this._attributeTypes = buildAttributeTypeMap(state.attributeTypes());
        this._typeRefsByClassname = buildTypeRefsByClassname(state.typesByClassname());
        this._identityConstraints = buildComponentRefMap(state.idConstraints());
        this._annotations = state.annotations();
        this._namespaces = new HashSet(Arrays.asList(state.getNamespaces()));
        this._containers = state.getContainerMap();
        fixupContainers();
        assertContainersSynchronized();
        setDependencies(state.getDependencies());
    }

    final SchemaTypeSystemImpl getTypeSystem() {
        return this;
    }

    void setDependencies(SchemaDependencies deps) {
        this._deps = deps;
    }

    SchemaDependencies getDependencies() {
        return this._deps;
    }

    public boolean isIncomplete() {
        return this._incomplete;
    }

    void setIncomplete(boolean incomplete) {
        this._incomplete = incomplete;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeSystemImpl$StringPool.class */
    static class StringPool {
        private List intsToStrings = new ArrayList();
        private Map stringsToInts = new HashMap();
        private String _handle;
        private String _name;

        StringPool(String handle, String name) {
            this._handle = handle;
            this._name = name;
            this.intsToStrings.add(null);
        }

        int codeForString(String str) {
            if (str == null) {
                return 0;
            }
            Integer result = (Integer) this.stringsToInts.get(str);
            if (result == null) {
                result = new Integer(this.intsToStrings.size());
                this.intsToStrings.add(str);
                this.stringsToInts.put(str, result);
            }
            return result.intValue();
        }

        String stringForCode(int code) {
            if (code == 0) {
                return null;
            }
            return (String) this.intsToStrings.get(code);
        }

        void writeTo(DataOutputStream output) throws IOException {
            if (this.intsToStrings.size() >= 65535) {
                throw new SchemaTypeLoaderException("Too many strings (" + this.intsToStrings.size() + ")", this._name, this._handle, 10);
            }
            try {
                output.writeShort(this.intsToStrings.size());
                Iterator i = this.intsToStrings.iterator();
                i.next();
                while (i.hasNext()) {
                    String str = (String) i.next();
                    output.writeUTF(str);
                }
            } catch (IOException e) {
                throw new SchemaTypeLoaderException(e.getMessage(), this._name, this._handle, 9);
            }
        }

        void readFrom(DataInputStream input) throws IOException {
            if (this.intsToStrings.size() != 1 || this.stringsToInts.size() != 0) {
                throw new IllegalStateException();
            }
            try {
                int size = input.readUnsignedShort();
                for (int i = 1; i < size; i++) {
                    String str = input.readUTF().intern();
                    int code = codeForString(str);
                    if (code != i) {
                        throw new IllegalStateException();
                    }
                }
            } catch (IOException e) {
                throw new SchemaTypeLoaderException(e.getMessage() == null ? e.getMessage() : "IO Exception", this._name, this._handle, 9, e);
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeSystemImpl$HandlePool.class */
    class HandlePool {
        private Map _handlesToRefs = new LinkedHashMap();
        private Map _componentsToHandles = new LinkedHashMap();
        private boolean _started;

        HandlePool() {
        }

        private String addUniqueHandle(SchemaComponent obj, String base) {
            String base2 = base.toLowerCase();
            String handle = base2;
            int index = 2;
            while (this._handlesToRefs.containsKey(handle)) {
                handle = base2 + index;
                index++;
            }
            this._handlesToRefs.put(handle, obj.getComponentRef());
            this._componentsToHandles.put(obj, handle);
            return handle;
        }

        String handleForComponent(SchemaComponent comp) {
            if (comp == null) {
                return null;
            }
            if (comp.getTypeSystem() != SchemaTypeSystemImpl.this.getTypeSystem()) {
                throw new IllegalArgumentException("Cannot supply handles for types from another type system");
            }
            if (comp instanceof SchemaType) {
                return handleForType((SchemaType) comp);
            }
            if (comp instanceof SchemaGlobalElement) {
                return handleForElement((SchemaGlobalElement) comp);
            }
            if (comp instanceof SchemaGlobalAttribute) {
                return handleForAttribute((SchemaGlobalAttribute) comp);
            }
            if (comp instanceof SchemaModelGroup) {
                return handleForModelGroup((SchemaModelGroup) comp);
            }
            if (comp instanceof SchemaAttributeGroup) {
                return handleForAttributeGroup((SchemaAttributeGroup) comp);
            }
            if (comp instanceof SchemaIdentityConstraint) {
                return handleForIdentityConstraint((SchemaIdentityConstraint) comp);
            }
            throw new IllegalStateException("Component type cannot have a handle");
        }

        String handleForElement(SchemaGlobalElement element) {
            if (element == null) {
                return null;
            }
            if (element.getTypeSystem() != SchemaTypeSystemImpl.this.getTypeSystem()) {
                throw new IllegalArgumentException("Cannot supply handles for types from another type system");
            }
            String handle = (String) this._componentsToHandles.get(element);
            if (handle == null) {
                handle = addUniqueHandle(element, NameUtil.upperCamelCase(element.getName().getLocalPart()) + "Element");
            }
            return handle;
        }

        String handleForAttribute(SchemaGlobalAttribute attribute) {
            if (attribute == null) {
                return null;
            }
            if (attribute.getTypeSystem() != SchemaTypeSystemImpl.this.getTypeSystem()) {
                throw new IllegalArgumentException("Cannot supply handles for types from another type system");
            }
            String handle = (String) this._componentsToHandles.get(attribute);
            if (handle == null) {
                handle = addUniqueHandle(attribute, NameUtil.upperCamelCase(attribute.getName().getLocalPart()) + "Attribute");
            }
            return handle;
        }

        String handleForModelGroup(SchemaModelGroup group) {
            if (group == null) {
                return null;
            }
            if (group.getTypeSystem() != SchemaTypeSystemImpl.this.getTypeSystem()) {
                throw new IllegalArgumentException("Cannot supply handles for types from another type system");
            }
            String handle = (String) this._componentsToHandles.get(group);
            if (handle == null) {
                handle = addUniqueHandle(group, NameUtil.upperCamelCase(group.getName().getLocalPart()) + "ModelGroup");
            }
            return handle;
        }

        String handleForAttributeGroup(SchemaAttributeGroup group) {
            if (group == null) {
                return null;
            }
            if (group.getTypeSystem() != SchemaTypeSystemImpl.this.getTypeSystem()) {
                throw new IllegalArgumentException("Cannot supply handles for types from another type system");
            }
            String handle = (String) this._componentsToHandles.get(group);
            if (handle == null) {
                handle = addUniqueHandle(group, NameUtil.upperCamelCase(group.getName().getLocalPart()) + "AttributeGroup");
            }
            return handle;
        }

        String handleForIdentityConstraint(SchemaIdentityConstraint idc) {
            if (idc == null) {
                return null;
            }
            if (idc.getTypeSystem() != SchemaTypeSystemImpl.this.getTypeSystem()) {
                throw new IllegalArgumentException("Cannot supply handles for types from another type system");
            }
            String handle = (String) this._componentsToHandles.get(idc);
            if (handle == null) {
                handle = addUniqueHandle(idc, NameUtil.upperCamelCase(idc.getName().getLocalPart()) + "IdentityConstraint");
            }
            return handle;
        }

        String handleForType(SchemaType type) {
            String baseName;
            if (type == null) {
                return null;
            }
            if (type.getTypeSystem() != SchemaTypeSystemImpl.this.getTypeSystem()) {
                throw new IllegalArgumentException("Cannot supply handles for types from another type system");
            }
            String handle = (String) this._componentsToHandles.get(type);
            if (handle == null) {
                QName name = type.getName();
                String suffix = "";
                if (name == null) {
                    if (type.isDocumentType()) {
                        name = type.getDocumentElementName();
                        suffix = "Doc";
                    } else if (type.isAttributeType()) {
                        name = type.getAttributeTypeAttributeName();
                        suffix = "AttrType";
                    } else if (type.getContainerField() != null) {
                        name = type.getContainerField().getName();
                        suffix = type.getContainerField().isAttribute() ? "Attr" : "Elem";
                    }
                }
                String uniq = Integer.toHexString(type.toString().hashCode() | Integer.MIN_VALUE).substring(4).toUpperCase();
                if (name == null) {
                    baseName = "Anon" + uniq + "Type";
                } else {
                    baseName = NameUtil.upperCamelCase(name.getLocalPart()) + uniq + suffix + "Type";
                }
                handle = addUniqueHandle(type, baseName);
            }
            return handle;
        }

        SchemaComponent.Ref refForHandle(String handle) {
            if (handle == null) {
                return null;
            }
            return (SchemaComponent.Ref) this._handlesToRefs.get(handle);
        }

        Set getAllHandles() {
            return this._handlesToRefs.keySet();
        }

        void startWriteMode() {
            this._started = true;
            this._componentsToHandles = new LinkedHashMap();
            for (String handle : this._handlesToRefs.keySet()) {
                SchemaComponent comp = ((SchemaComponent.Ref) this._handlesToRefs.get(handle)).getComponent();
                this._componentsToHandles.put(comp, handle);
            }
        }
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public void saveToDirectory(File classDir) {
        save(new FilerImpl(classDir, null, null, false, false));
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public void save(Filer filer) {
        if (this._incomplete) {
            throw new IllegalStateException("Incomplete SchemaTypeSystems cannot be saved.");
        }
        if (filer == null) {
            throw new IllegalArgumentException("filer must not be null");
        }
        this._filer = filer;
        this._localHandles.startWriteMode();
        saveTypesRecursively(globalTypes());
        saveTypesRecursively(documentTypes());
        saveTypesRecursively(attributeTypes());
        saveGlobalElements(globalElements());
        saveGlobalAttributes(globalAttributes());
        saveModelGroups(modelGroups());
        saveAttributeGroups(attributeGroups());
        saveIdentityConstraints(identityConstraints());
        saveTypesRecursively(redefinedGlobalTypes());
        saveModelGroups(redefinedModelGroups());
        saveAttributeGroups(redefinedAttributeGroups());
        saveIndex();
        savePointers();
        saveLoader();
    }

    void saveTypesRecursively(SchemaType[] types) throws IOException {
        for (int i = 0; i < types.length; i++) {
            if (types[i].getTypeSystem() == getTypeSystem()) {
                saveType(types[i]);
                saveTypesRecursively(types[i].getAnonymousTypes());
            }
        }
    }

    public void saveGlobalElements(SchemaGlobalElement[] elts) throws IOException {
        if (this._incomplete) {
            throw new IllegalStateException("This SchemaTypeSystem cannot be saved.");
        }
        for (SchemaGlobalElement schemaGlobalElement : elts) {
            saveGlobalElement(schemaGlobalElement);
        }
    }

    public void saveGlobalAttributes(SchemaGlobalAttribute[] attrs) throws IOException {
        if (this._incomplete) {
            throw new IllegalStateException("This SchemaTypeSystem cannot be saved.");
        }
        for (SchemaGlobalAttribute schemaGlobalAttribute : attrs) {
            saveGlobalAttribute(schemaGlobalAttribute);
        }
    }

    public void saveModelGroups(SchemaModelGroup[] groups) throws IOException {
        if (this._incomplete) {
            throw new IllegalStateException("This SchemaTypeSystem cannot be saved.");
        }
        for (SchemaModelGroup schemaModelGroup : groups) {
            saveModelGroup(schemaModelGroup);
        }
    }

    public void saveAttributeGroups(SchemaAttributeGroup[] groups) throws IOException {
        if (this._incomplete) {
            throw new IllegalStateException("This SchemaTypeSystem cannot be saved.");
        }
        for (SchemaAttributeGroup schemaAttributeGroup : groups) {
            saveAttributeGroup(schemaAttributeGroup);
        }
    }

    public void saveIdentityConstraints(SchemaIdentityConstraint[] idcs) throws IOException {
        if (this._incomplete) {
            throw new IllegalStateException("This SchemaTypeSystem cannot be saved.");
        }
        for (SchemaIdentityConstraint schemaIdentityConstraint : idcs) {
            saveIdentityConstraint(schemaIdentityConstraint);
        }
    }

    public void saveGlobalElement(SchemaGlobalElement elt) throws IOException {
        if (this._incomplete) {
            throw new IllegalStateException("This SchemaTypeSystem cannot be saved.");
        }
        String handle = this._localHandles.handleForElement(elt);
        XsbReader saver = new XsbReader(handle);
        saver.writeParticleData((SchemaParticle) elt);
        saver.writeString(elt.getSourceName());
        saver.writeRealHeader(handle, 3);
        saver.writeParticleData((SchemaParticle) elt);
        saver.writeString(elt.getSourceName());
        saver.writeEnd();
    }

    public void saveGlobalAttribute(SchemaGlobalAttribute attr) throws IOException {
        if (this._incomplete) {
            throw new IllegalStateException("This SchemaTypeSystem cannot be saved.");
        }
        String handle = this._localHandles.handleForAttribute(attr);
        XsbReader saver = new XsbReader(handle);
        saver.writeAttributeData(attr);
        saver.writeString(attr.getSourceName());
        saver.writeRealHeader(handle, 4);
        saver.writeAttributeData(attr);
        saver.writeString(attr.getSourceName());
        saver.writeEnd();
    }

    public void saveModelGroup(SchemaModelGroup grp) throws IOException {
        if (this._incomplete) {
            throw new IllegalStateException("This SchemaTypeSystem cannot be saved.");
        }
        String handle = this._localHandles.handleForModelGroup(grp);
        XsbReader saver = new XsbReader(handle);
        saver.writeModelGroupData(grp);
        saver.writeRealHeader(handle, 6);
        saver.writeModelGroupData(grp);
        saver.writeEnd();
    }

    public void saveAttributeGroup(SchemaAttributeGroup grp) throws IOException {
        if (this._incomplete) {
            throw new IllegalStateException("This SchemaTypeSystem cannot be saved.");
        }
        String handle = this._localHandles.handleForAttributeGroup(grp);
        XsbReader saver = new XsbReader(handle);
        saver.writeAttributeGroupData(grp);
        saver.writeRealHeader(handle, 7);
        saver.writeAttributeGroupData(grp);
        saver.writeEnd();
    }

    public void saveIdentityConstraint(SchemaIdentityConstraint idc) throws IOException {
        if (this._incomplete) {
            throw new IllegalStateException("This SchemaTypeSystem cannot be saved.");
        }
        String handle = this._localHandles.handleForIdentityConstraint(idc);
        XsbReader saver = new XsbReader(handle);
        saver.writeIdConstraintData(idc);
        saver.writeRealHeader(handle, 8);
        saver.writeIdConstraintData(idc);
        saver.writeEnd();
    }

    void saveType(SchemaType type) throws IOException {
        String handle = this._localHandles.handleForType(type);
        XsbReader saver = new XsbReader(handle);
        saver.writeTypeData(type);
        saver.writeRealHeader(handle, 2);
        saver.writeTypeData(type);
        saver.writeEnd();
    }

    public static String crackPointer(InputStream stream) {
        DataInputStream input = null;
        try {
            input = new DataInputStream(stream);
            int magic = input.readInt();
            if (magic != -629491010) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                    }
                }
                return null;
            }
            int majorver = input.readShort();
            int minorver = input.readShort();
            if (majorver != 2) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e2) {
                    }
                }
                return null;
            }
            if (minorver > 24) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e3) {
                    }
                }
                return null;
            }
            if (majorver > 2 || (majorver == 2 && minorver >= 18)) {
                input.readShort();
            }
            int actualfiletype = input.readShort();
            if (actualfiletype != 5) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e4) {
                    }
                }
                return null;
            }
            StringPool stringPool = new StringPool("pointer", "unk");
            stringPool.readFrom(input);
            String strStringForCode = stringPool.stringForCode(input.readShort());
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e5) {
                }
            }
            return strStringForCode;
        } catch (IOException e6) {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e7) {
                }
            }
            return null;
        } catch (Throwable th) {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e8) {
                }
            }
            throw th;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeSystemImpl$XsbReader.class */
    private class XsbReader {
        DataInputStream _input;
        DataOutputStream _output;
        StringPool _stringPool;
        String _handle;
        private int _majorver;
        private int _minorver;
        private int _releaseno;
        int _actualfiletype;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SchemaTypeSystemImpl.class.desiredAssertionStatus();
        }

        public XsbReader(String handle, int filetype) throws IOException {
            String resourcename = SchemaTypeSystemImpl.this._basePackage + handle + ".xsb";
            InputStream rawinput = getLoaderStream(resourcename);
            if (rawinput == null) {
                throw new SchemaTypeLoaderException("XML-BEANS compiled schema: Could not locate compiled schema resource " + resourcename, SchemaTypeSystemImpl.this._name, handle, 0);
            }
            this._input = new DataInputStream(rawinput);
            this._handle = handle;
            int magic = readInt();
            if (magic != -629491010) {
                throw new SchemaTypeLoaderException("XML-BEANS compiled schema: Wrong magic cookie", SchemaTypeSystemImpl.this._name, handle, 1);
            }
            this._majorver = readShort();
            this._minorver = readShort();
            if (this._majorver != 2) {
                throw new SchemaTypeLoaderException("XML-BEANS compiled schema: Wrong major version - expecting 2, got " + this._majorver, SchemaTypeSystemImpl.this._name, handle, 2);
            }
            if (this._minorver > 24) {
                throw new SchemaTypeLoaderException("XML-BEANS compiled schema: Incompatible minor version - expecting up to 24, got " + this._minorver, SchemaTypeSystemImpl.this._name, handle, 3);
            }
            if (this._minorver < 14) {
                throw new SchemaTypeLoaderException("XML-BEANS compiled schema: Incompatible minor version - expecting at least 14, got " + this._minorver, SchemaTypeSystemImpl.this._name, handle, 3);
            }
            if (atLeast(2, 18, 0)) {
                this._releaseno = readShort();
            }
            int actualfiletype = readShort();
            if (actualfiletype != filetype && filetype != 65535) {
                throw new SchemaTypeLoaderException("XML-BEANS compiled schema: File has the wrong type - expecting type " + filetype + ", got type " + actualfiletype, SchemaTypeSystemImpl.this._name, handle, 4);
            }
            this._stringPool = new StringPool(this._handle, SchemaTypeSystemImpl.this._name);
            this._stringPool.readFrom(this._input);
            this._actualfiletype = actualfiletype;
        }

        protected boolean atLeast(int majorver, int minorver, int releaseno) {
            if (this._majorver > majorver) {
                return true;
            }
            if (this._majorver < majorver) {
                return false;
            }
            if (this._minorver > minorver) {
                return true;
            }
            return this._minorver >= minorver && this._releaseno >= releaseno;
        }

        protected boolean atMost(int majorver, int minorver, int releaseno) {
            if (this._majorver > majorver) {
                return false;
            }
            if (this._majorver < majorver) {
                return true;
            }
            if (this._minorver > minorver) {
                return false;
            }
            return this._minorver < minorver || this._releaseno <= releaseno;
        }

        int getActualFiletype() {
            return this._actualfiletype;
        }

        XsbReader(String handle) {
            this._handle = handle;
            this._stringPool = new StringPool(this._handle, SchemaTypeSystemImpl.this._name);
        }

        void writeRealHeader(String handle, int filetype) throws IOException {
            String resourcename;
            if (handle.indexOf(47) < 0) {
                resourcename = SchemaTypeSystemImpl.this._basePackage + handle + ".xsb";
            } else {
                resourcename = handle + ".xsb";
            }
            OutputStream rawoutput = getSaverStream(resourcename);
            if (rawoutput == null) {
                throw new SchemaTypeLoaderException("Could not write compiled schema resource " + resourcename, SchemaTypeSystemImpl.this._name, handle, 12);
            }
            this._output = new DataOutputStream(rawoutput);
            this._handle = handle;
            writeInt(-629491010);
            writeShort(2);
            writeShort(24);
            writeShort(0);
            writeShort(filetype);
            this._stringPool.writeTo(this._output);
        }

        void readEnd() {
            try {
                if (this._input != null) {
                    this._input.close();
                }
            } catch (IOException e) {
            }
            this._input = null;
            this._stringPool = null;
            this._handle = null;
        }

        void writeEnd() throws IOException {
            try {
                if (this._output != null) {
                    this._output.flush();
                    this._output.close();
                }
                this._output = null;
                this._stringPool = null;
                this._handle = null;
            } catch (IOException e) {
                throw new SchemaTypeLoaderException(e.getMessage(), SchemaTypeSystemImpl.this._name, this._handle, 9);
            }
        }

        int fileTypeFromComponentType(int componentType) {
            switch (componentType) {
                case 0:
                    return 2;
                case 1:
                    return 3;
                case 2:
                default:
                    throw new IllegalStateException("Unexpected component type");
                case 3:
                    return 4;
                case 4:
                    return 7;
                case 5:
                    return 8;
                case 6:
                    return 6;
            }
        }

        void writeIndexData() throws IOException {
            writeHandlePool(SchemaTypeSystemImpl.this._localHandles);
            writeQNameMap(SchemaTypeSystemImpl.this.globalElements());
            writeQNameMap(SchemaTypeSystemImpl.this.globalAttributes());
            writeQNameMap(SchemaTypeSystemImpl.this.modelGroups());
            writeQNameMap(SchemaTypeSystemImpl.this.attributeGroups());
            writeQNameMap(SchemaTypeSystemImpl.this.identityConstraints());
            writeQNameMap(SchemaTypeSystemImpl.this.globalTypes());
            writeDocumentTypeMap(SchemaTypeSystemImpl.this.documentTypes());
            writeAttributeTypeMap(SchemaTypeSystemImpl.this.attributeTypes());
            writeClassnameMap(SchemaTypeSystemImpl.this._typeRefsByClassname);
            writeNamespaces(SchemaTypeSystemImpl.this._namespaces);
            writeQNameMap(SchemaTypeSystemImpl.this.redefinedGlobalTypes());
            writeQNameMap(SchemaTypeSystemImpl.this.redefinedModelGroups());
            writeQNameMap(SchemaTypeSystemImpl.this.redefinedAttributeGroups());
            writeAnnotations(SchemaTypeSystemImpl.this.annotations());
        }

        void writeHandlePool(HandlePool pool) throws IOException {
            writeShort(pool._componentsToHandles.size());
            for (SchemaComponent comp : pool._componentsToHandles.keySet()) {
                String handle = (String) pool._componentsToHandles.get(comp);
                int code = fileTypeFromComponentType(comp.getComponentType());
                writeString(handle);
                writeShort(code);
            }
        }

        void readHandlePool(HandlePool pool) {
            Object ref;
            if (pool._handlesToRefs.size() != 0 || pool._started) {
                throw new IllegalStateException("Nonempty handle set before read");
            }
            int size = readShort();
            for (int i = 0; i < size; i++) {
                String handle = readString();
                int code = readShort();
                switch (code) {
                    case 2:
                        ref = new SchemaType.Ref(SchemaTypeSystemImpl.this.getTypeSystem(), handle);
                        break;
                    case 3:
                        ref = new SchemaGlobalElement.Ref(SchemaTypeSystemImpl.this.getTypeSystem(), handle);
                        break;
                    case 4:
                        ref = new SchemaGlobalAttribute.Ref(SchemaTypeSystemImpl.this.getTypeSystem(), handle);
                        break;
                    case 5:
                    default:
                        throw new SchemaTypeLoaderException("Schema index has an unrecognized entry of type " + code, SchemaTypeSystemImpl.this._name, handle, 5);
                    case 6:
                        ref = new SchemaModelGroup.Ref(SchemaTypeSystemImpl.this.getTypeSystem(), handle);
                        break;
                    case 7:
                        ref = new SchemaAttributeGroup.Ref(SchemaTypeSystemImpl.this.getTypeSystem(), handle);
                        break;
                    case 8:
                        ref = new SchemaIdentityConstraint.Ref(SchemaTypeSystemImpl.this.getTypeSystem(), handle);
                        break;
                }
                Object result = ref;
                pool._handlesToRefs.put(handle, result);
            }
        }

        int readShort() {
            try {
                return this._input.readUnsignedShort();
            } catch (IOException e) {
                throw new SchemaTypeLoaderException(e.getMessage(), SchemaTypeSystemImpl.this._name, this._handle, 9);
            }
        }

        void writeShort(int s) throws IOException {
            if (s >= 65535 || s < -1) {
                throw new SchemaTypeLoaderException("Value " + s + " out of range: must fit in a 16-bit unsigned short.", SchemaTypeSystemImpl.this._name, this._handle, 10);
            }
            if (this._output != null) {
                try {
                    this._output.writeShort(s);
                } catch (IOException e) {
                    throw new SchemaTypeLoaderException(e.getMessage(), SchemaTypeSystemImpl.this._name, this._handle, 9);
                }
            }
        }

        int readInt() {
            try {
                return this._input.readInt();
            } catch (IOException e) {
                throw new SchemaTypeLoaderException(e.getMessage(), SchemaTypeSystemImpl.this._name, this._handle, 9);
            }
        }

        void writeInt(int i) throws IOException {
            if (this._output != null) {
                try {
                    this._output.writeInt(i);
                } catch (IOException e) {
                    throw new SchemaTypeLoaderException(e.getMessage(), SchemaTypeSystemImpl.this._name, this._handle, 9);
                }
            }
        }

        String readString() {
            return this._stringPool.stringForCode(readShort());
        }

        void writeString(String str) throws IOException {
            int code = this._stringPool.codeForString(str);
            writeShort(code);
        }

        QName readQName() {
            String namespace = readString();
            String localname = readString();
            if (localname == null) {
                return null;
            }
            return new QName(namespace, localname);
        }

        void writeQName(QName qname) throws IOException {
            if (qname == null) {
                writeString(null);
                writeString(null);
            } else {
                writeString(qname.getNamespaceURI());
                writeString(qname.getLocalPart());
            }
        }

        SOAPArrayType readSOAPArrayType() {
            QName qName = readQName();
            String dimensions = readString();
            if (qName == null) {
                return null;
            }
            return new SOAPArrayType(qName, dimensions);
        }

        void writeSOAPArrayType(SOAPArrayType arrayType) throws IOException {
            if (arrayType == null) {
                writeQName(null);
                writeString(null);
            } else {
                writeQName(arrayType.getQName());
                writeString(arrayType.soap11DimensionString());
            }
        }

        void writeAnnotation(SchemaAnnotation a) throws IOException {
            if (a == null) {
                writeInt(-1);
                return;
            }
            SchemaAnnotation.Attribute[] attributes = a.getAttributes();
            writeInt(attributes.length);
            for (int i = 0; i < attributes.length; i++) {
                QName name = attributes[i].getName();
                String value = attributes[i].getValue();
                String valueURI = attributes[i].getValueUri();
                writeQName(name);
                writeString(value);
                writeString(valueURI);
            }
            XmlObject[] documentationItems = a.getUserInformation();
            writeInt(documentationItems.length);
            XmlOptions opt = new XmlOptions().setSaveOuter().setSaveAggressiveNamespaces();
            for (XmlObject doc : documentationItems) {
                writeString(doc.xmlText(opt));
            }
            XmlObject[] appInfoItems = a.getApplicationInformation();
            writeInt(appInfoItems.length);
            for (XmlObject doc2 : appInfoItems) {
                writeString(doc2.xmlText(opt));
            }
        }

        SchemaAnnotation readAnnotation(SchemaContainer c) {
            int n;
            if (!atLeast(2, 19, 0) || (n = readInt()) == -1) {
                return null;
            }
            SchemaAnnotation.Attribute[] attributes = new SchemaAnnotation.Attribute[n];
            for (int i = 0; i < n; i++) {
                QName name = readQName();
                String value = readString();
                String valueUri = null;
                if (atLeast(2, 24, 0)) {
                    valueUri = readString();
                }
                attributes[i] = new SchemaAnnotationImpl.AttributeImpl(name, value, valueUri);
            }
            int n2 = readInt();
            String[] docStrings = new String[n2];
            for (int i2 = 0; i2 < n2; i2++) {
                docStrings[i2] = readString();
            }
            int n3 = readInt();
            String[] appInfoStrings = new String[n3];
            for (int i3 = 0; i3 < n3; i3++) {
                appInfoStrings[i3] = readString();
            }
            return new SchemaAnnotationImpl(c, appInfoStrings, docStrings, attributes);
        }

        void writeAnnotations(SchemaAnnotation[] anns) throws IOException {
            writeInt(anns.length);
            for (SchemaAnnotation schemaAnnotation : anns) {
                writeAnnotation(schemaAnnotation);
            }
        }

        List readAnnotations() {
            int n = readInt();
            List result = new ArrayList(n);
            SchemaContainer container = SchemaTypeSystemImpl.this.getContainerNonNull("");
            for (int i = 0; i < n; i++) {
                result.add(readAnnotation(container));
            }
            return result;
        }

        SchemaComponent.Ref readHandle() {
            String handle = readString();
            if (handle == null) {
                return null;
            }
            if (handle.charAt(0) != '_') {
                return SchemaTypeSystemImpl.this._localHandles.refForHandle(handle);
            }
            switch (handle.charAt(2)) {
                case 'A':
                    return SchemaTypeSystemImpl.this._linker.findAttributeRef(QNameHelper.forPretty(handle, 4));
                case 'B':
                case 'C':
                case 'F':
                case 'G':
                case 'H':
                case 'J':
                case 'K':
                case 'L':
                case 'P':
                case 'Q':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                default:
                    throw new SchemaTypeLoaderException("Cannot resolve handle " + handle, SchemaTypeSystemImpl.this._name, this._handle, 13);
                case 'D':
                    return SchemaTypeSystemImpl.this._linker.findIdentityConstraintRef(QNameHelper.forPretty(handle, 4));
                case 'E':
                    return SchemaTypeSystemImpl.this._linker.findElementRef(QNameHelper.forPretty(handle, 4));
                case 'I':
                    SchemaType st = (SchemaType) BuiltinSchemaTypeSystem.get().resolveHandle(handle);
                    if (st != null) {
                        return st.getRef();
                    }
                    return ((SchemaType) XQuerySchemaTypeSystem.get().resolveHandle(handle)).getRef();
                case 'M':
                    return SchemaTypeSystemImpl.this._linker.findModelGroupRef(QNameHelper.forPretty(handle, 4));
                case 'N':
                    return SchemaTypeSystemImpl.this._linker.findAttributeGroupRef(QNameHelper.forPretty(handle, 4));
                case 'O':
                    return SchemaTypeSystemImpl.this._linker.findDocumentTypeRef(QNameHelper.forPretty(handle, 4));
                case 'R':
                    SchemaGlobalAttribute attr = SchemaTypeSystemImpl.this._linker.findAttribute(QNameHelper.forPretty(handle, 4));
                    if (attr == null) {
                        throw new SchemaTypeLoaderException("Cannot resolve attribute for handle " + handle, SchemaTypeSystemImpl.this._name, this._handle, 13);
                    }
                    return attr.getType().getRef();
                case 'S':
                    SchemaGlobalElement elem = SchemaTypeSystemImpl.this._linker.findElement(QNameHelper.forPretty(handle, 4));
                    if (elem == null) {
                        throw new SchemaTypeLoaderException("Cannot resolve element for handle " + handle, SchemaTypeSystemImpl.this._name, this._handle, 13);
                    }
                    return elem.getType().getRef();
                case 'T':
                    return SchemaTypeSystemImpl.this._linker.findTypeRef(QNameHelper.forPretty(handle, 4));
                case 'Y':
                    SchemaType type = SchemaTypeSystemImpl.this._linker.typeForSignature(handle.substring(4));
                    if (type == null) {
                        throw new SchemaTypeLoaderException("Cannot resolve type for handle " + handle, SchemaTypeSystemImpl.this._name, this._handle, 13);
                    }
                    return type.getRef();
            }
        }

        void writeHandle(SchemaComponent comp) throws IOException {
            if (comp == null || comp.getTypeSystem() == SchemaTypeSystemImpl.this.getTypeSystem()) {
                writeString(SchemaTypeSystemImpl.this._localHandles.handleForComponent(comp));
                return;
            }
            switch (comp.getComponentType()) {
                case 0:
                    SchemaType type = (SchemaType) comp;
                    if (type.isBuiltinType()) {
                        writeString("_BI_" + type.getName().getLocalPart());
                        return;
                    }
                    if (type.getName() != null) {
                        writeString("_XT_" + QNameHelper.pretty(type.getName()));
                        return;
                    } else if (type.isDocumentType()) {
                        writeString("_XO_" + QNameHelper.pretty(type.getDocumentElementName()));
                        return;
                    } else {
                        writeString("_XY_" + type.toString());
                        return;
                    }
                case 1:
                    writeString("_XE_" + QNameHelper.pretty(comp.getName()));
                    return;
                case 2:
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    throw new SchemaTypeLoaderException("Cannot write handle for component " + comp, SchemaTypeSystemImpl.this._name, this._handle, 13);
                case 3:
                    writeString("_XA_" + QNameHelper.pretty(comp.getName()));
                    return;
                case 4:
                    writeString("_XN_" + QNameHelper.pretty(comp.getName()));
                    return;
                case 5:
                    writeString("_XD_" + QNameHelper.pretty(comp.getName()));
                    return;
                case 6:
                    writeString("_XM_" + QNameHelper.pretty(comp.getName()));
                    return;
            }
        }

        SchemaType.Ref readTypeRef() {
            return (SchemaType.Ref) readHandle();
        }

        void writeType(SchemaType type) throws IOException {
            writeHandle(type);
        }

        Map readQNameRefMap() {
            Map result = new HashMap();
            int size = readShort();
            for (int i = 0; i < size; i++) {
                QName name = readQName();
                SchemaComponent.Ref obj = readHandle();
                result.put(name, obj);
            }
            return result;
        }

        List readQNameRefMapAsList(List names) {
            int size = readShort();
            List result = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                QName name = readQName();
                SchemaComponent.Ref obj = readHandle();
                result.add(obj);
                names.add(name);
            }
            return result;
        }

        void writeQNameMap(SchemaComponent[] components) throws IOException {
            writeShort(components.length);
            for (int i = 0; i < components.length; i++) {
                writeQName(components[i].getName());
                writeHandle(components[i]);
            }
        }

        void writeDocumentTypeMap(SchemaType[] doctypes) throws IOException {
            writeShort(doctypes.length);
            for (int i = 0; i < doctypes.length; i++) {
                writeQName(doctypes[i].getDocumentElementName());
                writeHandle(doctypes[i]);
            }
        }

        void writeAttributeTypeMap(SchemaType[] attrtypes) throws IOException {
            writeShort(attrtypes.length);
            for (int i = 0; i < attrtypes.length; i++) {
                writeQName(attrtypes[i].getAttributeTypeAttributeName());
                writeHandle(attrtypes[i]);
            }
        }

        SchemaType.Ref[] readTypeRefArray() {
            int size = readShort();
            SchemaType.Ref[] result = new SchemaType.Ref[size];
            for (int i = 0; i < size; i++) {
                result[i] = readTypeRef();
            }
            return result;
        }

        void writeTypeArray(SchemaType[] array) throws IOException {
            writeShort(array.length);
            for (SchemaType schemaType : array) {
                writeHandle(schemaType);
            }
        }

        Map readClassnameRefMap() {
            Map result = new HashMap();
            int size = readShort();
            for (int i = 0; i < size; i++) {
                String name = readString();
                SchemaComponent.Ref obj = readHandle();
                result.put(name, obj);
            }
            return result;
        }

        void writeClassnameMap(Map typesByClass) throws IOException {
            writeShort(typesByClass.size());
            for (String className : typesByClass.keySet()) {
                writeString(className);
                writeHandle(((SchemaType.Ref) typesByClass.get(className)).get());
            }
        }

        Set readNamespaces() {
            Set result = new HashSet();
            int size = readShort();
            for (int i = 0; i < size; i++) {
                String ns = readString();
                result.add(ns);
            }
            return result;
        }

        void writeNamespaces(Set namespaces) throws IOException {
            writeShort(namespaces.size());
            Iterator i = namespaces.iterator();
            while (i.hasNext()) {
                String ns = (String) i.next();
                writeString(ns);
            }
        }

        OutputStream getSaverStream(String name) {
            try {
                return SchemaTypeSystemImpl.this._filer.createBinaryFile(name);
            } catch (IOException e) {
                throw new SchemaTypeLoaderException(e.getMessage(), SchemaTypeSystemImpl.this._name, this._handle, 9);
            }
        }

        InputStream getLoaderStream(String resourcename) {
            return SchemaTypeSystemImpl.this._resourceLoader.getResourceAsStream(resourcename);
        }

        void checkContainerNotNull(SchemaContainer container, QName name) {
            if (container == null) {
                throw new LinkageError("Loading of resource " + SchemaTypeSystemImpl.this._name + '.' + this._handle + "failed, information from " + SchemaTypeSystemImpl.this._name + ".index.xsb is  out of sync (or conflicting index files found)");
            }
        }

        public SchemaGlobalElement finishLoadingElement() {
            try {
                try {
                    int particleType = readShort();
                    if (particleType != 4) {
                        throw new SchemaTypeLoaderException("Wrong particle type ", SchemaTypeSystemImpl.this._name, this._handle, 11);
                    }
                    int particleFlags = readShort();
                    BigInteger minOccurs = readBigInteger();
                    BigInteger maxOccurs = readBigInteger();
                    QNameSet transitionRules = readQNameSet();
                    QName name = readQName();
                    SchemaContainer container = SchemaTypeSystemImpl.this.getContainer(name.getNamespaceURI());
                    checkContainerNotNull(container, name);
                    SchemaGlobalElementImpl impl = new SchemaGlobalElementImpl(container);
                    impl.setParticleType(particleType);
                    impl.setMinOccurs(minOccurs);
                    impl.setMaxOccurs(maxOccurs);
                    impl.setTransitionRules(transitionRules, (particleFlags & 1) != 0);
                    impl.setNameAndTypeRef(name, readTypeRef());
                    impl.setDefault(readString(), (particleFlags & 4) != 0, null);
                    if (atLeast(2, 16, 0)) {
                        impl.setDefaultValue(readXmlValueObject());
                    }
                    impl.setNillable((particleFlags & 8) != 0);
                    impl.setBlock((particleFlags & 16) != 0, (particleFlags & 32) != 0, (particleFlags & 64) != 0);
                    impl.setWsdlArrayType(readSOAPArrayType());
                    impl.setAbstract((particleFlags & 128) != 0);
                    impl.setAnnotation(readAnnotation(container));
                    impl.setFinal((particleFlags & 256) != 0, (particleFlags & 512) != 0);
                    if (atLeast(2, 17, 0)) {
                        impl.setSubstitutionGroup((SchemaGlobalElement.Ref) readHandle());
                    }
                    int substGroupCount = readShort();
                    for (int i = 0; i < substGroupCount; i++) {
                        impl.addSubstitutionGroupMember(readQName());
                    }
                    SchemaIdentityConstraint.Ref[] idcs = new SchemaIdentityConstraint.Ref[readShort()];
                    for (int i2 = 0; i2 < idcs.length; i2++) {
                        idcs[i2] = (SchemaIdentityConstraint.Ref) readHandle();
                    }
                    impl.setIdentityConstraints(idcs);
                    impl.setFilename(readString());
                    readEnd();
                    return impl;
                } catch (SchemaTypeLoaderException e) {
                    throw e;
                } catch (Exception e2) {
                    throw new SchemaTypeLoaderException("Cannot load type from typesystem", SchemaTypeSystemImpl.this._name, null, 14, e2);
                }
            } catch (Throwable th) {
                readEnd();
                throw th;
            }
        }

        public SchemaGlobalAttribute finishLoadingAttribute() {
            try {
                try {
                    QName name = readQName();
                    SchemaContainer container = SchemaTypeSystemImpl.this.getContainer(name.getNamespaceURI());
                    checkContainerNotNull(container, name);
                    SchemaGlobalAttributeImpl impl = new SchemaGlobalAttributeImpl(container);
                    loadAttribute(impl, name, container);
                    impl.setFilename(readString());
                    readEnd();
                    return impl;
                } catch (SchemaTypeLoaderException e) {
                    throw e;
                } catch (Exception e2) {
                    throw new SchemaTypeLoaderException("Cannot load type from typesystem", SchemaTypeSystemImpl.this._name, this._handle, 14, e2);
                }
            } catch (Throwable th) {
                readEnd();
                throw th;
            }
        }

        SchemaModelGroup finishLoadingModelGroup() {
            QName name = readQName();
            SchemaContainer container = SchemaTypeSystemImpl.this.getContainer(name.getNamespaceURI());
            checkContainerNotNull(container, name);
            SchemaModelGroupImpl impl = new SchemaModelGroupImpl(container);
            try {
                try {
                    String string = readString();
                    boolean z = readShort() == 1;
                    String string2 = atLeast(2, 22, 0) ? readString() : null;
                    String string3 = atLeast(2, 22, 0) ? readString() : null;
                    boolean z2 = atLeast(2, 15, 0) && readShort() == 1;
                    impl.init(name, string, z, string2, string3, z2, GroupDocument.Factory.parse(readString()).getGroup(), readAnnotation(container), null);
                    if (atLeast(2, 21, 0)) {
                        impl.setFilename(readString());
                    }
                    return impl;
                } catch (SchemaTypeLoaderException e) {
                    throw e;
                } catch (Exception e2) {
                    throw new SchemaTypeLoaderException("Cannot load type from typesystem", SchemaTypeSystemImpl.this._name, this._handle, 14, e2);
                }
            } finally {
                readEnd();
            }
        }

        SchemaIdentityConstraint finishLoadingIdentityConstraint() {
            try {
                try {
                    try {
                        QName name = readQName();
                        SchemaContainer container = SchemaTypeSystemImpl.this.getContainer(name.getNamespaceURI());
                        checkContainerNotNull(container, name);
                        SchemaIdentityConstraintImpl impl = new SchemaIdentityConstraintImpl(container);
                        impl.setName(name);
                        impl.setConstraintCategory(readShort());
                        impl.setSelector(readString());
                        impl.setAnnotation(readAnnotation(container));
                        String[] fields = new String[readShort()];
                        for (int i = 0; i < fields.length; i++) {
                            fields[i] = readString();
                        }
                        impl.setFields(fields);
                        if (impl.getConstraintCategory() == 2) {
                            impl.setReferencedKey((SchemaIdentityConstraint.Ref) readHandle());
                        }
                        int mapCount = readShort();
                        Map nsMappings = new HashMap();
                        for (int i2 = 0; i2 < mapCount; i2++) {
                            String prefix = readString();
                            String uri = readString();
                            nsMappings.put(prefix, uri);
                        }
                        impl.setNSMap(nsMappings);
                        if (atLeast(2, 21, 0)) {
                            impl.setFilename(readString());
                        }
                        return impl;
                    } catch (SchemaTypeLoaderException e) {
                        throw e;
                    }
                } catch (Exception e2) {
                    throw new SchemaTypeLoaderException("Cannot load type from typesystem", SchemaTypeSystemImpl.this._name, this._handle, 14, e2);
                }
            } finally {
                readEnd();
            }
        }

        SchemaAttributeGroup finishLoadingAttributeGroup() {
            QName name = readQName();
            SchemaContainer container = SchemaTypeSystemImpl.this.getContainer(name.getNamespaceURI());
            checkContainerNotNull(container, name);
            SchemaAttributeGroupImpl impl = new SchemaAttributeGroupImpl(container);
            try {
                try {
                    try {
                        String string = readString();
                        boolean z = readShort() == 1;
                        String string2 = atLeast(2, 22, 0) ? readString() : null;
                        boolean z2 = atLeast(2, 15, 0) && readShort() == 1;
                        impl.init(name, string, z, string2, z2, AttributeGroupDocument.Factory.parse(readString()).getAttributeGroup(), readAnnotation(container), null);
                        if (atLeast(2, 21, 0)) {
                            impl.setFilename(readString());
                        }
                        return impl;
                    } catch (SchemaTypeLoaderException e) {
                        throw e;
                    }
                } catch (Exception e2) {
                    throw new SchemaTypeLoaderException("Cannot load type from typesystem", SchemaTypeSystemImpl.this._name, this._handle, 14, e2);
                }
            } finally {
                readEnd();
            }
        }

        public SchemaType finishLoadingType() {
            QName name;
            try {
                try {
                    SchemaContainer cNonNull = SchemaTypeSystemImpl.this.getContainerNonNull("");
                    SchemaTypeImpl impl = new SchemaTypeImpl(cNonNull, true);
                    impl.setName(readQName());
                    impl.setOuterSchemaTypeRef(readTypeRef());
                    impl.setBaseDepth(readShort());
                    impl.setBaseTypeRef(readTypeRef());
                    impl.setDerivationType(readShort());
                    impl.setAnnotation(readAnnotation(null));
                    switch (readShort()) {
                        case 1:
                            impl.setContainerFieldRef(readHandle());
                            break;
                        case 2:
                            impl.setContainerFieldIndex((short) 1, readShort());
                            break;
                        case 3:
                            impl.setContainerFieldIndex((short) 2, readShort());
                            break;
                    }
                    String jn = readString();
                    impl.setFullJavaName(jn == null ? "" : jn);
                    String jn2 = readString();
                    impl.setFullJavaImplName(jn2 == null ? "" : jn2);
                    impl.setAnonymousTypeRefs(readTypeRefArray());
                    impl.setAnonymousUnionMemberOrdinal(readShort());
                    int flags = readInt();
                    boolean isComplexType = (flags & 1) == 0;
                    impl.setCompiled((flags & 2048) != 0);
                    impl.setDocumentType((flags & 2) != 0);
                    impl.setAttributeType((flags & 524288) != 0);
                    impl.setSimpleType(!isComplexType);
                    int complexVariety = 0;
                    if (isComplexType) {
                        impl.setAbstractFinal((flags & 262144) != 0, (flags & 16384) != 0, (flags & 32768) != 0, (flags & 131072) != 0, (flags & 65536) != 0);
                        impl.setBlock((flags & 4096) != 0, (flags & 8192) != 0);
                        impl.setOrderSensitive((flags & 512) != 0);
                        complexVariety = readShort();
                        impl.setComplexTypeVariety(complexVariety);
                        if (atLeast(2, 23, 0)) {
                            impl.setContentBasedOnTypeRef(readTypeRef());
                        }
                        SchemaAttributeModelImpl attrModel = new SchemaAttributeModelImpl();
                        int attrCount = readShort();
                        for (int i = 0; i < attrCount; i++) {
                            attrModel.addAttribute(readAttributeData());
                        }
                        attrModel.setWildcardSet(readQNameSet());
                        attrModel.setWildcardProcess(readShort());
                        Map attrProperties = new LinkedHashMap();
                        int attrPropCount = readShort();
                        for (int i2 = 0; i2 < attrPropCount; i2++) {
                            SchemaProperty prop = readPropertyData();
                            if (!prop.isAttribute()) {
                                throw new SchemaTypeLoaderException("Attribute property " + i2 + " is not an attribute", SchemaTypeSystemImpl.this._name, this._handle, 6);
                            }
                            attrProperties.put(prop.getName(), prop);
                        }
                        SchemaParticle contentModel = null;
                        Map elemProperties = null;
                        int isAll = 0;
                        if (complexVariety == 3 || complexVariety == 4) {
                            isAll = readShort();
                            SchemaParticle[] parts = readParticleArray();
                            if (parts.length == 1) {
                                contentModel = parts[0];
                            } else if (parts.length == 0) {
                                contentModel = null;
                            } else {
                                throw new SchemaTypeLoaderException("Content model not well-formed", SchemaTypeSystemImpl.this._name, this._handle, 7);
                            }
                            elemProperties = new LinkedHashMap();
                            int elemPropCount = readShort();
                            for (int i3 = 0; i3 < elemPropCount; i3++) {
                                SchemaProperty prop2 = readPropertyData();
                                if (prop2.isAttribute()) {
                                    throw new SchemaTypeLoaderException("Element property " + i3 + " is not an element", SchemaTypeSystemImpl.this._name, this._handle, 6);
                                }
                                elemProperties.put(prop2.getName(), prop2);
                            }
                        }
                        impl.setContentModel(contentModel, attrModel, elemProperties, attrProperties, isAll == 1);
                        StscComplexTypeResolver.WildcardResult wcElt = StscComplexTypeResolver.summarizeEltWildcards(contentModel);
                        StscComplexTypeResolver.WildcardResult wcAttr = StscComplexTypeResolver.summarizeAttrWildcards(attrModel);
                        impl.setWildcardSummary(wcElt.typedWildcards, wcElt.hasWildcards, wcAttr.typedWildcards, wcAttr.hasWildcards);
                    }
                    if (!isComplexType || complexVariety == 2) {
                        int simpleVariety = readShort();
                        impl.setSimpleTypeVariety(simpleVariety);
                        boolean isStringEnum = (flags & 64) != 0;
                        impl.setOrdered((flags & 4) != 0 ? 0 : (flags & 1024) != 0 ? 2 : 1);
                        impl.setBounded((flags & 8) != 0);
                        impl.setFinite((flags & 16) != 0);
                        impl.setNumeric((flags & 32) != 0);
                        impl.setUnionOfLists((flags & 128) != 0);
                        impl.setSimpleFinal((flags & 32768) != 0, (flags & 131072) != 0, (flags & 65536) != 0);
                        XmlValueRef[] facets = new XmlValueRef[12];
                        boolean[] fixedFacets = new boolean[12];
                        int facetCount = readShort();
                        for (int i4 = 0; i4 < facetCount; i4++) {
                            int facetCode = readShort();
                            facets[facetCode] = readXmlValueObject();
                            fixedFacets[facetCode] = readShort() == 1;
                        }
                        impl.setBasicFacets(facets, fixedFacets);
                        impl.setWhiteSpaceRule(readShort());
                        impl.setPatternFacet((flags & 256) != 0);
                        int patternCount = readShort();
                        RegularExpression[] patterns = new RegularExpression[patternCount];
                        for (int i5 = 0; i5 < patternCount; i5++) {
                            patterns[i5] = new RegularExpression(readString(), "X");
                        }
                        impl.setPatterns(patterns);
                        int enumCount = readShort();
                        XmlValueRef[] enumValues = new XmlValueRef[enumCount];
                        for (int i6 = 0; i6 < enumCount; i6++) {
                            enumValues[i6] = readXmlValueObject();
                        }
                        impl.setEnumerationValues(enumCount == 0 ? null : enumValues);
                        impl.setBaseEnumTypeRef(readTypeRef());
                        if (isStringEnum) {
                            int seCount = readShort();
                            SchemaStringEnumEntry[] entries = new SchemaStringEnumEntry[seCount];
                            for (int i7 = 0; i7 < seCount; i7++) {
                                entries[i7] = new SchemaStringEnumEntryImpl(readString(), readShort(), readString());
                            }
                            impl.setStringEnumEntries(entries);
                        }
                        switch (simpleVariety) {
                            case 1:
                                impl.setPrimitiveTypeRef(readTypeRef());
                                impl.setDecimalSize(readInt());
                                break;
                            case 2:
                                impl.setPrimitiveTypeRef(BuiltinSchemaTypeSystem.ST_ANY_SIMPLE.getRef());
                                impl.setUnionMemberTypeRefs(readTypeRefArray());
                                break;
                            case 3:
                                impl.setPrimitiveTypeRef(BuiltinSchemaTypeSystem.ST_ANY_SIMPLE.getRef());
                                impl.setListItemTypeRef(readTypeRef());
                                break;
                            default:
                                throw new SchemaTypeLoaderException("Simple type does not have a recognized variety", SchemaTypeSystemImpl.this._name, this._handle, 8);
                        }
                    }
                    impl.setFilename(readString());
                    if (impl.getName() != null) {
                        SchemaContainer container = SchemaTypeSystemImpl.this.getContainer(impl.getName().getNamespaceURI());
                        checkContainerNotNull(container, impl.getName());
                        impl.setContainer(container);
                    } else if (impl.isDocumentType()) {
                        QName name2 = impl.getDocumentElementName();
                        if (name2 != null) {
                            SchemaContainer container2 = SchemaTypeSystemImpl.this.getContainer(name2.getNamespaceURI());
                            checkContainerNotNull(container2, name2);
                            impl.setContainer(container2);
                        }
                    } else if (impl.isAttributeType() && (name = impl.getAttributeTypeAttributeName()) != null) {
                        SchemaContainer container3 = SchemaTypeSystemImpl.this.getContainer(name.getNamespaceURI());
                        checkContainerNotNull(container3, name);
                        impl.setContainer(container3);
                    }
                    return impl;
                } catch (SchemaTypeLoaderException e) {
                    throw e;
                } catch (Exception e2) {
                    throw new SchemaTypeLoaderException("Cannot load type from typesystem", SchemaTypeSystemImpl.this._name, this._handle, 14, e2);
                }
            } finally {
                readEnd();
            }
        }

        void writeTypeData(SchemaType type) throws IOException {
            SchemaParticle[] parts;
            writeQName(type.getName());
            writeType(type.getOuterType());
            writeShort(((SchemaTypeImpl) type).getBaseDepth());
            writeType(type.getBaseType());
            writeShort(type.getDerivationType());
            writeAnnotation(type.getAnnotation());
            if (type.getContainerField() == null) {
                writeShort(0);
            } else if (type.getOuterType().isAttributeType() || type.getOuterType().isDocumentType()) {
                writeShort(1);
                writeHandle((SchemaComponent) type.getContainerField());
            } else if (type.getContainerField().isAttribute()) {
                writeShort(2);
                writeShort(((SchemaTypeImpl) type.getOuterType()).getIndexForLocalAttribute((SchemaLocalAttribute) type.getContainerField()));
            } else {
                writeShort(3);
                writeShort(((SchemaTypeImpl) type.getOuterType()).getIndexForLocalElement((SchemaLocalElement) type.getContainerField()));
            }
            writeString(type.getFullJavaName());
            writeString(type.getFullJavaImplName());
            writeTypeArray(type.getAnonymousTypes());
            writeShort(type.getAnonymousUnionMemberOrdinal());
            int flags = 0;
            if (type.isSimpleType()) {
                flags = 0 | 1;
            }
            if (type.isDocumentType()) {
                flags |= 2;
            }
            if (type.isAttributeType()) {
                flags |= 524288;
            }
            if (type.ordered() != 0) {
                flags |= 4;
            }
            if (type.ordered() == 2) {
                flags |= 1024;
            }
            if (type.isBounded()) {
                flags |= 8;
            }
            if (type.isFinite()) {
                flags |= 16;
            }
            if (type.isNumeric()) {
                flags |= 32;
            }
            if (type.hasStringEnumValues()) {
                flags |= 64;
            }
            if (((SchemaTypeImpl) type).isUnionOfLists()) {
                flags |= 128;
            }
            if (type.hasPatternFacet()) {
                flags |= 256;
            }
            if (type.isOrderSensitive()) {
                flags |= 512;
            }
            if (type.blockExtension()) {
                flags |= 4096;
            }
            if (type.blockRestriction()) {
                flags |= 8192;
            }
            if (type.finalExtension()) {
                flags |= 16384;
            }
            if (type.finalRestriction()) {
                flags |= 16384;
            }
            if (type.finalList()) {
                flags |= 131072;
            }
            if (type.finalUnion()) {
                flags |= 65536;
            }
            if (type.isAbstract()) {
                flags |= 262144;
            }
            writeInt(flags);
            if (!type.isSimpleType()) {
                writeShort(type.getContentType());
                writeType(type.getContentBasedOnType());
                SchemaAttributeModel attrModel = type.getAttributeModel();
                SchemaLocalAttribute[] attrs = attrModel.getAttributes();
                writeShort(attrs.length);
                for (SchemaLocalAttribute schemaLocalAttribute : attrs) {
                    writeAttributeData(schemaLocalAttribute);
                }
                writeQNameSet(attrModel.getWildcardSet());
                writeShort(attrModel.getWildcardProcess());
                SchemaProperty[] attrProperties = type.getAttributeProperties();
                writeShort(attrProperties.length);
                for (SchemaProperty schemaProperty : attrProperties) {
                    writePropertyData(schemaProperty);
                }
                if (type.getContentType() == 3 || type.getContentType() == 4) {
                    writeShort(type.hasAllContent() ? 1 : 0);
                    if (type.getContentModel() != null) {
                        parts = new SchemaParticle[]{type.getContentModel()};
                    } else {
                        parts = new SchemaParticle[0];
                    }
                    writeParticleArray(parts);
                    SchemaProperty[] eltProperties = type.getElementProperties();
                    writeShort(eltProperties.length);
                    for (SchemaProperty schemaProperty2 : eltProperties) {
                        writePropertyData(schemaProperty2);
                    }
                }
            }
            if (type.isSimpleType() || type.getContentType() == 2) {
                writeShort(type.getSimpleVariety());
                int facetCount = 0;
                for (int i = 0; i <= 11; i++) {
                    if (type.getFacet(i) != null) {
                        facetCount++;
                    }
                }
                writeShort(facetCount);
                for (int i2 = 0; i2 <= 11; i2++) {
                    XmlAnySimpleType facet = type.getFacet(i2);
                    if (facet != null) {
                        writeShort(i2);
                        writeXmlValueObject(facet);
                        writeShort(type.isFacetFixed(i2) ? 1 : 0);
                    }
                }
                writeShort(type.getWhiteSpaceRule());
                RegularExpression[] patterns = ((SchemaTypeImpl) type).getPatternExpressions();
                writeShort(patterns.length);
                for (RegularExpression regularExpression : patterns) {
                    writeString(regularExpression.getPattern());
                }
                XmlAnySimpleType[] enumValues = type.getEnumerationValues();
                if (enumValues == null) {
                    writeShort(0);
                } else {
                    writeShort(enumValues.length);
                    for (XmlAnySimpleType xmlAnySimpleType : enumValues) {
                        writeXmlValueObject(xmlAnySimpleType);
                    }
                }
                writeType(type.getBaseEnumType());
                if (type.hasStringEnumValues()) {
                    SchemaStringEnumEntry[] entries = type.getStringEnumEntries();
                    writeShort(entries.length);
                    for (int i3 = 0; i3 < entries.length; i3++) {
                        writeString(entries[i3].getString());
                        writeShort(entries[i3].getIntValue());
                        writeString(entries[i3].getEnumName());
                    }
                }
                switch (type.getSimpleVariety()) {
                    case 1:
                        writeType(type.getPrimitiveType());
                        writeInt(type.getDecimalSize());
                        break;
                    case 2:
                        writeTypeArray(type.getUnionMemberTypes());
                        break;
                    case 3:
                        writeType(type.getListItemType());
                        break;
                }
            }
            writeString(type.getSourceName());
        }

        void readExtensionsList() {
            int count = readShort();
            if (!$assertionsDisabled && count != 0) {
                throw new AssertionError();
            }
            for (int i = 0; i < count; i++) {
                readString();
                readString();
                readString();
            }
        }

        SchemaLocalAttribute readAttributeData() {
            SchemaLocalAttributeImpl result = new SchemaLocalAttributeImpl();
            loadAttribute(result, readQName(), null);
            return result;
        }

        void loadAttribute(SchemaLocalAttributeImpl result, QName name, SchemaContainer container) {
            result.init(name, readTypeRef(), readShort(), readString(), null, atLeast(2, 16, 0) ? readXmlValueObject() : null, readShort() == 1, readSOAPArrayType(), readAnnotation(container), null);
        }

        void writeAttributeData(SchemaLocalAttribute attr) throws IOException {
            writeQName(attr.getName());
            writeType(attr.getType());
            writeShort(attr.getUse());
            writeString(attr.getDefaultText());
            writeXmlValueObject(attr.getDefaultValue());
            writeShort(attr.isFixed() ? 1 : 0);
            writeSOAPArrayType(((SchemaWSDLArrayType) attr).getWSDLArrayType());
            writeAnnotation(attr.getAnnotation());
        }

        void writeIdConstraintData(SchemaIdentityConstraint idc) throws IOException {
            writeQName(idc.getName());
            writeShort(idc.getConstraintCategory());
            writeString(idc.getSelector());
            writeAnnotation(idc.getAnnotation());
            String[] fields = idc.getFields();
            writeShort(fields.length);
            for (String str : fields) {
                writeString(str);
            }
            if (idc.getConstraintCategory() == 2) {
                writeHandle(idc.getReferencedKey());
            }
            Set<Map.Entry> mappings = idc.getNSMap().entrySet();
            writeShort(mappings.size());
            for (Map.Entry e : mappings) {
                String prefix = (String) e.getKey();
                String uri = (String) e.getValue();
                writeString(prefix);
                writeString(uri);
            }
            writeString(idc.getSourceName());
        }

        SchemaParticle[] readParticleArray() {
            SchemaParticle[] result = new SchemaParticle[readShort()];
            for (int i = 0; i < result.length; i++) {
                result[i] = readParticleData();
            }
            return result;
        }

        void writeParticleArray(SchemaParticle[] spa) throws IOException {
            writeShort(spa.length);
            for (SchemaParticle schemaParticle : spa) {
                writeParticleData(schemaParticle);
            }
        }

        SchemaParticle readParticleData() {
            SchemaParticleImpl result;
            int particleType = readShort();
            if (particleType != 4) {
                result = new SchemaParticleImpl();
            } else {
                result = new SchemaLocalElementImpl();
            }
            loadParticle(result, particleType);
            return result;
        }

        void loadParticle(SchemaParticleImpl result, int particleType) {
            int particleFlags = readShort();
            result.setParticleType(particleType);
            result.setMinOccurs(readBigInteger());
            result.setMaxOccurs(readBigInteger());
            result.setTransitionRules(readQNameSet(), (particleFlags & 1) != 0);
            switch (particleType) {
                case 1:
                case 2:
                case 3:
                    result.setParticleChildren(readParticleArray());
                    return;
                case 4:
                    SchemaLocalElementImpl lresult = (SchemaLocalElementImpl) result;
                    lresult.setNameAndTypeRef(readQName(), readTypeRef());
                    lresult.setDefault(readString(), (particleFlags & 4) != 0, null);
                    if (atLeast(2, 16, 0)) {
                        lresult.setDefaultValue(readXmlValueObject());
                    }
                    lresult.setNillable((particleFlags & 8) != 0);
                    lresult.setBlock((particleFlags & 16) != 0, (particleFlags & 32) != 0, (particleFlags & 64) != 0);
                    lresult.setWsdlArrayType(readSOAPArrayType());
                    lresult.setAbstract((particleFlags & 128) != 0);
                    lresult.setAnnotation(readAnnotation(null));
                    SchemaIdentityConstraint.Ref[] idcs = new SchemaIdentityConstraint.Ref[readShort()];
                    for (int i = 0; i < idcs.length; i++) {
                        idcs[i] = (SchemaIdentityConstraint.Ref) readHandle();
                    }
                    lresult.setIdentityConstraints(idcs);
                    return;
                case 5:
                    result.setWildcardSet(readQNameSet());
                    result.setWildcardProcess(readShort());
                    return;
                default:
                    throw new SchemaTypeLoaderException("Unrecognized particle type ", SchemaTypeSystemImpl.this._name, this._handle, 11);
            }
        }

        void writeParticleData(SchemaParticle part) throws IOException {
            writeShort(part.getParticleType());
            short flags = 0;
            if (part.isSkippable()) {
                flags = (short) (0 | 1);
            }
            if (part.getParticleType() == 4) {
                SchemaLocalElement lpart = (SchemaLocalElement) part;
                if (lpart.isFixed()) {
                    flags = (short) (flags | 4);
                }
                if (lpart.isNillable()) {
                    flags = (short) (flags | 8);
                }
                if (lpart.blockExtension()) {
                    flags = (short) (flags | 16);
                }
                if (lpart.blockRestriction()) {
                    flags = (short) (flags | 32);
                }
                if (lpart.blockSubstitution()) {
                    flags = (short) (flags | 64);
                }
                if (lpart.isAbstract()) {
                    flags = (short) (flags | 128);
                }
                if (lpart instanceof SchemaGlobalElement) {
                    SchemaGlobalElement gpart = (SchemaGlobalElement) lpart;
                    if (gpart.finalExtension()) {
                        flags = (short) (flags | 256);
                    }
                    if (gpart.finalRestriction()) {
                        flags = (short) (flags | 512);
                    }
                }
            }
            writeShort(flags);
            writeBigInteger(part.getMinOccurs());
            writeBigInteger(part.getMaxOccurs());
            writeQNameSet(part.acceptedStartNames());
            switch (part.getParticleType()) {
                case 1:
                case 2:
                case 3:
                    writeParticleArray(part.getParticleChildren());
                    return;
                case 4:
                    SchemaLocalElement lpart2 = (SchemaLocalElement) part;
                    writeQName(lpart2.getName());
                    writeType(lpart2.getType());
                    writeString(lpart2.getDefaultText());
                    writeXmlValueObject(lpart2.getDefaultValue());
                    writeSOAPArrayType(((SchemaWSDLArrayType) lpart2).getWSDLArrayType());
                    writeAnnotation(lpart2.getAnnotation());
                    if (lpart2 instanceof SchemaGlobalElement) {
                        SchemaGlobalElement gpart2 = (SchemaGlobalElement) lpart2;
                        writeHandle(gpart2.substitutionGroup());
                        QName[] substGroupMembers = gpart2.substitutionGroupMembers();
                        writeShort(substGroupMembers.length);
                        for (QName qName : substGroupMembers) {
                            writeQName(qName);
                        }
                    }
                    SchemaIdentityConstraint[] idcs = lpart2.getIdentityConstraints();
                    writeShort(idcs.length);
                    for (SchemaIdentityConstraint schemaIdentityConstraint : idcs) {
                        writeHandle(schemaIdentityConstraint);
                    }
                    return;
                case 5:
                    writeQNameSet(part.getWildcardSet());
                    writeShort(part.getWildcardProcess());
                    return;
                default:
                    throw new SchemaTypeLoaderException("Unrecognized particle type ", SchemaTypeSystemImpl.this._name, this._handle, 11);
            }
        }

        SchemaProperty readPropertyData() {
            SchemaPropertyImpl prop = new SchemaPropertyImpl();
            prop.setName(readQName());
            prop.setTypeRef(readTypeRef());
            int propflags = readShort();
            prop.setAttribute((propflags & 1) != 0);
            prop.setContainerTypeRef(readTypeRef());
            prop.setMinOccurs(readBigInteger());
            prop.setMaxOccurs(readBigInteger());
            prop.setNillable(readShort());
            prop.setDefault(readShort());
            prop.setFixed(readShort());
            prop.setDefaultText(readString());
            prop.setJavaPropertyName(readString());
            prop.setJavaTypeCode(readShort());
            prop.setExtendsJava(readTypeRef(), (propflags & 2) != 0, (propflags & 4) != 0, (propflags & 8) != 0);
            if (atMost(2, 19, 0)) {
                prop.setJavaSetterDelimiter(readQNameSet());
            }
            if (atLeast(2, 16, 0)) {
                prop.setDefaultValue(readXmlValueObject());
            }
            if (!prop.isAttribute() && atLeast(2, 17, 0)) {
                int size = readShort();
                LinkedHashSet qnames = new LinkedHashSet(size);
                for (int i = 0; i < size; i++) {
                    qnames.add(readQName());
                }
                prop.setAcceptedNames(qnames);
            }
            prop.setImmutable();
            return prop;
        }

        void writePropertyData(SchemaProperty prop) throws IOException {
            writeQName(prop.getName());
            writeType(prop.getType());
            writeShort((prop.isAttribute() ? 1 : 0) | (prop.extendsJavaSingleton() ? 2 : 0) | (prop.extendsJavaOption() ? 4 : 0) | (prop.extendsJavaArray() ? 8 : 0));
            writeType(prop.getContainerType());
            writeBigInteger(prop.getMinOccurs());
            writeBigInteger(prop.getMaxOccurs());
            writeShort(prop.hasNillable());
            writeShort(prop.hasDefault());
            writeShort(prop.hasFixed());
            writeString(prop.getDefaultText());
            writeString(prop.getJavaPropertyName());
            writeShort(prop.getJavaTypeCode());
            writeType(prop.javaBasedOnType());
            writeXmlValueObject(prop.getDefaultValue());
            if (!prop.isAttribute()) {
                QName[] names = prop.acceptedNames();
                writeShort(names.length);
                for (QName qName : names) {
                    writeQName(qName);
                }
            }
        }

        void writeModelGroupData(SchemaModelGroup grp) throws IOException {
            SchemaModelGroupImpl impl = (SchemaModelGroupImpl) grp;
            writeQName(impl.getName());
            writeString(impl.getTargetNamespace());
            writeShort(impl.getChameleonNamespace() != null ? 1 : 0);
            writeString(impl.getElemFormDefault());
            writeString(impl.getAttFormDefault());
            writeShort(impl.isRedefinition() ? 1 : 0);
            writeString(impl.getParseObject().xmlText(new XmlOptions().setSaveOuter()));
            writeAnnotation(impl.getAnnotation());
            writeString(impl.getSourceName());
        }

        void writeAttributeGroupData(SchemaAttributeGroup grp) throws IOException {
            SchemaAttributeGroupImpl impl = (SchemaAttributeGroupImpl) grp;
            writeQName(impl.getName());
            writeString(impl.getTargetNamespace());
            writeShort(impl.getChameleonNamespace() != null ? 1 : 0);
            writeString(impl.getFormDefault());
            writeShort(impl.isRedefinition() ? 1 : 0);
            writeString(impl.getParseObject().xmlText(new XmlOptions().setSaveOuter()));
            writeAnnotation(impl.getAnnotation());
            writeString(impl.getSourceName());
        }

        XmlValueRef readXmlValueObject() throws IOException {
            SchemaType.Ref typeref = readTypeRef();
            if (typeref == null) {
                return null;
            }
            int btc = readShort();
            switch (btc) {
                case 0:
                    break;
                case 2:
                case 3:
                case 6:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                    return new XmlValueRef(typeref, readString());
                case 4:
                case 5:
                    return new XmlValueRef(typeref, readByteArray());
                case 7:
                case 8:
                    return new XmlValueRef(typeref, readQName());
                case 9:
                case 10:
                    return new XmlValueRef(typeref, new Double(readDouble()));
                case 65535:
                    int size = readShort();
                    List values = new ArrayList();
                    writeShort(values.size());
                    for (int i = 0; i < size; i++) {
                        values.add(readXmlValueObject());
                    }
                    return new XmlValueRef(typeref, values);
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            return new XmlValueRef(typeref, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        void writeXmlValueObject(XmlAnySimpleType xmlAnySimpleType) throws IOException {
            SchemaType type = xmlAnySimpleType == 0 ? null : xmlAnySimpleType.schemaType();
            writeType(type);
            if (type == null) {
            }
            SchemaType iType = ((SimpleValue) xmlAnySimpleType).instanceType();
            if (iType == null) {
                writeShort(0);
                return;
            }
            if (iType.getSimpleVariety() == 3) {
                writeShort(-1);
                List values = ((XmlObjectBase) xmlAnySimpleType).xgetListValue();
                writeShort(values.size());
                Iterator i = values.iterator();
                while (i.hasNext()) {
                    writeXmlValueObject((XmlAnySimpleType) i.next());
                }
                return;
            }
            int btc = iType.getPrimitiveType().getBuiltinTypeCode();
            writeShort(btc);
            switch (btc) {
                case 2:
                case 3:
                case 6:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                    writeString(xmlAnySimpleType.getStringValue());
                    break;
                case 4:
                case 5:
                    writeByteArray(((SimpleValue) xmlAnySimpleType).getByteArrayValue());
                    break;
                case 7:
                case 8:
                    writeQName(((SimpleValue) xmlAnySimpleType).getQNameValue());
                    break;
                case 9:
                    writeDouble(((SimpleValue) xmlAnySimpleType).getFloatValue());
                    break;
                case 10:
                    writeDouble(((SimpleValue) xmlAnySimpleType).getDoubleValue());
                    break;
            }
        }

        double readDouble() {
            try {
                return this._input.readDouble();
            } catch (IOException e) {
                throw new SchemaTypeLoaderException(e.getMessage(), SchemaTypeSystemImpl.this._name, this._handle, 9);
            }
        }

        void writeDouble(double d) throws IOException {
            if (this._output != null) {
                try {
                    this._output.writeDouble(d);
                } catch (IOException e) {
                    throw new SchemaTypeLoaderException(e.getMessage(), SchemaTypeSystemImpl.this._name, this._handle, 9);
                }
            }
        }

        QNameSet readQNameSet() {
            int flag = readShort();
            Set uriSet = new HashSet();
            int uriCount = readShort();
            for (int i = 0; i < uriCount; i++) {
                uriSet.add(readString());
            }
            Set qnameSet1 = new HashSet();
            int qncount1 = readShort();
            for (int i2 = 0; i2 < qncount1; i2++) {
                qnameSet1.add(readQName());
            }
            Set qnameSet2 = new HashSet();
            int qncount2 = readShort();
            for (int i3 = 0; i3 < qncount2; i3++) {
                qnameSet2.add(readQName());
            }
            if (flag == 1) {
                return QNameSet.forSets(uriSet, null, qnameSet1, qnameSet2);
            }
            return QNameSet.forSets(null, uriSet, qnameSet2, qnameSet1);
        }

        void writeQNameSet(QNameSet set) throws IOException {
            boolean invert = set.excludedURIs() != null;
            writeShort(invert ? 1 : 0);
            Set uriSet = invert ? set.excludedURIs() : set.includedURIs();
            writeShort(uriSet.size());
            Iterator i = uriSet.iterator();
            while (i.hasNext()) {
                writeString((String) i.next());
            }
            Set qnameSet1 = invert ? set.excludedQNamesInIncludedURIs() : set.includedQNamesInExcludedURIs();
            writeShort(qnameSet1.size());
            Iterator i2 = qnameSet1.iterator();
            while (i2.hasNext()) {
                writeQName((QName) i2.next());
            }
            Set qnameSet2 = invert ? set.includedQNamesInExcludedURIs() : set.excludedQNamesInIncludedURIs();
            writeShort(qnameSet2.size());
            Iterator i3 = qnameSet2.iterator();
            while (i3.hasNext()) {
                writeQName((QName) i3.next());
            }
        }

        byte[] readByteArray() throws IOException {
            try {
                int len = this._input.readShort();
                byte[] result = new byte[len];
                this._input.readFully(result);
                return result;
            } catch (IOException e) {
                throw new SchemaTypeLoaderException(e.getMessage(), SchemaTypeSystemImpl.this._name, this._handle, 9);
            }
        }

        void writeByteArray(byte[] ba) {
            try {
                writeShort(ba.length);
                if (this._output != null) {
                    this._output.write(ba);
                }
            } catch (IOException e) {
                throw new SchemaTypeLoaderException(e.getMessage(), SchemaTypeSystemImpl.this._name, this._handle, 9);
            }
        }

        BigInteger readBigInteger() throws IOException {
            byte[] result = readByteArray();
            if (result.length == 0) {
                return null;
            }
            if (result.length == 1 && result[0] == 0) {
                return BigInteger.ZERO;
            }
            if (result.length == 1 && result[0] == 1) {
                return BigInteger.ONE;
            }
            return new BigInteger(result);
        }

        void writeBigInteger(BigInteger bi) throws IOException {
            if (bi == null) {
                writeShort(0);
            } else if (bi.signum() == 0) {
                writeByteArray(SchemaTypeSystemImpl.SINGLE_ZERO_BYTE);
            } else {
                writeByteArray(bi.toByteArray());
            }
        }
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType typeForHandle(String handle) {
        SchemaType schemaType;
        synchronized (this._resolvedHandles) {
            schemaType = (SchemaType) this._resolvedHandles.get(handle);
        }
        return schemaType;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType typeForClassname(String classname) {
        SchemaType.Ref ref = (SchemaType.Ref) this._typeRefsByClassname.get(classname);
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaComponent resolveHandle(String handle) {
        SchemaComponent result;
        synchronized (this._resolvedHandles) {
            result = (SchemaComponent) this._resolvedHandles.get(handle);
        }
        if (result == null) {
            XsbReader reader = new XsbReader(handle, 65535);
            int filetype = reader.getActualFiletype();
            switch (filetype) {
                case 2:
                    XBeanDebug.trace(1, "Resolving type for handle " + handle, 0);
                    result = reader.finishLoadingType();
                    break;
                case 3:
                    XBeanDebug.trace(1, "Resolving element for handle " + handle, 0);
                    result = reader.finishLoadingElement();
                    break;
                case 4:
                    XBeanDebug.trace(1, "Resolving attribute for handle " + handle, 0);
                    result = reader.finishLoadingAttribute();
                    break;
                case 5:
                default:
                    throw new IllegalStateException("Illegal handle type");
                case 6:
                    XBeanDebug.trace(1, "Resolving model group for handle " + handle, 0);
                    result = reader.finishLoadingModelGroup();
                    break;
                case 7:
                    XBeanDebug.trace(1, "Resolving attribute group for handle " + handle, 0);
                    result = reader.finishLoadingAttributeGroup();
                    break;
                case 8:
                    XBeanDebug.trace(1, "Resolving id constraint for handle " + handle, 0);
                    result = reader.finishLoadingIdentityConstraint();
                    break;
            }
            synchronized (this._resolvedHandles) {
                if (!this._resolvedHandles.containsKey(handle)) {
                    this._resolvedHandles.put(handle, result);
                } else {
                    result = (SchemaComponent) this._resolvedHandles.get(handle);
                }
            }
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public void resolve() {
        XBeanDebug.trace(1, "Resolve called type system " + this._name, 0);
        if (this._allNonGroupHandlesResolved) {
            return;
        }
        XBeanDebug.trace(1, "Resolving all handles for type system " + this._name, 1);
        List<SchemaComponent.Ref> refs = new ArrayList();
        refs.addAll(this._globalElements.values());
        refs.addAll(this._globalAttributes.values());
        refs.addAll(this._globalTypes.values());
        refs.addAll(this._documentTypes.values());
        refs.addAll(this._attributeTypes.values());
        refs.addAll(this._identityConstraints.values());
        for (SchemaComponent.Ref ref : refs) {
            ref.getComponent();
        }
        XBeanDebug.trace(1, "Finished resolving type system " + this._name, -1);
        this._allNonGroupHandlesResolved = true;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public boolean isNamespaceDefined(String namespace) {
        return this._namespaces.contains(namespace);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findTypeRef(QName name) {
        return (SchemaType.Ref) this._globalTypes.get(name);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findDocumentTypeRef(QName name) {
        return (SchemaType.Ref) this._documentTypes.get(name);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaType.Ref findAttributeTypeRef(QName name) {
        return (SchemaType.Ref) this._attributeTypes.get(name);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalElement.Ref findElementRef(QName name) {
        return (SchemaGlobalElement.Ref) this._globalElements.get(name);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaGlobalAttribute.Ref findAttributeRef(QName name) {
        return (SchemaGlobalAttribute.Ref) this._globalAttributes.get(name);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaModelGroup.Ref findModelGroupRef(QName name) {
        return (SchemaModelGroup.Ref) this._modelGroups.get(name);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaAttributeGroup.Ref findAttributeGroupRef(QName name) {
        return (SchemaAttributeGroup.Ref) this._attributeGroups.get(name);
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public SchemaIdentityConstraint.Ref findIdentityConstraintRef(QName name) {
        return (SchemaIdentityConstraint.Ref) this._identityConstraints.get(name);
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType[] globalTypes() {
        if (this._globalTypes.isEmpty()) {
            return EMPTY_ST_ARRAY;
        }
        SchemaType[] result = new SchemaType[this._globalTypes.size()];
        int j = 0;
        Iterator i = this._globalTypes.values().iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaType.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    public SchemaType[] redefinedGlobalTypes() {
        if (this._redefinedGlobalTypes == null || this._redefinedGlobalTypes.isEmpty()) {
            return EMPTY_ST_ARRAY;
        }
        SchemaType[] result = new SchemaType[this._redefinedGlobalTypes.size()];
        int j = 0;
        Iterator i = this._redefinedGlobalTypes.iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaType.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeLoader
    public InputStream getSourceAsStream(String sourceName) {
        if (!sourceName.startsWith("/")) {
            sourceName = "/" + sourceName;
        }
        return this._resourceLoader.getResourceAsStream(getMetadataPath() + "/src" + sourceName);
    }

    SchemaContainer[] containers() {
        SchemaContainer[] result = new SchemaContainer[this._containers.size()];
        int j = 0;
        Iterator i = this._containers.values().iterator();
        while (i.hasNext()) {
            result[j] = (SchemaContainer) i.next();
            j++;
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType[] documentTypes() {
        if (this._documentTypes.isEmpty()) {
            return EMPTY_ST_ARRAY;
        }
        SchemaType[] result = new SchemaType[this._documentTypes.size()];
        int j = 0;
        Iterator i = this._documentTypes.values().iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaType.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaType[] attributeTypes() {
        if (this._attributeTypes.isEmpty()) {
            return EMPTY_ST_ARRAY;
        }
        SchemaType[] result = new SchemaType[this._attributeTypes.size()];
        int j = 0;
        Iterator i = this._attributeTypes.values().iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaType.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaGlobalElement[] globalElements() {
        if (this._globalElements.isEmpty()) {
            return EMPTY_GE_ARRAY;
        }
        SchemaGlobalElement[] result = new SchemaGlobalElement[this._globalElements.size()];
        int j = 0;
        Iterator i = this._globalElements.values().iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaGlobalElement.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaGlobalAttribute[] globalAttributes() {
        if (this._globalAttributes.isEmpty()) {
            return EMPTY_GA_ARRAY;
        }
        SchemaGlobalAttribute[] result = new SchemaGlobalAttribute[this._globalAttributes.size()];
        int j = 0;
        Iterator i = this._globalAttributes.values().iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaGlobalAttribute.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaModelGroup[] modelGroups() {
        if (this._modelGroups.isEmpty()) {
            return EMPTY_MG_ARRAY;
        }
        SchemaModelGroup[] result = new SchemaModelGroup[this._modelGroups.size()];
        int j = 0;
        Iterator i = this._modelGroups.values().iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaModelGroup.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    public SchemaModelGroup[] redefinedModelGroups() {
        if (this._redefinedModelGroups == null || this._redefinedModelGroups.isEmpty()) {
            return EMPTY_MG_ARRAY;
        }
        SchemaModelGroup[] result = new SchemaModelGroup[this._redefinedModelGroups.size()];
        int j = 0;
        Iterator i = this._redefinedModelGroups.iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaModelGroup.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaAttributeGroup[] attributeGroups() {
        if (this._attributeGroups.isEmpty()) {
            return EMPTY_AG_ARRAY;
        }
        SchemaAttributeGroup[] result = new SchemaAttributeGroup[this._attributeGroups.size()];
        int j = 0;
        Iterator i = this._attributeGroups.values().iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaAttributeGroup.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    public SchemaAttributeGroup[] redefinedAttributeGroups() {
        if (this._redefinedAttributeGroups == null || this._redefinedAttributeGroups.isEmpty()) {
            return EMPTY_AG_ARRAY;
        }
        SchemaAttributeGroup[] result = new SchemaAttributeGroup[this._redefinedAttributeGroups.size()];
        int j = 0;
        Iterator i = this._redefinedAttributeGroups.iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaAttributeGroup.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public SchemaAnnotation[] annotations() {
        if (this._annotations == null || this._annotations.isEmpty()) {
            return EMPTY_ANN_ARRAY;
        }
        SchemaAnnotation[] result = new SchemaAnnotation[this._annotations.size()];
        return (SchemaAnnotation[]) this._annotations.toArray(result);
    }

    public SchemaIdentityConstraint[] identityConstraints() {
        if (this._identityConstraints.isEmpty()) {
            return EMPTY_IC_ARRAY;
        }
        SchemaIdentityConstraint[] result = new SchemaIdentityConstraint[this._identityConstraints.size()];
        int j = 0;
        Iterator i = this._identityConstraints.values().iterator();
        while (i.hasNext()) {
            result[j] = ((SchemaIdentityConstraint.Ref) i.next()).get();
            j++;
        }
        return result;
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public ClassLoader getClassLoader() {
        return this._classloader;
    }

    public String handleForType(SchemaType type) {
        return this._localHandles.handleForType(type);
    }

    @Override // org.apache.xmlbeans.SchemaTypeSystem
    public String getName() {
        return this._name;
    }

    public SchemaTypeSystem typeSystemForName(String name) {
        if (this._name != null && name.equals(this._name)) {
            return this;
        }
        return null;
    }

    protected String getMetadataPath() {
        return "schema" + METADATA_PACKAGE_GEN;
    }
}
