package org.apache.ibatis.javassist.util.proxy;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.bytecode.Bytecode;
import org.apache.ibatis.javassist.bytecode.ClassFile;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.Descriptor;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.apache.ibatis.javassist.bytecode.ExceptionsAttribute;
import org.apache.ibatis.javassist.bytecode.FieldInfo;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.ibatis.javassist.bytecode.StackMapTable;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/ProxyFactory.class */
public class ProxyFactory {
    private String classname;
    private String basename;
    private String superName;
    private static final String HOLDER = "_methods_";
    private static final String HOLDER_TYPE = "[Ljava/lang/reflect/Method;";
    private static final String FILTER_SIGNATURE_FIELD = "_filter_signature";
    private static final String FILTER_SIGNATURE_TYPE = "[B";
    private static final String HANDLER = "handler";
    private static final String NULL_INTERCEPTOR_HOLDER = "org.apache.ibatis.javassist.util.proxy.RuntimeSupport";
    private static final String DEFAULT_INTERCEPTOR = "default_interceptor";
    private static final String HANDLER_SETTER = "setHandler";
    private static final String HANDLER_GETTER = "getHandler";
    private static final String SERIAL_VERSION_UID_FIELD = "serialVersionUID";
    private static final String SERIAL_VERSION_UID_TYPE = "J";
    private static final long SERIAL_VERSION_UID_VALUE = -1;
    private static final String HANDLER_GETTER_KEY = "getHandler:()";
    public static boolean onlyPublicMethods = false;
    private static final Class OBJECT_TYPE = Object.class;
    private static final String HANDLER_TYPE = 'L' + MethodHandler.class.getName().replace('.', '/') + ';';
    private static final String HANDLER_SETTER_TYPE = "(" + HANDLER_TYPE + ")V";
    private static final String HANDLER_GETTER_TYPE = "()" + HANDLER_TYPE;
    public static volatile boolean useCache = true;
    public static volatile boolean useWriteReplace = true;
    private static WeakHashMap proxyCache = new WeakHashMap();
    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static ClassLoaderProvider classLoaderProvider = new ClassLoaderProvider() { // from class: org.apache.ibatis.javassist.util.proxy.ProxyFactory.1
        @Override // org.apache.ibatis.javassist.util.proxy.ProxyFactory.ClassLoaderProvider
        public ClassLoader get(ProxyFactory pf) {
            return pf.getClassLoader0();
        }
    };
    public static UniqueName nameGenerator = new UniqueName() { // from class: org.apache.ibatis.javassist.util.proxy.ProxyFactory.2
        private final String sep = "_$$_jvst" + Integer.toHexString(hashCode() & 4095) + "_";
        private int counter = 0;

        @Override // org.apache.ibatis.javassist.util.proxy.ProxyFactory.UniqueName
        public String get(String classname) {
            StringBuilder sbAppend = new StringBuilder().append(classname).append(this.sep);
            int i = this.counter;
            this.counter = i + 1;
            return sbAppend.append(Integer.toHexString(i)).toString();
        }
    };
    private static Comparator sorter = new Comparator() { // from class: org.apache.ibatis.javassist.util.proxy.ProxyFactory.3
        @Override // java.util.Comparator
        public int compare(Object o1, Object o2) {
            Map.Entry e1 = (Map.Entry) o1;
            Map.Entry e2 = (Map.Entry) o2;
            String key1 = (String) e1.getKey();
            String key2 = (String) e2.getKey();
            return key1.compareTo(key2);
        }
    };
    private Class superClass = null;
    private Class[] interfaces = null;
    private MethodFilter methodFilter = null;
    private MethodHandler handler = null;
    private byte[] signature = null;
    private List signatureMethods = null;
    private boolean hasGetHandler = false;
    private Class thisClass = null;
    public String writeDirectory = null;
    private boolean factoryUseCache = useCache;
    private boolean factoryWriteReplace = useWriteReplace;

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/ProxyFactory$ClassLoaderProvider.class */
    public interface ClassLoaderProvider {
        ClassLoader get(ProxyFactory proxyFactory);
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/ProxyFactory$UniqueName.class */
    public interface UniqueName {
        String get(String str);
    }

    public boolean isUseCache() {
        return this.factoryUseCache;
    }

    public void setUseCache(boolean useCache2) {
        if (this.handler != null && useCache2) {
            throw new RuntimeException("caching cannot be enabled if the factory default interceptor has been set");
        }
        this.factoryUseCache = useCache2;
    }

    public boolean isUseWriteReplace() {
        return this.factoryWriteReplace;
    }

    public void setUseWriteReplace(boolean useWriteReplace2) {
        this.factoryWriteReplace = useWriteReplace2;
    }

    public static boolean isProxyClass(Class cl) {
        return Proxy.class.isAssignableFrom(cl);
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/ProxyFactory$ProxyDetails.class */
    static class ProxyDetails {
        byte[] signature;
        WeakReference proxyClass;
        boolean isUseWriteReplace;

        ProxyDetails(byte[] signature, Class proxyClass, boolean isUseWriteReplace) {
            this.signature = signature;
            this.proxyClass = new WeakReference(proxyClass);
            this.isUseWriteReplace = isUseWriteReplace;
        }
    }

    public void setSuperclass(Class clazz) {
        this.superClass = clazz;
        this.signature = null;
    }

    public Class getSuperclass() {
        return this.superClass;
    }

    public void setInterfaces(Class[] ifs) {
        this.interfaces = ifs;
        this.signature = null;
    }

    public Class[] getInterfaces() {
        return this.interfaces;
    }

    public void setFilter(MethodFilter mf) {
        this.methodFilter = mf;
        this.signature = null;
    }

    public Class createClass() {
        if (this.signature == null) {
            computeSignature(this.methodFilter);
        }
        return createClass1();
    }

    public Class createClass(MethodFilter filter) {
        computeSignature(filter);
        return createClass1();
    }

    Class createClass(byte[] signature) {
        installSignature(signature);
        return createClass1();
    }

    private Class createClass1() {
        Class result = this.thisClass;
        if (result == null) {
            ClassLoader cl = getClassLoader();
            synchronized (proxyCache) {
                if (this.factoryUseCache) {
                    createClass2(cl);
                } else {
                    createClass3(cl);
                }
                result = this.thisClass;
                this.thisClass = null;
            }
        }
        return result;
    }

    public String getKey(Class superClass, Class[] interfaces, byte[] signature, boolean useWriteReplace2) {
        StringBuffer sbuf = new StringBuffer();
        if (superClass != null) {
            sbuf.append(superClass.getName());
        }
        sbuf.append(":");
        for (Class cls : interfaces) {
            sbuf.append(cls.getName());
            sbuf.append(":");
        }
        for (byte b : signature) {
            int lo = b & 15;
            int hi = (b >> 4) & 15;
            sbuf.append(hexDigits[lo]);
            sbuf.append(hexDigits[hi]);
        }
        if (useWriteReplace2) {
            sbuf.append(":w");
        }
        return sbuf.toString();
    }

    private void createClass2(ClassLoader cl) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        String key = getKey(this.superClass, this.interfaces, this.signature, this.factoryWriteReplace);
        HashMap cacheForTheLoader = (HashMap) proxyCache.get(cl);
        if (cacheForTheLoader == null) {
            cacheForTheLoader = new HashMap();
            proxyCache.put(cl, cacheForTheLoader);
        }
        ProxyDetails details = (ProxyDetails) cacheForTheLoader.get(key);
        if (details != null) {
            WeakReference reference = details.proxyClass;
            this.thisClass = (Class) reference.get();
            if (this.thisClass != null) {
                return;
            }
        }
        createClass3(cl);
        cacheForTheLoader.put(key, new ProxyDetails(this.signature, this.thisClass, this.factoryWriteReplace));
    }

    private void createClass3(ClassLoader cl) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        allocateClassName();
        try {
            ClassFile cf = make();
            if (this.writeDirectory != null) {
                FactoryHelper.writeFile(cf, this.writeDirectory);
            }
            this.thisClass = FactoryHelper.toClass(cf, cl, getDomain());
            setField(FILTER_SIGNATURE_FIELD, this.signature);
            if (!this.factoryUseCache) {
                setField(DEFAULT_INTERCEPTOR, this.handler);
            }
        } catch (CannotCompileException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void setField(String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        if (this.thisClass != null && value != null) {
            try {
                Field f = this.thisClass.getField(fieldName);
                SecurityActions.setAccessible(f, true);
                f.set(null, value);
                SecurityActions.setAccessible(f, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    static byte[] getFilterSignature(Class clazz) {
        return (byte[]) getField(clazz, FILTER_SIGNATURE_FIELD);
    }

    private static Object getField(Class clazz, String fieldName) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        try {
            Field f = clazz.getField(fieldName);
            f.setAccessible(true);
            Object value = f.get(null);
            f.setAccessible(false);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandler getHandler(Proxy p) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        try {
            Field f = p.getClass().getDeclaredField(HANDLER);
            f.setAccessible(true);
            Object value = f.get(p);
            f.setAccessible(false);
            return (MethodHandler) value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ClassLoader getClassLoader() {
        return classLoaderProvider.get(this);
    }

    protected ClassLoader getClassLoader0() {
        ClassLoader loader = null;
        if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
            loader = this.superClass.getClassLoader();
        } else if (this.interfaces != null && this.interfaces.length > 0) {
            loader = this.interfaces[0].getClassLoader();
        }
        if (loader == null) {
            loader = getClass().getClassLoader();
            if (loader == null) {
                loader = Thread.currentThread().getContextClassLoader();
                if (loader == null) {
                    loader = ClassLoader.getSystemClassLoader();
                }
            }
        }
        return loader;
    }

    protected ProtectionDomain getDomain() {
        Class clazz;
        if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
            clazz = this.superClass;
        } else if (this.interfaces != null && this.interfaces.length > 0) {
            clazz = this.interfaces[0];
        } else {
            clazz = getClass();
        }
        return clazz.getProtectionDomain();
    }

    public Object create(Class[] paramTypes, Object[] args, MethodHandler mh) throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Object obj = create(paramTypes, args);
        ((Proxy) obj).setHandler(mh);
        return obj;
    }

    public Object create(Class[] paramTypes, Object[] args) throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Class c = createClass();
        Constructor cons = c.getConstructor(paramTypes);
        return cons.newInstance(args);
    }

    public void setHandler(MethodHandler mi) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        if (this.factoryUseCache && mi != null) {
            this.factoryUseCache = false;
            this.thisClass = null;
        }
        this.handler = mi;
        setField(DEFAULT_INTERCEPTOR, this.handler);
    }

    private static String makeProxyName(String classname) {
        String str;
        synchronized (nameGenerator) {
            str = nameGenerator.get(classname);
        }
        return str;
    }

    private ClassFile make() throws CannotCompileException {
        ClassFile cf = new ClassFile(false, this.classname, this.superName);
        cf.setAccessFlags(1);
        setInterfaces(cf, this.interfaces, this.hasGetHandler ? Proxy.class : ProxyObject.class);
        ConstPool pool = cf.getConstPool();
        if (!this.factoryUseCache) {
            FieldInfo finfo = new FieldInfo(pool, DEFAULT_INTERCEPTOR, HANDLER_TYPE);
            finfo.setAccessFlags(9);
            cf.addField(finfo);
        }
        FieldInfo finfo2 = new FieldInfo(pool, HANDLER, HANDLER_TYPE);
        finfo2.setAccessFlags(2);
        cf.addField(finfo2);
        FieldInfo finfo3 = new FieldInfo(pool, FILTER_SIGNATURE_FIELD, FILTER_SIGNATURE_TYPE);
        finfo3.setAccessFlags(9);
        cf.addField(finfo3);
        FieldInfo finfo4 = new FieldInfo(pool, "serialVersionUID", SERIAL_VERSION_UID_TYPE);
        finfo4.setAccessFlags(25);
        cf.addField(finfo4);
        makeConstructors(this.classname, cf, pool, this.classname);
        ArrayList forwarders = new ArrayList();
        int s = overrideMethods(cf, pool, this.classname, forwarders);
        addClassInitializer(cf, pool, this.classname, s, forwarders);
        addSetter(this.classname, cf, pool);
        if (!this.hasGetHandler) {
            addGetter(this.classname, cf, pool);
        }
        if (this.factoryWriteReplace) {
            try {
                cf.addMethod(makeWriteReplace(pool));
            } catch (DuplicateMemberException e) {
            }
        }
        this.thisClass = null;
        return cf;
    }

    private void checkClassAndSuperName() {
        if (this.interfaces == null) {
            this.interfaces = new Class[0];
        }
        if (this.superClass == null) {
            this.superClass = OBJECT_TYPE;
            this.superName = this.superClass.getName();
            this.basename = this.interfaces.length == 0 ? this.superName : this.interfaces[0].getName();
        } else {
            this.superName = this.superClass.getName();
            this.basename = this.superName;
        }
        if (Modifier.isFinal(this.superClass.getModifiers())) {
            throw new RuntimeException(this.superName + " is final");
        }
        if (this.basename.startsWith("java.") || onlyPublicMethods) {
            this.basename = "org.apache.ibatis.javassist.util.proxy." + this.basename.replace('.', '_');
        }
    }

    private void allocateClassName() {
        this.classname = makeProxyName(this.basename);
    }

    private void makeSortedMethodList() {
        checkClassAndSuperName();
        this.hasGetHandler = false;
        HashMap allMethods = getMethods(this.superClass, this.interfaces);
        this.signatureMethods = new ArrayList(allMethods.entrySet());
        Collections.sort(this.signatureMethods, sorter);
    }

    private void computeSignature(MethodFilter filter) {
        makeSortedMethodList();
        int l = this.signatureMethods.size();
        int maxBytes = (l + 7) >> 3;
        this.signature = new byte[maxBytes];
        for (int idx = 0; idx < l; idx++) {
            Map.Entry e = (Map.Entry) this.signatureMethods.get(idx);
            Method m = (Method) e.getValue();
            int mod = m.getModifiers();
            if (!Modifier.isFinal(mod) && !Modifier.isStatic(mod) && isVisible(mod, this.basename, m) && (filter == null || filter.isHandled(m))) {
                setBit(this.signature, idx);
            }
        }
    }

    private void installSignature(byte[] signature) {
        makeSortedMethodList();
        int l = this.signatureMethods.size();
        int maxBytes = (l + 7) >> 3;
        if (signature.length != maxBytes) {
            throw new RuntimeException("invalid filter signature length for deserialized proxy class");
        }
        this.signature = signature;
    }

    private boolean testBit(byte[] signature, int idx) {
        int byteIdx = idx >> 3;
        if (byteIdx > signature.length) {
            return false;
        }
        int bitIdx = idx & 7;
        int mask = 1 << bitIdx;
        return (signature[byteIdx] & mask) != 0;
    }

    private void setBit(byte[] signature, int idx) {
        int byteIdx = idx >> 3;
        if (byteIdx < signature.length) {
            int bitIdx = idx & 7;
            int mask = 1 << bitIdx;
            signature[byteIdx] = (byte) (signature[byteIdx] | mask);
        }
    }

    private static void setInterfaces(ClassFile cf, Class[] interfaces, Class proxyClass) {
        String[] list;
        String setterIntf = proxyClass.getName();
        if (interfaces == null || interfaces.length == 0) {
            list = new String[]{setterIntf};
        } else {
            list = new String[interfaces.length + 1];
            for (int i = 0; i < interfaces.length; i++) {
                list[i] = interfaces[i].getName();
            }
            list[interfaces.length] = setterIntf;
        }
        cf.setInterfaces(list);
    }

    private static void addClassInitializer(ClassFile cf, ConstPool cp, String classname, int size, ArrayList forwarders) throws CannotCompileException {
        FieldInfo finfo = new FieldInfo(cp, HOLDER, HOLDER_TYPE);
        finfo.setAccessFlags(10);
        cf.addField(finfo);
        MethodInfo minfo = new MethodInfo(cp, "<clinit>", "()V");
        minfo.setAccessFlags(8);
        setThrows(minfo, cp, new Class[]{ClassNotFoundException.class});
        Bytecode code = new Bytecode(cp, 0, 2);
        code.addIconst(size * 2);
        code.addAnewarray("java.lang.reflect.Method");
        code.addAstore(0);
        code.addLdc(classname);
        code.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
        code.addAstore(1);
        Iterator it = forwarders.iterator();
        while (it.hasNext()) {
            Find2MethodsArgs args = (Find2MethodsArgs) it.next();
            callFind2Methods(code, args.methodName, args.delegatorName, args.origIndex, args.descriptor, 1, 0);
        }
        code.addAload(0);
        code.addPutstatic(classname, HOLDER, HOLDER_TYPE);
        code.addLconst(-1L);
        code.addPutstatic(classname, "serialVersionUID", SERIAL_VERSION_UID_TYPE);
        code.addOpcode(177);
        minfo.setCodeAttribute(code.toCodeAttribute());
        cf.addMethod(minfo);
    }

    private static void callFind2Methods(Bytecode code, String superMethod, String thisMethod, int index, String desc, int classVar, int arrayVar) {
        String findClass = RuntimeSupport.class.getName();
        code.addAload(classVar);
        code.addLdc(superMethod);
        if (thisMethod == null) {
            code.addOpcode(1);
        } else {
            code.addLdc(thisMethod);
        }
        code.addIconst(index);
        code.addLdc(desc);
        code.addAload(arrayVar);
        code.addInvokestatic(findClass, "find2Methods", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;[Ljava/lang/reflect/Method;)V");
    }

    private static void addSetter(String classname, ClassFile cf, ConstPool cp) throws CannotCompileException {
        MethodInfo minfo = new MethodInfo(cp, HANDLER_SETTER, HANDLER_SETTER_TYPE);
        minfo.setAccessFlags(1);
        Bytecode code = new Bytecode(cp, 2, 2);
        code.addAload(0);
        code.addAload(1);
        code.addPutfield(classname, HANDLER, HANDLER_TYPE);
        code.addOpcode(177);
        minfo.setCodeAttribute(code.toCodeAttribute());
        cf.addMethod(minfo);
    }

    private static void addGetter(String classname, ClassFile cf, ConstPool cp) throws CannotCompileException {
        MethodInfo minfo = new MethodInfo(cp, HANDLER_GETTER, HANDLER_GETTER_TYPE);
        minfo.setAccessFlags(1);
        Bytecode code = new Bytecode(cp, 1, 1);
        code.addAload(0);
        code.addGetfield(classname, HANDLER, HANDLER_TYPE);
        code.addOpcode(176);
        minfo.setCodeAttribute(code.toCodeAttribute());
        cf.addMethod(minfo);
    }

    private int overrideMethods(ClassFile cf, ConstPool cp, String className, ArrayList forwarders) throws CannotCompileException {
        String prefix = makeUniqueName("_d", this.signatureMethods);
        int index = 0;
        for (Map.Entry e : this.signatureMethods) {
            String key = (String) e.getKey();
            Method meth = (Method) e.getValue();
            if ((ClassFile.MAJOR_VERSION < 49 || !isBridge(meth)) && testBit(this.signature, index)) {
                override(className, meth, prefix, index, keyToDesc(key, meth), cf, cp, forwarders);
            }
            index++;
        }
        return index;
    }

    private static boolean isBridge(Method m) {
        return m.isBridge();
    }

    private void override(String thisClassname, Method meth, String prefix, int index, String desc, ClassFile cf, ConstPool cp, ArrayList forwarders) throws CannotCompileException {
        Class declClass = meth.getDeclaringClass();
        String delegatorName = prefix + index + meth.getName();
        if (Modifier.isAbstract(meth.getModifiers())) {
            delegatorName = null;
        } else {
            MethodInfo delegator = makeDelegator(meth, desc, cp, declClass, delegatorName);
            delegator.setAccessFlags(delegator.getAccessFlags() & (-65));
            cf.addMethod(delegator);
        }
        MethodInfo forwarder = makeForwarder(thisClassname, meth, desc, cp, declClass, delegatorName, index, forwarders);
        cf.addMethod(forwarder);
    }

    private void makeConstructors(String thisClassName, ClassFile cf, ConstPool cp, String classname) throws CannotCompileException {
        Constructor[] cons = SecurityActions.getDeclaredConstructors(this.superClass);
        boolean doHandlerInit = !this.factoryUseCache;
        for (Constructor c : cons) {
            int mod = c.getModifiers();
            if (!Modifier.isFinal(mod) && !Modifier.isPrivate(mod) && isVisible(mod, this.basename, c)) {
                MethodInfo m = makeConstructor(thisClassName, c, cp, this.superClass, doHandlerInit);
                cf.addMethod(m);
            }
        }
    }

    private static String makeUniqueName(String name, List sortedMethods) {
        if (makeUniqueName0(name, sortedMethods.iterator())) {
            return name;
        }
        for (int i = 100; i < 999; i++) {
            String s = name + i;
            if (makeUniqueName0(s, sortedMethods.iterator())) {
                return s;
            }
        }
        throw new RuntimeException("cannot make a unique method name");
    }

    private static boolean makeUniqueName0(String name, Iterator it) {
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            String key = (String) e.getKey();
            if (key.startsWith(name)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isVisible(int mod, String from, Member meth) {
        if ((mod & 2) != 0) {
            return false;
        }
        if ((mod & 5) != 0) {
            return true;
        }
        String p = getPackageName(from);
        String q = getPackageName(meth.getDeclaringClass().getName());
        if (p == null) {
            return q == null;
        }
        return p.equals(q);
    }

    private static String getPackageName(String name) {
        int i = name.lastIndexOf(46);
        if (i < 0) {
            return null;
        }
        return name.substring(0, i);
    }

    private HashMap getMethods(Class superClass, Class[] interfaceTypes) {
        HashMap hash = new HashMap();
        HashSet set = new HashSet();
        for (Class cls : interfaceTypes) {
            getMethods(hash, cls, set);
        }
        getMethods(hash, superClass, set);
        return hash;
    }

    private void getMethods(HashMap hash, Class clazz, Set visitedClasses) {
        if (!visitedClasses.add(clazz)) {
            return;
        }
        Class[] ifs = clazz.getInterfaces();
        for (Class cls : ifs) {
            getMethods(hash, cls, visitedClasses);
        }
        Class parent = clazz.getSuperclass();
        if (parent != null) {
            getMethods(hash, parent, visitedClasses);
        }
        Method[] methods = SecurityActions.getDeclaredMethods(clazz);
        for (int i = 0; i < methods.length; i++) {
            if (!Modifier.isPrivate(methods[i].getModifiers())) {
                Method m = methods[i];
                String key = m.getName() + ':' + RuntimeSupport.makeDescriptor(m);
                if (key.startsWith(HANDLER_GETTER_KEY)) {
                    this.hasGetHandler = true;
                }
                Method oldMethod = (Method) hash.put(key, m);
                if (null != oldMethod && isBridge(m) && !Modifier.isPublic(oldMethod.getDeclaringClass().getModifiers()) && !Modifier.isAbstract(oldMethod.getModifiers()) && !isOverloaded(i, methods)) {
                    hash.put(key, oldMethod);
                }
                if (null != oldMethod && Modifier.isPublic(oldMethod.getModifiers()) && !Modifier.isPublic(m.getModifiers())) {
                    hash.put(key, oldMethod);
                }
            }
        }
    }

    private static boolean isOverloaded(int index, Method[] methods) {
        String name = methods[index].getName();
        for (int i = 0; i < methods.length; i++) {
            if (i != index && name.equals(methods[i].getName())) {
                return true;
            }
        }
        return false;
    }

    private static String keyToDesc(String key, Method m) {
        return key.substring(key.indexOf(58) + 1);
    }

    private static MethodInfo makeConstructor(String thisClassName, Constructor cons, ConstPool cp, Class superClass, boolean doHandlerInit) {
        String desc = RuntimeSupport.makeDescriptor(cons.getParameterTypes(), Void.TYPE);
        MethodInfo minfo = new MethodInfo(cp, "<init>", desc);
        minfo.setAccessFlags(1);
        setThrows(minfo, cp, cons.getExceptionTypes());
        Bytecode code = new Bytecode(cp, 0, 0);
        if (doHandlerInit) {
            code.addAload(0);
            code.addGetstatic(thisClassName, DEFAULT_INTERCEPTOR, HANDLER_TYPE);
            code.addPutfield(thisClassName, HANDLER, HANDLER_TYPE);
            code.addGetstatic(thisClassName, DEFAULT_INTERCEPTOR, HANDLER_TYPE);
            code.addOpcode(199);
            code.addIndex(10);
        }
        code.addAload(0);
        code.addGetstatic(NULL_INTERCEPTOR_HOLDER, DEFAULT_INTERCEPTOR, HANDLER_TYPE);
        code.addPutfield(thisClassName, HANDLER, HANDLER_TYPE);
        int pc = code.currentPc();
        code.addAload(0);
        int s = addLoadParameters(code, cons.getParameterTypes(), 1);
        code.addInvokespecial(superClass.getName(), "<init>", desc);
        code.addOpcode(177);
        code.setMaxLocals(s + 1);
        CodeAttribute ca = code.toCodeAttribute();
        minfo.setCodeAttribute(ca);
        StackMapTable.Writer writer = new StackMapTable.Writer(32);
        writer.sameFrame(pc);
        ca.setAttribute(writer.toStackMapTable(cp));
        return minfo;
    }

    private MethodInfo makeDelegator(Method meth, String desc, ConstPool cp, Class declClass, String delegatorName) {
        MethodInfo delegator = new MethodInfo(cp, delegatorName, desc);
        delegator.setAccessFlags(17 | (meth.getModifiers() & (-1319)));
        setThrows(delegator, cp, meth);
        Bytecode code = new Bytecode(cp, 0, 0);
        code.addAload(0);
        int s = addLoadParameters(code, meth.getParameterTypes(), 1);
        Class targetClass = invokespecialTarget(declClass);
        code.addInvokespecial(targetClass.isInterface(), cp.addClassInfo(targetClass.getName()), meth.getName(), desc);
        addReturn(code, meth.getReturnType());
        code.setMaxLocals(s + 1);
        delegator.setCodeAttribute(code.toCodeAttribute());
        return delegator;
    }

    private Class invokespecialTarget(Class declClass) {
        if (declClass.isInterface()) {
            for (Class i : this.interfaces) {
                if (declClass.isAssignableFrom(i)) {
                    return i;
                }
            }
        }
        return this.superClass;
    }

    private static MethodInfo makeForwarder(String thisClassName, Method meth, String desc, ConstPool cp, Class declClass, String delegatorName, int index, ArrayList forwarders) {
        MethodInfo forwarder = new MethodInfo(cp, meth.getName(), desc);
        forwarder.setAccessFlags(16 | (meth.getModifiers() & (-1313)));
        setThrows(forwarder, cp, meth);
        int args = Descriptor.paramSize(desc);
        Bytecode code = new Bytecode(cp, 0, args + 2);
        int origIndex = index * 2;
        int delIndex = (index * 2) + 1;
        int arrayVar = args + 1;
        code.addGetstatic(thisClassName, HOLDER, HOLDER_TYPE);
        code.addAstore(arrayVar);
        forwarders.add(new Find2MethodsArgs(meth.getName(), delegatorName, desc, origIndex));
        code.addAload(0);
        code.addGetfield(thisClassName, HANDLER, HANDLER_TYPE);
        code.addAload(0);
        code.addAload(arrayVar);
        code.addIconst(origIndex);
        code.addOpcode(50);
        code.addAload(arrayVar);
        code.addIconst(delIndex);
        code.addOpcode(50);
        makeParameterList(code, meth.getParameterTypes());
        code.addInvokeinterface(MethodHandler.class.getName(), "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", 5);
        Class retType = meth.getReturnType();
        addUnwrapper(code, retType);
        addReturn(code, retType);
        CodeAttribute ca = code.toCodeAttribute();
        forwarder.setCodeAttribute(ca);
        return forwarder;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/ProxyFactory$Find2MethodsArgs.class */
    static class Find2MethodsArgs {
        String methodName;
        String delegatorName;
        String descriptor;
        int origIndex;

        Find2MethodsArgs(String mname, String dname, String desc, int index) {
            this.methodName = mname;
            this.delegatorName = dname;
            this.descriptor = desc;
            this.origIndex = index;
        }
    }

    private static void setThrows(MethodInfo minfo, ConstPool cp, Method orig) {
        Class[] exceptions = orig.getExceptionTypes();
        setThrows(minfo, cp, exceptions);
    }

    private static void setThrows(MethodInfo minfo, ConstPool cp, Class[] exceptions) {
        if (exceptions.length == 0) {
            return;
        }
        String[] list = new String[exceptions.length];
        for (int i = 0; i < exceptions.length; i++) {
            list[i] = exceptions[i].getName();
        }
        ExceptionsAttribute ea = new ExceptionsAttribute(cp);
        ea.setExceptions(list);
        minfo.setExceptionsAttribute(ea);
    }

    private static int addLoadParameters(Bytecode code, Class[] params, int offset) {
        int stacksize = 0;
        for (Class cls : params) {
            stacksize += addLoad(code, stacksize + offset, cls);
        }
        return stacksize;
    }

    private static int addLoad(Bytecode code, int n, Class type) {
        if (type.isPrimitive()) {
            if (type == Long.TYPE) {
                code.addLload(n);
                return 2;
            }
            if (type == Float.TYPE) {
                code.addFload(n);
                return 1;
            }
            if (type == Double.TYPE) {
                code.addDload(n);
                return 2;
            }
            code.addIload(n);
            return 1;
        }
        code.addAload(n);
        return 1;
    }

    private static int addReturn(Bytecode code, Class type) {
        if (type.isPrimitive()) {
            if (type == Long.TYPE) {
                code.addOpcode(173);
                return 2;
            }
            if (type == Float.TYPE) {
                code.addOpcode(174);
                return 1;
            }
            if (type == Double.TYPE) {
                code.addOpcode(175);
                return 2;
            }
            if (type == Void.TYPE) {
                code.addOpcode(177);
                return 0;
            }
            code.addOpcode(172);
            return 1;
        }
        code.addOpcode(176);
        return 1;
    }

    private static void makeParameterList(Bytecode code, Class[] params) {
        int regno = 1;
        int n = params.length;
        code.addIconst(n);
        code.addAnewarray("java/lang/Object");
        for (int i = 0; i < n; i++) {
            code.addOpcode(89);
            code.addIconst(i);
            Class type = params[i];
            if (type.isPrimitive()) {
                regno = makeWrapper(code, type, regno);
            } else {
                code.addAload(regno);
                regno++;
            }
            code.addOpcode(83);
        }
    }

    private static int makeWrapper(Bytecode code, Class type, int regno) {
        int index = FactoryHelper.typeIndex(type);
        String wrapper = FactoryHelper.wrapperTypes[index];
        code.addNew(wrapper);
        code.addOpcode(89);
        addLoad(code, regno, type);
        code.addInvokespecial(wrapper, "<init>", FactoryHelper.wrapperDesc[index]);
        return regno + FactoryHelper.dataSize[index];
    }

    private static void addUnwrapper(Bytecode code, Class type) {
        if (type.isPrimitive()) {
            if (type == Void.TYPE) {
                code.addOpcode(87);
                return;
            }
            int index = FactoryHelper.typeIndex(type);
            String wrapper = FactoryHelper.wrapperTypes[index];
            code.addCheckcast(wrapper);
            code.addInvokevirtual(wrapper, FactoryHelper.unwarpMethods[index], FactoryHelper.unwrapDesc[index]);
            return;
        }
        code.addCheckcast(type.getName());
    }

    private static MethodInfo makeWriteReplace(ConstPool cp) {
        MethodInfo minfo = new MethodInfo(cp, "writeReplace", "()Ljava/lang/Object;");
        String[] list = {"java.io.ObjectStreamException"};
        ExceptionsAttribute ea = new ExceptionsAttribute(cp);
        ea.setExceptions(list);
        minfo.setExceptionsAttribute(ea);
        Bytecode code = new Bytecode(cp, 0, 1);
        code.addAload(0);
        code.addInvokestatic(NULL_INTERCEPTOR_HOLDER, "makeSerializedProxy", "(Ljava/lang/Object;)Ljavassist/util/proxy/SerializedProxy;");
        code.addOpcode(176);
        minfo.setCodeAttribute(code.toCodeAttribute());
        return minfo;
    }
}
